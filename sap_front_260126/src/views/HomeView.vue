<template>
  <div class="dashboard-container">
    <header class="dashboard-header">
      <h1>{{ linetitle }}</h1>
    </header>

    <section class="main-summary-bar">
      <div class="summary-item main-info">
        <span class="item-label">운영 현황 요약</span>
        <div class="item-content">
          <template v-if="userAuth === '1'">
            <p>노선 : <span class="num highlight">{{ linetotal }}</span> 개</p>
            <p>전체 역 : <span class="num highlight">{{ stationtotal }}</span> 개</p>
          </template>
          <template v-else>
            <p>{{ userLine }}호선 역 개수 : <span class="num highlight">{{ line_stationcount }}</span> 개</p>
            <p>전체 역 개수 : <span class="num highlight">{{ stationtotal }}</span> 개</p>
          </template>
        </div>
      </div>

      <div class="v-divider"></div>

      <div class="summary-item">
        <span class="item-label">물품보관함 이용 현황</span>
        <div class="chart-box big-chart">
          <canvas id="usedStorageChart"></canvas>
        </div>
        <div class="chart-bottom-text big-text">
          <span class="used-color">{{ used_lockers.toLocaleString() }}</span>
          <span class="total-slash"> / {{ total_lockers.toLocaleString() }} 개</span>
        </div>
      </div>

      <div class="v-divider"></div>

      <div class="summary-item incident-widget" @click="goBackToList">
        <span class="item-label">장애 / 이슈 현황</span>
        <div class="issue-box-vertical large">
          <img src="@/assets/경고표시.png" alt="경고" class="issue-icon-big" />
          <div class="issue-text-group">
            <span class="error-count-big">{{ incident_count }}</span>
            <span class="unit-big">건</span>
          </div>
        </div>
      </div>
      
      <div class="v-divider"></div>

      <div class="summary-item weather-widget" @click="goWeatherDetail">
        <span class="item-label">오늘의 날씨</span>
        <div class="weather-content" v-if="seoulWeather.tm">
          <div class="weather-top">
            <span class="weather-icon">{{ weatherIcon }}</span>
            <span class="weather-temp">{{ seoulWeather.ta }}°</span>
          </div>
          <div class="weather-middle">
            <span class="weather-desc">{{ weatherStatus }}</span>
            <span class="slash">/</span>
            <span class="feels-like">체감 {{ feelsLikeTemp }}°</span>
          </div>
          <div class="weather-bottom">
            <span>{{ windDirText }} {{ seoulWeather.ws }}m/s</span>
            <span class="dot">•</span>
            <span>습도 {{ seoulWeather.hm === -9.0 ? 0 : seoulWeather.hm }}%</span>
            <span class="dot">•</span>
            <span>강수 {{ (seoulWeather.rn <= 0 || seoulWeather.rn === -9.0) ? '0mm' : seoulWeather.rn + 'mm' }}</span>
          </div>
          <div class="weather-time">
            서울기상관측소 | {{ seoulWeather.tm.substring(8, 10) }}:{{ seoulWeather.tm.substring(10, 12) }} 관측
          </div>
        </div>
      </div>

      <div class="v-divider"></div>

      <div class="summary-item contact-section">
        <span class="item-label">비상 연락망</span>
        <ul class="contact-list-big">
          <li><strong>서울교통공사 </strong> 1577-1234</li>
          <li><strong>코레일</strong> 1544-7788</li>
          <li><strong>서울시메트로9</strong> 1544-4009</li>
          <li><strong>네오트랜스</strong> 031-8018-7777</li>
          <li><strong>공항철도</strong> 1544-7769</li>
          <li><strong>인천교통공사</strong> 032-451-2114</li>
          <li><strong>우이신설선경</strong> 02-1660-1555</li>
        </ul>
      </div>
    </section>

    <div class="middle-widgets">
      <div class="card">
        <h3>실시간 이용률 현황</h3>
        <div class="canvas-wrapper">
          <canvas id="hourlyChart"></canvas>
        </div>
      </div>
      
      <div class="card">
        <h3>장애발생 현황 (최근 7일)</h3>
        <div class="canvas-wrapper">
          <canvas id="weeklyChart"></canvas>
          <div v-if="!hasWeeklyData" class="no-data-overlay">데이터 집계 대기 중</div>
        </div>
      </div>

      <div class="card clickable-card" @click="goTrainInfo">
        <h3>실시간 지하철 정보 <span v-if="userAuth === '2'">({{ userLine }}호선)</span><span v-else>(전체)</span></h3>
        <div class="image-display-container">
          <img :src="getLineImage()" alt="노선도 이미지" class="route-map-img-fixed" />
          <div class="hover-info">클릭 시 실시간 위치 페이지로 이동</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick } from 'vue';
