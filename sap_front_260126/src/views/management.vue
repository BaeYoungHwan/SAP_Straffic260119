<template>
  <div id="newuserlist-container">
    <h4 class="mb-4">신규 사용자 승인 대기 목록</h4>
    <table class="table table-hover text-center">
      <thead class="thead-light">
        <tr>
          <th>No</th>
          <th>ID</th>
          <th>이름</th>
          <th>소속 호선</th>
          <th>소속역</th>
          <th>상태</th>
          <th>관리</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(user, index) in users" :key="user.user_id">
          <td><strong>{{ cnt - ((pageNumber - 1) * 5) - index }}</strong></td>
          <td>{{ user.user_id }}</td>
          <td>{{ user.user_name }}</td>
          <td>{{ user.line_name }}</td>
          <td>{{ user.station_name }}</td>
          <td><span class="badge badge-warning">승인대기</span></td>
          <td>
            <button class="btn btn-sm btn-success mr-2" @click="approveUser(user.user_id)">승인</button>
            <button class="btn btn-sm btn-outline-danger" @click="rejectUser(user.user_id)">거절</button>
          </td>
        </tr>
        <tr v-if="users.length === 0">
          <td colspan="7" class="py-5 text-muted">대기 중인 사용자가 없습니다.</td>
        </tr>
      </tbody>
    </table>

    <div class="d-flex justify-content-center mt-4">
      <b-pagination
        v-model="pageNumber"
        :total-rows="cnt"
        :per-page="5"
        @input="getNewUserList"
        align="center"
      ></b-pagination>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'NewUserList',
  data() {
    return {
      users: [],
      pageNumber: 1, // b-pagination은 1부터 시작
      cnt: 0
    }
  },
  mounted() {
    this.getNewUserList();
  },
  methods: {
    getNewUserList() {
      // 서버 전송용 파라미터 (pageNumber 1을 보내면 백엔드 offset에서 0으로 계산함)
      const params = { pageNumber: this.pageNumber };

      axios.get("http://localhost:9000/get_newuserlist", { params })
        .then(resp => {
          this.users = resp.data.newuserlist;
          this.cnt = resp.data.cnt;
        })
        .catch(err => console.error("데이터 로딩 실패:", err));
    },
    approveUser(userId) {
      if(confirm(`${userId} 사용자를 승인하시겠습니까?`)) {
        axios.post("http://localhost:9000/approve_user", null, { params: { user_id: userId } })
          .then(resp => {
            alert("승인되었습니다.");
            this.getNewUserList();
          });
      }
    },
    rejectUser(userId) {
      if(confirm(`${userId} 요청을 거절하시겠습니까?`)) {
        axios.post("http://localhost:9000/reject_user", null, { params: { user_id: userId } })
          .then(resp => {
            alert("거절 처리되었습니다.");
            this.getNewUserList();
          });
      }
    }
  }
}
</script>

<style scoped>
#newuserlist-container { padding: 30px; background: #fff; border-radius: 10px; }
.table th { border-top: none; }
.badge-warning { background-color: #f39c12; color: white; padding: 5px 10px; }
</style>