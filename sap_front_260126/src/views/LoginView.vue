<template>
  <div class="login-bg">
    <div class="login-card">

      <!-- 로고 영역 -->
      <div class="logo-row">
        <img src="../assets/로고.png" class="logo-img" />
      </div>

      <!-- 타이틀 -->
      <div class="login-title">로그인</div>

      <!-- 아이디 -->
      <div class="input-box">
        <span class="icon">👤</span>
        <input v-model="user_id" placeholder="아이디" />
      </div>

      <!-- 비밀번호 -->
      <div class="input-box">
        <span class="icon">🔒</span>
        <input v-model="user_pw" type="password" placeholder="비밀번호" />
      </div>

      <!-- ID 저장 -->
      <div class="save-id">
        <input type="checkbox" v-model="save_id" @click="saveId"/>
        <span>ID 저장</span>
      </div>

      <!-- 로그인 버튼 -->
      <button class="login-btn" @click="login">로그인</button>

      <!-- 회원가입 텍스트 (이동만) -->
      <div class="join-text">
            <p @click="$emit('go-regi')" style="cursor:pointer">
                회원가입
            </p>
      </div>

    </div>
  </div>
</template>

<script>
import { useCookies } from 'vue3-cookies';
const { cookies } = useCookies();
import axios from 'axios'
import Regi from './regi.vue';

export default{
  data() {
    return {
      user_id : '',
      user_pw : '',
      save_id : false,
      user_email:''
    }
  },
  mounted(){
    let userId = cookies.get('userId');
    if(userId !== null){
      this.save_id = true;
      this.user_id = userId;
    }else{
      this.save_id = false;
      this.user_id = "";
    }
  },
  methods: {
      //유저아이디 저장
      saveId(){
       if (this.save_id === false && this.user_id.trim() !== "") {
      cookies.set("userId", this.user_id);
      } else {
      cookies.remove("userId");
       }
      },
      login(){
        let param = {
         params: {
        user_id: this.user_id,
        user_pw: this.user_pw,
          }
       }
      axios.post('http://localhost:9000/check_login', null, param)
      .then(resp => {
        let user = resp.data;

        // 백엔드 응답 데이터 확인 (id가 아니라 user_id로 올 확률이 높음)
        if (!user || user.user_id === undefined) {
          alert('아이디 또는 비밀번호가 틀렸거나 아직 승인대기중입니다');
          return;
        }

        // 세션 저장
        sessionStorage.setItem("login", JSON.stringify(user));
        
        alert('로그인 성공');

        
        this.$emit('login-success', {
          id: user.user_id,          
          name: user.user_name,      
          line_name: user.line_name,
          station_name: user.station_name,
          user_email: user.user_email
        });
      })
      .catch(err => {
        console.error(err);
        alert('서버 연결에 실패했습니다.');
      })
  },
},
}
</script>

<style scoped>
/* 배경 */
.login-bg {
  position: fixed;
  inset: 0;
  background: url("../assets/background.png") center / cover no-repeat;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 카드 */
.login-card {
  width: 350px;
  background: linear-gradient(180deg, #3b5bff 0%, #2436b8 100%);
  border-radius: 12px;
  padding: 5px 20px 26px;
  box-shadow: 0 20px 40px rgba(0,0,0,0.45);
  color: #fff;
}

/* 로고 라인 */
.logo-row {
  align-items: center;

}

.logo-img {
  width: 300px;
  height: auto;
  margin-left: 7px;
}

.logo-sub {
  font-size: 30px;
  opacity: 1;
}

/* 로그인 타이틀 */
.login-title {
  text-align: center;
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 10px;
}

/* 입력 박스 */
.input-box {
  display: flex;
  align-items: center;
  gap: 8px;
  background: #f4f6ff;
  border-radius: 8px;
  padding: 10px 12px;
  margin-bottom: 10px;
}

.input-box input {
  border: none;
  outline: none;
  background: transparent;
  width: 100%;
  font-size: 13px;
}

/* ID 저장 */
.save-id {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 15px;
  margin: 8px 0 14px;
}

/* 로그인 버튼 */
.login-btn {
  width: 100%;
  height: 40px;
  background: linear-gradient(180deg, #5f8bff 0%, #3f6bff 100%);
  border: none;
  border-radius: 8px;
  color: #fff;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
}

/* 회원가입 텍스트 */
.join-text {
  margin-top: 12px;
  text-align: center;
  font-size: 12px;
  opacity: 0.85;
  cursor: pointer;
}
</style>