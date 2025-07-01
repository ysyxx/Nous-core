<template>
	<div class="register-page">
		<div class="register-bg">
			<el-form class="register-form" v-if="pageFlag == 'register'" ref="registerForm" :model="registerForm"
				:rules="rules">
				<div class="hidden-title">USER / REGISTER</div>
				<div class="main-title">智核NousCore注册</div>

				<el-form-item class="form-item" v-if="tableName == 'yonghu'" prop="yonghuzhanghao">
					<div class="form-label" :class="changeRules('yonghuzhanghao') ? 'required' : ''">用户账号：</div>
					<el-input v-model="registerForm.yonghuzhanghao" placeholder="请输入用户账号" />
				</el-form-item>

				<el-form-item class="form-item" v-if="tableName == 'yonghu'" prop="mima">
					<div class="form-label" :class="changeRules('mima') ? 'required' : ''">密码：</div>
					<el-input v-model="registerForm.mima" type="password" placeholder="请输入密码" />
				</el-form-item>

				<el-form-item class="form-item" v-if="tableName == 'yonghu'" prop="mima2">
					<div class="form-label" :class="changeRules('mima') ? 'required' : ''">确认密码：</div>
					<el-input v-model="registerForm.mima2" type="password" placeholder="请再次输入密码" />
				</el-form-item>

				<el-form-item class="form-item" v-if="tableName == 'yonghu'" prop="yonghuxingming">
					<div class="form-label" :class="changeRules('yonghuxingming') ? 'required' : ''">用户姓名：</div>
					<el-input v-model="registerForm.yonghuxingming" placeholder="请输入用户姓名" />
				</el-form-item>

				<el-form-item class="form-item" v-if="tableName == 'yonghu'" prop="xingbie">
					<div class="form-label" :class="changeRules('xingbie') ? 'required' : ''">性别：</div>
					<el-select v-model="registerForm.xingbie" placeholder="请选择性别">
						<el-option v-for="(item, index) in yonghuxingbieOptions" :key="index" :label="item"
							:value="item">
						</el-option>
					</el-select>
				</el-form-item>

				<el-form-item class="form-item" v-if="tableName == 'yonghu'" prop="dianhua">
					<div class="form-label" :class="changeRules('dianhua') ? 'required' : ''">电话：</div>
					<el-input v-model="registerForm.dianhua" placeholder="请输入电话" />
				</el-form-item>

				<el-form-item class="form-item" v-if="tableName == 'yonghu'" prop="touxiang">
					<div class="form-label" :class="changeRules('touxiang') ? 'required' : ''">头像：</div>
					<file-upload tip="点击上传头像" action="file/upload" :limit="1" :multiple="true"
						:fileUrls="registerForm.touxiang ? registerForm.touxiang : ''"
						@change="yonghutouxiangUploadChange"></file-upload>
				</el-form-item>

				<el-button class="submit-btn" type="primary" @click="submitForm('registerForm')">注册</el-button>
				<el-button class="reset-btn" @click="resetForm('registerForm')">重置</el-button>
				<router-link class="login-link" to="/login">已有账户登录</router-link>

				<div class="idea1"></div>
				<div class="idea2"></div>
			</el-form>
		</div>
	</div>
</template>

<script>

