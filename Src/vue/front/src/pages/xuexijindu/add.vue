<template>
	<div class="course-form-container">
		<el-form class="form-panel" ref="ruleForm" :model="ruleForm" :rules="rules" label-width="180px">
			<el-form-item class="form-item" label="课程名称" prop="kechengmingcheng">
				<el-input v-model="ruleForm.kechengmingcheng" placeholder="课程名称" clearable
					:disabled="false || ro.kechengmingcheng"></el-input>
			</el-form-item>

			<el-form-item class="form-item" label="课程类型" prop="kechengleixing">
				<el-input v-model="ruleForm.kechengleixing" placeholder="课程类型" clearable
					:disabled="false || ro.kechengleixing"></el-input>
			</el-form-item>

			<el-form-item class="form-item" label="课程图片" v-if="type != 'cross' || (type == 'cross' && !ro.kechengtupian)"
				prop="kechengtupian">
				<file-upload tip="点击上传课程图片" action="file/upload" :limit="3" :multiple="true"
					:fileUrls="ruleForm.kechengtupian ? ruleForm.kechengtupian : ''"
					@change="kechengtupianUploadChange"></file-upload>
			</el-form-item>

			<el-form-item class="form-item upload" v-else label="课程图片" prop="kechengtupian">
				<img v-if="ruleForm.kechengtupian.substring(0, 4) == 'http'" class="upload-img" v-bind:key="index"
					:src="ruleForm.kechengtupian.split(',')[0]">
				<img v-else class="upload-img" v-bind:key="index"
					v-for="(item, index) in ruleForm.kechengtupian.split(',')" :src="baseUrl + item">
			</el-form-item>

			<el-form-item class="form-item" label="学习时长" prop="xuexishizhang">
				<el-input v-model.number="ruleForm.xuexishizhang" placeholder="学习时长" clearable
					:disabled="false || ro.xuexishizhang"></el-input>
			</el-form-item>

			<el-form-item class="form-item" label="学习进度" prop="xuexijindu">
				<el-select v-model="ruleForm.xuexijindu" placeholder="请选择学习进度" :disabled="false || ro.xuexijindu">
					<el-option v-for="(item, index) in xuexijinduOptions" :key="index" :label="item" :value="item">
					</el-option>
				</el-select>
			</el-form-item>

			<el-form-item class="form-item" label="学习效果" prop="xuexixiaoguo">
				<el-input v-model="ruleForm.xuexixiaoguo" placeholder="学习效果" clearable
					:disabled="false || ro.xuexixiaoguo"></el-input>
			</el-form-item>

			<el-form-item class="form-item" label="用户账号" prop="yonghuzhanghao">
				<el-select @change="yonghuzhanghaoChange" v-model="ruleForm.yonghuzhanghao" placeholder="请选择用户账号">
					<el-option v-for="(item, index) in yonghuzhanghaoOptions" :key="index" :label="item" :value="item">
					</el-option>
				</el-select>
			</el-form-item>

			<el-form-item class="form-item" label="用户姓名" prop="yonghuxingming">
				<el-input v-model="ruleForm.yonghuxingming" placeholder="用户姓名" clearable
					:disabled="false || ro.yonghuxingming"></el-input>
			</el-form-item>

			<el-form-item class="form-item" label="已完成内容" prop="yiwanchengneirong">
				<el-input type="textarea" :rows="8" placeholder="已完成内容" v-model="ruleForm.yiwanchengneirong">
				</el-input>
			</el-form-item>

			<el-form-item class="form-item" label="未完成内容" prop="weiwanchengneirong">
				<el-input type="textarea" :rows="8" placeholder="未完成内容" v-model="ruleForm.weiwanchengneirong">
				</el-input>
			</el-form-item>

			<el-form-item class="form-actions">
				<el-button class="submit-btn" type="primary" @click="onSubmit">提交</el-button>
				<el-button class="back-btn" @click="back()">返回</el-button>
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
				kechengmingcheng: false,
				kechengleixing: false,
				kechengtupian: false,
				xuexishizhang: false,
				xuexijindu: false,
				xuexixiaoguo: false,
				yiwanchengneirong: false,
				weiwanchengneirong: false,
				yonghuzhanghao: false,
				yonghuxingming: false,
			},
			type: '',
			userTableName: localStorage.getItem('UserTableName'),
			ruleForm: {
				kechengmingcheng: '',
				kechengleixing: '',
				kechengtupian: '',
				xuexishizhang: '',
				xuexijindu: '',
				xuexixiaoguo: '',
				yiwanchengneirong: '',
				weiwanchengneirong: '',
				yonghuzhanghao: '',
				yonghuxingming: '',
			},
			xuexijinduOptions: [],
			yonghuzhanghaoOptions: [],


			rules: {
				kechengmingcheng: [
				],
				kechengleixing: [
				],
				kechengtupian: [
				],
				xuexishizhang: [
					{ validator: this.$validate.isIntNumer, trigger: 'blur' },
				],
				xuexijindu: [
				],
				xuexixiaoguo: [
				],
				yiwanchengneirong: [
				],
				weiwanchengneirong: [
				],
				yonghuzhanghao: [
				],
				yonghuxingming: [
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
					if (o == 'kechengtupian') {
						this.ruleForm.kechengtupian = obj[o].split(",")[0];
						this.ro.kechengtupian = true;
						continue;
					}
					if (o == 'xuexishizhang') {
						this.ruleForm.xuexishizhang = obj[o];
						this.ro.xuexishizhang = true;
						continue;
					}
					if (o == 'xuexijindu') {
						this.ruleForm.xuexijindu = obj[o];
						this.ro.xuexijindu = true;
						continue;
					}
					if (o == 'xuexixiaoguo') {
						this.ruleForm.xuexixiaoguo = obj[o];
						this.ro.xuexixiaoguo = true;
						continue;
					}
					if (o == 'yiwanchengneirong') {
						this.ruleForm.yiwanchengneirong = obj[o];
						this.ro.yiwanchengneirong = true;
						continue;
					}
					if (o == 'weiwanchengneirong') {
						this.ruleForm.weiwanchengneirong = obj[o];
						this.ro.weiwanchengneirong = true;
						continue;
					}
					if (o == 'yonghuzhanghao') {
						this.ruleForm.yonghuzhanghao = obj[o];
						this.ro.yonghuzhanghao = true;
						continue;
					}
					if (o == 'yonghuxingming') {
						this.ruleForm.yonghuxingming = obj[o];
						this.ro.yonghuxingming = true;
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
			this.xuexijinduOptions = "10%,20%,30%,40%,50%,60%,70%,80%,90%,100%".split(',')
			this.$http.get('option/yonghu/yonghuzhanghao', { emulateJSON: true }).then(res => {
				if (res.data.code == 0) {
					this.yonghuzhanghaoOptions = res.data.data;
				}
			});

			if (localStorage.getItem('raffleType') && localStorage.getItem('raffleType') != null) {
				localStorage.removeItem('raffleType')
				setTimeout(() => {
					this.onSubmit()
				}, 300)
			}
		},
		// 下二随
		yonghuzhanghaoChange() {
			this.$http.get('follow/yonghu/yonghuzhanghao?columnValue=' + this.ruleForm.yonghuzhanghao, { emulateJSON: true }).then(res => {
				if (res.data.code == 0) {
					if (res.data.data.yonghuxingming) {
						this.ruleForm.yonghuxingming = res.data.data.yonghuxingming
					}
				}
			});
		},

		// 多级联动参数
		// 多级联动参数
		info() {
			this.$http.get(`xuexijindu/detail/${this.$route.query.id}`, { emulateJSON: true }).then(res => {
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
						this.$http.get('xuexijindu/list', {
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


								this.$http.post(`xuexijindu/${this.ruleForm.id ? 'update' : this.centerType ? 'save' : 'add'}`, this.ruleForm).then(res => {
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


						this.$http.post(`xuexijindu/${this.ruleForm.id ? 'update' : this.centerType ? 'save' : 'add'}`, this.ruleForm).then(res => {
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
		kechengtupianUploadChange(fileUrls) {
			this.ruleForm.kechengtupian = fileUrls.replace(new RegExp(this.$config.baseUrl, "g"), "");;
		},
	}
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.course-form-container {
	width: 100%;
	padding: 30px 7% 40px;
	margin: 0 auto;
	position: relative;
	background: #fff;
}

.form-panel {
	border: 1px solid #eee;
	width: 100%;
	padding: 30px;
	position: relative;
	background: #fcfcfc;
}

.form-item {
	padding: 10px;
	margin: 0 0 10px;
	background: none;
}

.upload-img {
	margin-right: 20px;
	width: 100px;
	height: 100px;
}

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
	color: rgba(255, 255, 255, 1);
	border-radius: 4px;
	background: #F5BB00;
	width: 128px;
	line-height: 40px;
	font-size: 14px;
	height: 40px;
}

.back-btn {
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
