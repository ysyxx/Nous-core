<template>
<div class="login-page">
    <div class="login-bg"></div>
  <el-form ref="loginForm" :model="loginForm" class="login-form" :rules="rules">
      <div v-if="false" class="hidden-title">USER / LOGIN</div>
      <div v-if="true" class="main-title">智核NousCore登录</div>
      
      <el-form-item v-if="loginType==1" class="form-item" prop="username">
        <div class="input-label">账号：</div>
        <input class="input-field" v-model="loginForm.username" placeholder="请输入账户">
      </el-form-item>
      
      <el-form-item v-if="loginType==1" class="form-item" prop="password">
        <div class="input-label">密码：</div>
        <input class="input-field" v-model="loginForm.password" placeholder="请输入密码" type="password">
      </el-form-item>

      <el-form-item class="role-selector" v-if="roles.length>1">
        <el-select v-model="loginForm.tableName" placeholder="请选择角色" @change="selectChange">
          <el-option v-for="(item,index) in roles" :key="index" :label="item.roleName" :value="item.tableName" />
        </el-select>
      </el-form-item>

      <el-form-item class="button-group">
        <el-button v-if="loginType==1" class="login-btn" @click="submitForm('loginForm')">登录</el-button>
        <el-button v-if="loginType==1" class="reset-btn" @click="resetForm('loginForm')">重置</el-button>
      </el-form-item>
      
      <div class="register-links">
        <router-link class="register-link" 
          :to="{path: '/register', query: {role: item.tableName,pageFlag:'register'}}" 
          v-if="item.hasFrontRegister=='是'" 
          v-for="(item, index) in roles" 
          :key="index">
          注册{{item.roleName.replace('注册','')}}
        </router-link>
      </div>
      
      <div class="idea1"></div>
      <div class="idea2"></div>
    </el-form>
  </div>

</template>

