<template>
	<div class="feedback-container">
		<div class="section-title">意见反馈</div>
		<el-form :model="form" :rules="rules" ref="form" class="feedback-form" label-width="65px" label-position="left">
			<el-form-item label="留言" prop="content">
				<editor class="content-editor" v-model="form.content" action="file/upload">
				</editor>
			</el-form-item>
			<el-form-item label="图片" prop="cpicture">
				<file-upload tip="点击上传图片" action="file/upload" :limit="1" :multiple="true"
					:fileUrls="form.cpicture ? form.cpicture : ''" @change="cpictureUploadChange"></file-upload>
			</el-form-item>
			<el-form-item class="form-actions">
				<el-button type="primary" @click="submitForm('form')">立即提交</el-button>
				<el-button @click="resetForm('form')">重置</el-button>
			</el-form-item>
		</el-form>

		<div class="feedback-list">
			<div class="feedback-item" v-for="item in infoList" :key="item.id">
				<div class="user-header">
					<el-avatar v-if="item.avatarurl" :size="50" :src="$config.baseUrl + item.avatarurl"></el-avatar>
					<el-avatar v-if="!item.avatarurl" :size="50" :src="require('@/assets/touxiang.png')"></el-avatar>
					<span class="username">用户：{{ item.username }}</span>
				</div>
				<div class="feedback-content">
					<div v-html="item.content"></div>
				</div>
				<div class="feedback-image" v-if="item.cpicture">
					<img :src="$config.baseUrl + item.cpicture">
				</div>
				<div class="admin-reply" v-if="item.reply">
					<div class="reply-text">回复：<div v-html="item.reply"></div>
					</div>
				</div>
				<div class="reply-image" v-if="item.rpicture">
					<img :src="$config.baseUrl + item.rpicture">
				</div>
				<el-divider></el-divider>
			</div>
		</div>

		<el-pagination background class="feedback-pagination" :pager-count="7" :page-size="pageSize"
			:page-sizes="pageSizes" prev-text="<" next-text=">" :hide-on-single-page="false"
			:layout='["total", "prev", "pager", "next", "sizes", "jumper"].join()' :total="total" @current-change="curChange"
			@prev-click="prevClick" @next-click="nextClick">
		</el-pagination>
	</div>
</template>


