<template>
	<div class="forum-list-container">
		<!-- 标题区域 -->
		<div class="forum-header">
			<div class="forum-title">互动交流</div>
		</div>

		<!-- 搜索表单 -->
		<el-form class="search-form" :inline="true" :model="formSearch">
			<el-form-item>
				<el-input v-model="title" placeholder="标题"></el-input>
			</el-form-item>

			<div class="action-buttons">
				<el-button class="search-btn" type="primary" @click="getForumList(1)">
					<span class="icon iconfont icon-shouye-zhihui"></span>
					查询
				</el-button>
				<el-button class="publish-btn" type="primary" @click="toForumAdd">
					<span class="icon iconfont icon-shouye-zhihui"></span>
					发布帖子
				</el-button>
			</div>
		</el-form>

		<!-- 帖子列表 -->
		<div class="forum-list">
			<div class="forum-item" v-for="item in forumList" :key="item.id" @click="toForumDetail(item)">

				<div class="item-title">{{ item.title }}</div>
				<div class="item-author">发布人：{{ item.username }}</div>
				<div class="item-time">{{ item.addtime }}</div>
			</div>
		</div>

		<!-- 分页组件 -->
		<el-pagination background class="pagination" :pager-count="7" :page-size="pageSize" :page-sizes="pageSizes"
			prev-text="<" next-text=">" :hide-on-single-page="false"
			:layout="['total', 'prev', 'pager', 'next', 'sizes', 'jumper'].join()" :total="total" @current-change="curChange"
			@prev-click="prevClick" @next-click="nextClick">
		</el-pagination>
	</div>
</template>

