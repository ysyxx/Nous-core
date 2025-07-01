import Vue from 'vue';
import VueRouter from 'vue-router'
Vue.use(VueRouter);

import Index from '@/views/index'
import Home from '@/views/home'
import Login from '@/views/login'
import NotFound from '@/views/404'
import UpdatePassword from '@/views/update-password'
import pay from '@/views/pay'
import register from '@/views/register'
import center from '@/views/center'

import yonghu from '@/views/modules/yonghu/list'
import users from '@/views/modules/users/list'
import course from '@/views/modules/course/list'
import chapter from '@/views/modules/chapter/list'
import lesson from '@/views/modules/lesson/list'
import courseType from '@/views/modules/courseType/list'
import kechengpingjia from '@/views/modules/kechengpingjia/list'
import forum from '@/views/modules/forum/list'
import messages from '@/views/modules/messages/list'
import config from '@/views/modules/config/list'
import syslog from '@/views/modules/syslog/list'

export const routes = [{
    path: '/',
    component: Index,
    children: [{
        path: '/',
        name: 'home', //【修改点】
        component: Home,
        meta: {icon:'', title:'center', affix: true}
    }, {
        path: '/updatePassword',
        name: 'updatePassword', //【修改点】
        component: UpdatePassword,
        meta: {icon:'', title:'updatePassword'}
    }, {
        path: '/pay',
        name: 'pay', //【修改点】
        component: pay,
        meta: {icon:'', title:'pay'}
    }, {
        path: '/center',
        name: 'center', //【修改点】
        component: center,
        meta: {icon:'', title:'center'}
    }
        ,{ path: '/yonghu', name: 'yonghu', component: yonghu } //【修改点】
        ,{ path: '/users', name: 'users', component: users } //【修改点】
        ,{ path: '/course', name: 'course', component: course } //【修改点】
        ,{ path: '/chapter', name: 'chapter', component: chapter } //【修改点】
        ,{ path: '/lesson', name: 'lesson', component: lesson } //【修改点】
        ,{ path: '/courseType', name: 'courseType', component: courseType } //【修改点】
        ,{ path: '/kechengpingjia', name: 'kechengpingjia', component: kechengpingjia } //【修改点】
        ,{ path: '/forum', name: 'forum', component: forum } //【修改点】
        ,{ path: '/messages', name: 'messages', component: messages } //【修改点】
        ,{ path: '/config', name: 'config', component: config } //【修改点】
        ,{ path: '/syslog', name: 'syslog', component: syslog } //【修改点】
    ]
},
    { path: '/login', name: 'login', component: Login, meta: {icon:'', title:'login'} },
    { path: '/register', name: 'register', component: register, meta: {icon:'', title:'register'} },
    { path: '*', component: NotFound }
]

const router = new VueRouter({
    mode: 'hash',
    routes
})
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}
export default router;