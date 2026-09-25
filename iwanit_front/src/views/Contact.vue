<script setup>
import { ref } from 'vue'
import NavBar from '../components/NavBar.vue'

const name = ref('')
const email = ref('')
const message = ref('')
const sent = ref(false)

// No hay endpoint de contacto todavia: el envio queda mockeado del lado del
// cliente hasta que exista una API real.
function submit() {
  sent.value = true
  name.value = ''
  email.value = ''
  message.value = ''
}
</script>

<template>
  <div class="min-h-screen bg-slate-100">
    <NavBar />

    <main class="p-4">
      <div class="max-w-3xl mx-auto">
        <header class="mb-6">
          <h1 class="text-2xl font-semibold text-slate-900">Contacto</h1>
          <p class="mt-1 text-sm text-slate-500">Escribinos o encontranos en estos canales.</p>
        </header>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <form
            class="bg-white rounded-xl shadow-sm border border-slate-200 p-6 space-y-4"
            @submit.prevent="submit"
          >
            <div>
              <label class="block text-sm font-medium text-slate-700">Nombre</label>
              <input
                v-model="name"
                type="text"
                required
                class="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm
                       focus:border-slate-900 focus:outline-none focus:ring-1 focus:ring-slate-900"
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-slate-700">Email</label>
              <input
                v-model="email"
                type="email"
                required
                class="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm
                       focus:border-slate-900 focus:outline-none focus:ring-1 focus:ring-slate-900"
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-slate-700">Mensaje</label>
              <textarea
                v-model="message"
                rows="4"
                required
                class="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm
                       focus:border-slate-900 focus:outline-none focus:ring-1 focus:ring-slate-900"
              ></textarea>
            </div>

            <button
              type="submit"
              class="w-full rounded-lg bg-slate-900 px-3 py-2 text-sm font-medium text-white
                     transition hover:bg-slate-800"
            >
              Enviar
            </button>

            <p v-if="sent" class="rounded-lg bg-emerald-50 px-3 py-2 text-sm text-emerald-700">
              Gracias por escribirnos, te vamos a responder a la brevedad.
            </p>
          </form>

          <div class="bg-white rounded-xl shadow-sm border border-slate-200 p-6">
            <h2 class="text-base font-medium text-slate-900">cybindev</h2>
            <p class="mt-2 text-sm text-slate-600 leading-relaxed">
              IWanit es un proyecto desarrollado por cybindev, una organizacion dedicada al
              desarrollo de software a medida.
            </p>

            <dl class="mt-4 space-y-3 text-sm">
              <div>
                <dt class="font-medium text-slate-700">Email</dt>
                <dd class="text-slate-500">contacto@cybindev.com</dd>
              </div>
              <div>
                <dt class="font-medium text-slate-700">Sitio web</dt>
                <dd class="text-slate-500">cybindev.com</dd>
              </div>
              <div>
                <dt class="font-medium text-slate-700">Ubicacion</dt>
                <dd class="text-slate-500">Argentina (remoto)</dd>
              </div>
            </dl>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>
