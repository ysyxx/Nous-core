<template>
    <div class="course-detail-container">
        <!-- 面包屑导航 -->
        <div class="breadcrumb-section">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item to="/"><a>首页</a></el-breadcrumb-item>
                <el-breadcrumb-item v-for="(item, index) in breadcrumbItem" :key="index" to="/index/course">
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
                    <div class="meta-value">{{ key === 'addtime' ? formatTime(detail[key]) : detail[key] }}
</div>
                </div>

                <div class="action-buttons">
                    <el-button v-if="btnAuth('course', '修改')" @click="editClick" class="edit-btn">
                        <i class="el-icon-edit-outline"></i>修改
                    </el-button>
                    <el-button v-if="btnAuth('course', '删除')" @click="delClick" class="delete-btn">
                        <i class="el-icon-delete2"></i>删除
                    </el-button>
                    <el-button @click="storeup(1)" v-if="!isStoreup" class="favorite-btn">
                        <i class="el-icon-star-off"></i>收藏
                    </el-button>
                    <el-button @click="storeup(0)" v-else class="favorite-btn active">
                        <i class="el-icon-star-on"></i>已收藏
                    </el-button>
                    <el-button @click="thumbsupOrCrazily(1)" v-if="!isThumbsupnum&&!isCrazilynum" class="like-btn">
                        <i class="el-icon-dianzan1"></i>赞
                    </el-button>
                    <el-button @click="cancelThumbsupOrCrazily(1)" v-else-if="!isCrazilynum" class="like-btn active">
                        <i class="el-icon-dianzan1"></i>已赞
                    </el-button>
                    <el-button @click="thumbsupOrCrazily(0)" v-if="!isThumbsupnum&&!isCrazilynum" class="dislike-btn">
                        <i class="el-icon-cai"></i>踩
                    </el-button>
                    <el-button @click="cancelThumbsupOrCrazily(0)" v-else-if="!isThumbsupnum" class="dislike-btn active">
                        <i class="el-icon-cai"></i>已踩
                    </el-button>
                </div>
            </div>
        </div>

        <!-- 课程内容 -->
        <div class="course-content-section">
            <div class="content-header">
                <h3 class="section-title">课程内容</h3>
                <el-button v-if="pdfUrl" size="mini" type="primary" @click="downloadDocument" class="download-btn">
                    下载文档
                </el-button>
            </div>

            <!-- 文档展示区域 -->
            <div class="document-viewer" v-if="pdfUrl">
                <iframe :src="`/pdfjs/web/viewer.html?file=${encodeURIComponent(pdfUrl)}`" frameborder="0" width="100%"
                    height="600px" style="border: 1px solid #ccc; border-radius: 6px;">
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
                    :class="{ active: video.id === lessonId }">
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
            tablename: 'course',
            baseUrl: this.$config ? this.$config.baseUrl : 'http://localhost:8080/springbootn3op2l20/',
            detail: {},
            detailBanner: [],
            isStoreup: 0,
            isCrazilynum: 0,
            isThumbsupnum: 0,
            storeupParams: {},
            id: '',
            courseVideos: [],
            lessonId: null,
            currentVideoUrl: '',
            currentVideoTitle: '',
            userId: null,
            pdfUrl: '',
            documentContent: null,
            isPdf: false,
            isLoadingDocument: false,
            pdfViewerUrl: '',
            isChaptersExpanded: true, // 默认展开
            isCurrentLessonVideo: false, // 控制视频播放器是否显示，默认不显示
            lessonId: null, // 当前选中的课程视频ID
            progressSeconds: 0,
            progressPercent: 0,
            completed: 0,
            playedLessons: {},
            jumped: {},
            // 元数据字段配置
            courseMetaFields: {
                title: '课程名称',
                kechengleixing: '课程类型',
                addtime: '发布日期',
                clickNum: '点击次数',
                thumbsUpNum: '赞',
                crazilyNum: '踩'
            },


            metaLabels: {
                title: '课程名称',
                kechengleixing: '课程类型',
                addtime: '发布日期',
                clickNum: '点击次数',
                thumbsUpNum: '赞',
                crazilyNum: '踩'
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


    beforeDestroy() {
        if (this.$refs.videoPlayer) {
            this.$refs.videoPlayer.removeEventListener('timeupdate', this.handleTimeUpdate);
            this.$refs.videoPlayer.removeEventListener('ended', this.handleVideoEnded);
        }
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
                        this.lessonId = this.id;
                        this.currentVideoUrl = this.detail.kechengshipin;
                        this.currentVideoTitle = this.detail.kechengmingcheng;
                        this.$nextTick(async () => {
                            if (this.$refs.videoPlayer && this.$refs.videoPlayer.paused) {
                                this.$refs.videoPlayer.play().catch(e => console.warn("视频播放失败或用户未交互:", e));
                            }
                        });
                    }

                    if (this.userId) {
                        this.getStoreupStatus();
                    }


                }
            } catch (error) {
                console.error("请求课程详情接口出错:", error);
            }
        },


            formatTime(timestamp) {
        if (!timestamp) return '';
        const date = new Date(Number(timestamp));
        return date.toLocaleDateString(); // 或自定义格式
    },

        async handleTimeUpdate() {
            const video = this.$refs.videoPlayer;
            if (!video || video.duration === 0) return;

            this.progressSeconds = Math.floor(video.currentTime);
            this.progressPercent = Math.floor((video.currentTime / video.duration) * 100);

            // 每30秒保存一次进度（你也可以改成每5秒）
            if (this.progressSeconds % 5 === 0) {
                await this.saveLearningProgress();
            }
        },

        // 视频播放完毕
        async handleVideoEnded() {
            this.progressSeconds = Math.floor(this.$refs.videoPlayer.duration);
            this.progressPercent = 100;
            this.completed = 1;
            this.lastPosition=0;
            await this.saveLearningProgress();
        },

        


        // ... 其他现有方法保持不变 ...
        async getCourseVideos() {
            try {
                const videosRes = await this.$http.get('lessons/listByCourseId', { params: { courseId: this.id } });
                if (videosRes.data && videosRes.data.code === 0 && videosRes.data.data) {

                    this.courseVideos = videosRes.data.data;
                }
            } catch (error) {
                console.error("请求课程视频列表接口出错:", error);
            }
        },

        async findLastViewedVideoId() {
            // 确保已获取用户ID和课程视频列表
            if (!this.userId || this.courseVideos.length === 0) return null;

            // 遍历所有课程视频
            for (const video of this.courseVideos) {

                try {
                    const progressRes = await this.$http.get('learningprogress/info', {
                        params: {
                            lessonId: this.lessonId
                        }
                    });
                    // 检查是否有进度记录，并且 progressSeconds 大于 0
                    if (progressRes.data && progressRes.data.code === 0 && progressRes.data.data && progressRes.data.data.progressSeconds > 0) {
                        return video.id; // 返回有进度的视频ID
                    }
                } catch (error) {
                    console.error(`检查视频 ${video.id} 进度时出错:`, error);
                    // 发生错误时，继续检查下一个视频，或者根据需求选择是否中断
                }
            }
            return null; // 如果所有视频都没有进度，返回 null
        },

        playSelectedVideo(videoId) {
            this.playedLessons[this.lessonId]=false;
            this.isCurrentLessonVideo = false;
            this.currentVideoUrl = '';
            this.pdfUrl = '';
            this.pdfViewerUrl = '';
            this.lessonId = null;

            const selectedVideo = this.courseVideos.find(video => video.id === videoId);
            if (!selectedVideo) return;

            this.lessonId = selectedVideo.id;

            if (selectedVideo.pdfUrl) {
                this.pdfUrl = selectedVideo.pdfUrl;
                this.pdfViewerUrl = `/pdfjs/web/viewer.html?file=${encodeURIComponent(this.pdfUrl)}`;
            }

            if (selectedVideo.lessonType === 'video') {
                this.isCurrentLessonVideo = true;
                this.currentVideoUrl = selectedVideo.videoUrl;

                this.$nextTick(() => {
                    const video = this.$refs.videoPlayer;
                    if (video) {
                        video.load(); // 加载新视频源

                        // ✅ 绑定事件只需要一次，可以判断是否已绑定
                        if (!this.videoEventsBound) {
                            video.addEventListener('timeupdate', this.handleTimeUpdate);
                            video.addEventListener('pause', this.saveLearningProgress);
                            video.addEventListener('ended', this.handleVideoEnded);
                            this.videoEventsBound = true;
                        }

                        video.oncanplay = async () => {
                            if (this.currentLessonTotalDuration === 0) {
                                this.currentLessonTotalDuration = video.duration;
                            }
                            if (!this.playedLessons[this.lessonId]) {
                                this.playedLessons[this.lessonId] = true;
                                await this.getLearningProgressAndSeek();
                            }
                            if (video.paused) {
                                video.play().catch(e => console.warn("自动播放失败:", e));
                            }
                        };
                    }
                });
            } else if (selectedVideo.lessonType === 'article') {
                this.isCurrentLessonVideo = false;
                // 可扩展文章阅读逻辑
            }
        },


        async getLearningProgressAndSeek() {
            if (!this.lessonId || !this.$refs.videoPlayer) return;
            this.playedLessons[this.lessonId]=false;
            try {
                const res = await this.$http.get(`${this.baseUrl}learningprogress/info`, {
                    params: { lessonId: this.lessonId }
                });
                if (res.data && res.data.code === 0) {
                    const progress = res.data.data;
                    if (progress) {
                        // ✅ 有进度：跳转到记录位置
                        if (progress && progress.lastPosition != null) {
                            this.lastPosition = progress.lastPosition;
                            this.$refs.videoPlayer.currentTime = progress.lastPosition;
                        }
                    } else {

                        const payload = {
                            lessonId: this.lessonId,
                            progressSeconds: 0,
                            progressPercent: 0,
                            completed: 0
                        };
                        await this.$http.post(`${this.baseUrl}learningprogress/saveOrUpdate`, payload);

                    }
  
                }
            } catch (e) {
                console.error("获取或创建学习进度失败：", e);
            }finally{
                this.playedLessons[this.lessonId]=true;
            }
        },

        async saveLearningProgress() {
            if(!this.playedLessons[this.lessonId])  return;
            if (!this.lessonId || !this.userId) return;
          
            const video = this.$refs.videoPlayer;
            if (!video || video.duration === 0) return;

            const payload = {
                lessonId: this.lessonId,
                progressSeconds: this.progressSeconds, // 最大观看位置
                progressPercent: this.progressPercent,
                completed: this.completed,
                lastPosition: video.currentTime 
            };

            try {
                const res = await this.$http.post(`${this.baseUrl}learningprogress/saveOrUpdate`, payload);
                if (res.data.code !== 0) {
                    console.warn("学习进度保存失败：", res.data.msg);
                } 
            } catch (e) {
                console.error("保存学习进度时出错", e);
            }
        },

        getFileNameFromUrl(url) {
            if (!url) return '';
            const parts = url.split('/');
            return parts[parts.length - 1];
        },

        downloadDocument() {
            const pdfUrl = this.pdfUrl;
            console.log("下载文档的URL:", pdfUrl);
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
            if (this.userId && this.lessonId && this.$refs.videoPlayer) {
                if (Math.floor(this.$refs.videoPlayer.currentTime) % 5 === 0 && Math.floor(this.$refs.videoPlayer.currentTime) !== 0) {
                    this.saveProgress(this.lessonId, this.$refs.videoPlayer.currentTime, this.$refs.videoPlayer.duration);
                }
            }
        },

        onVideoEnded() {
            if (this.userId && this.lessonId && this.$refs.videoPlayer) {
                this.saveProgress(this.lessonId, this.$refs.videoPlayer.duration || 0, this.$refs.videoPlayer.duration);
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

        async storeup(type) { 
            if (!this.userId) {
                this.$message.error('请先登录后操作');
                return
            }
     
            this.storeupParams.userId = this.userId;
            this.storeupParams.courseId = this.id;
            
            
            this.storeupParams.thumbsUp=this.isThumbsupnum;
            this.storeupParams.thumbsDown=this.isCrazilynum;
            let res = null;
            if (type == 1) {
                this.storeupParams.favorite = 2;
                res = await this.$http.post('storeup/add', this.storeupParams);
            }
            else{
                this.storeupParams.favorite = -1;
                res = await this.$http.post('storeup/cancel', this.storeupParams);
            }
            if (res.data.code == 0) {
   
                this.detail.storeupnum = res.data.data;
                
                this.isStoreup = type;

            } else {
                this.$message.error(res.data.msg || '操作失败');
            }
        },

   
        async getStoreupStatus() {
            if (!this.userId) return;
            let params = {
                userId: this.userId,
                courseId: this.id,
            }
            let res = await this.$http.post('storeup/status', params);
            
            this.isStoreup =res.data.data.favorite;
            this.isThumbsupnum = res.data.data.thumbsUp;
            this.isCrazilynum = res.data.data.thumbsDown;
        },

        async thumbsupOrCrazily(type) {
            if (!this.userId) {
                this.$message.error('请先登录后操作');
                return
            }

            if(type==1){
                this.storeupParams.userId = this.userId;
                this.storeupParams.courseId = this.id;
                this.storeupParams.thumbsUp = 2;
            }
            else{
                this.storeupParams.userId = this.userId;
                this.storeupParams.courseId = this.id;
                this.storeupParams.thumbsDown = 2;
            }
            this.storeupParams.favorite=this.isStoreup; // 保持收藏状态
            console.log("storeupParams:", this.storeupParams);
            let res = await this.$http.post('storeup/add', this.storeupParams);
            if (res.data.code == 0) {
                if (type == 1) {
                    this.detail.thumbsupnum = res.data.data;
                    this.isThumbsupnum = 1;
                    this.isCrazilynum = 0;
                } else {
                    this.detail.crazilynum = res.data.data;
                    this.isCrazilynum = 1;
                    this.isThumbsupnum = 0;
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

            if(type==1){
                this.storeupParams.userId = this.userId;
                this.storeupParams.courseId = this.id;
                this.storeupParams.thumbsUp = -1;
            }
            else{
                this.storeupParams.userId = this.userId;
                this.storeupParams.courseId = this.id;
                this.storeupParams.thumbsDown = -1;
            }
            

            let res = await this.$http.post('storeup/cancel', this.storeupParams);
            if (res.data.code == 0) {
                if (type == 1) {
                    this.detail.thumbsupnum = res.data.data;
                    this.isThumbsupnum = 0;
                    this.isCrazilynum = 0;
                } else {
                    this.detail.crazilynum = res.data.data;
                    this.isCrazilynum = 0;
                    this.isThumbsupnum = 0;
                }
                this.$message.success('操作成功!');
            } else {
                this.$message.error(res.data.msg || '操作失败');
            }
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
            let res = await this.$http.get('course/detail/' + this.id);
            this.detail = res.data.data;
            if (this.detail.pdfUrl) {
                this.pdfUrl = this.detail.pdfUrl;
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