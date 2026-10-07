import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Home from '../views/Home.vue'
import AboutUs from '../views/AboutUs.vue'
import Contact from '../views/Contact.vue'
import Profile from '../views/Profile.vue'
import CreateArticle from '../views/CreateArticle.vue'
import Article from '../views/Article.vue'
import BuyerArticles from '../views/BuyerArticles.vue'
import SellerOffers from '../views/SellerOffers.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', name: 'login', component: Login },
    { path: '/', name: 'home', component: Home },
    { path: '/about', name: 'about', component: AboutUs },
    { path: '/contact', name: 'contact', component: Contact },
    { path: '/profile', name: 'profile', component: Profile },
    { path: '/articles/create', name: 'create-article', component: CreateArticle },
    { path: '/my-articles', name: 'buyer-articles', component: BuyerArticles },
    { path: '/my-offers', name: 'seller-offers', component: SellerOffers },
    { path: '/articles/:id', name: 'article', component: Article, props: true },
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