import axios from 'axios';
import Chart from 'chart.js/auto';
import ChartDataLabels from 'chartjs-plugin-datalabels';

const emit = defineEmits(['go-list']);
const dashboardData = ref(null);
const userLine = ref(""); 
const userAuth = ref("");
const hasWeeklyData = ref(false); 
const seoulWeather = ref({ tm: '', wd: 0, ws: 0, ta: 0, hm: 0, rn: 0 });

let lockerChart = null;
let hourlyChart = null;
let weeklyChartInstance = null;
let refreshTimer = null;

const fetchData = async () => {
  try {
    // 1. 세션에서 현재 로그인된 정보를 '항상' 새로 긁어옵니다. (205 고정 방지)
    const sessionData = sessionStorage.getItem("user_info") || sessionStorage.getItem("login");
    if (!sessionData) return;
    
    const login = JSON.parse(sessionData);
    userAuth.value = String(login.auth);

    // 2. 파라미터에 세션의 최신 station_id를 넣습니다.
    const res = await axios.get('http://localhost:9000/get_dashboard', { 
      params: { 
        auth: userAuth.value, 
        station_id: login.station_id, // 여기가 핵심
        user_id: login.user_id 
      } 
    });

    if (res.data) {
      dashboardData.value = res.data;
      
      // 날씨 데이터 갱신
      seoulWeather.value = {
        tm: res.data.weather_tm || '', ta: res.data.weather_ta || 0, 
        ws: res.data.weather_ws || 0, hm: res.data.weather_hm || 0, 
        rn: res.data.weather_rn || 0, wd: res.data.weather_wd || 0
      };
      
      // 호선 이미지용 라인 번호 추출
      if (userAuth.value === '2') {
        const sid = String(res.data.station_id || login.station_id);
        userLine.value = sid.replace(/[^0-9]/g, '').charAt(0);
      }
      
      await nextTick();
      initCharts(); // 새 데이터로 그래프 재생성
    }
  } catch (err) {
    console.error("데이터 로드 실패:", err);
  }
};

