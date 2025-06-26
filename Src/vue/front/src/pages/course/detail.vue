<template>
    <div>
        <div :style='{ "padding": "20px 7%", "margin": "0px auto", "borderColor": "#ddd", "borderRadius": "0px", "background": "none", "borderWidth": "0 0 1px", "width": "100%", "borderStyle": "solid" }'
            class="breadcrumb-preview">
            <el-breadcrumb :separator="'Ξ'" :style='{ "fontSize": "14px", "lineHeight": "1" }'>
                <el-breadcrumb-item class="item1" to="/"><a>首页</a></el-breadcrumb-item>
                <el-breadcrumb-item class="item2" v-for="(item, index) in breadcrumbItem" :key="index"
                    to="/index/course"><a>{{ item.name }}</a></el-breadcrumb-item>
                <el-breadcrumb-item class="item3"><a href="javascript:void(0);">详情</a></el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div
            :style='{ "padding": "20px 7%", "margin": "0px auto", "borderColor": "#ddd", "borderRadius": "0px", "background": "none", "borderWidth": "0 0 1px", "width": "100%", "borderStyle": "solid" }'>
            <el-button size="mini" @click="backClick">返回</el-button>
        </div>

        <div class="detail-preview"
            :style='{ "padding": "0 7%", "margin": "40px auto", "flexWrap": "wrap", "display": "flex", "width": "100%", "position": "relative", "justifyContent": "space-between" }'>
            <div class="attr"
                :style='{ "minHeight": "400px", "padding": "30px", "boxShadow": "0px 0px 0px #ddd", "margin": "0 0 20px", "borderColor": "#ddd", "borderRadius": "0px", "background": "none", "borderWidth": "0 0 0px 0", "width": "48%", "borderStyle": "solid" }'>
                <el-carousel :indicator-position='detailBanner.length > 1 ? "outside" : "none"' :interval="5000"
                    :autoplay="true" :style='{ "width": "100%", "margin": "0 auto", "borderRadius": "0px", "height": "350px" }'>
                    <el-carousel-item :key="item" v-for="item in detailBanner">
                        <img :style='{ "width": "100%", "objectFit": "cover", "borderRadius": "0px", "height": "100%" }'
                            :src="baseUrl + item" />
                    </el-carousel-item>
                </el-carousel>
                <div class="info"
                    :style='{ "width": "100%", "padding": "0px", "margin": "20px 0 0 0", "background": "none", "height": "auto" }'>
                    <div class="item"
                        :style='{ "padding": "0px", "margin": "0 0 10px 0", "alignItems": "center", "background": "none", "display": "flex", "width": "100%", "justifyContent": "spaceBetween" }'>
                        <div class="title"
                            :style='{ "padding": "0 10px", "color": "#333", "textAlign": "left", "width": "90px", "fontSize": "16px", "lineHeight": "40px", "fontWeight": "600" }'>
                            课程名称</div>
                        <div class="text"
                            :style='{ "border": "0px solid #dfdfdf", "padding": "10px", "color": "#666", "borderRadius": "0px", "textAlign": "left", "background": "#f7f7f7", "flex": "1", "fontSize": "14px", "lineHeight": "24px" }'>
                            {{ detail.kechengmingcheng }}</div>
                    </div>
                    <div class="item"
                        :style='{ "padding": "0px", "margin": "0 0 10px 0", "alignItems": "center", "background": "none", "display": "flex", "width": "100%", "justifyContent": "spaceBetween" }'>
                        <div class="title"
                            :style='{ "padding": "0 10px", "color": "#333", "textAlign": "left", "width": "90px", "fontSize": "16px", "lineHeight": "40px", "fontWeight": "600" }'>
                            教师名称</div>
                        <div class="text"
                            :style='{ "border": "0px solid #dfdfdf", "padding": "10px", "color": "#666", "borderRadius": "0px", "textAlign": "left", "background": "#f7f7f7", "flex": "1", "fontSize": "14px", "lineHeight": "24px" }'>
                            {{ detail.jiaoshimc }}</div>
                    </div>
                    <div class="item"
                        :style='{ "padding": "0px", "margin": "0 0 10px 0", "alignItems": "center", "background": "none", "display": "flex", "width": "100%", "justifyContent": "spaceBetween" }'>
                        <div class="title"
                            :style='{ "padding": "0 10px", "color": "#333", "textAlign": "left", "width": "90px", "fontSize": "16px", "lineHeight": "40px", "fontWeight": "600" }'>
                            课程类型</div>
                        <div class="text"
                            :style='{ "border": "0px solid #dfdfdf", "padding": "10px", "color": "#666", "borderRadius": "0px", "textAlign": "left", "background": "#f7f7f7", "flex": "1", "fontSize": "14px", "lineHeight": "24px" }'>
                            {{ detail.lessonType }}</div>
                    </div>
                    <div class="item"
                        :style='{ "padding": "0px", "margin": "0 0 10px 0", "alignItems": "center", "background": "none", "display": "flex", "width": "100%", "justifyContent": "spaceBetween" }'>
                        <div class="title"
                            :style='{ "padding": "0 10px", "color": "#333", "textAlign": "left", "width": "90px", "fontSize": "16px", "lineHeight": "40px", "fontWeight": "600" }'>
                            发布日期</div>
                        <div class="text"
                            :style='{ "border": "0px solid #dfdfdf", "padding": "10px", "color": "#666", "borderRadius": "0px", "textAlign": "left", "background": "#f7f7f7", "flex": "1", "fontSize": "14px", "lineHeight": "24px" }'>
                            {{ detail.faburiqi }}</div>
                    </div>
                    <div class="item"
                        :style='{ "padding": "0px", "margin": "0 0 10px 0", "alignItems": "center", "background": "none", "display": "flex", "width": "100%", "justifyContent": "spaceBetween" }'>
                        <div class="title"
                            :style='{ "padding": "0 10px", "color": "#333", "textAlign": "left", "width": "90px", "fontSize": "16px", "lineHeight": "40px", "fontWeight": "600" }'>
                            点击次数</div>
                        <div class="text"
                            :style='{ "border": "0px solid #dfdfdf", "padding": "10px", "color": "#666", "borderRadius": "0px", "textAlign": "left", "background": "#f7f7f7", "flex": "1", "fontSize": "14px", "lineHeight": "24px" }'>
                            {{ detail.clicknum }}</div>
                    </div>
                    <div class="item"
                        :style='{ "padding": "0px", "margin": "0 0 10px 0", "alignItems": "center", "background": "none", "display": "flex", "width": "100%", "justifyContent": "spaceBetween" }'>
                        <div class="title"
                            :style='{ "padding": "0 10px", "color": "#333", "textAlign": "left", "width": "90px", "fontSize": "16px", "lineHeight": "40px", "fontWeight": "600" }'>
                            赞</div>
                        <div class="text"
                            :style='{ "border": "0px solid #dfdfdf", "padding": "10px", "color": "#666", "borderRadius": "0px", "textAlign": "left", "background": "#f7f7f7", "flex": "1", "fontSize": "14px", "lineHeight": "24px" }'>
                            {{ detail.thumbsupnum }}</div>
                    </div>
                    <div class="item"
                        :style='{ "padding": "0px", "margin": "0 0 10px 0", "alignItems": "center", "background": "none", "display": "flex", "width": "100%", "justifyContent": "spaceBetween" }'>
                        <div class="title"
                            :style='{ "padding": "0 10px", "color": "#333", "textAlign": "left", "width": "90px", "fontSize": "16px", "lineHeight": "40px", "fontWeight": "600" }'>
                            踩</div>
                        <div class="text"
                            :style='{ "border": "0px solid #dfdfdf", "padding": "10px", "color": "#666", "borderRadius": "0px", "textAlign": "left", "background": "#f7f7f7", "flex": "1", "fontSize": "14px", "lineHeight": "24px" }'>
                            {{ detail.crazilynum }}</div>
                    </div>
                    <div class="btn"
                        :style='{ "padding": "0px", "flexWrap": "wrap", "display": "flex", "width": "100%", "justifyContent": "center" }'>
                        <el-button
                            :style='{ "border": "1px solid #148f97", "cursor": "pointer", "padding": "0 20px", "margin": "0 10px 10px 0", "outline": "none", "color": "#148f97", "borderRadius": "0px", "background": "#fff", "width": "auto", "fontSize": "14px", "height": "36px" }'
                            v-if="btnAuth('course', '修改')" @click="editClick"><i
                                class="el-icon-edit-outline"></i>修改</el-button>
                        <el-button
                            :style='{ "border": "1px solid #dc3333", "cursor": "pointer", "padding": "0 20px", "margin": "0 10px 10px 0", "outline": "none", "color": "#dc3333", "borderRadius": "0px", "background": "#fff", "width": "auto", "fontSize": "14px", "height": "36px" }'
                            v-if="btnAuth('course', '删除')" @click="delClick"><i
                                class="el-icon-delete2"></i>删除</el-button>
                        <el-button
                            :style='{ "border": "1px solid #9240b9", "cursor": "pointer", "padding": "0 20px", "margin": "0 10px 10px 0", "outline": "none", "color": "#9240b9", "borderRadius": "0px", "background": "#fff", "width": "auto", "fontSize": "14px", "height": "36px" }'
                            @click="storeup(1)" v-if="!isStoreup"><i class="el-icon-star-off"></i>收藏</el-button>
                        <el-button
                            :style='{ "border": "1px solid #9240b9", "cursor": "pointer", "padding": "0 20px", "margin": "0 10px 10px 0", "outline": "none", "color": "#9240b9", "borderRadius": "0px", "background": "#fff", "width": "auto", "fontSize": "14px", "height": "36px" }'
                            @click="storeup(2)" v-else><i class="el-icon-star-on"></i>已收藏</el-button>
                        <el-button
                            :style='{ "border": "1px solid #f2b236", "cursor": "pointer", "padding": "0 20px", "margin": "0 10px 10px 0", "outline": "none", "color": "#f2b236", "borderRadius": "0px", "background": "#fff", "width": "auto", "fontSize": "14px", "height": "36px" }'
                            @click="thumbsupOrCrazily(21)" v-if="!isThumbsupnum"><i
                                class="el-icon-dianzan1"></i>赞</el-button>
                        <el-button
                            :style='{ "border": "1px solid #f2b236", "cursor": "pointer", "padding": "0 20px", "margin": "0 10px 10px 0", "outline": "none", "color": "#f2b236", "borderRadius": "0px", "background": "#fff", "width": "auto", "fontSize": "14px", "height": "36px" }'
                            @click="cancelThumbsupOrCrazily(21)" v-else><i class="el-icon-dianzan1"></i>已赞</el-button>
                        <el-button
                            :style='{ "border": "1px solid #54c03b", "cursor": "pointer", "padding": "0 20px", "margin": "0 10px 10px 0", "outline": "none", "color": "#54c03b", "borderRadius": "0px", "background": "#fff", "width": "auto", "fontSize": "14px", "height": "36px" }'
                            @click="thumbsupOrCrazily(22)" v-if="!isCrazilynum"><i class="el-icon-cai"></i>踩</el-button>
                        <el-button
                            :style='{ "border": "1px solid #54c03b", "cursor": "pointer", "padding": "0 20px", "margin": "0 10px 10px 0", "outline": "none", "color": "#54c03b", "borderRadius": "0px", "background": "#fff", "width": "auto", "fontSize": "14px", "height": "36px" }'
                            @click="cancelThumbsupOrCrazily(22)" v-else><i class="el-icon-cai"></i>已踩</el-button>
                    </div>
                </div>
            </div>
            <div class="content"
                :style='{ "boxShadow": "0px 0px 0px #ddd", "padding": "30px", "margin": "0 0 20px", "borderColor": "#ddd", "borderRadius": "0px", "background": "none", "borderWidth": "0 0 0px 0", "width": "48%", "borderStyle": "solid" }'>
                <div class="detail-content-item"
                    :style='{ "padding": "0px", "margin": "0 0 20px 0", "background": "#fff", "flexWrap": "wrap", "display": "flex", "width": "100%", "position": "relative", "justifyContent": "space-between" }'>
                    <div class="top"
                        :style='{ "width": "100%", "padding": "0px 0 10px", "margin": "0px auto", "borderColor": "#ddd", "textAlign": "left", "borderWidth": "0px 0px 1px 0px", "background": "none", "borderStyle": "solid" }'>
                        <div
                            :style='{ "padding": "0 10px", "lineHeight": "auto", "fontSize": "16px", "color": "#333", "fontWeight": "600" }'>
                            课程详情</div>
                    </div>
                    <div class="content"
                        :style='{ "padding": "10px", "width": "100%", "margin": "10px 0 0 0", "fontSize": "14px", "color": "#666", "textIndent": "2em", "lineHeight": "2" }'>
                        <div v-html="detail.kechenggaiyao"></div>
                    </div>
                </div>
            </div>
        </div>

        <div v-if="currentVideoUrl" :style='{ "padding": "20px 7%" }'>
            <h2 :style='{ "marginBottom": "10px", "fontSize": "24px", "color": "#333" }'>当前播放: {{ currentVideoTitle }}</h2>
            <video ref="videoPlayer" :src="currentVideoUrl" controls autoplay width="100%" height="auto"
                @timeupdate="onTimeUpdate" @ended="onVideoEnded"></video>
        </div>
        <div v-else-if="detail.kechengshipin" :style='{ "padding": "20px 7%" }'>
            <h2 :style='{ "marginBottom": "10px", "fontSize": "24px", "color": "#333" }'>课程视频</h2>
            <video ref="videoPlayer" :src="detail.kechengshipin" controls autoplay width="100%" height="auto"
                @timeupdate="onTimeUpdate" @ended="onVideoEnded"></video>
        </div>
        <div v-else :style='{ "padding": "20px 7%" }'>
            <p>暂无视频</p>
        </div>


        
        <div>
            <div class="detail2"
                :style='{ "border": "0px solid #ddd", "padding": "0px", "margin": "40px 0", "borderRadius": "0px", "background": "#fff", "flex": "1", "width": "65%", "clear": "both", "order": "50" }'>
                <div
                    :style='{ "border": "1px solid #ddd", "padding": "10px", "margin": "0 0 10px 0", "borderRadius": "4px", "background": "#fcfcfc", "display": "flex", "justifyContent": "space-between", "alignItems": "center" }'>
                    <div :style='{ "color": "#333", "fontSize": "16px", "fontWeight": "600" }'>课程内容</div>
                    <el-button v-if="contentpdfurl" size="mini" type="primary" @click="downloadDocument"
                        :style='{ "marginLeft": "10px" }'>下载文档</el-button>
                </div>
                <div :style='{ "background": "#FFF" }' v-html="detail.kechengneirong"></div>
            </div>

        </div>

        <div v-if="courseVideos.length > 0" :style='{ "padding": "20px 7%" }'>
            <h3 :style='{ "marginBottom": "10px", "fontSize": "20px", "color": "#333" }'>视频章节</h3>
            <ul :style='{ "listStyle": "none", "padding": "0" }'>
                <li v-for="video in courseVideos" :key="video.id" @click="playSelectedVideo(video.id)"
                    :style='{ "cursor": "pointer", "padding": "10px", "marginBottom": "5px", "borderBottom": "1px solid #eee", "background": video.id === currentSessionId ? "#e6f7ff" : "none", "fontWeight": video.id === currentSessionId ? "bold" : "normal" }'>
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
            baseUrl: this.$config ? this.$config.baseUrl : 'http://localhost:8080/springbootn3op2l20/', // Ensure this matches your backend URL
            detail: {},
            detailBanner: [],
            isStoreup: false,
            isCrazilynum: false,
            isThumbsupnum: false,
            storeupParams: {},
            thumbsupOrCrazilyInfo: {},
            id: '', // Course ID (from route query)

            courseVideos: [], // Stores list of video chapters from coursevideos table
            currentSessionId: null, // ID of the currently playing video chapter
            currentVideoUrl: '', // URL of the currently playing video chapter
            currentVideoTitle: '', // Title of the currently playing video chapter
            userId: null, // Current user ID (fetched from backend)

            contentpdfurl: '',
        };
    },
    async created() {
        this.id = this.$route.query.id;
        console.log("获取到的课程ID:", this.id);
        this.getDetail()
        // Fetch User ID first
        try {
            const userRes = await this.$http.get('yonghu/currentUserId'); // Adjust URL based on your user controller path
            if (userRes.data && userRes.data.code === 0) {
                this.userId = userRes.data.userId;
                console.log("从后端获取到的用户ID:", this.userId);
            } else {
                console.warn("获取用户ID失败:", userRes.data.msg || '用户未登录或会话过期');
                // Optionally redirect to login or show a message
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

                    // 4. Fetch storeup and thumbsup status if user is logged in
                    if (this.userId) {
                        this.getStoreupStatus();
                        this.getThumbsupOrCrazilyStatus();
                    }

                } else {
                    console.error("获取课程详情失败:", res.data.msg || '未知错误');
                }
            } catch (error) {
                console.error("请求课程详情接口出错:", error);
            }


        },

        async getCourseVideos() {
            try {
                const videosRes = await this.$http.get('coursevideos/listByCourseId', { params: { courseId: this.id } });
                if (videosRes.data && videosRes.data.code === 0 && videosRes.data.data) {
                    this.courseVideos = videosRes.data.data;
                    console.log("获取到的课程视频章节:", this.courseVideos);
                } else {
                    console.warn("获取课程视频章节失败:", videosRes.data.msg || "未知错误");
                    this.courseVideos = [];
                }
            } catch (error) {
                console.error("请求课程视频列表接口出错:", error);
                this.courseVideos = [];
            }
        },

        async findLastViewedVideoId() {
            if (!this.userId || this.courseVideos.length === 0) return null;

            // Iterate through videos to find if any has saved progress
            for (const video of this.courseVideos) {
                try {
                    const progressRes = await this.$http.get('courseprogress/getOne', {
                        params: {
                            courseId: this.id,
                            videoId: video.id
                        }
                    });
                    if (progressRes.data && progressRes.data.code === 0 && progressRes.data.data && progressRes.data.data.progressSeconds > 0) {
                        return video.id; // Return the ID of the first video with progress
                    }
                } catch (error) {
                    console.error(`检查视频 ${video.id} 进度时出错:`, error);
                }
            }
            return null; // No video with saved progress found
        },

        // Play selected video (for multi-chapter courses)
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

                console.log(`正在下载文档: ${documentFileName}`);
            } else {
                this.$message.warning('没有可下载的文档。');
            }
        },

        // Video playback events
        onTimeUpdate() {
            if (this.userId && this.currentSessionId && this.$refs.videoPlayer) {
                // Save progress every 5 seconds (or adjust frequency)
                if (Math.floor(this.$refs.videoPlayer.currentTime) % 5 === 0 && Math.floor(this.$refs.videoPlayer.currentTime) !== 0) {
                    this.saveProgress(this.currentSessionId, this.$refs.videoPlayer.currentTime, this.$refs.videoPlayer.duration);
                }
            }
        },
        onVideoEnded() {
            if (this.userId && this.currentSessionId && this.$refs.videoPlayer) {
                // Save progress at 0 (or total duration) when video ends
                this.saveProgress(this.currentSessionId, this.$refs.videoPlayer.duration || 0, this.$refs.videoPlayer.duration);
            }
        },

        // Save video playback progress to backend
        async saveProgress(videoId, currentTime, totalDuration) {
            if (!this.userId || !videoId || !this.$refs.videoPlayer) {
                return;
            }

            const progressData = {
                userId: this.userId,
                courseId: Number(this.id), // Ensure it's Number type
                videoId: Number(videoId), // Use the actual video ID for tracking
                progressSeconds: currentTime,
                totalSeconds: totalDuration // Can send total duration if you want to store it
            };

            try {
                const res = await this.$http.post('courseprogress/saveOrUpdate', progressData);
                if (res.data.code === 0) {
                    // console.log("学习进度已保存:", currentTime);
                } else {
                    console.error("保存学习进度失败:", res.data.msg);
                }
            } catch (error) {
                console.error("请求保存进度接口出错:", error);
            }
        },

        // Restore video playback progress from backend
        async restoreProgress(videoId) {
            if (!this.userId || !videoId || !this.$refs.videoPlayer) {
                return;
            }
            try {
                const res = await this.$http.get('courseprogress/getOne', {
                    params: {
                        courseId: this.id,
                        videoId: videoId // Request specific video progress
                    }
                });
                const data = res.data;
                if (data.code === 0 && data.data && data.data.progressSeconds > 0) {
                    this.$refs.videoPlayer.currentTime = parseFloat(data.data.progressSeconds);
                    console.log(`恢复进度: 用户 ${this.userId}, 课程 ${this.id}, 视频 ${videoId}, 时间 ${data.data.progressSeconds}`);
                } else {
                    this.$refs.videoPlayer.currentTime = 0; // No progress found, start from beginning
                }
            } catch (error) {
                console.error("请求获取进度接口出错:", error);
                this.$refs.videoPlayer.currentTime = 0; // Fallback to start from beginning on error
            }
        },

        // ... (Keep all your other existing methods: onAcross, storeup, getStoreupStatus, thumbsupOrCrazily, cancelThumbsupOrCrazily, getThumbsupOrCrazilyStatus, backClick, download, btnAuth, editClick, delClick)
        onAcross(tableName, crossOptAudit, compareKey, compareValue, id) {
            if (this.userId) { // 确保用户ID存在
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
                if (status == 1) {
                    this.isStoreup = true;
                } else {
                    this.isStoreup = false;
                }
            } else {
                this.$message.error(res.data.msg || '操作失败');
            }
        },
        async getStoreupStatus() {
            if (!this.userId) return;
            let params = {
                userid: this.userId,
                refid: this.id,
                tablename: 'course',
                type: 1
            }
            let res = await this.$http.get('storeup/list', { params: params });
            if (res.data.code == 0 && res.data.data.list && res.data.data.list.length > 0) {
                this.isStoreup = true;
            } else {
                this.isStoreup = false;
            }
        },
        async thumbsupOrCrazily(type) {
            if (!this.userId) {
                this.$message.error('请先登录后操作');
                return
            }
            this.thumbsupOrCrazilyInfo.userid = this.userId;
            this.thumbsupOrCrazilyInfo.refid = this.id;
            this.thumbsupOrCrazilyInfo.type = type;
            this.thumbsupOrCrazilyInfo.tablename = this.tablename;
            this.thumbsupOrCrazilyInfo.name = this.detail.kechengmingcheng;
            this.thumbsupOrCrazilyInfo.picture = this.detail.kechengtupian;

            let res = await this.$http.post('storeup/add', this.thumbsupOrCrazilyInfo);
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
            if (resThumbsup.data.code == 0 && resThumbsup.data.data.list && resThumbsup.data.data.list.length > 0) {
                this.isThumbsupnum = true;
                this.thumbsupOrCrazilyInfo = resThumbsup.data.data.list[0];
            } else {
                this.isThumbsupnum = false;
            }

            let resCrazily = await this.$http.get('storeup/list', { params: { ...params, type: 22 } });
            if (resCrazily.data.code == 0 && resCrazily.data.data.list && resCrazily.data.data.list.length > 0) {
                this.isCrazilynum = true;
                this.thumbsupOrCrazilyInfo = resCrazily.data.data.list[0];
            } else {
                this.isCrazilynum = false;
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
                if (item.tablename == tablename && item.buttonname == key) {
                    return true;
                } else return false;
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
            if (this.detail.contentpdfurl) {
                this.contentpdfurl = this.detail.contentpdfurl;
            }
        },
    }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
/* Your existing styles */
</style>