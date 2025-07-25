import VueRouter from 'vue-router'

//引入组件
import Index from '../pages'
import Home from '../pages/home/home'
import Login from '../pages/login/login'
import Register from '../pages/register/register'
import Center from '../pages/center/center'
import Messages from '../pages/messages/list'
import Forum from '../pages/forum/list'
import ForumAdd from '../pages/forum/add'
import ForumDetail from '../pages/forum/detail'
import MyForumList from '../pages/forum/myForumList'
import Storeup from '../pages/storeup/list'
import payList from '../pages/pay'

import yonghuList from '../pages/yonghu/list'
import yonghuDetail from '../pages/yonghu/detail'
import yonghuAdd from '../pages/yonghu/add'
import courseList from '../pages/course/list'
import courseDetail from '../pages/course/detail'
import courseAdd from '../pages/course/add'
import courseTypeList from '../pages/courseType/list'
import courseTypeDetail from '../pages/courseType/detail'
import courseTypeAdd from '../pages/courseType/add'
import kechengpingjiaList from '../pages/kechengpingjia/list'
import kechengpingjiaDetail from '../pages/kechengpingjia/detail'
import kechengpingjiaAdd from '../pages/kechengpingjia/add'
import xuexijinduList from '../pages/xuexijindu/list'
import xuexijinduDetail from '../pages/xuexijindu/detail'
import xuexijinduAdd from '../pages/xuexijindu/add'
import syslogList from '../pages/syslog/list'
import syslogDetail from '../pages/syslog/detail'
import syslogAdd from '../pages/syslog/add'

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
	return originalPush.call(this, location).catch(err => err)
}

//配置路由
export default new VueRouter({
	routes:[
		{
      path: '/',
      redirect: '/index/home'
    },
		{
			path: '/index',
			component: Index,
			children:[
				{
					path: 'home',
					component: Home
				},
				{
					path: 'center',
					component: Center,
				},
				{
					path: 'pay',
					component: payList,
				},
				{
					path: 'messages',
					component: Messages
				},
				{
					path: 'forum',
					component: Forum
				},
				{
					path: 'forumAdd',
					component: ForumAdd
				},
				{
					path: 'forumDetail',
					component: ForumDetail
				},
				{
					path: 'myForumList',
					component: MyForumList
				},
				{
					path: 'storeup',
					component: Storeup
				},
				{
					path: 'yonghu',
					component: yonghuList
				},
				{
					path: 'yonghuDetail',
					component: yonghuDetail
				},
				{
					path: 'yonghuAdd',
					component: yonghuAdd
				},
				{
					path: 'course',
					component: courseList
				},
				{
					path: 'courseDetail',
					component: courseDetail
				},
				{
					path: 'courseAdd',
					component: courseAdd
				},
				{
					path: 'courseType',
					component: courseTypeList
				},
				{
					path: 'courseTypeDetail',
					component: courseTypeDetail
				},
				{
					path: 'courseTypeAdd',
					component: courseTypeAdd
				},
				{
					path: 'kechengpingjia',
					component: kechengpingjiaList
				},
				{
					path: 'kechengpingjiaDetail',
					component: kechengpingjiaDetail
				},
				{
					path: 'kechengpingjiaAdd',
					component: kechengpingjiaAdd
				},
				{
					path: 'xuexijindu',
					component: xuexijinduList
				},
				{
					path: 'xuexijinduDetail',
					component: xuexijinduDetail
				},
				{
					path: 'xuexijinduAdd',
					component: xuexijinduAdd
				},
				{
					path: 'syslog',
					component: syslogList
				},
				{
					path: 'syslogDetail',
					component: syslogDetail
				},
				{
					path: 'syslogAdd',
					component: syslogAdd
				},
			]
		},
		{
			path: '/login',
			component: Login
		},
		{
			path: '/register',
			component: Register
		},
	]
})
