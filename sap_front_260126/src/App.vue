<template>
  <div v-if="!isLoggedIn" class="login-fullscreen-container">
      <Login v-if="currentView === 'Login'" 
         @login-success="handleLoginSuccess"
         @go-regi="currentView = 'Regi'" />

      <Regi v-if="currentView === 'Regi'" 
        @go-login="currentView = 'Login'" />
        
  </div>

  <div v-else class="layout" :class="'line_name-' + userData.line_name">
    <header class="header">
      <div class="header-left-col" @click="currentView = 'Home'">
        <div class="logo-wrapper">
          <img :src="logo" alt="Logo" class="company-logo" />
        </div>
      </div>
      
      <div class="header-right-col">
        <div class="header-status-row">
          <div class="user-profile-box">
          
            <div class="user-details-row">
              <span class="u-name">{{ userData.name }} 관리자     </span>
              <span class="u-sep"> | </span>
              <span class="u-station">{{ userData.station_name }}역</span>
              <span class="u-sep"> | </span>
           
              <button @click="logout" class="logout-small-btn">로그아웃</button>
              <span class="user-avatar" @click="currentView = 'my'" style="cursor:pointer">  |  👤</span>
            </div>
         
          </div>
        </div>

        <div class="header-info-row">
          <div class="sub-nav-text">
            <span class="dot">●</span> 실시간 관리 시스템 접속 중 : {{ userData.line_name }}호선 모니터링
          </div>
        </div>
      </div>
    </header>

    <div class="container">
      <aside class="sidebar">
        <nav>
          <div class="nav-item" @click="currentView = 'Home'" :class="{ active: currentView === 'Home' }">▶대시보드</div>
          <div class="nav-item" @click="currentView = 'Dashboard'" :class="{ active: currentView === 'Dashboard' }">▶역별현황</div>
          <div class="nav-item" @click="currentView = 'analysis'" :class="{ active: currentView === 'analysis' }">▶통계 분석</div>
          <div class="nav-item" @click="currentView = 'Issue'" :class="{ active: currentView === 'Issue' }">▶장애/이슈</div>
          <div class="nav-item" @click="currentView = 'Settings'" :class="{ active: currentView === 'Settings' }">▶사용자 관리</div>
        </nav>
      </aside>

    <main class="content">
    <component 
    :is="views[currentView]" 
    :key="currentView" 
    @change-view="currentView = $event" />
      </main>
    </div>

    <footer class="footer">
      <p>© 2026 SAP_Straffic | Smart Storage Management System</p>
    </footer>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import logo from './assets/로고.png'

// 뷰 컴포넌트 임포트
import Home from './views/HomeView.vue';
import Dashboard from './views/DashboardView.vue';
import Settings from './views/SettingsView.vue';
import Login from './views/LoginView.vue';
import Regi from './views/regi.vue';
import myedit from './views/myedit.vue';
import my from './views/my.vue';
// 컴포넌트 매핑
const views = { Home, Dashboard, Settings, Login, Regi , myedit,my};
const currentView = ref('Login');
const isLoggedIn = ref(false);

// 사용자 데이터 초기화
const userData = reactive({
  id: '',
  name: '',
  line_name: 'default',
  station_name: ''
});

// 데이터 적용 공통 함수
const applyUserData = (payload) => {
  if (!payload) return;

  userData.id = payload.user_id || payload.id || '';
  userData.name = payload.user_name || payload.name || '';
  userData.line_name = payload.line_name || 'default';
  userData.station_name = payload.station_name || '';
  userData.user_email = payload.user_email || '';
  isLoggedIn.value = true;
  currentView.value = 'Home';
};
// 새로고침 시 세션 정보 복구
onMounted(() => {
  const savedUser = sessionStorage.getItem("user_info");
  if (savedUser) {
    try {
      const payload = JSON.parse(savedUser);
      applyUserData(payload);
    } catch (e) {
      console.error("세션 데이터 파싱 에러:", e);
      sessionStorage.removeItem("user_info");
    }
  }
});
// 로그인 성공 핸들러
const handleLoginSuccess = (payload) => {

  applyUserData(payload);
  sessionStorage.setItem("user_info", JSON.stringify(payload));
};

// 로그아웃
const logout = () => {
  isLoggedIn.value = false;
  userData.id = '';
  userData.name = '';
  userData.line_name = 'default';
  //세션삭제
  sessionStorage.removeItem("user_info");
  currentView.value = 'Login';
};
</script>

