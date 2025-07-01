<template>
  <el-dialog
      :title="!dataForm.id ? '新增章节' : '修改章节'"
      :close-on-click-modal="false"
      :visible.sync="visible"
      @close="onDialogClose"
  >
    <el-form
        :model="dataForm"
        :rules="dataRule"
        ref="dataForm"
        @keyup.enter.native="dataFormSubmit()"
        label-width="120px"
    >
      <el-input v-model="dataForm.courseId" type="hidden"></el-input>

      <el-form-item label="章节标题" prop="title">
        <el-input v-model="dataForm.title" placeholder="请输入章节标题"></el-input>
      </el-form-item>

      <el-form-item label="显示顺序" prop="chapterOrder">
        <el-input-number v-model="dataForm.chapterOrder" controls-position="right" :min="0" label="显示顺序"></el-input-number>
      </el-form-item>

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
      // 数据模型与 ChapterEntity 对应
      dataForm: {
        id: 0,
        courseId: null,
        title: "",
        chapterOrder: 0,
      },
      dataRule: {
        title: [
          { required: true, message: "章节标题不能为空", trigger: "blur" }
        ],
        chapterOrder: [
          { required: true, message: "显示顺序不能为空", trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    init(id, courseId) {
      this.dataForm.id = id || 0;
      this.visible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].resetFields();
        this.dataForm.courseId = courseId; // 【核心】接收并设置 courseId

        if (this.dataForm.id) {
          this.$http({
            // 【注意】这里需要后端提供一个/chapter/info/{id}接口
            url: `chapter/info/${this.dataForm.id}`,
            method: "get"
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm = data.data;
            }
          });
        }
      });
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs["dataForm"].validate(valid => {
        if (valid) {
          this.$http({
            url: `chapter/${!this.dataForm.id ? "save" : "update"}`,
            method: "post",
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
      });
    },
           onDialogClose() {
    this.$emit("dialogClosed"); 
  },
  }
};
</script>