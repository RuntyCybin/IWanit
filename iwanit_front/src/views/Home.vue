<script setup>
import { ref } from 'vue'

// Datos mockeados con la misma forma que ArticleResponseDto (title, content,
// price). Se reemplazara por una llamada real a GET /v1/articles cuando el
// endpoint exista.
const articles = ref([
  {
    id: 1,
    title: 'Bicicleta de montana Trek',
    content: 'Rodado 29, frenos a disco, poco uso, ideal para principiantes.',
    price: 350000,
  },
  {
    id: 2,
    title: 'Notebook Lenovo IdeaPad 3',
    content: 'Intel i5, 8GB RAM, 256GB SSD. Con cargador original.',
    price: 420000,
  },
  {
    id: 3,
    title: 'Guitarra electrica Squier',
    content: 'Serie Stratocaster, color sunburst, incluye funda blanda.',
    price: 180000,
  },
  {
    id: 4,
    title: 'Mesa de living de madera',
    content: 'Mesa ratona 100x50cm, madera de pino maciza, muy buen estado.',
    price: 90000,
  },
  {
    id: 5,
    title: 'Consola PlayStation 5',
    content: 'Version estandar con un control adicional y tres juegos.',
    price: 650000,
  },
  {
    id: 6,
    title: 'Cafetera Nespresso',
    content: 'Modelo Essenza Mini, funciona perfecto, incluye base para capsulas.',
    price: 60000,
  },
])

function formatPrice(price) {
  return price.toLocaleString('es-AR', { style: 'currency', currency: 'ARS', maximumFractionDigits: 0 })
}
</script>

<template>
  <main class="min-h-screen bg-slate-100 p-4">
    <div class="max-w-5xl mx-auto">
      <header class="mb-6">
        <h1 class="text-2xl font-semibold text-slate-900">Articulos</h1>
        <p class="mt-1 text-sm text-slate-500">Listado de articulos disponibles.</p>
      </header>

      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
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
</template>
