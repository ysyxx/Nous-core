<template>
	<div class="course-recommendation">
		<!-- 课程推荐标题 -->
		<div class="section-header">
			<div class="section-title">
				<span>课程信息推荐</span>
			</div>
		</div>

		<!-- 课程推荐列表 -->
		<div v-if="courseRecommend.length" class="course-list">
			<div class="course-item" v-for="(item, index) in courseRecommend" :key="index"
				@click="toDetail('courseDetail', item)">

				<!-- 正确处理封面图 -->
				<img class="course-image"
					:src="preHttp(item.coverImageUrl) ? item.coverImageUrl : baseUrl + item.coverImageUrl"
					:alt="item.title" />

				<!-- 正确绑定课程名与类型 -->
				<div class="course-name">{{ item.title }}</div>
				<div class="course-type">{{ item.kechengleixing }}</div>
			</div>
		</div>


		<!-- 查看更多按钮 -->
		<div class="view-more" @click="moreBtn('course')">
			<span>查看更多</span>
			<i class="icon iconfont icon-gengduo1"></i>
		</div>
	</div>
</template>


<script>
import 'animate.css'
import Swiper from "swiper";

export default {
	//数据集合
	data() {
		return {
			baseUrl: '',
			newsList: [],
			courseRecommend: [],
		}
	},
	created() {
		this.baseUrl = this.$config.baseUrl;
		this.getList();
	},
	mounted() {
		window.addEventListener('scroll', this.handleScroll)
		setTimeout(() => {
			this.handleScroll()
		}, 100)

		this.swiperChanges()
	},
	beforeDestroy() {
		window.removeEventListener('scroll', this.handleScroll)
	},
	//方法集合
	methods: {
		swiperChanges() {
			setTimeout(() => {
			}, 750)
		},
		recommendIndexClick12(index, name) {
			this['recommendIndex12' + name] = index
			this.getList()

			document.querySelectorAll('.recommend .list12' + name + ' .list .item').forEach(el => {
				el.classList.remove("active")
			})
			setTimeout(() => {
				document.querySelectorAll('.recommend .list12' + name + ' .list .item').forEach(el => {
					el.classList.add("active")
				})
			}, 1);
		},

		listIndexClick11(index, name) {
			this['listIndex11' + name] = index[this['listColumn11' + name]]
			this.getList()
		},

		handleScroll() {
			let arr = [
				{ id: 'search', css: 'animate__bounce' },
				{ id: 'about', css: 'animate__fadeInUp' },
				{ id: 'system', css: 'animate__fadeInUp' },
				{ id: 'animate_recommendcourse', css: 'animate__fadeInUp' },
				{ id: 'msgs', css: 'animate__bounce' },
				{ id: 'friendly', css: 'animate__bounce' }
			]

			for (let i in arr) {
				let doc = document.getElementById(arr[i].id)
				if (doc) {
					let top = doc.offsetTop
					let win_top = window.innerHeight + window.pageYOffset
					// console.log(top,win_top)
					if (win_top > top && doc.classList.value.indexOf(arr[i].css) < 0) {
						// console.log(doc)
						doc.classList.add(arr[i].css)
					}
				}
			}
		},
		preHttp(str) {
			return str && str.substr(0, 4) == 'http';
		},
		getList() {
			let autoSortUrl = "";
			let data = {}
			autoSortUrl = "course/autoSort";
			if (localStorage.getItem('frontToken')) {
				autoSortUrl = "course/autoSort";
			}
			
			data = {
				page: 1,
				limit: 6,
			}
	
			this.$http.get(this.baseUrl + autoSortUrl, { params: data }).then(res => {
				if (res.data.code == 0) {
					this.courseRecommend = res.data.data.list;
				}
			});

		},
		toDetail(path, item) {
			this.$router.push({ path: '/index/' + path, query: { id: item.id } });
		},
		moreBtn(path) {
			this.$router.push({ path: '/index/' + path });
		}
	}
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.course-recommendation {
	width: 100%;
	padding: 20px 0;
	margin: 0 0 20px;
	background: #f1f1f1;
	order: 1;
}

/* 标题区域 */
.section-header {
	width: 100%;
	margin: 30px auto;
}

.section-title {
	width: 100%;
	line-height: 50px;
	text-align: center;
	background: url(http://codegen.caihongy.cn/20230915/b96d1e15768f4ac495e60a79e04cb731.png) no-repeat center top;

	span {
		color: #F5BB00;
		font-size: 32px;
		line-height: 40px;
	}
}

/* 课程列表 */
.course-list {
	width: 100%;
	padding: 0 7%;
	background: none;
	height: auto;
	display: flex;
	flex-wrap: wrap;
	margin: 30px 0 0;
}

.course-item {
	cursor: pointer;
	width: 31.3%;
	margin: 0 2% 20px 0;
	overflow: hidden;
	position: relative;
	height: auto;
	transition: all 0.3s;

	&:hover {
		transform: translateY(-5px);
		box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
	}
}

.course-image {
	width: 100%;
	margin: 0 0 6px;
	object-fit: cover;
	border-radius: 6px;
	display: block;
	height: 280px;
}

.course-name,
.course-type {
	padding: 0 10px;
	border-bottom: 1px solid #ddd;
	margin: 0 0 6px;
	white-space: nowrap;
	color: #333;
	overflow: hidden;
	border-radius: 6px;
	background: #fff;
	width: 100%;
	line-height: 36px;
	font-size: 14px;
	text-overflow: ellipsis;
}

/* 查看更多按钮 */
.view-more {
	border: 0;
	cursor: pointer;
	margin: 0px auto 20px;
	border-radius: 6px;
	text-align: center;
	background: #F5BB00;
	display: block;
	width: 120px;
	line-height: 36px;
	transition: all 0.3s;

	&:hover {
		background: darken(#F5BB00, 10%);
	}

	span {
		color: #f5f5f5;
		font-size: 14px;
	}

	.icon {
		color: #f5f5f5;
		font-size: 12px;
		margin-left: 5px;
	}
}

/* 响应式设计 */
@media (max-width: 992px) {
	.course-item {
		width: 48%;
		margin-right: 2%;
	}
}

@media (max-width: 768px) {
	.course-item {
		width: 100%;
		margin-right: 0;
	}

	.section-title span {
		font-size: 24px;
	}
}

.home-preview {

	// -------- search --------
	.search .select /deep/ .el-input__inner {
		border: 1px solid #ddd;
		border-radius: 4px;
		padding: 0 30px 0 10px;
		box-shadow: 0 0 0px rgba(64, 158, 255, .3);
		outline: none;
		color: rgba(64, 158, 255, 1);
		width: 180px;
		font-size: 14px;
		height: 36px;
	}

	.search .input /deep/ .el-input__inner {
		border: 1px solid #ddd;
		border-radius: 4px;
		padding: 0 10px;
		box-shadow: 0 0 0px rgba(64, 158, 255, .3);
		outline: none;
		color: rgba(64, 158, 255, 1);
		width: 180px;
		font-size: 14px;
		height: 36px;
	}

	// -------- search --------
	.recommend {
		.list3 .swiper-button-prev {
			left: 10px;
			right: auto;
		}

		.list3 .swiper-button-prev::after {
			color: rgb(64, 158, 255);
		}

		.list3 .swiper-button-next {
			left: auto;
			right: 10px;
		}

		.list3 .swiper-button-next::after {
			color: rgb(64, 158, 255);
		}

		.list5 .swiper-button-prev {
			left: 10px;
			right: auto;
		}

		.list5 .swiper-button-prev::after {
			color: rgb(64, 158, 255);
		}

		.list5 .swiper-button-next {
			left: auto;
			right: 10px;
		}

		.list5 .swiper-button-next::after {
			color: rgb(64, 158, 255);
		}

		.list5 {
			.swiper-slide-prev {
				position: relative;
				z-index: 3;
			}

			.swiper-slide-next {
				position: relative;
				z-index: 3;
			}

			.swiper-slide-active {
				position: relative;
				z-index: 5;
			}
		}

		.index-pv1 .animation-box {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			z-index: initial;
		}

		.index-pv1 .animation-box:hover {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 10px, 0px);
			-webkit-perspective: 1000px;
			perspective: 1000px;
			transition: 0.3s;
			z-index: 1;
		}

		.index-pv1 .animation-box img {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
		}

		.index-pv1 .animation-box img:hover {
			transform: rotate(0deg) scale(1.01) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			-webkit-perspective: 1000px;
			perspective: 1000px;
			transition: 0.3s;
		}
	}

	.news {
		.list3 .swiper-button-prev {
			left: 10px;
			right: auto;
		}

		.list3 .swiper-button-prev::after {
			color: rgb(64, 158, 255);
		}

		.list3 .swiper-button-next {
			left: auto;
			right: 10px;
		}

		.list3 .swiper-button-next::after {
			color: rgb(64, 158, 255);
		}

		.list6 .swiper-button-prev {
			left: 10px;
			right: auto;
		}

		.list6 .swiper-button-prev::after {
			color: rgb(64, 158, 255);
		}

		.list6 .swiper-button-next {
			left: auto;
			right: 10px;
		}

		.list6 .swiper-button-next::after {
			color: rgb(64, 158, 255);
		}

		.index-pv1 .animation-box {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			z-index: initial;
		}

		.index-pv1 .animation-box:hover {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			-webkit-perspective: 1000px;
			perspective: 1000px;
			transition: 0.3s;
			z-index: 1;
		}

		.index-pv1 .animation-box img {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
		}

		.index-pv1 .animation-box img:hover {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			-webkit-perspective: 1000px;
			perspective: 1000px;
			transition: 0.3s;
		}
	}

	.lists {
		.list3 .swiper-button-prev {
			left: 10px;
			right: auto;
		}

		.list3 .swiper-button-prev::after {
			color: rgb(64, 158, 255);
		}

		.list3 .swiper-button-next {
			left: auto;
			right: 10px;
		}

		.list3 .swiper-button-next::after {
			color: rgb(64, 158, 255);
		}

		.list5 .swiper-button-prev {
			left: 10px;
			right: auto;
		}

		.list5 .swiper-button-prev::after {
			color: rgb(64, 158, 255);
		}

		.list5 .swiper-button-next {
			left: auto;
			right: 10px;
		}

		.list5 .swiper-button-next::after {
			color: rgb(64, 158, 255);
		}

		.list5 {
			.swiper-slide-prev {
				position: relative;
				z-index: 3;
			}

			.swiper-slide-next {
				position: relative;
				z-index: 3;
			}

			.swiper-slide-active {
				position: relative;
				z-index: 5;
			}
		}

		.index-pv1 .animation-box {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			z-index: initial;
		}

		.index-pv1 .animation-box:hover {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 10px, 0px);
			-webkit-perspective: 1000px;
			perspective: 1000px;
			transition: 0.3s;
			z-index: 1;
		}

		.index-pv1 .animation-box img {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
		}

		.index-pv1 .animation-box img:hover {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			-webkit-perspective: 1000px;
			perspective: 1000px;
			transition: 0.3s;
		}
	}
}








.home-preview .recommend .list12 .tab .item {
	cursor: pointer;
	border: 0;
	border-radius: 6px;
	padding: 0 20px;
	margin: 0 10px;
	color: #fff;
	background: #333;
	width: auto;
	font-size: 16px;
	line-height: 40px;
}

.home-preview .recommend .list12 .tab .item:hover {
	color: #fff;
	background: #F5BB00;
	border-color: #F5BB00;
	border-width: 0 0 0px 0;
	border-style: solid;
}

.home-preview .recommend .list12 .tab .item.active {
	color: #fff;
	background: #F5BB00;
	border-color: #F5BB00;
	border-width: 0 0 0px 0;
	border-style: solid;
}

.home-preview .recommend .list12 .tab .more {
	cursor: pointer;
	padding: 5px 10px;
	margin: 0 10px;
	color: #666;
	background: #fff;
	display: none;
	line-height: 44px;
	align-items: center;
}

.home-preview .recommend .list12 .tab .more:hover {
	color: red;
}

.home-preview .recommend .list12 .item.active {
	animation-name: mymove;

	&:nth-of-type(1) {
		animation-duration: 1s;
	}

	&:nth-of-type(2) {
		animation-duration: 1.2s;
	}

	&:nth-of-type(3) {
		animation-duration: 1.4s;
	}

	&:nth-of-type(4) {
		animation-duration: 1.6s;
	}

	&:nth-of-type(5) {
		animation-duration: 1.8s;
	}

	&:nth-of-type(6) {
		animation-duration: 2s;
	}
}

@keyframes mymove {
	from {
		top: 320px;
	}

	to {
		top: 0;
	}
}
</style>
