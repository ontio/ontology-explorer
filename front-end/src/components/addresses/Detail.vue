<template>
  <div class="container container-margin-top">
    <return-home></return-home>
    <list-title :name="$t('addressDetail.name')"></list-title>
    <detail-title :name="$t('addressDetail.address')" :val="$route.params.address"></detail-title>

    <!--主要余额显示-->
    <div class="row">
      <div class="col">
        <div class="detail-col detail-col-left">
          <span class="table1_item_title font-size24 font-Regular"
                v-for="asset in AssetBalance" v-if="asset.AssetName === 'ont'">
            <span class="f-color">{{ $t('addressDetail.ontBalance') }}</span>
            <span class="important_color">{{asset.Balance}}</span>
          </span>
        </div>
      </div>
      <div class="col">
        <div class="detail-col detail-col-right">
          <span class="table1_item_title font-size24 font-Regular"
                v-for="asset in AssetBalance" v-if="asset.AssetName === 'ong'">
            <span class="f-color">{{ $t('addressDetail.ongBalance') }}</span>
            <span class="important_color">{{asset.Balance}}</span>
          </span>
        </div>
      </div>
    </div>

    <!--可领取和未领取的ONG显示-->
    <div class="row">
      <div class="col">
        <div class="detail-col">
          <p class="table1_item_title font-size24 font-Regular"
             v-for="asset in AssetBalance" v-if="asset.AssetName === 'unboundong'">
            <span class="f-color">{{ $t('addressDetail.claimable') }}</span>
            <span class="normal_color">{{asset.Balance}}</span>
          </p>
          <p class="table1_item_title font-size24 font-Regular no-margin-bottom"
             v-for="asset in AssetBalance" v-if="asset.AssetName === 'waitboundong'">
            <span class="f-color">{{ $t('addressDetail.unbound') }}</span>
            <span class="normal_color">{{asset.Balance}}</span>
          </p>
        </div>
      </div>
    </div>

    <!--交易历史-->
    <div class="row">
      <div class="col">
        <div class="detail-col">
          <div class="row font-size24 font-blod normal_color">
            <div class="col">
              {{ addressDetail.info.allPage }} {{ $t('addressDetail.txOnAddr') }}
            </div>
          </div>
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
                  <span v-if="tx.amount.ont > 0" style="color: #00AE1D">
                    {{ tx.amount.ont === 0 ? '' : tx.amount.ont + ' ONT' }}
                  </span>
                    <span v-else style="color: #32A4BE">
                    {{ tx.amount.ont === 0 ? '' : tx.amount.ont + ' ONT' }}
                  </span>

                    <span v-if="tx.amount.ont > 0 | tx.amount.ong > 0" style="color: #00AE1D">
                    {{ (tx.amount.ont !== 0 & tx.amount.ong !== 0) ? ' , ' : '' }}
                  </span>
                    <span v-else style="color: #32A4BE">
                    {{ (tx.amount.ont !== 0 & tx.amount.ong !== 0) ? ' , ' : '' }}
                  </span>

                    <span v-if="tx.amount.ong > 0" style="color: #00AE1D">
                    {{ tx.amount.ong === 0 ? '' : tx.amount.ong + ' ONG' }}
                  </span>
                    <span v-else style="color: #32A4BE">
                    {{ tx.amount.ong === 0 ? '' : tx.amount.ong + ' ONG' }}
                  </span>
                  </td>
                  <td class="font-size14 font-Regular s-color">{{ tx.ConfirmFlag === 1 ? 'Confirmed' : 'Failed' }}</td>
                  <td class="font-size14 font-Regular normal_color">{{$HelperTools.getTransDate(tx.TxnTime)}}</td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="row justify-content-center" style="margin-top: 30px;">
      <div id="page" v-show="addressDetail.info.allPage > 10">
        <ul class="pagination">
          <li class="transaction-list-page-check-hand padding0" @click="goToPage(addressDetail.info.firstPage)" ><button class="goto_btn"><a>{{$t('page.First')}}</a> </button></li>
          <li class="transaction-list-page-check-hand padding0" @click="goToPage(addressDetail.info.lastPage)"><button style="border-left:0px" class="goto_btn"><a>{{$t('page.PreviousPage')}}</a></button></li>
          <li class="transaction-list-page-check-hand padding0" @click="goToPage(addressDetail.info.nextPage)"><button style="border-left:0px" class="goto_btn"><a>{{$t('page.NextPage')}}</a></button></li>
          <li class="transaction-list-page-check-hand padding0" @click="goToPage(addressDetail.info.finalPage)" ><button style="border-left:0px" class="goto_btn"><a>{{$t('page.Last')}}</a></button> </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapState} from 'vuex'

  export default {
    name: "address-detail-page",
    data() {
      return {
        Ddo: {},
        claimflag: true,
        AssetBalance: [],
        TxnList: [],
        info: []
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
      'addressDetail.info.info': function () {
        this.info = this.addressDetail.info.info
        this.AssetBalance = this.info.AssetBalance
        this.TxnList = this.info.TxnList
      }
    },
    computed: {
      ...mapState({
        addressDetail: state => state.AddressDetailPage.AddressDetail,
      })
    },
    methods: {
      scrollTo: function (id) {
        document.getElementById(id).scrollIntoView(true);
      },
      getAddressDetailData() {
        this.$store.dispatch('getAddressDetailPage', this.$route.params).then()
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
          this.$router.push({name: 'AddressDetail', params: {address: $address, pageSize: 10, pageNumber: 1}})
        } else {
          this.$router.push({
            name: 'AddressDetailTest',
            params: {address: $address, pageSize: 10, pageNumber: 1, net: "testnet"}
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
    }
  }
</script>

<style scoped>
  .no-margin-bottom {
    margin-bottom: 0 !important;
  }
</style>
