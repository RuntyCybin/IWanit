<script setup>
import NavBar from '../components/NavBar.vue'
</script>

<template>
  <div class="min-h-screen bg-slate-100">
    <NavBar />

    <main class="p-4">
      <div class="max-w-3xl mx-auto">
        <header class="mb-6">
          <h1 class="text-2xl font-semibold text-slate-900">Sobre nosotros</h1>
          <p class="mt-1 text-sm text-slate-500">Que es IWanit y como funciona.</p>
        </header>

        <div class="bg-white rounded-xl shadow-sm border border-slate-200 p-6 space-y-5">
          <section>
            <h2 class="text-base font-medium text-slate-900">Nuestra mision</h2>
            <p class="mt-2 text-sm text-slate-600 leading-relaxed">
              IWanit nace para que comprar y vender articulos nuevos o de segunda mano sea simple, rapido
              y confiable. Conectamos a compradores y vendedores en un mismo lugar, sin
              intermediarios innecesarios, para darle una segunda vida a las cosas que ya no usas.
            </p>
          </section>

          <section>
            <h2 class="text-base font-medium text-slate-900">Como funciona</h2>
            <p class="mt-2 text-sm text-slate-600 leading-relaxed">
              Los compradores publican los articulos que quieren adquirir con una descripcion y un precio, y los
              vendedores publican ofertas que solo los compradores pueden ver en su seccion Home.
              Todo el proceso queda centralizado en la plataforma, para que ambas partes tengan
              claridad sobre el estado de cada operacion.
            </p>
          </section>

          <section>
            <h2 class="text-base font-medium text-slate-900">En que estamos trabajando</h2>
            <p class="mt-2 text-sm text-slate-600 leading-relaxed">
              El proyecto esta en desarrollo activo. Estamos construyendo las funcionalidades
              principales de la plataforma: gestion de articulos, sistema de ofertas y perfiles
              de comprador y vendedor, con el foco puesto en una experiencia clara y directa.
            </p>
          </section>
        </div>
      </div>
    </main>
  </div>
</template>
