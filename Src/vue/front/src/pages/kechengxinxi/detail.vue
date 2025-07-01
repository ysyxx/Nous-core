<template>
    <div class="course-detail-container">
        <!-- 面包屑导航 -->
        <div class="breadcrumb-section">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item to="/"><a>首页</a></el-breadcrumb-item>
                <el-breadcrumb-item v-for="(item, index) in breadcrumbItem" :key="index" to="/index/kechengxinxi">
                    <a>{{ item.name }}</a>
                </el-breadcrumb-item>
                <el-breadcrumb-item>详情</el-breadcrumb-item>
            </el-breadcrumb>
            <el-button size="mini" @click="backClick" class="back-btn" icon="el-icon-back" type="warning">返回</el-button>
        </div>

        <!-- 课程基本信息 -->
        <div class="basic-info-section">
            <div class="course-images">
                <el-carousel :indicator-position='detailBanner.length > 1 ? "outside" : "none"' :interval="5000"
                    :autoplay="true">
                    <el-carousel-item v-for="item in detailBanner" :key="item">
                        <img :src="baseUrl + item" />
                    </el-carousel-item>
                </el-carousel>
            </div>

            <div class="course-meta">
                <div class="meta-item" v-for="(value, key) in courseMetaFields" :key="key">
                    <div class="meta-label">{{ metaLabels[key] }}</div>
                    <div class="meta-value">{{ detail[key] }}</div>
                </div>

                <div class="action-buttons">
                    <el-button v-if="btnAuth('kechengxinxi', '修改')" @click="editClick" class="edit-btn">
                        <i class="el-icon-edit-outline"></i>修改
                    </el-button>
                    <el-button v-if="btnAuth('kechengxinxi', '删除')" @click="delClick" class="delete-btn">
                        <i class="el-icon-delete2"></i>删除
                    </el-button>
                    <el-button @click="storeup(1)" v-if="!isStoreup" class="favorite-btn">
                        <i class="el-icon-star-off"></i>收藏
                    </el-button>
                    <el-button @click="storeup(2)" v-else class="favorite-btn active">
                        <i class="el-icon-star-on"></i>已收藏
                    </el-button>
                    <el-button @click="thumbsupOrCrazily(1)" v-if="!isThumbsupnum" class="like-btn">
                        <i class="el-icon-dianzan1"></i>赞
                    </el-button>
                    <el-button @click="cancelThumbsupOrCrazily(21)" v-else class="like-btn active">
                        <i class="el-icon-dianzan1"></i>已赞
                    </el-button>
                    <el-button @click="thumbsupOrCrazily(0)" v-if="!isCrazilynum" class="dislike-btn">
                        <i class="el-icon-cai"></i>踩
                    </el-button>
                    <el-button @click="cancelThumbsupOrCrazily(22)" v-else class="dislike-btn active">
                        <i class="el-icon-cai"></i>已踩
                    </el-button>
                </div>
            </div>
        </div>

        <!-- 课程内容 -->
        <div class="course-content-section">
            <div class="content-header">
                <h3 class="section-title">课程内容</h3>
                <el-button v-if="contentpdfurl" size="mini" type="primary" @click="downloadDocument"
                    class="download-btn">
                    下载文档
                </el-button>
            </div>

            <!-- 文档展示区域 -->
            <div class="document-viewer" v-if="contentpdfurl">
                <iframe :src="`/pdfjs/web/viewer.html?file=${encodeURIComponent(contentpdfurl)}`" frameborder="0"
                    width="100%" height="600px" style="border: 1px solid #ccc; border-radius: 6px;">
                </iframe>
            </div>

        </div>


        <!-- 视频播放区域 -->
        <div class="video-section" v-if="currentVideoUrl || detail.kechengshipin">
            <h3 class="section-title">
                视频课程: {{ currentVideoTitle || detail.kechengmingcheng }}
            </h3>
            <video ref="videoPlayer" :src="currentVideoUrl || detail.kechengshipin" controls autoplay width="100%"
                height="auto" @timeupdate="onTimeUpdate" @ended="onVideoEnded"></video>
        </div>
        <div class="no-video" v-else>
            <p>暂无视频</p>
        </div>

        <!-- 视频章节列表 -->
        <div class="video-chapters" v-if="courseVideos.length > 0">
            <div class="section-header">
                <h3 class="section-title">视频章节</h3>
                <button class="toggle-btn" @click.stop="isChaptersExpanded = !isChaptersExpanded"
                    :aria-expanded="isChaptersExpanded">
                    <span class="toggle-text">{{ isChaptersExpanded ? '收起章节' : '展开章节' }}</span>
                    <svg class="toggle-icon" width="12" height="8" viewBox="0 0 12 8"
                        :class="{ 'expanded': isChaptersExpanded }">
                        <path d="M1 1L6 6L11 1" stroke="currentColor" stroke-width="2" />
                    </svg>
                </button>
            </div>
            <ul class="chapter-list" v-show="isChaptersExpanded">
                <li v-for="video in courseVideos" :key="video.id" @click.stop="playSelectedVideo(video.id)"
                    :class="{ active: video.id === currentSessionId }">
                    {{ video.courseSession }} - {{ video.title || '无标题' }}
                </li>
            </ul>
        </div>
    </div>
