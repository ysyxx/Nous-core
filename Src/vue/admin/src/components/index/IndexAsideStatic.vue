<template>
  <div class="aside">
    <div class="title" :style='{"padding":"10px","boxShadow":"0 1px 6px #ccc","margin":"0","borderColor":"#f2f2f2","backgroundColor":"#fff","color":"#333","textAlign":"center","borderRadius":"0","borderWidth":"0 0 1px","width":"100%","lineHeight":"50px","fontSize":"20px","borderStyle":"solid"}'>
      {{this.$project.projectName}}
    </div>
    <el-menu
        @select="handleSelect"
        :default-active="defaultActive"
        :unique-opened="true"
        class="el-menu-vertical-demo"
        background-color="rgba(0, 0, 0, 0)"
        text-color="#ffffff"
        active-text-color="#FFD700">

      <el-submenu index="2">
        <template slot="title"><i class="el-icon-user-solid"></i><span>用户管理</span></template>
        <el-menu-item index="2-1" @click="menuHandler('yonghu')">前台用户</el-menu-item>
        <el-menu-item index="2-2" @click="menuHandler('users')">管理员</el-menu-item>
      </el-submenu>

      <el-submenu index="3">
        <template slot="title"><i class="el-icon-s-management"></i><span>课程管理</span></template>
        <el-menu-item index="3-1" @click="menuHandler('course')">课程列表</el-menu-item>
        <el-menu-item index="3-2" @click="menuHandler('chapter')">章节管理</el-menu-item>
        <el-menu-item index="3-3" @click="menuHandler('lesson')">课时管理</el-menu-item>
        <el-menu-item index="3-4" @click="menuHandler('courseType')">课程类型</el-menu-item>
      </el-submenu>

      <el-menu-item index="4" @click="menuHandler('kechengpingjia')"><i class="el-icon-s-comment"></i>课程评价管理</el-menu-item>

      <el-menu-item index="5" @click="menuHandler('messages')"><i class="el-icon-s-promotion"></i>意见反馈</el-menu-item>

      <el-menu-item index="6" @click="menuHandler('forum')"><i class="el-icon-s-data"></i>互动交流</el-menu-item>

      <el-submenu index="7">
        <template slot="title"><i class="el-icon-s-tools"></i><span>系统管理</span></template>
        <el-menu-item index="7-1" @click="menuHandler('config')">轮播图管理</el-menu-item>
        <el-menu-item index="7-2" @click="menuHandler('syslog')">系统日志</el-menu-item>
      </el-submenu>

    </el-menu>
  </div>
</template>

<script>
// 【修改点】移除了 import menu from '@/utils/menu'
export default {
  data() {
    return {
      // 【修改点】移除了 menuList
      defaultActive: '',
      // 【修改点】移除了 style_o
    };
  },
  created() {
    // 【修改点】移除了 this.style_o = this.style.default; 和 this.menuList = menu.list();
    this.defaultActive = localStorage.getItem('default-active')?localStorage.getItem('default-active'):'';
  },
  watch: {
    $route: {
      handler(to, from) {
        this.defaultActive = to.path.split('/')[1]; // 【修改点】用路径来确定激活的菜单
        localStorage.setItem('default-active',this.defaultActive)
      },
      immediate: true
    }
  },
  methods: {
    handleSelect(key, keyPath) {
      // console.log(key, keyPath);
    },
    menuHandler(name) {
      this.$router.push({
        path: '/' + name // 【修改点】确保通过路径跳转
      });
    }
  }
}
</script>
<style lang="scss" scoped>
/* 样式保持不变 */
.aside {
  position: relative;
  display: flex;
  flex-direction: column;
  height: 100%;
  .title {
    height: 60px;
    background-color: #f2f2f2;
    box-shadow: 0 1px 6px #ccc;
    color: #333;
    width: 100%;
    text-align: center;
    line-height: 50px;
    border-bottom: 1px solid #f2f2f2;
    border-radius: 0;
    padding: 10px;
    margin: 0;
    font-size: 20px;
  }
  .el-menu-vertical-demo {
    flex: 1;
    width: 100% !important;
    overflow: auto;
    border: none;
    .el-menu-item {
      cursor: pointer;
      &:hover {
        background-color: #ccc !important;
      }
    }
    .el-submenu {
      cursor: pointer;
      & /deep/ .el-submenu__title:hover {
        background-color: #ccc !important;
      }
    }
  }
}
</style>