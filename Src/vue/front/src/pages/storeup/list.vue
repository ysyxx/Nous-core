<template>
	<div class="storeup-container">
		<el-button class="back-button" type="warning" size="mini" @click="backClick" icon="el-icon-back">返回</el-button>
		<div v-if="storeupType == 1" class="section-title">我的收藏</div>
		<el-form :inline="true" :model="formSearch" class="search-form">
			<el-form-item>
				<el-input v-model="formSearch.name" placeholder="名称"></el-input>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" @click="getStoreupList(1)">查询</el-button>
			</el-form-item>
		</el-form>
		<el-row class="storeup-list" :gutter="20">
			<el-col :span="6" v-for="(item, index) in storeupList" :key="index" @click.native="toDetail(item)">
				<el-card class="storeup-card">
					<el-image v-if="item.picture && item.picture.substr(0, 4) == 'http'" :src="item.picture" fit="fill"
						class="card-image"></el-image>
					<el-image v-else :src="baseUrl + item.picture" fit="fill" class="card-image"></el-image>
					<div class="card-content">
						<span>{{ item.name }}</span>
					</div>
				</el-card>
			</el-col>
		</el-row>

		<el-pagination background class="storeup-pagination" :pager-count="7" :page-size="pageSize"
			:page-sizes="pageSizes" prev-text="<" next-text=">" :hide-on-single-page="false"
			:layout='["total", "prev", "pager", "next", "sizes", "jumper"].join()' :total="total" @current-change="curChange"
			@prev-click="prevClick" @next-click="nextClick"></el-pagination>
	</div>
</template>

<script>
import config from '@/config/config'
export default {
	data() {
		return {
			layouts: '',
			baseUrl: config.baseUrl,
			formSearch: {
				name: ''
			},
			storeupType: 1,
			storeupList: [],
			total: 1,
			pageSize: 8,
			pageSizes: [10, 20, 30, 50],
			totalPage: 1
		}
	},
	created() {
		this.storeupType = localStorage.getItem('storeupType');
		this.getStoreupList(1);
	},
	methods: {
		backClick() {
			this.$router.push('/index/center')
		},
		getStoreupList(page) {
			let params = { page, limit: this.pageSize, type: this.storeupType, userid: localStorage.getItem('frontUserid'), sort: "addtime", order: "desc" };
			let searchWhere = {
			};
			if (this.formSearch.name != '') searchWhere.name = '%' + this.formSearch.name + '%';
			this.$http.get('storeup/list', { params: Object.assign(params, searchWhere) }).then(res => {
				if (res.data.code == 0) {
					this.storeupList = res.data.data.list;
					this.total = res.data.data.total;
					this.pageSize = res.data.data.pageSize; this.pageSizes = [this.pageSize, this.pageSize * 2, this.pageSize * 3, this.pageSize * 5];
					this.totalPage = res.data.data.totalPage;
				}
			});
		},
		curChange(page) {
			this.getStoreupList(page);
		},
		prevClick(page) {
			this.getStoreupList(page);
		},
		nextClick(page) {
			this.getStoreupList(page);
		},
		toDetail(item) {
			this.$router.push({ path: `/index/${item.tablename}Detail`, query: { id: item.refid } });
		}
	}
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.storeup-container {
	width: 100%;
	padding: 30px 7% 40px;
	margin: 0 auto;
	position: relative;
	background: #fff;
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

.section-title {
	margin: 10px 0;
	color: #fff;
	text-align: center;
	background: #333;
	width: 100%;
	font-size: 20px;
	line-height: 54px;
}

.search-form {
	margin-top: 20px;
}

.storeup-list {
	margin-top: 20px;
}

.storeup-card {
	margin-bottom: 20px;
}

.card-image {
	width: 100%;
	height: 200px;
}

.card-content {
	padding: 14px;
}

.storeup-pagination {
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

.formSearch {
	text-align: right;
}

.image {
	height: 233px;
	width: 100%;
	display: block;
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