</template>

<script>
import { Vue } from 'vue';

export default {
    data() {
        return {
            tablename: 'kechengxinxi',
            baseUrl: this.$config ? this.$config.baseUrl : 'http://localhost:8080/springbootn3op2l20/',
            detail: {},
            detailBanner: [],
            isStoreup: false,
            isCrazilynum: false,
            isThumbsupnum: false,
            storeupParams: {},
            storeupParams: {},
            id: '',
            courseVideos: [],
            currentSessionId: null,
            currentVideoUrl: '',
            currentVideoTitle: '',
            userId: null,
            contentpdfurl: '',
            documentContent: null,
            isPdf: false,
            isLoadingDocument: false,
            pdfViewerUrl: '',
            isChaptersExpanded: true, // 默认展开

            // 元数据字段配置
            courseMetaFields: {
                kechengmingcheng: '课程名称',
                jiaoshimc: '教师名称',
                kechengleixing: '课程类型',
                faburiqi: '发布日期',
                clicknum: '点击次数',
                thumbsupnum: '赞',
                crazilynum: '踩'
            },

            metaLabels: {
                kechengmingcheng: '课程名称',
                jiaoshimc: '教师名称',
                kechengleixing: '课程类型',
                faburiqi: '发布日期',
                clicknum: '点击次数',
                thumbsupnum: '赞',
                crazilynum: '踩'
            }
        };
    },
    async created() {
        this.id = this.$route.query.id;
        this.getDetail();

        try {
            const userRes = await this.$http.get('yonghu/currentUserId');
            if (userRes.data && userRes.data.code === 0) {
                this.userId = userRes.data.userId;
            }
        } catch (error) {
            console.error("请求用户ID接口出错:", error);
        }

        this.init();
    },
    methods: {
        async init() {
            try {
                let res = await this.$http.get(`${this.tablename}/detail/${this.id}`);
                if (res.data && res.data.code === 0 && res.data.data) {
                    this.detail = res.data.data;
                    this.detailBanner = this.detail.kechengtupian ? this.detail.kechengtupian.split(',') : [];

                    await this.getCourseVideos();

                    if (this.courseVideos.length > 0) {
                        let lastViewedVideoId = await this.findLastViewedVideoId();
                        if (lastViewedVideoId) {
                            this.playSelectedVideo(lastViewedVideoId);
                        } else {
                            this.playSelectedVideo(this.courseVideos[0].id);
                        }
                    } else if (this.detail.kechengshipin) {
                        this.currentSessionId = this.id;
                        this.currentVideoUrl = this.detail.kechengshipin;
                        this.currentVideoTitle = this.detail.kechengmingcheng;
                        this.$nextTick(async () => {
                            await this.restoreProgress(this.currentSessionId);
                            if (this.$refs.videoPlayer && this.$refs.videoPlayer.paused) {
                                this.$refs.videoPlayer.play().catch(e => console.warn("视频播放失败或用户未交互:", e));
                            }
                        });
                    }

                    // 加载文档内容
                    // 若你想换成 PDF.js（你有部署 pdf.js 的话）：
                    // this.pdfViewerUrl = `/pdfjs/web/viewer.html?file=${encodeURIComponent(this.contentpdfurl)}`;
                    if (this.detail.contentpdfurl) {
                        this.contentpdfurl = this.detail.contentpdfurl;
                        this.pdfViewerUrl = `/pdfjs/web/viewer.html?file=${encodeURIComponent(this.contentpdfurl)}`;
                    }




                    if (this.userId) {
                        this.getStoreupStatus();
                        this.getThumbsupOrCrazilyStatus();
                    }


                }
            } catch (error) {
                console.error("请求课程详情接口出错:", error);
            }
        },

        downloadDocument() {
            const pdfUrl = this.detail.contentpdfurl;
            if (pdfUrl) {
                const documentFileName = this.getFileNameFromUrl(pdfUrl);
                const link = document.createElement('a');
                link.href = pdfUrl;

                link.setAttribute('download', documentFileName);
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);

                console.log(`正在下载文档: ${documentFileName}`);
            } else {
                this.$message.warning('没有可下载的文档。');
            }
        },

        // ... 其他现有方法保持不变 ...
        async getCourseVideos() {
            try {

                const videosRes = await this.$http.get('coursevideos/listByCourseId', { params: { courseId: this.id } });
                if (videosRes.data && videosRes.data.code === 0 && videosRes.data.data) {
                    this.courseVideos = videosRes.data.data;
                }
            } catch (error) {
                console.error("请求课程视频列表接口出错:", error);
            }
        },

        async findLastViewedVideoId() {
            if (!this.userId || this.courseVideos.length === 0) return null;

            for (const video of this.courseVideos) {
                try {
                    const progressRes = await this.$http.get('courseprogress/getOne', {
                        params: {
                            courseId: this.id,
                            videoId: video.id
                        }
                    });
                    if (progressRes.data && progressRes.data.code === 0 && progressRes.data.data && progressRes.data.data.progressSeconds > 0) {
                        return video.id;
                    }
                } catch (error) {
                    console.error(`检查视频 ${video.id} 进度时出错:`, error);
                }
            }
            return null;
        },

        playSelectedVideo(videoId) {
            const selectedVideo = this.courseVideos.find(video => video.id === videoId);
            if (selectedVideo) {
                this.currentSessionId = selectedVideo.id;
                this.currentVideoUrl = selectedVideo.videoUrl;
                this.currentVideoTitle = `${selectedVideo.courseSession || ''} - ${selectedVideo.title || '无标题'}`;
                this.$nextTick(async () => {
                    await this.restoreProgress(this.currentSessionId);
                    if (this.$refs.videoPlayer && this.$refs.videoPlayer.paused) {
                        this.$refs.videoPlayer.play().catch(e => console.warn("视频播放失败或用户未交互", e));
                    }
                });
            }
        },

        getFileNameFromUrl(url) {
            if (!url) return '';
            const parts = url.split('/');
            return parts[parts.length - 1];
        },

        downloadDocument() {
            const pdfUrl = this.detail.contentpdfurl;
            if (pdfUrl) {
                const documentFileName = this.getFileNameFromUrl(pdfUrl);
                const link = document.createElement('a');
                link.href = pdfUrl;
                link.setAttribute('download', documentFileName);
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            } else {
                this.$message.warning('没有可下载的文档。');
            }
        },

        onTimeUpdate() {
            if (this.userId && this.currentSessionId && this.$refs.videoPlayer) {
                if (Math.floor(this.$refs.videoPlayer.currentTime) % 5 === 0 && Math.floor(this.$refs.videoPlayer.currentTime) !== 0) {
                    this.saveProgress(this.currentSessionId, this.$refs.videoPlayer.currentTime, this.$refs.videoPlayer.duration);
                }
            }
        },

        onVideoEnded() {
            if (this.userId && this.currentSessionId && this.$refs.videoPlayer) {
                this.saveProgress(this.currentSessionId, this.$refs.videoPlayer.duration || 0, this.$refs.videoPlayer.duration);
            }
        },

        async saveProgress(videoId, currentTime, totalDuration) {
            if (!this.userId || !videoId || !this.$refs.videoPlayer) return;

            const progressData = {
                userId: this.userId,
                courseId: Number(this.id),
                videoId: Number(videoId),
                progressSeconds: currentTime,
                totalSeconds: totalDuration
            };

            try {
                const res = await this.$http.post('courseprogress/saveOrUpdate', progressData);
                if (res.data.code !== 0) {
                    console.error("保存学习进度失败:", res.data.msg);
                }
            } catch (error) {
                console.error("请求保存进度接口出错:", error);
            }
        },

        async restoreProgress(videoId) {
            if (!this.userId || !videoId || !this.$refs.videoPlayer) return;
            try {
                const res = await this.$http.get('courseprogress/getOne', {
                    params: {
                        courseId: this.id,
                        videoId: videoId
                    }
                });
                const data = res.data;
                if (data.code === 0 && data.data && data.data.progressSeconds > 0) {
                    this.$refs.videoPlayer.currentTime = parseFloat(data.data.progressSeconds);
                } else {
                    this.$refs.videoPlayer.currentTime = 0;
                }
            } catch (error) {
                console.error("请求获取进度接口出错:", error);
                this.$refs.videoPlayer.currentTime = 0;
            }
        },

        onAcross(tableName, crossOptAudit, compareKey, compareValue, id) {
            if (this.userId) {
                this.$router.push({
                    path: `/index/${tableName}`,
                    query: {
                        crossOptAudit: crossOptAudit,
                        compareKey: compareKey,
                        compareValue: compareValue,
                        id: id
                    }
                })
            } else {
                this.$message.error('请先登录后操作');
            }
        },

        async storeup(status) {
            if (!this.userId) {
                this.$message.error('请先登录后操作');
                return
            }
            this.storeupParams.userid = this.userId;
            this.storeupParams.name = this.detail.kechengmingcheng;
            this.storeupParams.picture = this.detail.kechengtupian;
            this.storeupParams.refid = this.id;
            let res = await this.$http.post('storeup/add', this.storeupParams, { params: { type: status } });
            if (res.data.code == 0) {
                this.detail.storeupnum = res.data.data;
                this.isStoreup = status == 1;
            } else {
                this.$message.error(res.data.msg || '操作失败');
            }
        },

        async getStoreupStatus() {
            if (!this.userId) return;
            let params = {
                userid: this.userId,
                refid: this.id,
                tablename: 'kechengxinxi',
                type: 1
            }
            let res = await this.$http.get('storeup/list', { params: params });
            this.isStoreup = res.data.code == 0 && res.data.data.list && res.data.data.list.length > 0;
        },

        async thumbsupOrCrazily(type) {
            if (!this.userId) {
                this.$message.error('请先登录后操作');
                return
            }
     
            this.storeupParams.userid = this.userId;
            this.storeupParams.refid = this.id;
            this.storeupParams.type = type;
            this.storeupParams.tablename = this.tablename;
            this.storeupParams.name = this.detail.kechengmingcheng;
            this.storeupParams.picture = this.detail.kechengtupian;

            let res = await this.$http.post('storeup/add', this.storeupParams);
            if (res.data.code == 0) {
                if (type == 21) {
                    this.detail.thumbsupnum = res.data.data;
                    this.isThumbsupnum = true;
                    this.isCrazilynum = false;
                } else {
                    this.detail.crazilynum = res.data.data;
                    this.isCrazilynum = true;
                    this.isThumbsupnum = false;
                }
                this.$message.success('操作成功!');
            } else {
                this.$message.error(res.data.msg || '操作失败');
            }
        },

        async cancelThumbsupOrCrazily(type) {
            if (!this.userId) {
                this.$message.error('请先登录后操作');
                return
            }
            let params = {
                userid: this.userId,
                refid: this.id,
                tablename: this.tablename,
                type: type
            }
            let resList = await this.$http.get('storeup/list', { params: params });
            if (resList.data.code == 0 && resList.data.data.list.length > 0) {
                let delId = resList.data.data.list[0].id;
                let res = await this.$http.post('storeup/delete', [delId]);
                if (res.data.code == 0) {
                    if (type == 21) {
                        this.detail.thumbsupnum = res.data.data;
                        this.isThumbsupnum = false;
                    } else {
                        this.detail.crazilynum = res.data.data;
                        this.isCrazilynum = false;
                    }
                    this.$message.success('取消成功!');
                } else {
                    this.$message.error(res.data.msg || '取消失败');
                }
            } else {
                this.$message.error('未找到可取消的记录');
            }
        },

        async getThumbsupOrCrazilyStatus() {
            if (!this.userId) return;
            let params = {
                userid: this.userId,
                refid: this.id,
                tablename: this.tablename
            }
            let resThumbsup = await this.$http.get('storeup/list', { params: { ...params, type: 21 } });
            this.isThumbsupnum = resThumbsup.data.code == 0 && resThumbsup.data.data.list && resThumbsup.data.data.list.length > 0;

            let resCrazily = await this.$http.get('storeup/list', { params: { ...params, type: 22 } });
            this.isCrazilynum = resCrazily.data.code == 0 && resCrazily.data.data.list && resCrazily.data.data.list.length > 0;
        },

        backClick() {
            this.$router.go(-1);
        },

        download(file) {
            window.location.href = this.baseUrl + file
        },

        btnAuth(tablename, key) {
            if (!key) return true;
            if (!this.$store.state.user || !this.$store.state.user.session || !this.$store.state.user.session.menuList) return false
            return this.$store.state.user.session.menuList.filter(item => {
                return item.tablename == tablename && item.buttonname == key;
            }).length > 0
        },

        editClick() {
            this.$router.push('/index/' + this.tablename + '/add/' + this.id)
        },

        async delClick() {
            this.$confirm('确定要删除吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                let res = await this.$http.post(this.tablename + '/delete', [this.id]);
                if (res.data.code == 0) {
                    this.$message({
                        message: '删除成功',
                        type: 'success',
                        duration: 500,
                        onClose: () => {
                            this.$router.go(-1);
                        }
                    });
                } else {
                    this.$message.error(res.data.msg || '删除失败');
                }
            });
        },

        async getDetail() {
            let res = await this.$http.get('kechengxinxi/detail/' + this.id);
            this.detail = res.data.data;
            if (this.detail.contentpdfurl) {
                this.contentpdfurl = this.detail.contentpdfurl;
            }
        },
    }
};
</script>