export default {
	//数据集合
	data() {
		return {
			pageFlag: '',
			tableName: '',
			registerForm: {},
			forgetForm: {},
			rules: {},
			requiredRules: {},
			yonghuxingbieOptions: [],
		}
	},
	mounted() {
		if (this.$route.query.pageFlag == 'register') {
			this.tableName = this.$route.query.role;
			if (this.tableName == 'yonghu') {
				this.registerForm = {
					yonghuzhanghao: '',
					mima: '',
					mima2: '',
					yonghuxingming: '',
					xingbie: '',
					dianhua: '',
					touxiang: '',
				}
			}
			if ('yonghu' == this.tableName) {
				this.requiredRules.yonghuzhanghao = [{ required: true, message: '请输入用户账号', trigger: 'blur' }]
			}
			if ('yonghu' == this.tableName) {
				this.requiredRules.mima = [{ required: true, message: '请输入密码', trigger: 'blur' }]
			}
			if ('yonghu' == this.tableName) {
				this.requiredRules.yonghuxingming = [{ required: true, message: '请输入用户姓名', trigger: 'blur' }]
			}
		}
	},
	created() {
		this.pageFlag = this.$route.query.pageFlag;
		if (this.$route.query.pageFlag == 'register') {
			if ('yonghu' == this.tableName) {
				this.rules.yonghuzhanghao = [{ required: true, message: '请输入用户账号', trigger: 'blur' }];
			}
			if ('yonghu' == this.tableName) {
				this.rules.mima = [{ required: true, message: '请输入密码', trigger: 'blur' }];
			}
			if ('yonghu' == this.tableName) {
				this.rules.yonghuxingming = [{ required: true, message: '请输入用户姓名', trigger: 'blur' }];
			}
			this.yonghuxingbieOptions = "男,女".split(',');
			if ('yonghu' == this.tableName) {
				this.rules.dianhua = [{ required: true, validator: this.$validate.isMobile, trigger: 'blur' }];
			}
		}
	},
	//方法集合
	methods: {
		changeRules(name) {
			if (this.requiredRules[name]) {
				return true
			}
			return false
		},
		// 获取uuid
		getUUID() {
			return new Date().getTime();
		},
		// 下二随
		yonghutouxiangUploadChange(fileUrls) {
			this.registerForm.touxiang = fileUrls.replace(new RegExp(this.$config.baseUrl, "g"), "");
		},

		// 多级联动参数


		submitForm(formName) {
			this.$refs[formName].validate((valid) => {
				if (valid) {
					var url = this.tableName + "/register";
					if ((!this.registerForm.yonghuzhanghao) && `yonghu` == this.tableName) {
						this.$message.error(`用户账号不能为空`);
						return
					}
					if (`yonghu` == this.tableName && this.registerForm.mima != this.registerForm.mima2) {
						this.$message.error(`两次密码输入不一致`);
						return
					}
					if ((!this.registerForm.mima) && `yonghu` == this.tableName) {
						this.$message.error(`密码不能为空`);
						return
					}
					if ((!this.registerForm.yonghuxingming) && `yonghu` == this.tableName) {
						this.$message.error(`用户姓名不能为空`);
						return
					}
					if (`yonghu` == this.tableName && this.registerForm.dianhua && (!this.$validate.isMobile2(this.registerForm.dianhua))) {
						this.$message.error(`电话应输入手机格式`);
						return
					}
					this.$http.post(url, this.registerForm).then(res => {
						if (res.data.code === 0) {
							this.$message({
								message: '注册成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.$router.push('/login');
								}
							});
						} else {
							this.$message.error(res.data.msg);
						}
					});
				} else {
					return false;
				}
			});
		},
		resetForm(formName) {
			this.$refs[formName].resetFields();
		}
	}
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.register-page {
  min-height: 100vh;
  width: 100%;
  position: relative;
  overflow: hidden;
}

.register-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: url(https://nous-core.obs.cn-north-4.myhuaweicloud.com/images/W%60%40P4H_AU%7DJD%60G0W9DE9CYQ.png);
  background-size: cover;
  background-position: center center;
  background-repeat: no-repeat;
  z-index: 1;
}

.register-form {
  position: fixed;
  right: 10%;  /* 调整右侧间距 */
  top: 50%;
  transform: translateY(-50%);
  width: 30vw;
  padding: 20px;
  border-radius: 0px;
  background: #fff;
  height: auto;
  z-index: 2;
  max-height: 90vh; /* 防止表单过高 */
  overflow-y: auto; /* 如果内容过多可以滚动 */
   /* 隐藏滚动条但保留滚动功能 */
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE和Edge */
}

.hidden-title {
	width: 100%;
	margin: 0 0 10px 0;
	line-height: 44px;
	font-size: 20px;
	color: rgba(64, 158, 255, 1);
	text-align: center;
	display: none;
}

.main-title {
	width: 80%;
	margin: 20px auto 10px;
	line-height: 44px;
	font-size: 28px;
	color: #333;
	text-align: left;
}

