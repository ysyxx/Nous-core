<template>
	<div class="container">
		<div class="header">
			<div class="header-title">互动交流</div>
		</div>

		<div class="back-button-container">
			<el-button size="mini" @click="backClick" icon="el-icon-back" type="warning">返回</el-button>
		</div>

		<div class="content-container">
			<div class="content-title">{{ detail.title }}</div>

			<div class="meta-info">
				<div class="author">发布人：{{ detail.username }}</div>
				<div class="publish-time">发布时间：{{ detail.addtime }}</div>
			</div>

			<div class="operate">
				<div class="like-button" @click="zanClick">
					<span class="icon iconfont icon-guanzhu-zhihui"></span>
					<span>{{ zanType ? '取消赞' : '点赞' }}({{ allZan }})</span>
				</div>
			</div>

			<div class="content-detail" v-html="detail.content"></div>

			<div class="comment-section">
				<div class="comment-header">
					<div class="comment-title">评论列表</div>
					<el-button class="pub" type="primary" @click="addComment">
						<span class="icon iconfont icon-xiugai10"></span>
						点击评论
					</el-button>
				</div>

				<div class="comment-list">
					<template v-if="commentList && commentList.length">
						<div class="comment-item" v-for="item in commentList" :key="item.id"
							@mouseenter="commentEnter(item.id)" @mouseleave="commentLeave">
							<div class="user-info">
								<img :src="item.avatarurl ? baseUrl + item.avatarurl : require('@/assets/touxiang.png')"
									class="avatar">
								<div class="username">用户：{{ item.username }}</div>
							</div>

							<div class="comment-content" v-html="item.content"></div>

							<div class="comment-actions">
								<el-button v-if="showIndex == item.id && showIndex1 == -1" @click="replyClick(item.id)"
									class="reply-button">
									回复
								</el-button>
								<el-button v-if="showIndex == item.id && userid == item.userid && showIndex1 == -1"
									@click="commentDel(item.id)" class="delete-button">
									删除
								</el-button>
							</div>

							<template v-if="item.childs && item.childs.length">
								<div class="child-comments">
									<div class="child-comment-item" v-for="items in item.childs" :key="items.id"
										@mouseenter="commentEnter1(items.id)" @mouseleave="commentLeave1">
										<div class="user-info">
											<img :src="items.avatarurl ? baseUrl + items.avatarurl : require('@/assets/touxiang.png')"
												class="avatar">
											<div class="username">用户：{{ items.username }}</div>
										</div>

										<div class="child-comment-content" v-html="items.content"></div>

										<div class="comment-actions">
											<el-button
												v-if="showIndex == item.id && userid == items.userid && showIndex1 == items.id"
												@click="commentDel(items.id)" class="delete-button">
												删除
											</el-button>
										</div>
									</div>
								</div>
							</template>
						</div>
					</template>
				</div>
			</div>
		</div>

		<el-dialog title="添加评论" :visible.sync="dialogFormVisible">
			<el-form :model="form" :rules="rules" ref="form">
				<el-form-item label="评论" label-width="60px" prop="content">
					<editor class="comment-editor" v-model="form.content" action="file/upload"></editor>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="addForum('form')">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