<style scoped lang="scss">
.course-detail-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
    font-family: 'Helvetica Neue', Arial, sans-serif;
    color: #333;
    line-height: 1.6;
}

/* 面包屑导航 */
.breadcrumb-section {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px 0;
    border-bottom: 1px solid #eee;
    margin-bottom: 20px;

    .back-btn {
        background-color: #f5f5f5;
        border-color: #ddd;
        color: #666;

        &:hover {
            background-color: #e6e6e6;
        }
    }
}

/* 基本信息区域 */
.basic-info-section {
    display: flex;
    gap: 30px;
    margin-bottom: 40px;

    .course-images {
        flex: 1;
        min-width: 0;

        .el-carousel {
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

            img {
                width: 100%;
                height: 350px;
                object-fit: cover;
            }
        }
    }

    .course-meta {
        flex: 1;
        min-width: 0;

        .meta-item {
            display: flex;
            margin-bottom: 15px;

            .meta-label {
                width: 100px;
                font-weight: 600;
                color: #555;
            }

            .meta-value {
                flex: 1;
                padding: 8px 12px;
                background-color: #f8f9fa;
                border-radius: 4px;
                color: #333;
            }
        }

        .action-buttons {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            margin-top: 25px;

            .el-button {
                border-radius: 4px;
                transition: all 0.3s;

                i {
                    margin-right: 5px;
                }

                &.edit-btn {
                    border-color: #409EFF;
                    color: #409EFF;

                    &:hover {
                        background-color: #ecf5ff;
                    }
                }

                &.delete-btn {
                    border-color: #F56C6C;
                    color: #F56C6C;

                    &:hover {
                        background-color: #fef0f0;
                    }
                }

                &.favorite-btn {
                    border-color: #E6A23C;
                    color: #E6A23C;

                    &.active {
                        background-color: #fdf6ec;
                        color: #E6A23C;
                    }

                    &:hover {
                        background-color: #fdf6ec;
                    }
                }

                &.like-btn {
                    border-color: #67C23A;
                    color: #67C23A;

                    &.active {
                        background-color: #f0f9eb;
                        color: #67C23A;
                    }

                    &:hover {
                        background-color: #f0f9eb;
                    }
                }

                &.dislike-btn {
                    border-color: #909399;
                    color: #909399;

                    &.active {
                        background-color: #f4f4f5;
                        color: #909399;
                    }

                    &:hover {
                        background-color: #f4f4f5;
                    }
                }
            }
        }
    }
}

