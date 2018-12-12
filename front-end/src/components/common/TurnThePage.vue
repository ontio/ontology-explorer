<template>
  <div class="row justify-content-center turn-the-page-top">
    <div id="page">
      <ul class="pagination">
        <li class="pointer padding0" @click="goToPage(pagesInfo.firstPage)">
          <button class="goto_btn"><a>{{$t('page.First')}}</a></button>
        </li>
        <li class="pointer padding0" @click="goToPage(pagesInfo.lastPage)">
          <button class="goto_btn btn-left-0-border"><a>{{$t('page.PreviousPage')}}</a></button>
        </li>
        <li class="pointer padding0" @click="goToPage(pagesInfo.nextPage)">
          <button class="goto_btn btn-left-0-border"><a>{{$t('page.NextPage')}}</a></button>
        </li>
        <li class="pointer padding0" @click="goToPage(pagesInfo.finalPage)">
          <button class="goto_btn btn-left-0-border"><a>{{$t('page.Last')}}</a></button>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
	export default {
    /**
     * 绘制页数导航
     *
     * pagesInfo： 传递store里构造的页面数据，firstPage/lastPage等等
     * pagesName： 跳转的路由名称
     * who： 可选；标识特殊参数名称
     * paramVal： 可选；特殊参数的值
     */
    name: "TurnThePage",
    props: ['pagesInfo', 'pagesName', 'who', 'paramVal'],
    methods: {
      goToPage($Page) {
        let name = this.pagesName;
        let params = {pageSize: $Page.pageSize.toString(), pageNumber: $Page.pageNumber};

        // 判断网络
        if (this.$route.params.net === 'testnet') {
          name = name + 'Test';
          params['net'] = 'testnet'
        }

        // 判断是否有其他参数
        if (this.who !== undefined) {
          params[this.who] = this.paramVal;
        }

        // 注意在view组件中需要用watch触发数据刷新！！
        this.$router.push({name: name, params: params})
      }
    }
  }
</script>

<style scoped>
  .turn-the-page-top {
    margin-top: 30px;
  }

  .btn-left-0-border {
    border-left: 0;
  }
</style>
