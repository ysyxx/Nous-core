<template>
  <div>
    <div class="container" :style='{"minHeight":"100vh","alignItems":"center","background":"url(http://codegen.caihongy.cn/20230930/02567f8bbf29475095a68adeb32aa995.jpg)","display":"flex","width":"100%","backgroundSize":"100% 100%","backgroundPosition":"center center","backgroundRepeat":"no-repeat","justifyContent":"center"}'>
      <el-form :model="rulesForm" :rules="rules" ref="loginForm" :style='{"border":"0px solid #f6f6f6","minHeight":"620px","padding":"30px 5% 30px 45%","margin":"50px auto 50px","borderRadius":"0px","textAlign":"center","flexWrap":"wrap","background":"url(http://codegen.caihongy.cn/20230930/f52d5a109d7248d69a33749b059f901d.jpg) no-repeat left center / 50% 101%,#fff","display":"flex","width":"75%","fontSize":"14px","height":"auto"}'>
        <div v-if="true" :style='{"padding":"0px","margin":"0px auto 10px","borderColor":"#ddd","color":"#57759b","textAlign":"center","display":"inline-block","background":"none","borderWidth":"0px","width":"100%","lineHeight":"40px","fontSize":"24px","borderStyle":"solid","fontWeight":"600"}' class="title-container">智核NousCore - 管理员登录</div>

        <div class="list-item" :style='{"width":"100%","margin":"0 auto 20px","position":"relative","alignItems":"center","flexWrap":"wrap","display":"flex"}'>
          <div v-if="true" class="lable" :style='{"color":"#666","textAlign":"right","left":"-150px","width":"150px","lineHeight":"44px","fontSize":"inherit","position":"absolute"}'>用户名：</div>
          <input :style='{"padding":"0 10px","boxShadow":"0 0 0px rgba(64, 158, 255, .3)","borderColor":"#62779c","color":"#666","outline":"0px solid #eee","outlineOffset":"0px","borderRadius":"0px","background":"#fff","borderWidth":"0 0 2px","width":"100%","fontSize":"inherit","borderStyle":"solid","height":"50px"}' placeholder="请输入用户名" name="username" type="text" v-model="rulesForm.username">
        </div>
        <div class="list-item" :style='{"width":"100%","margin":"0 auto 20px","position":"relative","alignItems":"center","flexWrap":"wrap","display":"flex"}'>
          <div v-if="true" class="lable" :style='{"color":"#666","textAlign":"right","left":"-150px","width":"150px","lineHeight":"44px","fontSize":"inherit","position":"absolute"}'>密码：</div>
          <input :style='{"padding":"0 10px","boxShadow":"0 0 0px rgba(64, 158, 255, .3)","borderColor":"#62779c","color":"#666","outline":"0px solid #eee","outlineOffset":"0px","borderRadius":"0px","background":"#fff","borderWidth":"0 0 2px","width":"100%","fontSize":"inherit","borderStyle":"solid","height":"50px"}' placeholder="请输入密码" name="password" type="password" v-model="rulesForm.password">
        </div>

        <div :style='{"margin":"40px auto 0","alignItems":"center","flexWrap":"wrap","display":"flex","width":"100%","fontSize":"16px","position":"relative","justifyContent":"flex-start"}'>
          <el-button :style='{"border":"0","cursor":"pointer","padding":"0px","margin":"0 auto 12px","color":"#fff","textAlign":"center","outline":"none","borderRadius":"0px","background":"#62779c","width":"100%","fontSize":"16px","fontWeight":"600","height":"50px","order":"2"}' type="primary" @click="login()" class="loginInBt">登录</el-button>
        </div>
      </el-form>

    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // 【核心修正点】data中的数据模型名称与模板 v-model="rulesForm.xxx" 保持完全一致
      rulesForm: {
        username: "",
        password: "",
      },
      rules: {
        username: [{ required: true, message: "请输入账号", trigger: "blur" }],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
      },
      tableName: 'users'
    };
  },
  methods: {
    login() {
      //【核心修正点】确保验证的是与模板绑定的 this.$refs.loginForm
      this.$refs.loginForm.validate((valid) => {
        // 在你的Vue方法中
        if (valid) {
          // 1. 创建一个 URLSearchParams 对象
          const params = new URLSearchParams();
          params.append('username', this.rulesForm.username);
          params.append('password', this.rulesForm.password);

          this.$http({
            url: `${this.tableName}/login`,
            method: "post",
            // 2. 将 params 对象作为 data 发送
            data: params,
            // 3. (可选但推荐) 明确设置请求头
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded'
            }
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.$storage.set("Token", data.token);
              this.$storage.set("role", "管理员");
              this.$storage.set("sessionTable", this.tableName);
              this.$router.replace({ name: "home" });
            } else {
              this.$message.error(data.msg);
            }
          });
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
/* 您的样式保持不变 */
.container {
  min-height: 100vh;
  position: relative;
  background-repeat: no-repeat;
  background-position: center center;
  background-size: cover;
  background: url(http://codegen.caihongy.cn/20230930/02567f8bbf29475095a68adeb32aa995.jpg);

  .list-item /deep/ .el-input .el-input__inner {
    padding: 0 10px;
    color: #666;
    font-size: inherit;
    border-color: #62779c;
    outline-offset: 0px;
    border-radius: 0px;
    box-shadow: 0 0 0px rgba(64, 158, 255, .3);
    outline: 0px solid #eee;
    background: #fff;
    width: 100%;
    border-width: 0 0 2px;
    border-style: solid;
    height: 50px;
  }
}
</style>