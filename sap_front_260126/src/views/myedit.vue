<template>
  <div class="mypage-wrapper">
    <div class="mypage-header">
      <div class="header-content">
        <h2 class="title">정보 수정</h2>
        <div class="btn-group">
          <button class="save-btn" @click="update_user">저장하기</button>
          <button class="cancel-btn" @click="$emit('change-view', 'my')">취소</button>
        </div>
      </div>
    </div>

    <div class="info-section">
      <div class="section-top">
        <p class="section-desc">보안을 위해 비밀번호 변경 시 현재 비밀번호 확인이 필요합니다.</p>
      </div>
      
      <div class="info-table">
        <div class="table-row">
          <div class="table-label">
            <span class="bullet">●</span> 이름
          </div>
          <div class="table-value">
            <input type="text" class="edit-input readonly" v-model="user_name" readonly />
          </div>
        </div>

        <div class="table-row">
          <div class="table-label">
            <span class="bullet">●</span> 아이디(ID)
          </div>
          <div class="table-value">
            <input type="text" class="edit-input readonly" v-model="user_id" readonly />
          </div>
        </div>

        <div class="table-row">
          <div class="table-label highlight-label">
            <span class="bullet">●</span> 현재 PW 확인
          </div>
          <div class="table-value">
            <input type="password" class="edit-input" v-model="current_pw" placeholder="정보 변경 시 입력하세요" />
            <span class="input-tip">* 본인 확인을 위해 입력이 필요합니다.</span>
          </div>
        </div>

        <div class="table-row" v-if="current_pw.trim() !== ''">
          <div class="table-label">
            <span class="bullet">●</span> 새 비밀번호
          </div>
          <div class="table-value">
            <div class="pw-input-wrapper">
              <input type="password" class="edit-input" v-model="user_pw" placeholder="새 비밀번호 입력" />
              <p class="pw-guide">* 8~16자 이내, 영문/숫자/특수문자 조합 (ID와 동일 불가)</p>
            </div>
          </div>
        </div>

        <div class="table-row">
          <div class="table-label">
            <span class="bullet">●</span> 이메일
          </div>
          <div class="table-value">
            <div class="email-row">
              <input v-model.trim="email_id" type="text" class="edit-input email-id" />
              <span class="at">@</span>
              <div class="email-domain-group">
                <input v-if="email_domain_select === 'direct'" 
                       v-model.trim="email_domain_direct" 
                       type="text" class="edit-input email-domain" placeholder="직접 입력" />
                <select v-else v-model="email_domain_select" class="edit-input email-select">
                  <option value="naver.com">naver.com</option>
                  <option value="gmail.com">gmail.com</option>
                  <option value="daum.net">daum.net</option>
                  <option value="direct">직접 입력</option>
                </select>
                <button v-if="email_domain_select === 'direct'" 
                        @click="email_domain_select = 'naver.com'" 
                        class="reset-domain">✕</button>
              </div>
            </div>
          </div>
        </div>

        <div class="table-row route-row">
          <div class="table-label">
            <span class="bullet">●</span> 담당 노선 수정
          </div>
          <div class="table-value">
            <div class="route-selector-container">
              <div class="current-selection-box">
                <span class="info-tag">현재 선택 :</span>
                <div v-if="selectedSubway.line_name" class="selection-badge">
                  <span class="badge-line">{{ selectedSubway.line_name }}</span>
                  <span class="badge-station">{{ selectedSubway.station_name }}</span>
                </div>
                <span v-else class="no-selection">노선과 역을 선택해주세요.</span>
              </div>
              
              <div class="selector-content">
                <SubwaySelector @update-selection="handleSubwayChange" />
                <p class="selector-tip">* 아래 목록에서 변경할 호선과 역을 클릭하세요.</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import SubwaySelector from './SubwaySelector.vue';

