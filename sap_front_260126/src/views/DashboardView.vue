<template>
  <div class="dashboard-page">
    <h2>📊 대시보드</h2>
    <p>프로젝트의 통계 데이터를 확인하는 공간입니다.</p>
    
    <div class="card-grid">
      <div class="card">
        <h4>방문자 수</h4>
        <p>1,234명</p>
      </div>
      <div class="card">
        <h4>새 메시지</h4>
        <p>5건</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.dashboard-page {
  padding: 20px;
}
.card-grid {
  display: flex;
  gap: 20px;
  margin-top: 20px;
}
.card {
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background: #f9f9f9;
  flex: 1;
}
</style>