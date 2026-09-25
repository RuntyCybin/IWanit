<script setup>
import { logout, role, avatarUrl, email } from '../auth'

const links = [
  { name: 'home', label: 'Articulos' },
  { name: 'about', label: 'Sobre nosotros' },
  { name: 'contact', label: 'Contacto' },
]
</script>

<template>
  <nav class="bg-white border-b border-slate-200">
    <div class="max-w-5xl mx-auto px-4 py-3 flex items-center justify-between gap-4">
      <span class="text-lg font-semibold text-slate-900">IWanit</span>

      <div class="flex items-center gap-1">
        <router-link
          v-for="link in links"
          :key="link.name"
          :to="{ name: link.name }"
          class="rounded-lg px-3 py-2 text-sm font-medium transition"
          active-class="bg-slate-900 text-white"
          exact-active-class="bg-slate-900 text-white"
        >
          {{ link.label }}
        </router-link>

        <span
          v-if="role"
          class="ml-2 rounded-full bg-slate-100 px-3 py-1 text-xs font-medium text-slate-600"
        >
          {{ role }}
        </span>

        <span class="group relative ml-2 shrink-0">
          <span
            class="flex h-8 w-8 items-center justify-center overflow-hidden rounded-full
                   bg-slate-100 border border-slate-200"
          >
            <img
              v-if="avatarUrl"
              :src="avatarUrl"
              alt="Foto de perfil"
              class="h-full w-full object-cover"
            />
            <svg
              v-else
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 24 24"
              fill="currentColor"
              class="h-5 w-5 text-slate-400"
            >
              <path
                d="M12 12c2.7 0 4.9-2.2 4.9-4.9S14.7 2.2 12 2.2 7.1 4.4 7.1 7.1 9.3 12 12 12Zm0 2.4c-3.3 0-9.8 1.6-9.8 4.9v2.5h19.6v-2.5c0-3.3-6.5-4.9-9.8-4.9Z"
              />
            </svg>
          </span>

          <template v-if="!email">
            <span
              class="absolute -right-0.5 -top-0.5 h-2.5 w-2.5 rounded-full bg-red-500
                     ring-2 ring-white"
            />
            <span
              class="pointer-events-none absolute left-1/2 top-full z-10 mt-2 hidden
                     -translate-x-1/2 whitespace-nowrap rounded-md bg-slate-900 px-2 py-1
                     text-xs text-white group-hover:block"
            >
              Necesita completar el perfil
            </span>
          </template>
        </span>
      </div>

      <button
        type="button"
        class="shrink-0 rounded-lg bg-white px-3 py-2 text-sm font-medium text-slate-600
               border border-slate-200 transition hover:bg-slate-50"
        @click="logout"
      >
        Cerrar sesion
      </button>
    </div>
  </nav>
</template>