<script>
export default {
	//数据集合
	data() {
		return {
			formSearch: {},
			title: '',
			layouts: '',
			forumList: [],
			total: 1,
			pageSize: 10, pageSizes: [10, 20, 30, 50],
			totalPage: 1
		}
	},
	created() {
		this.getForumList(1);
	},
	//方法集合
	methods: {
		getForumList(page) {
			let params = { page, limit: this.pageSize, isdone: '开放', sort: 'istop,toptime', order: 'desc,desc' };
			let searchWhere = {};
			if (this.title != '') searchWhere.title = '%' + this.title + '%';
			this.$http.get('forum/flist', { params: Object.assign(params, searchWhere) }).then(res => {
				if (res.data.code == 0) {
					this.forumList = res.data.data.list;
					this.total = res.data.data.total;
					this.pageSize = res.data.data.pageSize; this.pageSizes = [this.pageSize, this.pageSize * 2, this.pageSize * 3, this.pageSize * 5];
					this.totalPage = res.data.data.totalPage;
				}
			});
		},
		curChange(page) {
			this.getForumList(page);
		},
		prevClick(page) {
			this.getForumList(page);
		},
		nextClick(page) {
			this.getForumList(page);
		},
		toForumAdd() {
			this.$router.push('/index/forumAdd');
		},
		toForumDetail(item) {
			this.$router.push({ path: '/index/forumDetail', query: { id: item.id } });
		}
	}
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.forum-list-container {
	width: 100%;
	padding: 30px 7% 40px;
	margin: 0 auto;
	position: relative;
	background: #fff;
}

/* 标题区域 */
.forum-header {
	width: 100%;
	line-height: 54px;
	text-align: center;
	background: #333;
	height: 54px;
}

.forum-title {
	padding: 0 20px;
	color: #fff;
	text-align: center;
	background: none;
	display: inline-block;
	width: auto;
	font-size: 20px;
}

/* 搜索表单 */
.search-form {
	padding: 10px;
	margin: 10px 0;
	align-items: center;
	flex-wrap: wrap;
	background: none;
	display: flex;
	width: 100%;
	justify-content: center;
	height: auto;

	.el-form-item {
		margin: 0 10px;
	}
}

.action-buttons {
	display: flex;
	gap: 10px;
}

.search-btn,
.publish-btn {
	.icon {
		color: #fff;
		margin: 0 10px 0 0;
		font-size: 14px;
	}
}

.search-btn {
	background: #409EFF;
	border-color: #409EFF;

	&:hover {
		background: #66b1ff;
		border-color: #66b1ff;
	}
}

.publish-btn {
	background: #F5BB00;
	border-color: #F5BB00;

	&:hover {
		background: #F5BB00;
		border-color: #F5BB00;
	}
}

/* 帖子列表 */
.forum-list {
	width: 100%;
	padding: 0;
}

.forum-item {
	display: flex;
	align-items: center;
	padding: 12px 15px;
	border-bottom: 1px solid #f0f0f0;
	cursor: pointer;
	transition: all 0.3s;

	&:hover {
		background-color: #f5f7fa;
		transform: translateX(5px);
	}
}

.item-title {
	overflow: hidden;
	white-space: nowrap;
	color: #333;
	flex: 1;
	width: calc(100% - 260px);
	font-size: 14px;
	text-overflow: ellipsis;
}

.item-author {
	color: #999;
	margin: 0 30px 0 0;
	font-size: 14px;
	min-width: 150px;
}

.item-time {
	color: #999;
	font-size: 14px;
	min-width: 120px;
}

/* 分页组件 */
.pagination {
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

/* 响应式设计 */
@media (max-width: 768px) {
	.forum-list-container {
		padding: 15px 5%;
	}

	.search-form {
		flex-direction: column;
		align-items: stretch;

		.el-form-item {
			margin: 0 0 10px 0;
		}
	}

	.action-buttons {
		flex-direction: column;
		gap: 10px;
	}

	.forum-item {
		flex-direction: column;
		align-items: flex-start;
		padding: 15px;
	}

	.item-title {
		width: 100%;
		margin-bottom: 5px;
		white-space: normal;
		text-overflow: clip;
	}

	.item-author,
	.item-time {
		margin: 5px 0;
		width: 100%;
	}
}

.section {
	width: 900px;
	margin: 0 auto;
}

.section-content {
	display: flex;
	justify-content: space-between;
	line-height: 60px;
	cursor: pointer;
	box-sizing: border-box;
	padding: 0 10px;
}

.section-content:hover {
	background-color: #E4E7ED;
	color: #fff;
}

.item-style {
	color: #909399;
	font-weight: 400;
}

.section-btn {
	text-align: right;
	margin-bottom: 15px;
	padding-right: 10px;
}

.forum-preview .el-form-item /deep/ .el-form-item__content {
	display: flex;
	align-items: center;
}

.forum-preview .el-form-item .el-input /deep/ .el-input__inner {
	border: 1px solid #eee;
	border-radius: 4px;
	padding: 0 10px;
	margin: 0;
	outline: none;
	color: #333;
	width: 280px;
	font-size: 14px;
	line-height: 36px;
	height: 36px;
}

.forum-preview .searchBtn {
	cursor: pointer;
	border: 0;
	border-radius: 4px;
	padding: 0px 15px;
	margin: 0 10px 0 0;
	outline: none;
	color: #fff;
	background: #F5BB00;
	width: auto;
	font-size: 14px;
	line-height: 36px;
	height: 36px;
}

.forum-preview .searchBtn:hover {
	background: #F5BB0090;
}

.forum-preview .pubBtn {
	cursor: pointer;
	border: 0;
	border-radius: 4px;
	padding: 0px 15px;
	margin: 0 10px 0 0;
	outline: none;
	color: #fff;
	background: #333;
	width: auto;
	font-size: 14px;
	line-height: 36px;
	height: 36px;
}

.forum-preview .pubBtn:hover {
	background: #33333390;
}

.forum-preview .z-box .section-content {
	cursor: pointer;
	padding: 0 20px;
	color: #333;
	background: #fff;
	display: flex;
	width: 100%;
	border-color: #ddd;
	border-width: 0 0 1px 0;
	line-height: 40px;
	align-items: center;
	border-style: dashed;
	height: 40px;
}

.forum-preview .z-box .section-content:hover {
	color: #fff;
	background: #e4e7ed20;
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
