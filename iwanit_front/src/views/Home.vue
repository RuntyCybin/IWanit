<script setup>
import { onMounted, ref } from 'vue'
import NavBar from '../components/NavBar.vue'
import { userId, role, token, buyerId, sellerId, setEmail, setName, setPhoneNumber, setBuyerId, setSellerId } from '../auth'

const API = '/api'

const articles = ref([])

// Para un buyer, este endpoint devuelve el mismo listado completo de
// articulos pero con offersCount incluido (para el aviso de ofertas nuevas).
// Los sellers no tienen buyerId, asi que siguen viendo el listado simple.
async function fetchArticles() {
  const path = buyerId.value ? `/v1/articles/offers-count` : '/v1/articles'

  try {
    const res = await fetch(API + path, {
      headers: { Authorization: `Bearer ${token.value}` },
    })

    if (!res.ok) return

    const data = await res.json()
    articles.value = data.map((article) => ({
      id: article.id,
      title: article.title ?? article.name,
      content: article.content ?? article.description,
      price: article.price,
      buyer: article.buyer ?? article.buyerId,
      seller: article.seller ?? article.sellerId,
      offersCount: article.offersCount ?? 0,
      offers: article.offers ?? [],
    }))
  } catch (e) {
    // sin conexion a la API: se mantiene la lista vacia
  }
}

// Traemos los datos del buyer/seller asociados al userId logueado y los
// guardamos en localStorage, para que el resto de la app (navbar, perfil)
// los tenga disponibles sin pedirlos de nuevo.
async function fetchUserProfile() {
  if (!userId.value || !role.value) return

  const path =
    role.value === 'BUYER'
      ? `/v1/buyers/user/${userId.value}`
      : role.value === 'SELLER'
        ? `/v1/sellers/user/${userId.value}`
        : null

  if (!path) return

  try {
    const res = await fetch(API + path, {
      headers: { Authorization: `Bearer ${token.value}` },
    })

    if (!res.ok) return

    const data = await res.json()
    setEmail(data.email)
    setName(data.name)
    setPhoneNumber(data.phoneNumber)

    if (role.value === 'BUYER') {
      setBuyerId(data.id)
    } else if (role.value === 'SELLER') {
      setSellerId(data.id)
    }
  } catch (e) {
    // sin conexion a la API: se mantienen los datos ya guardados en localStorage
  }
}

// Tanto buyers como sellers ven aqui el listado completo de articulos.
// fetchArticles necesita el buyerId (si lo hay), asi que espera a que
// fetchUserProfile termine antes de decidir que endpoint llamar.
onMounted(async () => {
  await fetchUserProfile()
  await fetchArticles()
})

function formatPrice(price) {
  return price.toLocaleString('es-ES', { style: 'currency', currency: 'EUR' })
}

// Un buyer solo puede abrir sus propios articulos. Un seller puede abrir
// cualquiera excepto los que ya tienen una oferta suya.
function canOpen(article) {
  if (role.value === 'BUYER') {
    return Number(article.buyer) === Number(buyerId.value)
  }

  if (role.value === 'SELLER') {
    return !article.offers.some((offer) => Number(offer.sellerId) === Number(sellerId.value))
  }

  return true
}
</script>

<template>
  <div class="min-h-screen bg-slate-100">
    <NavBar />

    <main class="p-4">
      <div class="max-w-5xl mx-auto">
        <header class="mb-6 flex items-center justify-between gap-4">
          <div>
            <h1 class="text-2xl font-semibold text-slate-900">Articulos</h1>
            <p class="mt-1 text-sm text-slate-500">Listado de articulos deseables.</p>
          </div>

          <router-link
            v-if="role !== 'SELLER'"
            :to="{ name: 'create-article' }"
            class="shrink-0 rounded-lg bg-slate-900 px-3 py-2 text-sm font-medium text-white
                   transition hover:bg-slate-800"
          >
            Crear
          </router-link>
        </header>

        <p v-if="articles.length === 0" class="text-sm text-slate-500">
          No hay articulos para mostrar.
        </p>

        <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          <template v-for="article in articles" :key="article.id">
            <router-link
              v-if="canOpen(article)"
              :to="{ name: 'article', params: { id: article.id } }"
              class="relative bg-white rounded-xl shadow-sm border border-slate-200 p-4 flex flex-col
                     transition hover:border-slate-300"
            >
              <span
                v-if="article.offersCount >= 1"
                class="absolute -right-1.5 -top-1.5 h-3 w-3 rounded-full bg-red-500 ring-2 ring-white"
              />
              <h2 class="text-base font-medium text-slate-900">{{ article.title }}</h2>
              <p class="mt-2 text-sm text-slate-500 flex-1">{{ article.content }}</p>
              <p class="mt-3 text-lg font-semibold text-slate-900">{{ formatPrice(article.price) }}</p>
            </router-link>

            <div
              v-else
              class="relative bg-white rounded-xl shadow-sm border border-slate-200 p-4 flex flex-col
                     opacity-60 cursor-not-allowed"
            >
              <span
                v-if="article.offersCount >= 1"
                class="absolute -right-1.5 -top-1.5 h-3 w-3 rounded-full bg-red-500 ring-2 ring-white"
              />
              <h2 class="text-base font-medium text-slate-900">{{ article.title }}</h2>
              <p class="mt-2 text-sm text-slate-500 flex-1">{{ article.content }}</p>
              <p class="mt-3 text-lg font-semibold text-slate-900">{{ formatPrice(article.price) }}</p>
            </div>
          </template>
        </div>
      </div>
    </main>
  </div>
</template>
