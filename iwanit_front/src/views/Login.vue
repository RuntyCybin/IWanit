<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

// All API calls go through the /api prefix. In the container nginx proxies it
// to the backend over the Docker network, so the browser only ever talks to a
// single origin and there is no CORS involved.
const API = '/api'

const router = useRouter()

const mode = ref('login')
const username = ref('')
const password = ref('')
const role = ref('BUYER')

const error = ref('')
const loading = ref(false)

async function submit() {
  loading.value = true
  error.value = ''

  const path = mode.value === 'login' ? '/v1/auth/login' : '/v1/auth/register'
  const body =
    mode.value === 'login'
      ? { username: username.value, password: password.value }
      : { username: username.value, password: password.value, role: role.value }

  try {
    const res = await fetch(API + path, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body),
    })

    if (!res.ok) {
      error.value = `La API respondio ${res.status} ${res.statusText}`
      return
    }

    const data = await res.json()

    if (mode.value === 'login') {
      localStorage.setItem('token', data.token)
      router.push({ name: 'home' })
    } else {
      mode.value = 'login'
    }
  } catch (e) {
    error.value = 'No se pudo contactar la API'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <main class="min-h-screen bg-slate-100 flex items-center justify-center p-4">
    <div class="w-full max-w-md bg-white rounded-xl shadow-sm border border-slate-200 p-6">
      <h1 class="text-2xl font-semibold text-slate-900">IWanit</h1>
      <p class="mt-1 text-sm text-slate-500">
        Vista de ejemplo para verificar la conexion con la API.
      </p>

      <div class="mt-5 flex gap-2">
        <button
          type="button"
          class="flex-1 rounded-lg px-3 py-2 text-sm font-medium transition"
          :class="mode === 'login'
            ? 'bg-slate-900 text-white'
            : 'bg-slate-100 text-slate-600 hover:bg-slate-200'"
          @click="mode = 'login'"
        >
          Entrar
        </button>
        <button
          type="button"
          class="flex-1 rounded-lg px-3 py-2 text-sm font-medium transition"
          :class="mode === 'register'
            ? 'bg-slate-900 text-white'
            : 'bg-slate-100 text-slate-600 hover:bg-slate-200'"
          @click="mode = 'register'"
        >
          Registrarse
        </button>
      </div>

      <form class="mt-5 space-y-4" @submit.prevent="submit">
        <div>
          <label class="block text-sm font-medium text-slate-700">Usuario</label>
          <input
            v-model="username"
            type="text"
            required
            autocomplete="username"
            class="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm
                   focus:border-slate-900 focus:outline-none focus:ring-1 focus:ring-slate-900"
          />
        </div>

        <div>
          <label class="block text-sm font-medium text-slate-700">Contrasena</label>
          <input
            v-model="password"
            type="password"
            required
            autocomplete="current-password"
            class="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm
                   focus:border-slate-900 focus:outline-none focus:ring-1 focus:ring-slate-900"
          />
        </div>

        <div v-if="mode === 'register'">
          <label class="block text-sm font-medium text-slate-700">Rol</label>
          <select
            v-model="role"
            class="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm
                   focus:border-slate-900 focus:outline-none focus:ring-1 focus:ring-slate-900"
          >
            <option value="BUYER">BUYER</option>
            <option value="SELLER">SELLER</option>
            <option value="ADMIN">ADMIN</option>
          </select>
        </div>

        <button
          type="submit"
          :disabled="loading"
          class="w-full rounded-lg bg-slate-900 px-3 py-2 text-sm font-medium text-white
                 transition hover:bg-slate-800 disabled:opacity-50"
        >
          {{ loading ? 'Enviando...' : mode === 'login' ? 'Entrar' : 'Crear cuenta' }}
        </button>
      </form>

      <p v-if="error" class="mt-4 rounded-lg bg-red-50 px-3 py-2 text-sm text-red-700">
        {{ error }}
      </p>
    </div>
  </main>
</template>
