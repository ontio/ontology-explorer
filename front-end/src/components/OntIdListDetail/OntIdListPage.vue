<template>
  <div class="container container-margin-top">
    <div class="div-ont-id-list-page form-group">
      <div class="row">
        <div class="col-lg-6">
          <p class=" font-Regular normal_color font-size18 title-more float-left block-detail-page-check-hand" @click="toReturn"><< {{ $t('all.return') }}</p>
        </div>
      </div>
      <div class="row">
        <div class="col-lg-12">
          <p  class="text-center font-size40 font-ExtraLight p_margin_bottom_L normal_color" >ONT ID EVENTS</p>
        </div>
      </div>

      <div class="row justify-content-center">
        <div class="col-lg-12">
          <table class="table table-hover">
            <thead>
            <tr>
              <th class="oil-tab-border-top-none font-size18" scope="col">{{ $t('ontIdList.TxnId') }}</th>
              <th class="oil-tab-border-top-none font-size18" scope="col">{{ $t('ontIdList.OntId') }}</th>
              <th class="oil-tab-border-top-none font-size18" scope="col">{{ $t('ontIdList.Description') }}</th>
              <th class="oil-tab-border-top-none font-size18" scope="col">{{ $t('ontIdList.height') }}</th>
              <th class="oil-tab-border-top-none font-size18" scope="col">{{ $t('ontIdList.Fee') }}</th>
              <th class="oil-tab-border-top-none font-size18" scope="col">{{ $t('ontIdList.TxnTime') }}</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="OntId in OntIdListDetail.info">
              <td class="font-size14 font-Regular normal_color td_height3 click_able" @click="toTransactionDetailPage(OntId.TxnHash)">{{OntId.TxnHash.substr(0,15)}}...</td>
              <td class="font-size14 font-Regular important_color td_height3 click_able" @click="toOntIdDetailPage(OntId.OntId)">{{OntId.OntId.substr(0,10)}}...{{OntId.OntId.substr(35,45)}}</td>
              <td class="font-size14 font-Regular normal_color td_height3">{{getOntIDEvent(OntId.Description)}}</td>
              <td class="font-size14 font-Regular normal_color td_height3">{{OntId.Height}}</td>
              <td class="font-size14 font-Regular normal_color td_height3">{{OntId.Fee}}</td>
              <td class="font-size14 font-Regular normal_color td_height3">{{getDate(OntId.TxnTime)}}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <div class="row justify-content-center">
      <div id="page" v-show="OntIdListDetail.allPage!=1">
        <ul class="pagination"  >
          <li class="transaction-list-page-check-hand padding0" @click="goToPage(OntIdListDetail.firstPage)" ><button class="goto_btn"><a>{{$t('page.First')}}</a> </button></li>
          <li class="transaction-list-page-check-hand padding0" @click="goToPage(OntIdListDetail.lastPage)"><button style="border-left:0px" class="goto_btn"><a>{{$t('page.PreviousPage')}}</a></button></li>
          <li class="transaction-list-page-check-hand padding0" @click="goToPage(OntIdListDetail.nextPage)"><button style="border-left:0px" class="goto_btn"><a>{{$t('page.NextPage')}}</a></button></li>
          <li class="transaction-list-page-check-hand  padding0" @click="goToPage(OntIdListDetail.finalPage)" ><button style="border-left:0px" class="goto_btn"><a>{{$t('page.Last')}}</a></button> </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapState} from 'vuex'
  import Helper from './../../helpers/helper.js'
  import GetTransactionType from './../../common/OntMsg/GetTransactionType.js'

  export default {
      name: "ont-id-list-page",

    data() {
      return {
      }
    },
    created() {
      this.getOntIdListPage()
    },
    watch: {
      '$route': 'getOntIdListPage'
    },
    computed: {
      ...mapState({
        OntIdListDetail: state => state.OntIdListPage.OntIdListDetail,
      })
    },
    methods: {
      getOntIdListPage() {
        this.$store.dispatch('getOntIdListPage',this.$route.params).then(response => {
          /* console.log(response) */
        }).catch(error => {
          console.log(error)
        })
      },
      toReturn(){
        this.$router.push({ name:'Home'})
      },
      goToPage($Page){
        this.$router.push({ name:'OntIdListDetail', params:$Page})
      },
      getTime($time){
        return Helper.getDateTime($time)
      },
      getDate($time){
        return Helper.getDate($time)
      },
      getTransactionType($case){
        return GetTransactionType.getTransactionType($case)
      },
      toTransactionDetailPage($TxnId){
        this.$router.push({ name:'TransactionDetail', params:{txnHash:$TxnId}})
      },
      toOntIdDetailPage($OntId){
        this.$router.push({ name:'OntIdDetail', params:{ontid:$OntId}})
      },
      getOntIDEvent:function($event){
             switch ($event.substr(0,12)) {
              case "register Ont":
                return "Register ONT ID"
              case "add publicKe":
                return "Add publickey"
              case "delete publi":
                return "Delete publickey"
              case "add attribut":
                return "Add identity attribute"
              case "update attri":
                return "Update identity attribute"
              case "delete attri":
                return "Delete identity attribute"
            }
      }
    }
  }
</script>

<style scoped>
  .div-ont-id-list-page {
    /* border: 1px solid rgba(0, 0, 0, 0.1); */
    border-radius: 0.25rem;
    padding: 15px;
  }
  .ont-id-list-page-hr {
    height: 1px;
  }
  .oil-tab-border-top-none{
    border-top: none;
  }
  .ont-id-list-underline{
    cursor: pointer;
    text-decoration:underline;
  }
  .ont-id-list-check-hand{
    cursor: pointer;
  }
</style>
