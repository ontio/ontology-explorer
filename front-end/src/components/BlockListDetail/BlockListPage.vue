<template>
  <div class="container container-margin-top">
    <div class="div-block-list-page form-group">
      <div class="row">
        <div class="col-lg-6">
          <p  class="title-more  float-left font-Regular normal_color font-size18 block-detail-page-check-hand" @click="toReturn"><< {{ $t('all.return') }}</p>
        </div>
      </div>
      <div class="row">
        <div class="col-lg-12">
          <p  class="text-center font-size40 font-ExtraLight p_margin_bottom_L normal_color" >BLOCKS</p>
        </div>
      </div>

      <div class="row justify-content-center">
        <div class="col-lg-12">
          <table class="table table-hover">
            <thead>
            <tr>
              <th class="blp-ab-border-top-none font-size18" style="padding-top:34px;" scope="col">{{ $t('blockList.Height') }}</th>
              <th class="blp-ab-border-top-none font-size18" scope="col">{{ $t('blockList.TxnNum') }}</th>
              <th class="blp-ab-border-top-none font-size18" scope="col">{{ $t('blockList.BlockSize') }}</th>
              <th class="blp-ab-border-top-none font-size18" scope="col">{{ $t('blockList.BlockTime') }}</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="block in blockListDetail.info">
              <td class="font-size14 font-Regular important_color td_height3 click_able"  @click="toBlockDetailPage(block.Height)">{{block.Height}}</td>
              <td class="font-size14 font-Regular normal_color td_height3">{{block.TxnNum}}</td>
              <td class="font-size14 font-Regular normal_color td_height3">{{block.BlockSize}}</td>
              <td class="font-size14 font-Regular normal_color td_height3">{{getDate(block.BlockTime)}}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <div class="row justify-content-center">
      <div id="page" v-show="blockListDetail.allPage!=1">
        <ul class="pagination"  >
          <li class="transaction-list-page-check-hand padding0" @click="goToPage(blockListDetail.firstPage)" ><button class="goto_btn"><a>{{$t('page.First')}}</a> </button></li>
          <li class="transaction-list-page-check-hand padding0" @click="goToPage(blockListDetail.lastPage)"><button style="border-left:0px" class="goto_btn"><a>{{$t('page.PreviousPage')}}</a></button></li>
          <li class="transaction-list-page-check-hand padding0" @click="goToPage(blockListDetail.nextPage)"><button style="border-left:0px" class="goto_btn"><a>{{$t('page.NextPage')}}</a></button></li>
          <li class="transaction-list-page-check-hand  padding0" @click="goToPage(blockListDetail.finalPage)" ><button style="border-left:0px" class="goto_btn"><a>{{$t('page.Last')}}</a></button> </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapState} from 'vuex'
  import Helper from './../../helpers/helper.js'

  export default {
      name: "block-list-page",

    data() {
      return {
      }
    },
    created() {
      this.getBlockListPage()
    },
    watch: {
      '$route': 'getBlockListPage'
    },
    computed: {
      ...mapState({
        blockListDetail: state => state.BlockListPage.BlockListDetail,
      }),
    },
    methods: {
      getBlockListPage() {
        this.$store.dispatch('getBlockListPage',this.$route.params).then(response => {
          /* console.log(response) */
        }).catch(error => {
          console.log(error)
        })
      },
      toReturn(){
        this.$router.push({ name:'Home'})
      },
      goToPage($Page){
        this.$router.push({ name:'blockListDetail', params:$Page})
      },
      toBlockDetailPage($blockHeight){
        this.$router.push({ name:'blockDetail', params:{param:$blockHeight}})
      },
      getTime($time){
        return Helper.getDateTime($time)
      },
      getDate($time){
        return Helper.getDate($time)
      },
    }
  }
</script>

<style scoped>
  .div-block-list-page {
    /* border: 1px solid rgba(0, 0, 0, 0.1); */
    border-radius: 0.25rem;
    padding: 15px;
  }
  .block-list-page-hr {
    height: 1px;
  }
  .blp-ab-border-top-none{
    border-top: none;
  }
  .block-list-page-underline{
    cursor: pointer;
    text-decoration:underline;
  }
  .block-list-page-check-hand{
    cursor: pointer;
  }
</style>
