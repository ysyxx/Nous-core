<template>
	<div class="form-container">
		<el-form class="form-preview" ref="ruleForm" :model="ruleForm" :rules="rules" label-width="180px">
			<div class="form-content">
				<el-form-item class="form-item" label="用户账号" prop="yonghuzhanghao">
					<el-input v-model="ruleForm.yonghuzhanghao" placeholder="用户账号" clearable
						:disabled="false || ro.yonghuzhanghao">
					</el-input>
				</el-form-item>

				<el-form-item class="form-item" label="密码" prop="mima">
					<el-input v-model="ruleForm.mima" placeholder="密码" clearable :disabled="false || ro.mima">
					</el-input>
				</el-form-item>

				<el-form-item class="form-item" label="用户姓名" prop="yonghuxingming">
					<el-input v-model="ruleForm.yonghuxingming" placeholder="用户姓名" clearable
						:disabled="false || ro.yonghuxingming">
					</el-input>
				</el-form-item>

				<el-form-item class="form-item" label="性别" prop="xingbie">
					<el-select v-model="ruleForm.xingbie" placeholder="请选择性别" :disabled="false || ro.xingbie">
						<el-option v-for="(item, index) in xingbieOptions" :key="index" :label="item" :value="item">
						</el-option>
					</el-select>
				</el-form-item>

				<el-form-item class="form-item" label="电话" prop="dianhua">
					<el-input v-model="ruleForm.dianhua" placeholder="电话" clearable :disabled="false || ro.dianhua">
					</el-input>
				</el-form-item>

				<el-form-item class="form-item" label="头像" v-if="type != 'cross' || (type == 'cross' && !ro.touxiang)"
					prop="touxiang">
					<file-upload tip="点击上传头像" action="file/upload" :limit="3" :multiple="true"
						:fileUrls="ruleForm.touxiang ? ruleForm.touxiang : ''" @change="touxiangUploadChange">
					</file-upload>
				</el-form-item>
				<el-form-item class="form-item upload" v-else label="头像" prop="touxiang">
					<img v-if="ruleForm.touxiang.substring(0, 4) == 'http'" class="upload-img"
						:src="ruleForm.touxiang.split(',')[0]" width="100" height="100">
					<img v-else class="upload-img" style="margin-right:20px;" v-bind:key="index"
						v-for="(item, index) in ruleForm.touxiang.split(',')" :src="baseUrl + item" width="100"
						height="100">
				</el-form-item>

				<el-form-item class="form-actions">
					<el-button class="submit-button" type="primary" @click="onSubmit">提交</el-button>
					<el-button class="back-button" @click="back()">返回</el-button>
				</el-form-item>
			</div>
		</el-form>
	</div>
</template>

