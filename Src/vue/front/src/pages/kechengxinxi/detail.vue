<template>
  <div v-if="this.detail.id">
    <div class="detail-container">
      <el-row :gutter="20">
        <el-col :sm="24" :md="8">
          <img :src="detail.coverImageUrl" alt="课程封面" class="course-cover-image">
        </el-col>
        <el-col :sm="24" :md="16">
          <h1 class="course-title">{{ detail.title }}</h1>
          <p class="course-description">{{ detail.description }}</p>
          <div class="course-meta">
            <span>课程类型: {{ detail.kechengleixing }}</span>
          </div>
        </el-col>
      </el-row>
    </div>

    <div class="content-container">
      <el-card>
        <div slot="header" class="clearfix">
          <span>课程大纲</span>
        </div>

        <el-collapse v-if="detail.chapters && detail.chapters.length > 0" v-model="activeChapterName" accordion>
          <el-collapse-item
              v-for="chapter in detail.chapters"
              :key="chapter.id"
              :title="chapter.title"
              :name="chapter.id">

            <ul class="lesson-list">
              <li
                  v-for="lesson in chapter.lessons"
                  :key="lesson.id"
                  @click="handleLessonClick(lesson)"
                  class="lesson-item">

                <i :class="getLessonIcon(lesson.lessonType)" class="lesson-icon"></i>
                <span class="lesson-title">{{ lesson.title }}</span>
                <span class="lesson-duration">{{ formatDuration(lesson.durationSeconds) }}</span>
                <i v-if="isLessonCompleted(lesson.id)" class="el-icon-circle-check completed-icon"></i>

              </li>
            </ul>
          </el-collapse-item>
        </el-collapse>
        <div v-else class="no-content">
          <p>该课程暂无章节内容。</p>
        </div>
      </el-card>
    </div>

    <el-dialog
        :title="currentLesson.title"
        :visible.sync="videoDialogVisible"
        width="60%"
        @close="closeVideoPlayer">
      <video v-if="videoDialogVisible" :src="currentLesson.videoUrl" controls autoplay style="width: 100%;"></video>
    </el-dialog>

  </div>
</template>

<script>
export default {
  data() {
    return {
      id: '',
      detail: {}, // 存放从后端获取的完整课程对象
      activeChapterName: null, // 当前展开的章节ID
      videoDialogVisible: false, // 控制视频播放对话框的显示
      currentLesson: {}, // 当前正在播放的课时
      // a-t-todo: 存放当前用户的所有学习进度
      learningProgress: [],
    }
  },
  methods: {
    // 获取课程完整详情
    getInfo() {
      // 【核心修改点】调用新的后端接口
      this.$http.get('courses/detail/' + this.id).then(res => {
        if (res.data.code == 0) {
          this.detail = res.data.data;
          // 默认展开第一个章节
          if (this.detail.chapters && this.detail.chapters.length > 0) {
            this.activeChapterName = this.detail.chapters[0].id;
          }
          // a-t-todo: 同时获取该用户的学习进度
          this.fetchLearningProgress();
        }
      });
    },
    // a-t-todo: 获取学习进度的方法
    fetchLearningProgress() {
      this.$http.get('learning-progress/my-progress').then(res => {
        if (res.data.code == 0) {
          this.learningProgress = res.data.data;
        }
      });
    },
    // a-t-todo: 判断课时是否已完成
    isLessonCompleted(lessonId) {
      const progress = this.learningProgress.find(p => p.lessonId === lessonId);
      return progress ? progress.isCompleted : false;
    },
    // 根据课时类型返回不同的图标
    getLessonIcon(type) {
      if (type === 'video') return 'el-icon-video-play';
      if (type === 'article') return 'el-icon-document';
      return 'el-icon-collection-tag';
    },
    // 格式化时长显示
    formatDuration(seconds) {
      if (!seconds) return '';
      const minutes = Math.floor(seconds / 60);
      const remainingSeconds = seconds % 60;
      return `${minutes}:${remainingSeconds < 10 ? '0' : ''}${remainingSeconds}`;
    },
    // 点击课时后的处理逻辑
    handleLessonClick(lesson) {
      if (lesson.lessonType === 'video' && lesson.videoUrl) {
        this.currentLesson = lesson;
        this.videoDialogVisible = true;
        // a-t-todo: 在这里可以开始上报学习进度
      } else if (lesson.lessonType === 'article') {
        // 对于文章，可以使用 $alert 或另一个对话框来展示内容
        this.$alert(`<div style="white-space: pre-wrap;">${lesson.contentForAi}</div>`, lesson.title, {
          dangerouslyUseHTMLString: true,
          center: true
        });
      }
    },
    // 关闭视频播放器时停止视频播放
    closeVideoPlayer() {
      this.currentLesson = {};
      // 如果使用专业的视频播放器组件，这里需要调用其暂停或销毁的方法
    }
  },
  created() {
    this.id = this.$route.query.id;
    this.getInfo();
  }
}
</script>

<style lang="scss" scoped>
/* 添加一些样式让页面更好看 */
.detail-container {
  padding: 20px;
  background-color: #fff;
  margin-bottom: 20px;
}
.course-cover-image {
  width: 100%;
  height: auto;
  border-radius: 8px;
}
.course-title {
  font-size: 28px;
  margin-top: 0;
}
.course-description {
  color: #606266;
  line-height: 1.6;
}
.course-meta {
  margin-top: 20px;
  color: #909399;
}
.content-container {
  padding: 20px;
}
.lesson-list {
  list-style-type: none;
  padding-left: 20px;
}
.lesson-item {
  padding: 10px 0;
  cursor: pointer;
  border-bottom: 1px solid #EBEEF5;
  transition: background-color 0.3s;
}
.lesson-item:hover {
  background-color: #f5f7fa;
}
.lesson-icon {
  margin-right: 10px;
  color: #409EFF;
}
.lesson-duration {
  float: right;
  color: #909399;
}
.completed-icon {
  margin-left: 10px;
  color: #67C23A;
  font-size: 18px;
}
.no-content {
  text-align: center;
  color: #909399;
  padding: 40px 0;
}
</style>