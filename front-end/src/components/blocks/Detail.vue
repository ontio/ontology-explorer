<template>
  <div class="container container-margin-top">
    <return-home></return-home>
    <list-title :name="$t('blockDetail.nickname')"></list-title>

    <div class="row">
      <div class="col">
        <p class="font-size24 important_color font-blod">{{ $t('blockDetail.name') + "  " + blockDetailPage.info.Height }}</p>
      </div>
    </div>

    <div class="row">
      <div class="col">
        <div class="detail-col detail-col-left">
          <span class="f_color">{{ $t('blockDetail.BlockSize') }} </span>
          {{blockDetailPage.info.BlockSize}} {{ $t('all.byte') }}
        </div>
      </div>
      <div class="col">
        <div class="detail-col detail-col-right">
          <span class="f_color">{{ $t('blockDetail.BlockTime') }} </span>
          {{$HelperTools.getTransDate(blockDetailPage.info.BlockTime)}}
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col">
        <div class="detail-col">
          <span class="font-size24 f_color font-Regular p_margin_bottom">{{ $t('blockDetail.keeper') }}</span>
          <p class="font-size14 normal_color font-Regular p_margin_bottom">{{blockDetailPage.info.BookKeeper}}</p>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col">
        <div class="detail-col">
          <span class="font-size24 f_color font-Regular p_margin_bottom">{{ $t('blockDetail.hash') }}</span>
          <p class="font-size14 normal_color font-Regular p_margin_bottom"> {{blockDetailPage.info.Hash}}</p>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col">
        <div class="detail-col detail-col-left">
          <span class="font-size24 f_color font-Regular p_margin_bottom">{{ $t('blockDetail.PrevBlock') }}</span>
          <p class="font-size14 important_color font-Regular p_margin_bottom pointer click_able" @click="toBlockDetailPage(blockDetailPage.info.Height-1)"  > {{blockDetailPage.info.PrevBlock.substr(0,16) + '...'}}</p>
        </div>
      </div>
      <div class="col">
        <div class="detail-col detail-col-right">
          <span class="font-size24 f_color font-Regular p_margin_bottom">{{ $t('blockDetail.NextBlock') }}</span>
          <p v-if="blockDetailPage.info.NextBlock" class="font-size14 important_color font-Regular p_margin_bottom pointer click_able" @click="toBlockDetailPage(blockDetailPage.info.Height+1)"> {{blockDetailPage.info.NextBlock.substr(0,16) + '...'}}</p>
          <p v-else class="font-size14 normal_color font-Regular p_margin_bottom"> none</p>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col">
        <div class="detail-col">
          <span class="font-size24 f_color font-Regular p_margin_bottom">{{ $t('blockDetail.merkle') }}</span>
          <p class="font-size14 normal_color font-Regular p_margin_bottom"> {{blockDetailPage.info.TxnsRoot}}</p>
          <span class="font-size24 f_color font-Regular p_margin_bottom">{{ $t('blockDetail.Consensus') }}</span>
          <p class="font-size14 normal_color font-Regular p_margin_bottom"> {{blockDetailPage.info.ConsensusData}}</p>
        </div>
      </div>
    </div>

    <div class="row" v-if="blockDetailPage.info.TxnNum !== 0">
      <div class="col">
        <div class="detail-col">
          {{ blockDetailPage.info.TxnNum }} {{ $t('blockDetail.txOnBlock') }}
          <div class="table-responsive">
            <table class="table ">
              <thead>
              <tr>
                <th class="td-tx-head font-size18 normal_color font-Blod">{{ $t('all.hash') }}</th>
                <th class="td-tx-head font-size18 normal_color font-Blod">{{ $t('all.status') }}</th>
                <th class="td-tx-head font-size18 normal_color font-Blod">{{ $t('all.time') }}</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="tx in blockDetailPage.info.TxnList">
                <td class="font-size14 f_color font-Regular click_able" @click="toTransactionDetailPage(tx.TxnHash)">
                  {{tx.TxnHash.substr(0,16) + '...'}}
                </td>
                <td class="font-size14 s_color font-Regular" v-if="tx.ConfirmFlag === 1">
                  Confirmed
                </td>
                <td class="font-size14 f_color font-Regular" v-else>
                  Failed
                </td>
                <td class="font-size14 normal_color font-Regular">
                  {{$HelperTools.getTransDate(tx.TxnTime)}}
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
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
