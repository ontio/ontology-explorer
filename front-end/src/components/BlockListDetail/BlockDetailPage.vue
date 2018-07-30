<template>
  <div class="container container-margin-top">
    <div class="div-block-detail-page form-group">
      <div class="row">
        <div class="col-lg-6">
          <p  class="title-more float-left block-detail-page-check-hand font-Regular font-Regular normal_color font-size18" @click="toReturn"><<{{ $t('all.return') }}</p>
        </div>
      </div>
      <div class="row">
        <div class="col-lg-12">
          <p  class="text-center font-size40 font-ExtraLight p_margin_bottom_L normal_color" >BLOCK DETAILS</p>
        </div>
      </div>
      <div class="row">
        <div class="col-lg-6">
          <p class="font-size24 important_color font-blod">{{ $t('blockDetail.name') + "  " + blockDetailPage.info.Height }}</p>
        </div>
      </div>
      <table class="table table-hover">
        <thead>
        <tr>
        </tr>
        </thead>
        <tbody>
        <tr >
          <td class="td12 font-size24 normal_color font-Regular td_height">
            Block Size: {{blockDetailPage.info.BlockSize}} byte
          </td>
          <td class="td12  td_height">
            <p class="font-size24 normal_color font-Regular p_margin_bottom">BookKeeper: </p>
            <p class="font-size14 normal_color font-Regular p_margin_bottom">{{blockDetailPage.info.BookKeeper}}</p>
          </td>
        </tr>
        </tbody>
      </table>
      <table class="table table-hover">
        <thead>
        <tr>
        </tr>
        </thead>
        <tbody>
        <tr >
          <td class="td11 font-size24 normal_color font-Regular td_height p_margin_bottom">
            Time: {{getDate(blockDetailPage.info.BlockTime)}}
          </td>
        </tr>
        </tbody>
      </table>
      <table class="table table-hover">
        <thead>
        <tr>
        </tr>
        </thead>
        <tbody>
        <tr >
          <td class="td11 td_height">
            <p class="font-size24 normal_color font-Regular p_margin_bottom ">Hash:</p>
            <p class="font-size14 normal_color font-Regular p_margin_bottom"> {{blockDetailPage.info.Hash}}</p>
          </td>
        </tr>
        </tbody>
      </table>
      <table class="table table-hover">
        <thead>
        <tr>
        </tr>
        </thead>
        <tbody>
        <tr >
          <td class="td23 td_height">
            <p class="font-size24 normal_color font-Regular p_margin_bottom">Previous Block:</p>
            <p class="font-size14 important_color font-Regular p_margin_bottom pointer click_able" @click="toBlockDetailPage(blockDetailPage.info.Height-1)"  > {{blockDetailPage.info.PrevBlock}}</p>
          </td>
          <td class="td13 td_height">
            <p class="font-size24 normal_color font-Regular p_margin_bottom">Next Block:</p>
            <p v-if="blockDetailPage.info.NextBlock" class="font-size14 important_color font-Regular p_margin_bottom pointer click_able" @click="toBlockDetailPage(blockDetailPage.info.Height+1)"> {{blockDetailPage.info.NextBlock.substr(0,35)}}...</p>
            <p v-else class="font-size14 normal_color font-Regular p_margin_bottom"> none</p>
          </td>
        </tr>
        </tbody>
      </table>
      <table class="table table-hover">
        <thead>
        <tr>
        </tr>
        </thead>
        <tbody>
        <tr >
          <td class="td11">
            <p class="font-size24 f_color font-Regular p_margin_bottom">Merkle Root:</p>
            <p class="font-size14 f_color font-Regular p_margin_bottom"> {{blockDetailPage.info.TxnsRoot}}</p>
            <p class="font-size24 f_color font-Regular p_margin_bottom">ConsensusData:</p>
            <p class="font-size14 f_color font-Regular p_margin_bottom"> {{blockDetailPage.info.ConsensusData}}</p>
          </td>
        </tr>
        </tbody>
      </table>
      <table v-if="blockDetailPage.info.TxnNum != 0" class="table ">
        <thead>
        <tr style="border-bottom:0px;">
          <td class="font-size24 normal_color font-Blod td_height2">
            Transactions on this block:
          </td>
        </tr>
        </thead>
        <thead>
        <tr>
          <td class="td-tx-head font-size18 normal_color font-Blod td_height3">
            TxnHash
          </td>
          <td class="td-tx-head font-size18 normal_color font-Blod td_height3">
            Status
          </td>
          <td class="td-tx-head font-size18 normal_color font-Blod td_height3">
            TxnTime
          </td>
        </tr>
        </thead>
        <tbody>
        <tr v-for="tx in blockDetailPage.info.TxnList">
          <td class="font-size14 f_color font-Regular td_height3 click_able" @click="toTransactionDetailPage(tx.TxnHash)">
            {{tx.TxnHash}}
          </td>
          <td class="font-size14 s_color font-Regular td_height3" v-if="tx.ConfirmFlag == 1">
            Confirmed
          </td>
          <td class="font-size14 f_color font-Regular td_height3" v-else>
            Failed
          </td>
          <td class="font-size14 normal_color font-Regular td_height3">
            {{getDate(tx.TxnTime)}}
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <div class="row justify-content-center">
      <div id="page" v-show="blockDetailPage.info.TxnTotal > 10">
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
      name: "block-detail-page",

    data() {
      return {
      }
    },
    created() {
      this.getBlockDetailPage()
    },
    watch: {
      '$route': 'getBlockDetailPage'
    },
    computed: {
      ...mapState({
        blockDetailPage: state => state.BlockDetailPage.BlockDetail,
      })
    },
    methods: {
      getBlockDetailPage() {
        this.$store.dispatch('getBlockDetailPage',this.$route.params).then(response => {
          /* console.log(response) */
        }).catch(error => {
          console.log(error)
        })
      },
      toReturn(){
        this.$router.go(-1)
      },
      toBlockDetailPage($blockHeight){
        if(this.$route.params.net == undefined){
          this.$router.push({ name:'blockDetail', params:{param:$blockHeight}})
        }else{
          this.$router.push({ name:'blockDetailTest', params:{param:$blockHeight,net:'testnet'}})
        }

      },
      getTime($time){
        return Helper.getDateTime($time)
      },
      getDate($time){
        return Helper.getDate($time)
      },
      toTransactionDetailPage($TxnId){
        if(this.$route.params.net == undefined){
          this.$router.push({ name:'TransactionDetail', params:{txnHash:$TxnId}})
        }else{
          this.$router.push({ name:'TransactionDetailTest', params:{txnHash:$TxnId,net:'testnet'}})
        }
      },
    }
  }
</script>

<style scoped>
  .div-block-detail-page {
    /* border: 1px solid rgba(0, 0, 0, 0.1); */
    border-radius: 0.25rem;
    padding: 15px;
  }

  .block-detail-page-msg {
    border: 1px solid rgba(0, 0, 0, 0.1);
    margin-top: 10px;
    border-radius: 0.25rem;
    padding: 15px;
  }
  .block-detail-page-txt {
    margin-left: 15px;
    margin-bottom: 14px;
    line-height:1.6;
  }
  .block-detail-page-check-hand{
    cursor: pointer;
  }
</style>
