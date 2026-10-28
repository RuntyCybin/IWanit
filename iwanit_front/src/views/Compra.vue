<script setup>
import { nextTick, onMounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import NavBar from '../components/NavBar.vue'
import { role, token, buyerId, sellerId } from '../auth'

const props = defineProps({
  offerId: { type: String, required: true },
})

const router = useRouter()

const API = '/api'

const buyerEmail = ref('')
const buyerPhoneNumber = ref('')
const buyerAddress = ref('')
const buyerCp = ref('')
const buyerCountry = ref('')
const buyerLoading = ref(false)
const buyerError = ref('')

async function fetchBuyer() {
  if (!buyerId.value) return

  buyerLoading.value = true
  buyerError.value = ''

  try {
    const res = await fetch(`${API}/v1/buyers/${buyerId.value}`, {
      headers: { Authorization: `Bearer ${token.value}` },
    })

    if (!res.ok) {
      buyerError.value = `La API respondio ${res.status} ${res.statusText}`
      return
    }

    const data = await res.json()
    buyerEmail.value = data.email
    buyerPhoneNumber.value = data.phoneNumber
    buyerAddress.value = data.address
    buyerCp.value = data.cp
    buyerCountry.value = data.country
  } catch (e) {
    buyerError.value = 'No se pudo contactar la API'
  } finally {
    buyerLoading.value = false
  }
}

onMounted(fetchBuyer)

const paymentMethods = [
  { value: 'CARD', label: 'Tarjeta de credito/debito' },
  { value: 'TRANSFER', label: 'Transferencia bancaria' },
  { value: 'PAYPAL', label: 'PayPal' },
  { value: 'APPLE_PAY', label: 'Apple Pay' },
  { value: 'GOOGLE_PAY', label: 'Google Pay' },
]

const shippingMethods = [
  { value: 'HOME_DELIVERY', label: 'Envio a domicilio' },
  //{ value: 'PICKUP_POINT', label: 'Recogida en punto de encuentro' },
  //{ value: 'EXPRESS', label: 'Mensajeria urgente' },
]

const paymentMethod = ref('')
const shippingMethod = ref('')

// Preliminar: todavia no hay endpoint para confirmar la compra. La gestion
// real del envio (etiqueta, recogida, tracking) quedara integrada mas
// adelante con un agregador de transportistas a nivel UE (p. ej. Sendcloud
// o Packlink PRO), en vez de contratar cada transportista por separado.
function confirm() {}

function cancel() {
  router.back()
}

// Carga el SDK oficial de PayPal y renderiza su boton cuando se elige ese
// metodo de pago. El Client ID "sb" es el generico de sandbox de PayPal
// para probar el boton sin credenciales propias; se cambia por el real
// cuando se despliegue a produccion.
const PAYPAL_CLIENT_ID = 'sb'

const paypalContainer = ref(null)
const paypalLoading = ref(false)
const paypalError = ref('')

let paypalScriptPromise = null

function loadPaypalSdk() {
  if (window.paypal) return Promise.resolve()
  if (paypalScriptPromise) return paypalScriptPromise

  paypalScriptPromise = new Promise((resolve, reject) => {
    const script = document.createElement('script')
    script.src = `https://www.paypal.com/sdk/js?client-id=${PAYPAL_CLIENT_ID}&currency=EUR`
    script.onload = resolve
    script.onerror = () => reject(new Error('No se pudo cargar el SDK de PayPal'))
    document.head.appendChild(script)
  })

  return paypalScriptPromise
}

// Placeholder: todavia no existe el endpoint que cree la orden de pago en
// el backend. Cuando exista, aqui se debe pedir el orderId al servidor.
async function createPaypalOrder() {
  const res = await fetch(`${API}/v1/payments/paypal/create-order`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token.value}`,
    },
    body: JSON.stringify({ offerId: Number(props.offerId) }),
  })

  if (!res.ok) {
    throw new Error(`La API respondio ${res.status} ${res.statusText}`)
  }

  const data = await res.json()
  return data.orderId
}

// Placeholder: todavia no existe el endpoint que capture el pago aprobado
// en el backend.
async function capturePaypalOrder(orderId) {
  await fetch(`${API}/v1/payments/paypal/capture-order`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token.value}`,
    },
    body: JSON.stringify({ orderId, offerId: Number(props.offerId) }),
  })
}

watch(paymentMethod, async (value) => {
  if (value !== 'PAYPAL') return

  paypalError.value = ''
  paypalLoading.value = true

  try {
    await loadPaypalSdk()
    await nextTick()

    if (paypalContainer.value) {
      paypalContainer.value.innerHTML = ''
      window.paypal
        .Buttons({
          createOrder: () => createPaypalOrder(),
          onApprove: (data) => capturePaypalOrder(data.orderID),
        })
        .render(paypalContainer.value)
    }
  } catch (e) {
    paypalError.value = 'No se pudo cargar PayPal'
  } finally {
    paypalLoading.value = false
  }
})
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

        <div class="mb-4 bg-white rounded-xl shadow-sm border border-slate-200 p-6">
          <h2 class="text-sm font-medium text-slate-700">Tus datos de entrega</h2>

          <p v-if="buyerError" class="mt-2 rounded-lg bg-red-50 px-3 py-2 text-sm text-red-700">
            {{ buyerError }}
          </p>

          <dl v-else class="mt-2 space-y-1 text-sm text-slate-600">
            <div class="flex justify-between gap-4">
              <dt class="text-slate-500">Email</dt>
              <dd>{{ buyerEmail || '-' }}</dd>
            </div>
            <div class="flex justify-between gap-4">
              <dt class="text-slate-500">Telefono</dt>
              <dd>{{ buyerPhoneNumber || '-' }}</dd>
            </div>
            <div class="flex justify-between gap-4">
              <dt class="text-slate-500">Direccion</dt>
              <dd>{{ buyerAddress || '-' }}</dd>
            </div>
            <div class="flex justify-between gap-4">
              <dt class="text-slate-500">Codigo postal</dt>
              <dd>{{ buyerCp || '-' }}</dd>
            </div>
            <div class="flex justify-between gap-4">
              <dt class="text-slate-500">Pais</dt>
              <dd>{{ buyerCountry || '-' }}</dd>
            </div>
          </dl>
        </div>

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
            <p class="mt-2 text-xs text-slate-400">
              La generacion de etiquetas y el seguimiento del envio se gestionaran mas
              adelante a traves de un agregador de transportistas a nivel UE.
            </p>
          </div>

          <div v-if="paymentMethod === 'PAYPAL'">
            <p v-if="paypalLoading" class="text-sm text-slate-500">Cargando PayPal...</p>
            <p v-if="paypalError" class="rounded-lg bg-red-50 px-3 py-2 text-sm text-red-700">
              {{ paypalError }}
            </p>
            <div ref="paypalContainer"></div>
          </div>

          <div class="flex gap-2">
            <button
              v-if="paymentMethod !== 'PAYPAL'"
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
