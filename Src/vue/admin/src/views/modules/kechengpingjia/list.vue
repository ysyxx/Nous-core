<template>
  <div class="main-content">
    <div v-show="!addOrUpdateFlag">
      <el-form :inline="true" :model="searchForm" class="form-content">
        <el-form-item label="课程名称">
          <el-input v-model="searchForm.kechengmingcheng" placeholder="课程名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="审核状态">
          <el-select v-model="searchForm.sfsh" placeholder="请选择审核状态" clearable>
            <el-option label="已通过" value="是"></el-option>
            <el-option label="未通过" value="否"></el-option>
            <el-option label="待审核" value="待审核"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button round @click="search()">查询</el-button>
          <el-button
              v-if="isAuth('kechengpingjia','删除')"
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
          <el-table-column prop="kechengmingcheng" header-align="center" align="center" label="课程名称"></el-table-column>
          <el-table-column prop="yonghuzhanghao" header-align="center" align="center" label="评价用户"></el-table-column>
          <el-table-column prop="pingjiashijian" header-align="center" align="center" label="评价时间" width="120"></el-table-column>
          <el-table-column prop="sfsh" header-align="center" align="center" label="审核状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="scope.row.sfsh === '是' ? 'success' : (scope.row.sfsh === '否' ? 'danger' : 'warning')">
                {{ scope.row.sfsh === '是' ? '已通过' : (scope.row.sfsh === '否' ? '未通过' : '待审核') }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="addOrUpdateHandler(scope.row.id)">查看</el-button>
              <el-button v-if="scope.row.sfsh == '待审核'" type="text" size="small" @click="shBatchHandler(scope.row)">审核</el-button>
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
import AddOrUpdate from "./add-or-update"; // 这个文件将作为详情页
export default {
  data() {
    return {
      searchForm: {
        kechengmingcheng: "",
        sfsh: ""
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      showFlag: true,
      addOrUpdateFlag: false,

      dataListUrl: "courseAssessment/page",
      deleteUrl: "courseAssessment/delete",
      shBatchUrl: "courseAssessment/shBatch",
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
    getDataList() {
      this.dataListLoading = true;


       let params = {
        page: this.pageIndex,
        limit: this.pageSize,
        sort: 'id',
        order: 'desc',
      };

      
      if (this.searchForm.kechengmingcheng !== "") {
        params.kechengmingcheng = this.searchForm.kechengmingcheng;
        
      }
      if (this.searchForm.sfsh !== "") {
        params.sfsh = this.searchForm.sfsh;
      }

      this.$http({
        url: this.dataListUrl,
        method: "get",
        params:params
      }).then(({ data }) => {
      console.log(data);
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
    // 查看详情
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

    // 【新增】审核功能
    shBatchHandler(row) {
      this.$confirm(`确定要审核这条评价吗?`, "提示", {
        confirmButtonText: "通过",
        cancelButtonText: "不通过",
        distinguishCancelAndClose: true,
        type: "warning"
      }).then(() => {
        this.executeSh(row.id, '是');
      }).catch(action => {
        if (action === 'cancel') {
          this.executeSh(row.id, '否');
        }
      });
    },
    executeSh(id, sfsh) {
      this.$http({
        url: this.shBatchUrl,
        method: "post",
        data: [id],
        params: { sfsh }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.$message({
            message: "审核操作成功",
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