<script>
export default {
	data() {
		let self = this
		return {
			id: '',
			baseUrl: '',
			ro: {
				yonghuzhanghao: false,
				mima: false,
				yonghuxingming: false,
				xingbie: false,
				dianhua: false,
				touxiang: false,
			},
			type: '',
			userTableName: localStorage.getItem('UserTableName'),
			ruleForm: {
				yonghuzhanghao: '',
				mima: '',
				yonghuxingming: '',
				xingbie: '',
				dianhua: '',
				touxiang: '',
			},
			xingbieOptions: [],


			rules: {
				yonghuzhanghao: [
					{ required: true, message: '用户账号不能为空', trigger: 'blur' },
				],
				mima: [
					{ required: true, message: '密码不能为空', trigger: 'blur' },
				],
				yonghuxingming: [
					{ required: true, message: '用户姓名不能为空', trigger: 'blur' },
				],
				xingbie: [
				],
				dianhua: [
					{ validator: this.$validate.isMobile, trigger: 'blur' },
				],
				touxiang: [
				],
			},
			centerType: false,
		};
	},
	computed: {



	},
	components: {
	},
	created() {
		if (this.$route.query.centerType) {
			this.centerType = true
		}
		//this.bg();
		let type = this.$route.query.type ? this.$route.query.type : '';
		this.init(type);
		this.baseUrl = this.$config.baseUrl;
	},
	methods: {
		getMakeZero(s) {
			return s < 10 ? '0' + s : s;
		},
		// 下载
		download(file) {
			window.open(`${file}`)
		},
		// 初始化
		init(type) {
			this.type = type;
			if (type == 'cross') {
				var obj = JSON.parse(localStorage.getItem('crossObj'));
				for (var o in obj) {
					if (o == 'yonghuzhanghao') {
						this.ruleForm.yonghuzhanghao = obj[o];
						this.ro.yonghuzhanghao = true;
						continue;
					}
					if (o == 'mima') {
						this.ruleForm.mima = obj[o];
						this.ro.mima = true;
						continue;
					}
					if (o == 'yonghuxingming') {
						this.ruleForm.yonghuxingming = obj[o];
						this.ro.yonghuxingming = true;
						continue;
					}
					if (o == 'xingbie') {
						this.ruleForm.xingbie = obj[o];
						this.ro.xingbie = true;
						continue;
					}
					if (o == 'dianhua') {
						this.ruleForm.dianhua = obj[o];
						this.ro.dianhua = true;
						continue;
					}
					if (o == 'touxiang') {
						this.ruleForm.touxiang = obj[o].split(",")[0];
						this.ro.touxiang = true;
						continue;
					}
				}
			} else if (type == 'edit') {
				this.info()
			}
			// 获取用户信息
			this.$http.get(this.userTableName + '/session', { emulateJSON: true }).then(res => {
				if (res.data.code == 0) {
					var json = res.data.data;
				}
			});
			this.xingbieOptions = "男,女".split(',')

			if (localStorage.getItem('raffleType') && localStorage.getItem('raffleType') != null) {
				localStorage.removeItem('raffleType')
				setTimeout(() => {
					this.onSubmit()
				}, 300)
			}
		},

		// 多级联动参数
		// 多级联动参数
		info() {
			this.$http.get(`yonghu/detail/${this.$route.query.id}`, { emulateJSON: true }).then(res => {
				if (res.data.code == 0) {
					this.ruleForm = res.data.data;
				}
			});
		},
		// 提交
		onSubmit() {

			//更新跨表属性
			var crossuserid;
			var crossrefid;
			var crossoptnum;
			this.$refs["ruleForm"].validate(valid => {
				if (valid) {
					if (this.type == 'cross') {
						var statusColumnName = localStorage.getItem('statusColumnName');
						var statusColumnValue = localStorage.getItem('statusColumnValue');
						if (statusColumnName && statusColumnName != '') {
							var obj = JSON.parse(localStorage.getItem('crossObj'));
							if (!statusColumnName.startsWith("[")) {
								for (var o in obj) {
									if (o == statusColumnName) {
										obj[o] = statusColumnValue;
									}
								}
								var table = localStorage.getItem('crossTable');
								this.$http.post(table + '/update', obj).then(res => { });
							} else {
								crossuserid = Number(localStorage.getItem('frontUserid'));
								crossrefid = obj['id'];
								crossoptnum = localStorage.getItem('statusColumnName');
								crossoptnum = crossoptnum.replace(/\[/, "").replace(/\]/, "");
							}
						}
					}
					if (crossrefid && crossuserid) {
						this.ruleForm.crossuserid = crossuserid;
						this.ruleForm.crossrefid = crossrefid;
						var params = {
							page: 1,
							limit: 10,
							crossuserid: crossuserid,
							crossrefid: crossrefid,
						}
						this.$http.get('yonghu/list', {
							params: params
						}).then(res => {
							if (res.data.data.total >= crossoptnum) {
								this.$message({
									message: localStorage.getItem('tips'),
									type: 'error',
									duration: 1500,
								});
								return false;
							} else {
								// 跨表计算


								this.$http.post(`yonghu/${this.ruleForm.id ? 'update' : this.centerType ? 'save' : 'add'}`, this.ruleForm).then(res => {
									if (res.data.code == 0) {
										this.$message({
											message: '操作成功',
											type: 'success',
											duration: 1500,
											onClose: () => {
												this.$router.go(-1);
											}
										});
									} else {
										this.$message({
											message: res.data.msg,
											type: 'error',
											duration: 1500
										});
									}
								});
							}
						});
					} else {


						this.$http.post(`yonghu/${this.ruleForm.id ? 'update' : this.centerType ? 'save' : 'add'}`, this.ruleForm).then(res => {
							if (res.data.code == 0) {
								this.$message({
									message: '操作成功',
									type: 'success',
									duration: 1500,
									onClose: () => {
										this.$router.go(-1);
									}
								});
							} else {
								this.$message({
									message: res.data.msg,
									type: 'error',
									duration: 1500
								});
							}
						});
					}
				}
			});
		},
		// 获取uuid
		getUUID() {
			return new Date().getTime();
		},
		// 返回
		back() {
			this.$router.go(-1);
		},
		touxiangUploadChange(fileUrls) {
			this.ruleForm.touxiang = fileUrls.replace(new RegExp(this.$config.baseUrl, "g"), "");;
		},
	}
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.form-container {
	width: 100%;
	padding: 30px 7% 40px;
	margin: 0px auto;
	position: relative;
	background: #fff;

	.form-preview {
		border: 1px solid #eee;
		width: 100%;
		padding: 30px;
		position: relative;
		background: #fcfcfc;

		.form-content {
			.form-item {
				padding: 10px;
				margin: 0 0 10px;
				background: none;

				&.upload {
					.upload-img {
						margin-right: 20px;
					}
				}
			}

			.form-actions {
				padding: 0;
				margin: 0;

				.submit-button {
					border: 0;
					cursor: pointer;
					padding: 0;
					margin: 0 20px 0 0;
					outline: none;
					color: rgba(255, 255, 255, 1);
					border-radius: 4px;
					background: #F5BB00;
					width: 128px;
					line-height: 40px;
					font-size: 14px;
					height: 40px;
				}

				.back-button {
					border: 0px solid rgba(64, 158, 255, 1);
					cursor: pointer;
					padding: 0;
					margin: 0;
					outline: none;
					color: #fff;
					border-radius: 4px;
					background: #333;
					width: 128px;
					line-height: 40px;
					font-size: 14px;
					height: 40px;
				}
			}
		}
	}
}

.el-date-editor.el-input {
	width: auto;
}

.add-update-preview .el-form-item /deep/ .el-form-item__label {
	padding: 0 10px 0 0;
	color: #666;
	font-weight: 500;
	width: 180px;
	font-size: 14px;
	line-height: 40px;
	text-align: right;
}

.add-update-preview .el-form-item /deep/ .el-form-item__content {
	margin-left: 180px;
}

.add-update-preview .el-input /deep/ .el-input__inner {
	border: 1px solid #ddd;
	border-radius: 4px;
	padding: 0 12px;
	box-shadow: 0 0 0px rgba(64, 158, 255, .5);
	outline: none;
	color: #666;
	background: #fff;
	width: 400px;
	font-size: 14px;
	height: 40px;
}

.add-update-preview .el-input-number /deep/ .el-input__inner {
	text-align: left;
	border: 1px solid #ddd;
	border-radius: 4px;
	padding: 0 12px;
	box-shadow: 0 0 0px rgba(64, 158, 255, .5);
	outline: none;
	color: #666;
	background: #fff;
	width: 400px;
	font-size: 14px;
	height: 40px;
}

.add-update-preview .el-input-number /deep/ .el-input-number__decrease {
	display: none;
}

.add-update-preview .el-input-number /deep/ .el-input-number__increase {
	display: none;
}

.add-update-preview .el-select /deep/ .el-input__inner {
	border: 1px solid #ddd;
	border-radius: 4px;
	padding: 0 10px;
	box-shadow: 0 0 0px rgba(64, 158, 255, .5);
	outline: none;
	color: #666;
	background: #fff;
	width: 200px;
	font-size: 14px;
	height: 40px;
}

.add-update-preview .el-date-editor /deep/ .el-input__inner {
	border: 1px solid #ddd;
	border-radius: 4px;
	padding: 0 10px 0 30px;
	box-shadow: 0 0 0px rgba(64, 158, 255, .5);
	outline: none;
	color: #666;
	background: #fff;
	width: 200px;
	font-size: 14px;
	height: 40px;
}

.add-update-preview /deep/ .el-upload--picture-card {
	background: transparent;
	border: 0;
	border-radius: 0;
	width: auto;
	height: auto;
	line-height: initial;
	vertical-align: middle;
}

.add-update-preview /deep/ .upload .upload-img {
	border: 1px solid #ddd;
	cursor: pointer;
	border-radius: 6px;
	color: #999;
	background: #fff;
	width: 200px;
	font-size: 32px;
	line-height: 100px;
	text-align: center;
	height: 100px;
}

.add-update-preview /deep/ .el-upload-list .el-upload-list__item {
	border: 1px solid #ddd;
	cursor: pointer;
	border-radius: 6px;
	color: #999;
	background: #fff;
	width: 200px;
	font-size: 32px;
	line-height: 100px;
	text-align: center;
	height: 100px;
}

.add-update-preview /deep/ .el-upload .el-icon-plus {
	border: 1px solid #ddd;
	cursor: pointer;
	border-radius: 6px;
	color: #999;
	background: #fff;
	width: 200px;
	font-size: 32px;
	line-height: 100px;
	text-align: center;
	height: 100px;
}

.add-update-preview .el-textarea /deep/ .el-textarea__inner {
	border: 1px solid #ddd;
	border-radius: 4px;
	padding: 12px;
	box-shadow: 0 0 0px rgba(64, 158, 255, .5);
	outline: none;
	color: #666;
	background: #fff;
	width: 400px;
	font-size: 14px;
	height: 120px;
}
</style>
