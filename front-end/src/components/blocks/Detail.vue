<template>
  <div class="e-container container-margin-top">
    <list-title :name="$t('blockDetail.nickname')"></list-title>
    <detail-title :name="$t('blockDetail.name')" :val="block.Height"></detail-title>

    <!--区块时间和大小-->
    <detail-block-2 :name1="$t('blockDetail.BlockTime')" :val1="$HelperTools.getTransDate(block.BlockTime)" :rows1="'1.1'"
                    :name2="$t('blockDetail.BlockSize')" :val2="block.BlockSize + ' bytes'" :rows2="'1.1'">
    </detail-block-2>

    <detail-block :params="detailParams" :styleVal="'new'"></detail-block>

    <!--上一个区块及下一个区块-->
    <detail-block-2 :name1="$t('blockDetail.PrevBlock')" :val1="prevBlockUrl" :rows1="'2'"
                    :params1="['block', block.Height-1]"
                    :name2="$t('blockDetail.NextBlock')" :val2="nextBlockUrl" :rows2="'2'"
                    :params2="nextBlockUrl !== 'Null' ? ['block', block.Height+1] : ''">
    </detail-block-2>

    <div class="row" v-if="block.TxnNum !== 0">
      <div class="col">
        <div class="detail-col">
          {{ block.TxnNum }}<span class="f-color"> {{ $t('blockDetail.txOnBlock') }}</span>
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
              <tr v-for="tx in block.TxnList">
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
    created() {
      this.getBlock()
    },
    watch: {
      '$route': 'getBlock'
    },
    computed: {
      ...mapState({
        block: state => state.Blocks.Detail,
      }),
      detailParams: function () {
        return [
          {name: this.$t('blockDetail.hash'), val: this.block.Hash, rows: 2},
          {name: this.$t('blockDetail.keeper'), val: this.block.BookKeeper, rows: 2},
          {name: this.$t('blockDetail.merkle'), val: this.block.TxnsRoot, rows: 2},
          {name: this.$t('blockDetail.Consensus'), val: this.block.ConsensusData, rows: 2},
        ]
      },
      prevBlockUrl: function () {
        return typeof(this.block.PrevBlock) === 'undefined' ? 'Null' : this.block.PrevBlock.substr(0, 4) + '...' + this.block.PrevBlock.substr(60)
      },
      nextBlockUrl: function () {
        if (typeof(this.block.NextBlock) !== 'undefined') {
          if (this.block.NextBlock !== '') {
            return this.block.NextBlock.substr(0, 4) + '...' + this.block.NextBlock.substr(60)
          }
        }

        return 'Null'
      }
    },
    methods: {
      getBlock() {
        this.$store.dispatch('GetBlock', this.$route.params).then()
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
