<script setup>
import { onMounted, ref } from 'vue'
import NavBar from '../components/NavBar.vue'
import { userId, role, token, setEmail, setName, setPhoneNumber, setBuyerId } from '../auth'

const API = '/api'

const articles = ref([])

async function fetchArticles(id) {
  try {
    const res = await fetch(`${API}/v1/articles/buyer/${id}`, {
      headers: { Authorization: `Bearer ${token.value}` },
    })

    if (!res.ok) return

    articles.value = await res.json()
  } catch (e) {
    // sin conexion a la API: se mantiene la lista vacia
  }
}

// Al entrar a Home traemos los datos del buyer/seller asociados al userId
// logueado y los guardamos en localStorage, para que el resto de la app
// (navbar, perfil) los tenga disponibles sin pedirlos de nuevo. Si es un
// buyer, con su buyerId pedimos ademas sus articulos.
onMounted(async () => {
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
      await fetchArticles(data.id)
    }
  } catch (e) {
    // sin conexion a la API: se mantienen los datos ya guardados en localStorage
  }
})

function formatPrice(price) {
  return price.toLocaleString('es-AR', { style: 'currency', currency: 'ARS', maximumFractionDigits: 0 })
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
          <article
            v-for="article in articles"
            :key="article.id"
            class="bg-white rounded-xl shadow-sm border border-slate-200 p-4 flex flex-col"
          >
            <h2 class="text-base font-medium text-slate-900">{{ article.title }}</h2>
            <p class="mt-2 text-sm text-slate-500 flex-1">{{ article.content }}</p>
            <p class="mt-3 text-lg font-semibold text-slate-900">{{ formatPrice(article.price) }}</p>
          </article>
        </div>
      </div>
    </main>
  </div>
</template>
