import { createRouter, createWebHistory } from 'vue-router'

// 1. 컴포넌트들을 상단에서 모두 import (정적 임포트 방식)
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import DashboardView from '../views/DashboardView.vue'
import regi from '../views/regi.vue'
import my from '../views/my.vue'
import myedit from '../views/myedit.vue'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { 
      path: '/', 
      name: 'home', 
      component: HomeView 
    },
    { 
      path: '/user/login',
      name: 'login', 
      component: LoginView 
    },
    { 
      path: '/main/dashboard', 
      name: 'dashboard', 
      component: DashboardView 
    },
    { 
      path: '/user/ reg', 
      name: 'regi', 
      component: regi
    },
    {
       path: '/user/my', 
      name: 'my', 
      component: my
    },
       {
       path: '/user/my/edit', 
      name: 'myedit', 
      component: myedit
    }

  ]
})

export default router