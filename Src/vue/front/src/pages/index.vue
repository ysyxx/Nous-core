<template>
	<div class="main-layout">
		<div class="layout-body">
			<!-- 顶部导航栏 -->
			<div class="top-navbar">
				<div class="logo-container">
					<span class="logo-text">智核NousCore</span>
				</div>

				<div class="user-controls">
					<img v-if="headportrait && Token" class="user-avatar"
						:src="headportrait ? headportrait : require('@/assets/avator.png')">
					<div v-if="Token" class="username">{{ username }}</div>
					<div v-if="Token && notAdmin" class="user-center" @click="goMenu('/index/center')">个人中心</div>
					<el-button v-if="!Token" class="login-btn" @click="toLogin()">登录/注册</el-button>
					<el-button v-if="Token" class="logout-btn" @click="logout">退出</el-button>
				</div>
			</div>

			<!-- 主菜单 -->
			<div class="main-menu">
				<el-scrollbar wrap-class="scrollbar-wrapper-horizontal">
					<el-menu class="horizontal-menu" :default-active="activeMenu" :unique-opened="true"
						mode="horizontal" :router="true" @select="handleSelect">
						<div class="user-info">
							<el-image class="user-avatar-small"
								:src="headportrait ? headportrait : require('@/assets/avator.png')"
								fit="cover"></el-image>
							<div class="username-small">{{ username }}</div>
						</div>

						<el-menu-item class="menu-item home" index="/index/home" @click.native="goMenu('/index/home')">
							<span class="menu-icon iconfont icon-shouye-zhihui"></span>
							<span class="menu-text">首页</span>
						</el-menu-item>

						<el-menu-item class="menu-item" v-for="(menu, index) in menuList" :index="menu.url" :key="index"
							@click.native="goMenu(menu.url)">
							<i class="menu-icon" :class="iconArr[index]"></i>
							<span class="menu-text">{{ menu.name }}</span>
						</el-menu-item>

						<el-menu-item class="menu-item user" index="/index/center" v-if="Token && notAdmin"
							@click.native="goMenu('/index/center')">
							<span class="menu-icon iconfont icon-shouye-zhihui"></span>
							<span class="menu-text">用户中心</span>
						</el-menu-item>
					</el-menu>
				</el-scrollbar>
			</div>

			<!-- 轮播图 -->
			<div class="banner-carousel">
				<el-carousel class="carousel" trigger="click" indicator-position="inside" arrow="always" type="default"
					direction="horizontal" height="500px" :autoplay="true" :interval="3000" :loop="true">
					<el-carousel-item v-for="item in carouselList" :key="item.id">
						<el-image @click="carouselClick(item.url)" class="carousel-image" :src="baseUrl + item.value"
							fit="cover"></el-image>
					</el-carousel-item>
				</el-carousel>
			</div>

			<!-- 主内容区 -->
			<router-view id="scrollView"></router-view>

			<!-- 页脚 -->
			<div class="footer">
				<div class="footer-content" v-html="bottomContent"></div>
			</div>
		</div>
	</div>
</template>

<script>
import Vue from 'vue'
import Swiper from "swiper";
import axios from 'axios'

