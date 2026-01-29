
<template>
  <div class="mypage-wrapper">
    <div class="mypage-header">
      <h2 class="title">마이페이지</h2>
     <button class="edit-btn" @click="user_update">수정</button>
    </div>

    <div class="info-section">
      <h3 class="section-subtitle">- 직원 정보</h3>
      
      <div class="info-container">
        <ul class="user-info-list">
          <li>
            <span class="bullet">●</span>
            <span class="label">이름 :</span>
            <span class="value">{{ user_name }}</span> 
          </li>
          <li>
            <span class="bullet">●</span>
            <span class="label">ID :</span>
            <span class="value">{{ user_id }}</span>
          </li>
          <li>
            <span class="bullet">●</span>
            <span class="label">PW :</span>
            <span class="value">******</span>
          </li>
          <li>
            <span class="bullet">●</span>
            <span class="label">E-mail :</span>
            <span class="value">{{ user_email }}</span>
          </li>
          
          <li class="route-container">
            <div class="route-header">
              <span class="bullet">●</span>
              <span class="label">담당 노선 정보</span>
            </div>
            <div class="route-content">
            <!-- 클래스명 지정해서 호선대로 색 가져옴 -->
            <div class="display-box" :class="'line-' + line_name">
            {{ line_name }}
            </div>
            <div class="display-box station-box">
            {{ station_name }}
            </div>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      user_id: "",
      user_name: "",
      user_email: "",
      line_name: "",
      station_name: ""
    }
  },
mounted() {

  // "login"과 "user_info" 중 어떤 이름으로 저장했는지 확인해서 둘 다 시도해보세요.
  const sessionData = sessionStorage.getItem("user_info") || sessionStorage.getItem("login");
  const login = JSON.parse(sessionData);

  if (!login) {
    console.error("세션에 데이터가 없습니다!");
    // alert("로그인 해주세요"); 
    // this.$emit('change-view', 'Login'); 
  } else {
    this.user_id = login.id || login.user_id || '';
    this.user_name = login.name || login.user_name || ''; 
    this.user_email = login.user_email || '';
    this.line_name = login.line_name || '';
    this.station_name = login.station_name || '';
  }
},
  methods: {
    user_update() {
      this.$emit('change-view', 'myedit');
    }
  }
}
</script>

<style scoped>
/* 전체 컨테이너: 패딩을 고정하여 화면 흔들림 방지 */
.mypage-wrapper {
  width: 100%;
  background-color: #ffffff;
  font-family: 'Pretendard', sans-serif;
  color: #333;
}

/* 헤더: 수정 페이지와 동일한 스타일/높이 유지 */
.mypage-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 25px; /* 수정페이지와 패딩 통일 */
  background-color: #eeeeee;
  border-bottom: 1px solid #ddd;
}

.title {
  font-size: 20px;
  font-weight: 700;
}

/* 수정 버튼 */
.edit-btn {
  background-color: #3f417e; 
  color: #fff;
  border: none;
  padding: 5px 22px;
  border-radius: 20px;
  font-size: 14px;
  cursor: pointer;
}

.info-section {
  padding: 30px; /* 내부 컨텐츠 여백 */
}

.section-subtitle {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 25px;
}

.user-info-list {
  list-style: none;
  padding: 0;
}

.user-info-list li {
  display: flex;
  align-items: center;
  margin-bottom: 18px; /* 간격 고정 */
  font-size: 16px;
}

.bullet {
  font-size: 8px;
  margin-right: 12px;
}

.label {
  font-weight: 700;
  min-width: 110px;
}

.value {
  color: #555;
}

/* 담당 노선 정보 레이아웃 */
.route-container {
  flex-direction: column;
  align-items: flex-start !important;
  margin-top: 10px;
}

.route-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.route-content {
  padding-left: 20px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.display-box {
  padding: 8px 15px;          
  background-color: #f9f9f9;   
  border: 1px solid #ddd;      
  border-radius: 8px;          
  font-size: 14px;
  text-align: center;
  color: #555;
  min-width: 20px;             
  box-shadow: inset 0 1px 2px rgba(0,0,0,0.05); 
  display: inline-block;   
 }

 /* 역 이름 박스 (흰색 배경) */
.station-box {
  background-color: #fff;
  color: #333;
}

/* --- 각 호선별 뱃지 스타일 (글자는 흰색, 배경은 호선색) --- */

/* 1호선 */
.display-box.line-1호선 {
  background-color: #2a317c;
  color: white;
  border-color: #2a317c;
}

/* 2호선 */
.display-box.line-2호선 {
  background-color: #2fae35;
  color: white;
  border-color: #2fae35;
}

/* 3호선 */
.display-box.line-3호선 {
  background-color: #ff6000;
  color: white;
  border-color: #ff6000;
}

/* 4호선 */
.display-box.line-4호선 {
  background-color: #1a97dd;
  color: white;
  border-color: #1a97dd;
}

/* 5호선 */
.display-box.line-5호선 {
  background-color: #822fe1;
  color: white;
  border-color: #822fe1;
}

/* 6호선 */
.display-box.line-6호선 {
  background-color: #ae4908;
  color: white;
  border-color: #ae4908;
}

/* 7호선 */
.display-box.line-7호선 {
  background-color: #636b10;
  color: white;
  border-color: #636b10;
}

/* 8호선 */
.display-box.line-8호선 {
  background-color: #e6265b;
  color: white;
  border-color: #e6265b;
}

/* 9호선 */
.display-box.line-9호선 {
  background-color: #bdb092;
  color: white;
  border-color: #bdb092;
}
</style>

