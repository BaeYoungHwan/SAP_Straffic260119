<template>
  <div class="subway-selector">
    <select v-model="selectedLine" @change="fetchStations">
      <option value="">호선 선택</option>
      <option v-for="line in lineOptions" :key="line" :value="line">
        {{ line }}
      </option>
    </select>

    <select v-model="selectedStation" :disabled="!selectedLine" @change="emitSelection">
      <option value="">역 선택</option>
      <option v-for="st in stationOptions" :key="st.station_id" :value="st">
        {{ st.station_name }}
      </option>
    </select>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

// 부모에게 선택된 데이터를 보냄
const emit = defineEmits(['update-selection']);

const selectedLine = ref('');
const selectedStation = ref('');
const lineOptions = ['1호선', '2호선', '3호선', '4호선', '5호선', '6호선', '7호선', '8호선', '9호선'];
const stationOptions = ref([]);

// 호선 변경 시 실행: 작성하신 MyBatis 쿼리 호출
const fetchStations = async () => {
  if (!selectedLine.value) {
    stationOptions.value = [];
    return;
  }
  
  try {
    // 백엔드 search_stationname 호출
    const resp = await axios.get('http://localhost:9000/search_stationname', {
      params: { line_name: selectedLine.value }
    });
    stationOptions.value = resp.data; // [{station_name: '...', station_id: '...'}, ...]
    selectedStation.value = ''; // 호선 바뀌면 역 선택 초기화
  } catch (err) {
    console.error("역 목록 로드 실패:", err);
  }
};

// 최종 선택 값 부모에게 전달
const emitSelection = () => {
  emit('update-selection', {
    line_name: selectedLine.value,
    station: selectedStation.value // name과 id가 담긴 객체 전달
  });
};
</script>