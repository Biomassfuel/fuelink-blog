import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/views/layout/index.vue'
import Home from '@/views/home/index.vue'
import Anime from '@/views/anime/index.vue'
import Archive from '@/views/archive/index.vue'
import About from '@/views/about/index.vue'
import FriendLink from '@/views/friendLink/index.vue'
import Blog from '@/views/blog/index.vue'
import TodayReport from '@/views/todayReport/index.vue'



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
        component: Anime
      },
      {
        path: '/todayReport',
        component: TodayReport
      },
      {
        path: '/archive',
        component: Archive
      },
      {
        path: '/about',
        component: About
      },
      {
        path: '/friendLink',
        component: FriendLink
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
