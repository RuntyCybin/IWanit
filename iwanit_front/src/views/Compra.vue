<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import NavBar from '../components/NavBar.vue'

const props = defineProps({
  offerId: { type: String, required: true },
})

const router = useRouter()

const paymentMethods = [
  { value: 'CARD', label: 'Tarjeta de credito/debito' },
  { value: 'TRANSFER', label: 'Transferencia bancaria' },
  { value: 'PAYPAL', label: 'PayPal' },
  { value: 'CASH', label: 'Efectivo en la entrega' },
]

const shippingMethods = [
  { value: 'HOME_DELIVERY', label: 'Envio a domicilio' },
  { value: 'PICKUP_POINT', label: 'Recogida en punto de encuentro' },
  { value: 'EXPRESS', label: 'Mensajeria urgente' },
]

const paymentMethod = ref('')
const shippingMethod = ref('')

function confirm() {
  // Preliminar: todavia no hay endpoint para confirmar la compra.
}

function cancel() {
  router.back()
}
</script>

<template>
  <div class="min-h-screen bg-slate-100">
    <NavBar />

    <main class="p-4">
      <div class="max-w-md mx-auto">
        <header class="mb-6">
          <h1 class="text-2xl font-semibold text-slate-900">Compra</h1>
          <p class="mt-1 text-sm text-slate-500">
            Oferta #{{ offerId }} - elegi como quieres pagar y recibir tu articulo.
          </p>
        </header>

        <form
          class="bg-white rounded-xl shadow-sm border border-slate-200 p-6 space-y-5"
          @submit.prevent="confirm"
        >
          <div>
            <h2 class="text-sm font-medium text-slate-700">Metodo de pago</h2>
            <div class="mt-2 space-y-2">
              <label
                v-for="method in paymentMethods"
                :key="method.value"
                class="flex items-center gap-2 rounded-lg border border-slate-200 px-3 py-2 text-sm
                       text-slate-700 cursor-pointer transition hover:border-slate-300"
              >
                <input
                  v-model="paymentMethod"
                  type="radio"
                  name="paymentMethod"
                  :value="method.value"
                  required
                  class="h-4 w-4 border-slate-300 text-slate-900 focus:ring-slate-900"
                />
                {{ method.label }}
              </label>
            </div>
          </div>

          <div>
            <h2 class="text-sm font-medium text-slate-700">Metodo de transporte</h2>
            <div class="mt-2 space-y-2">
              <label
                v-for="method in shippingMethods"
                :key="method.value"
                class="flex items-center gap-2 rounded-lg border border-slate-200 px-3 py-2 text-sm
                       text-slate-700 cursor-pointer transition hover:border-slate-300"
              >
                <input
                  v-model="shippingMethod"
                  type="radio"
                  name="shippingMethod"
                  :value="method.value"
                  required
                  class="h-4 w-4 border-slate-300 text-slate-900 focus:ring-slate-900"
                />
                {{ method.label }}
              </label>
            </div>
          </div>

          <div class="flex gap-2">
            <button
              type="submit"
              class="flex-1 rounded-lg bg-slate-900 px-3 py-2 text-sm font-medium text-white
                     transition hover:bg-slate-800"
            >
              Confirmar compra
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
        </form>
      </div>
    </main>
  </div>
</template>
