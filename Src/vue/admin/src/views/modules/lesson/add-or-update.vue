<template>
  <el-dialog
      :title="!dataForm.id ? '新增课时' : '修改课时'"
      :close-on-click-modal="false"
      :visible.sync="visible"
      width="800px"
  >
    <el-form
        :model="dataForm"
        :rules="dataRule"
        ref="dataForm"
        @keyup.enter.native="dataFormSubmit()"
        label-width="120px"
    >
      <el-input v-model="dataForm.chapterId" type="hidden"></el-input>
      <el-input v-model="dataForm.courseId" type="hidden"></el-input>

      <el-form-item label="课时标题" prop="title">
        <el-input v-model="dataForm.title" placeholder="请输入课时标题"></el-input>
      </el-form-item>

      <el-form-item label="显示顺序" prop="lessonOrder">
        <el-input-number v-model="dataForm.lessonOrder" controls-position="right" :min="0" label="显示顺序"></el-input-number>
      </el-form-item>

      <el-form-item label="课时类型" prop="lessonType">
        <el-radio-group v-model="dataForm.lessonType">
          <el-radio label="video">视频</el-radio>
          <el-radio label="article">图文</el-radio>
        </el-radio-group>
      </el-form-item>

      <template v-if="dataForm.lessonType === 'video'">
        <el-form-item label="上传视频" prop="videoUrl">
          <file-upload
              :type="2"
              tip="点击上传视频文件"
              :action="videoUploadUrl"
              :limit="1"
              @change="videoUploadChange"
              :fileUrls="dataForm.videoUrl ? dataForm.videoUrl:''"
          ></file-upload>
          <div class="el-upload__tip">视频上传可能需要一些时间，请耐心等待。</div>
        </el-form-item>
        <el-form-item label="视频时长(秒)" prop="durationSeconds">
          <el-input-number v-model="dataForm.durationSeconds" controls-position="right" :min="0"></el-input-number>
        </el-form-item>
      </template>

      <template v-if="dataForm.lessonType === 'article'">
        <el-form-item label="图文内容" prop="contentForAi">
          <editor v-model="dataForm.contentForAi" action="file/upload" ></editor>
        </el-form-item>
      </template>

    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      dataForm: {
        id: 0,
        chapterId: null,
        courseId: null, // 从后端获取后设置
        title: "",
        lessonOrder: 0,
        lessonType: "video", // 默认类型
        videoUrl: "",
        durationSeconds: 0,
        contentForAi: "",
        pdfUrl: "" // 如果有PDF上传需求，保留此字段
      },
      dataRule: {
        title: [
          { required: true, message: "课时标题不能为空", trigger: "blur" }
        ],
        lessonOrder: [
          { required: true, message: "显示顺序不能为空", trigger: "blur" }
        ],
        lessonType: [
          { required: true, message: "请选择课时类型", trigger: "change" }
        ]
      },
      videoUploadUrl: '', // 动态构建上传URL
    };
  },
  methods: {
    init(id, chapterId) {
      this.dataForm.id = id || 0;
      this.visible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].resetFields();
        this.dataForm.chapterId = chapterId;

        // 如果是修改，则获取课时详情
        if (this.dataForm.id) {
          // 动态设置视频上传URL
          this.videoUploadUrl = `api/upload/lesson/video?lessonId=${this.dataForm.id}`;
          this.$http({
            url: `lessons/info/${this.dataForm.id}`,
            method: "get"
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm = data.data;
            }
          });
        } else {
          // 新增时，暂时不设置上传URL，需要先保存课时获取ID
          this.videoUploadUrl = '';
        }
      });
    },
    // 处理视频上传
    videoUploadChange(fileUrl) {
      this.dataForm.videoUrl = fileUrl;
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs["dataForm"].validate(valid => {
        if (valid) {

          //【核心逻辑】区分新增和修改
          if(!this.dataForm.id){
            // 1. 新增：先保存课时基本信息（不含视频），获取到课时ID
            let lessonData = {...this.dataForm};
            delete lessonData.videoUrl; // 暂时移除videoUrl

            this.$http({
              url: `lessons/add`, // 使用 /add 接口
              method: 'post',
              data: lessonData
            }).then(({data}) => {
              if (data && data.code === 0) {
                // 后端应该在保存后返回新创建的课时ID
                const newLessonId = data.lessonId; // 假设后端返回了 lessonId
                if (this.dataForm.lessonType === 'video' && this.$children.find(comp => comp.$refs.upload).$refs.upload.uploadFiles.length > 0) {
                  // 2. 如果是视频类型且有文件，使用新ID去触发视频上传
                  this.dataForm.id = newLessonId; // 更新表单的ID
                  this.videoUploadUrl = `api/upload/lesson/video?lessonId=${newLessonId}`;
                  // 触发表单上传
                  this.$nextTick(()=>{
                    const fileComponent = this.$children.find(comp => comp.$refs.upload);
                    fileComponent.$refs.upload.submit();
                  });
                }
                this.$message({
                  message: "操作成功",
                  type: "success",
                  duration: 1500,
                  onClose: () => {
                    this.visible = false;
                    this.$emit("refreshDataList");
                  }
                });
              } else {
                this.$message.error(data.msg);
              }
            });
          } else {
            // 2. 修改：直接提交所有数据
            this.$http({
              url: `lessons/update`,
              method: 'post',
              data: this.dataForm
            }).then(({ data }) => {
              if (data && data.code === 0) {
                this.$message({
                  message: "操作成功",
                  type: "success",
                  duration: 1500,
                  onClose: () => {
                    this.visible = false;
                    this.$emit("refreshDataList");
                  }
                });
              } else {
                this.$message.error(data.msg);
              }
            });
          }
        }
      });
    }
  }
};
</script>