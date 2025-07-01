<template>
  <div class="main-content">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/course' }">课程管理</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: `/chapter?courseId=${courseId}` }">章节管理</el-breadcrumb-item>
      <el-breadcrumb-item>课时管理</el-breadcrumb-item>
      <el-breadcrumb-item>{{ chapterTitle }}</el-breadcrumb-item>
    </el-breadcrumb>
    <el-divider></el-divider>

    <div v-if="showFlag">
      <el-form :inline="true" :model="searchForm" class="form-content">
        <el-form-item>
          <el-button round @click="search()">刷新</el-button>
          <el-button
              v-if="isAuth('lesson','新增')"
              type="primary"
              @click="addOrUpdateHandler()"
              round
          >新增课时</el-button>
          <el-button
              v-if="isAuth('lesson','删除')"
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

          <el-table-column prop="id" header-align="center" align="center" label="课时ID" width="80"></el-table-column>
          <el-table-column prop="title" header-align="center" align="center" label="课时标题"></el-table-column>
          <el-table-column prop="lessonOrder" header-align="center" align="center" label="显示顺序" width="100"></el-table-column>
          <el-table-column prop="lessonType" header-align="center" align="center" label="课时类型" width="100">
            <template slot-scope="scope">
              <el-tag :type="scope.row.lessonType === 'video' ? 'primary' : 'success'">
                {{ scope.row.lessonType === 'video' ? '视频' : (scope.row.lessonType === 'article' ? '图文' : '其他') }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="videoUrl" header-align="center" align="center" label="视频/PDF链接">
            <template slot-scope="scope">
              <a :href="scope.row.videoUrl || scope.row.pdfUrl" target="_blank" v-if="scope.row.videoUrl || scope.row.pdfUrl">点击查看</a>
              <span v-else>无</span>
            </template>
          </el-table-column>

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
    <add-or-update v-if="addOrUpdateFlag" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
  </div>
</template>

<script>
import AddOrUpdate from "./add-or-update"; // 我们下一步会创建这个文件
export default {
  data() {
    return {
      searchForm: {},
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      showFlag: true,
      addOrUpdateFlag: false,
      courseId: null,
      chapterId: null,
      chapterTitle: '',

      dataListUrl: "lessons/page",
      deleteUrl: "lessons/delete",
    };
  },
  components: {
    AddOrUpdate
  },
  created() {
    // 从路由参数中获取 courseId 和 chapterId
    this.courseId = this.$route.query.courseId;
    this.chapterId = this.$route.query.chapterId;

    if (this.chapterId) {
      this.getChapterInfo();
      this.getDataList();
    } else {
      this.$message.error("未找到章节ID，请返回重试");
      this.$router.push({ path: '/course' });
    }
  },
  methods: {
    search() {
      this.pageIndex = 1;
      this.getDataList();
    },
    // 获取所属章节信息用于面包屑导航
    getChapterInfo() {
      this.$http({
        url: `chapter/info/${this.chapterId}`,
        method: "get"
      }).then(({data})=>{
        if (data && data.code === 0) {
          this.chapterTitle = data.data.title;
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
          sort: 'lessonOrder', // 按课时顺序排序
          order: 'asc',
          chapterId: this.chapterId, //【核心】只查询当前章节下的课时
        }
      }).then(({ data }) => {
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
      this.showFlag = false;
      this.addOrUpdateFlag = true;
      this.$nextTick(() => {
        // 【核心】将 chapterId 传递给子组件
        this.$refs.addOrUpdate.init(id, this.chapterId);
      });
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