<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import NavBar from '../components/NavBar.vue'
import { buyerId, token } from '../auth'

const API = '/api'

const router = useRouter()

const title = ref('')
const content = ref('')
const price = ref('')

const error = ref('')
const loading = ref(false)

async function submit() {
  if (!buyerId.value) {
    error.value = 'No se encontro el buyer asociado a tu cuenta.'
    return
  }

  loading.value = true
  error.value = ''

  const body = {
    title: title.value,
    content: content.value,
    price: Number(price.value),
    buyerId: Number(buyerId.value),
  }

  try {
    const res = await fetch(API + '/v1/articles', {
      method: 'POST',
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

    router.push({ name: 'home' })
  } catch (e) {
    error.value = 'No se pudo contactar la API'
  } finally {
    loading.value = false
  }
}

function cancel() {
  router.push({ name: 'home' })
}
</script>

<template>
  <div class="min-h-screen bg-slate-100">
    <NavBar />

    <main class="p-4">
      <div class="max-w-md mx-auto">
        <header class="mb-6">
          <h1 class="text-2xl font-semibold text-slate-900">Crear articulo</h1>
          <p class="mt-1 text-sm text-slate-500">Cargalo para agregarlo a tu lista de deseados.</p>
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
              required
              class="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm
                     focus:border-slate-900 focus:outline-none focus:ring-1 focus:ring-slate-900"
            />
          </div>

          <div>
            <label class="block text-sm font-medium text-slate-700">Descripcion</label>
            <textarea
              v-model="content"
              rows="4"
              required
              class="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm
                     focus:border-slate-900 focus:outline-none focus:ring-1 focus:ring-slate-900"
            ></textarea>
          </div>

          <div>
            <label class="block text-sm font-medium text-slate-700">Precio</label>
            <input
              v-model="price"
              type="number"
              min="0"
              step="0.01"
              required
              class="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm
                     focus:border-slate-900 focus:outline-none focus:ring-1 focus:ring-slate-900"
            />
          </div>

          <div class="flex gap-2">
            <button
              type="submit"
              :disabled="loading"
              class="flex-1 rounded-lg bg-slate-900 px-3 py-2 text-sm font-medium text-white
                     transition hover:bg-slate-800 disabled:opacity-50"
            >
              {{ loading ? 'Creando...' : 'Crear' }}
            </button>
            <button
              type="button"
              class="flex-1 rounded-lg bg-white px-3 py-2 text-sm font-medium text-slate-600
                     border border-slate-200 transition hover:bg-slate-50"
              @click="cancel"
            >
              Cancelar
            </button>
          </div>

          <p v-if="error" class="rounded-lg bg-red-50 px-3 py-2 text-sm text-red-700">
            {{ error }}
          </p>
        </form>
      </div>
    </main>
  </div>
</template>
