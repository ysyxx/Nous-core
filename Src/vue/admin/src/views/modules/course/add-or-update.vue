<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
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
      <el-form-item label="课程名称" prop="title">
        <el-input v-model="dataForm.title" placeholder="课程名称"></el-input>
      </el-form-item>

      <el-form-item label="课程类型" prop="kechengleixing">
        <el-select v-model="dataForm.kechengleixing" placeholder="请选择课程类型">
          <el-option
            v-for="item in courseTypeOptions"
            :key="item.id"
            :label="item.courseType"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="课程封面" prop="coverImageUrl">
        <file-upload
          tip="点击上传课程封面"
          action=""
          :auto-upload="false"
          :limit="1"
          @change="coverImageUploadChange"
        ></file-upload>
      </el-form-item>

      <el-form-item label="课程介绍" prop="description">
        <editor v-model="dataForm.description" action="" ></editor>
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
      // 【修改点】表单数据模型与 CourseEntity 对应
      dataForm: {
        title: "",
        kechengleixing: "",
        description: "",
      },

      selectedFile: null,


      // 【修改点】表单验证规则
      dataRule: {
        title: [
          { required: true, message: "课程名称不能为空", trigger: "blur" }
        ],
        kechengleixing: [
          { required: true, message: "课程类型不能为空", trigger: "blur" }
        ]
      },
      courseTypeOptions: [], // 存储课程类型下拉选项
    };
  },
  methods: {
    init(id) {
      this.dataForm.id = id || 0;
      this.visible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].resetFields();
        this.getCourseTypes(); // 初始化时获取课程类型
        if (this.dataForm.id) {
          this.$http({
            url: `course/info/${this.dataForm.id}`,
            method: "get"
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm = data.data;
            }
          });
        }
      });
    },
    // 获取课程类型
    getCourseTypes() {
        this.$http({
            url: `courseType/page`, // 【注意】这里的接口是复用旧的 option 接口
            method: "get"
        }).then(({data}) => {
            if(data && data.code === 0){
                this.courseTypeOptions = data.data.list;
                console.log("课程类型选项：", this.courseTypeOptions);
            }
        })
    },
    // 【新增】处理封面图片上传
    coverImageUploadChange(file) {
        this.selectedFile = file;
    },
    // 表单提交
    dataFormSubmit() {
    // 验证表单数据
    this.$refs.dataForm.validate((valid) => { // 假设你的el-form有一个ref="dataForm"
      if (valid) {
        if (!this.selectedFile && !this.dataForm.coverImageUrl) { // 确保文件或URL至少存在一个
          this.$message.error('请上传课程封面！');
          return;
        }

        this.dataListLoading = true; // 显示加载状态

        // 1. 将 dataForm 转换为 JSON 字符串
        const courseJsonString = JSON.stringify(this.dataForm);

        // 2. 创建 FormData 对象
        const formData = new FormData();
        formData.append('courseData', courseJsonString); // 添加 JSON 字符串
        if (this.selectedFile) {
            formData.append('file', this.selectedFile); // 添加 File 对象
            console.log("添加的封面图片：", this.selectedFile);
        }


        this.$http({
          url: `course/${this.dataForm.id ? 'update' : 'save'}`, // 根据 id 判断是新增还是修改
          method: 'post',
          data: formData, // 直接传入 FormData 对象
          headers: {
            'Content-Type': 'multipart/form-data' // 浏览器会自动设置正确的 boundary
          }
        }).then(({ data }) => {
          this.dataListLoading = false;
          if (data && data.code === 0) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                // 操作成功后，通常会返回列表页或刷新列表
                this.addOrUpdateFlag = false; // 隐藏新增/修改组件
                this.showFlag = true; // 显示列表页
                this.$emit('refreshDataList'); // 通知父组件刷新数据
                this.visible = false;
              }
            });
          } else {
            this.$message.error(data.msg || '操作失败');
          }
        }).catch(error => {
          this.dataListLoading = false;
          console.error("课程保存/更新接口错误:", error);
          this.$message.error('请求失败，请稍后再试');
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