export default {
  components: { SubwaySelector },
  data() {
    return {
      user_id: "",
      user_name: "",
      current_pw: "",
      user_pw: "",
      email_id: "",
      email_domain_select: "naver.com",
      email_domain_direct: "",
      selectedSubway: { line_name: '', station_id: '', station_name: '' },
      originData: null 
    };
  },
  watch: {
    current_pw(newVal) {
      if (newVal.trim() === "") {
        this.user_pw = "";
      }
    }
  },
  computed: {
    fullEmail() {
      const domain = this.email_domain_select === 'direct' 
                     ? this.email_domain_direct 
                     : this.email_domain_select;
      return this.email_id && domain ? `${this.email_id}@${domain}` : '';
    }
  },
  mounted() {
    const sessionData = sessionStorage.getItem("user_info") || sessionStorage.getItem("login");
    const login = JSON.parse(sessionData);

    if (!login) {
      alert("로그인이 필요합니다.");
      this.$emit('change-view', 'Home');
      return;
    }

    this.user_id = login.user_id || login.id || '';
    this.user_name = login.user_name || login.name || '';
    
    const fullEmail = login.user_email || login.email || '';
    if (fullEmail.includes('@')) {
      const parts = fullEmail.split('@');
      this.email_id = parts[0];
      const domain = parts[1];
      const commonDomains = ['naver.com', 'gmail.com', 'daum.net'];
      if (commonDomains.includes(domain)) {
        this.email_domain_select = domain;
      } else {
        this.email_domain_select = 'direct';
        this.email_domain_direct = domain;
      }
    }

    this.selectedSubway.line_name = login.line_name || ''; 
    this.selectedSubway.station_name = login.station_name || '';
    this.selectedSubway.station_id = login.station_id || '';

    this.saveOrigin();
  },
  methods: {
    handleSubwayChange(data) {
      this.selectedSubway.line_name = data.line_name;
      this.selectedSubway.station_id = data.station.station_id;
      this.selectedSubway.station_name = data.station.station_name;
    },
    saveOrigin() {
      this.originData = JSON.stringify({
        email_id: this.email_id,
        email_domain_select: this.email_domain_select,
        email_domain_direct: this.email_domain_direct,
        line_name: this.selectedSubway.line_name,
        station_id: this.selectedSubway.station_id
      });
    },
    async update_user() {
      const currentData = JSON.stringify({
        email_id: this.email_id,
        email_domain_select: this.email_domain_select,
        email_domain_direct: this.email_domain_direct,
        line_name: this.selectedSubway.line_name,
        station_id: this.selectedSubway.station_id
      });

      if (this.user_pw.trim() === "" && this.originData === currentData) {
        alert("수정된 내용이 없습니다.");
        return;
      }

      const validatePassword = (pw, id, currentPw) => {
        if (pw.length < 8 || pw.length > 16) {
          alert("비밀번호는 8자 이상 16자 이하로 설정해주세요.");
          return false;
        }
        if (pw === currentPw) {
          alert("이전 비밀번호와 똑같은 건 비밀번호 변경불가입니다.");
          return false;
        }
        if (pw === id) {
          alert("아이디와 동일한 비밀번호는 사용할 수 없습니다.");
          return false;
        }
        const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;
        if (!passwordRegex.test(pw)) {
          alert("비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.");
          return false;
        }
        return true;
      };

      if (!this.selectedSubway.line_name || this.selectedSubway.line_name === "미선택") {
        alert("노선을 선택해주세요.");
        return;
      }
      if (!this.selectedSubway.station_name || this.selectedSubway.station_name === "미선택") {
        alert("역을 선택해주세요.");
        return;
      }

      const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{3,}$/;
      if (!emailPattern.test(this.fullEmail)) {
        alert("이메일 주소 형식이 잘못되었습니다.");
        return; 
      }

      const updateData = {
        user_id: this.user_id,
        user_email: this.fullEmail,
        line_name: this.selectedSubway.line_name,
        station_id: this.selectedSubway.station_id,
        station_name: this.selectedSubway.station_name,
        passwordChange: false
      };
   
      if (this.user_pw.trim() !== "") {
        if (!this.current_pw.trim()) {
          alert("현재 비밀번호를 입력해주세요.");
          return;
        }
        if (!validatePassword(this.user_pw, this.user_id, this.current_pw)) {
          return;
        }
        updateData.user_pw = this.user_pw;
        updateData.current_pw = this.current_pw;
        updateData.passwordChange = true;
      }

      try {
        const resp = await axios.post('http://localhost:9000/update_user', updateData);
        if (resp.data === "PW_ERROR") {
          alert('현재 비밀번호가 틀렸습니다.');
          this.current_pw = "";
        } else if (resp.data === "YES" || resp.data === 1) {
          alert('수정 완료!');
          if (updateData.passwordChange) {
            sessionStorage.clear();
            window.location.href = "/";
          } else {
            // [연동 핵심] 세션을 업데이트하고 '담당 호선'에 맞게 화면을 새로 고칩니다.
            this.updateSession(updateData);
            this.$emit('change-view', 'my');
            // 사용자가 구현한 대시보드 갱신 로직이 작동하도록 강제 새로고침을 포함합니다.
            setTimeout(() => { window.location.reload(); }, 100);
          }
        }
      } catch (err) {
        console.error(err);
        alert('통신 오류 발생!');
      }
    },
    updateSession(data) {
      const key = sessionStorage.getItem("user_info") ? "user_info" : "login";
      const old = JSON.parse(sessionStorage.getItem(key));
      const updated = { ...old, ...data };
      sessionStorage.setItem(key, JSON.stringify(updated));
    }
  }
}
</script>