<style>
/* --- 기본 레이아웃 설정 --- */
* { margin: 0; padding: 0; box-sizing: border-box; }
body { overflow: hidden; } /* 스크롤 방지 */

.layout { 
  display: flex; 
  flex-direction: column; 
  height: 100vh; 
  width: 100vw;
  font-family: 'Pretendard', sans-serif; 
  transition: background 0.3s; 
}

.container { display: flex; flex: 1; overflow: hidden; }

.header {
  height: 110px;
  display: flex;
  background-color: #2c3e50;
  color: white;
  transition: background 0.3s;
  border-bottom: 2px solid rgba(0,0,0,0.1);
}
.logout-small-btn{
  /* 배경을 투명하게 */
  background: transparent; 
  /* 테두리 설정 */
  border: 1px solid rgba(255, 255, 255, 0.5); 
  color: white;
  
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s ease; /* 부드러운 변화 효과 */
}
.header-left-col {
  width: 240px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-right: 1px solid rgba(255,255,255,0.1);
  cursor: pointer;
}

.company-logo { width: 180px; height: auto; object-fit: contain; }

.header-right-col { flex: 1; display: flex; flex-direction: column; }
.header-status-row { flex: 1.2; display: flex; align-items: center; justify-content: flex-end; padding: 0 30px; }
.header-info-row {
  flex: 0.8;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding: 0 30px;
  background-color: rgba(0, 0, 0, 0.1); /* 배경색 유지 */
  
  /* [중요] 가상 요소 배치를 위한 기준점 */
  position: relative; 
  overflow: hidden; 
}

/* 글자 영역: 이미지보다 위에 표시 */
.sub-nav-text {
  position: relative;
  z-index: 2; /* 이미지보다 높은 레이어 */
  font-size: 1.20rem;
  color: #ffffff; /* 가독성을 위해 흰색 계열 추천 */
  font-weight: 500;
  text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.7); /* 이미지 위에서도 글자가 선명하게 보임 */
}

/* 이미지 영역: ::after를 사용하여 배경으로 배치 */
.header-info-row::after {
  content: "";
  position: absolute;
  top: 0;
  right: 0; /* 우측 정렬 */
  width: 250px; /* 로고 글자 이미지의 적절한 너비 */
  height: 100%;
  
  background-image: url('./assets/로고 글자.png');
  background-repeat: no-repeat;
  background-position: center right;
  background-size: contain; /* 영역 안에서 비율 유지 */
  
  /* [핵심] 글자 방해 안 되게 투명도 조절 */
  opacity: 0.4; 
  z-index: 1; /* 글자(2)보다 뒤로 배치 */
  
  }
.sidebar {

  font-size: 2cap;
  width: 240px;
  background-color: #f8f9fa;
  border-right: 1px solid #ddd;
  padding: 20px;
  transition: background 0.3s;
}

