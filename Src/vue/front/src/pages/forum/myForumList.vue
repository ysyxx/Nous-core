<template>
  <div class="my-posts-container">
    <!-- 返回按钮 -->
    <el-button class="back-button" type="warning" size="mini" @click="backClick">
      <i class="el-icon-back"></i>返回
    </el-button>
    
    <!-- 标题区域 -->
    <div class="section-title">我的发布</div>
    
    <!-- 表格区域 -->
    <el-table
      class="posts-table"
      :data="tableData"
      style="width: 100%">
      
      <el-table-column
        label="标题"
        prop="title"
        min-width="200">
      </el-table-column>
      
      <el-table-column
        label="发布时间"
        prop="addtime"
        width="180">
      </el-table-column>
      
      <el-table-column 
        label="操作" 
        width="150"
        align="center">
        <template slot-scope="scope">
          <el-button
            class="edit-btn"
            size="mini"
            @click="handleEdit(scope.$index, scope.row)">
            修改
          </el-button>
          <el-button
            class="delete-btn"
            size="mini"
            type="danger"
            @click="handleDelete(scope.$index, scope.row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 分页组件 -->
    <el-pagination
      class="pagination"
      background
      :pager-count="7"
      :page-size="pageSize"
      :page-sizes="pageSizes"
      prev-text="<"
      next-text=">"
      :hide-on-single-page="false"
      :layout="['total','prev','pager','next','sizes','jumper'].join()"
      :total="total"
      @current-change="curChange"
      @prev-click="prevClick"
      @next-click="nextClick">
    </el-pagination>
  </div>
</template>

