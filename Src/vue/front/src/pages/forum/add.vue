<template>
  <div class="forum-form-container">
    <!-- 标题区域 -->
    <div class="section-title">互动交流</div>

    <!-- 返回按钮 -->
    <div class="back-button-section">
      <el-button size="mini" @click="backClick" icon="el-icon-back" type="warning">返回</el-button>
    </div>

    <!-- 表单区域 -->
    <el-form class="forum-form" ref="form" :model="form" :rules="rules" label-width="180px">

      <!-- 标题输入 -->
      <el-form-item label="标题" prop="title">
        <el-input v-model="form.title" placeholder="请输入标题">
        </el-input>
      </el-form-item>

      <!-- 类型选择 -->
      <el-form-item label="类型" prop="isdone">
        <el-radio-group v-model="form.isdone">
          <el-radio label="开放">公开</el-radio>
          <el-radio label="关闭">私人</el-radio>
        </el-radio-group>
      </el-form-item>

      <!-- 内容编辑器 -->
      <el-form-item label="内容" prop="content">
        <editor class="content-editor" v-model="form.content" action="file/upload">
        </editor>
      </el-form-item>

      <!-- 表单操作按钮 -->
      <el-form-item class="form-actions">
        <el-button class="submit-btn" type="primary" @click="submitForm('form')">
          {{ isEdit ? '修改' : '发布帖子' }}
        </el-button>
        <el-button class="reset-btn" @click="resetForm('form')">
          重置
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  //数据集合
  data() {
    return {
      form: {
        title: '',
        isdone: '开放',
        content: '',
        parentid: 0,
        userid: localStorage.getItem('frontUserid'),
        username: localStorage.getItem('username'),
        toptime: ''
      },
      editorOption: {
        modules: {
          toolbar: [
            ["bold", "italic", "underline", "strike"],
            ["blockquote", "code-block"],
            [{ header: 1 }, { header: 2 }],
            [{ list: "ordered" }, { list: "bullet" }],
            [{ script: "sub" }, { script: "super" }],
            [{ indent: "-1" }, { indent: "+1" }],
            [{ direction: "rtl" }],
            [{ size: ["small", false, "large", "huge"] }],
            [{ header: [1, 2, 3, 4, 5, 6, false] }],
            [{ color: [] }, { background: [] }],
            [{ font: [] }],
            [{ align: [] }],
            ["clean"],
            ["image", "video"]
          ]
        }
      },
      isEdit: false,
      rules: {
        title: [
          { required: true, message: '请输入标题', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    if (this.$route.query.id != undefined) {
      this.isEdit = true;
      this.$http.get('forum/detail/' + this.$route.query.id, {}).then(res => {
        if (res.data.code == 0) {
          this.form = res.data.data
        }
      })
    }
  },
  //方法集合
  methods: {
    // 返回按钮
    backClick() {
      history.back()
    },
    onEditorReady(editor) {
      editor.root.setAttribute('data-placeholder', "请输入内容...");
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (!this.isEdit) {
            this.form.toptime = this.getCurDateTime()
          }
          this.$http.post(`forum/${this.isEdit ? 'update' : 'add'}`, this.form).then(res => {
            if (res.data.code === 0) {
              this.$message({
                message: `${this.isEdit ? '修改' : '发布'}成功`,
                type: 'success',
                duration: 1500,
                onClose: () => {
                  if (this.isEdit) {
                    this.$router.push('/index/myForumList');
                  } else {
                    this.$router.push('/index/forum');
                  }
                }
              });
            } else {
              this.$message.error(res.data.msg);
            }
          });
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.forum-form-container {
  width: 100%;
  padding: 30px 7% 40px;
  margin: 0 auto;
  position: relative;
  background: #fff;
}

/* 标题区域 */
.section-title {
  margin: 10px 0;
  color: #fff;
  text-align: center;
  background: #333;
  width: 100%;
  font-size: 20px;
  line-height: 54px;
}

/* 返回按钮区域 */
.back-button-section {
  padding: 20px 7%;
  margin: 0 auto;
  border-bottom: 1px solid #ddd;
}

/* 表单区域 */
.forum-form {
  margin-top: 20px;

  /* 表单项通用样式 */
  .el-form-item {
    padding: 10px;
    margin: 0 0 10px;
    background: none;
  }

  /* 标题输入框 */
  .el-input {
    width: 100%;
  }

  /* 单选按钮组 */
  .el-radio-group {
    display: flex;
    gap: 20px;
  }

  /* 内容编辑器 */
  .content-editor {
    min-height: 200px;
    border: 0;
    outline: none;
    color: #333;
    border-radius: 4px;
    width: 100%;
    line-height: 32px;
    font-size: 14px;
  }

  /* 表单操作按钮 */
  .form-actions {
    padding: 0;
    margin: 20px 0 0;
  }

  .submit-btn {
    border: 0;
    cursor: pointer;
    padding: 0;
    margin: 0 20px 0 0;
    outline: none;
    color: #fff;
    border-radius: 4px;
    background: #F5BB00;
    width: 128px;
    line-height: 40px;
    font-size: 14px;
    height: 40px;
    transition: all 0.3s;

    &:hover {
      background: darken(#F5BB00, 10%);
    }
  }

  .reset-btn {
    border: 0;
    cursor: pointer;
    padding: 0;
    margin: 0;
    outline: none;
    color: #fff;
    border-radius: 4px;
    background: #333;
    width: 128px;
    line-height: 40px;
    font-size: 14px;
    height: 40px;
    transition: all 0.3s;

    &:hover {
      background: lighten(#333, 10%);
    }
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .forum-form-container {
    padding: 15px 5%;
  }

  .back-button-section {
    padding: 15px 5%;
  }

  .forum-form {
    .el-form-item {
      padding: 8px 0;
    }

    .el-radio-group {
      flex-direction: column;
      gap: 10px;
    }

    .form-actions {
      display: flex;
      flex-direction: column;
      gap: 10px;

      .submit-btn,
      .reset-btn {
        width: 100%;
        margin: 0;
      }
    }
  }
}

.add-update-preview .el-form-item /deep/ .el-form-item__label {
  padding: 0 10px 0 0;
  color: #666;
  font-weight: 500;
  width: 180px;
  font-size: 14px;
  line-height: 40px;
  text-align: right;
}

.add-update-preview .el-form-item /deep/ .el-form-item__content {
  margin-left: 180px;
}

.add-update-preview .el-input /deep/ .el-input__inner {
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 0 12px;
  box-shadow: 0 0 0px rgba(64, 158, 255, .5);
  outline: none;
  color: #666;
  background: #fff;
  width: 400px;
  font-size: 14px;
  height: 40px;
}

.add-update-preview .el-select /deep/ .el-input__inner {
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 0 10px;
  box-shadow: 0 0 0px rgba(64, 158, 255, .5);
  outline: none;
  color: #666;
  background: #fff;
  width: 200px;
  font-size: 14px;
  height: 40px;
}

.add-update-preview .el-date-editor /deep/ .el-input__inner {
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 0 10px 0 30px;
  box-shadow: 0 0 0px rgba(64, 158, 255, .5);
  outline: none;
  color: #666;
  background: #fff;
  width: 200px;
  font-size: 14px;
  height: 40px;
}

.add-update-preview /deep/ .el-upload--picture-card {
  background: transparent;
  border: 0;
  border-radius: 0;
  width: auto;
  height: auto;
  line-height: initial;
  vertical-align: middle;
}

.add-update-preview /deep/ .upload .upload-img {
  border: 1px solid #ddd;
  cursor: pointer;
  border-radius: 6px;
  color: #999;
  background: #fff;
  width: 200px;
  font-size: 32px;
  line-height: 100px;
  text-align: center;
  height: 100px;
}

.add-update-preview /deep/ .el-upload-list .el-upload-list__item {
  border: 1px solid #ddd;
  cursor: pointer;
  border-radius: 6px;
  color: #999;
  background: #fff;
  width: 200px;
  font-size: 32px;
  line-height: 100px;
  text-align: center;
  height: 100px;
}

.add-update-preview /deep/ .el-upload .el-icon-plus {
  border: 1px solid #ddd;
  cursor: pointer;
  border-radius: 6px;
  color: #999;
  background: #fff;
  width: 200px;
  font-size: 32px;
  line-height: 100px;
  text-align: center;
  height: 100px;
}

.add-update-preview .el-textarea /deep/ .el-textarea__inner {
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 12px;
  box-shadow: 0 0 0px rgba(64, 158, 255, .5);
  outline: none;
  color: #666;
  background: #fff;
  width: 400px;
  font-size: 14px;
  height: 120px;
}
</style>
