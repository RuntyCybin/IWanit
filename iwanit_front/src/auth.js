import { ref } from 'vue'
import router from './router'

export const token = ref(localStorage.getItem('token'))
export const userId = ref(localStorage.getItem('userId'))
export const role = ref(localStorage.getItem('role'))
export const avatarUrl = ref(localStorage.getItem('avatarUrl'))
export const email = ref(localStorage.getItem('email'))
export const name = ref(localStorage.getItem('name'))
export const phoneNumber = ref(localStorage.getItem('phoneNumber'))
export const buyerId = ref(localStorage.getItem('buyerId'))
export const sellerId = ref(localStorage.getItem('sellerId'))
export const country = ref(localStorage.getItem('country'))
export const cp = ref(localStorage.getItem('cp'))
export const address = ref(localStorage.getItem('address'))
export const iban = ref(localStorage.getItem('iban'))
export const paypal = ref(localStorage.getItem('paypal'))

export function setToken(value) {
  token.value = value
  localStorage.setItem('token', value)
}

export function setUserId(value) {
  userId.value = value
  localStorage.setItem('userId', value)
}

export function setRole(value) {
  role.value = value
  localStorage.setItem('role', value)
}

export function setAvatarUrl(value) {
  avatarUrl.value = value
  localStorage.setItem('avatarUrl', value)
}

export function setEmail(value) {
  email.value = value
  localStorage.setItem('email', value)
}

export function setName(value) {
  name.value = value
  localStorage.setItem('name', value)
}

export function setPhoneNumber(value) {
  phoneNumber.value = value
  localStorage.setItem('phoneNumber', value)
}

export function setBuyerId(value) {
  buyerId.value = value
  localStorage.setItem('buyerId', value)
}

export function setSellerId(value) {
  sellerId.value = value
  localStorage.setItem('sellerId', value)
}

export function setCountry(value) {
  country.value = value
  localStorage.setItem('country', value)
}

export function setCp(value) {
  cp.value = value
  localStorage.setItem('cp', value)
}

export function setAddress(value) {
  address.value = value
  localStorage.setItem('address', value)
}

export function setIban(value) {
  iban.value = value
  localStorage.setItem('iban', value)
}

export function setPaypal(value) {
  paypal.value = value
  localStorage.setItem('paypal', value)
}

export function logout() {
  token.value = null
  userId.value = null
  role.value = null
  avatarUrl.value = null
  email.value = null
  name.value = null
  phoneNumber.value = null
  buyerId.value = null
  sellerId.value = null
  country.value = null
  cp.value = null
  address.value = null
  iban.value = null
  paypal.value = null
  localStorage.removeItem('token')
  localStorage.removeItem('userId')
  localStorage.removeItem('role')
  localStorage.removeItem('avatarUrl')
  localStorage.removeItem('email')
  localStorage.removeItem('name')
  localStorage.removeItem('phoneNumber')
  localStorage.removeItem('buyerId')
  localStorage.removeItem('sellerId')
  localStorage.removeItem('country')
  localStorage.removeItem('cp')
  localStorage.removeItem('address')
  localStorage.removeItem('iban')
  localStorage.removeItem('paypal')
  router.push({ name: 'login' })
}

// Keeps every open tab in sync: if the session is cleared anywhere (logout
// in another tab, devtools, expiry cleanup), this tab drops straight to
// /login instead of sitting on a page it no longer has access to.
window.addEventListener('storage', (event) => {
  if (event.key === 'token' && !event.newValue) {
    token.value = null
    userId.value = null
    role.value = null
    avatarUrl.value = null
    email.value = null
    name.value = null
    phoneNumber.value = null
    buyerId.value = null
    sellerId.value = null
    country.value = null
    cp.value = null
    address.value = null
    iban.value = null
    paypal.value = null
    router.push({ name: 'login' })
  }
})