<script>
export default {
	//数据集合
	data() {
		return {
			layouts: '',
			form: {
				content: '',
				userid: localStorage.getItem('frontUserid'),
				username: localStorage.getItem('username')
			},
			total: 1,
			pageSize: 20,
			pageSizes: [10, 20, 30, 50],
			totalPage: 1,
			rules: {
				content: [
					{ required: true, message: '请输入内容', trigger: 'blur' }
				]
			},
			infoList: []
		}
	},
	created() {
		this.getInfo(1);
	},
	//方法集合
	methods: {
		getInfo(page) {
			this.$http.get('messages/list', { params: { page, limit: this.pageSize, sort: "addtime", order: "desc" } }).then(res => {
				if (res.data.code == 0) {
					this.infoList = res.data.data.list;
					this.total = res.data.data.total;
					this.pageSize = res.data.data.pageSize; this.pageSizes = [this.pageSize, this.pageSize * 2, this.pageSize * 3, this.pageSize * 5];
					this.totalPage = res.data.data.totalPage;
				}
			});
		},
		curChange(page) {
			this.getInfo(page);
		},
		prevClick(page) {
			this.getInfo(page);
		},
		nextClick(page) {
			this.getInfo(page);
		},
		cpictureUploadChange(fileUrls) {
			this.form.cpicture = fileUrls;
		},
		submitForm(formName) {
			this.$refs[formName].validate((valid) => {
				if (valid) {
					if (this.form.cpicture != null) {
						this.form.cpicture = this.form.cpicture.replace(new RegExp(this.$config.baseUrl, "g"), "");
					}
					this.form.avatarurl = localStorage.getItem('frontHeadportrait') ? localStorage.getItem('frontHeadportrait') : '';
					let sensitiveWords = "";
					let sensitiveWordsArr = [];
					if (sensitiveWords) {
						sensitiveWordsArr = sensitiveWords.split(",");
					}
					for (var i = 0; i < sensitiveWordsArr.length; i++) {
						//全局替换
						var reg = new RegExp(sensitiveWordsArr[i], "g");
						//判断内容中是否包括敏感词
						if (this.form.content.indexOf(sensitiveWordsArr[i]) > -1) {
							// 将敏感词替换为 **
							this.form.content = this.form.content.replace(reg, "**");
						}
					}
					this.$http.post('messages/add', this.form).then(res => {
						if (res.data.code == 0) {
							this.$message({
								type: 'success',
								message: '留言成功!',
								duration: 1500,
								onClose: () => {
									this.form.content = '';
									this.getInfo(1);
								}
							});
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
.feedback-container {
	width: 100%;
	padding: 30px 7% 40px;
	margin: 0 auto;
	position: relative;
	background: #fff;
}

.section-title {
	margin: 10px 0;
	color: #fff;
	text-align: center;
	background: #333;
	width: 100%;
	font-size: 20px;
	line-height: 54px;
}

.feedback-form {
	margin-top: 20px;
}

.content-editor {
	min-height: 250px;
	padding: 0;
	box-shadow: 0 0 0px rgba(75, 223, 201, .5);
	margin: 0;
	border: 1px solid #eee;
	background-color: #fff;
	border-radius: 0;
	width: 100%;
	height: auto;
}

.form-actions {
	margin-top: 20px;
}

.feedback-list {
	margin-top: 30px;
}

.user-header {
	display: flex;
	align-items: center;
	margin-bottom: 10px;
}

.username {
	margin-left: 10px;
}

.feedback-content {
	margin-left: 60px;
}

.feedback-image img,
.reply-image img {
	max-width: 300px;
	max-height: 300px;
	border: 2px solid #EEEEEE;
	margin: 8px 0 0 50px;
}

.admin-reply {
	margin-top: 15px;
	padding: 10px;
	background: #f5f5f5;
	border-radius: 4px;
}

.reply-text {
	margin-left: 10px;
}

.feedback-pagination {
	padding: 0 7%;
	margin: 10px auto;
	white-space: nowrap;
	overflow: hidden;
	color: #333;
	text-align: center;
	width: 100%;
	clear: both;
	font-weight: 500;
}

.section {
	width: 900px;
	margin: 0 auto;
}

.section-content {
	margin-top: 50px;
}

.section-pagination {
	margin-top: 30px;
	text-align: center;
}

.header-block {
	height: 50px;
	line-height: 50px;
	display: flex;
}

.userinfo {
	align-self: center;
	margin-left: 15px;
}

.content-block-ask,
.content-block-reply {
	margin-left: 65px;
	margin-top: 15px;
}

.content-block-reply {
	margin-top: 30px;
}

.z-box {
	width: 100% !important;
}

#pagination.el-pagination /deep/ .el-pagination__total {
	margin: 0 10px 0 0;
	color: #666;
	font-weight: 400;
	display: inline-block;
	vertical-align: top;
	font-size: 13px;
	line-height: 28px;
	height: 28px;
}

#pagination.el-pagination /deep/ .btn-prev {
	border: none;
	border-radius: 2px;
	padding: 0;
	margin: 0 5px;
	color: #666;
	background: #f4f4f5;
	display: inline-block;
	vertical-align: top;
	font-size: 13px;
	line-height: 28px;
	min-width: 35px;
	height: 28px;
}

#pagination.el-pagination /deep/ .btn-next {
	border: none;
	border-radius: 2px;
	padding: 0;
	margin: 0 5px;
	color: #666;
	background: #f4f4f5;
	display: inline-block;
	vertical-align: top;
	font-size: 13px;
	line-height: 28px;
	min-width: 35px;
	height: 28px;
}

#pagination.el-pagination /deep/ .btn-prev:disabled {
	border: none;
	cursor: not-allowed;
	border-radius: 2px;
	padding: 0;
	margin: 0 5px;
	color: #C0C4CC;
	background: #f4f4f5;
	display: inline-block;
	vertical-align: top;
	font-size: 13px;
	line-height: 28px;
	height: 28px;
}

#pagination.el-pagination /deep/ .btn-next:disabled {
	border: none;
	cursor: not-allowed;
	border-radius: 2px;
	padding: 0;
	margin: 0 5px;
	color: #C0C4CC;
	background: #f4f4f5;
	display: inline-block;
	vertical-align: top;
	font-size: 13px;
	line-height: 28px;
	height: 28px;
}

#pagination.el-pagination /deep/ .el-pager {
	padding: 0;
	margin: 0;
	display: inline-block;
	vertical-align: top;
}

#pagination.el-pagination /deep/ .el-pager .number {
	cursor: pointer;
	padding: 0 4px;
	margin: 0 5px;
	color: #666;
	display: inline-block;
	vertical-align: top;
	font-size: 13px;
	line-height: 28px;
	border-radius: 2px;
	background: #f4f4f5;
	text-align: center;
	min-width: 30px;
	height: 28px;
}

#pagination.el-pagination /deep/ .el-pager .number:hover {
	cursor: pointer;
	padding: 0 4px;
	margin: 0 5px;
	color: #fff;
	display: inline-block;
	vertical-align: top;
	font-size: 13px;
	line-height: 28px;
	border-radius: 2px;
	background: #F5BB00;
	text-align: center;
	min-width: 30px;
	height: 28px;
}

#pagination.el-pagination /deep/ .el-pager .number.active {
	cursor: default;
	padding: 0 4px;
	margin: 0 5px;
	color: #FFF;
	display: inline-block;
	vertical-align: top;
	font-size: 13px;
	line-height: 28px;
	border-radius: 2px;
	background: #F5BB00;
	text-align: center;
	min-width: 30px;
	height: 28px;
}

