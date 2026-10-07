<script setup>
import { onMounted, ref } from 'vue'
import NavBar from '../components/NavBar.vue'
import { buyerId, token } from '../auth'

const API = '/api'

const articles = ref([])
const loading = ref(false)
const error = ref('')

async function fetchArticles() {
  if (!buyerId.value) return

  loading.value = true
  error.value = ''

  try {
    const res = await fetch(`${API}/v1/articles/buyer/${buyerId.value}`, {
      headers: { Authorization: `Bearer ${token.value}` },
    })

    if (!res.ok) {
      error.value = `La API respondio ${res.status} ${res.statusText}`
      return
    }

    articles.value = await res.json()
  } catch (e) {
    error.value = 'No se pudo contactar la API'
  } finally {
    loading.value = false
  }
}

onMounted(fetchArticles)

function formatPrice(price) {
  return price.toLocaleString('es-ES', { style: 'currency', currency: 'EUR' })
}
</script>

<template>
  <div class="min-h-screen bg-slate-100">
    <NavBar />

    <main class="p-4">
      <div class="max-w-3xl mx-auto">
        <header class="mb-6">
          <h1 class="text-2xl font-semibold text-slate-900">Mis articulos</h1>
          <p class="mt-1 text-sm text-slate-500">Listado de articulos que creaste.</p>
        </header>

        <p v-if="error" class="mb-4 rounded-lg bg-red-50 px-3 py-2 text-sm text-red-700">
          {{ error }}
        </p>

        <p v-else-if="!loading && articles.length === 0" class="text-sm text-slate-500">
          No hay articulos para mostrar.
        </p>

        <ul v-else class="divide-y divide-slate-200 bg-white rounded-xl shadow-sm border border-slate-200">
          <li v-for="article in articles" :key="article.id">
            <router-link
              :to="{ name: 'article', params: { id: article.id } }"
              class="flex items-center justify-between gap-4 p-4 transition hover:bg-slate-50"
            >
              <div>
                <h2 class="text-base font-medium text-slate-900">{{ article.title }}</h2>
                <p class="mt-1 text-sm text-slate-500">{{ article.content }}</p>
              </div>
              <p class="shrink-0 text-base font-semibold text-slate-900">
                {{ formatPrice(article.price) }}
              </p>
            </router-link>
          </li>
        </ul>
      </div>
    </main>
  </div>
</template>
