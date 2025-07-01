<template>
	<!-- 详情页容器 -->
	<div class="detail-page">
		<!-- 面包屑导航区 -->
		<div class="breadcrumb-nav">
			<el-breadcrumb separator="Ξ">
				<el-breadcrumb-item to="/">
					<a>首页</a>
				</el-breadcrumb-item>
				<el-breadcrumb-item v-for="(item, index) in breadcrumbItem" :key="index" to="/index/kechengpingjia">
					<a>{{ item.name }}</a>
				</el-breadcrumb-item>
				<el-breadcrumb-item>
					<a href="javascript:void(0);">详情</a>
				</el-breadcrumb-item>
			</el-breadcrumb>
		</div>

		<!-- 返回按钮区 -->
		<div class="back-button-section">
			<el-button size="mini" @click="backClick" icon="el-icon-back" type="warning">返回</el-button>
		</div>

		<!-- 主要内容区 -->
		<div class="detail-content">
			<!-- 图片轮播区 -->
			<div v-if="detailBanner.length" class="image-gallery">
				<div class="main-gallery">
					<div class="swiper-container">
						<div class="swiper-wrapper">
							<!-- 轮播图项 -->
							<div class="swiper-slide" v-for="item in detailBanner" :key="item.id">
								<div class="gallery-image">
									<!-- 图片显示，支持网络图片和本地图片 -->
									<img v-if="item.substr(0, 4) == 'http'" :src="item" class="detail-img">
									<img v-else :src="baseUrl + item" class="detail-img">
								</div>
							</div>
						</div>
						<!-- 轮播导航按钮 -->
						<div class="swiper-nav-prev">
							<span class="icon iconfont icon-jiantou39"></span>
						</div>
						<div class="swiper-nav-next">
							<span class="icon iconfont icon-jiantou18"></span>
						</div>
					</div>
				</div>
			</div>

			<!-- 详情信息区 -->
			<div class="detail-info">
				<div class="info-section">
					<!-- 标题区 -->
					<div class="info-title">
						{{ detail.kechengmingcheng }}
					</div>

					<!-- 详情项列表 -->
					<!-- 课程类型 -->
					<div class="info-item">
						<div class="item-label">课程类型</div>
						<div class="item-value">{{ detail.kechengleixing }}</div>
					</div>

					<!-- 教学目标 -->
					<div class="info-item">
						<div class="item-label">教学目标</div>
						<div class="item-value">{{ detail.jiaoxuemubiao }}</div>
					</div>

					<!-- 教学方法 -->
					<div class="info-item">
						<div class="item-label">教学方法</div>
						<div class="item-value">{{ detail.jiaoxuefangfa }}</div>
					</div>

					<!-- 教学内容 -->
					<div class="info-item">
						<div class="item-label">教学内容</div>
						<div class="item-value">{{ detail.jiaoxueneirong }}</div>
					</div>

					<!-- 教学效果 -->
					<div class="info-item">
						<div class="item-label">教学效果</div>
						<div class="item-value">{{ detail.jiaoxuexiaoguo }}</div>
					</div>

					<!-- 评价时间 -->
					<div class="info-item">
						<div class="item-label">评价时间</div>
						<div class="item-value">{{ detail.pingjiashijian }}</div>
					</div>

					<!-- 评价建议 -->
					<div class="info-item">
						<div class="item-label">评价建议</div>
						<div class="item-value">{{ detail.pingjiajianyi }}</div>
					</div>

					<!-- 用户账号 -->
					<div class="info-item">
						<div class="item-label">用户账号</div>
						<div class="item-value">{{ detail.yonghuzhanghao }}</div>
					</div>

					<!-- 用户姓名 -->
					<div class="info-item">
						<div class="item-label">用户姓名</div>
						<div class="item-value">{{ detail.yonghuxingming }}</div>
					</div>

					<!-- 审核状态（管理员可见） -->
					<div class="info-item" v-if="centerType">
						<div class="item-label">是否审核</div>
						<div class="item-value">{{ detail.sfsh }}</div>
					</div>

					<!-- 审核回复（管理员可见） -->
					<div class="info-item" v-if="centerType">
						<div class="item-label">审核回复</div>
						<div class="item-value">{{ detail.shhf }}</div>
					</div>

					<!-- 操作按钮区 -->
					<div class="action-buttons">
						<!-- 修改按钮（满足条件时显示） -->
						<el-button v-if="btnAuth('kechengpingjia', '修改') && detail.sfsh != '是' && detail.sfsh != '否'"
							class="edit-btn" @click="editClick">修改</el-button>

						<!-- 删除按钮 -->
						<el-button v-if="btnAuth('kechengpingjia', '删除')" class="delete-btn"
							@click="delClick">删除</el-button>

						<!-- 审核按钮（管理员可见） -->
						<el-button v-if="btnAuth('kechengpingjia', '审核') && (detail.sfsh == '待审核' || !detail.sfsh)"
							class="review-btn" @click="onSH" type="warning">审核</el-button>
					</div>
				</div>
			</div>

			<!-- 额外内容区（预留） -->
			<div class="additional-content"></div>

			<!-- 标签页区（预留） -->
			<el-tabs class="detail-tabs" v-model="activeName" type="border-card">
			</el-tabs>

			<!-- 审核对话框 -->
			<el-dialog title="审核" :visible.sync="sfshVisiable" width="50%">
				<el-form ref="shForm" :model="shForm" label-width="80px">
					<el-form-item label="审核状态">
						<el-select v-model="shForm.sfsh" placeholder="审核状态">
							<el-option label="通过" value="是"></el-option>
							<el-option label="不通过" value="否"></el-option>
							<el-option label="待审核" value="待审核"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="内容">
						<el-input type="textarea" :rows="8" v-model="shForm.shhf"></el-input>
					</el-form-item>
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="sfshVisiable = false">取 消</el-button>
					<el-button type="primary" @click="shHandler">确 定</el-button>
				</div>
			</el-dialog>
		</div>

		<!-- 分享视图（预留） -->
		<div class="share-view"></div>
	</div>
