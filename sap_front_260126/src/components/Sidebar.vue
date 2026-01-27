<template>
  <nav class="sidebar-nav">
    <ul>
      <li><router-link to="/">홈으로</router-link></li>
      <li><router-link to="/dashboard">대시보드</router-link></li>
      <li><router-link to="/about">정보</router-link></li>
    </ul>
  </nav>
</template>

<style scoped>
.sidebar-nav ul {
  list-style: none;
  padding: 20px;
}
.sidebar-nav li {
  margin-bottom: 15px;
}
.router-link-active {
  font-weight: bold;
  color: #42b983; /* 현재 활성화된 메뉴 강조 */
}
</style>