<script>
  export default {
    data() {
      return {
		layouts: '',
        tableData: [],
        total: 1,
        pageSize: 10,pageSizes: [10,20,30,50],
        totalPage: 1
      }
    },
    created() {
      this.getMyForumList(1);
    },
    methods: {
      backClick() {
          this.$router.push('/index/center')
      },
      getMyForumList(page) {
        this.$http.get('forum/list', {params: {page, limit: this.pageSize, parentid: 0, sort: 'istop,toptime', order: 'desc,desc'}}).then(res => {
          if (res.data.code == 0) {
            this.tableData = res.data.data.list;
            this.total = res.data.data.total;
            this.pageSize = res.data.data.pageSize;this.pageSizes = [this.pageSize, this.pageSize*2, this.pageSize*3, this.pageSize*5];
            this.totalPage = res.data.data.totalPage;
          }
        });
      },
      curChange(page) {
        this.getMyForumList(page);
      },
      prevClick(page) {
        this.getMyForumList(page);
      },
      nextClick(page) {
        this.getMyForumList(page);
      },
      handleEdit(index, row) {
        this.$router.push({path: '/index/forumAdd', query: {id: row.id}})
      },
      handleDelete(index, row) {
        this.$confirm('是否确认删除?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let delIds = new Array();
          delIds.push(row.id);
          this.$http.post('forum/delete', delIds).then(res => {
            if (res.data.code == 0) {
              this.$message({
                type: 'success',
                message: '删除成功!',
                duration: 1500,
                onClose: () => {
                  this.getMyForumList(1);
                }
              });
            }
          });
        });
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.my-posts-container {
  width: 100%;
  padding: 30px 7% 40px;
  margin: 0 auto;
  position: relative;
  background: #fff;
}

/* 返回按钮 */
.back-button {
  border: 0;
  cursor: pointer;
  padding: 0 10px;
  margin: 0 5px 0 0;
  outline: none;
  color: #333;
  border-radius: 4px;
  background: #F5BB0050;
  width: auto;
  line-height: 40px;
  font-size: 14px;
  height: 40px;
  transition: all 0.3s;
  
  &:hover {
    background: #F5BB0070;
  }
  
  i {
    margin-right: 5px;
  }
}

/* 标题区域 */
.section-title {
  margin: 10px 0;
  color: #fff;
  text-align: center;
  background: #333;
  width: 100%;
  font-size: 20px;
  line-height: 54px;
}

/* 表格区域 */
.posts-table {
  margin: 20px 0;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  overflow: hidden;
  
  /* 表头样式 */
  /deep/ .el-table__header-wrapper {
    th {
      background-color: #f5f7fa;
      color: #333;
      font-weight: bold;
    }
  }
  
  /* 表格行样式 */
  /deep/ .el-table__body-wrapper {
    tr:hover {
      td {
        background-color: #f5f7fa !important;
      }
    }
  }
}

/* 操作按钮 */
.edit-btn {
  background-color: #409EFF;
  border-color: #409EFF;
  color: #fff;
  
  &:hover {
    background-color: #66b1ff;
    border-color: #66b1ff;
  }
}

.delete-btn {
  background-color: #F56C6C;
  border-color: #F56C6C;
  color: #fff;
  
  &:hover {
    background-color: #f78989;
    border-color: #f78989;
  }
}

/* 分页组件 */
.pagination {
  padding: 0 7%;
  margin: 10px auto;
  white-space: nowrap;
  overflow: hidden;
  color: #333;
  text-align: center;
  width: 100%;
  clear: both;
  font-weight: 500;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .my-posts-container {
    padding: 15px 5%;
  }
  
  .posts-table {
    /deep/ .el-table__header-wrapper {
      display: none;
    }
    
    /deep/ .el-table__body {
      tr {
        display: block;
        margin-bottom: 15px;
        border: 1px solid #ebeef5;
        border-radius: 4px;
        
        td {
          display: block;
          text-align: right;
          padding: 10px 15px;
          border: none;
          
          &::before {
            content: attr(data-label);
            float: left;
            font-weight: bold;
          }
        }
      }
    }
    
    /deep/ .el-table__row {
      td:last-child {
        text-align: center;
        
        &::before {
          display: none;
        }
      }
    }
  }
  
  .pagination {
    padding: 0 5%;
  }
}
  .section {
    width: 900px;
    margin: 0 auto;
  }
  
  #pagination.el-pagination /deep/ .el-pagination__total {
  	  	margin: 0 10px 0 0;
  	  	color: #666;
  	  	font-weight: 400;
  	  	display: inline-block;
  	  	vertical-align: top;
  	  	font-size: 13px;
  	  	line-height: 28px;
  	  	height: 28px;
  	  }
  
  #pagination.el-pagination /deep/ .btn-prev {
  	  	border: none;
  	  	border-radius: 2px;
  	  	padding: 0;
  	  	margin: 0 5px;
  	  	color: #666;
  	  	background: #f4f4f5;
  	  	display: inline-block;
  	  	vertical-align: top;
  	  	font-size: 13px;
  	  	line-height: 28px;
  	  	min-width: 35px;
  	  	height: 28px;
  	  }
  
  #pagination.el-pagination /deep/ .btn-next {
  	  	border: none;
  	  	border-radius: 2px;
  	  	padding: 0;
  	  	margin: 0 5px;
  	  	color: #666;
  	  	background: #f4f4f5;
  	  	display: inline-block;
  	  	vertical-align: top;
  	  	font-size: 13px;
  	  	line-height: 28px;
  	  	min-width: 35px;
  	  	height: 28px;
  	  }
  
  #pagination.el-pagination /deep/ .btn-prev:disabled {
  	  	border: none;
  	  	cursor: not-allowed;
  	  	border-radius: 2px;
  	  	padding: 0;
  	  	margin: 0 5px;
  	  	color: #C0C4CC;
  	  	background: #f4f4f5;
  	  	display: inline-block;
  	  	vertical-align: top;
  	  	font-size: 13px;
  	  	line-height: 28px;
  	  	height: 28px;
  	  }
  
  #pagination.el-pagination /deep/ .btn-next:disabled {
  	  	border: none;
  	  	cursor: not-allowed;
  	  	border-radius: 2px;
  	  	padding: 0;
  	  	margin: 0 5px;
  	  	color: #C0C4CC;
  	  	background: #f4f4f5;
  	  	display: inline-block;
  	  	vertical-align: top;
  	  	font-size: 13px;
  	  	line-height: 28px;
  	  	height: 28px;
  	  }
  
  #pagination.el-pagination /deep/ .el-pager {
  	  	padding: 0;
  	  	margin: 0;
  	  	display: inline-block;
  	  	vertical-align: top;
  	  }
  
  #pagination.el-pagination /deep/ .el-pager .number {
  	  	cursor: pointer;
  	  	padding: 0 4px;
  	  	margin: 0 5px;
  	  	color: #666;
  	  	display: inline-block;
  	  	vertical-align: top;
  	  	font-size: 13px;
  	  	line-height: 28px;
  	  	border-radius: 2px;
  	  	background: #f4f4f5;
  	  	text-align: center;
  	  	min-width: 30px;
  	  	height: 28px;
  	  }
  
  #pagination.el-pagination /deep/ .el-pager .number:hover {
  	  	cursor: pointer;
  	  	padding: 0 4px;
  	  	margin: 0 5px;
  	  	color: #fff;
  	  	display: inline-block;
  	  	vertical-align: top;
  	  	font-size: 13px;
  	  	line-height: 28px;
  	  	border-radius: 2px;
  	  	background: #F5BB00;
  	  	text-align: center;
  	  	min-width: 30px;
  	  	height: 28px;
  	  }
  
  #pagination.el-pagination /deep/ .el-pager .number.active {
  	  	cursor: default;
  	  	padding: 0 4px;
  	  	margin: 0 5px;
  	  	color: #FFF;
  	  	display: inline-block;
  	  	vertical-align: top;
  	  	font-size: 13px;
  	  	line-height: 28px;
  	  	border-radius: 2px;
  	  	background: #F5BB00;
  	  	text-align: center;
  	  	min-width: 30px;
  	  	height: 28px;
  	  }
  
  #pagination.el-pagination /deep/ .el-pagination__sizes {
  	  	display: inline-block;
  	  	vertical-align: top;
  	  	font-size: 13px;
  	  	line-height: 28px;
  	  	height: 28px;
  	  }
  
  #pagination.el-pagination /deep/ .el-pagination__sizes .el-input {
  	  	margin: 0 5px;
  	  	width: 100px;
  	  	position: relative;
  	  }
  
  #pagination.el-pagination /deep/ .el-pagination__sizes .el-input .el-input__inner {
  	  	border: 1px solid #DCDFE6;
  	  	cursor: pointer;
  	  	padding: 0 25px 0 8px;
  	  	color: #606266;
  	  	display: inline-block;
  	  	font-size: 13px;
  	  	line-height: 28px;
  	  	border-radius: 3px;
  	  	outline: 0;
  	  	background: #FFF;
  	  	width: 100%;
  	  	text-align: center;
  	  	height: 28px;
  	  }
  
  #pagination.el-pagination /deep/ .el-pagination__sizes .el-input span.el-input__suffix {
  	  	top: 0;
  	  	position: absolute;
  	  	right: 0;
  	  	height: 100%;
  	  }
  
  #pagination.el-pagination /deep/ .el-pagination__sizes .el-input .el-input__suffix .el-select__caret {
  	  	cursor: pointer;
  	  	color: #C0C4CC;
  	  	width: 25px;
  	  	font-size: 14px;
  	  	line-height: 28px;
  	  	text-align: center;
  	  }
  
  #pagination.el-pagination /deep/ .el-pagination__jump {
  	  	margin: 0 0 0 24px;
  	  	color: #606266;
  	  	display: inline-block;
  	  	vertical-align: top;
  	  	font-size: 13px;
  	  	line-height: 28px;
  	  	height: 28px;
  	  }
  
  #pagination.el-pagination /deep/ .el-pagination__jump .el-input {
  	  	border-radius: 3px;
  	  	padding: 0 2px;
  	  	margin: 0 2px;
  	  	display: inline-block;
  	  	width: 50px;
  	  	font-size: 14px;
  	  	line-height: 18px;
  	  	position: relative;
  	  	text-align: center;
  	  	height: 28px;
  	  }
  
  #pagination.el-pagination /deep/ .el-pagination__jump .el-input .el-input__inner {
  	  	border: 1px solid #DCDFE6;
  	  	cursor: pointer;
  	  	padding: 0 3px;
  	  	color: #606266;
  	  	display: inline-block;
  	  	font-size: 14px;
  	  	line-height: 28px;
  	  	border-radius: 3px;
  	  	outline: 0;
  	  	background: #FFF;
  	  	width: 100%;
  	  	text-align: center;
  	  	height: 28px;
  	  }
</style>
