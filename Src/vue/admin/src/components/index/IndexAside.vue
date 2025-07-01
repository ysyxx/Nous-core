<template>
  <el-aside class="index-aside" width="200px">
    <div class="index-aside-inner">
      <el-menu :default-active="activeMenu" :unique-opened="true">
        <el-menu-item @click="menuHandler('/')" index="/">
          <i class="el-icon-s-home"></i>
          <span slot="title">首页</span>
        </el-menu-item>

        <el-submenu index="center">
          <template slot="title">
            <i class="el-icon-user-solid"></i>
            <span>个人中心</span>
          </template>
          <el-menu-item @click="menuHandler('updatePassword')">修改密码</el-menu-item>
          <el-menu-item @click="menuHandler('center')">个人信息</el-menu-item>
        </el-submenu>

        <template v-for="menu in menuList">
          <el-submenu v-if="menu.child && menu.child.length > 0" :index="menu.tableName">
            <template slot="title">
              <i :class="menu.icon"></i>
              <span>{{ menu.menu }}</span>
            </template>
            <el-menu-item v-for="(child, Cindex) in menu.child" :key="Cindex" :index="`/${child.tableName}`" @click="menuHandler(child.tableName)">
              {{ child.menu }}
            </el-menu-item>
          </el-submenu>
          <el-menu-item v-else :index="`/${menu.tableName}`" @click="menuHandler(menu.tableName)">
            <i :class="menu.icon"></i>
            <span slot="title">{{ menu.menu }}</span>
          </el-menu-item>
        </template>
      </el-menu>
    </div>
  </el-aside>
</template>

<script>
export default {
  data() {
    return {
      menuList: [],
      dynamicMenuRoutes: [],
    };
  },
  computed: {
    activeMenu() {
      const route = this.$route;
      const { meta, path } = route;
      if (meta.activeMenu) {
        return meta.activeMenu;
      }
      return path;
    },
  },
  mounted() {
    // 【核心修改点】
    // 原先的逻辑是直接从 sessionStorage 读取后端的扁平化菜单。
    // 现在，我们根据新的架构在前端定义好菜单的层级结构。
    // 理想情况下，后端 /menu/list 接口应该返回下面这样的JSON结构，前端代码就无需修改。
    // 在后端接口更新前，我们先在前端模拟这个结构，以确保功能正常。

    const role = this.$storage.get('role');
    let adminMenu = [
      {
        "menu": "用户管理",
        "icon": "el-icon-user-solid",
        "tableName": "userManagement", // 父菜单的唯一标识
        "child": [
          {
            "menu": "前台用户",
            "tableName": "yonghu",
            "perms": ["list", "add", "update", "delete"]
          },
          {
            "menu": "管理员",
            "tableName": "users",
            "perms": ["list", "add", "update", "delete"]
          }
        ]
      },
      {
        "menu": "课程管理",
        "icon": "el-icon-s-management",
        "tableName": "courseManagement",
        "child": [
          {
            "menu": "课程列表",
            "tableName": "course",
            "perms": ["list", "add", "update", "delete"]
          },
          // 【注意】章节和课时管理页面需要您后续创建，这里先加上菜单项
          {
            "menu": "章节管理",
            "tableName": "chapter",
            "perms": ["list", "add", "update", "delete"]
          },
          {
            "menu": "课时管理",
            "tableName": "lesson",
            "perms": ["list", "add", "update", "delete"]
          },
          {
            "menu": "课程类型",
            "tableName": "courseType",
            "perms": ["list", "add", "update", "delete"]
          }
        ]
      },
      {
        "menu": "课程评价管理",
        "icon": "el-icon-s-comment",
        "tableName": "course_assessment",
        "child": []
      },
      {
        "menu": "意见反馈",
        "icon": "el-icon-s-promotion",
        "tableName": "messages",
        "child": []
      },
      {
        "menu": "互动交流",
        "icon": "el-icon-s-data",
        "tableName": "forum",
        "child": []
      },
      {
        "menu": "系统管理",
        "icon": "el-icon-s-tools",
        "tableName": "systemManagement",
        "child": [
          {
            "menu": "轮播图管理",
            "tableName": "config",
            "perms": ["list", "add", "update", "delete"]
          },
          {
            "menu": "系统日志",
            "tableName": "syslog",
            "perms": ["list"]
          }
        ]
      }
    ];

    // 这里简单处理，直接赋予管理员所有菜单
    if (role === '管理员') {
      this.menuList = adminMenu;
    }

    // 您可以从 sessionStorage 获取动态路由，如果后端已经实现了权限路由
    this.dynamicMenuRoutes = JSON.parse(sessionStorage.getItem("dynamicMenuRoutes") || "[]");
  },
  methods: {
    menuHandler(name) {
      this.$router.push({ path: `/${name}` });
    }
  }
};
</script>
<style lang="scss" scoped>
.index-aside {
  margin-top: 60px; /* 根据标题栏高度调整 */
  overflow: hidden;
  .index-aside-inner {
    width: 100%;
    height: 100%;
    overflow-y: auto;
    .el-menu {
      border-right: none;
    }
  }
}
</style>