<script setup>
import { onMounted, ref } from 'vue'
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

onMounted(fetchArticle)

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
              :disabled="role !== 'BUYER'"
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
              :disabled="role !== 'BUYER'"
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
              :disabled="role !== 'BUYER'"
              class="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm
                     focus:border-slate-900 focus:outline-none focus:ring-1 focus:ring-slate-900
                     disabled:bg-slate-100 disabled:text-slate-500"
            />
          </div>

          <div v-if="role === 'BUYER'" class="flex gap-2">
            <button
              type="submit"
              :disabled="saving"
              class="flex-1 rounded-lg bg-slate-900 px-3 py-2 text-sm font-medium text-white
                     transition hover:bg-slate-800 disabled:opacity-50"
            >
              {{ saving ? 'Guardando...' : 'Guardar' }}
            </button>
            <button
              type="button"
              class="flex-1 rounded-lg bg-white px-3 py-2 text-sm font-medium text-slate-600
                     border border-slate-200 transition hover:bg-slate-50"
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

          <p v-if="saved" class="rounded-lg bg-emerald-50 px-3 py-2 text-sm text-emerald-700">
            Articulo actualizado correctamente.
          </p>

          <p v-if="error" class="rounded-lg bg-red-50 px-3 py-2 text-sm text-red-700">
            {{ error }}
          </p>
        </form>
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
