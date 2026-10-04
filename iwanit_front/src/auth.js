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

export function logout() {
  token.value = null
  userId.value = null
  role.value = null
  avatarUrl.value = null
  email.value = null
  name.value = null
  phoneNumber.value = null
  buyerId.value = null
  localStorage.removeItem('token')
  localStorage.removeItem('userId')
  localStorage.removeItem('role')
  localStorage.removeItem('avatarUrl')
  localStorage.removeItem('email')
  localStorage.removeItem('name')
  localStorage.removeItem('phoneNumber')
  localStorage.removeItem('buyerId')
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
    router.push({ name: 'login' })
  }
})