/* 详情区域 */
.course-detail-section {
    margin-bottom: 40px;

    .detail-block {
        background: #fff;
        border-radius: 8px;
        padding: 20px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

        .detail-title {
            font-size: 18px;
            color: #333;
            padding-bottom: 10px;
            margin-bottom: 15px;
            border-bottom: 1px solid #eee;
        }

        .detail-content {
            line-height: 1.8;

            >>>img {
                max-width: 100%;
                height: auto;
            }
        }
    }
}

/* 视频区域 */
.video-section,
.no-video {
    margin-bottom: 40px;

    .section-title {
        font-size: 18px;
        color: #333;
        margin-bottom: 15px;
    }

    video {
        width: 100%;
        border-radius: 8px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    }
}

.no-video {
    text-align: center;
    color: #999;
    padding: 20px;
    background: #f8f9fa;
    border-radius: 8px;
}

/* 课程内容区域 */
.course-content-section {
    margin-bottom: 40px;

    .content-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15px;

        .section-title {
            font-size: 18px;
            color: #333;
            margin: 0;
        }

        .download-btn {
            background-color: #409EFF;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 6px;
            font-size: 14px;
            font-weight: 500;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 8px;

            &:hover {
                background-color: #66b1ff;
                transform: translateY(-2px);
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
            }

            &:active {
                transform: translateY(0);
                box-shadow: 0 2px 3px rgba(0, 0, 0, 0.1);
            }
        }
    }

    .document-viewer {
        background: #f8f9fa;
        border-radius: 8px;
        padding: 20px;
        margin-bottom: 20px;
        min-height: 300px;

        iframe {
            width: 100%;
            height: 600px;
            border: none;
            border-radius: 4px;
        }
    }

    .html-content {
        background: #fff;
        padding: 20px;
        border-radius: 8px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
        line-height: 1.8;

        >>>img {
            max-width: 100%;
            height: auto;
        }
    }
}