<style scoped>
/* 사용자님의 원래 CSS 그대로 유지 */
.mypage-wrapper { width: 100%; min-height: 100%; background-color: #fff; font-family: 'Pretendard', sans-serif; }
.mypage-header { padding: 10px 40px; border-bottom: 1px solid #eef0f3; }
.header-content { display: flex; justify-content: space-between; align-items: center; }
.title { font-size: 35px; font-weight: 700; color: #1a1c23; margin: 0; }
.btn-group { display: flex; gap: 10px; }
.save-btn { background-color: #3f417e; color: #fff; border: none; padding: 10px 30px; border-radius: 4px; font-size: 14px; font-weight: 600; cursor: pointer; }
.cancel-btn { background-color: #f1f3f5; color: #495057; border: none; padding: 10px 30px; border-radius: 4px; font-size: 14px; font-weight: 600; cursor: pointer; }
.info-section { padding: 40px; }
.section-top { margin-bottom: 30px; }
.section-desc { font-size: 14px; color: #888; margin: 0; }
.info-table { border-top: 2px solid #3f417e; }
.table-row { display: flex; align-items: center; border-bottom: 1px solid #edeff2; min-height: 75px; }
.table-label { width: 220px; background-color: #f9fafc; height: 100%; padding: 20px 25px; font-weight: 700; color: #4a5568; display: flex; align-items: center; }
.table-value { padding: 15px 30px; flex: 1; }
.bullet { font-size: 6px; margin-right: 12px; color: #3f417e; }
.highlight-label { color: #e74c3c; }
.edit-input { height: 40px; padding: 0 15px; border: 1px solid #ddd; border-radius: 4px; width: 320px; font-size: 15px; transition: border-color 0.2s; }
.edit-input:focus { border-color: #3f417e; outline: none; }
.edit-input.readonly { background-color: #f5f5f5; color: #888; cursor: not-allowed; border-color: #eee; }
.input-tip { font-size: 12px; color: #999; margin-left: 15px; }
.pw-input-wrapper { display: flex; flex-direction: column; gap: 8px; }
.pw-guide { font-size: 12px; color: #e74c3c; margin: 0; }
.email-row { display: flex; align-items: center; gap: 10px; }
.email-id { width: 180px; }
.at { color: #999; font-weight: 600; }
.email-domain-group { display: flex; align-items: center; gap: 8px; }
.email-select { width: 180px; }
.email-domain { width: 180px; }
.reset-domain { background: #adb5bd; color: #fff; border: none; width: 24px; height: 24px; border-radius: 50%; cursor: pointer; font-size: 12px; line-height: 1; }
.route-row { align-items: flex-start; }
.route-selector-container { display: flex; flex-direction: column; gap: 20px; width: 100%; max-width: 700px; }
.current-selection-box { display: flex; align-items: center; gap: 15px; padding: 15px; background: #f8f9fa; border-radius: 6px; border: 1px dashed #ced4da; }
.info-tag { font-size: 14px; font-weight: 600; color: #666; }
.selection-badge { display: inline-flex; border-radius: 4px; overflow: hidden; border: 1px solid #3f417e; }
.badge-line { background: #3f417e; color: #fff; padding: 4px 12px; font-size: 13px; font-weight: 700; }
.badge-station { background: #fff; color: #3f417e; padding: 4px 15px; font-size: 14px; font-weight: 700; }
.selector-content { padding: 10px 0; }
.selector-tip { font-size: 13px; color: #3498db; margin-top: 10px; font-weight: 500; }
</style>