#pagination.el-pagination /deep/ .el-pagination__sizes {
	display: inline-block;
	vertical-align: top;
	font-size: 13px;
	line-height: 28px;
	height: 28px;
}

#pagination.el-pagination /deep/ .el-pagination__sizes .el-input {
	margin: 0 5px;
	width: 100px;
	position: relative;
}

#pagination.el-pagination /deep/ .el-pagination__sizes .el-input .el-input__inner {
	border: 1px solid #DCDFE6;
	cursor: pointer;
	padding: 0 25px 0 8px;
	color: #606266;
	display: inline-block;
	font-size: 13px;
	line-height: 28px;
	border-radius: 3px;
	outline: 0;
	background: #FFF;
	width: 100%;
	text-align: center;
	height: 28px;
}

#pagination.el-pagination /deep/ .el-pagination__sizes .el-input span.el-input__suffix {
	top: 0;
	position: absolute;
	right: 0;
	height: 100%;
}

#pagination.el-pagination /deep/ .el-pagination__sizes .el-input .el-input__suffix .el-select__caret {
	cursor: pointer;
	color: #C0C4CC;
	width: 25px;
	font-size: 14px;
	line-height: 28px;
	text-align: center;
}

#pagination.el-pagination /deep/ .el-pagination__jump {
	margin: 0 0 0 24px;
	color: #606266;
	display: inline-block;
	vertical-align: top;
	font-size: 13px;
	line-height: 28px;
	height: 28px;
}

#pagination.el-pagination /deep/ .el-pagination__jump .el-input {
	border-radius: 3px;
	padding: 0 2px;
	margin: 0 2px;
	display: inline-block;
	width: 50px;
	font-size: 14px;
	line-height: 18px;
	position: relative;
	text-align: center;
	height: 28px;
}

#pagination.el-pagination /deep/ .el-pagination__jump .el-input .el-input__inner {
	border: 1px solid #DCDFE6;
	cursor: pointer;
	padding: 0 3px;
	color: #606266;
	display: inline-block;
	font-size: 14px;
	line-height: 28px;
	border-radius: 3px;
	outline: 0;
	background: #FFF;
	width: 100%;
	text-align: center;
	height: 28px;
}
</style>