/* 视频章节列表 */
.video-chapters {
    .section-title {
        font-size: 18px;
        color: #333;
        margin-bottom: 15px;
    }

    .chapter-list {
        list-style: none;
        padding: 0;
        margin: 0;
        background: #fff;
        border-radius: 8px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
        overflow: hidden;

        li {
            padding: 12px 20px;
            border-bottom: 1px solid #eee;
            cursor: pointer;
            transition: all 0.2s;

            &:last-child {
                border-bottom: none;
            }

            &:hover {
                background-color: #f5f7fa;
            }

            &.active {
                background-color: #ecf5ff;
                color: #409EFF;
                font-weight: 500;
            }
        }
    }
}

/* 响应式设计 */
@media (max-width: 768px) {
    .basic-info-section {
        flex-direction: column;
    }

    .course-images,
    .course-meta {
        width: 100%;
    }

    .action-buttons {
        justify-content: center;
    }
}

/* 新增的样式 */
.loading-text {
    padding: 20px;
    text-align: center;
    color: #999;
}

.document-viewer {
    margin-top: 16px;
    margin-bottom: 20px;
    background-color: #fafafa;
    padding: 8px;
    border-radius: 8px;
}

//展开视频章节
.section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
}

.toggle-btn {
    display: flex;
    align-items: center;
    gap: 6px;
    background: none;
    border: 1px solid #ddd;
    border-radius: 4px;
    padding: 4px 8px;
    cursor: pointer;
    font-size: 14px;
    color: #666;
    transition: all 0.2s ease;
}

.toggle-btn:hover {
    background-color: #f5f5f5;
    border-color: #ccc;
}

.toggle-icon {
    transition: transform 0.2s ease;
}

.toggle-icon.expanded {
    transform: rotate(180deg);
}

.chapter-list {
    margin: 0;
    padding: 0;
    list-style: none;
}
</style>