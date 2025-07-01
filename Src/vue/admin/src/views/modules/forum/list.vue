<template>
  <div class="main-content">
    <div v-if="showFlag">
      <el-form :inline="true" :model="searchForm" class="form-content">
        <el-form-item label="帖子标题">
          <el-input v-model="searchForm.title" placeholder="帖子标题" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button round @click="search()">查询</el-button>
          <el-button
              v-if="isAuth('forum','删除')"
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
          <el-table-column prop="title" header-align="center" align="center" label="帖子标题"></el-table-column>
          <el-table-column prop="username" header-align="center" align="center" label="发帖用户" width="150"></el-table-column>
          <el-table-column prop="isdone" header-align="center" align="center" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="scope.row.isdone === '开放' ? 'success' : 'info'">{{ scope.row.isdone }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="istop" header-align="center" align="center" label="是否置顶" width="100">
            <template slot-scope="scope">
              <el-tag :type="scope.row.istop === 1 ? 'danger' : 'primary'">{{ scope.row.istop === 1 ? '是' : '否' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="addtime" header-align="center" align="center" label="发帖时间" width="180"></el-table-column>

          <el-table-column fixed="right" header-align="center" align="center" width="200" label="操作">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="addOrUpdateHandler(scope.row.id)">查看评论</el-button>
              <el-button v-if="scope.row.istop !== 1" type="text" size="small" @click="updateIstop(scope.row.id, 1)">置顶</el-button>
              <el-button v-if="scope.row.istop === 1" type="text" size="small" @click="updateIstop(scope.row.id, 0)">取消置顶</el-button>
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
import AddOrUpdate from "./add-or-update"; // 这个文件将作为详情和评论查看页
export default {
  data() {
    return {
      searchForm: {
        title: "",
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      showFlag: true,
      addOrUpdateFlag: false,

      dataListUrl: "forum/page",
      deleteUrl: "forum/delete",
      updateUrl: "forum/update", // 【新增】用于更新置顶状态
    };
  },
  components: {
    AddOrUpdate
  },
  activated() {
    this.getDataList();
  },
  methods: {
    search() {
      this.pageIndex = 1;
      this.getDataList();
    },
    getDataList() {
      this.dataListLoading = true;
      this.$http({
        url: this.dataListUrl,
        method: "get",
        params: {
          page: this.pageIndex,
          limit: this.pageSize,
          sort: 'addtime',
          order: 'desc',
          parentid: 0, // 【核心】只查询主帖
          ...this.searchForm
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
    sizeChangeHandle(val) {
      this.pageSize = val;
      this.pageIndex = 1;
      this.getDataList();
    },
    currentChangeHandle(val) {
      this.pageIndex = val;
      this.getDataList();
    },
    selectionChangeHandler(val) {
      this.dataListSelections = val;
    },
    // 查看详情/评论
    addOrUpdateHandler(id) {
      this.showFlag = false;
      this.addOrUpdateFlag = true;
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id);
      });
    },
    // 【新增】置顶/取消置顶功能
    updateIstop(id, istop) {
      this.$http({
        url: this.updateUrl,
        method: "post",
        data: { id, istop }
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