// src/main.js
import { createApp } from 'vue'
import App from './App.vue'
import router from './router' // 1. 라우터 파일 불러오기

const app = createApp(App)

app.use(router) // 2. 라우터 등록 (이 부분이 필수!)

app.mount('#app')