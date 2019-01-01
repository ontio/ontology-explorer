<template>
  <div class="container container-margin-top">
    <list-title :name="$t('addressDetail.name')"></list-title>
    <detail-title :name="$t('addressDetail.address')" :val="$route.params.address"></detail-title>

    <!--主要余额显示-->
    <detail-block-2 :name1="$t('addressDetail.ontBalance')" :val1="assetsVal.ont" :rows1="'1.3'"
                    :name2="$t('addressDetail.ongBalance')" :val2="assetsVal.ong" :rows2="'1.3'">
    </detail-block-2>

    <!--可领取和未领取的ONG显示-->
    <div class="detail-col">
      <div class="row">
        <div class="col table1_item_title">
          <span class="f-color">{{ $t('addressDetail.claimable') }}</span>
          <span class="important_color">{{assetsVal.unboundong}}</span>
        </div>
      </div>
      <div class="row table1_item_title">
        <div class="col">
          <span class="f-color">{{ $t('addressDetail.unbound') }}</span>
          <span class="important_color">{{assetsVal.waitboundong}}</span>
        </div>
      </div>
    </div>

    <!--2018年万圣节南瓜活动资产-->
    <div class="row" v-if="havePumpkin">
      <div class="col">
        <div class="detail-col">
          {{ $t('addressDetail.oep8Assets') }}
          <div class="row pumpkin-color font-size14 text-center" style="margin-top: 20px">
            <div class="col">
              <div>{{ $t('assetName.pumpkin08' ) }}</div>
              <div class="font-size24">{{assetsVal.pumpkin08}}</div>
            </div>
            <div class="col">
              <div>{{ $t('assetName.pumpkin01' ) }}</div>
              <div class="font-size24">{{assetsVal.pumpkin01}}</div>
            </div>
            <div class="col">
              <div>{{ $t('assetName.pumpkin02' ) }}</div>
              <div class="font-size24">{{assetsVal.pumpkin02}}</div>
            </div>
            <div class="col">
              <div>{{ $t('assetName.pumpkin03' ) }}</div>
              <div class="font-size24">{{assetsVal.pumpkin03}}</div>
            </div>
          </div>

          <div class="row pumpkin-color font-size14 text-center" style="margin-top: 20px">
            <div class="col">
              <div>{{ $t('assetName.pumpkin04' ) }}</div>
              <div class="font-size24">{{assetsVal.pumpkin04}}</div>
            </div>
            <div class="col">
              <div>{{ $t('assetName.pumpkin05' ) }}</div>
              <div class="font-size24">{{assetsVal.pumpkin05}}</div>
            </div>
            <div class="col">
              <div>{{ $t('assetName.pumpkin06' ) }}</div>
              <div class="font-size24">{{assetsVal.pumpkin06}}</div>
            </div>
            <div class="col">
              <div>{{ $t('assetName.pumpkin07' ) }}</div>
              <div class="font-size24">{{assetsVal.pumpkin07}}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!--有其他的OEP-4/5资产-->
    <div class="row" v-if="haveOtherOep">
      <div class="col">
        <div class="detail-col">
          <span class="font-blod table1_item_title">{{ $t('addressDetail.oepOtherAssets') }}</span>
          <div v-for="(asset,index) in AssetBalance" v-if="index > 12" class="row font-size14 oep-4-5-div">
            <div class="table1_item_title font-Regular">
              <span class="f-color">{{ asset.AssetName.toUpperCase() + ": " }}</span>
              <span class="important_color">{{parseFloat(asset.Balance)}}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!--交易历史-->
    <div class="row">
      <div class="col">
        <div class="detail-col">
          <div class="row font-size18 font-blod normal_color">
            <div class="col">
              <!--{{ addressDetail.allPage }} {{ $t('addressDetail.txOnAddr') }}-->
              {{ $t('addressDetail.txns') }}
            </div>
            <div class="col">
              <to-excel :address="$route.params.address"></to-excel>
            </div>
          </div>

          <ont-pagination :total="addressDetail.total"></ont-pagination>

          <div class="row table-responsive">
            <div class="col">
              <table v-if="info.TxnTotal !== 0" class="table">
                <thead>
                <tr>
                  <th class="td-tx-head table3_head font-size18 font-blod normal_color">{{$t('all.hash')}}</th>
                  <th class="td-tx-head table3_head font-size18 font-blod normal_color">{{$t('all.amount')}}</th>
                  <th class="td-tx-head table3_head font-size18 font-blod normal_color">{{$t('all.status')}}</th>
                  <th class="td-tx-head table3_head font-size18 font-blod normal_color">{{$t('all.time')}}</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="tx in TxnList">
                  <td class="font-size14 font-Regular f-color pointer" @click="toTransactionDetailPage(tx.TxnHash)">
                    {{tx.TxnHash.substr(0,16) + '...'}}
                  </td>

                  <td class="font-size14 font-Regular">
                    <span v-for="(tl,index) in tx.TransferList">
                      <!--支出-->
                      <span class="expenditure-color" v-if="tl.FromAddress === $route.params.address">
                        <span v-if="tl.AssetName.indexOf('pumpkin') > -1">
                          {{ '-' + $HelperTools.toFinancialVal(tl.Amount) + ' ' + $t('assetName.' + tl.AssetName ) }}
                        </span>
                        <span v-else>
                          {{ '-' + $HelperTools.toFinancialVal(tl.Amount) + ' ' + tl.AssetName.toUpperCase() }}
                        </span>
                      </span>
                      <!--收入-->
                      <span class="income-color" v-else>
                        <span v-if="tl.AssetName.indexOf('pumpkin') > -1">
                          {{ $HelperTools.toFinancialVal(tl.Amount) + ' ' + $t('assetName.' + tl.AssetName ) }}
                        </span>
                        <span v-else>
                          {{ $HelperTools.toFinancialVal(tl.Amount) + ' ' + tl.AssetName.toUpperCase() }}
                        </span>
                      </span>

                      <!--逗号分隔符-->
                      <span v-if="index !== tx.TransferList.length - 1"
                            :class="tl.FromAddress === $route.params.address ? 'expenditure-color' : 'income-color'">
                        {{ ', ' }}
                      </span>
                    </span>
                  </td>
                  <td class="font-size14 font-Regular s-color">{{ tx.ConfirmFlag === 1 ? 'Confirmed' : 'Failed' }}</td>
                  <td class="font-size14 font-Regular normal_color">{{$HelperTools.getTransDate(tx.TxnTime)}}</td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>

          <ont-pagination :total="addressDetail.total"></ont-pagination>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapState} from 'vuex'
  import ToExcel from './../common/DownloadExcel'

  export default {
    data() {
      return {
        Ddo: {},
        claimflag: true,
        AssetBalance: [],
        havePumpkin: false, // 标识是否显示2018年万圣节南瓜资产
        haveOtherOep: false, // 标识是否显示OEP-4/5资产
        TxnList: [],
        info: [],
        tmpDown: ''
      }
    },
    created() {
      if (this.$route.params.pageSize == undefined || this.$route.params.pageNumber == undefined) {
        this.toAddressDetailPage(this.$route.params.address)
      } else {
        this.getAddressDetailData()
      }
    },
    watch: {
      '$route': 'getAddressDetailData',
      'addressDetail': function () {
        this.info = this.addressDetail.list;
        this.AssetBalance = this.info.AssetBalance;
        if (this.info.AssetBalance.length > 4) { // 有南瓜资产
          this.havePumpkin = (this.info.AssetBalance[12].Balance !== '0' && this.info.AssetBalance[12].Balance !== 0)
        }
        if (this.info.AssetBalance.length > 13) { // 有OEP4、5的其他资产
          this.haveOtherOep = true
        }
        this.TxnList = this.info.TxnList;
      }
    },
    computed: {
      ...mapState({
        addressDetail: state => state.Addresses.Detail
      }),
      /**
       * 取出全部资产名称和值
       */
      assetsVal: function () {
        let retAssets = {};

        for (let index in this.AssetBalance) {
          retAssets[this.AssetBalance[index].AssetName] = this.$HelperTools.toFinancialVal(this.AssetBalance[index].Balance)
        }

        return retAssets
      }
    },
    methods: {
      getAddressDetailData() {
        this.$store.dispatch('GetAddressDetail', this.$route.params).then()
      },
      toReturn() {
        this.$router.go(-1)
      },
      toTransactionDetailPage($TxnId) {
        if (this.$route.params.net == undefined) {
          this.$router.push({name: 'TransactionDetail', params: {txnHash: $TxnId}})
        } else {
          this.$router.push({name: 'TransactionDetailTest', params: {txnHash: $TxnId, net: "testnet"}})
        }
      },
      toAddressDetailPage($address) {
        if (this.$route.params.net == undefined) {
          this.$router.push({name: 'AddressDetail', params: {address: $address, pageSize: 20, pageNumber: 1}})
        } else {
          this.$router.push({
            name: 'AddressDetailTest',
            params: {address: $address, pageSize: 20, pageNumber: 1, net: "testnet"}
          })
        }
      },
      goToPage($Page) {
        var address = this.$route.params.address
        if (this.$route.params.net == undefined) {
          this.$router.push({
            name: 'AddressDetail',
            params: {address: address, pageSize: $Page.pageSize, pageNumber: $Page.pageNumber}
          })
        } else {
          this.$router.push({
            name: 'AddressDetailTest',
            params: {address: address, pageSize: $Page.pageSize, pageNumber: $Page.pageNumber, net: 'testnet'}
          })
        }
        this.getAddressDetailData()
      }
    },
    components: {ToExcel}
  }
</script>

<style scoped>
  .income-color {
    color: #00AE1D;
  }

  .expenditure-color {
    color: #32A4BE;
  }

  .oep-4-5-div {
    margin-top: 15px;
    padding: 0 15px;
  }

  .table1_item_title {
    font-size: 18px;
  }

  @media screen and (max-width: 768px) {
    .table1_item_title {
      font-size: 14px;
    }
  }
</style>
