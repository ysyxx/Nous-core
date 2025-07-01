<template>
  <div class="main-content">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/course' }">课程管理</el-breadcrumb-item>
      <el-breadcrumb-item>章节管理</el-breadcrumb-item>
      <el-breadcrumb-item>{{ courseTitle }}</el-breadcrumb-item>
    </el-breadcrumb>
    <el-divider></el-divider>

    <div v-show="!addOrUpdateFlag">
      <el-form :inline="true" :model="searchForm" class="form-content">
        <el-form-item>
          <el-button round @click="search()">刷新</el-button>
          <el-button
              v-if="isAuth('chapter','新增')"
              type="primary"
              @click="addOrUpdateHandler()"
              round
          >新增章节</el-button>
          <el-button
              v-if="isAuth('chapter','删除')"
              :disabled="dataListSelections.length <= 0"
              type="danger"
              @click="deleteHandler()"
              round
          >批量删除</el-button>
        </el-form-item>
      </el-form>
      <div class="table-content">
        <el-table
            :data="dataList"
            border
            v-loading="dataListLoading"
            @selection-change="selectionChangeHandler"
            style="width: 100%;"
        >
          <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>

          <el-table-column prop="id" header-align="center" align="center" label="章节ID" width="80"></el-table-column>
          <el-table-column prop="title" header-align="center" align="center" label="章节标题"></el-table-column>
          <el-table-column prop="chapterOrder" header-align="center" align="center" label="显示顺序" width="100"></el-table-column>

          <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="addOrUpdateHandler(scope.row.id)">修改</el-button>
              <el-button type="text" size="small" @click="deleteHandler(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
            @size-change="sizeChangeHandle"
            @current-change="currentChangeHandle"
            :current-page="pageIndex"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="pageSize"
            :total="totalPage"
            layout="total, sizes, prev, pager, next, jumper"
            class="pagination-content"
        ></el-pagination>
      </div>
    </div>
    <add-or-update v-if="addOrUpdateFlag" ref="addOrUpdate" @refreshDataList="onRefreshDataList"  @dialogClosed="onDialogClosed"></add-or-update>
  </div>
</template>

<script>
import AddOrUpdate from "./add-or-update"; // 我们下一步会创建这个文件
export default {
  data() {
    return {
      searchForm: {}, // 章节列表通常不需要复杂搜索
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      showFlag: true,
      addOrUpdateFlag: false,
      courseId: null,
      courseTitle: '',

      // 假设的后端接口，需要后端实现
      dataListUrl: "chapter/page",
      deleteUrl: "chapter/delete",
    };
  },
  components: {
    AddOrUpdate
  },
  created() {
    // 从路由参数中获取 courseId 和 courseTitle
    this.courseId = this.$route.query.courseId;
    if (this.courseId) {
      this.getCourseInfo();
      this.getDataList();
    } else {
      this.$message.error("未找到课程ID，请返回重试");
      this.$router.push({ path: '/course' });
    }
  },
    mounted() {
    this.getDataList();
  },
  methods: {
    search() {
      this.pageIndex = 1;
      this.getDataList();
    },
    getCourseInfo() {
      this.$http({
        url: `course/info/${this.courseId}`,
        method: "get"
      }).then(({data})=>{
        if (data && data.code === 0) {
          this.courseTitle = data.data.title;
        }
      });
    },
    // 获取数据列表
    getDataList() {
      this.dataListLoading = true;
      this.$http({
        url: this.dataListUrl,
        method: "get",
        params: {
          page: this.pageIndex,
          limit: this.pageSize,
          sort: 'chapter_order', // 按章节顺序排序
          order: 'asc',
          courseId: this.courseId, //【核心】只查询当前课程下的章节
          ...this.searchForm
        }
      }).then(({ data }) => {
        // 处理返回数据
        if (data && data.code === 0) {
          this.dataList = data.data.list;
          this.totalPage = data.data.total;
        } else {
          this.dataList = [];
          this.totalPage = 0;
        }
        this.dataListLoading = false;
      });
    },
    // 每页数
    sizeChangeHandle(val) {
      this.pageSize = val;
      this.pageIndex = 1;
      this.getDataList();
    },
    // 当前页
    currentChangeHandle(val) {
      this.pageIndex = val;
      this.getDataList();
    },
    // 多选
    selectionChangeHandler(val) {
      this.dataListSelections = val;
    },
    // 添加/修改
    addOrUpdateHandler(id) {
      this.addOrUpdateFlag = true;
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id);
      });
    },
     onDialogClosed() {
    this.addOrUpdateFlag = false;
  },
  onRefreshDataList() {
    this.addOrUpdateFlag = false;

    this.search();
  },
    // 删除
    deleteHandler(id) {
      var ids = id
          ? [Number(id)]
          : this.dataListSelections.map(item => {
            return Number(item.id);
          });
      this.$confirm(`确定进行[${id ? "删除" : "批量删除"}]操作?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$http({
          url: this.deleteUrl,
          method: "post",
          data: ids
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message({
              message: "操作成功",
              type: "success",
              duration: 1500,
              onClose: () => {
                this.search();
              }
            });
          } else {
            this.$message.error(data.msg);
          }
        });
      });
    }
  }
};
</script>
<style lang="scss" scoped>
.main-content {
  padding: 20px;
}
</style>