<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import NavBar from '../components/NavBar.vue'
import { buyerId, token } from '../auth'

const API = '/api'

const router = useRouter()

const acceptedOffers = ref([])
const loading = ref(false)
const error = ref('')

async function fetchAcceptedOffers() {
  if (!buyerId.value) return

  loading.value = true
  error.value = ''

  try {
    const res = await fetch(`${API}/v1/accepted-offers/buyer/${buyerId.value}`, {
      headers: { Authorization: `Bearer ${token.value}` },
    })

    if (!res.ok) {
      const message = await res.text()
      error.value = `La API respondio ${res.status} ${message}`
      return
    }

    acceptedOffers.value = await res.json()
  } catch (e) {
    error.value = 'No se pudo contactar la API'
  } finally {
    loading.value = false
  }
}

onMounted(fetchAcceptedOffers)

function formatPrice(price) {
  return price.toLocaleString('es-ES', { style: 'currency', currency: 'EUR' })
}

function formatDate(value) {
  if (!value) return '-'
  return new Date(value).toLocaleString('es-ES')
}

function buy(acceptedOffer) {
  router.push({
    name: 'compra',
    params: { offerId: acceptedOffer.offerId },
    query: { articleId: acceptedOffer.articleId },
  })
}
</script>

<template>
  <div class="min-h-screen bg-slate-100">
    <NavBar />

    <main class="p-4">
      <div class="max-w-3xl mx-auto">
        <header class="mb-6">
          <h1 class="text-2xl font-semibold text-slate-900">Ofertas aceptadas</h1>
          <p class="mt-1 text-sm text-slate-500">
            Ofertas que aceptaste y estan pendientes de compra.
          </p>
        </header>

        <p v-if="error" class="mb-4 rounded-lg bg-red-50 px-3 py-2 text-sm text-red-700">
          {{ error }}
        </p>

        <p v-else-if="!loading && acceptedOffers.length === 0" class="text-sm text-slate-500">
          No tienes ofertas aceptadas pendientes de compra.
        </p>

        <div v-else class="overflow-hidden rounded-xl border border-slate-200 bg-white shadow-sm">
          <table class="min-w-full divide-y divide-slate-200 text-sm">
            <thead class="bg-slate-50">
              <tr>
                <th class="px-4 py-2 text-left font-medium text-slate-500">Articulo</th>
                <th class="px-4 py-2 text-left font-medium text-slate-500">Precio</th>
                <th class="px-4 py-2 text-left font-medium text-slate-500">Aceptada el</th>
                <th class="px-4 py-2"></th>
              </tr>
            </thead>
            <tbody class="divide-y divide-slate-200">
              <tr v-for="acceptedOffer in acceptedOffers" :key="acceptedOffer.id">
                <td class="px-4 py-3 text-slate-900">{{ acceptedOffer.articleName }}</td>
                <td class="px-4 py-3 font-semibold text-slate-900">
                  {{ formatPrice(acceptedOffer.price) }}
                </td>
                <td class="px-4 py-3 text-slate-500">{{ formatDate(acceptedOffer.acceptedAt) }}</td>
                <td class="px-4 py-3 text-right">
                  <button
                    type="button"
                    class="rounded-lg bg-slate-900 px-3 py-2 text-sm font-medium text-white
                           transition hover:bg-slate-800"
                    @click="buy(acceptedOffer)"
                  >
                    Comprar
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </main>
  </div>
</template>
