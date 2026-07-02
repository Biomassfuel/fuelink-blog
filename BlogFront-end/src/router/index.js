import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/views/layout/index.vue'
import Home from '@/views/home/index.vue'
import { startLoading, doneLoading } from '@/composables/useLoadingBar'



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

// 路由跳转时显示 / 隐藏顶部加载进度条
router.beforeEach((to, from, next) => {
  // 仅在真正切换页面时启动（同页仅 query 变化也算一次加载，体验更完整）
  startLoading()
  next()
})

router.afterEach(() => {
  doneLoading()
})

router.onError(() => {
  doneLoading()
})

export default router
