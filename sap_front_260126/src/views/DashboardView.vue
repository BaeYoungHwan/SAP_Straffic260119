<template>
  <div class="status-container">

    <SubwaySearch @search="handleSearch" />

    <div class="category-filter-card">
      <p class="filter-title">항목 필터 설정 <span class="sub-text">(체크박스를 선택하면 해당 컬럼만 노출됩니다)</span></p>
      <div class="checkbox-group">
        <label v-for="cat in categories" :key="cat.key" class="check-label">
          <input type="checkbox" :value="cat.key" v-model="selectedCategories">
          {{ cat.label }}
        </label>
      </div>
    </div>

    <div class="table-wrapper">
      <table class="status-table">
        <thead>
          <tr>
            <th>역명</th>
            <th>호선</th>
            <th v-if="isVisible('incident_count')">장애발생건수</th>
            <th v-if="isVisible('lockers')">물품보관함 (사용/설치)</th>
            <th v-if="isVisible('elevator')">엘리베이터</th>
            <th v-if="isVisible('wheelchairlift')">휠체어리프트</th>
            <th v-if="isVisible('parking')">환승주차장</th>
            <th v-if="isVisible('complaint')">무인민원발급기</th>
            <th v-if="isVisible('exchange')">환전키오스크</th>
            <th v-if="isVisible('trainreservation')">기차예매역</th>
            <th v-if="isVisible('culturalspace')">문화공간</th>
            <th v-if="isVisible('meeting')">만남의장소</th>
            <th v-if="isVisible('lactation')">유아 수유방</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="displayList.length === 0">
            <td colspan="15" class="no-data">상단에서 역을 검색하여 현황을 조회해 주세요.</td>
          </tr>

          <tr v-for="item in displayList" :key="item.station_id + item.line_name">
            <td class="bold">{{ item.station_name }}</td>
            <td>{{ item.line_name }}</td>
            <td v-if="isVisible('incident_count')">{{ item.incident_count }}건</td>
            
            <td v-if="isVisible('lockers')">
              <span class="blue-text">{{ item.used_lockers }}</span> / {{ item.total_lockers }}
            </td>

            <td v-if="isVisible('elevator')">{{ formatYN(item.elevator) }}</td>
            <td v-if="isVisible('wheelchairlift')">{{ formatYN(item.wheelchairlift) }}</td>
            <td v-if="isVisible('parking')">{{ formatYN(item.parking) }}</td>
            <td v-if="isVisible('complaint')">{{ formatYN(item.complaint) }}</td>
            <td v-if="isVisible('exchange')">{{ formatYN(item.exchange) }}</td>
            <td v-if="isVisible('trainreservation')">{{ formatYN(item.trainreservation) }}</td>
            <td v-if="isVisible('culturalspace')">{{ formatYN(item.culturalspace) }}</td>
            <td v-if="isVisible('meeting')">{{ formatYN(item.meeting) }}</td>
            <td v-if="isVisible('lactation')">{{ formatYN(item.lactation) }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import SubwaySearch from './SubwaySearch.vue'; // 검색 컴포넌트 임포트 [cite: 2026-01-29]

// 1. 카테고리 정의 (10종) [cite: 2026-01-29]
const categories = [
  { key: 'incident_count', label: '장애발생건수' },
  { key: 'lockers', label: '물품보관함' },
  { key: 'elevator', label: '엘리베이터' },
  { key: 'wheelchairlift', label: '휠체어리프트' },
  { key: 'parking', label: '환승주차장' },
  { key: 'complaint', label: '무인민원발급기' },
  { key: 'exchange', label: '환전키오스크' },
  { key: 'trainreservation', label: '기차예매역' },
  { key: 'culturalspace', label: '문화공간' },
  { key: 'meeting', label: '만남의장소' },
  { key: 'lactation', label: '유아 수유방' }
];

const selectedCategories = ref([]); // 선택된 체크박스 키 [cite: 2026-01-29]
const displayList = ref([]);       // 테이블 데이터 리스트 [cite: 2026-01-29]

// 2. 검색 이벤트 처리 (subwaysearch.vue에서 전달받음) [cite: 2026-01-29]
const handleSearch = async (stationInfo) => {
  // Proxy 객체에서 실제 데이터만 뽑아옵니다.
  const targetId = stationInfo.station_id;
  console.log("실제 보낼 station_id:", targetId); 

  try {
    const response = await axios.get('http://localhost:9000/getstatus', {
      params: { 
        auth: '1', 
        station_id: targetId  // stationInfo 대신 targetId를 직접 넣으세요.
      }
    });
    displayList.value = response.data;
  } catch (error) {
    console.error("조회 실패:", error);
  }
};
// 3. 필터링 로직: 선택된 게 없으면 전체 노출, 있으면 포함된 것만 [cite: 2026-01-29]
const isVisible = (key) => {
  return selectedCategories.value.length === 0 || selectedCategories.value.includes(key);
};

// 4. Y/N -> O/X 변환 함수 [cite: 2026-01-29]
const formatYN = (val) => {
  return val === 'Y' ? 'O' : 'X';
};
</script>

<style scoped>
.status-container { padding: 30px; background-color: #f5f7f9; min-height: 100vh; }
h2 { margin-bottom: 25px; color: #2c3e50; }

/* 카테고리 필터 스타일 */
.category-filter-card { 
  background: white; padding: 20px; border-radius: 12px; 
  margin-bottom: 20px; box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}
.filter-title { font-weight: bold; margin-bottom: 15px; }
.sub-text { font-size: 12px; color: #888; font-weight: normal; }
.checkbox-group { display: flex; flex-wrap: wrap; gap: 15px; }
.check-label { cursor: pointer; font-size: 14px; display: flex; align-items: center; gap: 5px; }

/* 테이블 스타일 */
.table-wrapper { 
  background: white; border-radius: 12px; overflow-x: auto; 
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}
.status-table { width: 100%; border-collapse: collapse; min-width: 1000px; }
.status-table th { 
  background: #f8f9fa; padding: 15px; font-size: 13px; color: #666; 
  border-bottom: 2px solid #edf0f2; text-align: center;
}
.status-table td { 
  padding: 15px; text-align: center; border-bottom: 1px solid #f1f1f1; 
  font-size: 14px; color: #333;
}
.bold { font-weight: bold; }
.blue-text { color: #007bff; font-weight: bold; }
.no-data { padding: 60px 0; color: #999; }
</style>