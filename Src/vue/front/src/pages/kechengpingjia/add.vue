<template>
  <div class="form-container">
    <el-form class="form-preview" ref="ruleForm" :model="ruleForm" :rules="rules" label-width="180px">
      <div class="form-content">
        <!-- 课程信息部分 -->
        <div class="form-section">
          <el-form-item label="课程名称" prop="kechengmingcheng">
            <el-input v-model="ruleForm.kechengmingcheng" placeholder="课程名称" clearable
              :disabled="false || ro.kechengmingcheng"></el-input>
          </el-form-item>

          <el-form-item label="课程类型" prop="kechengleixing">
            <el-input v-model="ruleForm.kechengleixing" placeholder="课程类型" clearable
              :disabled="false || ro.kechengleixing"></el-input>
          </el-form-item>

          <el-form-item label="课程图片" v-if="type != 'cross' || (type == 'cross' && !ro.kechengtupian)" prop="kechengtupian">
            <file-upload tip="点击上传课程图片" action="file/upload" :limit="3" :multiple="true"
              :fileUrls="ruleForm.kechengtupian ? ruleForm.kechengtupian : ''"
              @change="kechengtupianUploadChange"></file-upload>
          </el-form-item>

          <el-form-item class="upload" v-else label="课程图片" prop="kechengtupian">
            <div class="image-preview">
              <img v-if="ruleForm.kechengtupian.substring(0, 4) == 'http'" class="upload-img"
                :src="ruleForm.kechengtupian.split(',')[0]" width="100" height="100">
              <img v-else class="upload-img" v-for="(item, index) in ruleForm.kechengtupian.split(',')"
                :src="baseUrl + item" width="100" height="100" :key="index">
            </div>
          </el-form-item>
        </div>

        <!-- 教学信息部分 -->
        <div class="form-section">
          <el-form-item label="教学目标" prop="jiaoxuemubiao">
            <el-select v-model="ruleForm.jiaoxuemubiao" placeholder="请选择教学目标" :disabled="false || ro.jiaoxuemubiao">
              <el-option v-for="(item, index) in jiaoxuemubiaoOptions" :key="index" :label="item" :value="item">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="教学方法" prop="jiaoxuefangfa">
            <el-select v-model="ruleForm.jiaoxuefangfa" placeholder="请选择教学方法" :disabled="false || ro.jiaoxuefangfa">
              <el-option v-for="(item, index) in jiaoxuefangfaOptions" :key="index" :label="item" :value="item">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="教学内容" prop="jiaoxueneirong">
            <el-select v-model="ruleForm.jiaoxueneirong" placeholder="请选择教学内容" :disabled="false || ro.jiaoxueneirong">
              <el-option v-for="(item, index) in jiaoxueneirongOptions" :key="index" :label="item" :value="item">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="教学效果" prop="jiaoxuexiaoguo">
            <el-select v-model="ruleForm.jiaoxuexiaoguo" placeholder="请选择教学效果" :disabled="false || ro.jiaoxuexiaoguo">
              <el-option v-for="(item, index) in jiaoxuexiaoguoOptions" :key="index" :label="item" :value="item">
              </el-option>
            </el-select>
          </el-form-item>
        </div>

        <!-- 评价信息部分 -->
        <div class="form-section">
          <el-form-item label="评价时间" prop="pingjiashijian">
            <el-date-picker :disabled="true || ro.pingjiashijian" format="yyyy 年 MM 月 dd 日" value-format="yyyy-MM-dd"
              v-model="ruleForm.pingjiashijian" type="date" placeholder="评价时间"></el-date-picker>
          </el-form-item>

          <el-form-item label="用户账号" prop="yonghuzhanghao">
            <el-input v-model="ruleForm.yonghuzhanghao" placeholder="用户账号" clearable
              :disabled="false || ro.yonghuzhanghao"></el-input>
          </el-form-item>

          <el-form-item label="用户姓名" prop="yonghuxingming">
            <el-input v-model="ruleForm.yonghuxingming" placeholder="用户姓名" clearable
              :disabled="false || ro.yonghuxingming"></el-input>
          </el-form-item>

          <el-form-item label="评价建议" prop="pingjiajianyi">
            <el-input type="textarea" :rows="8" placeholder="评价建议" v-model="ruleForm.pingjiajianyi"></el-input>
          </el-form-item>
        </div>
      </div>

      <!-- 表单操作按钮 -->
      <div class="form-actions">
        <el-button class="submit-btn" type="primary" @click="onSubmit">提交</el-button>
        <el-button class="cancel-btn" @click="back()">返回</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
