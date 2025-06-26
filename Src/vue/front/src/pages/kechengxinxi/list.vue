<template>
  <div class="list-body">
    <div class="search-container">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="课程名称">
          <el-input v-model="searchForm.title" placeholder="请输入课程名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="课程类型" v-if="kechengleixingOptions.length > 0">
          <el-select v-model="searchForm.kechengleixing" placeholder="请选择课程类型" clearable>
            <el-option
                v-for="(item, index) in kechengleixingOptions"
                :key="index"
                :label="item"
                :value="item">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search" icon="el-icon-search">查询</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="list-container">
      <el-row :gutter="20">
        <el-col :span="6" v-for="item in dataList" :key="item.id" class="list-item-col">
          <div class="list-item" @click="toDetail(item.id)">
            <img :src="item.coverImageUrl" alt="课程封面" class="item-image" v-if="item.coverImageUrl">
            <div class="item-info">
              <div class="item-title">{{ item.title }}</div>
              <div class="item-meta">
                <span><i class="el-icon-folder"></i> {{ item.kechengleixing }}</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <div class="pager-container">
      <el-pagination
          @size-change="sizeChangeHandle"
          @current-change="currentChangeHandle"
          :current-page="pageIndex"
          :page-sizes="[8, 12, 16, 20]"
          :page-size="pageSize"
          :total="totalPage"
          layout="total, sizes, prev, pager, next, jumper"
          class="pager">
      </el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // 课程类型选项
      kechengleixingOptions: [],
      // 搜索表单
      searchForm: {
        title: '', // 【修改点】搜索字段改为 title
        kechengleixing: '',
      },
      // 数据列表
      dataList: [],
      // 分页相关
      pageIndex: 1,
      pageSize: 8,
      totalPage: 0,
      layouts: '',
    };
  },
  methods: {
    // 获取课程列表数据
    getDataList() {
      // 【核心修改点】API接口地址改为 /courses/page
      this.$http.get('courses/page', {
        params: {
          page: this.pageIndex,
          limit: this.pageSize,
          // 传递搜索参数
          title: this.searchForm.title,
          kechengleixing: this.searchForm.kechengleixing,
        }
      }).then(res => {
        if (res.data.code == 0) {
          this.dataList = res.data.data.list;
          this.totalPage = res.data.data.total;
        } else {
          this.dataList = [];
          this.totalPage = 0;
        }
      });
    },
    // 获取课程类型用于搜索下拉框
    getKechengleixingList() {
      this.$http.get('kechengleixing/list').then(res => {
        if (res.data.code == 0) {
          this.kechengleixingOptions = res.data.data.map(item => item.kechengleixing);
        }
      });
    },
    // 执行搜索
    search() {
      this.pageIndex = 1;
      this.getDataList();
    },
    // 跳转到详情页
    toDetail(id) {
      // 【核心修改点】路由地址改为 /courses/detail
      this.$router.push({ path: '/courses/detail', query: { id: id }});
    },
    // 分页大小改变
    sizeChangeHandle(val) {
      this.pageSize = val;
      this.pageIndex = 1;
      this.getDataList();
    },
    // 当前页改变
    currentChangeHandle(val) {
      this.pageIndex = val;
      this.getDataList();
    },
  },
  created() {
    // 页面加载时，获取课程类型和第一页的数据
    this.getKechengleixingList();
    this.getDataList();
  }
};
</script>

<style lang="scss" scoped>
/* 样式可以根据需要进行微调 */
.list-body {
  width: 100%;
  margin: 0 auto;
}
.search-container {
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  margin-bottom: 20px;
}
.list-container {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
}
.list-item-col {
  margin-bottom: 20px;
}
.list-item {
  border: 1px solid #EBEEF5;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}
.list-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 20px 0 rgba(0,0,0,0.15);
}
.item-image {
  width: 100%;
  height: 150px;
  object-fit: cover;
}
.item-info {
  padding: 14px;
}
.item-title {
  font-size: 16px;
  color: #303133;
  font-weight: bold;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.item-meta {
  margin-top: 10px;
  font-size: 12px;
  color: #909399;
}
.pager-container {
  text-align: center;
  margin-top: 20px;
}
</style>