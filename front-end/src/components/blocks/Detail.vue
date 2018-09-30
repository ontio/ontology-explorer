<template>
  <div class="container container-margin-top">
    <return-home></return-home>
    <list-title :name="$t('blockDetail.nickname')"></list-title>
    <detail-title :name="$t('blockDetail.name')" :val="blockData.Height"></detail-title>

    <!--test：-->
    <!--<detail-block :params="detailParams"></detail-block>-->

    <!--区块时间和大小-->
    <detail-block-2 :name1="$t('blockDetail.BlockTime')" :val1="$HelperTools.getTransDate(blockData.BlockTime)"
                    :name2="$t('blockDetail.BlockSize')" :val2="blockData.BlockSize + ' bytes'">
    </detail-block-2>

    <!--<detail-block :params="[{name:$t('blockDetail.keeper'), val:blockData.BookKeeper, rows:2}]"></detail-block>-->
    <!--<detail-block :params="[{name:$t('blockDetail.hash'), val:blockData.Hash, rows:2}]"></detail-block>-->


    <!--<detail-block :params="[{name:$t('blockDetail.merkle'), val:blockData.TxnsRoot, rows:2},-->
      <!--{name:$t('blockDetail.Consensus'), val:blockData.ConsensusData, rows:2}]"></detail-block>-->

    <detail-block :params="detailParams" :styleVal="'new'"></detail-block>

    <!--上一个区块及下一个区块-->
    <detail-block-2 :name1="$t('blockDetail.PrevBlock')" :val1="prevBlockUrl" :rows1="'2'"
                    :params1="['block', blockData.Height-1]"
                    :name2="$t('blockDetail.NextBlock')" :val2="nextBlockUrl" :rows2="'2'"
                    :params2="nextBlockUrl !== 'Null' ? ['block', blockData.Height+1] : ''">
    </detail-block-2>

    <div class="row" v-if="blockData.TxnNum !== 0">
      <div class="col">
        <div class="detail-col">
          {{ blockData.TxnNum }}<span class="f-color"> {{ $t('blockDetail.txOnBlock') }}</span>
          <div class="table-responsive">
            <table class="table">
              <thead>
              <tr class="f-color">
                <th class="td-tx-head font-size18 font-Blod">{{ $t('all.hash') }}</th>
                <th class="td-tx-head font-size18 font-Blod">{{ $t('all.status') }}</th>
                <th class="td-tx-head font-size18 font-Blod">{{ $t('all.time') }}</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="tx in blockData.TxnList">
                <td class="font-size14 important_color font-Regular pointer" @click="toTransDetailPage(tx.TxnHash)">
                  {{tx.TxnHash.substr(0,4) + '...' + tx.TxnHash.substr(60)}}
                </td>
                <td class="font-size14 s-color font-Regular" v-if="tx.ConfirmFlag === 1">
                  Confirmed
                </td>
                <td class="font-size14 f-color font-Regular" v-else>
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

  export default {
    name: "block-detail-page",
    created() {
      this.getBlockData()
    },
    watch: {
      '$route': 'getBlockData'
    },
    computed: {
      ...mapState({
        blockData: state => state.BlockDetailPage.BlockDetail.info,
      }),
      detailParams: function () {
        return [
          {name: this.$t('blockDetail.hash'), val: this.blockData.Hash, rows: 2},
          {name: this.$t('blockDetail.keeper'), val: this.blockData.BookKeeper, rows: 2},
          {name: this.$t('blockDetail.merkle'), val: this.blockData.TxnsRoot, rows: 2},
          {name: this.$t('blockDetail.Consensus'), val: this.blockData.ConsensusData, rows: 2},
        ]
      },
      prevBlockUrl: function () {
        return typeof(this.blockData.PrevBlock) === 'undefined' ? 'Null' : this.blockData.PrevBlock.substr(0, 4) + '...' + this.blockData.PrevBlock.substr(60)
      },
      nextBlockUrl: function () {
        if (typeof(this.blockData.NextBlock) !== 'undefined') {
          if (this.blockData.NextBlock !== '') {
            return this.blockData.NextBlock.substr(0, 4) + '...' + this.blockData.NextBlock.substr(60)
          }
        }

        return 'Null'
      }
    },
    methods: {
      getBlockData() {
        this.$store.dispatch('getBlockDetailPage', this.$route.params).then()
      },
      toBlockDetailPage($blockHeight) {
        if (this.$route.params.net === 'testnet') {
          this.$router.push({name: 'blockDetailTest', params: {param: $blockHeight, net: 'testnet'}})
        } else {
          this.$router.push({name: 'blockDetail', params: {param: $blockHeight}})
        }
      },
      toTransDetailPage($TxnId) {
        if (this.$route.params.net === 'testnet') {
          this.$router.push({name: 'TransactionDetailTest', params: {txnHash: $TxnId, net: 'testnet'}})
        } else {
          this.$router.push({name: 'TransactionDetail', params: {txnHash: $TxnId}})
        }
      }
    }
  }
</script>

<style scoped>
</style>
