<template>
  <div v-if="total > 10" class="row ont-pagination-top">
    <div class="col total-data-lh">
      <span class="f-color"> {{ $t('ontPagination.total') }}</span>
      <span class="ont-blue">{{ total }}</span>
      <span class="f-color"> {{ $t('ontPagination.data') }}</span>
    </div>
    <div class="col">
      <div class="row justify-content-end">
        <el-pagination
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
          background
          layout="sizes, prev, pager, next"
          :current-page="currentPage"
          :page-size="pageSize"
          :total="total">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
	export default {
    /**
     * 绘制页数导航
     *
     * total： 总页数
     * paramVal： 可选；特殊参数的值
     */
    name: "TurnThePage",
    props: ['total', 'paramVal'],
    data() {
      return {
        pageSize: 20,
        currentPage: 0
      }
    },
    watch: {
      '$route': 'setParams',
      'total': 'setParams'
    },
    methods: {
      setParams() {
        this.currentPage = Number(this.$route.params.pageNumber);
        this.pageSize = Number(this.$route.params.pageSize)
      },
      handleCurrentChange(val) {
        let params = {pageSize: this.$route.params.pageSize, pageNumber: val};
        this.routerPush(params)
      },
      handleSizeChange(val) {
        let params = {pageSize: val, pageNumber: '1'};
        this.routerPush(params)
      },
      routerPush(params) {
        // 判断网络
        if (this.$route.params.net === 'testnet') {
          params['net'] = 'testnet'
        }

        // 判断是否有其他参数
        if (typeof(this.paramVal) !== 'undefined') {
          params[this.$route.name] = this.paramVal;
        }

        // 注意在view组件中需要用watch触发数据刷新！！
        this.$router.push({name: this.$route.name, params: params})
      }
    }
  }
</script>

<style scoped>
  .ont-pagination-top {
    margin: 15px -15px;
  }

  .total-data-lh {
    line-height: 32px;
  }
</style>