</template>

<script>
import CountDown from '@/components/CountDown';
import axios from 'axios'
import Swiper from "swiper";

export default {
	//数据集合
	data() {
		return {
			tablename: 'course_assessment',
			baseUrl: '',
			breadcrumbItem: [
				{
					name: '课程评价'
				}
			],
			title: '',
			detailBanner: [],
			userid: localStorage.getItem('frontUserid'),
			id: 0,
			detail: {},
			activeName: 'first',
			total: 1,
			pageSize: 5,
			pageSizes: [10, 20, 30, 50],
			totalPage: 1,
			buynumber: 1,
			sfshVisiable: false,
			shForm: {
				sfsh: '是',
				shhf: ''
			},
			centerType: false,
			shareUrl: location.href,
		}
	},
	created() {
		if (this.$route.query.centerType) {
			this.centerType = true
		}

		this.init();
	},
	mounted() {
		setTimeout(() => {
			let mySwiper22 = new Swiper(".mySwiper22", { "navigation": { "nextEl": ".swiper-button-next", "prevEl": ".swiper-button-prev" }, "freeMode": true, "watchSlidesVisibility": true, "watchSlidesProgress": true, "loopedSlides": 10, "slidesPerView": 5, "spaceBetween": 10 })
			let option21 = { ...{ "navigation": { "nextEl": ".swiper-button-next", "prevEl": ".swiper-button-prev" }, "loopedSlides": 10, "spaceBetween": 10, "autoplay": { "delay": 2500, "disableOnInteraction": false } } }
			option21.thumbs = {
				swiper: mySwiper22
			}
			new Swiper(".mySwiper21", option21)
		}, 100)
	},
	//方法集合
	methods: {
		init() {
			this.id = this.$route.query.id
			this.baseUrl = this.$config.baseUrl;
			this.$http.get(this.tablename + '/detail/' + this.id, {}).then(res => {
				if (res.data.code == 0) {
					this.detail = res.data.data;
					this.title = this.detail.kechengmingcheng;
					this.detailBanner = this.detail.kechengtupian ? this.detail.kechengtupian.split(",") : [];
					this.$forceUpdate();

					if (localStorage.getItem('frontToken')) {
					}

				}
			});
		},
		onSH() {
			this.sfshVisiable = true;
		},
		shHandler() {
			this.$confirm(`确定操作?`, "提示", {
				confirmButtonText: "确定",
				cancelButtonText: "取消",
				type: "warning"
			}).then(() => {
				this.detail.sfsh = this.shForm.sfsh;
				this.detail.shhf = this.shForm.shhf;
				this.$http.post(`${this.tablename}/update`, this.detail).then(res => {
					if (res.data.code == 0) {
						this.sfshVisiable = false;
						this.$message({
							message: "操作成功",
							type: "success",
							duration: 1500,
						});
					}
				});
			});
		},
		curChange(page) {
			this.getDiscussList(page);
		},
		prevClick(page) {
			this.getDiscussList(page);
		},
		nextClick(page) {
			this.getDiscussList(page);
		},
		// 返回按钮
		backClick() {
			history.back()
		},
		// 下载
		download(file) {
			if (!file) {
				this.$message({
					type: 'error',
					message: '文件不存在',
					duration: 1500,
				});
				return;
			}
			let arr = file.replace(new RegExp('upload/', "g"), "")
			axios.get(this.baseUrl + '/file/download?fileName=' + arr, {
				headers: {
					token: localStorage.getItem("frontToken")
				},
				responseType: "blob"
			}).then(({
				data
			}) => {
				const binaryData = [];
				binaryData.push(data);
				const objectUrl = window.URL.createObjectURL(new Blob(binaryData, {
					type: 'application/pdf;chartset=UTF-8'
				}))
				const a = document.createElement('a')
				a.href = objectUrl
				a.download = arr
				// a.click()
				// 下面这个写法兼容火狐
				a.dispatchEvent(new MouseEvent('click', {
					bubbles: true,
					cancelable: true,
					view: window
				}))
				window.URL.revokeObjectURL(data)
			}, err => {
				axios.get((location.href.split(this.$config.name).length > 1 ? location.href.split(this.$config.name)[0] : '') + this.$config.name + '/file/download?fileName=' + arr, {
					headers: {
						token: localStorage.getItem("frontToken")
					},
					responseType: "blob"
				}).then(({
					data
				}) => {
					const binaryData = [];
					binaryData.push(data);
					const objectUrl = window.URL.createObjectURL(new Blob(binaryData, {
						type: 'application/pdf;chartset=UTF-8'
					}))
					const a = document.createElement('a')
					a.href = objectUrl
					a.download = arr
					// a.click()
					// 下面这个写法兼容火狐
					a.dispatchEvent(new MouseEvent('click', {
						bubbles: true,
						cancelable: true,
						view: window
					}))
					window.URL.revokeObjectURL(data)
				})
			})
		},


		// 权限判断
		btnAuth(tableName, key) {
			if (this.centerType) {
				return this.isBackAuth(tableName, key)
			} else {
				return this.isAuth(tableName, key)
			}
		},
		// 修改
		editClick() {
			this.$router.push(`/index/kechengpingjiaAdd?type=edit&&id=${this.detail.id}`);
		},
		// 删除
		delClick() {
			this.$confirm('是否删除此课程评价？')
				.then(_ => {
					this.$http.post('courseAssessment/delete', [this.detail.id]).then(res => {
						if (res.data.code == 0) {
							this.$message({
								type: 'success',
								message: '删除成功!',
								duration: 1500,
								onClose: () => {
									history.back()
								}
							});
						}
					});
				}).catch(_ => { });
		},
	},
	components: {
		CountDown
	}
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
/* 详情页整体样式 */
.detail-page {
	width: 100%;
	padding: 0;
	margin: 0 auto;
	background: #fff;
}

/* 面包屑导航样式 */
.breadcrumb-nav {
	padding: 20px 7%;
	margin: 0 auto;
	border-bottom: 1px solid #ddd;
	width: 100%;

	.el-breadcrumb {
		font-size: 14px;
		line-height: 1;

		a {
			color: #333;
			text-decoration: none;
			transition: color 0.3s;

			&:hover {
				color: #F5BB00;
				/* 悬停时变为黄色 */
			}
		}
	}
}

/* 返回按钮区样式 */
.back-button-section {
	padding: 20px 7%;
	margin: 0 auto;
	border-bottom: 1px solid #ddd;
	width: 100%;
}

/* 主要内容区样式 */
.detail-content {
	padding: 0 7%;
	margin: 40px auto;
	display: flex;
	flex-wrap: wrap;
	width: 100%;
	position: relative;
	justify-content: space-between;
}

/* 图片轮播区样式 */
.image-gallery {
	width: 40%;
	margin: 0 5% 0 0;
	background: #fff;
	height: auto;
	order: 1;

	.main-gallery {
		width: 100%;
		height: auto;

		.swiper-container {
			width: 100%;
			height: 100%;

			.gallery-image {
				width: 100%;
				height: 100%;

				.detail-img {
					object-fit: cover;
					/* 图片填充方式 */
					width: 100%;
					height: 480px;
				}
			}
		}

		/* 轮播导航按钮样式 */
		.swiper-nav-prev,
		.swiper-nav-next {
			width: 24px;
			margin: -12px 0 0;
			top: 50%;
			height: 24px;

			.icon {
				width: 24px;
				font-size: 24px;
				color: #fff;
				height: 24px;
			}
		}
	}
}

/* 详情信息区样式 */
.detail-info {
	min-height: 480px;
	padding: 0;
	margin: 0;
	flex-wrap: wrap;
	background: none;
	display: flex;
	width: 55%;
	position: relative;
	justify-content: space-between;
	order: 2;

	.info-section {
		width: 100%;
		padding: 0;
		margin: 0;
		background: #fff;
		flex: 1;
		order: 2;

		/* 标题样式 */
		.info-title {
			padding: 5px 20px;
			margin: 0 0 10px 0;
			overflow: hidden;
			align-items: center;
			background: #F5BB00;
			/* 黄色背景 */
			display: flex;
			line-height: 24px;
			justify-content: space-between;
			color: #fff;
			font-size: 16px;
		}

		/* 详情项样式 */
		.info-item {
			padding: 0 10px;
			margin: 0 0 0;
			border-bottom: 1px dashed #ddd;
			/* 虚线边框 */
			background: none;
			display: flex;
			justify-content: space-between;

			/* 标签样式 */
			.item-label {
				padding: 0 10px;
				color: #999;
				/* 灰色文字 */
				text-align: right;
				width: auto;
				font-size: 14px;
				line-height: 40px;
				height: 40px;
			}

			/* 值样式 */
			.item-value {
				padding: 8px 10px 0;
				font-size: 14px;
				line-height: 24px;
				color: #666;
				/* 深灰色文字 */
				flex: 1;
				height: auto;
			}
		}

		/* 操作按钮区样式 */
		.action-buttons {
			padding: 10px 0;
			flex-wrap: wrap;
			display: flex;

			.el-button {
				border: 0;
				cursor: pointer;
				padding: 0 10px;
				margin: 0 5px 0 0;
				outline: none;
				color: #333;
				border-radius: 4px;
				width: auto;
				line-height: 40px;
				font-size: 14px;
				height: 40px;

				/* 修改按钮样式 */
				&.edit-btn {
					background: #009cf550;
					/* 浅蓝色背景 */
				}

				/* 删除按钮样式 */
				&.delete-btn {
					background: #f5340050;
					/* 浅红色背景 */
				}

				/* 审核按钮样式 */
				&.review-btn {
					background: #f5340050;
					/* 浅红色背景 */
				}
			}
		}
	}
}

/* 额外内容区样式（预留） */
.additional-content {
	border: 0;
	padding: 0;
	margin: 40px 0;
	border-radius: 0;
	background: #fff;
	flex: 1;
	width: 65%;
	clear: both;
	order: 50;
}

/* 标签页区样式 */
.detail-tabs {
	border: 1px solid #ddd;
	width: 100%;
	box-shadow: none;
	margin: 20px auto;
	background: #fff;
	order: 50;
}

/* 对话框底部样式 */
.dialog-footer {
	text-align: right;
}

/* 分享视图样式（预留） */
.share-view {
	box-shadow: 0 1px 6px rgba(0, 0, 0, .3);
	position: fixed;
	right: 0;
	bottom: 20%;
	background: #fff;
	z-index: 11;
}

.editor /deep/ .avatar-uploader {
	height: 0;
	line-height: 0;
}

.detail-preview {

	.attr {
		.el-carousel /deep/ .el-carousel__indicator button {
			width: 0;
			height: 0;
			display: none;
		}

		.el-input-number__decrease:hover:not(.is-disabled)~.el-input .el-input__inner:not(.is-disabled),
		.el-input-number__increase:hover:not(.is-disabled)~.el-input .el-input__inner:not(.is-disabled) {
			border-color: none;
		}
	}

	.detail {
		& /deep/ .el-tabs__header .el-tabs__nav-wrap {
			margin-bottom: 0;
		}

		& .add .el-textarea {
			width: auto;
		}
	}
}

.attr .el-carousel /deep/ .el-carousel__container .el-carousel__arrow--left {
	width: 36px;
	font-size: 12px;
	height: 36px;
}

.attr .el-carousel /deep/ .el-carousel__container .el-carousel__arrow--left:hover {
	background: red;
}

.attr .el-carousel /deep/ .el-carousel__container .el-carousel__arrow--right {
	width: 36px;
	font-size: 12px;
	height: 36px;
}

.attr .el-carousel /deep/ .el-carousel__container .el-carousel__arrow--right:hover {
	background: red;
}

.attr .el-carousel /deep/ .el-carousel__indicators {
	padding: 0;
	margin: 0;
	z-index: 2;
	position: absolute;
	list-style: none;
}

.attr .el-carousel /deep/ .el-carousel__indicators li {
	padding: 0;
	margin: 0 4px;
	background: #fff;
	display: inline-block;
	width: 12px;
	opacity: 0.4;
	transition: 0.3s;
	height: 12px;
}

.attr .el-carousel /deep/ .el-carousel__indicators li:hover {
	padding: 0;
	margin: 0 4px;
	background: #fff;
	display: inline-block;
	width: 24px;
	opacity: 0.7;
	height: 12px;
}

.attr .el-carousel /deep/ .el-carousel__indicators li.is-active {
	padding: 0;
	margin: 0 4px;
	background: #fff;
	display: inline-block;
	width: 24px;
	opacity: 1;
	height: 12px;
}

.attr .el-input-number /deep/ .el-input-number__decrease {
	cursor: pointer;
	z-index: 1;
	display: flex;
	border-color: #DCDFE6;
	border-radius: 4px 0 0 4px;
	top: 1px;
	left: 1px;
	background: #f5f5f5;
	width: 40px;
	justify-content: center;
	border-width: 0 1px 0 0;
	align-items: center;
	position: absolute;
	border-style: solid;
	text-align: center;
	height: 38px;
}

.attr .el-input-number /deep/ .el-input-number__decrease i {
	color: #666;
	font-size: 14px;
}

.attr .el-input-number /deep/ .el-input-number__increase {
	cursor: pointer;
	z-index: 1;
	display: flex;
	border-color: #DCDFE6;
	right: 1px;
	border-radius: 0 4px 4px 0;
	top: 1px;
	background: #f5f5f5;
	width: 40px;
	justify-content: center;
	border-width: 0 0 0 1px;
	align-items: center;
	position: absolute;
	border-style: solid;
	text-align: center;
	height: 38px;
}

.attr .el-input-number /deep/ .el-input-number__increase i {
	color: #666;
	font-size: 14px;
}

.attr .el-input-number /deep/ .el-input .el-input__inner {
	border: 1px solid #DCDFE6;
	border-radius: 4px;
	padding: 0 40px;
	outline: none;
	color: #666;
	background: #FFF;
	display: inline-block;
	width: 100%;
	font-size: 14px;
	line-height: 40px;
	text-align: center;
	height: 40px;
}

.detail-preview .detail.el-tabs /deep/ .el-tabs__header {
	margin: 0;
	background: #fff;
	border-color: #E4E7ED;
	border-width: 0 0 1px 0;
	border-style: solid;
}

.detail-preview .detail.el-tabs /deep/ .el-tabs__header .el-tabs__item {
	border: 0;
	padding: 0 20px;
	margin: 0;
	color: #999;
	background: transparent;
	font-weight: 500;
	display: inline-block;
	font-size: 16px;
	line-height: 40px;
	position: relative;
	list-style: none;
	height: 40px;
}

.detail-preview .detail.el-tabs /deep/ .el-tabs__header .el-tabs__item:hover {
	border: 0;
	color: #333;
	background: #fff;
}

.detail-preview .detail.el-tabs /deep/ .el-tabs__header .el-tabs__item.is-active {
	border: 0;
	color: #333;
	background: #fff;
	font-size: 16px;
}

.detail-preview .detail.el-tabs /deep/ .el-tabs__content {
	padding: 15px;
}

.detail-preview .detail.el-tabs .add /deep/ .el-form-item__label {
	padding: 0 10px 0 0;
	color: #666;
	width: 80px;
	font-size: 14px;
	line-height: 40px;
	text-align: right;
}

.detail-preview .detail.el-tabs .add /deep/ .el-textarea__inner {}

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

.breadcrumb-preview .el-breadcrumb .item3 /deep/ .el-breadcrumb__inner a {
	color: #999;
	display: inline-block;
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

.share_view {
	position: fixed;
	right: 0;
	bottom: 20%;
	background: #fff;
	box-shadow: 0 4px 6px rgba(0, 0, 0, .1);

	.share {
		width: 40px;
		height: 40px;
		display: flex;
		align-items: center;
		justify-content: center;
		border-bottom: 1px solid #eee;
		cursor: pointer;
	}

	.share:last-of-type {
		border: none;
	}
}

.swiper21 .swiper-button-prev:after {
	display: none;
}

.swiper21 .swiper-button-next:after {
	display: none;
}

.swiper22 .swiper-button-prev:after {
	display: none;
}

.swiper22 .swiper-button-next:after {
	display: none;
}

.swiper22 .swiper-slide div {
	width: 100%;
	opacity: 0.4;
	height: auto;
}

.swiper22 .swiper-slide.swiper-slide-thumb-active div {
	opacity: 1;
}

.detail-preview .el-rate /deep/ .el-rate__item {
	cursor: pointer;
	display: inline-block;
	vertical-align: middle;
	font-size: 0;
	position: relative;
}

.detail-preview .el-rate /deep/ .el-rate__item .el-rate__icon {
	margin: 0 3px;
	display: block;
	font-size: 18px;
	position: relative;
	transition: .3s;
}
</style>