<script>
import menu from '@/config/menu'
export default {
	//数据集合
	data() {
		return {
            baseUrl: this.$config.baseUrl,
            loginType: 1,
			roleMenus: [],
			loginForm: {
				username: '',
				password: '',
				tableName: '',
				code: '',
			},
			role: '',
            roles: [],
			rules: {
				username: [
					{ required: true, message: '请输入账户', trigger: 'blur' }
				],
				password: [
					{ required: true, message: '请输入密码', trigger: 'blur' }
				]
			},
			codes: [{
				num: 1,
				color: '#000',
				rotate: '10deg',
				size: '16px'
			}, {
				num: 2,
				color: '#000',
				rotate: '10deg',
				size: '16px'
			}, {
				num: 3,
				color: '#000',
				rotate: '10deg',
				size: '16px'
			}, {
				num: 4,
				color: '#000',
				rotate: '10deg',
				size: '16px'
			}],
			flag: false,
			verifyCheck2: false,
		}
	},
  components: {
  },
	created() {
		this.roleMenus = menu.list()
		for(let item in this.roleMenus) {
		    if(this.roleMenus[item].hasFrontLogin=='是') {
		        this.roles.push(this.roleMenus[item]);
		    }
		}
		
	},
	mounted() {
	},
    //方法集合
    methods: {
		randomString() {
			var len = 4;
			var chars = [
			  'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
			  'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
			  'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
			  'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
			  'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2',
			  '3', '4', '5', '6', '7', '8', '9'
			]
			var colors = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f']
			var sizes = ['14', '15', '16', '17', '18']
			
			var output = []
			for (var i = 0; i < len; i++) {
			  // 随机验证码
			  var key = Math.floor(Math.random() * chars.length)
			  this.codes[i].num = chars[key]
			  // 随机验证码颜色
			  var code = '#'
			  for (var j = 0; j < 6; j++) {
			    var key = Math.floor(Math.random() * colors.length)
			    code += colors[key]
			  }
			  this.codes[i].color = code
			  // 随机验证码方向
			  var rotate = Math.floor(Math.random() * 45)
			  var plus = Math.floor(Math.random() * 2)
			  if (plus == 1) rotate = '-' + rotate
			  this.codes[i].rotate = 'rotate(' + rotate + 'deg)'
			  // 随机验证码字体大小
			  var size = Math.floor(Math.random() * sizes.length)
			  this.codes[i].size = sizes[size] + 'px'
			}
		},
	  selectChange(e){
		  this.role = e
	  },
      submitForm(formName) {
        if (this.roles.length!=1) {
            if (!this.role) {
                this.$message.error("请选择登录用户类型");
                return false;
            }
        } else {
            this.role = this.roles[0].roleName;
            this.loginForm.tableName = this.roles[0].tableName;
        }

		this.loginPost(formName)
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      },
	  loginPost(formName) {
		this.$refs[formName].validate((valid) => {
		  if (valid) {
		    this.$http.get(`${this.loginForm.tableName}/login`, {params: this.loginForm}).then(res => {
		      if (res.data.code === 0) {
		        localStorage.setItem('frontToken', res.data.token);
		        localStorage.setItem('UserTableName', this.loginForm.tableName);
		        localStorage.setItem('username', this.loginForm.username);
		        localStorage.setItem('adminName', this.loginForm.username);
		        localStorage.setItem('frontSessionTable', this.loginForm.tableName);
		        localStorage.setItem('frontRole', this.role);
		        localStorage.setItem('keyPath', 0);
		        this.$router.push('/');
		        this.$message({
		          message: '登录成功',
		          type: 'success',
		          duration: 1500,
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
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.login-page {
  position: relative;
  min-height: 100vh;
  width: 100%;
  overflow: hidden;
}

.login-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 60%;
  height: 100%;
  background: url(https://nous-core.obs.cn-north-4.myhuaweicloud.com/images/W%60%40P4H_AU%7DJD%60G0W9DE9CYQ.png我先不要这个图);
  background-size: cover;
  background-position: center center;
  background-repeat: no-repeat;
  z-index: 1;
}

.login-form {
  position: fixed;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 30vw;
  padding: 20px;
  border-radius: 0px;
  background: #fff;
  height: auto;
  z-index: 2;
  margin-right:10%; /* 可根据需要调整右侧间距 */
}

.hidden-title {
  width: 100%;
  margin: 0 0 10px 0;
  line-height: 44px;
  font-size: 20px;
  color: rgba(64, 158, 255, 1);
  text-align: center;
  display: none;
}

.main-title {
  width: 80%;
  margin: 0 auto 10px;
  line-height: 44px;
  font-size: 28px;
  color: #333;
  text-align: left;
}

.form-item {
  width: 80%;
  margin: 0 auto 10px;
}

.input-label {
  width: 100%;
  line-height: 36px;
  font-size: 14px;
  color: #333;
}

.input-field {
  border: 1px solid #ddd;
  padding: 0 10px;
  color: #666;
  border-radius: 4px;
  width: 100%;
  font-size: 14px;
  height: 40px;
}

.role-selector {
  width: 80%;
  margin: 0 auto 10px;
}

.button-group {
  width: 80%;
  margin: 20px auto;
}

.login-btn {
  border: 0;
  cursor: pointer;
  padding: 0 24px;
  margin: 0 5px;
  outline: none;
  color: #fff;
  border-radius: 4px;
  background: #ffc000;
  width: 100%;
  font-size: 16px;
  height: 44px;
}

.reset-btn {
  border: 0;
  cursor: pointer;
  padding: 0 24px;
  margin: 0 5px;
  outline: none;
  color: #666;
  border-radius: 4px;
  text-align: right;
  background: none;
  width: 100%;
  font-size: 14px;
  height: 44px;
}

.register-links {
  width: 80%;
  margin: 20px auto;
}

.register-link {
  cursor: pointer;
  margin: 0 5px;
  font-size: 14px;
  text-decoration: none;
  color: #F5BB00;
  background: #fff;
}

.idea1, .idea2 {
  width: 100%;
  height: 40px;
  display: none;
}

.idea1 {
  background: red;
}

.idea2 {
  background: blue;
}
	.container {
		position: relative;
		background: url(https://nous-core.obs.cn-north-4.myhuaweicloud.com/images/W%60%40P4H_AU%7DJD%60G0W9DE9CYQ.png我先不要这个图);
		
		.el-form-item {
		  & /deep/ .el-form-item__content {
		    width: 100%;
		  }
		}
		
		.list-item /deep/ .el-form-item__content {
			display: block;
		}

		.list-code /deep/ .el-form-item__content {
			display: block;
		}

		.list-type /deep/ .el-form-item__content {
			margin: 10px 0 0;
			display: flex;
		}

		.list-btn /deep/ .el-form-item__content {
			display: flex;
			justify-content: center;
			flex-wrap: wrap;
		}
		
		.list-item /deep/ .el-input .el-input__inner {
			border: 1px solid #ddd;
			border-radius: 4px;
			padding: 0 10px;
			color: #666;
			width: 100%;
			font-size: 14px;
			height: 40px;
		}
		
		.list-code /deep/ .el-input .el-input__inner {
			border: 1px solid #ddd;
			border-radius: 4px;
			padding: 0 10px;
			outline: none;
			color: #666;
			display: inline-block;
			vertical-align: middle;
			width: 144px;
			font-size: 14px;
			height: 40px;
		}

		// select
		.list-type.select .el-select /deep/ .el-input__inner {
			border: 1px solid #ddd;
			border-radius: 4px;
			padding: 0 10px;
			color: #666;
			width: 288px;
			font-size: 14px;
			height: 40px;
		}
	}

</style>
