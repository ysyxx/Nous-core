<template>
  <div class="home-preview" :style='{"width":"100%","margin":"10px auto","flexWrap":"wrap","justifyContent":"space-between","display":"flex"}'>
    <el-row :gutter="20" style="width: 100%; margin-bottom: 20px;">
      <el-col :span="6">
        <div class="card-item">
          <i class="el-icon-user-solid"></i>
          <div class="card-text">
            <div class="card-title">用户总数</div>
            <div class="card-number">{{ userCount }}</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="card-item">
          <i class="el-icon-s-management"></i>
          <div class="card-text">
            <div class="card-title">课程总数</div>
            <div class="card-number">{{ courseCount }}</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="card-item">
          <i class="el-icon-s-comment"></i>
          <div class="card-text">
            <div class="card-title">评价总数</div>
            <div class="card-number">{{ reviewCount }}</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="card-item">
          <i class="el-icon-s-data"></i>
          <div class="card-text">
            <div class="card-title">主帖总数</div>
            <div class="card-number">{{ forumCount }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="width: 100%;">
      <el-col :span="12">
        <el-card>
          <div id="chart1" style="width:100%;height:400px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div id="chart2" style="width:100%;height:400px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  data() {
    return {
      userCount: 0,
      courseCount: 0,
      reviewCount: 0,
      forumCount: 0,
    };
  },
  mounted() {
    this.getCounts();
    this.initChart1();
    this.initChart2();
  },
  methods: {
    getCounts() {
      this.$http.get('yonghu/count').then(({ data }) => {
        if(data && data.code === 0) this.userCount = data.count;
      });
      this.$http.get('course/count').then(({ data }) => {
        if(data && data.code === 0) this.courseCount = data.count;
      });
      this.$http.get('courseAssessment/count').then(({ data }) => {
        if(data && data.code === 0) this.reviewCount = data.count;
      });
      this.$http.get('forum/count', { params: { parentid: 0 } }).then(({ data }) => {
        if(data && data.code === 0) this.forumCount = data.count;
      });
    },

    initChart1() {
      this.$http.get('common/group/courses/kechengleixing').then(({data}) => {
        if (data && data.code === 0) {
          this.$nextTick(()=>{
            const chartDom = document.getElementById('chart1');
            // 【核心修改点】增加DOM存在性检查
            if (chartDom) {
              let chartData = data.data;
              let legendData = chartData.map(item => item.kechengleixing);
              let seriesData = chartData.map(item => ({
                name: item.kechengleixing,
                value: item.total
              }));
              const chart1 = echarts.init(chartDom);
              const option = {
                title: { text: '课程类型分布', left: 'center' },
                tooltip: { trigger: 'item', formatter: '{a} <br/>{b} : {c} ({d}%)' },
                legend: { orient: 'vertical', left: 'left', data: legendData },
                series: [{
                  name: '课程数量',
                  type: 'pie',
                  radius: '55%',
                  center: ['50%', '60%'],
                  data: seriesData,
                }]
              };
              chart1.setOption(option);
            }
          });
        }
      }).catch(err => { // 【核心修改点】捕获API请求错误
        console.error("Failed to fetch data for chart1:", err);
      });
    },

    initChart2() {
      this.$http.get('common/group/course_assessment/kechengmingcheng').then(({data}) => {
        if (data && data.code === 0) {
          this.$nextTick(()=>{
            const chartDom = document.getElementById('chart2');
            // 【核心修改点】增加DOM存在性检查
            if (chartDom) {
              let chartData = data.data;
              let xAxisData = chartData.map(item => item.kechengmingcheng);
              let seriesData = chartData.map(item => item.total);
              const chart2 = echarts.init(chartDom);
              const option = {
                title: { text: '各课程评价数统计', left: 'center' },
                tooltip: { trigger: 'axis' },
                xAxis: { type: 'category', data: xAxisData, axisLabel: { interval: 0, rotate: 30 } },
                yAxis: { type: 'value' },
                grid: { left: '3%', right: '4%', bottom: '15%', containLabel: true },
                series: [{ name: '评价数', data: seriesData, type: 'bar' }]
              };
              chart2.setOption(option);
            }
          });
        }
      }).catch(err => { // 【核心修改点】捕获API请求错误
        console.error("Failed to fetch data for chart2:", err);
      });
    }
  }
};
</script>

<style lang="scss" scoped>
/* 样式保持不变 */
.home-preview {
  .card-item {
    display: flex;
    align-items: center;
    padding: 20px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
    i {
      font-size: 48px;
      color: #409EFF;
      margin-right: 20px;
    }
    .card-text {
      .card-title {
        font-size: 16px;
        color: #666;
      }
      .card-number {
        font-size: 24px;
        font-weight: bold;
        color: #333;
      }
    }
  }
  .el-card {
    border-radius: 8px;
  }
}
</style>