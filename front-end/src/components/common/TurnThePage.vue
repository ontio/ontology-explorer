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
    name: "TurnThePage",
    props: ['pagesInfo', 'pagesName', 'who', 'params'],
    methods: {
      goToPage($Page) {
        if (this.$route.params.net === 'testnet') {
          this.$router.push({
            name: this.pagesName + 'Test',
            params: {pageSize: $Page.pageSize, pageNumber: $Page.pageNumber, net: 'testnet'}
          })
        } else {
          this.$router.push({
            name: this.pagesName,
            params: {pageSize: $Page.pageSize, pageNumber: $Page.pageNumber}
          })
        }
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
