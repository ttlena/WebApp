import { useLogin } from '@/services/useLogin'
import { createRouter, createWebHistory } from 'vue-router'
import AngeboteView from '../views/AngeboteView.vue'
import GebotView from '../views/GebotView.vue'
import LoginView from '../views/LoginView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: AngeboteView
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/AboutView.vue')
    },
    {
      path: '/gebot/:angebotidstr',
      name: 'GebotView',
      component: GebotView,
      props: true
    },
    {
      path: '/login',
      name: 'Login/Logout',
      component: LoginView
    }
  ]
})

router.beforeEach(async (to) => {
  if (useLogin().logindata.loggedin == false && to.path !== '/login'){
    return { name: 'Login/Logout'}
  }
})

export default router
