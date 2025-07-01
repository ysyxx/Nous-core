<template>
	<div class="xuexijindu-list-container">
		<!-- 面包屑导航 -->
		<div class="breadcrumb-wrapper">
			<el-breadcrumb separator="Ξ">
				<el-breadcrumb-item class="breadcrumb-item" to="/"><a>首页</a></el-breadcrumb-item>
				<el-breadcrumb-item class="breadcrumb-item" v-for="(item, index) in breadcrumbItem"
					:key="index"><a>{{ item.name }}</a></el-breadcrumb-item>
			</el-breadcrumb>
		</div>

		<!-- 返回按钮 -->
		<div v-if="centerType" class="back-button-wrapper">
			<el-button size="mini" class="back-button" @click="backClick" icon="el-icon-back" type="warning">返回</el-button>
		</div>

		<div class="list-content-wrapper">
			<!-- 搜索表单 -->
			<el-form :inline="true" :model="formSearch" class="search-form">
				<el-form-item class="search-form-item">
					<div class="search-label">课程名称：</div>
					<el-input v-model="formSearch.kechengmingcheng" placeholder="课程名称"
						@keydown.enter.native="getList(1, curFenlei)" clearable></el-input>
				</el-form-item>
				<el-form-item class="search-form-item">
					<div class="search-label">课程类型：</div>
					<el-input v-model="formSearch.kechengleixing" placeholder="课程类型"
						@keydown.enter.native="getList(1, curFenlei)" clearable></el-input>
				</el-form-item>
				<el-button class="search-button" type="primary" @click="getList(1, curFenlei)">
					<i class="el-icon-search"></i>查询
				</el-button>
				<el-button v-if="btnAuth('xuexijindu', '新增')" class="add-button" type="primary"
					@click="add('/index/xuexijinduAdd')">
					<i class="el-icon-circle-plus-outline"></i>添加
				</el-button>
			</el-form>

			<!-- 筛选选项 -->
			<div class="filter-options-wrapper">
				<div class="filter-option" v-for="(item, index) in selectOptionsList" :key="item">
					<div class="filter-label">{{ item.name }}：</div>
					<div class="filter-items">
						<div class="filter-item" @click="selectClick2(item, -1)" :class="{ 'active': item.check == -1 }">全部
						</div>
						<div class="filter-item" @click="selectClick2(item, index1)"
							:class="{ 'active': item.check == index1 }" v-for="(item1, index1) in item.list" :key="item1">
							{{ item1 }}</div>
					</div>
				</div>
			</div>

			<!-- 列表内容 -->
			<div class="list-wrapper">
				<div class="list-content">
					<div v-for="(item, index) in dataList" :key="index" @click.stop="toDetail(item)" class="list-item">
						<div class="item-image">
							<img @click.stop="imgPreView(item.kechengtupian)"
								v-if="item.kechengtupian && item.kechengtupian.substr(0, 4) == 'http'"
								:src="item.kechengtupian.split(',')[0]" />
							<img @click.stop="imgPreView(baseUrl + (item.kechengtupian ? item.kechengtupian.split(',')[0] : ''))"
								v-else :src="baseUrl + (item.kechengtupian ? item.kechengtupian.split(',')[0] : '')" />
						</div>
						<div class="item-info">
							<div class="info-main">
								<div class="item-title">{{ item.kechengmingcheng }}</div>
								<div class="item-title">{{ item.kechengleixing }}</div>
								<div v-if="item.price" class="item-price"><span>￥</span>{{ item.price }}</div>
								<div class="item-time">
									<span class="icon iconfont icon-shijian21"></span>
									<span class="text">{{ item.addtime }}</span>
								</div>
							</div>
							<div class="item-desc" v-html="item.yiwanchengneirong"></div>
						</div>
					</div>
				</div>
			</div>

			<!-- 分页 -->
			<el-pagination background class="list-pagination" :pager-count="7" :page-size="pageSize"
				:page-sizes="pageSizes" prev-text="<" next-text=">" :hide-on-single-page="false"
				:layout='["total", "prev", "pager", "next", "sizes", "jumper"].join()' :total="total"
				@current-change="curChange" @size-change="sizeChange" @prev-click="prevClick" @next-click="nextClick">
			</el-pagination>
		</div>

		<!-- 图片预览对话框 -->
		<el-dialog title="预览图" :visible.sync="previewVisible" width="50%">
			<img :src="previewImg" alt="" class="preview-image">
		</el-dialog>
	</div>
