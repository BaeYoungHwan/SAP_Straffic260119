import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue' // 로그인 컴포넌트 추가

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', name: 'home', component: HomeView },
    { path: '/user', name: 'login', component: LoginView }, // 로그인 경로
    { path: '/main/dashboard', name: 'dashboard', component: () => import('../views/DashboardView.vue') },
    { path: '/user/my', name: 'settings', component: () => import('../views/SettingsView.vue') }
  ]
})

export default router