<template>
  <div class="main-content">
    <div v-show="!addOrUpdateFlag">
      <el-form :inline="true" :model="searchForm" class="form-content">
        <el-form-item label="课程名称">
          <el-input v-model="searchForm.title" placeholder="课程名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="课程类型">
          <el-select v-model="searchForm.kechengleixing" placeholder="请选择课程类型" clearable @change="search()">
            <el-option
              v-for="item in courseTypes"
              :key="item.value"
              :label="item.label"
              :value="item.value"
              
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button round @click="search()">查询</el-button>
          <el-button
              v-if="isAuth('course','新增')"
              type="primary"
              @click="addOrUpdateHandler()"
              round
          >新增</el-button>
          <el-button
              v-if="isAuth('course','删除')"
              :disabled="dataListSelections.length <= 0"
              type="danger"
              @click="deleteHandler()"
              round
          >删除</el-button>
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

          <el-table-column prop="id" header-align="center" align="center" label="ID" width="80"></el-table-column>
          <el-table-column prop="title" header-align="center" align="center" label="课程名称"></el-table-column>
          <el-table-column prop="kechengleixing" header-align="center" align="center" label="课程类型"></el-table-column>
          <el-table-column prop="coverImageUrl" header-align="center" align="center" width="200" label="课程封面">
            <template slot-scope="scope">
              <el-image
                  style="width: 100px; height: 100px"
                  :src="scope.row.coverImageUrl"
                  :preview-src-list="[scope.row.coverImageUrl]"
              ></el-image>
            </template>
          </el-table-column>
          <el-table-column prop="clickNum" header-align="center" align="center" label="点击数"></el-table-column>
          <el-table-column prop="storeupNum" header-align="center" align="center" label="收藏数"></el-table-column>

          <el-table-column fixed="right" header-align="center" align="center" width="250" label="操作">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="addOrUpdateHandler(scope.row.id)">修改</el-button>

              <el-button type="text" size="small" @click="chapterHandler(scope.row.id)">章节管理</el-button>
              <el-button type="text" size="small" @click="lessonHandler(scope.row.id)">课时管理</el-button>

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
import AddOrUpdate from "./add-or-update.vue"; // 【注意】将需要创建或修改此文件
export default {
  data() {
    return {
      searchForm: {
        title: "",
        kechengleixing: ""
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      showFlag: true,
      addOrUpdateFlag: false,

      courseTypeOptionsUrl: "courseType/page", 
      // 【修改点】更新API接口路径
      dataListUrl: "course/page",
      deleteUrl: "course/delete",

        courseTypes: [],
    };
  },
  components: {
    AddOrUpdate
  },
  mounted() {
    this.getDataList();
    this.getCourseTypes(); // 获取课程类型
  },
  activated() {
    this.getDataList();
  },
  methods: {
    search() {
      this.pageIndex = 1;
      this.getDataList();
    },
    // 获取数据列表
    getDataList() {
      this.dataListLoading = true;

      // --- 构建 params 对象 ---
      let params = {
        page: this.pageIndex,
        limit: this.pageSize,
        sort: 'id',
        order: 'desc',
      };

      if (this.searchForm.title !== "") {
        params.title = this.searchForm.title;
      }
      if (this.searchForm.kechengleixing !== "") {
        params.kechengleixing = this.searchForm.kechengleixing;
      }

      this.$http({
        url: this.dataListUrl,
        method: "get",
        params: params
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
    // 【新增】跳转到章节管理
    chapterHandler(id) {
      this.$router.push({ path: '/chapter', query: { courseId: id } });
    },
    // 【新增】跳转到课时管理
    lessonHandler(id) {
      this.$router.push({ path: '/lesson', query: { courseId: id } });
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
    },
    getCourseTypes() {
      this.$http({
        url: this.courseTypeOptionsUrl,
        method: "get",
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.courseTypes = data.data.list.map(item => ({
            label: item.courseType,
            value: item.courseType  
          }));
        } else {
          this.courseTypes = [];
          this.$message.error(data.msg || '获取课程类型失败');
        }
      }).catch(error => {
        console.error("获取课程类型接口错误:", error);
        this.$message.error('获取课程类型失败，请稍后再试');
        this.courseTypes = [];
      });
    },

  }
};
</script>
<style lang="scss" scoped>
// 样式可以根据需要自行调整
</style>