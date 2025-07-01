<template>
	<div class="form-container">
		<el-form class="config-form" ref="ruleForm" :model="ruleForm" :rules="rules" label-width="180px">
			<!-- 名称字段 -->
			<el-form-item label="名称" prop="name">
				<el-input v-model="ruleForm.name" placeholder="名称" clearable :disabled="false || ro.name">
				</el-input>
			</el-form-item>

			<!-- 值字段 - 上传或显示 -->
			<el-form-item v-if="type != 'cross' || (type == 'cross' && !ro.value)" label="值" prop="value">
				<file-upload tip="点击上传值" action="file/upload" :limit="3" :multiple="true"
					:fileUrls="ruleForm.value ? ruleForm.value : ''" @change="valueUploadChange">
				</file-upload>
			</el-form-item>

			<el-form-item v-else class="upload-item" label="值" prop="value">
				<img v-if="ruleForm.value.substring(0, 4) == 'http'" class="upload-img" :src="ruleForm.value.split(',')[0]"
					width="100" height="100">
				<img v-else class="upload-img" v-for="(item, index) in ruleForm.value.split(',')" :key="index"
					:src="baseUrl + item" width="100" height="100">
			</el-form-item>

			<!-- URL字段 -->
			<el-form-item label="url" prop="url">
				<el-input type="textarea" :rows="8" placeholder="url" v-model="ruleForm.url">
				</el-input>
			</el-form-item>

			<!-- 操作按钮 -->
			<el-form-item class="form-actions">
				<el-button class="submit-btn" type="primary" @click="onSubmit">提交</el-button>
				<el-button class="cancel-btn" @click="back">返回</el-button>
			</el-form-item>
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
				name: false,
				value: false,
				url: false,
			},
			type: '',
			userTableName: localStorage.getItem('UserTableName'),
			ruleForm: {
				name: '',
				value: '',
				url: '',
			},


			rules: {
				name: [
					{ required: true, message: '名称不能为空', trigger: 'blur' },
				],
				value: [
				],
				url: [
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
					if (o == 'name') {
						this.ruleForm.name = obj[o];
						this.ro.name = true;
						continue;
					}
					if (o == 'value') {
						this.ruleForm.value = obj[o].split(",")[0];
						this.ro.value = true;
						continue;
					}
					if (o == 'url') {
						this.ruleForm.url = obj[o];
						this.ro.url = true;
						continue;
					}
				}
			} else if (type == 'edit') {
				this.info()
			}

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
			this.$http.get(`config/detail/${this.$route.query.id}`, { emulateJSON: true }).then(res => {
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
						this.$http.get('config/list', {
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


								this.$http.post(`config/${this.ruleForm.id ? 'update' : this.centerType ? 'save' : 'add'}`, this.ruleForm).then(res => {
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


						this.$http.post(`config/${this.ruleForm.id ? 'update' : this.centerType ? 'save' : 'add'}`, this.ruleForm).then(res => {
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
		valueUploadChange(fileUrls) {
			this.ruleForm.value = fileUrls.replace(new RegExp(this.$config.baseUrl, "g"), "");;
		},
	}
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.form-container {
	width: 100%;
	padding: 30px 7% 40px;
	margin: 0 auto;
	position: relative;
	background: #fff;
}

.config-form {
	border: 1px solid #eee;
	width: 100%;
	padding: 30px;
	position: relative;
	background: #fcfcfc;

	/* 表单项通用样式 */
	.el-form-item {
		padding: 10px;
		margin: 0 0 10px;
		background: none;
	}

	/* 上传图片样式 */
	.upload-item {
		.upload-img {
			margin-right: 20px;
		}
	}

	/* 操作按钮区域 */
	.form-actions {
		padding: 0;
		margin: 0;
	}

	.submit-btn {
		border: 0;
		cursor: pointer;
		padding: 0;
		margin: 0 20px 0 0;
		outline: none;
		color: #fff;
		border-radius: 4px;
		background: #F5BB00;
		width: 128px;
		line-height: 40px;
		font-size: 14px;
		height: 40px;
	}

	.cancel-btn {
		border: 0;
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

	/* 响应式设计 */
	@media (max-width: 768px) {
		padding: 15px;

		.el-form-item {
			padding: 8px 0;
		}

		.submit-btn,
		.cancel-btn {
			width: 100%;
			margin-bottom: 10px;
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
