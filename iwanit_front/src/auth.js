import { ref } from 'vue'
import router from './router'

export const token = ref(localStorage.getItem('token'))
export const role = ref(localStorage.getItem('role'))
export const avatarUrl = ref(localStorage.getItem('avatarUrl'))
export const email = ref(localStorage.getItem('email'))

export function setToken(value) {
  token.value = value
  localStorage.setItem('token', value)
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

export function logout() {
  token.value = null
  role.value = null
  avatarUrl.value = null
  email.value = null
  localStorage.removeItem('token')
  localStorage.removeItem('role')
  localStorage.removeItem('avatarUrl')
  localStorage.removeItem('email')
  router.push({ name: 'login' })
}

// Keeps every open tab in sync: if the session is cleared anywhere (logout
// in another tab, devtools, expiry cleanup), this tab drops straight to
// /login instead of sitting on a page it no longer has access to.
window.addEventListener('storage', (event) => {
  if (event.key === 'token' && !event.newValue) {
    token.value = null
    role.value = null
    avatarUrl.value = null
    email.value = null
    router.push({ name: 'login' })
  }
})
