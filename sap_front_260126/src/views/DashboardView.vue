<template>
  <div class="status-container">
    <SubwaySearch @search="handleSearch" />

    <div class="category-filter-card">
      <div class="filter-header">
        <p class="filter-title">항목 필터 설정 <span class="sub-text">(원하지 않는 항목은 체크를 해제하세요)</span></p>
        <button @click="toggleAll" class="all-select-btn">
          {{ isAllSelected ? '전체 해제' : '전체 선택' }}
        </button>
      </div>
      
      <div class="checkbox-group">
        <label v-for="cat in categories" :key="cat.key" class="check-label">
          <input 
            type="checkbox" 
            :value="cat.key" 
            v-model="selectedCategories"
            @change="updateAllState"
          >
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
            <td :colspan="columnCount" class="no-data">
              역 또는 호선을 선택하여 현황을 조회해 주세요.
            </td>
          </tr>

          <tr v-for="item in displayList" :key="item.station_id + item.line_name">
            <td class="bold">{{ item.station_name }}</td>
            <td><span class="line-badge">{{ item.line_name }}</span></td>
            
            <td v-if="isVisible('incident_count')">{{ item.incident_count }}건</td>
            <td v-if="isVisible('lockers')">
              <span class="blue-text">{{ item.used_lockers }}</span> / {{ item.total_lockers }}
            </td>

            <td v-if="isVisible('elevator')">{{ item.elevator }}</td>
            <td v-if="isVisible('wheelchairlift')">{{ item.wheelchairlift }}</td>
            <td v-if="isVisible('parking')">{{ item.parking }}</td>
            <td v-if="isVisible('complaint')">{{ item.complaint }}</td>
            <td v-if="isVisible('exchange')">{{ item.exchange }}</td>
            <td v-if="isVisible('trainreservation')">{{ item.trainreservation }}</td>
            <td v-if="isVisible('culturalspace')">{{ item.culturalspace }}</td>
            <td v-if="isVisible('meeting')">{{ item.meeting }}</td>
            <td v-if="isVisible('lactation')">{{ item.lactation }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import axios from 'axios';
import SubwaySearch from './SubwaySearch.vue';

// 1. 모든 카테고리 정의
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

// 2. ⭐ 페이지 로드 시 전체 체크되도록 모든 key를 배열에 기본값으로 설정
const selectedCategories = ref(categories.map(cat => cat.key));

// 3. 전체 선택 버튼 상태도 초기에는 true
const isAllSelected = ref(true);

const displayList = ref([]);

// 필터 노출 로직
const isVisible = (key) => selectedCategories.value.includes(key);

// 동적 colspan 계산 (역명, 호선 기본 2개 + 선택된 카테고리 수)
const columnCount = computed(() => selectedCategories.value.length + 2);

// 전체 선택/해제 토글
const toggleAll = () => {
  if (isAllSelected.value) {
    selectedCategories.value = [];
  } else {
    selectedCategories.value = categories.map(c => c.key);
  }
  isAllSelected.value = !isAllSelected.value;
};

// 개별 체크박스 변경 시 전체 선택 상태 동기화
const updateAllState = () => {
  isAllSelected.value = selectedCategories.value.length === categories.length;
};

// 검색 처리
const handleSearch = async (searchData) => {
  try {
    const params = {
      station_id: searchData.type === 'station' ? searchData.station_id : null,
      line_name: searchData.type === 'line' ? searchData.line_name : null
    };

    const res = await axios.get('http://localhost:9000/getstatus', { params });
    displayList.value = res.data;
  } catch (err) {
    console.error("데이터 조회 중 오류 발생:", err);
  }
};
</script>

<style scoped>
.status-container { padding: 30px; background-color: #f5f7f9; min-height: 100vh; }

.category-filter-card { 
  background: white; padding: 20px; border-radius: 12px; 
  margin-bottom: 25px; box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}
.filter-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; }
.filter-title { font-weight: bold; color: #333; }
.sub-text { font-size: 12px; color: #888; margin-left: 8px; font-weight: normal; }

.all-select-btn {
  padding: 6px 15px; font-size: 12px; background: #fff; border: 1px solid #dcdfe6;
  border-radius: 6px; cursor: pointer; transition: 0.2s;
}
.all-select-btn:hover { background: #007bff; color: white; border-color: #007bff; }

.checkbox-group { display: flex; flex-wrap: wrap; gap: 15px; }
.check-label { cursor: pointer; font-size: 14px; display: flex; align-items: center; gap: 6px; }

.table-wrapper { background: white; border-radius: 12px; overflow-x: auto; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }
.status-table { width: 100%; border-collapse: collapse; min-width: 1000px; }
.status-table th { background: #f8f9fa; padding: 15px; font-size: 13px; color: #666; border-bottom: 2px solid #edf0f2; }
.status-table td { padding: 15px; text-align: center; border-bottom: 1px solid #f1f1f1; font-size: 14px; }

.line-badge { background: #eef6ff; color: #007bff; padding: 4px 10px; border-radius: 20px; font-size: 12px; font-weight: bold; }
.bold { font-weight: bold; color: #222; }
.blue-text { color: #007bff; font-weight: bold; }
.no-data { padding: 100px 0; color: #aaa; text-align: center; font-size: 15px; }
</style>