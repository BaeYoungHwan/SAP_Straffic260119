<template>
  <div class="join-bg">
    <div class="join-card">

      <div class="logo-row">
        <img src="../assets/로고.png" class="logo-img" />
      </div>

      <div class="join-title">회원가입</div>

      <div class="input-box">
        <input v-model="add_name" type="text" placeholder="이름" />
      </div>

      <div class="input-box row">
        <input v-model="add_id" type="text" placeholder="아이디" />
        <button @click="check_id" class="check-btn">중복확인</button>
      </div>

      <div class="input-box">
        <input v-model="add_email" type="email" placeholder="이메일" />
      </div>

      <div class="input-box">
        <input v-model="add_pw" type="password" placeholder="비밀번호" />
      </div>

      <div class="input-box">
        <input v-model="check_pw" type="password" placeholder="비밀번호 확인" />
      </div>

      <div class="section-title">역 선택</div>
      <SubwaySelector @update-selection="handleSubwayChange" />

      <button class="join-btn" @click="adduser">회원가입</button>

    </div>
  </div>
</template>

<script>
import SubwaySelector from './SubwaySelector.vue';
import axios from 'axios';

export default {
  name: "regi",
  components: {
    SubwaySelector
  },
  data() {
    return {
      add_name: '',
      add_id: '',
      add_email: '',
      add_pw: '',
      check_pw: '',
      selectedSubway: {
        line_name: '',
        station_id: '',
        station_name: ''
      }
    }
  },
  methods: {
    // SubwaySelector에서 선택된 데이터를 받아오는 함수
    handleSubwayChange(data) {
      this.selectedSubway.line_name = data.line_name;
      this.selectedSubway.station_id = data.station.station_id;
      this.selectedSubway.station_name = data.station.station_name;
      console.log("선택된 역 정보:", this.selectedSubway);
    },
    check_id(){
        if(!this.add_id || this.add_id.trim() === ""){
            alert('아이디를 입력해주세요')
            return;
        }

        axios.post('http://localhost:9000/check_userid', null, { 
                params: { user_id: this.add_id } 
            }).then(resp=>{
                if(resp.data === "YES"){
                    alert('사용 가능한 아이디입니다');
                
                }else{
                    alert('중복된 아이디입니다');

                    this.add_id = "";
                }
            }).catch(err=>{
                alert(err);
            })
            
    },
    
    // 회원가입 전송
    adduser() {
      // 1. 기본 유효성 검사
      if (!this.add_id || !this.add_pw || !this.add_name || !this.add_email) {
        alert("모든 정보를 입력해주세요.");
        return;
      }
      if (this.add_pw !== this.check_pw) {
        alert("비밀번호가 일치하지 않습니다.");
        return;
      }
      if (!this.selectedSubway.station_id) {
        alert("담당 호선과 역을 선택해주세요.");
        return;
      }

      // 2. 서버 전송 데이터 구성
      let param = { params: {
        user_name: this.add_name,
        user_id: this.add_id,
        user_email: this.add_email,
        user_pw: this.add_pw,
        line_name: this.selectedSubway.line_name,
        station_id: this.selectedSubway.station_id,
        station_name: this.selectedSubway.station_name
      }

  
      };

      // 3. Axios 전송
      axios.post('http://localhost:9000/add_regi', null, param)
        .then(resp=>{
            if(resp.data === "YES"){
                alert('가입신청이 완료되었습니다 승인기다려주세요')

                this.$emit('go-login');
            }else{
                alert('회원가입 실패')
            }
        })
        .catch(err=>{
            alert(err);
        })
    }
  }
}
</script>

<style scoped>
.join-bg {
  position: fixed;
  inset: 0;
  background: url("../assets/background.png") center / cover no-repeat;
  display: flex;
  justify-content: center;
  align-items: center;
}

.join-card {
  width: 380px;
  background: linear-gradient(180deg, #3b5bff 0%, #2436b8 100%);
  border-radius: 12px;
  padding: 5px 20px 26px;
  color: #fff;
  box-shadow: 0 20px 40px rgba(0,0,0,0.45);
}

.logo-row {
  align-items: center;

}

.logo-img {
  width: 300px;
  height: auto;
  margin-left: 25px;
}


.logo-title { font-size: 18px; font-weight: 700; }
.logo-sub { font-size: 11px; opacity: 0.85; }

.join-title {
  text-align: center;
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 16px;
}

.input-box {
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
  color: #333;
}

.input-box.row {
  display: flex;
  gap: 6px;
}

.check-btn {
  font-size: 12px;
  border: none;
  background: #dfe6ff;
  border-radius: 6px;
  padding: 0 10px;
  cursor: pointer;
  white-space: nowrap;
}

.section-title {
  font-size: 13px;
  margin: 14px 0 6px;
}

.join-btn {
  width: 100%;
  height: 42px;
  margin-top: 18px;
  background: linear-gradient(180deg, #5f8bff 0%, #3f6bff 100%);
  border: none;
  border-radius: 8px;
  color: #fff;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
}
</style>