const initCharts = () => {
  // 기존 차트 객체들을 메모리에서 완전히 지웁니다.
  if (lockerChart) lockerChart.destroy();
  if (hourlyChart) hourlyChart.destroy();
  if (weeklyChartInstance) weeklyChartInstance.destroy();

  const data = dashboardData.value;
  if (!data) return;

  // 1. 물품보관함
  const ctxUsed = document.getElementById('usedStorageChart')?.getContext('2d');
  if (ctxUsed) {
    const t = Number(data.total_lockers || 0);
    const u = Number(data.used_lockers || 0);
    lockerChart = new Chart(ctxUsed, { 
      type: 'doughnut', 
      data: { datasets: [{ data: t === 0 ? [0, 1] : [u, t - u], backgroundColor: ['#36A2EB', '#f2f2f2'], borderWidth: 0 }] },
      options: { cutout: '70%', responsive: true, maintainAspectRatio: false, plugins: { legend: { display: false } } }
    });
  }

  // 2. 시간대별 이용률
  const ctxHourly = document.getElementById('hourlyChart')?.getContext('2d');
  if (ctxHourly) {
    const labels = ['08시', '11시', '14시', '17시', '20시', '23시'];
    const rawRate = Number(data.hour_usedlocker || 0);
    const fallback = [rawRate * 0.8, rawRate * 0.9, rawRate, rawRate * 1.1, rawRate * 1.05, rawRate];
    hourlyChart = new Chart(ctxHourly, { 
      type: 'line', 
      data: { labels, datasets: [{ label: '이용률', data: fallback, borderColor: '#36A2EB', backgroundColor: 'rgba(54, 162, 235, 0.2)', fill: true, tension: 0.4 }] }, 
      plugins: [ChartDataLabels],
      options: { responsive: true, maintainAspectRatio: false, scales: { y: { beginAtZero: true, max: 100 } }, plugins: { datalabels: { align: 'top', formatter: (v) => Math.round(v) + '%' } } } 
    });
  }

  // 3. 주간 장애 (현재 station_id 필터링 적용)
  const ctxWeekly = document.getElementById('weeklyChart')?.getContext('2d');
  const weeklyData = data.weekly_issue || [];
  if (ctxWeekly) {
    hasWeeklyData.value = true;
    const labels = [];
    const matchKeys = [];
    for (let i = 6; i >= 0; i--) {
      const d = new Date(); d.setDate(d.getDate() - i);
      const yyyy = d.getFullYear();
      const mm = String(d.getMonth() + 1).padStart(2, '0');
      const dd = String(d.getDate()).padStart(2, '0');
      labels.push(`${mm}/${dd}`);
      matchKeys.push(`${yyyy}-${mm}-${dd}`);
    }

    const datasets = [];
    const currentId = String(data.station_id);
    const currentLn = currentId.replace(/[^0-9]/g, '').charAt(0);

    if (userAuth.value === '1') {
      datasets.push({
        type: 'bar', label: '전체 합계', backgroundColor: '#E0E0E0', order: 2,
        data: matchKeys.map(k => weeklyData.filter(d => (d.event_date || "").startsWith(k)).reduce((a, c) => a + Number(c.count || 0), 0))
      });
      ['1','2','3','4','5','6','7','8','9'].forEach(ln => {
        datasets.push({
          type: 'line', label: `${ln}호선`, borderColor: getLineColor(ln), tension: 0.3, order: 1, borderWidth: 2,
          data: matchKeys.map(k => weeklyData.filter(d => (d.event_date || "").startsWith(k) && String(d.line_num || d.line_name).includes(ln)).reduce((a, c) => a + Number(c.count || 0), 0))
        });
      });
    } else {
      datasets.push({
        type: 'bar', label: `${currentLn}호선 전체`, backgroundColor: getLineColor(currentLn) + '80', order: 2,
        data: matchKeys.map(k => weeklyData.filter(d => (d.event_date || "").startsWith(k) && String(d.line_num || d.line_name).includes(currentLn)).reduce((sum, curr) => sum + Number(curr.count || 0), 0))
      });
      datasets.push({
        type: 'line', label: '본인 역', borderColor: '#FF6384', borderWidth: 4, tension: 0.3, order: 1,
        data: matchKeys.map(k => weeklyData.filter(d => (d.event_date || "").startsWith(k) && String(d.station_id) === currentId).reduce((sum, curr) => sum + Number(curr.count || 0), 0))
      });
    }

    weeklyChartInstance = new Chart(ctxWeekly, {
      data: { labels, datasets },
      options: { responsive: true, maintainAspectRatio: false, scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } }, plugins: { legend: { display: true } } }
    });
  }
};

const getLineColor = (ln) => ({ '1': '#0052A4', '2': '#00A84D', '3': '#EF7C1C', '4': '#00A4E3', '5': '#996CAC', '6': '#CD7C2F', '7': '#747F00', '8': '#E6186C', '9': '#BB8336' }[ln] || '#888');

// Computed 원본 유지
const linetitle = computed(() => dashboardData.value?.linetitle || '운영 현황');
const linetotal = computed(() => Number(dashboardData.value?.linetotal || 0));
const stationtotal = computed(() => Number(dashboardData.value?.stationtotal || 0));
const line_stationcount = computed(() => Number(dashboardData.value?.line_stationcount || 0));
const total_lockers = computed(() => Number(dashboardData.value?.total_lockers || 0));
const used_lockers = computed(() => Number(dashboardData.value?.used_lockers || 0));
const incident_count = computed(() => Number(dashboardData.value?.incident_count || 0));

const windDirText = computed(() => {
  const wd = Number(seoulWeather.value.wd);
  const directions = ['북', '북동', '동', '남동', '남', '남서', '서', '북서', '북'];
  return directions[Math.round(wd / 4.5) % 8] + '풍';
});
const feelsLikeTemp = computed(() => {
  const t = seoulWeather.value.ta, v = seoulWeather.value.ws;
  return (13.12 + (0.6215 * t) - (11.37 * Math.pow(v, 0.16)) + (0.3965 * t * Math.pow(v, 0.16))).toFixed(1);
});
const weatherStatus = computed(() => (seoulWeather.value.rn > 0) ? '비/눈' : (seoulWeather.value.hm > 70 ? '흐림' : '맑음'));
const weatherIcon = computed(() => (seoulWeather.value.rn > 0) ? '☔' : (seoulWeather.value.hm > 70 ? '☁️' : '☀️'));

const getLineImage = () => {
  let fileName = (userAuth.value === "2" && userLine.value) ? `Line${userLine.value}.png` : "Lineall.png";
  return new URL(`../assets/Line/${fileName}`, import.meta.url).href;
};

