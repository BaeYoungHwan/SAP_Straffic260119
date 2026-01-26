import { createWebHistory, createRouter } from 'vue-router';     // npm i vue-router@next

import member from '@/router/member/member.js';
import bbs from '@/router/bbs/bbs';

const router = createRouter({
    history: createWebHistory(),
    routes: [
        ...member,
        ...bbs,

    ]
})

export default router;