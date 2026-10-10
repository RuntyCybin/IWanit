<script setup>
import { computed, onMounted, ref } from 'vue'
import NavBar from '../components/NavBar.vue'
import { role, token, buyerId, sellerId } from '../auth'

const props = defineProps({
  id: { type: String, required: true },
})

const API = '/api'

const title = ref('')
const content = ref('')
const price = ref('')

const loading = ref(false)
const saving = ref(false)
const saved = ref(false)
const error = ref('')

async function fetchArticle() {
  loading.value = true
  error.value = ''

  try {
    const res = await fetch(`${API}/v1/articles/${props.id}`, {
      headers: { Authorization: `Bearer ${token.value}` },
    })

    if (!res.ok) {
      error.value = `La API respondio ${res.status} ${res.statusText}`
      return
    }

    const data = await res.json()
    title.value = data.title
    content.value = data.content
    price.value = data.price
  } catch (e) {
    error.value = 'No se pudo contactar la API'
  } finally {
    loading.value = false
  }
}

const offers = ref([])
const offersLoading = ref(false)
const offersError = ref('')

// Una vez que el articulo tiene alguna oferta, el buyer ya no puede
// seguir editandolo.
const hasOffers = computed(() => offers.value.length > 0)

async function fetchOffers() {
  offersLoading.value = true
  offersError.value = ''

  try {
    const res = await fetch(`${API}/v1/offers/article/${props.id}`, {
      headers: { Authorization: `Bearer ${token.value}` },
    })

    if (!res.ok) {
      offersError.value = `La API respondio ${res.status} ${res.statusText}`
      return
    }

    offers.value = await res.json()
  } catch (e) {
    offersError.value = 'No se pudo contactar la API'
  } finally {
    offersLoading.value = false
  }
}

const deletingOfferId = ref(null)
const deleteOfferError = ref('')

async function deleteOffer(offer) {
  deletingOfferId.value = offer.id
  deleteOfferError.value = ''

  try {
    const res = await fetch(`${API}/v1/offers/${offer.id}`, {
      method: 'DELETE',
      headers: { Authorization: `Bearer ${token.value}` },
    })

    if (!res.ok) {
      deleteOfferError.value = `La API respondio ${res.status} ${res.statusText}`
      return
    }

    offers.value = offers.value.filter((o) => o.id !== offer.id)
  } catch (e) {
    deleteOfferError.value = 'No se pudo contactar la API'
  } finally {
    deletingOfferId.value = null
  }
}

onMounted(() => {
  fetchArticle()

  if (role.value === 'BUYER') {
    fetchOffers()
  }
})

function formatPrice(value) {
  return value.toLocaleString('es-ES', { style: 'currency', currency: 'EUR' })
}

async function submit() {
  if (!buyerId.value) {
    error.value = 'No se encontro el buyer asociado a tu cuenta.'
    return
  }

  saving.value = true
  error.value = ''
  saved.value = false

  const body = {
    title: title.value,
    content: content.value,
    price: Number(price.value),
    buyerId: Number(buyerId.value),
  }

  try {
    const res = await fetch(`${API}/v1/articles/${props.id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token.value}`,
      },
      body: JSON.stringify(body),
    })

    if (!res.ok) {
      error.value = `La API respondio ${res.status} ${res.statusText}`
      return
    }

    saved.value = true
  } catch (e) {
    error.value = 'No se pudo contactar la API'
  } finally {
    saving.value = false
  }
}

function clear() {
  title.value = ''
  content.value = ''
  price.value = ''
  saved.value = false
  error.value = ''
}

const showOfferModal = ref(false)
const offerPrice = ref('')
const offerMessage = ref('')
const offerSaving = ref(false)
const offerError = ref('')

function makeOffer() {
  showOfferModal.value = true
}

function closeOfferModal() {
  showOfferModal.value = false
  offerPrice.value = ''
  offerMessage.value = ''
  offerError.value = ''
}

