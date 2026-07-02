import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/views/layout/index.vue'
import Home from '@/views/home/index.vue'



const routes = [
  {
    path: '/',
    component: Layout,
    redirect: '/',
    children: [
      {
        path: '/',
        component: Home

      },
      {
        path: '/anime',
        component: () => import('@/views/anime/index.vue')
      },
      {
        path: '/todayReport',
        component: () => import('@/views/todayReport/index.vue')
      },
      {
        path: '/archive',
        component: () => import('@/views/archive/index.vue')
      },
      {
        path: '/about',
        component: () => import('@/views/about/index.vue')
      },
      {
        path: '/friendLink',
        component: () => import('@/views/friendLink/index.vue')
      },
      {
        path: '/front/blog',
        // path: '/blog',
        component: () => import('@/views/blog/index.vue')
        // component: Blog
      }
    ]
  }
]
const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
