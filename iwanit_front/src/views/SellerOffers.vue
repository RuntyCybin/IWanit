<script setup>
import { onMounted, ref } from 'vue'
import NavBar from '../components/NavBar.vue'
import { sellerId, token } from '../auth'

const API = '/api'

const offers = ref([])
const loading = ref(false)
const error = ref('')

async function fetchOffers() {
  if (!sellerId.value) return

  loading.value = true
  error.value = ''

  try {
    const res = await fetch(`${API}/v1/offers/seller/${sellerId.value}`, {
      headers: { Authorization: `Bearer ${token.value}` },
    })

    if (!res.ok) {
      error.value = `La API respondio ${res.status} ${res.statusText}`
      return
    }

    offers.value = await res.json()
  } catch (e) {
    error.value = 'No se pudo contactar la API'
  } finally {
    loading.value = false
  }
}

onMounted(fetchOffers)

function formatPrice(price) {
  return price.toLocaleString('es-ES', { style: 'currency', currency: 'EUR' })
}

const editingId = ref(null)
const editPrice = ref('')
const editDescription = ref('')
const saving = ref(false)
const saveError = ref('')

function startEdit(offer) {
  editingId.value = offer.id
  editPrice.value = offer.price
  editDescription.value = offer.description
  saveError.value = ''
}

function cancelEdit() {
  editingId.value = null
  saveError.value = ''
}

async function saveEdit(offer) {
  saving.value = true
  saveError.value = ''

  const body = {
    name: offer.name,
    description: editDescription.value,
    price: Number(editPrice.value),
    articleId: offer.articleId,
    sellerId: Number(sellerId.value),
  }

  try {
    const res = await fetch(`${API}/v1/offers/${offer.id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token.value}`,
      },
      body: JSON.stringify(body),
    })

    if (!res.ok) {
      saveError.value = `La API respondio ${res.status} ${res.statusText}`
      return
    }

    offer.description = editDescription.value
    offer.price = Number(editPrice.value)
    editingId.value = null
  } catch (e) {
    saveError.value = 'No se pudo contactar la API'
  } finally {
    saving.value = false
  }
}

const deletingId = ref(null)
const deleteError = ref('')

async function deleteOffer(offer) {
  deletingId.value = offer.id
  deleteError.value = ''

  try {
    const res = await fetch(`${API}/v1/offers/${offer.id}`, {
      method: 'DELETE',
      headers: { Authorization: `Bearer ${token.value}` },
    })

    if (!res.ok) {
      deleteError.value = `La API respondio ${res.status} ${res.statusText}`
      return
    }

    offers.value = offers.value.filter((o) => o.id !== offer.id)
  } catch (e) {
    deleteError.value = 'No se pudo contactar la API'
  } finally {
    deletingId.value = null
  }
}
</script>

<template>
  <div class="min-h-screen bg-slate-100">
    <NavBar />

    <main class="p-4">
      <div class="max-w-3xl mx-auto">
        <header class="mb-6">
          <h1 class="text-2xl font-semibold text-slate-900">Mis ofertas</h1>
          <p class="mt-1 text-sm text-slate-500">Listado de ofertas que enviaste.</p>
        </header>

        <p v-if="error" class="mb-4 rounded-lg bg-red-50 px-3 py-2 text-sm text-red-700">
          {{ error }}
        </p>

        <p v-else-if="!loading && offers.length === 0" class="text-sm text-slate-500">
          No hay ofertas para mostrar.
        </p>

        <ul v-else class="divide-y divide-slate-200 bg-white rounded-xl shadow-sm border border-slate-200">
          <li v-for="offer in offers" :key="offer.id" class="p-4">
            <div v-if="editingId !== offer.id" class="flex items-center justify-between gap-4">
              <div>
                <h2 class="text-base font-medium text-slate-900">{{ offer.name }}</h2>
                <p class="mt-1 text-sm text-slate-500">{{ offer.description }}</p>
              </div>

              <div class="flex shrink-0 items-center gap-3">
                <p class="text-base font-semibold text-slate-900">
                  {{ formatPrice(offer.price) }}
                </p>
                <button
                  type="button"
                  class="rounded-lg bg-white px-3 py-2 text-sm font-medium text-slate-600
                         border border-slate-200 transition hover:bg-slate-50"
                  @click="startEdit(offer)"
                >
                  Editar
                </button>
                <button
                  type="button"
                  :disabled="deletingId === offer.id"
                  class="rounded-lg bg-white px-3 py-2 text-sm font-medium text-red-600
                         border border-slate-200 transition hover:bg-red-50 disabled:opacity-50"
                  @click="deleteOffer(offer)"
                >
                  {{ deletingId === offer.id ? 'Eliminando...' : 'Eliminar' }}
                </button>
              </div>
            </div>

            <form v-else class="space-y-3" @submit.prevent="saveEdit(offer)">
              <h2 class="text-base font-medium text-slate-900">{{ offer.name }}</h2>

              <div>
                <label class="block text-sm font-medium text-slate-700">Precio sugerido</label>
                <input
                  v-model="editPrice"
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
                  v-model="editDescription"
                  rows="3"
                  required
                  class="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm
                         focus:border-slate-900 focus:outline-none focus:ring-1 focus:ring-slate-900"
                ></textarea>
              </div>

              <div class="flex gap-2">
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
                  @click="cancelEdit"
                >
                  Cancelar
                </button>
              </div>

              <p v-if="saveError" class="rounded-lg bg-red-50 px-3 py-2 text-sm text-red-700">
                {{ saveError }}
              </p>
            </form>
          </li>
        </ul>

        <p v-if="deleteError" class="mt-4 rounded-lg bg-red-50 px-3 py-2 text-sm text-red-700">
          {{ deleteError }}
        </p>
      </div>
    </main>
  </div>
</template>