async function submitOffer() {
  offerSaving.value = true
  offerError.value = ''

  const body = {
    name: `${title.value} ${sellerId.value}`,
    description: offerMessage.value,
    price: Number(offerPrice.value),
    articleId: Number(props.id),
    sellerId: Number(sellerId.value),
  }

  try {
    const res = await fetch(`${API}/v1/offers`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token.value}`,
      },
      body: JSON.stringify(body),
    })

    if (!res.ok) {
      offerError.value = `La API respondio ${res.status} ${res.statusText}`
      return
    }

    closeOfferModal()
  } catch (e) {
    offerError.value = 'No se pudo contactar la API'
  } finally {
    offerSaving.value = false
  }
}
</script>

<template>
  <div class="min-h-screen bg-slate-100">
    <NavBar />

    <main class="p-4">
      <div class="max-w-md mx-auto">
        <header class="mb-6">
          <h1 class="text-2xl font-semibold text-slate-900">Articulo</h1>
        </header>

        <form
          class="bg-white rounded-xl shadow-sm border border-slate-200 p-6 space-y-4"
          @submit.prevent="submit"
        >
          <div>
            <label class="block text-sm font-medium text-slate-700">Titulo</label>
            <input
              v-model="title"
              type="text"
              :disabled="role !== 'BUYER' || hasOffers"
              class="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm
                     focus:border-slate-900 focus:outline-none focus:ring-1 focus:ring-slate-900
                     disabled:bg-slate-100 disabled:text-slate-500"
            />
          </div>

          <div>
            <label class="block text-sm font-medium text-slate-700">Descripcion</label>
            <textarea
              v-model="content"
              rows="4"
              :disabled="role !== 'BUYER' || hasOffers"
              class="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm
                     focus:border-slate-900 focus:outline-none focus:ring-1 focus:ring-slate-900
                     disabled:bg-slate-100 disabled:text-slate-500"
            ></textarea>
          </div>

          <div>
            <label class="block text-sm font-medium text-slate-700">Precio</label>
            <input
              v-model="price"
              type="number"
              min="0"
              step="0.01"
              :disabled="role !== 'BUYER' || hasOffers"
              class="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm
                     focus:border-slate-900 focus:outline-none focus:ring-1 focus:ring-slate-900
                     disabled:bg-slate-100 disabled:text-slate-500"
            />
          </div>

          <div v-if="role === 'BUYER'" class="flex gap-2">
            <button
              type="submit"
              :disabled="saving || hasOffers"
              class="flex-1 rounded-lg bg-slate-900 px-3 py-2 text-sm font-medium text-white
                     transition hover:bg-slate-800 disabled:opacity-50"
            >
              {{ saving ? 'Guardando...' : 'Guardar' }}
            </button>
            <button
              type="button"
              :disabled="hasOffers"
              class="flex-1 rounded-lg bg-white px-3 py-2 text-sm font-medium text-slate-600
                     border border-slate-200 transition hover:bg-slate-50 disabled:opacity-50"
              @click="clear"
            >
              Limpiar
            </button>
          </div>

          <button
            v-else-if="role === 'SELLER'"
            type="button"
            class="w-full rounded-lg bg-slate-900 px-3 py-2 text-sm font-medium text-white
                   transition hover:bg-slate-800"
            @click="makeOffer"
          >
            Hacer oferta
          </button>

          <p v-if="role === 'BUYER' && hasOffers" class="rounded-lg bg-slate-100 px-3 py-2 text-sm text-slate-600">
            Este articulo ya tiene ofertas y no se puede modificar.
          </p>

          <p v-if="saved" class="rounded-lg bg-emerald-50 px-3 py-2 text-sm text-emerald-700">
            Articulo actualizado correctamente.
          </p>

          <p v-if="error" class="rounded-lg bg-red-50 px-3 py-2 text-sm text-red-700">
            {{ error }}
          </p>
        </form>

        <div v-if="role === 'BUYER'" class="mt-6">
          <h2 class="text-lg font-semibold text-slate-900">Ofertas recibidas</h2>

          <p v-if="offersError" class="mt-3 rounded-lg bg-red-50 px-3 py-2 text-sm text-red-700">
            {{ offersError }}
          </p>

          <p v-else-if="!offersLoading && offers.length === 0" class="mt-3 text-sm text-slate-500">
            Todavia no hay ofertas para este articulo.
          </p>

          <ul
            v-else
            class="mt-3 divide-y divide-slate-200 bg-white rounded-xl shadow-sm border border-slate-200"
          >
            <li
              v-for="offer in offers"
              :key="offer.id"
              class="p-4 flex items-center justify-between gap-4"
            >
              <div>
                <h3 class="text-base font-medium text-slate-900">{{ offer.name }}</h3>
                <p class="mt-1 text-sm text-slate-500">{{ offer.description }}</p>
              </div>

              <div class="flex shrink-0 items-center gap-3">
                <p class="text-base font-semibold text-slate-900">
                  {{ formatPrice(offer.price) }}
                </p>
                <button
                  type="button"
                  :disabled="deletingOfferId === offer.id"
                  class="rounded-lg bg-white px-3 py-2 text-sm font-medium text-red-600
                         border border-slate-200 transition hover:bg-red-50 disabled:opacity-50"
                  @click="deleteOffer(offer)"
                >
                  {{ deletingOfferId === offer.id ? 'Eliminando...' : 'Eliminar' }}
                </button>
              </div>
            </li>
          </ul>

          <p v-if="deleteOfferError" class="mt-3 rounded-lg bg-red-50 px-3 py-2 text-sm text-red-700">
            {{ deleteOfferError }}
          </p>
        </div>
      </div>
    </main>

    <div
      v-if="showOfferModal"
      class="fixed inset-0 z-50 flex items-center justify-center bg-slate-900/50 p-4"
      @click.self="closeOfferModal"
    >
      <div class="w-full max-w-md rounded-xl bg-white p-6 shadow-sm">
        <h2 class="text-lg font-semibold text-slate-900">{{ title }}</h2>
        <p class="mt-2 text-sm text-slate-500">{{ content }}</p>

        <form class="mt-5 space-y-4" @submit.prevent="submitOffer">
          <div>
            <label class="block text-sm font-medium text-slate-700">Precio sugerido</label>
            <input
              v-model="offerPrice"
              type="number"
              min="0"
              step="0.01"
              required
              class="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm
                     focus:border-slate-900 focus:outline-none focus:ring-1 focus:ring-slate-900"
            />
          </div>

          <div>
            <label class="block text-sm font-medium text-slate-700">Mensaje</label>
            <textarea
              v-model="offerMessage"
              rows="3"
              required
              class="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm
                     focus:border-slate-900 focus:outline-none focus:ring-1 focus:ring-slate-900"
            ></textarea>
          </div>

          <div class="flex gap-2">
            <button
              type="submit"
              :disabled="offerSaving"
              class="flex-1 rounded-lg bg-slate-900 px-3 py-2 text-sm font-medium text-white
                     transition hover:bg-slate-800 disabled:opacity-50"
            >
              {{ offerSaving ? 'Enviando...' : 'Enviar oferta' }}
            </button>
            <button
              type="button"
              class="flex-1 rounded-lg bg-white px-3 py-2 text-sm font-medium text-slate-600
                     border border-slate-200 transition hover:bg-slate-50"
              @click="closeOfferModal"
            >
              Cancelar
            </button>
          </div>

          <p v-if="offerError" class="rounded-lg bg-red-50 px-3 py-2 text-sm text-red-700">
            {{ offerError }}
          </p>
        </form>
      </div>
    </div>
  </div>
</template>
