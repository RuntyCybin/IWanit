import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Home from '../views/Home.vue'
import AboutUs from '../views/AboutUs.vue'
import Contact from '../views/Contact.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', name: 'login', component: Login },
    { path: '/', name: 'home', component: Home },
    { path: '/about', name: 'about', component: AboutUs },
    { path: '/contact', name: 'contact', component: Contact },
  ],
})

router.beforeEach((to) => {
  const isLoggedIn = !!localStorage.getItem('token')

  if (to.name !== 'login' && !isLoggedIn) {
    return { name: 'login' }
  }

  if (to.name === 'login' && isLoggedIn) {
    return { name: 'home' }
  }
})

export default router
