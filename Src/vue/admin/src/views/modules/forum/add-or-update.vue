<template>
  <el-dialog
      title="帖子详情与评论"
      :close-on-click-modal="false"
      :visible.sync="visible"
      width="80%"
  >
    <div class="forum-container" v-if="dataForm.id">
      <el-card class="box-card main-post">
        <div slot="header" class="clearfix">
          <h3>{{ dataForm.title }}</h3>
          <span class="post-meta">发帖人: {{ dataForm.username }} | 时间: {{ dataForm.addtime }}</span>
        </div>
        <div class="post-content" v-html="dataForm.content"></div>
      </el-card>

      <el-divider content-position="left">全部评论</el-divider>
      <div class="comment-list">
        <div v-if="!dataForm.childs || dataForm.childs.length === 0" class="no-comments">
          暂无评论
        </div>
        <comment-item v-for="comment in dataForm.childs" :key="comment.id" :comment="comment" @reply-success="refreshComments"></comment-item>
      </div>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">关闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
// 【新增】定义一个内部递归组件来展示评论
const CommentItem = {
  name: 'CommentItem',
  props: ['comment'],
  template: `
    <div class="comment-item">
      <div class="comment-main">
        <div class="comment-header">
          <strong class="comment-author">{{ comment.username }}</strong>
          <span class="comment-time">{{ comment.addtime }}</span>
        </div>
        <div class="comment-body" v-html="comment.content"></div>
      </div>
      <div class="comment-children" v-if="comment.childs && comment.childs.length > 0">
        <comment-item v-for="child in comment.childs" :key="child.id" :comment="child"></comment-item>
      </div>
    </div>
  `
};

export default {
  components: {
    CommentItem
  },
  data() {
    return {
      visible: false,
      dataForm: {},
    };
  },
  methods: {
    init(id) {
      this.visible = true;
      this.dataForm = {};
      if (id) {
        this.fetchPostAndComments(id);
      }
    },
    fetchPostAndComments(id) {
      // 【核心】调用后端特殊接口获取整个帖子树
      this.$http({
        url: `forum/list/${id}`,
        method: "get"
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataForm = data.data;
        } else {
          this.$message.error('加载帖子失败：' + data.msg);
        }
      });
    },
    // 刷新评论（如果未来增加管理员回复功能，会用到）
    refreshComments() {
      if (this.dataForm.id) {
        this.fetchPostAndComments(this.dataForm.id);
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.forum-container {
  max-height: 70vh;
  overflow-y: auto;
}
.main-post {
  margin-bottom: 20px;
}
.post-meta {
  font-size: 14px;
  color: #888;
}
.post-content {
  padding: 15px 0;
}
.comment-list {
  padding-left: 20px;
}
.no-comments {
  text-align: center;
  color: #999;
  padding: 20px;
}
.comment-item {
  border-left: 2px solid #ebebeb;
  padding: 10px 0 10px 20px;
  margin-bottom: 15px;
}
.comment-header {
  margin-bottom: 5px;
}
.comment-author {
  color: #337ab7;
  margin-right: 10px;
}
.comment-time {
  font-size: 12px;
  color: #999;
}
.comment-body {
  font-size: 14px;
}
.comment-children {
  margin-top: 10px;
}
</style>