.nav-item { padding: 12px; margin-bottom: 5px; cursor: pointer; border-radius: 8px; transition: 0.2s; color: #2c3e50; }
.nav-item.active { background-color: #42b983; color: white; font-weight: bold; }
.content { flex: 1; padding: 30px; overflow-y: auto; background-color: #fcfcfc; }

/* --- 호선별 테마 스타일 (헤더 + 사이드바 + 활성 메뉴 세트) --- */

/* 1호선 */  /*header-left-col*/
.layout.line_name-1호선 .header { background-color: #2a317c !important; }
.layout.line_name-1호선 .sidebar { background-color: #eef1f7 !important; }
.layout.line_name-1호선 .nav-item.active { background-color: #2a317c !important; color: white !important; }
.layout.line_name-1호선 .footer { background-color: #2a317c !important; }
/* 2호선 */
.layout.line_name-2호선 .header { background-color: #2fae35 !important; }
.layout.line_name-2호선 .sidebar { background-color: #eff7ef !important; }
.layout.line_name-2호선 .nav-item.active { background-color: #2fae35 !important; color: white !important; }
.layout.line_name-2호선 .footer { background-color: #2fae35 !important; }
/* 3호선 */
.layout.line_name-3호선 .header { background-color: #ff6000 !important; }
.layout.line_name-3호선 .sidebar { background-color: #fff6f0 !important; }
.layout.line_name-3호선 .nav-item.active { background-color: #ff6000 !important; color: white !important; }
.layout.line_name-3호선 .footer { background-color: #ff6000 !important; }
/* 4호선 */
.layout.line_name-4호선 .header { background-color: #1a97dd !important; }
.layout.line_name-4호선 .sidebar { background-color: #f0f9ff !important; }
.layout.line_name-4호선 .nav-item.active { background-color: #1a97dd !important; color: white !important; }
.layout.line_name-4호선 .footer { background-color: #1a97dd !important; }
/* 5호선 */
.layout.line_name-5호선 .header { background-color: #822fe1 !important; }
.layout.line_name-5호선 .sidebar { background-color: #f8f2ff !important; }
.layout.line_name-5호선 .nav-item.active { background-color: #822fe1 !important; color: white !important; }
.layout.line_name-5호선 .footer { background-color: #822fe1 !important; }
/* 6호선 */
.layout.line_name-6호선 .header { background-color: #ae4908 !important; }
.layout.line_name-6호선 .sidebar { background-color: #f9f4f1 !important; }
.layout.line_name-6호선 .nav-item.active { background-color: #ae4908 !important; color: white !important; }
.layout.line_name-6호선 .footer { background-color: #ae4908 !important; }
/* 7호선 */
.layout.line_name-7호선 .header { background-color: #636b10 !important; }
.layout.line_name-7호선 .sidebar { background-color: #f8f9f0 !important; }
.layout.line_name-7호선 .nav-item.active { background-color: #636b10 !important; color: white !important; }
.layout.line_name-7호선 .footer { background-color: #636b10 !important; }
/* 8호선 */
.layout.line_name-8호선 .header { background-color: #e6265b !important; }
.layout.line_name-8호선 .sidebar { background-color: #fff0f4 !important; }
.layout.line_name-8호선 .nav-item.active { background-color: #e6265b !important; color: white !important; }
.layout.line_name-8호선 .footer { background-color: #e6265b !important; }
/* 9호선 */
.layout.line_name-9호선 .header { background-color: #bdb092 !important; }
.layout.line_name-9호선 .sidebar { background-color: #f9f8f4 !important; }
.layout.line_name-9호선 .nav-item.active { background-color: #bdb092 !important; color: white !important; }
.layout.line_name-9호선 .footer { background-color: #bdb092 !important; }
/* 공통 nav-item 스타일 보완 */
.nav-item {
  padding: 12px;
  margin-bottom: 5px;
  cursor: pointer;
  border-radius: 8px;
  transition: all 0.2s ease; /* 부드러운 전환 */
  color: #555;
  font-weight: 500;
}

.nav-item:hover:not(.active) {
  background-color: rgba(0, 0, 0, 0.05); /* 마우스 올렸을 때 살짝 어두워짐 */
}



/* --- 1. 로그인 전 전용 스타일 --- */
.login-fullscreen-container {
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #2c3e50 0%, #000000 100%); /* 전문적인 배경색 */
}

/* --- 2. 로그인 후 레이아웃 스타일 (기존과 동일) --- */
* { margin: 0; padding: 0; box-sizing: border-box; }
body { overflow: hidden; }

.layout { display: flex; flex-direction: column; height: 100vh; width: 100vw; font-family: 'Pretendard', sans-serif; }
.container { display: flex; flex: 1; overflow: hidden; }

/* 헤더/사이드바/테마 CSS는 이전과 동일하게 유지... */

/* [생략된 부분: 이전 답변의 호선별 .line_name-X 테마 CSS를 그대로 붙여넣으시면 됩니다] */

.header { height: 160px; display: flex; color: white; border-bottom: 2px solid rgba(0,0,0,0.1); }
.header-left-col { width: 240px; display: flex; align-items: center; justify-content: center; border-right: 1px solid rgba(255,255,255,0.1); }
.header-right-col { flex: 1; display: flex; flex-direction: column; }
.header-status-row { flex: 1.2; display: flex; align-items: center; justify-content: flex-end; padding: 0 30px; }
.header-info-row { flex: 2.0; display: flex; align-items: center; justify-content: flex-end; padding: 0 30px; background-color: rgba(0,0,0,0.1); }

.sidebar { width: 240px; background-color: #f8f9fa; border-right: 1px solid #ddd; padding: 20px; }
.nav-item { padding: 12px; margin-bottom: 5px; cursor: pointer; border-radius: 8px; color: #555; }
.nav-item.active { color: white !important; font-weight: bold; }

.footer { height: 40px; background: #eee; display: flex; align-items: center; justify-content: center; font-size: 0.8rem; }
</style>