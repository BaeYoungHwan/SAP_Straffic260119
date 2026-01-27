<template>
  <div class="home-container">
    <section class="welcome-banner">
      <div class="banner-text">
        <h1>안녕하세요, 관리자님! 👋</h1>
        <p>오늘도 프로젝트 현황을 한눈에 확인하고 관리해 보세요.</p>
      </div>
      <button class="primary-btn">리포트 다운로드</button>
    </section>

    <div class="stats-grid">
      <div class="stat-card blue">
        <div class="icon">👥</div>
        <div class="info">
          <span>전체 사용자</span>
          <h3>1,240명</h3>
        </div>
      </div>
      <div class="stat-card green">
        <div class="icon">📈</div>
        <div class="info">
          <span>일간 방문자</span>
          <h3>458명</h3>
        </div>
      </div>
      <div class="stat-card orange">
        <div class="icon">💰</div>
        <div class="info">
          <span>이번 달 매출</span>
          <h3>₩2,450,000</h3>
        </div>
      </div>
    </div>

    <div class="details-section">
      <div class="recent-activity">
        <h3>최근 활동</h3>
        <ul>
          <li><span>✅</span> 신규 사용자 '김철수'님이 가입했습니다. <small>2분 전</small></li>
          <li><span>📝</span> 시스템 보안 업데이트가 완료되었습니다. <small>1시간 전</small></li>
          <li><span>⚠️</span> 서버 응답 속도가 지연되고 있습니다. <small>3시간 전</small></li>
        </ul>
      </div>
      <div class="quick-links">
        <h3>바로가기</h3>
        <div class="link-buttons">
          <router-link to="/dashboard" class="link-item">📊 통계 확인</router-link>
          <router-link to="/settings" class="link-item">⚙️ 설정 변경</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.home-container {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

/* 배너 */
.welcome-banner {
  background: linear-gradient(135deg, #42b983 0%, #35495e 100%);
  padding: 40px;
  border-radius: 16px;
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.banner-text h1 { font-size: 2rem; margin-bottom: 10px; }
.banner-text p { opacity: 0.9; }

.primary-btn {
  background: white;
  color: #2c3e50;
  border: none;
  padding: 12px 24px;
  border-radius: 8px;
  font-weight: bold;
  cursor: pointer;
  transition: transform 0.2s;
}
.primary-btn:hover { transform: translateY(-2px); }

/* 통계 카드 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
}

.stat-card {
  background: white;
  padding: 25px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  gap: 20px;
  box-shadow: 0 4px 6px rgba(0,0,0,0.05);
  border: 1px solid #eee;
}

.stat-card .icon { font-size: 2.5rem; }
.stat-card .info span { color: #666; font-size: 0.9rem; }
.stat-card .info h3 { font-size: 1.5rem; margin-top: 5px; }

/* 하단 섹션 */
.details-section {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 20px;
}

.recent-activity, .quick-links {
  background: white;
  padding: 25px;
  border-radius: 12px;
  border: 1px solid #eee;
}

.recent-activity ul { list-style: none; margin-top: 15px; }
.recent-activity li {
  padding: 12px 0;
  border-bottom: 1px solid #f9f9f9;
  font-size: 0.95rem;
  display: flex;
  justify-content: space-between;
}
.recent-activity small { color: #999; }

.link-buttons {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-top: 15px;
}

.link-item {
  text-decoration: none;
  background: #f8f9fa;
  padding: 15px;
  border-radius: 8px;
  color: #2c3e50;
  text-align: center;
  font-weight: 500;
  transition: 0.2s;
}
.link-item:hover { background: #e9ecef; }

@media (max-width: 768px) {
  .details-section { grid-template-columns: 1fr; }
  .welcome-banner { flex-direction: column; text-align: center; gap: 20px; }
}
</style>