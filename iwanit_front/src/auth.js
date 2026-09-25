import { ref } from 'vue'
import router from './router'

export const token = ref(localStorage.getItem('token'))
export const role = ref(localStorage.getItem('role'))

export function setToken(value) {
  token.value = value
  localStorage.setItem('token', value)
}

export function setRole(value) {
  role.value = value
  localStorage.setItem('role', value)
}

export function logout() {
  token.value = null
  role.value = null
  localStorage.removeItem('token')
  localStorage.removeItem('role')
  router.push({ name: 'login' })
}

// Keeps every open tab in sync: if the session is cleared anywhere (logout
// in another tab, devtools, expiry cleanup), this tab drops straight to
// /login instead of sitting on a page it no longer has access to.
window.addEventListener('storage', (event) => {
  if (event.key === 'token' && !event.newValue) {
    token.value = null
    role.value = null
    router.push({ name: 'login' })
  }
})
