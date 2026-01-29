<template>
  <div class="search-container">
    <div class="filter-box">
      
      <div class="filter-item">
        <label class="filter-label">호선 선택 <span class="required">(필수)</span></label>
        <div class="line-button-group">
          <button 
            v-for="line in lineOptions" 
            :key="line"
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
            placeholder="역명을 입력하세요 (예: 서울)" 
            :disabled="!filters.line"
            @input="onInput"
            @focus="onFocus"
          >
          
          <ul v-if="showResults && filteredStations.length > 0" class="auto-complete-list">
            <li 
              v-for="st in filteredStations" 
              :key="st.station_id" 
              @click="selectStation(st)"
            >
              {{ st.station_name }}
            </li>
          </ul>
        </div>
      </div>

      <button 
        class="search-btn" 
        @click="executeSearch" 
        :disabled="!selectedStation"
      >
        현황 조회
      </button>

    </div>
  </div>
</template>

<script setup>
import { ref, computed, reactive, defineEmits } from 'vue';

// 부모 컴포넌트로 검색 이벤트를 보내기 위한 설정 [cite: 2026-01-29]
const emit = defineEmits(['search']);

// --- 데이터 정의 --- [cite: 2026-01-29]
const lineOptions = ['1호선', '2호선', '3호선', '4호선', '5호선', '6호선', '7호선', '8호선', '9호선'];

// 역 정보 DB (나중에 API 연동 가능) [cite: 2026-01-29]
const allStationData = ref([
  { station_id: '130', station_name: '서울역', line_name: '1호선' },
  { station_id: '426', station_name: '서울역', line_name: '4호선' },
  { station_id: '131', station_name: '시청', line_name: '1호선' },
  { station_id: '201', station_name: '시청', line_name: '2호선' },
  { station_id: '132', station_name: '종각', line_name: '1호선' },
]);

const filters = reactive({
  line: '',
  stationSearch: ''
});

const showResults = ref(false);
const selectedStation = ref(null);

// --- 로직 처리 --- [cite: 2026-01-29]

// 1. 호선 버튼 클릭 시 처리 [cite: 2026-01-29]
const handleLineClick = (line) => {
  filters.line = line;
  filters.stationSearch = ''; // 호선 변경 시 입력값 초기화 [cite: 2026-01-29]
  selectedStation.value = null;
  showResults.value = false;
};

// 2. 검색어 입력 시 필터링 (호선 일치 + 검색어 포함) [cite: 2026-01-29]
const filteredStations = computed(() => {
  if (!filters.line || !filters.stationSearch) return [];
  
  const searchKeyword = filters.stationSearch.trim();
  
  return allStationData.value.filter(s => 
    s.line_name === filters.line && // 1. 선택한 호선과 일치해야 함
    s.station_name.includes(searchKeyword) // 2. 입력한 키워드를 포함해야 함
  );
});

const onInput = () => {
  showResults.value = true;
};

const onFocus = () => {
  if (filters.stationSearch) showResults.value = true;
};

// 3. 리스트에서 역 선택 시 [cite: 2026-01-29]
const selectStation = (st) => {
  filters.stationSearch = st.station_name;
  selectedStation.value = st;
  showResults.value = false; // 리스트 닫기 [cite: 2026-01-29]
};

// 4. 최종 검색 버튼 실행 [cite: 2026-01-29]
const executeSearch = () => {
  if (selectedStation.value) {
    // 부모 컴포넌트로 선택된 역의 전체 객체를 전달 [cite: 2026-01-29]
    emit('search', selectedStation.value); 
  }
};
</script>

<style scoped>
/* 컨테이너 및 레이아웃 [cite: 2026-01-29] */
.search-container { 
  padding: 25px; 
  background: #ffffff; 
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
  margin-bottom: 20px;
}

.filter-box { 
  display: flex; 
  flex-wrap: wrap;
  align-items: flex-end; 
  gap: 30px; 
}

.filter-item { 
  display: flex; 
  flex-direction: column; 
  gap: 10px; 
}

.filter-label {
  font-weight: bold;
  font-size: 14px;
  color: #444;
}

.required { color: #ff4d4f; font-size: 12px; }

/* 호선 버튼 스타일 [cite: 2026-01-29] */
.line-button-group {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.line-btn {
  padding: 8px 16px;
  border: 1px solid #e0e0e0;
  background: #fff;
  border-radius: 25px;
  cursor: pointer;
  font-size: 13px;
  transition: all 0.2s ease;
}

.line-btn.active {
  background: #007bff;
  color: white;
  border-color: #007bff;
  font-weight: 600;
}

/* 검색 인풋 스타일 [cite: 2026-01-29] */
.input-relative { position: relative; }

.search-input-wrapper input { 
  width: 220px; 
  padding: 10px 15px; 
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
}

.search-input-wrapper input:disabled { background: #f9f9f9; cursor: not-allowed; }

/* 자동완성 리스트 스타일 [cite: 2026-01-29] */
.auto-complete-list {
  position: absolute; 
  top: calc(100% + 5px); 
  left: 0; 
  width: 100%;
  background: white; 
  border: 1px solid #ddd; 
  border-radius: 8px;
  z-index: 1000;
  max-height: 200px; 
  overflow-y: auto; 
  list-style: none; 
  padding: 0; 
  margin: 0;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.auto-complete-list li { 
  padding: 12px 15px; 
  cursor: pointer; 
  border-bottom: 1px solid #f0f0f0; 
  color: #333; /* 글자 안 보이는 현상 방지 [cite: 2026-01-29] */
  font-size: 14px;
  text-align: left;
}

.auto-complete-list li:hover { background: #f0f7ff; color: #007bff; }

/* 조회 버튼 [cite: 2026-01-29] */
.search-btn { 
  padding: 12px 25px; 
  background: #007bff; 
  color: white; 
  border: none; 
  cursor: pointer; 
  border-radius: 8px; 
  font-weight: bold;
  font-size: 14px;
  height: 42px;
}

.search-btn:disabled { background: #bdc3c7; cursor: not-allowed; }
</style>