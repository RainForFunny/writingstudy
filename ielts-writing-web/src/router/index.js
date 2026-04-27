import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Writing',
    component: () => import('../views/WritingPage.vue'),
    meta: { title: '写作' }
  },
  {
    path: '/archive',
    name: 'Archive',
    component: () => import('../views/ArchivePage.vue'),
    meta: { title: '文章归档' }
  },
  {
    path: '/archive/:id',
    name: 'ArchiveDetail',
    component: () => import('../views/ArchiveDetail.vue'),
    meta: { title: '文章详情' }
  },
  {
    path: '/weakness',
    name: 'Weakness',
    component: () => import('../views/WeaknessPage.vue'),
    meta: { title: '个人弱点' }
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/LoginPage.vue'),
    meta: { title: '登录' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 雅思写作智能教练` : '雅思写作智能教练'
  next()
})

export default router