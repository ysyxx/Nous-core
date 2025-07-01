<template>
	<div class="user-center-container">
		<div class="page-title">{{ title }}</div>

		<div class="user-layout">
			<!-- 个人信息侧边栏 -->
			<div class="user-info-sidebar">
				<div class="sidebar-header">个人信息</div>

				<div class="avatar-section" v-if="userTableName == 'yonghu'">
					<el-image class="user-avatar"
						:src="sessionForm.touxiang ? sessionForm.touxiang : require('@/assets/avator.png')"
						fit="cover">
					</el-image>
				</div>

				<div class="info-item" v-if="userTableName == 'yonghu'">
					<span class="icon iconfont icon-shouye-zhihui"></span>
					<div class="info-label">用户账号</div>
					<div class="info-value">{{ sessionForm.yonghuzhanghao }}</div>
				</div>

				<div class="info-item" v-if="userTableName == 'yonghu'">
					<span class="icon iconfont icon-shouye-zhihui"></span>
					<div class="info-label">用户姓名</div>
					<div class="info-value">{{ sessionForm.yonghuxingming }}</div>
				</div>

				<div class="info-item" v-if="userTableName == 'yonghu'">
					<span class="icon iconfont icon-shouye-zhihui"></span>
					<div class="info-label">性别</div>
					<div class="info-value">{{ sessionForm.xingbie }}</div>
				</div>

				<div class="info-item" v-if="userTableName == 'yonghu'">
					<span class="icon iconfont icon-shouye-zhihui"></span>
					<div class="info-label">电话</div>
					<div class="info-value">{{ sessionForm.dianhua }}</div>
				</div>
			</div>

			<!-- 主内容区域 -->
			<div class="main-content">
				<el-tabs class="evenly-distributed-tabs" v-model="activeName" type="card" @tab-click="handleClick">
					<!-- 个人中心标签页 -->
					<el-tab-pane label="个人中心">
						<el-form class="user-form" ref="sessionForm" :model="sessionForm" :rules="rules"
							label-width="180px">
							<el-form-item v-if="userTableName == 'yonghu'" label="用户账号" prop="yonghuzhanghao">
								<el-input v-model="sessionForm.yonghuzhanghao" placeholder="用户账号" readonly></el-input>
							</el-form-item>

							<el-form-item v-if="userTableName == 'yonghu'" label="用户姓名" prop="yonghuxingming">
								<el-input v-model="sessionForm.yonghuxingming" placeholder="用户姓名"></el-input>
							</el-form-item>

							<el-form-item v-if="userTableName == 'yonghu'" label="性别" prop="xingbie">
								<el-select v-model="sessionForm.xingbie" placeholder="请选择性别">
									<el-option v-for="(item, index) in dynamicProp.xingbie" :key="index" :label="item"
										:value="item">
									</el-option>
								</el-select>
							</el-form-item>

							<el-form-item v-if="userTableName == 'yonghu'" label="电话" prop="dianhua">
								<el-input v-model="sessionForm.dianhua" placeholder="电话"></el-input>
							</el-form-item>

							<el-form-item v-if="userTableName == 'yonghu'" label="头像" prop="touxiang">
								<file-upload tip="点击上传头像" action="file/upload" :limit="1" :multiple="true"
									:fileUrls="sessionForm.touxiang ? sessionForm.touxiang : ''"
									@change="yonghutouxiangHandleAvatarSuccess">
								</file-upload>
							</el-form-item>

							<el-form-item class="form-actions">
								<el-button class="update-btn" type="primary"
									@click="onSubmit('sessionForm')">更新信息</el-button>
								<el-button class="logout-btn" type="danger" @click="logout">退出登录</el-button>
							</el-form-item>
						</el-form>
					</el-tab-pane>

					<!-- 修改密码标签页 -->
					<el-tab-pane label="修改密码">
						<el-form class="user-form" ref="passwordForm" :model="passwordForm" :rules="passwordRules"
							label-width="180px">
							<el-form-item label="原密码" prop="password">
								<el-input type="password" v-model="passwordForm.password" placeholder="原密码"></el-input>
							</el-form-item>

							<el-form-item label="新密码" prop="newpassword">
								<el-input type="password" v-model="passwordForm.newpassword"
									placeholder="新密码"></el-input>
							</el-form-item>

							<el-form-item label="确认密码" prop="repassword">
								<el-input type="password" v-model="passwordForm.repassword"
									placeholder="确认密码"></el-input>
							</el-form-item>

							<el-form-item class="form-actions">
								<el-button class="update-btn" type="primary" @click="updatePassword">修改密码</el-button>
							</el-form-item>
						</el-form>
					</el-tab-pane>

					<!-- 动态标签页 -->
					<el-tab-pane v-for="(item, index) in menuList" :key="index" v-if="hasBack(item.menu)"
						:label="item.child[0].menu" :name="item.child[0].tableName">
					</el-tab-pane>

					<el-tab-pane label="我的发布"></el-tab-pane>
					<el-tab-pane label="我的收藏"></el-tab-pane>
				</el-tabs>
			</div>
		</div>
	</div>