export default {
	data() {
		return {
			activeIndex: '0',
			roleMenus: [{ "backMenu": [{ "child": [{ "allButtons": ["新增", "查看", "修改", "删除"], "appFrontIcon": "cuIcon-flashlightopen", "buttons": ["新增", "查看", "修改", "删除"], "menu": "用户", "menuJump": "列表", "tableName": "yonghu" }], "menu": "用户管理" }, { "child": [{ "allButtons": ["新增", "查看", "修改", "删除", "课程评价", "学习进度"], "appFrontIcon": "cuIcon-paint", "buttons": ["新增", "查看", "修改", "删除", "学习进度"], "menu": "课程信息", "menuJump": "列表", "tableName": "kechengxinxi" }], "menu": "课程信息管理" }, { "child": [{ "allButtons": ["新增", "查看", "修改", "删除"], "appFrontIcon": "cuIcon-flashlightopen", "buttons": ["新增", "查看", "修改", "删除"], "menu": "课程类型", "menuJump": "列表", "tableName": "kechengleixing" }], "menu": "课程类型管理" }, { "child": [{ "allButtons": ["新增", "查看", "修改", "删除", "审核"], "appFrontIcon": "cuIcon-list", "buttons": ["查看", "删除", "审核"], "menu": "课程评价", "menuJump": "列表", "tableName": "course_assessment" }], "menu": "课程评价管理" }, { "child": [{ "allButtons": ["新增", "查看", "修改", "删除", "学习时长", "学习进度", "首页总数", "首页统计"], "appFrontIcon": "cuIcon-present", "buttons": ["查看", "修改", "删除", "首页总数", "首页统计"], "menu": "学习进度", "menuJump": "列表", "tableName": "xuexijindu" }], "menu": "学习进度管理" }, { "child": [{ "allButtons": ["查看", "修改", "回复", "删除"], "appFrontIcon": "cuIcon-message", "buttons": ["查看", "回复", "删除"], "menu": "意见反馈", "tableName": "messages" }], "menu": "意见反馈" }, { "child": [{ "allButtons": ["新增", "查看", "修改", "删除"], "appFrontIcon": "cuIcon-group", "buttons": ["查看", "删除"], "menu": "互动交流", "tableName": "forum" }], "menu": "互动交流" }, { "child": [{ "allButtons": ["新增", "查看", "修改", "删除"], "appFrontIcon": "cuIcon-send", "buttons": ["查看", "修改", "删除"], "menu": "轮播图管理", "tableName": "config" }, { "allButtons": ["查看", "删除"], "appFrontIcon": "cuIcon-similar", "buttons": ["查看", "删除"], "menu": "系统日志", "tableName": "syslog" }], "menu": "系统管理" }], "frontMenu": [{ "child": [{ "allButtons": ["新增", "查看", "修改", "删除", "课程评价", "学习进度"], "appFrontIcon": "cuIcon-skin", "buttons": ["查看", "课程评价"], "menu": "课程信息列表", "menuJump": "列表", "tableName": "kechengxinxi" }], "menu": "课程信息模块" }], "hasBackLogin": "是", "hasBackRegister": "否", "hasFrontLogin": "否", "hasFrontRegister": "否", "roleName": "管理员", "tableName": "users" }, { "backMenu": [{ "child": [{ "allButtons": ["新增", "查看", "修改", "删除", "审核"], "appFrontIcon": "cuIcon-list", "buttons": ["查看"], "menu": "课程评价", "menuJump": "列表", "tableName": "course_assessment" }], "menu": "课程评价管理" }, { "child": [{ "allButtons": ["新增", "查看", "修改", "删除", "学习时长", "学习进度", "首页总数", "首页统计"], "appFrontIcon": "cuIcon-present", "buttons": ["查看"], "menu": "学习进度", "menuJump": "列表", "tableName": "xuexijindu" }], "menu": "学习进度管理" }], "frontMenu": [{ "child": [{ "allButtons": ["新增", "查看", "修改", "删除", "课程评价", "学习进度"], "appFrontIcon": "cuIcon-skin", "buttons": ["查看", "课程评价"], "menu": "课程信息列表", "menuJump": "列表", "tableName": "kechengxinxi" }], "menu": "课程信息模块" }], "hasBackLogin": "否", "hasBackRegister": "否", "hasFrontLogin": "是", "hasFrontRegister": "是", "roleName": "用户", "tableName": "yonghu" }],
			baseUrl: '',
			carouselList: [],
			menuList: [],
			form: {
				ask: '',
				userid: localStorage.getItem('frontUserid')
			},
			headportrait: localStorage.getItem('frontHeadportrait') ? localStorage.getItem('frontHeadportrait') : '',
			Token: localStorage.getItem('frontToken'),
			username: localStorage.getItem('username'),
			notAdmin: localStorage.getItem('frontSessionTable') != '"users"',
			timer: '',
			iconArr: [
				'el-icon-star-off',
				'el-icon-goods',
				'el-icon-warning',
				'el-icon-question',
				'el-icon-info',
				'el-icon-help',
				'el-icon-picture-outline-round',
				'el-icon-camera-solid',
				'el-icon-video-camera-solid',
				'el-icon-video-camera',
				'el-icon-bell',
				'el-icon-s-cooperation',
				'el-icon-s-order',
				'el-icon-s-platform',
				'el-icon-s-operation',
				'el-icon-s-promotion',
				'el-icon-s-release',
				'el-icon-s-ticket',
				'el-icon-s-management',
				'el-icon-s-open',
				'el-icon-s-shop',
				'el-icon-s-marketing',
				'el-icon-s-flag',
				'el-icon-s-comment',
				'el-icon-s-finance',
				'el-icon-s-claim',
				'el-icon-s-opportunity',
				'el-icon-s-data',
				'el-icon-s-check'
			],
			bottomContent: '',
		}
	},
	created() {
		this.baseUrl = this.$config.baseUrl;
		this.menuList = this.$config.indexNav;
		this.getCarousel();
		if (localStorage.getItem('frontToken') && localStorage.getItem('frontToken') != null) {
			this.getSession()
		}
	},
	mounted() {
		this.activeIndex = localStorage.getItem('keyPath') || '0';



	},
	computed: {
		activeMenu() {
			const route = this.$route
			const {
				meta,
				path
			} = route
			// if st path, the sidebar will highlight the path you sete
			if (meta.activeMenu) {
				return meta.activeMenu
			}
			return path
		},
	},
	watch: {
		$route(newValue) {
			let that = this
			let url = window.location.href
			let arr = url.split('#')
			for (let x in this.menuList) {
				if (newValue.path == this.menuList[x].url) {
					this.activeIndex = x
				}
			}
			this.Token = localStorage.getItem('frontToken')
			if (arr[1] != '/index/home') {
				var element = document.getElementById('scrollView');
				var distance = element.offsetTop;
				window.scrollTo(0, distance)
			} else {
				window.scrollTo(0, 0)
			}
		},
		headportrait() {
			this.$forceUpdate()
		},
	},
	methods: {

		async getSession() {
			await this.$http.get(`${localStorage.getItem('UserTableName')}/session`, { emulateJSON: true }).then(async res => {
				if (res.data.code == 0) {
					localStorage.setItem('sessionForm', JSON.stringify(res.data.data))
					localStorage.setItem('frontUserid', res.data.data.id);
					if (res.data.data.vip) {
						localStorage.setItem('vip', res.data.data.vip);
					}
					if (res.data.data.touxiang) {
						this.headportrait = res.data.data.touxiang
						localStorage.setItem('frontHeadportrait', res.data.data.touxiang);
					} else if (res.data.data.headportrait) {
						this.headportrait = res.data.data.headportrait
						localStorage.setItem('frontHeadportrait', res.data.data.headportrait);
					}
				}
			});
		},
		handleSelect(keyPath) {
			if (keyPath) {
				localStorage.setItem('keyPath', keyPath)
			}
		},
		toLogin() {
			this.$router.push('/login');
		},
		logout() {
			localStorage.clear();
			Vue.http.headers.common['Token'] = "";
			this.$router.push('/index/home');
			this.activeIndex = '0'
			localStorage.setItem('keyPath', this.activeIndex)
			this.Token = ''
			this.$forceUpdate()
			this.$message({
				message: '登出成功',
				type: 'success',
				duration: 1000,
			});
		},
		getCarousel() {
			this.$http.get('config/list', { params: { page: 1, limit: 3 } }).then(res => {
				if (res.data.code == 0) {
					this.carouselList = res.data.data.list;
				}
			});
		},
		// 轮播图跳转
		carouselClick(url) {
			if (url) {
				if (url.indexOf('https') != -1) {
					window.open(url)
				} else {
					this.$router.push(url)
				}
			}
		},
		goBackend() {
			localStorage.setItem('Token', localStorage.getItem('frontToken'));
			localStorage.setItem('role', localStorage.getItem('frontRole'));
			localStorage.setItem('sessionTable', localStorage.getItem('frontSessionTable'));
			localStorage.setItem('headportrait', localStorage.getItem('frontHeadportrait'));
			localStorage.setItem('userid', localStorage.getItem('frontUserid'));
			window.open(`${this.$config.baseUrl}admin/dist/index.html`, "_blank");
		},
		goMenu(path) {
			this.$router.push(path);
		},
	}
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.main-layout {
	width: 100%;

	.layout-body {
		min-height: 100vh;
		padding: 64px 0 0;
		margin: 0;
		position: relative;
		background: #fff;
	}

	/* 顶部导航栏样式 */
	.top-navbar {
		box-shadow: 0 0px 0px rgba(64, 158, 255, .3);
		padding: 10px 7% 0;
		border-color: #eee;
		color: #666;
		text-align: right;
		overflow: hidden;
		top: 0;
		left: 0;
		background: #ffffff;
		border-width: 0 0 1px;
		width: 100%;
		font-size: 14px;
		position: fixed;
		border-style: solid;
		height: 64px;
		z-index: 1002;

		.logo-container {
			float: left;
			display: block;

			.logo-text {
				padding: 0 0 0 12px;
				line-height: 44px;
				font-size: 22px;
				color: #F5BB00;
				float: left;
			}
		}

		.user-controls {
			display: inline-block;

			.user-avatar {
				width: 40px;
				margin: 0 0px;
				border-radius: 50%;
				height: 40px;
			}

			.username,
			.user-center {
				padding: 0 12px;
				font-size: inherit;
				line-height: 32px;
				color: inherit;
				display: inline-block;
				height: 32px;
			}

			.user-center {
				cursor: pointer;
			}

			.login-btn {
				border: 0;
				padding: 0 12px;
				margin: 0 10px;
				color: #f4f4f5;
				border-radius: 2px;
				background: #F5BB00;
				display: inline-block;
				font-size: 14px;
				line-height: 32px;
				height: 32px;
			}

			.logout-btn {
				border: 0;
				padding: 0 12px;
				margin: 0 10px;
				color: #fff;
				border-radius: 2px;
				background: #333;
				display: inline-block;
				font-size: 14px;
				line-height: 32px;
				height: 32px;
			}
		}
	}

	/* 主菜单样式 */
	.main-menu {
		padding: 0 7%;
		border-color: #efefef;
		background: #fff;
		border-width: 0 0 1px 0;
		width: 100%;
		border-style: solid;
		height: auto;

		.horizontal-menu {
			border: 0;
			padding: 0;
			list-style: none;
			margin: 0;
			align-items: flex-start;
			background: none;
			display: flex;
			position: relative;
			justify-content: space-between;

			.user-info {
				width: 84px;
				padding: 6px 10px 0;
				display: none;
				height: auto;

				.user-avatar-small {
					width: 100%;
					object-fit: cover;
					border-radius: 20px;
					display: block;
					height: 32px;
				}

				.username-small {
					font-size: 12px;
					line-height: 1.5;
					color: #333;
					text-align: center;
				}
			}

			.menu-item {

				&.home,
				&.user {
					.menu-icon {
						padding: 0 10px;
						margin: 0;
						color: inherit;
						width: 14px;
						line-height: 56px;
						font-size: 14px;
						height: 56px;
					}

					.menu-text {
						padding: 0 10px;
						line-height: 56px;
						font-size: 14px;
						color: inherit;
						height: 56px;
					}
				}
			}
		}
	}

	/* 轮播图样式 */
	.banner-carousel {
		width: 100%;
		padding: 0;
		height: auto;

		.carousel {
			width: 100%;
			margin: 0 auto;
			height: 100%;

			.carousel-item {
				border-radius: 0px;
				width: 100%;
				height: 100%;
			}

			.carousel-image {
				object-fit: cover;
				width: 100%;
				height: 100%;
			}
		}
	}

	/* 页脚样式 */
	.footer {
		width: 100%;
		height: auto;

		.footer-content {
			min-height: 120px;
			padding: 20px 7%;
			overflow: hidden;
			color: #fff;
			text-align: center;
			background: #333;
			width: 100%;
			font-size: 14px;
			height: auto;
		}
	}
}

.menu-preview {
	.el-scrollbar {
		height: 100%;

		& /deep/ .scrollbar-wrapper-vertical {
			overflow-x: hidden;
		}

		& /deep/ .scrollbar-wrapper-horizontal {
			overflow-y: hidden;

			.el-scrollbar__view {
				white-space: nowrap;
			}
		}
	}
}


.menu-preview .el-menu-horizontal-demo .el-menu-item.home {
	cursor: pointer;
	border: 0;
	padding: 0 0px;
	margin: 0;
	color: #333;
	white-space: nowrap;
	display: flex;
	font-size: 14px;
	line-height: 56px;
	background: none;
	align-items: center;
	position: relative;
	list-style: none;
	height: 56px;
}

.menu-preview .el-menu-horizontal-demo .el-menu-item.home:hover {
	color: #F5BB00;
	background: none;
	border-color: #F5BB00;
	border-width: 0 0 2px;
	border-style: solid;
}

.menu-preview .el-menu-horizontal-demo .el-menu-item.home.is-active {
	color: #F5BB00;
	background: none;
	border-color: #F5BB00;
	border-width: 0 0 2px;
	border-style: solid;
}

.menu-preview .el-menu-horizontal-demo .el-menu-item.user {
	cursor: pointer;
	border: 0;
	padding: 0 0px;
	color: #333;
	white-space: nowrap;
	display: none;
	font-size: 14px;
	line-height: 56px;
	background: #fff;
	align-items: center;
	position: relative;
	list-style: none;
	height: 56px;
	order: 2;
}

.menu-preview .el-menu-horizontal-demo .el-menu-item.user:hover {
	color: #F5BB00;
	background: none;
	border-color: #F5BB00;
	border-width: 0 0 2px;
	border-style: solid;
}

.menu-preview .el-menu-horizontal-demo .el-menu-item.user.is-active {
	color: #F5BB00;
	background: none;
	border-color: #F5BB00;
	border-width: 0 0 2px;
	border-style: solid;
}

.menu-preview .el-menu-horizontal-demo .el-menu-item.service {
	cursor: pointer;
	border: 0;
	padding: 0 0px;
	color: #333;
	white-space: nowrap;
	display: none;
	font-size: 14px;
	line-height: 56px;
	background: #fff;
	align-items: center;
	position: relative;
	list-style: none;
	height: 56px;
	order: 3;
}

.menu-preview .el-menu-horizontal-demo .el-menu-item.service:hover {
	color: #F5BB00;
	background: none;
	border-color: #F5BB00;
	border-width: 0 0 2px;
	border-style: solid;
}

.menu-preview .el-menu-horizontal-demo .el-menu-item.service.is-active {
	color: #F5BB00;
	background: none;
	border-color: #F5BB00;
	border-width: 0 0 2px;
	border-style: solid;
}

.menu-preview .el-menu-horizontal-demo .el-menu-item.shop {
	cursor: pointer;
	border: 0;
	padding: 0 0px;
	color: #333;
	white-space: nowrap;
	display: none;
	font-size: 14px;
	line-height: 56px;
	background: #fff;
	align-items: center;
	position: relative;
	list-style: none;
	height: 56px;
	order: 4;
}

.menu-preview .el-menu-horizontal-demo .el-menu-item.shop:hover {
	color: #F5BB00;
	background: none;
	border-color: #F5BB00;
	border-width: 0 0 2px;
	border-style: solid;
}

.menu-preview .el-menu-horizontal-demo .el-menu-item.shop.is-active {
	color: #F5BB00;
	background: none;
	border-color: #F5BB00;
	border-width: 0 0 2px;
	border-style: solid;
}

.menu-preview .el-menu-horizontal-demo .el-menu-item.back {
	cursor: pointer;
	border: 0;
	padding: 0 10px;
	color: #333;
	white-space: nowrap;
	display: flex;
	font-size: 14px;
	line-height: 56px;
	background: #fff;
	align-items: center;
	position: relative;
	list-style: none;
	height: 56px;
	order: 5;
}

.menu-preview .el-menu-horizontal-demo .el-menu-item.back:hover {
	color: #F5BB00;
	background: none;
	border-color: #F5BB00;
	border-width: 0 0 2px;
	border-style: solid;
}

.menu-preview .el-menu-horizontal-demo .el-menu-item.back.is-active {
	color: #F5BB00;
	background: none;
	border-color: #F5BB00;
	border-width: 0 0 2px;
	border-style: solid;
}

.menu-preview .el-menu-horizontal-demo .el-menu-item.item {
	cursor: pointer;
	border: 0;
	padding: 0 0px;
	color: #333;
	white-space: nowrap;
	display: flex;
	font-size: 14px;
	line-height: 56px;
	background: #fff;
	align-items: center;
	position: relative;
	list-style: none;
	height: 56px;
}

.menu-preview .el-menu-horizontal-demo .el-menu-item.item:hover {
	color: #F5BB00;
	background: none;
	border-color: #F5BB00;
	border-width: 0 0 2px;
	border-style: solid;
}

.menu-preview .el-menu-horizontal-demo .el-menu-item.item.is-active {
	color: #F5BB00;
	background: none;
	border-color: #F5BB00;
	border-width: 0 0 2px;
	border-style: solid;
}

.banner-preview {
	.el-carousel /deep/ .el-carousel__indicator button {
		width: 0;
		height: 0;
		display: none;
	}
}

.banner-preview .el-carousel /deep/ .el-carousel__container .el-carousel__arrow--left {
	width: 36px;
	font-size: 12px;
	height: 36px;
}

.banner-preview .el-carousel /deep/ .el-carousel__container .el-carousel__arrow--left:hover {
	background: red;
}

.banner-preview .el-carousel /deep/ .el-carousel__container .el-carousel__arrow--right {
	width: 36px;
	font-size: 12px;
	height: 36px;
}

.banner-preview .el-carousel /deep/ .el-carousel__container .el-carousel__arrow--right:hover {
	background: red;
}

.banner-preview .el-carousel /deep/ .el-carousel__indicators {
	padding: 0;
	margin: 0 0 10px;
	z-index: 2;
	position: absolute;
	list-style: none;
}

.banner-preview .el-carousel /deep/ .el-carousel__indicators li {
	border-radius: 100%;
	padding: 0;
	margin: 0 4px;
	background: #fff;
	display: inline-block;
	width: 12px;
	opacity: 0.4;
	transition: 0.3s;
	height: 12px;
}

.banner-preview .el-carousel /deep/ .el-carousel__indicators li:hover {
	border-radius: 100%;
	padding: 0;
	margin: 0 4px;
	background: #fff;
	display: inline-block;
	width: 12px;
	opacity: 0.7;
	height: 12px;
}

.banner-preview .el-carousel /deep/ .el-carousel__indicators li.is-active {
	border-radius: 100%;
	padding: 0;
	margin: 0 4px;
	background: #fff;
	display: inline-block;
	width: 12px;
	opacity: 1;
	height: 12px;
}

.chat-content {
	padding-bottom: 20px;
	width: 100%;
	margin-bottom: 10px;
	max-height: 300px;
	height: 300px;
	overflow-y: scroll;
	border: 1px solid #eeeeee;
	background: #fff;

	.left-content {
		float: left;
		margin-bottom: 10px;
		padding: 10px;
		max-width: 80%;
	}

	.right-content {
		float: right;
		margin-bottom: 10px;
		padding: 10px;
		max-width: 80%;
	}
}

.clear-float {
	clear: both;
}



// -------- search --------
.main-containers .search .select /deep/ .el-input__inner {
	border: 0;
	border-radius: 4px;
	padding: 0 30px 0 10px;
	box-shadow: 0 0 6px rgba(64, 158, 255, .3);
	outline: none;
	color: rgba(64, 158, 255, 1);
	width: 180px;
	font-size: 14px;
	height: 44px;
}

.main-containers .search .input /deep/ .el-input__inner {
	border: 0;
	border-radius: 4px;
	padding: 0 10px;
	box-shadow: 0 0 6px rgba(64, 158, 255, .3);
	outline: none;
	color: rgba(64, 158, 255, 1);
	width: 180px;
	font-size: 14px;
	height: 44px;
}

// -------- search --------

.main-containers .btn-service {
	border: 0;
	padding: 0 8px;
	margin: 0 10px;
	color: #333;
	background: none;
	width: auto;
	font-size: 14px;
	line-height: 32px;
	height: 32px;
}

.main-containers .btn-service:hover {
	color: #333;
	background: none;
}

.main-containers .btn-shop {
	border: 0;
	padding: 0 8px;
	margin: 0 10px;
	color: #333;
	background: none;
	width: auto;
	font-size: 14px;
	line-height: 32px;
	height: 32px;
}

.main-containers .btn-shop:hover {
	color: #333;
	background: none;
}
</style>
