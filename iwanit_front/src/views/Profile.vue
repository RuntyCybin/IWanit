<script setup>
import { ref } from 'vue'
import NavBar from '../components/NavBar.vue'
import {
  name as savedName,
  email as savedEmail,
  phoneNumber as savedPhoneNumber,
  setName,
  setEmail,
  setPhoneNumber,
} from '../auth'

const name = ref(savedName.value ?? '')
const email = ref(savedEmail.value ?? '')
const phoneNumber = ref(savedPhoneNumber.value ?? '')
const saved = ref(false)

function submit() {
  setName(name.value)
  setEmail(email.value)
  setPhoneNumber(phoneNumber.value)
  saved.value = true
}

function clear() {
  name.value = ''
  email.value = ''
  phoneNumber.value = ''
  saved.value = false
}
</script>

<template>
  <div class="min-h-screen bg-slate-100">
    <NavBar />

    <main class="p-4">
      <div class="max-w-md mx-auto">
        <header class="mb-6">
          <h1 class="text-2xl font-semibold text-slate-900">Mi perfil</h1>
          <p class="mt-1 text-sm text-slate-500">Completa tus datos de contacto.</p>
        </header>

        <form
          class="bg-white rounded-xl shadow-sm border border-slate-200 p-6 space-y-4"
          @submit.prevent="submit"
        >
          <div>
            <label class="block text-sm font-medium text-slate-700">Nombre</label>
            <input
              v-model="name"
              type="text"
              class="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm
                     focus:border-slate-900 focus:outline-none focus:ring-1 focus:ring-slate-900"
            />
          </div>

          <div>
            <label class="block text-sm font-medium text-slate-700">Email</label>
            <input
              v-model="email"
              type="email"
              class="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm
                     focus:border-slate-900 focus:outline-none focus:ring-1 focus:ring-slate-900"
            />
          </div>

          <div>
            <label class="block text-sm font-medium text-slate-700">Numero de telefono</label>
            <input
              v-model="phoneNumber"
              type="tel"
              class="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm
                     focus:border-slate-900 focus:outline-none focus:ring-1 focus:ring-slate-900"
            />
          </div>

          <div class="flex gap-2">
            <button
              type="submit"
              class="flex-1 rounded-lg bg-slate-900 px-3 py-2 text-sm font-medium text-white
                     transition hover:bg-slate-800"
            >
              Guardar
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

          <p v-if="saved" class="rounded-lg bg-emerald-50 px-3 py-2 text-sm text-emerald-700">
            Perfil guardado correctamente.
          </p>
        </form>
      </div>
    </main>
  </div>
</template>
