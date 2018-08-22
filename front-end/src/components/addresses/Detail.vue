<template>
  <div id="addresstop" class="container container-margin-top">
    <div class="div-ont-id-detail-page form-group">
      <div class="row">
        <div class="col">
          <p class="title-more float-left block-detail-page-check-hand font-Regular normal_color font-size18" @click="toReturn"><< {{ $t('all.return') }}</p>
        </div>
      </div>

      <div class="row">
        <div class="col">
          <p class="text-center font-size40 font-ExtraLight normal_color">ADDRESS DETAILS</p>
        </div>
      </div>

      <!--实际地址显示-->
      <div class="row">
        <div class="col font-size24 important_color">
          <span class="font-blod">ADDRESS: </span>
          <span class="font-size14 important_color">{{$route.params.address}}</span>
        </div>
      </div>

      <!--主要余额显示-->
      <div class="row">
        <div class="col address-detail-col margin-right-2px">
          <span class="table1_item_title font-size24 font-Regular normal_color"
             v-for="asset in AssetBalance" v-if="asset.AssetName === 'ont'">
            ONT Balance: <span class="important_color">{{asset.Balance}}</span>
          </span>
        </div>
        <div class="col address-detail-col margin-left-2px">
          <span class="table1_item_title font-size24 font-Regular normal_color"
             v-for="asset in AssetBalance" v-if="asset.AssetName === 'ong'">
            ONG Balance: <span class="important_color">{{asset.Balance}}</span>
          </span>
        </div>
      </div>

      <!--可领取和未领取的ONG显示-->
      <div class="row">
        <div class="col address-detail-col-2">
          <p class="table1_item_title font-size24 font-Regular f_color"
             v-for="asset in AssetBalance" v-if="asset.AssetName === 'unboundong'">
            Claimable ONG: <span class="f_color">{{asset.Balance}}</span>
          </p>
          <p class="table1_item_title font-size24 font-Regular f_color no-margin-bottom"
             v-for="asset in AssetBalance" v-if="asset.AssetName === 'waitboundong'">
            Unbound ONG: <span class="f_color">{{asset.Balance}}</span>
          </p>
        </div>
      </div>

      <!--交易历史-->
      <div class="row margin-top-4px address-detail-table-col">
        <div class="col">
          <div class="row font-size24 font-blod normal_color">
            {{ addressDetail.info.allPage }} Transactions on this Address:
          </div>
          <div class="row table-responsive">
            <table v-if="info.TxnTotal !== 0" class="table">
              <thead>
              <tr>
                <th class="td-tx-head table3_head font-size18 font-blod normal_color">
                  HASH
                </th>
                <th class="td-tx-head table3_head font-size18 font-blod normal_color">
                  AMOUNT
                </th>
                <th class="td-tx-head table3_head font-size18 font-blod normal_color">
                  STATUS
                </th>
                <th class="td-tx-head table3_head font-size18 font-blod normal_color">
                  TIME
                </th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="tx in TxnList">
                <td class="font-size14 font-Regular f_color click_able" @click="toTransactionDetailPage(tx.TxnHash)">
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
                <td class="font-size14 font-Regular s_color">{{ tx.ConfirmFlag === 1 ? 'Confirmed' : 'Failed' }}</td>
                <td class="font-size14 font-Regular normal_color">{{getDate(tx.TxnTime)}}</td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

    <div class="row justify-content-center">
      <div id="page" v-show="addressDetail.info.allPage >10">
        <ul class="pagination"  >
          <li class="transaction-list-page-check-hand padding0" @click="goToPage(addressDetail.info.firstPage)" ><button class="goto_btn"><a>{{$t('page.First')}}</a> </button></li>
          <li class="transaction-list-page-check-hand padding0" @click="goToPage(addressDetail.info.lastPage)"><button style="border-left:0px" class="goto_btn"><a>{{$t('page.PreviousPage')}}</a></button></li>
          <li class="transaction-list-page-check-hand padding0" @click="goToPage(addressDetail.info.nextPage)"><button style="border-left:0px" class="goto_btn"><a>{{$t('page.NextPage')}}</a></button></li>
          <li class="transaction-list-page-check-hand  padding0" @click="goToPage(addressDetail.info.finalPage)" ><button style="border-left:0px" class="goto_btn"><a>{{$t('page.Last')}}</a></button> </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapState} from 'vuex'
  import Helper from './../../helpers/helper.js'

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
        /* this.info.reverse().reverse( */
        this.AssetBalance = this.info.AssetBalance
        /* this.AssetBalance.reverse().reverse() */
        this.TxnList = this.info.TxnList
        /* this.TxnList.reverse().reverse() */
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
      getGas(fee) {
        return Helper.getNormalgas(fee)
      },
      getAddressDetailData() {
        this.$store.dispatch('getAddressDetailPage', this.$route.params).then()
      },
      toReturn() {
        this.$router.go(-1)
      },
      getTime($time) {
        return Helper.getDateTime($time)
      },
      getDate($time) {
        return Helper.getDate($time)
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
      },
    }
  }
</script>

<style scoped>
  .address-detail-col,
  .address-detail-col-2 {
    width: 100%;
    /*height: 6rem;*/
    padding: 2rem;
    line-height: 2rem;
    background: white;
    margin-top: 4px;
  }

  .address-detail-col-2 {
    height: 100%;
  }

  .margin-left-2px {
    margin-left: 2px;
  }

  .margin-right-2px {
    margin-right: 2px;
  }

  .margin-top-4px {
    margin-top: 4px;
  }

  .no-margin-bottom {
    margin-bottom: 0 !important;
  }

  .address-detail-table-col {
    background: white;
    padding: 1rem 2rem;
  }
</style>
