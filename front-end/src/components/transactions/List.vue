<template>
  <div class="container container-padding container-margin-top">
    <div class="div-transaction-list-page form-group">
      <div class="row">
        <div class="col-lg-6">
          <p  class=" font-Regular normal_color font-size18 title-more float-left block-detail-page-check-hand" @click="toReturn"><< {{ $t('all.return') }}</p>
        </div>
      </div>
      <div class="row">
        <div class="col-lg-12">
          <p  class="text-center font-size40 font-ExtraLight p_margin_bottom_L normal_color" >TRANSACTIONS</p>
        </div>
      </div>

      <div class="row justify-content-center">
        <div class="col-lg-12">
          <table class="table table-hover">
            <thead>
            <tr>
              <th class="trl-tab-border-top-none font-size18 " style="padding-top:34px;" scope="col">{{ $t('transactionList.TxnId') }}</th>
              <th class="trl-tab-border-top-none font-size18" scope="col">STAUTS</th>
              <th class="trl-tab-border-top-none font-size18" scope="col">HEIGHT</th>
              <th class="trl-tab-border-top-none font-size18" scope="col">{{ $t('transactionList.Fee') }}</th>
              <th class="trl-tab-border-top-none font-size18" scope="col">{{ $t('transactionList.TxnTime') }}</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="transaction in transactionListDetail.info">
              <!-- <td>{{getTransactionType(transaction.TxnType)}}</td> -->
              <td class="font-size14 font-Regular important_color td_height3 click_able"@click="toTransactionDetailPage(transaction.TxnHash)">{{transaction.TxnHash.substr(0,16)}}...</td>
              <td v-if="transaction.ConfirmFlag ==1" class="font-size14 font-Regular s_color td_height3">Confirmed</td>
              <td v-else class="font-size14 font-Regular f_color td_height3">Failed</td>
              <td class="font-size14 font-Regular normal_color td_height3">{{transaction.Height}}</td>
              <td class="font-size14 font-Regular normal_color td_height3">{{transaction.Fee}}</td>
              <td class="font-size14 font-Regular normal_color td_height3">{{getDate(transaction.TxnTime)}}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <div class="row justify-content-center">
      <div id="page" v-show="transactionListDetail.allPage!=1">
        <ul class="pagination"  >
          <li class="transaction-list-page-check-hand padding0" @click="goToPage(transactionListDetail.firstPage)" ><button class="goto_btn"><a>{{$t('page.First')}}</a> </button></li>
          <li class="transaction-list-page-check-hand padding0" @click="goToPage(transactionListDetail.lastPage)"><button style="border-left:0px" class="goto_btn"><a>{{$t('page.PreviousPage')}}</a></button></li>
          <li class="transaction-list-page-check-hand padding0" @click="goToPage(transactionListDetail.nextPage)"><button style="border-left:0px" class="goto_btn"><a>{{$t('page.NextPage')}}</a></button></li>
          <li class="transaction-list-page-check-hand  padding0" @click="goToPage(transactionListDetail.finalPage)" ><button style="border-left:0px" class="goto_btn"><a>{{$t('page.Last')}}</a></button> </li>
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
      name: "transaction-list-page",

    data() {
      return {
          current:1,
          showItem:5,
          allpage:1,
          allnum:'',
          size:0,
      }
    },
    created() {
      this.getTransactionListPage()
    },
    watch: {
      '$route': 'getTransactionListPage',
    },
    computed: {
      ...mapState({
        transactionListDetail: state => state.TransactionListPage.TransactionListDetail,
      }),
    },
    methods: {
      getTransactionListPage() {
        this.$store.dispatch('getTransactionListPage',this.$route.params).then(response => {
          /* console.log(response) */
        }).catch(error => {
          console.log(error)
        })
      },
      toReturn(){
        if(this.$route.params.net == undefined){
          this.$router.push({ name:'Home'})
        }else{
          this.$router.push({ name:'HomeTest', params:{net:'testnet'}})
        }
      },
      goToPage($Page){
        if(this.$route.params.net == undefined){
          this.$router.push({ name:'TransactionListDetail', params:{pageSize:$Page.pageSize,pageNumber:$Page.pageNumber}})
        }else{
          this.$router.push({ name:'TransactionListDetailTest', params:{pageSize:$Page.pageSize,pageNumber:$Page.pageNumber,net:'testnet'}})
        }
      },
      toTransactionDetailPage($TxnId){
        if(this.$route.params.net == undefined){
          this.$router.push({ name:'TransactionDetail', params:{txnHash:$TxnId}})
        }else{
          this.$router.push({ name:'TransactionDetailTest', params:{txnHash:$TxnId,net:'testnet'}})
        }
      },
      getTransactionType($case){
        return GetTransactionType.getTransactionType($case)
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
  .div-transaction-list-page {
    /* height: 600px; */
    /* border: 1px solid rgba(0, 0, 0, 0.1); */
    border-radius: 0.25rem;
    padding: 15px;
  }
  .transaction-list-page-hr {
    height: 1px;
  }
  .trl-tab-border-top-none{
    border-top: none;
  }
  .transaction-list-page-check-hand{
    cursor: pointer;
  }
  .transaction-list-page-underline{
    cursor: pointer;
    text-decoration:underline;
  }
</style>