export default {
	//数据集合
	data() {
		return {
			baseUrl: '',
			id: '',
			detail: {},
			commentList: [],
			dialogFormVisible: false,
			form: {
				content: '',
				parentid: '',
				userid: localStorage.getItem('frontUserid'),
				username: localStorage.getItem('username'),
				avatarurl: '',
			},
			userid: localStorage.getItem('frontUserid'),
			editorOption: {
				modules: {
					toolbar: [
						["bold", "italic", "underline", "strike"],
						["blockquote", "code-block"],
						[{ header: 1 }, { header: 2 }],
						[{ list: "ordered" }, { list: "bullet" }],
						[{ script: "sub" }, { script: "super" }],
						[{ indent: "-1" }, { indent: "+1" }],
						[{ direction: "rtl" }],
						[{ size: ["small", false, "large", "huge"] }],
						[{ header: [1, 2, 3, 4, 5, 6, false] }],
						[{ color: [] }, { background: [] }],
						[{ font: [] }],
						[{ align: [] }],
						["clean"],
						["image", "video"]
					]
				}
			},
			rules: {
				content: [
					{ required: true, message: '请输入评论', trigger: 'blur' }
				]
			},
			zanType: false,
			zanForm: {},
			showIndex: -1,
			showIndex1: -1,
			allZan: 0
		}
	},
	created() {
		this.baseUrl = this.$config.baseUrl;
		this.id = this.$route.query.id
		this.getDetail()
	},
	//方法集合
	methods: {
		// 返回按钮
		backClick() {
			history.back()
		},
		onEditorReady(editor) {
			editor.root.setAttribute('data-placeholder', "请输入内容...");
		},
		getDetail() {
			this.$http.get(`forum/list/${this.id}`).then(res => {
				if (res.data.code == 0) {
					res.data.data.content = res.data.data.content.replace(/img src/gi, "img style=\"width:100%;\" src");
					this.detail = res.data.data
					this.commentList = res.data.data.childs;
					this.getZan()
					this.getAllZan()
				}
			});
		},
		getZan() {
			this.$http.get('storeup/list', {
				params: {
					page: 1,
					limit: 1,
					type: 21,
					userid: localStorage.getItem('frontUserid'),
					tablename: 'forum',
					refid: this.id
				}
			}).then(res => {
				if (res.data && res.data.code == 0) {
					if (res.data.data.list.length) {
						this.zanType = true
						this.zanForm = res.data.data.list[0]
					} else {
						this.zanType = false
					}
				}
			})
		},
		getAllZan() {
			this.$http.get('storeup/list', {
				params: {
					page: 1,
					type: 21,
					tablename: 'forum',
					refid: this.id
				}
			}).then(res => {
				if (res.data && res.data.code == 0) {
					this.allZan = Number(res.data.data.total)
				}
			})
		},
		zanClick() {
			if (this.zanType) {
				this.$http.post('storeup/delete', [this.zanForm.id]).then(res => {
					if (res.data && res.data.code == 0) {
						this.$message.success('取消成功')
						this.getZan()
						this.getAllZan()
					}
				})
			} else {
				let data = {
					name: this.detail.title,
					picture: '1',
					refid: this.detail.id,
					type: 21,
					tablename: 'forum',
					userid: localStorage.getItem('frontUserid')
				}
				this.$http.post('storeup/add', data).then(res => {
					if (res.data && res.data.code == 0) {
						this.$message.success('点赞成功')
						this.getZan()
						this.getAllZan()
					}
				})
			}
		},
		// 新增评论
		addComment() {
			this.form.parentid = this.detail.id
			this.dialogFormVisible = true
		},
		// 鼠标移入
		commentEnter(index) {
			this.showIndex = index
		},
		// 鼠标移出
		commentLeave() {
			this.showIndex = -1
		},
		// 二级评论鼠标移入
		commentEnter1(index) {
			this.showIndex1 = index
		},
		// 二级评论鼠标移出
		commentLeave1() {
			this.showIndex1 = -1
		},
		// 删除评论
		commentDel(id) {
			this.$confirm('是否删除此评论？')
				.then(_ => {
					this.$http.post('forum/delete', [id]).then(res => {
						if (res.data && res.data.code == 0) {
							this.$message({
								type: 'success',
								message: '删除成功!',
								duration: 1500,
								onClose: () => {
									this.getDetail();
								}
							});
						}
					})
				}).catch(_ => { });
		},
		// 回复评论
		replyClick(id) {
			this.form.parentid = id
			this.dialogFormVisible = true
		},
		addForum(formName) {
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
			this.$refs[formName].validate((valid) => {
				if (valid) {
					this.form.avatarurl = localStorage.getItem('frontHeadportrait') ? localStorage.getItem('frontHeadportrait') : '';
					this.$http.post('forum/add', this.form).then(res => {
						if (res.data.code == 0) {
							this.$message({
								type: 'success',
								message: '评论成功!',
								duration: 1500,
								onClose: () => {
									this.form.content = '';
									this.getDetail();
									this.dialogFormVisible = false;
								}
							});
						}
					});
				} else {
					return false;
				}
			});
		}
	}
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.container {
	width: 100%;
	padding: 0px 7%;
	margin: 20px auto 40px;
	position: relative;
	background: rgb(255, 255, 255);
}

.header {
	width: 100%;
	line-height: 54px;
	text-align: center;
	background: #333;
	height: 54px;
}

.header-title {
	padding: 0 20px;
	color: #fff;
	text-align: center;
	background: none;
	display: inline-block;
	width: auto;
	font-size: 20px;
}

.back-button-container {
	padding: 20px 7%;
	margin: 0px auto;
	border-color: #ddd;
	border-radius: 0px;
	background: none;
	border-width: 0 0 1px;
	width: 100%;
	border-style: solid;
}

.content-container {
	width: 100%;
	margin: 20px auto;
	position: relative;
	background: none;
}

.content-title {
	padding: 0 10px;
	margin: 0 0 10px;
	color: #333;
	text-align: center;
	width: 100%;
	line-height: 32px;
	font-size: 18px;
	height: 32px;
}

.meta-info {
	width: 100%;
	align-items: center;
	justify-content: center;
	display: flex;
}

.author {
	color: #999;
	text-align: right;
	font-size: 14px;
}

.publish-time {
	color: #999;
	text-align: left;
	font-size: 14px;
}

.operate {
	padding: 0px;
	align-items: center;
	text-align: right;
	background: none;
	display: flex;
	width: 100%;
	justify-content: flex-end;
}

.like-button {
	padding: 0 15px;
	align-items: center;
	justify-content: center;
	display: flex;
	cursor: pointer;
}

.like-button span.icon {
	margin: 0 2px 0 0;
	line-height: 40px;
	font-size: 14px;
	color: #333;
}

.like-button span.text {
	color: #333;
	line-height: 40px;
	font-size: 14px;
}

.content-detail {
	border: 1px solid #ddd;
	padding: 20px;
}

.comment-section {
	border: 1px solid #eee;
	width: 100%;
	padding: 20px;
	margin: 20px auto;
	background: #fcfcfc;
}

.comment-header {
	padding: 0 0 20px 0;
	border-color: #efefef;
	align-items: center;
	border-width: 0 0 1px 0;
	display: flex;
	width: 100%;
	border-style: solid;
	justify-content: space-between;
}

.comment-title {
	color: #333;
	font-size: 16px;
}

.pub {
	cursor: pointer;
	border: 0;
	padding: 0px 15px;
	margin: 0 10px 0 0;
	outline: none;
	color: #fff;
	border-radius: 4px;
	background: #F5BB00;
	width: auto;
	font-size: 14px;
	line-height: 32px;
	height: 32px;
}

.pub span.icon {
	color: #fff;
	margin: 0 4px 0 0;
	font-size: 14px;
}

.comment-list {
	width: 100%;
	padding: 0px;
	background: none;
}

.comment-item {
	border: 1px solid #eee;
	padding: 10px;
	margin: 20px 0;
	align-items: center;
	color: #666;
	background: #fff;
	width: 100%;
	height: auto;
}

.user-info {
	width: 100%;
	align-items: center;
	display: flex;
	height: auto;
}

.avatar {
	width: 40px;
	margin: 0 10px 0 0;
	border-radius: 100%;
	object-fit: cover;
	height: 40px;
}

.username {
	color: #666;
	font-size: 16px;
}

.comment-content {
	border: 1px solid #eee;
	padding: 10px;
	margin: 10px 0px 0px;
	color: #666;
	border-radius: 4px;
	background: none;
	line-height: 24px;
	font-size: 14px;
}

.comment-actions {
	width: 100%;
	margin: 12px 0 0 0;
	align-items: center;
	justify-content: flex-end;
	display: flex;
	height: auto;
}

.reply-button {
	border: 0;
	cursor: pointer;
	padding: 0 20px;
	margin: 0 10px;
	outline: none;
	color: rgba(255, 255, 255, 1);
	border-radius: 4px;
	background: #F5BB00;
	width: auto;
	line-height: 32px;
	font-size: 14px;
	height: 32px;
}

.delete-button {
	border: 0;
	cursor: pointer;
	padding: 0 20px;
	margin: 0 10px;
	outline: none;
	color: rgba(255, 255, 255, 1);
	border-radius: 4px;
	background: #333;
	width: auto;
	line-height: 32px;
	font-size: 14px;
	height: 32px;
}

.child-comments {
	width: 100%;
	padding: 0 20px;
}

.child-comment-item {
	padding: 8px 0;
	margin: 20px 0 0 0;
	border-color: #efefef;
	align-items: center;
	color: #666;
	background: #fff;
	border-width: 1px 0 0 0;
	width: 100%;
	border-style: solid;
	height: auto;
}

.child-comment-content {
	padding: 0px;
	margin: 4px 0 0;
	color: #666;
	border-radius: 4px;
	background: none;
	line-height: 30px;
	font-size: 14px;
}

.comment-editor {
	min-height: 200px;
	border: 0;
	outline: none;
	color: #333;
	border-radius: 4px;
	width: 100%;
	line-height: 32px;
	font-size: 14px;
}

.section {
	width: 900px;
	margin: 0 auto;
}

.section-content {
	margin-top: 30px;
}

.content-title {
	text-align: center;
	font-size: 22px;
	font-weight: bold;
}

.content-sub-title {
	text-align: center;
	margin-top: 20px;
	color: #888888;
	font-size: 14px;
}

.clearfix:before,
.clearfix:after {
	display: table;
	content: "";
}

.clearfix:after {
	clear: both
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

.content-block-ask {
	margin: 15px 0 20px 65px;
}

.content-detail img {
	max-width: 900px;
	height: auto;
}

.operate {
	display: flex;
	align-items: center;
	justify-content: flex-start;
	padding: 0 40px;
}
</style>