</template>

<script>
import config from '@/config/config'
import menu from '@/config/menu'
import Vue from 'vue'
export default {
	//数据集合
	data() {
		return {
			title: '个人中心',
			baseUrl: config.baseUrl,
			sessionForm: {},
			passwordForm: {},
			passwordRules: {
				password: [
					{
						required: true,
						message: "密码不能为空",
						trigger: "blur"
					}
				],
				newpassword: [
					{
						required: true,
						message: "新密码不能为空",
						trigger: "blur"
					}
				],
				repassword: [
					{
						required: true,
						message: "确认密码不能为空",
						trigger: "blur"
					}
				]
			},
			rules: {},
			menuList: [],
			disabled: false,
			uploadUrl: config.baseUrl + 'file/upload',
			imageUrl: '',
			headers: { Token: localStorage.getItem('frontToken') },
			userTableName: localStorage.getItem('UserTableName'),
			dynamicProp: {},
		}
	},
	created() {
		let menus = menu.list()
		for (let x in menus) {
			if (menus[x].tableName == this.userTableName) {
				this.menuList = menus[x].backMenu
			}
		}
		if ('yonghu' == this.userTableName) {
			this.$set(this.sessionForm, 'yonghuzhanghao', null);
		}
		if ('yonghu' == this.userTableName) {
			this.$set(this.sessionForm, 'mima', null);
		}
		if ('yonghu' == this.userTableName) {
			this.$set(this.sessionForm, 'yonghuxingming', null);
		}
		if ('yonghu' == this.userTableName) {
			this.$set(this.sessionForm, 'xingbie', null);
		}
		if ('yonghu' == this.userTableName) {
			this.$set(this.sessionForm, 'dianhua', null);
		}
		if ('yonghu' == this.userTableName) {
			this.$set(this.sessionForm, 'touxiang', null);
		}

		if ('yonghu' == this.userTableName && this.rules['yonghuzhanghao']) {
			this.rules['yonghuzhanghao'].push({ required: true, message: '请输入用户账号', trigger: 'blur' })
		} else if ('yonghu' == this.userTableName && !this.rules['yonghuzhanghao']) {
			this.$set(this.rules, 'yonghuzhanghao', [{ required: true, message: '请输入用户账号', trigger: 'blur' }]);
		}
		if ('yonghu' == this.userTableName && this.rules['mima']) {
			this.rules['mima'].push({ required: true, message: '请输入密码', trigger: 'blur' })
		} else if ('yonghu' == this.userTableName && !this.rules['mima']) {
			this.$set(this.rules, 'mima', [{ required: true, message: '请输入密码', trigger: 'blur' }]);
		}
		if ('yonghu' == this.userTableName && this.rules['yonghuxingming']) {
			this.rules['yonghuxingming'].push({ required: true, message: '请输入用户姓名', trigger: 'blur' })
		} else if ('yonghu' == this.userTableName && !this.rules['yonghuxingming']) {
			this.$set(this.rules, 'yonghuxingming', [{ required: true, message: '请输入用户姓名', trigger: 'blur' }]);
		}
		if ('yonghu' == this.userTableName) {
			this.$set(this.rules, 'dianhua', [{ required: false, validator: this.$validate.isMobile, trigger: 'blur' }]);
		}

		this.init();
		this.sessionForm = JSON.parse(localStorage.getItem('sessionForm'))
		console.log(this.sessionForm)
	},
	//方法集合
	methods: {
		init() {
			if ('yonghu' == this.userTableName) {
				this.dynamicProp.xingbie = '男,女'.split(',');
			}
		},
		setSession() {
			localStorage.setItem('sessionForm', JSON.stringify(this.sessionForm))
		},
		onSubmit(formName) {
			if (`yonghu` == this.userTableName && this.sessionForm.touxiang != null) {
				this.sessionForm.touxiang = this.sessionForm.touxiang.replace(new RegExp(this.$config.baseUrl, "g"), "");
			}
			this.$refs[formName].validate((valid) => {
				if (valid) {
					this.$http.post(this.userTableName + '/update', this.sessionForm).then(res => {
						if (res.data.code == 0) {
							this.setSession()
							this.$message({
								message: '更新成功',
								type: 'success',
								duration: 1500
							});
						}
					});
				} else {
					return false;
				}
			});
		},
		yonghutouxiangHandleAvatarSuccess(fileUrls) {
			this.sessionForm.touxiang = fileUrls;
		},
		handleClick(tab, event) {
			switch (event.target.outerText) {
				case '个人中心':
					tab.$router.push('/index/center');
					break;
				case '修改密码':
					this.passwordForm = {
						password: '',
						newpassword: '',
						repassword: '',
					}
					this.$forceUpdate()
					break;
				case '我的发布':
					tab.$router.push('/index/myForumList');
					break;
				case '我的收藏':
					localStorage.setItem('storeupType', 1);
					tab.$router.push('/index/storeup');
					break;
				default:
					tab.$router.push(`/index/${tab.name}?centerType=1`);
			}

			this.title = event.target.outerText;
		},
		async updatePassword() {
			this.$refs["passwordForm"].validate(async valid => {
				if (valid) {
					var password = "";
					if (this.sessionForm.mima) {
						password = this.sessionForm.mima;
					} else if (this.sessionForm.password) {
						password = this.sessionForm.password;
					}
					if (this.passwordForm.password != password) {
						this.$message.error("原密码错误");
						return;
					}
					if (this.passwordForm.newpassword != this.passwordForm.repassword) {
						this.$message.error("两次密码输入不一致");
						return;
					}
					if (this.userTableName == 'yonghu') {
					}
					this.sessionForm.password = this.passwordForm.newpassword;
					this.sessionForm.mima = this.passwordForm.newpassword;
					this.$http.post(`${this.userTableName}/update`, this.sessionForm).then(({ data }) => {
						if (data && data.code === 0) {
							this.$message({
								message: "修改密码成功,下次登录系统生效",
								type: "success",
								duration: 1500,
								onClose: () => {
								}
							});
							this.setSession()
						} else {
							this.$message.error(data.msg);
						}
					});
				}
			})
		},
		logout() {
			localStorage.clear();
			Vue.http.headers.common['Token'] = "";
			this.$router.push('/index/home');
			this.activeIndex = '0'
			localStorage.setItem('keyPath', this.activeIndex)
			this.$forceUpdate()
			this.$message({
				message: '登出成功',
				type: 'success',
				duration: 1500,
			});
		},
		hasBack(name) {
			switch (name) {
				case '我的收藏管理':
					return false
					break;
				default:
					return true
			}
		}
	}
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.user-center-container {
	padding: 0 7%;
	margin: 20px auto 40px;
	background: #fff;
	width: 100%;
	position: relative;
}

.page-title {
	margin: 10px 0;
	color: #fff;
	text-align: center;
	background: #333;
	width: 100%;
	font-size: 20px;
	line-height: 54px;
}

.user-layout {
	display: flex;
	justify-content: space-between;
	margin-top: 20px;
}

/* 个人信息侧边栏样式 */
.user-info-sidebar {
	padding: 10px;
	box-shadow: 0 0px 0px rgba(0, 0, 0, 0.3);
	margin-right: 3%;
	border: 4px solid #eee;
	border-width: 4px 2px 1px;
	border-radius: 0px;
	background: #ffffff;
	width: 25%;
}

.sidebar-header {
	padding: 0 20px;
	border-bottom: 1px solid #efefef;
	color: #fff;
	background: #F5BB00;
	width: 100%;
	line-height: 44px;
	font-size: 15px;
	height: 44px;
}

.avatar-section {
	border-bottom: 1px solid #efefef;
	margin: 0 auto;
	width: 90%;
	font-size: 0;
	padding: 10px 0;
}

.user-avatar {
	margin: 10px auto;
	border-radius: 100%;
	display: block;
	width: 80px;
	height: 80px;
	object-fit: cover;
	}
	
	.info-item {
		padding: 0 20px;
		border-bottom: 1px solid #efefef;
		margin: 0 auto;
		display: flex;
		width: 90%;
		line-height: 40px;
		height: auto;
		align-items: center;
	}
	
	.info-item .icon {
		color: #333;
		padding: 0 5px;
		font-size: 14px;
	}
	
	.info-label {
		color: #333;
		font-size: 14px;
		margin-right: 10px;
	}
	
	.info-value {
		font-size: 14px;
		color: #333;
		text-align: right;
		flex: 1;
	}
	
	/* 主内容区域样式 */
	.main-content {
		width: 72%;
		margin: 20px 0 0;
		background: #fff;
		flex: 1;
	}
	
	.user-tabs {
		width: 100%;
	}
	
	.user-form {
		padding: 20px;
	}
	
	.form-actions {
		padding: 0;
		margin: 20px 0 0;
	}
	
	.update-btn {
		border: 0;
		cursor: pointer;
		padding: 0;
		margin-right: 20px;
		outline: none;
		color: #fff;
		border-radius: 4px;
		background: #F5BB00;
		width: 128px;
		line-height: 40px;
		font-size: 14px;
		height: 40px;
	}
	
	.logout-btn {
		border: 0;
		cursor: pointer;
		padding: 0;
		outline: none;
		color: #fff;
		border-radius: 4px;
		background: #333;
		width: 128px;
		line-height: 40px;
		font-size: 14px;
		height: 40px;
	}
	
/* 完全重写选项卡样式 */
.evenly-distributed-tabs {
  width: 100%;
}

.evenly-distributed-tabs::v-deep .el-tabs__header {
  margin: 0;
}

.evenly-distributed-tabs::v-deep .el-tabs__nav-wrap {
  padding: 0;
}

.evenly-distributed-tabs::v-deep .el-tabs__nav {
  display: flex;
  width: 100%;
  justify-content: space-between;
}

.evenly-distributed-tabs::v-deep .el-tabs__item {
  flex: 1;
  text-align: center;
  padding: 0 12px;
  height: 40px;
  line-height: 40px;
  margin: 0;
  border: 1px solid #e4e7ed;
  border-bottom: none;
  background: #f5f7fa;
  transition: all 0.3s;
  position: relative;
  box-sizing: border-box;
}

/* 移除默认的激活条 */
.evenly-distributed-tabs::v-deep .el-tabs__active-bar {
  display: none;
}

/* 激活状态样式 */
.evenly-distributed-tabs::v-deep .el-tabs__item.is-active {
  background: #fff;
  color: #409EFF;
  border-bottom: 1px solid #fff;
  margin-bottom: -1px;
  font-weight: bold;
}

/* 悬停效果 */
.evenly-distributed-tabs::v-deep .el-tabs__item:hover {
  color: #409EFF;
}

/* 第一个和最后一个选项卡的圆角处理 */
.evenly-distributed-tabs::v-deep .el-tabs__item:first-child {
  border-top-left-radius: 4px;
}

.evenly-distributed-tabs::v-deep .el-tabs__item:last-child {
  border-top-right-radius: 4px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .evenly-distributed-tabs::v-deep .el-tabs__nav {
    flex-wrap: wrap;
  }
  
  .evenly-distributed-tabs::v-deep .el-tabs__item {
    flex: 0 0 50%;
    border-bottom: 1px solid #e4e7ed;
  }
  
  .evenly-distributed-tabs::v-deep .el-tabs__item:nth-child(odd) {
    border-right: 1px solid #e4e7ed;
  }
  
  .evenly-distributed-tabs::v-deep .el-tabs__item.is-active {
    border-bottom: 1px solid #fff;
  }
}
	
	.center-preview {
	
		.el-tabs {
			& /deep/ .el-tabs__header {
				.el-tabs__nav {
					overflow: auto;
				}
	
				::-webkit-scrollbar {
					-webkit-appearance: none;
					width: 6px;
					height: 6px;
				}
	
				::-webkit-scrollbar-track {
					background: rgba(0, 0, 0, 0.1);
					border-radius: 0;
				}
	
				::-webkit-scrollbar-thumb {
					cursor: pointer;
					border-radius: 5px;
					background: rgba(0, 0, 0, 0.15);
					transition: color 0.2s ease;
				}
	
				::-webkit-scrollbar-thumb:hover {
					background: rgba(0, 0, 0, 0.3);
				}
	
				.el-tabs__nav-wrap {
					margin: 0;
	
					&::after {
						content: none;
					}
				}
	
				.el-tabs__active-bar {
					display: none !important;
				}
			}
	
			.center-preview-pv {
				.el-date-editor.el-input {
					width: auto;
				}
	
				.balance {
					.el-input {
						width: auto;
					}
				}
			}
		}
	}
	
	.center-preview .el-tabs /deep/ .el-tabs__header {
		padding: 0;
		margin: 0;
		background: #fff;
		width: 100%;
		border-color: #eee;
		border-width: 0 0 1px;
		position: relative;
		float: left;
		border-style: solid;
		height: auto;
	}
	
	.center-preview .el-tabs /deep/ .el-tabs__header .el-tabs__item {
		padding: 0 10px;
		margin: 0 10px 10px 0;
		color: #fff;
		font-weight: 500;
		display: block;
		font-size: 14px;
		line-height: 40px;
		float: left;
		background: #333;
		width: auto;
		position: relative;
		text-align: center;
		height: 40px;
	}
	
	.center-preview .el-tabs /deep/ .el-tabs__header .el-tabs__item:hover {
		padding: 0 10px;
		color: #fff;
		background: #F5BB00;
		font-weight: 500;
		font-size: 14px;
		line-height: 40px;
		position: relative;
		text-align: center;
		height: 40px;
	}
	
	.center-preview .el-tabs /deep/ .el-tabs__header .el-tabs__item.is-active {
		padding: 0 10px;
		margin: 0 10px 10px 0;
		color: #fff;
		font-weight: 500;
		display: block;
		font-size: 14px;
		line-height: 40px;
		float: left;
		background: #F5BB00;
		width: auto;
		position: relative;
		text-align: center;
		height: 40px;
	}
	
	.center-preview .el-tabs /deep/ .el-tabs__content {
		padding: 0px;
		margin: 20px 0 0;
		background: #fff;
		width: 100%;
	}
	
	.center-preview .el-tabs /deep/ .el-tabs__content .el-tab-pane {
		width: 100%;
	}
	
	.center-preview-pv .el-form-item /deep/ .el-form-item__label {
		padding: 0 10px 0 0;
		color: #666;
		font-weight: 500;
		width: 180px;
		font-size: 14px;
		line-height: 40px;
		text-align: right;
	}
	
	.center-preview-pv .el-form-item .el-form-item__content {
		margin-left: 180px;
	}
	
	.center-preview-pv .el-input /deep/ .el-input__inner {
		border: 1px solid #ddd;
		border-radius: 4px;
		padding: 0 12px;
		box-shadow: 0 0 0px rgba(64, 158, 255, .5);
		outline: none;
		color: #666;
		width: 400px;
		font-size: 14px;
		height: 40px;
	}
	
	.center-preview-pv .el-select /deep/ .el-input__inner {
		border: 1px solid #ddd;
		border-radius: 4px;
		padding: 0 12px;
		box-shadow: 0 0 0px rgba(64, 158, 255, .5);
		outline: none;
		color: #666;
		width: 400px;
		font-size: 14px;
		height: 40px;
}

.center-preview-pv .el-date-editor /deep/ .el-input__inner {
	border: 1px solid #ddd;
	border-radius: 4px;
	padding: 0 10px 0 30px;
	box-shadow: 0 0 0px rgba(64, 158, 255, .5);
	outline: none;
	color: #666;
	width: 200px;
	font-size: 14px;
	height: 40px;
}

.center-preview-pv /deep/ .avatar-uploader-icon {
	border: 1px solid #ddd;
	cursor: pointer;
	border-radius: 6px;
	color: #999;
	width: 160px;
	font-size: 32px;
	line-height: 80px;
	text-align: center;
	height: 80px;
}

.center-preview-pv .el-form-item.balance /deep/ .el-input__inner {
	border: 1px solid #ddd;
	border-radius: 4px 0 0 4px;
	padding: 0 12px;
	box-shadow: 0 0 0px rgba(64, 158, 255, .5);
	outline: none;
	color: #666;
	display: inline-block;
	width: 200px;
	font-size: 14px;
	height: 40px;
}
</style>