</template>

<script>
export default {
	//数据集合
	data() {
		return {
			selectIndex2: 0,
			selectOptionsList: [],
			layouts: '',
			swiperIndex: -1,
			baseUrl: '',
			breadcrumbItem: [
				{
					name: '学习进度'
				}
			],
			formSearch: {
				kechengmingcheng: '',
				kechengleixing: '',
				xuexijindu: '',
			},
			fenlei: [],
			feileiColumn: '',
			dataList: [],
			total: 1,
			pageSize: 8,
			pageSizes: [10, 20, 30, 50],
			totalPage: 1,
			curFenlei: '全部',
			isPlain: false,
			indexQueryCondition: '',
			xuexijinduOptions: [],
			timeRange: [],
			centerType: false,
			previewImg: '',
			previewVisible: false,
			sortType: 'id',
			sortOrder: 'desc',
		}
	},
	created() {
		if (this.$route.query.centerType) {
			this.centerType = true
		}
		this.baseUrl = this.$config.baseUrl;
		this.xuexijinduOptions = '10%,20%,30%,40%,50%,60%,70%,80%,90%,100%'.split(',');
		this.selectOptionsList.push({ name: '学习进度', list: this.xuexijinduOptions, tableName: 'xuexijindu', check: -1 })
		this.getFenlei();
		this.getList(1, '全部');
	},
	//方法集合
	methods: {
		selectClick2(row, index) {
			row.check = index
			if (index == -1) {
				this.formSearch[row.tableName] = ''
			} else {
				this.formSearch[row.tableName] = row.list[index]
			}
			this.getList()
		},
		add(path) {
			let query = {}
			if (this.centerType) {
				query.centerType = 1
			}
			this.$router.push({ path: path, query: query });
		},
		getFenlei() {
		},
		getList(page, fenlei, ref = '') {
			let params = { page, limit: this.pageSize };
			let searchWhere = {};
			if (this.formSearch.kechengmingcheng != '') searchWhere.kechengmingcheng = '%' + this.formSearch.kechengmingcheng + '%';
			if (this.formSearch.kechengleixing != '') searchWhere.kechengleixing = '%' + this.formSearch.kechengleixing + '%';
			if (this.formSearch.xuexijindu != '') searchWhere.xuexijindu = this.formSearch.xuexijindu;
			let user = JSON.parse(localStorage.getItem('sessionForm'))
			if (this.sortType) searchWhere.sort = this.sortType
			if (this.sortOrder) searchWhere.order = this.sortOrder
			this.$http.get(`xuexijindu/${this.centerType ? 'page' : 'list'}`, { params: Object.assign(params, searchWhere) }).then(res => {
				if (res.data.code == 0) {
					this.dataList = res.data.data.list;
					this.total = Number(res.data.data.total);
					this.pageSize = Number(res.data.data.pageSize);
					this.totalPage = res.data.data.totalPage;

					this.pageSizes = [this.pageSize, this.pageSize * 2, this.pageSize * 3, this.pageSize * 5];
				}
			});
		},
		curChange(page) {
			this.getList(page);
		},
		prevClick(page) {
			this.getList(page);
		},
		sizeChange(size) {
			this.pageSize = size
			this.getList(1);
		},
		nextClick(page) {
			this.getList(page);
		},
		imgPreView(url) {
			this.previewImg = url
			this.previewVisible = true
		},
		toDetail(item) {
			let params = {
				id: item.id
			}
			if (this.centerType) {
				params.centerType = 1
			}
			this.$router.push({ path: '/index/xuexijinduDetail', query: params });
		},
		btnAuth(tableName, key) {
			if (this.centerType) {
				return this.isBackAuth(tableName, key)
			} else {
				return this.isAuth(tableName, key)
			}
		},
		backClick() {
			this.$router.push({ path: '/index/center' });
		},
	}
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.xuexijindu-list-container {
	width: 100%;
	background: #fff;

	.breadcrumb-wrapper {
		padding: 20px 7%;
		margin: 0px auto;
		border-color: #ddd;
		border-width: 0 0 1px;
		border-style: solid;
		width: 100%;

		.breadcrumb-item {
			font-size: 14px;
			line-height: 1;
		}
	}

	.back-button-wrapper {
		padding: 20px 7%;
		margin: 0px auto;
		border-color: #ddd;
		border-width: 0 0 1px;
		border-style: solid;
		width: 100%;
	}

	.list-content-wrapper {
		padding: 0;
		margin: 0px auto;
		display: flex;
		flex-wrap: wrap;
		width: 100%;
		position: relative;
		background: #fff;

		.search-form {
			padding: 0px 7% 20px;
			margin: 20px 0 0;
			border-color: #ddd;
			border-width: 0 0 1px;
			border-style: solid;
			display: flex;
			align-items: center;
			flex-wrap: wrap;
			width: 100%;
			height: auto;
			order: 1;

			.search-form-item {
				margin: 0 10px;

				.search-label {
					width: auto;
					padding: 0 10px;
					line-height: 42px;
					display: inline-block;
				}
			}

			.search-button {
				cursor: pointer;
				border: 0;
				padding: 0px 15px;
				margin: 0 10px 0 0;
				outline: none;
				color: #fff;
				border-radius: 0px;
				background: #F5BB00;
				width: auto;
				font-size: 14px;
				line-height: 36px;
				height: 36px;

				i {
					color: #fff;
					margin: 0 10px 0 0;
					font-size: 14px;
				}
			}

			.add-button {
				cursor: pointer;
				border: 0;
				padding: 0px 15px;
				margin: 0 10px 0 0;
				outline: none;
				color: #fff;
				border-radius: 0px;
				background: #333;
				width: auto;
				font-size: 14px;
				line-height: 36px;
				height: 36px;

				i {
					color: #fff;
					margin: 0 10px 0 0;
					font-size: 14px;
				}
			}
		}

		.filter-options-wrapper {
			width: 100%;
			padding: 0 7%;
			margin: 10px 0 0;
			background: #fff;
			height: auto;
			order: 2;

			.filter-option {
				padding: 0px;
				margin: 4px 0;
				border-color: #eee;
				background: none;
				border-width: 0 0 1px;
				width: 100%;
				position: relative;
				border-style: dashed;
				height: auto;

				.filter-label {
					padding: 0 5px;
					color: #555;
					text-align: right;
					display: inline-block;
					width: auto;
					line-height: 32px;
					font-size: 14px;
				}

				.filter-items {
					width: auto;
					display: inline-block;
					height: auto;

					.filter-item {
						display: inline-block;
						margin-right: 10px;
						padding: 0 15px;
						cursor: pointer;
						border-radius: 4px;

						&.active {
							background-color: #F5BB00;
							color: #fff;
						}
					}
				}
			}
		}

		.list-wrapper {
			width: 100%;
			padding: 0 7%;
			margin: 10px auto;
			background: #fff;
			order: 4;

			.list-content {
				padding: 0 0px;
				overflow: hidden;
				flex-wrap: wrap;
				background: #fff;
				display: flex;
				width: 100%;
				clear: both;
				justify-content: space-between;
				height: auto;

				.list-item {
					cursor: pointer;
					margin-bottom: 20px;
					display: flex;
					width: 100%;
					transition: all 0.3s;

					&:hover {
						transform: translateY(-5px);
						box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
					}

					.item-image {
						border: 1px solid #f6f6f6;
						width: 200px;
						padding: 0px;
						margin: 0 10px 0 0;
						overflow: hidden;
						height: 200px;

						img {
							width: 100%;
							object-fit: cover;
							display: block;
							height: 100%;
						}
					}

					.item-info {
						padding: 0 10px;
						overflow: hidden;
						flex: 1;
						display: flex;
						height: auto;

						.info-main {
							width: 50%;
							padding: 0;
							align-items: center;
							flex-wrap: wrap;
							flex: 1;
							display: flex;

							.item-title {
								width: 100%;
								font-size: 16px;
								margin-bottom: 10px;
							}

							.item-price {
								color: #F5BB00;
								font-size: 18px;
								font-weight: bold;

								span {
									font-size: 12px;
								}
							}

							.item-time {
								width: 100%;
								padding: 2px 10px;
								border-color: #f0f0f0;
								border-style: dashed;
								border-width: 0 0 1px 0;
								display: none;

								.icon {
									margin-right: 5px;
								}
							}
						}

						.item-desc {
							flex: 1;
							color: #666;
							font-size: 14px;
							line-height: 1.6;
							overflow: hidden;
							text-overflow: ellipsis;
							display: -webkit-box;
							-webkit-line-clamp: 3;
							-webkit-box-orient: vertical;
						}
					}
				}
			}
		}

		.list-pagination {
			padding: 0 7%;
			margin: 10px auto;
			white-space: nowrap;
			overflow: hidden;
			color: #333;
			text-align: center;
			width: 100%;
			clear: both;
			font-weight: 500;
			order: 50;
		}
	}

	.preview-image {
		width: 100%;
	}
}


.back-button {
	border: 0;
	cursor: pointer;
	padding: 0 10px;
	margin: 0 5px 0 0;
	outline: none;
	color: #333;
	border-radius: 4px;
	background: #F5BB0050;
	width: auto;
	line-height: 40px;
	font-size: 14px;
	height: 40px;
}


.list-preview .list-form-pv .el-input {
	width: auto;
}

.breadcrumb-preview .el-breadcrumb /deep/ .el-breadcrumb__separator {
	margin: 0 9px;
	color: #ccc;
	font-weight: 500;
}

.breadcrumb-preview .el-breadcrumb .item1 /deep/ .el-breadcrumb__inner a {
	color: #333;
	display: inline-block;
}

.breadcrumb-preview .el-breadcrumb .item2 /deep/ .el-breadcrumb__inner a {
	color: #666;
	display: inline-block;
}

.category-1 .item {
	cursor: pointer;
	border-radius: 0px;
	padding: 0 20px;
	margin: 0 10px 10px;
	color: #fff;
	background: #333;
	width: auto;
	font-size: 16px;
	line-height: 44px;
	text-align: center;
}

.category-1 .item:hover {
	cursor: pointer;
	margin: 0 10px 10px;
	color: #fff;
	background: #F5BB00;
	width: auto;
	font-size: 16px;
	line-height: 44px;
	text-align: center;
}

.category-1 .item.active {
	border-radius: 0px;
	margin: 0 10px 10px;
	color: #fff;
	background: #F5BB00;
	width: auto;
	font-size: 16px;
	line-height: 44px;
}

.category-2 .item {
	cursor: pointer;
	border-radius: 4px;
	margin: 0 0 10px 0;
	color: #999;
	background: #efefef;
	width: 100%;
	font-size: 14px;
	line-height: 36px;
	text-align: center;
}

.category-2 .item:hover {
	cursor: pointer;
	border-radius: 4px;
	margin: 0 0 10px 0;
	color: #999;
	background: #efefef;
	width: 100%;
	font-size: 14px;
	line-height: 36px;
	text-align: center;
}

.category-2 .item.active {
	cursor: pointer;
	border-radius: 4px;
	margin: 0 0 10px 0;
	color: #999;
	background: #efefef;
	width: 100%;
	font-size: 14px;
	line-height: 36px;
	text-align: center;
}

.category-3 .item {
	cursor: pointer;
	padding: 0;
	margin: 0 0 26px;
	color: #333;
	display: flex;
	border-color: #ddd;
	line-height: 30px;
	flex-wrap: wrap;
	border-radius: 0px;
	background: none;
	width: 15%;
	justify-content: center;
	border-width: 0 1px 0 0;
	align-items: center;
	border-style: solid;
	text-align: center;
	height: 80px;
}

.category-3 .item:hover {
	border-radius: 0;
	padding: 0;
	color: #333;
	background: none;
	width: 15%;
	line-height: 30px;
}

.category-3 .item.active {
	border-radius: 0;
	padding: 0;
	color: #333;
	background: none;
	width: 15%;
	line-height: 30px;
}

.list-form-pv .el-input /deep/ .el-input__inner {
	border: 1px solid #ddd;
	border-radius: 0px;
	padding: 0 10px;
	margin: 0;
	outline: none;
	color: #333;
	width: auto;
	font-size: 14px;
	line-height: 36px;
	height: 36px;
}


.list-form-pv .el-date-editor /deep/ .el-input__inner {
	border: 1px solid #ddd;
	border-radius: 0px;
	padding: 0 30px;
	margin: 0;
	outline: none;
	color: #333;
	width: auto;
	font-size: 14px;
	line-height: 36px;
	height: 36px;
}

.list .index-pv1 .animation-box {
	transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
	z-index: initial;
}

.list .index-pv1 .animation-box:hover {
	transform: rotate(0deg) scale(1.02) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
	-webkit-perspective: 1000px;
	perspective: 1000px;
	transition: 0.3s;
	z-index: 1;
}

.list .index-pv1 .animation-box img {
	transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
}

.list .index-pv1 .animation-box img:hover {
	transform: rotate(0deg) scale(1.05) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
	-webkit-perspective: 1000px;
	perspective: 1000px;
	transition: 0.3s;
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

.list3 .list-item {
	border: 1px solid #eee;
	cursor: pointer;
	padding: 10px;
	margin: 0 0 30px;
	overflow: hidden;
	background: #fff;
	display: flex;
	width: 49%;
	font-size: 0;
	align-items: center;
	position: relative;
	height: auto;
}

.list3 .list-item:hover {
	border: 1px solid #F5BB0030;
	background: #F5BB00;
}

.list3 .list-item .title {
	padding: 0 10px;
	overflow: hidden;
	color: #333;
	white-space: nowrap;
	background: none;
	width: 100%;
	font-size: 15px;
	border-color: #eee;
	border-width: 0 0 1px 0;
	line-height: 24px;
	text-overflow: ellipsis;
	border-style: dashed;
}

.list3 .list-item:hover .title {
	color: #fff;
}

.list3 .list-item .price {
	padding: 0 10px;
	color: #f00;
	width: 100%;
	font-size: 20px;
	border-color: #f0f0f0;
	border-width: 0 0 1px 0;
	line-height: 34px;
	border-style: dashed;
}

.list3 .list-item:hover .price {
	color: #fff;
}

.list3 .list-item .time .icon {
	margin: 0 4px 0 0;
	color: #999;
	font-size: 14px;
	line-height: 24px;
}

.list3 .list-item:hover .time .icon {
	color: #fff;
}

.list3 .list-item .time .text {
	color: #999;
	font-size: 14px;
	line-height: 24px;
}

.list3 .list-item:hover .time .text {
	color: #fff;
}

.list3 .list-item .publisher .icon {
	margin: 0 2px 0 0;
	color: #999;
	font-size: 14px;
	line-height: 24px;
}

.list3 .list-item:hover .publisher .icon {
	color: #fff;
}

.list3 .list-item .publisher .text {
	color: #999;
	font-size: 14px;
	line-height: 24px;
}

.list3 .list-item:hover .publisher .text {
	color: #fff;
}

.list3 .list-item .like .icon {
	margin: 0 2px 0 0;
	color: #999;
	font-size: 14px;
	line-height: 24px;
}

.list3 .list-item:hover .like .icon {
	color: #fff;
}

.list3 .list-item .like .text {
	color: #999;
	font-size: 14px;
	line-height: 24px;
}

.list3 .list-item:hover .like .text {
	color: #fff;
}

.list3 .list-item .collect .icon {
	margin: 0 2px 0 0;
	color: #999;
	font-size: 14px;
	line-height: 24px;
}

.list3 .list-item:hover .collect .icon {
	color: #fff;
}

.list3 .list-item .collect .text {
	color: #999;
	font-size: 14px;
	line-height: 24px;
}

.list3 .list-item:hover .collect .text {
	color: #fff;
}

.list3 .list-item .view .icon {
	margin: 0 2px 0 0;
	color: #999;
	font-size: 14px;
	line-height: 24px;
}

.list3 .list-item:hover .view .icon {
	color: #fff;
}

.list3 .list-item .view .text {
	color: #999;
	font-size: 14px;
	line-height: 24px;
}

.list3 .list-item:hover .view .text {
	color: #fff;
}

.list3 .list-item .desc {
	padding: 0 0 0 20px;
	overflow: hidden;
	color: #666;
	text-indent: 2em;
	display: flex;
	width: 50%;
	font-size: 14px;
	line-height: 24px;
	align-items: center;
	height: 196px;
}

.list3 .list-item:hover .desc {
	color: #fff;
}

.select2 .list .item-body .item {
	padding: 0 5px;
	color: #666;
	display: inline-block;
	font-size: 14px;
	line-height: 32px;
}

.select2 .list .item-body .item:hover {
	color: #F5BB00;
	background: none;
}

.select2 .list .item-body .item.active {
	color: #F5BB00;
	background: none;
}
</style>
