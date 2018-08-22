<template>
  <div class="container container-margin-top">
    <return-home></return-home>
    <list-title :name="$t('blockDetail.nickname')"></list-title>

    <div class="row">
      <div class="col">
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
          Time: {{$HelperTools.getTransDate(blockDetailPage.info.BlockTime)}}
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
          {{$HelperTools.getTransDate(tx.TxnTime)}}
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
  import {mapState} from 'vuex'
  import ReturnHome from '../common/ReturnHome'
  import ListTitle from '../common/ListTitle'

  export default {
    name: "block-detail-page",
    components: {ReturnHome, ListTitle},
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
        this.$store.dispatch('getBlockDetailPage', this.$route.params).then()
      },
      toBlockDetailPage($blockHeight) {
        if (this.$route.params.net == undefined) {
          this.$router.push({name: 'blockDetail', params: {param: $blockHeight}})
        } else {
          this.$router.push({name: 'blockDetailTest', params: {param: $blockHeight, net: 'testnet'}})
        }
      },
      toTransactionDetailPage($TxnId) {
        if (this.$route.params.net == undefined) {
          this.$router.push({name: 'TransactionDetail', params: {txnHash: $TxnId}})
        } else {
          this.$router.push({name: 'TransactionDetailTest', params: {txnHash: $TxnId, net: 'testnet'}})
        }
      }
    }
  }
</script>

<style scoped>
</style>
