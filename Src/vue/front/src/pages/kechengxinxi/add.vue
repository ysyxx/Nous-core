<template>
	<div class="course-form-container">
		<!-- 表单容器 -->
		<el-form class="course-form" ref="ruleForm" :model="ruleForm" :rules="rules" label-width="180px">
			<!-- 课程基本信息区 -->
			<div class="form-section">
				<!-- 课程名称 -->
				<el-form-item label="课程名称" prop="kechengmingcheng">
					<el-input v-model="ruleForm.kechengmingcheng" placeholder="课程名称" clearable
						:disabled="false || ro.kechengmingcheng"></el-input>
				</el-form-item>

				<!-- 课程类型 -->
				<el-form-item label="课程类型" prop="kechengleixing">
					<el-select v-model="ruleForm.kechengleixing" placeholder="请选择课程类型"
						:disabled="false || ro.kechengleixing">
						<el-option v-for="(item, index) in kechengleixingOptions" :key="index" :label="item"
							:value="item"></el-option>
					</el-select>
				</el-form-item>

				<!-- 课程课时 -->
				<el-form-item label="课程课时" prop="kechengkeshi">
					<el-input v-model="ruleForm.kechengkeshi" placeholder="课程课时" clearable
						:disabled="false || ro.kechengkeshi"></el-input>
				</el-form-item>

				<!-- 课程评分 -->
				<el-form-item label="课程评分" prop="kechengpingfen">
					<el-input v-model.number="ruleForm.kechengpingfen" placeholder="课程评分" clearable
						:disabled="false || ro.kechengpingfen"></el-input>
				</el-form-item>
			</div>

			<!-- 课程媒体区 -->
			<div class="form-section">
				<!-- 课程视频 -->
				<el-form-item label="课程视频" v-if="type != 'cross' || (type == 'cross' && !ro.kechengshipin)"
					prop="kechengshipin">
					<file-upload tip="点击上传课程视频" action="file/upload" :limit="1" :type="2" :multiple="true"
						:fileUrls="ruleForm.kechengshipin ? ruleForm.kechengshipin : ''"
						@change="kechengshipinUploadChange"></file-upload>
				</el-form-item>

				<el-form-item v-else label="课程视频" prop="kechengshipin" class="upload-item">
					<el-button v-if="ruleForm.kechengshipin" @click="download(baseUrl + ruleForm.kechengshipin)"
						type="success" class="preview-btn">预览</el-button>
					<el-button v-else disabled class="disabled-btn">暂无</el-button>
				</el-form-item>

				<!-- 课程图片 -->
				<el-form-item :style='{ "padding": "10px", "margin": "0 0 10px", "background": "none" }' label="课程图片"
					v-if="type != 'cross' || (type == 'cross' && !ro.kechengtupian)" prop="kechengtupian">
					<file-upload tip="点击上传课程图片" action="file/upload" :limit="3" :multiple="true"
						:fileUrls="ruleForm.kechengtupian ? ruleForm.kechengtupian : ''"
						@change="kechengtupianUploadChange"></file-upload>
				</el-form-item>
			</div>

			<!-- 课程内容编辑器 -->
			<div class="form-section">
				<el-form-item label="课程内容" prop="kechengneirong">
					<editor v-model="ruleForm.kechengneirong" class="content-editor" action="file/upload"></editor>
				</el-form-item>
			</div>

			<!-- 表单操作按钮 -->
			<div class="form-actions">
				<el-button type="primary" class="submit-btn" @click="onSubmit">提交</el-button>
				<el-button class="cancel-btn" @click="back()">返回</el-button>
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
				kechengmingcheng: false,
				kechengleixing: false,
				kechengkeshi: false,
				kechengpingfen: false,
				kechengshipin: false,
				kechengneirong: false,
				kechengtupian: false,
				thumbsupnum: false,
				crazilynum: false,
				clicktime: false,
				clicknum: false,
				storeupnum: false,
			},
			type: '',
			userTableName: localStorage.getItem('UserTableName'),
			ruleForm: {
				kechengmingcheng: '',
				kechengleixing: '',
				kechengkeshi: '',
				kechengpingfen: '',
				kechengshipin: '',
				kechengneirong: '',
				kechengtupian: '',
				thumbsupnum: '',
				crazilynum: '',
				clicktime: '',
				clicknum: '',
				storeupnum: '',
			},
			kechengleixingOptions: [],


			rules: {
				kechengmingcheng: [
				],
				kechengleixing: [
				],
				kechengkeshi: [
				],
				kechengpingfen: [
					{ validator: this.$validate.isIntNumer, trigger: 'blur' },
				],
				kechengshipin: [
				],
				kechengneirong: [
				],
				kechengtupian: [
				],
				thumbsupnum: [
					{ validator: this.$validate.isIntNumer, trigger: 'blur' },
				],
				crazilynum: [
					{ validator: this.$validate.isIntNumer, trigger: 'blur' },
				],
				clicktime: [
				],
				clicknum: [
					{ validator: this.$validate.isIntNumer, trigger: 'blur' },
				],
				storeupnum: [
					{ validator: this.$validate.isIntNumer, trigger: 'blur' },
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
					if (o == 'kechengmingcheng') {
						this.ruleForm.kechengmingcheng = obj[o];
						this.ro.kechengmingcheng = true;
						continue;
					}
					if (o == 'kechengleixing') {
						this.ruleForm.kechengleixing = obj[o];
						this.ro.kechengleixing = true;
						continue;
					}
					if (o == 'kechengkeshi') {
						this.ruleForm.kechengkeshi = obj[o];
						this.ro.kechengkeshi = true;
						continue;
					}
					if (o == 'kechengpingfen') {
						this.ruleForm.kechengpingfen = obj[o];
						this.ro.kechengpingfen = true;
						continue;
					}
					if (o == 'kechengshipin') {
						this.ruleForm.kechengshipin = obj[o];
						this.ro.kechengshipin = true;
						continue;
					}
					if (o == 'kechengneirong') {
						this.ruleForm.kechengneirong = obj[o];
						this.ro.kechengneirong = true;
						continue;
					}
					if (o == 'kechengtupian') {
						this.ruleForm.kechengtupian = obj[o].split(",")[0];
						this.ro.kechengtupian = true;
						continue;
					}
					if (o == 'thumbsupnum') {
						this.ruleForm.thumbsupnum = obj[o];
						this.ro.thumbsupnum = true;
						continue;
					}
					if (o == 'crazilynum') {
						this.ruleForm.crazilynum = obj[o];
						this.ro.crazilynum = true;
						continue;
					}
					if (o == 'clicktime') {
						this.ruleForm.clicktime = obj[o];
						this.ro.clicktime = true;
						continue;
					}
					if (o == 'clicknum') {
						this.ruleForm.clicknum = obj[o];
						this.ro.clicknum = true;
						continue;
					}
					if (o == 'storeupnum') {
						this.ruleForm.storeupnum = obj[o];
						this.ro.storeupnum = true;
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
			this.$http.get('option/kechengleixing/kechengleixing', { emulateJSON: true }).then(res => {
				if (res.data.code == 0) {
					this.kechengleixingOptions = res.data.data;
				}
			});

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
			this.$http.get(`kechengxinxi/detail/${this.$route.query.id}`, { emulateJSON: true }).then(res => {
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
						this.$http.get('kechengxinxi/list', {
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


								this.$http.post(`kechengxinxi/${this.ruleForm.id ? 'update' : this.centerType ? 'save' : 'add'}`, this.ruleForm).then(res => {
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


						this.$http.post(`kechengxinxi/${this.ruleForm.id ? 'update' : this.centerType ? 'save' : 'add'}`, this.ruleForm).then(res => {
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
		kechengshipinUploadChange(fileUrls) {
			this.ruleForm.kechengshipin = fileUrls.replace(new RegExp(this.$config.baseUrl, "g"), "");;
		},
		kechengtupianUploadChange(fileUrls) {
			this.ruleForm.kechengtupian = fileUrls.replace(new RegExp(this.$config.baseUrl, "g"), "");;
		},
	}
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
/* 表单容器样式 */
.course-form-container {
  width: 100%;
  padding: 30px 7% 40px;
  margin: 0 auto;
  position: relative;
  background: #fff;
}

/* 表单主体样式 */
.course-form {
  border: 1px solid #eee;
  width: 100%;
  padding: 30px;
  position: relative;
  background: #fcfcfc;
}

/* 表单区块样式 */
.form-section {
  margin-bottom: 20px;
  
  .el-form-item {
    padding: 10px;
    margin: 0 0 10px;
    background: none;
  }
}

/* 上传项样式 */
.upload-item {
  .image-preview {
    display: flex;
    flex-wrap: wrap;
    
    .upload-img {
      width: 100px;
      height: 100px;
      object-fit: cover;
      margin-right: 20px;
      border: 1px solid #eee;
    }
  }
  
  .preview-btn {
    background-color: #67c23a;
    color: #fff;
    border: none;
    
    &:hover {
      opacity: 0.9;
    }
  }
  
  .disabled-btn {
    background-color: #f5f7fa;
    color: #c0c4cc;
    border: none;
  }
}

/* 内容编辑器样式 */
.content-editor {
  min-height: 250px;
  padding: 0;
  margin: 0;
  border: 1px solid #eee;
  background-color: #fff;
  width: 100%;
  border-radius: 0;
}

/* 表单操作按钮样式 */
.form-actions {
  display: flex;
  justify-content: center;
  padding: 20px 0 0;
  margin-top: 20px;
  
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
    
    &:hover {
      opacity: 0.9;
    }
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
    
    &:hover {
      opacity: 0.9;
    }
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .course-form {
    padding: 15px;
  }
  
  .form-actions {
    flex-direction: column;
    align-items: center;
    
    .submit-btn, .cancel-btn {
      margin: 0 0 10px 0;
      width: 100%;
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
