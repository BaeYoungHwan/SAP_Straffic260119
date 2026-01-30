<template>
  <div class="search-container">
    <div class="filter-box">
      <div class="filter-item">
        <label class="filter-label">호선 선택</label>
        <div class="line-button-group">
          <button 
            v-for="line in lineOptions" :key="line"
            type="button"
            :class="['line-btn', { active: filters.line === line }]"
            @click="handleLineClick(line)"
          >
            {{ line }}
          </button>
        </div>
      </div>

      <div class="filter-item search-input-wrapper">
        <label class="filter-label">역 검색</label>
        <div class="input-relative">
          <input 
            type="text" 
            v-model="filters.stationSearch" 
            placeholder="역명 입력 또는 호선 선택" 
            @input="onInput" 
            @focus="onFocus"
            @keyup.enter="executeSearch"
          >
          <ul v-if="showResults && filteredStations.length > 0" class="auto-complete-list">
            <li v-for="st in filteredStations" :key="st.station_id" @click="selectStation(st)">
              <span class="st-name">{{ st.station_name }}</span>
              <span class="st-line">{{ st.line_name }}</span>
            </li>
          </ul>
        </div>
      </div>

      <button class="search-btn" @click="executeSearch" :disabled="!isSearchReady">
        현황 조회
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, reactive, defineEmits, onMounted } from 'vue';
import axios from 'axios';

const emit = defineEmits(['search']);
const lineOptions = ['1호선', '2호선', '3호선', '4호선', '5호선', '6호선', '7호선', '8호선', '9호선'];
const allStationData = ref([]);
const filters = reactive({ line: '', stationSearch: '' });
const showResults = ref(false); // 연관 검색어 창 노출 여부
const selectedStation = ref(null);

// 호선이 있거나 역이 선택되었을 때 버튼 활성화
const isSearchReady = computed(() => filters.line !== '' || selectedStation.value !== null || filters.stationSearch !== '');

// 전체 역 목록 가져오기
const fetchAllStations = async () => {
  try {
    const res = await axios.get('http://localhost:9000/getallstations');
    allStationData.value = res.data;
  } catch (err) {
    console.error("역 목록 로드 실패", err);
  }
};

onMounted(fetchAllStations);

// 자동완성 필터링 로직
const filteredStations = computed(() => {
  const keyword = filters.stationSearch.trim();
  if (filters.line !== '') {
    // 호선이 선택된 경우: 해당 호선의 역들만 필터링
    return allStationData.value.filter(s => 
      s.line_name === filters.line && 
      (keyword === '' || s.station_name.includes(keyword))
    );
  }
  // 호선이 선택되지 않은 경우: 전체에서 검색
  if (keyword !== '') {
    return allStationData.value.filter(s => s.station_name.includes(keyword));
  }
  return [];
});

// 호선 버튼 클릭 핸들러
const handleLineClick = (line) => {
  if (filters.line === line) {
    filters.line = '';
    showResults.value = false;
  } else {
    filters.line = line;
    filters.stationSearch = ''; 
    selectedStation.value = null;
    showResults.value = true; // 호선 선택 시 해당 호선 역 목록 보여주기
  }
};

// 연관 검색어에서 역 선택 시
const selectStation = (st) => {
  filters.stationSearch = st.station_name;
  filters.line = st.line_name;
  selectedStation.value = st;
  showResults.value = false; // 선택 후 창 닫기
};

const onInput = () => {
  showResults.value = true;
  selectedStation.value = null;
};

const onFocus = () => {
  if (filteredStations.value.length > 0) {
    showResults.value = true;
  }
};

// ⭐ [현황 조회] 실행 시 연관 검색어 창을 닫도록 수정
const executeSearch = () => {
  showResults.value = false; // 검색어 창 닫기

  if (selectedStation.value) {
    emit('search', { type: 'station', station_id: selectedStation.value.station_id });
  } else if (filters.line) {
    emit('search', { type: 'line', line_name: filters.line });
  } else {
    // 아무것도 선택 안하고 검색 시 전체 조회 또는 알림 처리 (필요시)
    emit('search', { type: 'all' });
  }
};
</script>

<style scoped>
.search-container { padding: 25px; background: #fff; border-radius: 12px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); margin-bottom: 20px; }
.filter-box { display: flex; flex-wrap: wrap; align-items: flex-end; gap: 30px; }
.filter-item { display: flex; flex-direction: column; gap: 10px; }
.filter-label { font-weight: bold; font-size: 14px; }
.line-button-group { display: flex; flex-wrap: wrap; gap: 8px; }
.line-btn { padding: 8px 16px; border: 1px solid #e0e0e0; background: #fff; border-radius: 25px; cursor: pointer; font-size: 13px; transition: all 0.2s; }
.line-btn.active { background: #007bff; color: white; border-color: #007bff; }

.input-relative { position: relative; }
.search-input-wrapper input { width: 250px; padding: 10px 15px; border: 1px solid #ddd; border-radius: 8px; outline: none; }
.search-input-wrapper input:focus { border-color: #007bff; }

.auto-complete-list { 
  position: absolute; top: calc(100% + 5px); left: 0; width: 100%; 
  background: white; border: 1px solid #ddd; border-radius: 8px; 
  z-index: 1000; max-height: 250px; overflow-y: auto; list-style: none; padding: 0; 
  box-shadow: 0 4px 12px rgba(0,0,0,0.15); 
}
.auto-complete-list li { 
  padding: 12px 15px; cursor: pointer; border-bottom: 1px solid #f0f0f0; 
  display: flex; justify-content: space-between; align-items: center;
}
.auto-complete-list li:hover { background-color: #f8f9fa; }
.st-line { font-size: 11px; color: #007bff; background: #eef6ff; padding: 2px 6px; border-radius: 4px; }

.search-btn { 
  padding: 0 30px; background: #007bff; color: white; border: none; 
  border-radius: 8px; font-weight: bold; height: 42px; cursor: pointer; transition: background 0.2s;
}
.search-btn:hover { background: #0056b3; }
.search-btn:disabled { background: #bdc3c7; cursor: not-allowed; }
</style>