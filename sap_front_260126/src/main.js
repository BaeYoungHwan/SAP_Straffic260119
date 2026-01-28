import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import VueCookies from 'vue3-cookies' // 패키지 임포트 확인 (s 붙음)

const app = createApp(App)

// 1. 라우터 등록
app.use(router)

// 2. 쿠키 플러그인 등록 (이게 있어야 컴포넌트에서 $cookies 사용 가능)
app.use(VueCookies, {
  expireTimes: "30d",
  path: "/",
  domain: "",
  secure: true,
  sameSite: "None",
})

app.mount('#app')