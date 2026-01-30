<template>
  <div class="status-container">
    <SubwaySearch @search="handleSearch" />

    <div class="category-filter-card">
      <div class="filter-header">
        <h3 class="filter-title">데이터 노출 항목 설정</h3>
        <button @click="toggleAllCategories" class="toggle-all-btn">
          {{ isAllSelected ? '전체 해제' : '전체 선택' }}
        </button>
      </div>
      <div class="checkbox-group">
        <label v-for="cat in categoryOptions" :key="cat.key" class="check-label">
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
            <th v-if="isVisible('lockers')">물품보관함 (실시간/설치)</th>
            <th v-if="isVisible('elevator')">엘리베이터</th>
            <th v-if="isVisible('parking')">환승주차장</th>
            <th v-if="isVisible('wheelchair')">휠체어리프트</th>
            <th v-if="isVisible('civil_service')">무인민원발급기</th>
            <th v-if="isVisible('currency')">환전키오스크</th>
            <th v-if="isVisible('train_ticket')">기차예매역</th>
            <th v-if="isVisible('culture')">문화공간</th>
            <th v-if="isVisible('meeting')">만남의장소</th>
            <th v-if="isVisible('lactation')">유아 수유방</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="displayList.length === 0">
            <td colspan="13" class="no-data">조회된 데이터가 없습니다. 역명 또는 호선을 선택해 주세요.</td>
          </tr>
          <tr v-for="item in displayList" :key="item.station_id + item.line_name">
            <td class="bold">{{ item.station_name }}</td>
            <td>
              <span class="line-badge" :style="{ backgroundColor: getLineColor(item.line_name) }">
                {{ item.line_name }}
              </span>
            </td>
            <td v-if="isVisible('incident_count')">{{ item.incident_count }}개</td>
            <td v-if="isVisible('lockers')">
              <span class="used-cnt">{{ item.used_lockers }}</span> / {{ item.total_lockers }}개
            </td>
            <td v-if="isVisible('elevator')">{{ formatStatus(item.elevator) }}</td>
            <td v-if="isVisible('parking')">{{ formatStatus(item.parking) }}</td>
            <td v-if="isVisible('wheelchair')">{{ formatStatus(item.wheelchairlift) }}</td>
            <td v-if="isVisible('civil_service')">{{ formatStatus(item.complaint) }}</td>
            <td v-if="isVisible('currency')">{{ formatStatus(item.exchange) }}</td>
            <td v-if="isVisible('train_ticket')">{{ formatStatus(item.trainreservation) }}</td>
            <td v-if="isVisible('culture')">{{ formatStatus(item.culturalspace) }}</td>
            <td v-if="isVisible('meeting')">{{ formatStatus(item.meeting) }}</td>
            <td v-if="isVisible('lactation')">{{ formatStatus(item.lactation) }}</td>
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

// 카테고리 옵션에 엘리베이터(elevator) 추가
const categoryOptions = [
  { key: 'incident_count', label: '장애발생건수' }, 
  { key: 'lockers', label: '물품보관함' },
  { key: 'elevator', label: '엘리베이터' }, // 추가됨
  { key: 'parking', label: '환승주차장' }, 
  { key: 'wheelchair', label: '휠체어리프트' },
  { key: 'civil_service', label: '무인민원발급기' }, 
  { key: 'currency', label: '환전키오스크' },
  { key: 'train_ticket', label: '기차예매역' }, 
  { key: 'culture', label: '문화공간' },
  { key: 'meeting', label: '만남의장소' }, 
  { key: 'lactation', label: '유아 수유방' }
];

const selectedCategories = ref(categoryOptions.map(c => c.key));
const displayList = ref([]);
const isAllSelected = computed(() => selectedCategories.value.length === categoryOptions.length);
const toggleAllCategories = () => { selectedCategories.value = isAllSelected.value ? [] : categoryOptions.map(c => c.key); };

const getLineColor = (line) => {
  const colors = { '1호선': '#0052A4', '2호선': '#00A84D', '3호선': '#EF7C1C', '4호선': '#00A5DE', '5호선': '#996CAC', '6호선': '#CD7C2F', '7호선': '#747F28', '8호선': '#E6186C', '9호선': '#BDB092' };
  return colors[line] || '#333';
};

const formatStatus = (val) => (val === 'Y' || val === 'O' ? 'O' : 'X');
const isVisible = (key) => selectedCategories.value.includes(key);

const handleSearch = async (searchData) => {
  try {
    const res = await axios.get('http://localhost:9000/get_status');
    let allData = res.data;

    if (searchData.type === 'station') {
      displayList.value = allData.filter(s => s.station_id === searchData.station_id);
    } 
    else if (searchData.type === 'line') {
      displayList.value = allData.filter(s => {
        const matchLine = s.line_name === searchData.line_name;
        return searchData.keyword ? (matchLine && s.station_name.includes(searchData.keyword)) : matchLine;
      });
    } 
    else if (searchData.type === 'keyword') {
      displayList.value = allData.filter(s => s.station_name.includes(searchData.keyword));
    }
    else {
      displayList.value = allData;
    }

    displayList.value.sort((a, b) => a.line_name.localeCompare(b.line_name, undefined, { numeric: true }));

  } catch (err) { console.error("데이터 로드 실패:", err); }
};
</script>

<style scoped>
/* 기존 스타일 그대로 유지 */
.status-container { padding: 20px; }
.category-filter-card { background: #fff; border: 1px solid #eee; border-radius: 12px; padding: 20px; margin-bottom: 20px; }
.filter-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; }
.toggle-all-btn { padding: 5px 12px; font-size: 12px; cursor: pointer; background: #f4f4f4; border: 1px solid #ddd; border-radius: 4px; }
.checkbox-group { display: flex; flex-wrap: wrap; gap: 15px; }
.table-wrapper { background: #fff; border-radius: 12px; overflow-x: auto; border: 1px solid #eee; }
.status-table { width: 100%; border-collapse: collapse; text-align: center; min-width: 1000px; }
.status-table th { background: #f8f9fa; padding: 15px; border-bottom: 2px solid #eee; font-size: 14px; }
.status-table td { padding: 15px; border-bottom: 1px solid #f0f0f0; font-size: 14px; }
.line-badge { color: white; padding: 4px 10px; border-radius: 20px; font-size: 12px; font-weight: bold; }
.used-cnt { color: #007bff; font-weight: bold; }
.bold { font-weight: bold; }
.no-data { padding: 40px; color: #999; }
</style>