export default {
  data() {
    let self = this
    return {
      id: '',
      baseUrl: '',
      ro: {
        kechengmingcheng: false,
        kechengleixing: false,
        kechengtupian: false,
        jiaoxuemubiao: false,
        jiaoxuefangfa: false,
        jiaoxueneirong: false,
        jiaoxuexiaoguo: false,
        pingjiashijian: false,
        pingjiajianyi: false,
        yonghuzhanghao: false,
        yonghuxingming: false,
        sfsh: false,
        shhf: false,
      },
      type: '',
      userTableName: localStorage.getItem('UserTableName'),
      ruleForm: {
        kechengmingcheng: '',
        kechengleixing: '',
        kechengtupian: '',
        jiaoxuemubiao: '',
        jiaoxuefangfa: '',
        jiaoxueneirong: '',
        jiaoxuexiaoguo: '',
        pingjiashijian: '',
        pingjiajianyi: '',
        yonghuzhanghao: '',
        yonghuxingming: '',
      },
      jiaoxuemubiaoOptions: [],
      jiaoxuefangfaOptions: [],
      jiaoxueneirongOptions: [],
      jiaoxuexiaoguoOptions: [],


      rules: {
        kechengmingcheng: [
        ],
        kechengleixing: [
        ],
        kechengtupian: [
        ],
        jiaoxuemubiao: [
        ],
        jiaoxuefangfa: [
        ],
        jiaoxueneirong: [
        ],
        jiaoxuexiaoguo: [
        ],
        pingjiashijian: [
        ],
        pingjiajianyi: [
        ],
        yonghuzhanghao: [
        ],
        yonghuxingming: [
        ],
        sfsh: [
        ],
        shhf: [
        ],
      },
      centerType: false,
    };
  },
  computed: {



  },
  components: {
  },
  created() {
    if (this.$route.query.centerType) {
      this.centerType = true
    }
    //this.bg();
    let type = this.$route.query.type ? this.$route.query.type : '';
    this.init(type);
    this.baseUrl = this.$config.baseUrl;
    this.ruleForm.pingjiashijian = this.getCurDate()
  },
  methods: {
    getMakeZero(s) {
      return s < 10 ? '0' + s : s;
    },
    // 下载
    download(file) {
      window.open(`${file}`)
    },
    // 初始化
    init(type) {
      this.type = type;
      if (type == 'cross') {
        var obj = JSON.parse(localStorage.getItem('crossObj'));
        for (var o in obj) {
          if (o == 'kechengmingcheng') {
            this.ruleForm.kechengmingcheng = obj[o];
            this.ro.kechengmingcheng = true;
            continue;
          }
          if (o == 'kechengleixing') {
            this.ruleForm.kechengleixing = obj[o];
            this.ro.kechengleixing = true;
            continue;
          }
          if (o == 'kechengtupian') {
            this.ruleForm.kechengtupian = obj[o].split(",")[0];
            this.ro.kechengtupian = true;
            continue;
          }
          if (o == 'jiaoxuemubiao') {
            this.ruleForm.jiaoxuemubiao = obj[o];
            this.ro.jiaoxuemubiao = true;
            continue;
          }
          if (o == 'jiaoxuefangfa') {
            this.ruleForm.jiaoxuefangfa = obj[o];
            this.ro.jiaoxuefangfa = true;
            continue;
          }
          if (o == 'jiaoxueneirong') {
            this.ruleForm.jiaoxueneirong = obj[o];
            this.ro.jiaoxueneirong = true;
            continue;
          }
          if (o == 'jiaoxuexiaoguo') {
            this.ruleForm.jiaoxuexiaoguo = obj[o];
            this.ro.jiaoxuexiaoguo = true;
            continue;
          }
          if (o == 'pingjiashijian') {
            this.ruleForm.pingjiashijian = obj[o];
            this.ro.pingjiashijian = true;
            continue;
          }
          if (o == 'pingjiajianyi') {
            this.ruleForm.pingjiajianyi = obj[o];
            this.ro.pingjiajianyi = true;
            continue;
          }
          if (o == 'yonghuzhanghao') {
            this.ruleForm.yonghuzhanghao = obj[o];
            this.ro.yonghuzhanghao = true;
            continue;
          }
          if (o == 'yonghuxingming') {
            this.ruleForm.yonghuxingming = obj[o];
            this.ro.yonghuxingming = true;
            continue;
          }
        }
      } else if (type == 'edit') {
        this.info()
      }
      // 获取用户信息
      this.$http.get(this.userTableName + '/session', { emulateJSON: true }).then(res => {
        if (res.data.code == 0) {
          var json = res.data.data;
          if ((json.yonghuzhanghao != '' && json.yonghuzhanghao) || json.yonghuzhanghao == 0) {
            this.ruleForm.yonghuzhanghao = json.yonghuzhanghao
          }
          if ((json.yonghuxingming != '' && json.yonghuxingming) || json.yonghuxingming == 0) {
            this.ruleForm.yonghuxingming = json.yonghuxingming
          }
        }
      });
      this.jiaoxuemubiaoOptions = "A,B,C,D".split(',')
      this.jiaoxuefangfaOptions = "A,B,C,D".split(',')
      this.jiaoxueneirongOptions = "A,B,C,D".split(',')
      this.jiaoxuexiaoguoOptions = "A,B,C,D".split(',')

      if (localStorage.getItem('raffleType') && localStorage.getItem('raffleType') != null) {
        localStorage.removeItem('raffleType')
        setTimeout(() => {
          this.onSubmit()
        }, 300)
      }
    },

    // 多级联动参数
    // 多级联动参数
    info() {
      this.$http.get(`courseAssessment/detail/${this.$route.query.id}`, { emulateJSON: true }).then(res => {
        if (res.data.code == 0) {
          this.ruleForm = res.data.data;
        }
      });
    },
    // 提交
    onSubmit() {

      //更新跨表属性
      var crossuserid;
      var crossrefid;
      var crossoptnum;
      this.$refs["ruleForm"].validate(valid => {
        if (valid) {
          if (this.type == 'cross') {
            var statusColumnName = localStorage.getItem('statusColumnName');
            var statusColumnValue = localStorage.getItem('statusColumnValue');
            if (statusColumnName && statusColumnName != '') {
              var obj = JSON.parse(localStorage.getItem('crossObj'));
              if (!statusColumnName.startsWith("[")) {
                for (var o in obj) {
                  if (o == statusColumnName) {
                    obj[o] = statusColumnValue;
                  }
                }
                var table = localStorage.getItem('crossTable');
                this.$http.post(table + '/update', obj).then(res => { });
              } else {
                crossuserid = Number(localStorage.getItem('frontUserid'));
                crossrefid = obj['id'];
                crossoptnum = localStorage.getItem('statusColumnName');
                crossoptnum = crossoptnum.replace(/\[/, "").replace(/\]/, "");
              }
            }
          }
          if (crossrefid && crossuserid) {
            this.ruleForm.crossuserid = crossuserid;
            this.ruleForm.crossrefid = crossrefid;
            var params = {
              page: 1,
              limit: 10,
              crossuserid: crossuserid,
              crossrefid: crossrefid,
            }
            this.$http.get('courseAssessment/list', {
              params: params
            }).then(res => {
              if (res.data.data.total >= crossoptnum) {
                this.$message({
                  message: localStorage.getItem('tips'),
                  type: 'error',
                  duration: 1500,
                });
                return false;
              } else {
                // 跨表计算


                this.$http.post(`courseAssessment/${this.ruleForm.id ? 'update' : this.centerType ? 'save' : 'add'}`, this.ruleForm).then(res => {
                  if (res.data.code == 0) {
                    this.$message({
                      message: '操作成功',
                      type: 'success',
                      duration: 1500,
                      onClose: () => {
                        this.$router.go(-1);
                      }
                    });
                  } else {
                    this.$message({
                      message: res.data.msg,
                      type: 'error',
                      duration: 1500
                    });
                  }
                });
              }
            });
          } else {


            this.$http.post(`courseAssessment/${this.ruleForm.id ? 'update' : this.centerType ? 'save' : 'add'}`, this.ruleForm).then(res => {
              if (res.data.code == 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.$router.go(-1);
                  }
                });
              } else {
                this.$message({
                  message: res.data.msg,
                  type: 'error',
                  duration: 1500
                });
              }
            });
          }
        }
      });
    },
    // 获取uuid
    getUUID() {
      return new Date().getTime();
    },
    // 返回
    back() {
      this.$router.go(-1);
    },
    kechengtupianUploadChange(fileUrls) {
      this.ruleForm.kechengtupian = fileUrls.replace(new RegExp(this.$config.baseUrl, "g"), "");;
    },
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.form-container {
  width: 100%;
  padding: 30px 7% 40px;
  margin: 0 auto;
  position: relative;
  background: #fff;
}

.form-preview {
  border: 1px solid #eee;
  width: 100%;
  padding: 30px;
  position: relative;
  background: #fcfcfc;
}

.form-content {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}

.form-section {
  width: 100%;
  margin-bottom: 20px;

  .el-form-item {
    padding: 10px;
    margin: 0 0 10px;
    background: none;
  }
}

.image-preview {
  display: flex;
  flex-wrap: wrap;

  .upload-img {
    margin-right: 20px;
    object-fit: cover;
    border: 1px solid #eee;
  }
}

.form-actions {
  display: flex;
  justify-content: center;
  padding: 20px 0 0;
  margin-top: 20px;
  border-top: 1px solid #eee;

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

    &:hover {
      opacity: 0.9;
    }
  }

  .cancel-btn {
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

    &:hover {
      opacity: 0.9;
    }
  }
}

@media (min-width: 768px) {
  .form-section {
    width: 48%;
  }
}

.el-date-editor.el-input {
  width: auto;
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

.add-update-preview .el-input-number /deep/ .el-input__inner {
  text-align: left;
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

.add-update-preview .el-input-number /deep/ .el-input-number__decrease {
  display: none;
}

.add-update-preview .el-input-number /deep/ .el-input-number__increase {
  display: none;
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
