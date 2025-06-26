import Vue from 'vue'
import Router from 'vue-router'

// 【核心修改点】修正了 Index 和 Home 组件的导入路径
import Index from '@/pages/index.vue'
import Home from '@/pages/home/home.vue'

// 引入其他必要的页面组件
import Login from '@/pages/login/login'
import Register from '@/pages/register/register'
import Center from '@/pages/center/center'
import ForumList from '@/pages/forum/list'
import ForumDetail from '@/pages/forum/detail'
import MyForumList from '@/pages/forum/myForumList'
import StoreupList from '@/pages/storeup/list'
import MessagesList from '@/pages/messages/list'

// 【核心修改点】引入我们重构后的课程列表和详情页
import CourseList from '@/pages/kechengxinxi/list'
import CourseDetail from '@/pages/kechengxinxi/detail'

Vue.use(Router)

// 定义路由规则
const routes = [
	{
		path: '/login',
		component: Login,
		name: '登录'
	},
	{
		path: '/register',
		component: Register,
		name: '注册'
	},
	{
		path: '/',
		component: Index,
		name: '首页',
		redirect: '/index/home', // 默认重定向到home
		children: [
			{
				path: 'home',
				component: Home,
				name: '首页'
			},
			{
				path: 'center',
				component: Center,
				name: '个人中心'
			},
			{
				path: 'messages/list',
				component: MessagesList,
				name: '意见反馈'
			},
			{
				path: 'storeup/list',
				component: StoreupList,
				name: '我的收藏'
			},
			{
				path: 'forum/list',
				component: ForumList,
				name: '交流论坛'
			},
			{
				path: 'forum/detail',
				component: ForumDetail,
				name: '帖子详情'
			},
			{
				path: 'forum/myForumList',
				component: MyForumList,
				name: '我的帖子'
			},
			// --- 【路由适配】让旧路径和新路径都能工作 ---
			{
				path: 'kechengxinxi/list', // 保留旧的菜单路径
				component: CourseList,
				name: '课程列表'
			},
			{
				path: 'courses/detail', // 使用新的、规范的详情页路径
				component: CourseDetail,
				name: '课程详情'
			}
		]
	},
	// 默认重定向，如果访问未知路径则跳转到首页
	{
		path: '*',
		redirect: '/index/home'
	}
]

const router = new Router({
	routes
})

export default router