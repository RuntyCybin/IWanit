<script setup>
import { logout, role } from '../auth'

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
