<template>
  <div class="main-content">
    <div v-if="showFlag">
      <el-form :inline="true" :model="searchForm" class="form-content">
        <el-form-item label="用户账号">
          <el-input v-model="searchForm.yonghuzhanghao" placeholder="用户账号" clearable></el-input>
        </el-form-item>
        <el-form-item label="用户姓名">
          <el-input v-model="searchForm.yonghuxingming" placeholder="用户姓名" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button round @click="search()">查询</el-button>
          <el-button
              v-if="isAuth('yonghu','新增')"
              type="primary"
              @click="addOrUpdateHandler()"
              round
          >新增</el-button>
          <el-button
              v-if="isAuth('yonghu','删除')"
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
          <el-table-column prop="yonghuzhanghao" header-align="center" align="center" label="用户账号"></el-table-column>
          <el-table-column prop="yonghuxingming" header-align="center" align="center" label="用户姓名"></el-table-column>
          <el-table-column prop="touxiang" header-align="center" align="center" width="120" label="头像">
            <template slot-scope="scope">
              <el-image
                  style="width: 80px; height: 80px; border-radius: 50%;"
                  :src="scope.row.touxiang"
                  :preview-src-list="[scope.row.touxiang]"
              ></el-image>
            </template>
          </el-table-column>
          <el-table-column prop="xingbie" header-align="center" align="center" label="性别" width="80"></el-table-column>
          <el-table-column prop="shoujihaoma" header-align="center" align="center" label="手机号码"></el-table-column>
          <el-table-column prop="youxiang" header-align="center" align="center" label="邮箱"></el-table-column>

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
import AddOrUpdate from "./add-or-update"; // 下一步我们将修改这个文件
export default {
  data() {
    return {
      // 【修改点】更新搜索表单模型
      searchForm: {
        yonghuzhanghao: "",
        yonghuxingming: ""
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      showFlag: true,
      addOrUpdateFlag: false,

      dataListUrl: "yonghu/page",
      deleteUrl: "yonghu/delete",
    };
  },
  components: {
    AddOrUpdate
  },

  mounted() {
    this.getDataList();
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

      // 只有当 searchForm.yonghuzhanghao 不为空时才添加到 params
      if (this.searchForm.yonghuzhanghao !== "") {
        params.yonghuzhanghao = this.searchForm.yonghuzhanghao;
      }

      // 只有当 searchForm.yonghuxingming 不为空时才添加到 params
      if (this.searchForm.yonghuxingming !== "") {
        params.yonghuxingming = this.searchForm.yonghuxingming;
      }
      // --- 构建 params 对象结束 ---

      this.$http({
        url: this.dataListUrl,
        method: "get",
        params: params // 使用我们动态构建的 params
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
        this.$refs.addOrUpdate.init(id);
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
// 样式可以根据需要自行调整
</style>