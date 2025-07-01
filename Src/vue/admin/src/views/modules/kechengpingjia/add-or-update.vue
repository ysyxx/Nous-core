<template>
  <el-dialog
      title="评价详情"
      :close-on-click-modal="false"
      :visible.sync="visible"
      @close="onDialogClose"
  >
    <el-form
        :model="dataForm"
        ref="dataForm"
        label-width="120px"
    >
      <el-form-item label="课程名称：">
        <span>{{ dataForm.kechengmingcheng }}</span>
      </el-form-item>
      <el-form-item label="课程类型：">
        <span>{{ dataForm.kechengleixing }}</span>
      </el-form-item>
      <el-form-item label="课程图片：">
        <el-image
            v-if="dataForm.kechengtupian"
            style="width: 100px; height: 100px"
            :src="$base.url + dataForm.kechengtupian"
            :preview-src-list="[$base.url + dataForm.kechengtupian]"
        ></el-image>
        <span v-else>无</span>
      </el-form-item>
      <el-form-item label="评价用户：">
        <span>{{ dataForm.yonghuzhanghao }} ({{dataForm.yonghuxingming}})</span>
      </el-form-item>
      <el-form-item label="评价时间：">
        <span>{{ dataForm.pingjiashijian }}</span>
      </el-form-item>
      <el-divider content-position="left">评价内容</el-divider>
      <el-form-item label="教学目标：">
        <span>{{ dataForm.jiaoxuemubiao }}</span>
      </el-form-item>
      <el-form-item label="教学方法：">
        <span>{{ dataForm.jiaoxuefangfa }}</span>
      </el-form-item>
      <el-form-item label="教学内容：">
        <span>{{ dataForm.jiaoxueneirong }}</span>
      </el-form-item>
      <el-form-item label="教学效果：">
        <span>{{ dataForm.jiaoxuexiaoguo }}</span>
      </el-form-item>
      <el-form-item label="评价建议：">
        <div v-html="dataForm.pingjiajianyi"></div>
      </el-form-item>
      <el-divider content-position="left">审核信息</el-divider>
      <el-form-item label="审核状态：">
        <el-tag :type="dataForm.sfsh === '是' ? 'success' : (dataForm.sfsh === '否' ? 'danger' : 'warning')">
          {{ dataForm.sfsh === '是' ? '已通过' : (dataForm.sfsh === '否' ? '未通过' : '待审核') }}
        </el-tag>
      </el-form-item>

      <el-form-item label="审核回复：" prop="shhf">
        <el-input
            type="textarea"
            :rows="4"
            placeholder="请输入审核回复"
            v-model="dataForm.shhf">
        </el-input>
      </el-form-item>

    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">关闭</el-button>
      <el-button type="primary" @click="dataFormSubmit()">保存回复</el-button>
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
        kechengmingcheng: "",
        kechengleixing: "",
        kechengtupian: "",
        jiaoxuemubiao: "",
        jiaoxuefangfa: "",
        jiaoxueneirong: "",
        jiaoxuexiaoguo: "",
        pingjiashijian: "",
        pingjiajianyi: "",
        yonghuzhanghao: "",
        yonghuxingming: "",
        sfsh: "",
        shhf: ""
      },
    };
  },
  methods: {
    init(id) {
      this.dataForm.id = id || 0;
      this.visible = true;
      this.$nextTick(() => {
        if (this.dataForm.id) {
          this.$http({
            url: `courseAssessment/info/${this.dataForm.id}`,
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
      this.$http({
        url: `courseAssessment/update`,
        method: "post",
        // 【核心】只提交包含 id 和 shhf 的对象，避免覆盖其他字段
        data: {
          id: this.dataForm.id,
          shhf: this.dataForm.shhf
        }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.$message({
            message: "回复保存成功",
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
    },
       onDialogClose() {
    this.$emit("dialogClosed"); 
  },
  }
};
</script>
<style scoped>
.el-form-item {
  margin-bottom: 10px;
}
</style>