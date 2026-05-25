import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/login/index.vue';
import Layout from '@/views/layout/index.vue';
import Index from '@/views/index/index.vue';
import Blog from '@/views/blog/index.vue';
import Write from '@/views/write/index.vue';
import Label from '@/views/label/index.vue';
import Sentence from '@/views/sentence/index.vue';
import Friend from '@/views/friend/index.vue';


const routes = [
  { path: '/', 
    component: Layout,
    redirect: '/index',
    children: [
      { path: '/index', component: Index },
      { path: '/blog', component: Blog },
      { path: '/write', component: Write },
      { path: '/label', component: Label },
      { path: '/sentence', component: Sentence },
      { path: '/friend', component: Friend },
      // { path: '/report/emp', component: EmpReport },
      // { path: '/report/stu', component: StuReport },
      // { path: '/log', component: Log }
    ]
  },
  { path: '/login', component: Login }
];

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
