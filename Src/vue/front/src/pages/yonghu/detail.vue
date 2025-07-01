<template>
	<div class="detail-container">
		<!-- 面包屑导航 -->
		<div class="breadcrumb-wrapper">
			<el-breadcrumb separator="Ξ">
				<el-breadcrumb-item class="breadcrumb-item" to="/"><a>首页</a></el-breadcrumb-item>
				<el-breadcrumb-item class="breadcrumb-item" v-for="(item, index) in breadcrumbItem" :key="index"
					to="/index/yonghu">
					<a>{{ item.name }}</a>
				</el-breadcrumb-item>
				<el-breadcrumb-item class="breadcrumb-item">
					<a href="javascript:void(0);">详情</a>
				</el-breadcrumb-item>
			</el-breadcrumb>
		</div>

		<!-- 返回按钮 -->
		<div class="back-button-wrapper">
			<el-button size="mini" @click="backClick" icon="el-icon-back" type="warning">返回</el-button>
		</div>

		<!-- 主要内容区 -->
		<div class="detail-content">
			<!-- 左侧信息区 -->
			<div class="detail-info">
				<div class="info-header">
					{{ detail.yonghuxingming }}
				</div>

				<div class="info-item">
					<div class="info-label">用户账号</div>
					<div class="info-value">{{ detail.yonghuzhanghao }}</div>
				</div>

				<div class="info-item">
					<div class="info-label">性别</div>
					<div class="info-value">{{ detail.xingbie }}</div>
				</div>

				<div class="info-item">
					<div class="info-label">电话</div>
					<div class="info-value">{{ detail.dianhua }}</div>
				</div>

				<div class="action-buttons">
					<el-button class="edit-button" v-if="btnAuth('yonghu', '修改')" @click="editClick">修改</el-button>
					<el-button class="delete-button" v-if="btnAuth('yonghu', '删除')" @click="delClick">删除</el-button>
				</div>
			</div>

			<!-- 右侧图片轮播区 -->
			<div class="detail-gallery" v-if="detailBanner.length">
				<div class="gallery-main">
					<div class="swiper-container mySwiper21">
						<div class="swiper-wrapper">
							<div class="swiper-slide" v-for="item in detailBanner" :key="item.id">
								<img v-if="item.substr(0, 4) == 'http'" :src="item" class="gallery-image">
								<img v-else :src="baseUrl + item" class="gallery-image">
							</div>
						</div>
						<div class="swiper-button-prev">
							<span class="icon iconfont icon-jiantou39"></span>
						</div>
						<div class="swiper-button-next">
							<span class="icon iconfont icon-jiantou18"></span>
						</div>
					</div>
				</div>

				<div class="gallery-thumbnail">
					<div class="swiper-container mySwiper22">
						<div class="swiper-wrapper">
							<div class="swiper-slide" v-for="item in detailBanner" :key="item.id">
								<img v-if="item.substr(0, 4) == 'http'" :src="item" class="thumbnail-image">
								<img v-else :src="baseUrl + item" class="thumbnail-image">
							</div>
						</div>
						<div class="swiper-button-prev">
							<span class="icon iconfont icon-jiantou39"></span>
						</div>
						<div class="swiper-button-next">
							<span class="icon iconfont icon-jiantou18"></span>
						</div>
					</div>
				</div>
			</div>

			<!-- 底部扩展区 -->
			<div class="detail-extension">
				<el-tabs class="detail-tabs" v-model="activeName" type="border-card">
					<!-- 标签内容 -->
				</el-tabs>
			</div>
		</div>

		<!-- 分享按钮 -->
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
			tablename: 'yonghu',
			baseUrl: '',
			breadcrumbItem: [
				{
					name: '用户'
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
					this.title = this.detail.yonghuxingming;
					this.detailBanner = this.detail.touxiang ? this.detail.touxiang.split(",") : [];
					this.$forceUpdate();

					if (localStorage.getItem('frontToken')) {
					}

				}
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
			this.$router.push(`/index/yonghuAdd?type=edit&&id=${this.detail.id}`);
		},
		// 删除
		delClick() {
			this.$confirm('是否删除此用户？')
				.then(_ => {
					this.$http.post('yonghu/delete', [this.detail.id]).then(res => {
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
.detail-container {
	width: 100%;
	padding: 0 7%;
	margin: 0 auto;
	position: relative;

	.breadcrumb-wrapper {
		padding: 20px 7%;
		margin: 0px auto;
		border-color: #ddd;
		border-radius: 0px;
		background: none;
		border-width: 0 0 1px;
		width: 100%;
		border-style: solid;

		.breadcrumb-item {
			font-size: 14px;
			line-height: 1;
		}
	}

	.back-button-wrapper {
		padding: 20px 7%;
		margin: 0px auto;
		border-color: #ddd;
		border-radius: 0px;
		background: none;
		border-width: 0 0 1px;
		width: 100%;
		border-style: solid;
	}

	.detail-content {
		padding: 0 7%;
		margin: 40px auto;
		display: flex;
		flex-wrap: wrap;
		width: 100%;
		position: relative;
		justify-content: space-between;

		.detail-info {
			min-height: 480px;
			padding: 0;
			margin: 0;
			display: flex;
			flex-wrap: wrap;
			width: 55%;
			position: relative;
			justify-content: space-between;
			order: 2;
			background: none;

			.info-header {
				width: 100%;
				padding: 5px 20px;
				margin: 0 0 10px 0;
				overflow: hidden;
				align-items: center;
				background: #F5BB00;
				display: flex;
				line-height: 24px;
				justify-content: space-between;
				color: #fff;
				font-size: 16px;
			}

			.info-item {
				padding: 0 10px;
				margin: 0 0 0px 0;
				border-color: #ddd;
				background: none;
				border-width: 0 0 1px;
				display: flex;
				border-style: dashed;
				justify-content: space-between;

				.info-label {
					padding: 0 10px;
					color: #999;
					text-align: right;
					width: auto;
					font-size: 14px;
					line-height: 40px;
					height: 40px;
				}

				.info-value {
					padding: 8px 10px 0;
					font-size: 14px;
					line-height: 24px;
					color: #666;
					flex: 1;
					height: auto;
				}
			}

			.action-buttons {
				padding: 10px 0;
				display: flex;
				flex-wrap: wrap;

				.edit-button {
					border: 0;
					cursor: pointer;
					padding: 0 10px;
					margin: 0 5px 0 0;
					outline: none;
					color: #333;
					border-radius: 4px;
					background: #009cf550;
					width: auto;
					line-height: 40px;
					font-size: 14px;
					height: 40px;
				}

				.delete-button {
					border: 0;
					cursor: pointer;
					padding: 0 10px;
					margin: 0 5px 0 0;
					outline: none;
					color: #333;
					border-radius: 4px;
					background: #f5340050;
					width: auto;
					line-height: 40px;
					font-size: 14px;
					height: 40px;
				}
			}
		}

		.detail-gallery {
			width: 40%;
			margin: 0 5% 0 0;
			background: #fff;
			height: auto;
			order: 1;

			.gallery-main {
				width: 100%;
				height: auto;

				.swiper-container {
					width: 100%;
					height: auto;

					.gallery-image {
						object-fit: cover;
						width: 100%;
						height: 480px;
					}

					.swiper-button-prev {
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

					.swiper-button-next {
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

			.gallery-thumbnail {
				width: 100%;
				display: none;
				height: auto;

				.swiper-container {
					width: 100%;
					height: auto;

					.thumbnail-image {
						object-fit: cover;
						width: 100%;
						height: 100px;
					}

					.swiper-button-prev {
						width: 14px;
						margin: -7px 0 0;
						top: 50%;
						height: 14px;

						.icon {
							width: 14px;
							font-size: 14px;
							color: #fff;
							height: 14px;
						}
					}

					.swiper-button-next {
						width: 14px;
						margin: -7px 0 0;
						top: 50%;
						height: 14px;

						.icon {
							width: 14px;
							font-size: 14px;
							color: #fff;
							height: 14px;
						}
					}
				}
			}
		}

		.detail-extension {
			border: 0px solid #ddd;
			padding: 0px;
			margin: 40px 0;
			border-radius: 0px;
			background: #fff;
			flex: 1;
			width: 65%;
			clear: both;
			order: 50;

			.detail-tabs {
				border: 1px solid #ddd;
				width: 100%;
				box-shadow: none;
				margin: 20px auto;
				background: #fff;
			}
		}
	}

	.share-view {
		box-shadow: 0 1px 6px rgba(0, 0, 0, .3);
		position: fixed;
		right: 0;
		bottom: 20%;
		background: #fff;
		z-index: 11;
	}
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
