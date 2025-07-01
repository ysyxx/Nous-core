<template>
  <el-dialog
      :title="!dataForm.id ? '新增用户' : '修改用户'"
      :close-on-click-modal="false"
      :visible.sync="visible"
  >
    <el-form
        :model="dataForm"
        :rules="dataRule"
        ref="dataForm"
        @keyup.enter.native="dataFormSubmit()"
        label-width="120px"
    >
      <el-form-item label="用户账号" prop="yonghuzhanghao">
        <el-input v-model="dataForm.yonghuzhanghao" placeholder="用户账号"></el-input>
      </el-form-item>

      <el-form-item label="密码" prop="mima">
        <el-input v-model="dataForm.mima" placeholder="密码" type="password"></el-input>
      </el-form-item>

      <el-form-item label="用户姓名" prop="yonghuxingming">
        <el-input v-model="dataForm.yonghuxingming" placeholder="用户姓名"></el-input>
      </el-form-item>

      <el-form-item label="头像" prop="touxiang">
        <file-upload
            tip="点击上传头像"
            action="file/upload"
            :limit="1"
            @change="touxiangUploadChange"
            :fileUrls="dataForm.touxiang ? dataForm.touxiang:''"
        ></file-upload>
      </el-form-item>

      <el-form-item label="性别" prop="xingbie">
        <el-radio-group v-model="dataForm.xingbie">
          <el-radio label="男">男</el-radio>
          <el-radio label="女">女</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="电话" prop="dianhua">
        <el-input v-model="dataForm.dianhua" placeholder="电话"></el-input>
      </el-form-item>

      <el-form-item label="邮箱" prop="youxiang">
        <el-input v-model="dataForm.youxiang" placeholder="邮箱"></el-input>
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
      // 【修改点】数据模型与 yonghu 表对应
      dataForm: {
        id: 0,
        yonghuzhanghao: "",
        mima: "",
        yonghuxingming: "",
        touxiang: "",
        xingbie: "",
        dianhua: "",
        youxiang: ""
      },
      // 【修改点】更新验证规则
      dataRule: {
        yonghuzhanghao: [
          { required: true, message: "用户账号不能为空", trigger: "blur" }
        ],
        mima: [
          { required: true, message: "密码不能为空", trigger: "blur" }
        ],
        yonghuxingming: [
          { required: true, message: "用户姓名不能为空", trigger: "blur" }
        ],
      }
    };
  },
  methods: {
    init(id) {
      this.dataForm.id = id || 0;
      this.visible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].resetFields();
        if (this.dataForm.id) {
          this.$http({
            url: `yonghu/info/${this.dataForm.id}`,
            method: "get"
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm = data.data;
            }
          });
        }
      });
    },
    // 处理头像上传
    touxiangUploadChange(fileUrl) {
      this.dataForm.touxiang = fileUrl;
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs["dataForm"].validate(valid => {
        if (valid) {
          this.$http({
            url: `yonghu/${!this.dataForm.id ? "save" : "update"}`,
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
    }
  }
};
</script>