.form-item {
	width: auto;
	padding: 0;
	margin: 0 auto 15px 10%;
	background: none;
	height: auto;
}

.form-label {
	width: 100%;
	line-height: 36px;
	font-size: 14px;
	color: #333;
}

.form-label.required:before {
	content: "*";
	color: red;
	margin-right: 4px;
}

.submit-btn {
	border: 0;
	cursor: pointer;
	padding: 0 10px;
	margin: 20px auto 5px 10%;
	color: #fff;
	display: block;
	min-width: 400px;
	outline: none;
	border-radius: 4px;
	background: #ffc000;
	width: auto;
	font-size: 16px;
	height: 44px;
}

.reset-btn {
	border: 0;
	cursor: pointer;
	padding: 0 10px;
	margin: 0px auto 0px 10%;
	color: #666;
	text-align: right;
	display: block;
	min-width: 400px;
	outline: none;
	border-radius: 8px;
	background: none;
	width: auto;
	font-size: 16px;
	height: 44px;
}

.login-link {
	cursor: pointer;
	padding: 0 10%;
	margin: 0 0 40px;
	color: rgba(159, 159, 159, 1);
	display: inline-block;
	line-height: 1;
	font-size: 14px;
	text-decoration: none;
}

.idea1,
.idea2 {
	width: 100%;
	background: red;
	display: none;
	height: 40px;
}

.idea2 {
	background: blue;
}

.container {
	position: relative;
	background: url(https://nous-core.obs.cn-north-4.myhuaweicloud.com/images/W%60%40P4H_AU%7DJD%60G0W9DE9CYQ.png);

	.el-input {
		width: auto;
	}

	.el-date-editor.el-input {
		width: auto;
	}

	.el-form-item /deep/ .el-form-item__content {
		display: block;
		width: 100%;
	}

	.rgs-form .el-input /deep/ .el-input__inner {
		border: 1px solid #ddd;
		border-radius: 4px;
		padding: 0 10px;
		outline: none;
		color: #666;
		width: 100%;
		font-size: 14px;
		min-width: 400px;
		height: 40px;
	}

	.rgs-form .el-select /deep/ .el-input__inner {
		border: 1px solid #ddd;
		border-radius: 4px;
		padding: 0 10px;
		outline: none;
		color: #666;
		width: 100%;
		font-size: 14px;
		min-width: 400px;
		height: 40px;
	}

	.rgs-form .el-date-editor /deep/ .el-input__inner {
		border: 1px solid #ddd;
		border-radius: 4px;
		padding: 0 10px 0 30px;
		outline: none;
		color: #666;
		width: 1;
		font-size: 14px;
		min-width: 400px;
		height: 40px;
	}

	.rgs-form .el-date-editor /deep/ .el-input__inner {
		border: 1px solid #ddd;
		border-radius: 4px;
		padding: 0 10px 0 30px;
		outline: none;
		color: #666;
		width: 1;
		font-size: 14px;
		min-width: 400px;
		height: 40px;
	}

	.rgs-form /deep/ .el-upload--picture-card {
		background: transparent;
		border: 0;
		border-radius: 0;
		width: auto;
		height: auto;
		line-height: initial;
		vertical-align: middle;
	}

	.rgs-form /deep/ .upload .upload-img {
		border: 1px solid #ddd;
		cursor: pointer;
		border-radius: 4px;
		color: #999;
		width: 160px;
		font-size: 32px;
		line-height: 80px;
		text-align: center;
		height: 80px;
	}

	.rgs-form /deep/ .el-upload-list .el-upload-list__item {
		border: 1px solid #ddd;
		cursor: pointer;
		border-radius: 4px;
		color: #999;
		width: 160px;
		font-size: 32px;
		line-height: 80px;
		text-align: center;
		height: 80px;
	}

	.rgs-form /deep/ .el-upload .el-icon-plus {
		border: 1px solid #ddd;
		cursor: pointer;
		border-radius: 4px;
		color: #999;
		width: 160px;
		font-size: 32px;
		line-height: 80px;
		text-align: center;
		height: 80px;
	}
}

.required {
	position: relative;
}

.required::after {
	color: red;
	left: -10px;
	position: absolute;
	content: "*";
}
</style>