const goBackToList = () => emit('go-list');
const goTrainInfo = () => window.open("https://smss.seoulmetro.co.kr/traininfo/traininfoUserView.do", '_blank');
const goWeatherDetail = () => window.open("https://weather.naver.com/map/", '_blank');

onMounted(() => { 
  fetchData(); 
  refreshTimer = setInterval(fetchData, 30000); 
});

onUnmounted(() => { if (refreshTimer) clearInterval(refreshTimer); });
</script>

<style scoped>
.dashboard-container { padding: 30px; background: #f4f7fa; min-height: 100vh; font-family: 'Pretendard', sans-serif; }
.dashboard-header { margin-bottom: 25px; padding-left: 10px; }
.dashboard-header h1 { font-size: 28px; font-weight: 800; color: #1a2a3a; margin: 0; }
.main-summary-bar { display: flex; align-items: center; background: #fff; padding: 25px; border-radius: 15px; box-shadow: 0 8px 25px rgba(0,0,0,0.06); gap: 5px; margin-bottom: 30px; }
.summary-item { flex: 1; display: flex; flex-direction: column; align-items: center; text-align: center; }
.v-divider { width: 1.5px; height: 130px; background: #eee; margin: 0 5px; }
.middle-widgets { display: grid; grid-template-columns: repeat(3, 1fr); gap: 25px; }
.card { background: #fff; padding: 25px; border-radius: 18px; height: 480px; box-shadow: 0 5px 15px rgba(0,0,0,0.04); display: flex; flex-direction: column; }
.card h3 { font-size: 19px; font-weight: 700; margin-bottom: 20px; color: #333; border-left: 5px solid #36A2EB; padding-left: 12px; }
.canvas-wrapper { height: 360px; width: 100%; position: relative; }
.incident-widget, .weather-widget { cursor: pointer; transition: transform 0.2s; }
.incident-widget:hover, .weather-widget:hover { transform: translateY(-3px); }
.weather-content { display: flex; flex-direction: column; align-items: center; }
.weather-top { display: flex; align-items: center; gap: 8px; }
.weather-icon { font-size: 32px; }
.weather-temp { font-size: 36px; font-weight: 800; color: #222; }
.weather-middle { margin-top: 2px; font-size: 15px; color: #444; font-weight: 600; display: flex; align-items: center; gap: 6px; }
.feels-like { color: #36A2EB; }
.slash { color: #ddd; }
.weather-bottom { margin-top: 8px; font-size: 12.5px; color: #777; display: flex; align-items: center; gap: 4px; }
.dot { font-size: 8px; color: #ccc; }
.weather-time { margin-top: 6px; font-size: 11px; color: #999; font-weight: 500; }
.item-label { font-size: 18px; font-weight: 700; color: #1a2a3a; margin-bottom: 12px; }
.num.highlight { font-size: 24px; font-weight: 800; color: #1a2a3a; }
.item-content p { font-size: 16px; margin: 4px 0; color: #444; }
.chart-box.big-chart { width: 110px; height: 110px; margin-bottom: 8px; position: relative; }
.chart-bottom-text.big-text { font-size: 20px; font-weight: 800; color: #222; }
.used-color { color: #36A2EB; font-size: 22px; }
.total-slash { font-size: 16px; color: #888; font-weight: 500; }
.issue-box-vertical.large { display: flex; flex-direction: column; align-items: center; justify-content: center; flex: 1; width: 100%; }
.issue-icon-big { width: 120px; height: auto; margin-bottom: 5px; }
.error-count-big { font-size: 25px; font-weight: 900; color: #d32f2f; line-height: 1; }
.unit-big { font-size: 20px; font-weight: 700; color: #d32f2f; margin-left: 4px; }
.contact-list-big { list-style: none; padding: 0; font-size: 13.5px; line-height: 1.5; text-align: left; }
.image-display-container { display: flex; justify-content: center; align-items: center; cursor: pointer; flex: 1; position: relative; overflow: hidden; border-radius: 12px; border: 1px solid #eee; }
.route-map-img-fixed { width: 100%; height: 100%; object-fit: cover; }
.hover-info { position: absolute; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.4); color: #fff; display: flex; align-items: center; justify-content: center; opacity: 0; transition: 0.3s; font-weight: bold; }
.image-display-container:hover .hover-info { opacity: 1; }
.no-data-overlay { position: absolute; top: 0; left: 0; width: 100%; height: 100%; background: rgba(249, 249, 249, 0.9); display: flex; align-items: center; justify-content: center; color: #aaa; font-size: 14px; border-radius: 12px; z-index: 5; }
</style>