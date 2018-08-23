<template>
  <div class="container margin-top-60">
    <div class="pc-display">
      <return-home></return-home>
      <list-title :name="$t('transactionDetail.name')"></list-title>

      <!-- Transaction Detail Basic Info: -->
      <div class="row">
        <p class="wordbreak font-size24 color32a4be font-blod important_color">Transaction Hash: <span
          class="font-size14">{{ transactionDetail.info.TxnHash }}</span></p>
      </div>
      <div class="row">
        <div class="col detail-col">
          Transaction Time: {{$HelperTools.getTransDate(transactionDetail.info.TxnTime)}}
        </div>
      </div>
      <div class="row">
        <div class="col detail-col">
          <span v-if="transactionDetail.info.TxnType !== 209">Type: Deploy Smart Contract</span>
          <span v-else>Type: Smart Contract</span>
        </div>
      </div>
      <div class="row">
        <div class="col detail-col" @click="toBlockDetailPage(transactionDetail.info.Height)">
          Block Height: <span class="click_able important_color">{{transactionDetail.info.Height}}</span>
        </div>
        <div class="col detail-col detail-col-middle">Fee: {{Number(transactionDetail.info.Fee)}} ONG</div>
        <div class="col detail-col">
          Status:
          <span v-if="transactionDetail.info.ConfirmFlag === 1" style="color:#00AE1D">Confirmed</span>
          <span v-else style="color:#AFACAC">Failed</span>
        </div>
      </div>

      <!-- Transaction Detail Data Info: -->
      <table v-if="authflag" class="table table-hover">
        <thead>
        <tr>
        </tr>
        </thead>
        <tbody>
        <tr>
          <td class="td11 td_height" style="padding: 34px 24px;">
            <p class="font-size24  p_margin_bottom n_color font-Regular">Description:
              {{transactionDetail.info.Description}}</p>
          </td>
        </tr>
        </tbody>
      </table>

      <!--展示Issuer OntId和Description的数据块-->
      <div class="row detail-ont-id-desc-tit font-Regular" v-if="recordflag">
        <div class="col">
          <div class="row font-size24">Issuer OntId:</div>
          <div class="row font-size14 detail-ont-id-desc-txt">{{transactionDetail.info.Description.substr(12,42)}}</div>
          <div class="row font-size24 margin-top-15">Description:</div>
          <div class="row font-size14 detail-ont-id-desc-txt">{{transactionDetail.info.Description}}</div>
        </div>
      </div>

      <!--展示ONT ID和Description的数据块-->
      <div class="row detail-ont-id-desc-tit font-Regular font-size14" v-if="idflag">
        <div class="col">
          <div class="row font-size24">OntId:</div>
          <div class="row detail-ont-id-desc-txt pointer click_able" @click="toOntIdDetailPage(Detail.OntId)">{{Detail.OntId}}</div>
          <div class="row font-size24 margin-top-15">Description:</div>
          <div class="row detail-ont-id-desc-txt">{{Detail.Description}}</div>
        </div>
      </div>

      <!--展示转账金额等详情的数据块-->
      <div class="row font-size14" v-if="txflag" v-for="tx in Detail.TransferList">
        <div class="col-4 detail-col trans-tx-col pointer" @click="toAddressDetailPage(tx.FromAddress)">{{tx.FromAddress}}</div>
        <div class="col-1 detail-col trans-tx-col">>></div>
        <div class="col-2 detail-col trans-tx-col text-center font-weight-bold font-size18">{{toMoney(tx)}} <span class="text-uppercase">{{tx.AssetName}}</span></div>
        <div class="col-1 detail-col trans-tx-col">>></div>
        <div class="col-4 detail-col trans-tx-col pointer" @click="toAddressDetailPage(tx.ToAddress)">{{tx.ToAddress}}</div>
      </div>
    </div>

    <!-- mobile -->
    <div class="mobile-display">
      <div class="row">
        <div class="col-lg-12">
          <p class="text-center font-size28 font-ExtraLight p_margin_bottom_L normal_color">TRANSACTION DETAILS</p>
        </div>
      </div>
      <div class="row">
        <div class="col-lg-12">
          <p class="wordbreak font-size18 color32a4be font-blod important_color">Transaction Hash: <span
            style="font-size:14px;">{{  transactionDetail.info.TxnHash }}</span></p>
        </div>
      </div>
      <table class="table table-hover">
        <thead>
        <tr>
        </tr>
        </thead>
        <tbody>
        <tr>
          <td class="td11 table1_item_title td_height3 font-size16 normal_color">
            Transaction Time: {{$HelperTools.getTransDate(transactionDetail.info.TxnTime)}}
          </td>
        </tr>
        <tr>
          <td class="td11 table1_item_title td_height3 font-size16 normal_color"
              v-if="transactionDetail.info.TxnType != 209">
            Type: Deploy Smart Contract
          </td>
          <td class="td11 table1_item_title td_height3 font-size16 normal_color" v-else>
            Type: Smart Contract
          </td>
        </tr>
        <tr>
          <td class="td11 table1_item_title td_height3 font-size16 normal_color">
            Block Height: <span class=" important_color">{{transactionDetail.info.Height}}</span>
          </td>
        </tr>
        <tr>
          <td class="td11 table1_item_title td_height3 font-size16 normal_color">
            Fee: {{transactionDetail.info.Fee}}
          </td>
        </tr>
        <tr>
          <td class="td11 table1_item_title td_height3 font-size16 normal_color"
              v-if="transactionDetail.info.ConfirmFlag == 1">
            Status: <span style="color:#00AE1D">Confirmed</span>
          </td>
          <td class="td11 table1_item_title td_height3 font-size16 normal_color" v-else>
            Status: <span style="color:#AFACAC">Failed</span>
          </td>
        </tr>
        </tbody>
      </table>
      <table v-if="recordflag" class="table table-hover">
        <thead>
        <tr>
        </tr>
        </thead>
        <tbody>
        <tr>
          <td class="td11 td_height" style="padding: 34px 24px;">
            <p class="font-size24  p_margin_bottom f_color font-Regular">OntId:</p>
            <p class="font-size14 f_color p_margin_bottom font-Regular ">
              {{transactionDetail.info.Description.substr(12,42)}}</p>
            <p class="font-size24  p_margin_bottom n_color font-Regular">Description:</p>
            <p class="font-size14 f_color p_margin_bottom font-Regular">{{transactionDetail.info.Description}}</p>
          </td>
        </tr>
        </tbody>
      </table>
      <table v-if="authflag" class="table table-hover">
        <thead>
        <tr>
        </tr>
        </thead>
        <tbody>
        <tr>
          <td class="td11 td_height" style="padding: 34px 24px;">
            <p class="font-size24  p_margin_bottom n_color font-Regular">Description:</p>
            <p class="font-size14 f_color p_margin_bottom font-Regular">{{transactionDetail.info.Description}}</p>
          </td>
        </tr>
        </tbody>
      </table>
      <table v-if="idflag" class="table table-hover">
        <thead>
        <tr>
        </tr>
        </thead>
        <tbody>
        <tr>
          <td class="td11 td_height" style="padding: 34px 24px;">
            <p class="font-size24  p_margin_bottom f_color font-Regular">OntId:</p>
            <p class="font-size14 important_color p_margin_bottom font-Regular pointer click_able">{{Detail.OntId}}</p>
            <p class="font-size24  p_margin_bottom f_color font-Regular">Description:</p>
            <p class="font-size14 f_color p_margin_bottom font-Regular">{{Detail.Description}}</p>
          </td>
        </tr>
        </tbody>
      </table>
      <table v-if="txflag" class="table table-hover">
        <thead>
        <tr>
        </tr>
        </thead>
        <tbody>
        <tr v-for="tx in Detail.TransferList">
          <td class="td11 td_height" style="background-color:#32A4BE;color:white;padding: 34px 24px;">
            <div class="row ">
              <div class="col-lg-4 padding0-right pointer">{{tx.FromAddress}}</div>
              <div class="col-lg-1 ">>></div>
              <div class="col-lg-2 ">{{toMoney(tx)}} {{tx.AssetName}}</div>
              <div class="col-lg-1 ">>></div>
              <div class="col-lg-4 padding0-left pointer ">{{tx.ToAddress}}</div>
            </div>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
  import {mapState} from 'vuex'
  import GetTransactionType from './../../common/OntMsg/GetTransactionType.js'
  import ReturnHome from '../common/ReturnHome'
  import ListTitle from '../common/ListTitle'

  export default {
    name: "transaction-detail-page",
    components: {ReturnHome, ListTitle},
    data() {
      return {
        Detail: '',
        txflag: false,
        idflag: false,
        recordflag: false,
        authflag: false,
      }
    },
    created() {
      this.getTransactionData()
    },
    watch: {
      '$route': 'getTransactionData',
      'transactionDetail.info': function () {
        if (this.transactionDetail.info.ConfirmFlag == 1) {
          if (this.transactionDetail.info.Detail == undefined) {
            this.recordflag = true;
          } else {
            this.Detail = this.transactionDetail.info.Detail
            if (this.Detail.OntId != undefined) {
              this.idflag = true;
              this.txflag = false;
            } else {
              this.txflag = true;
              this.idflag = false;
            }
          }
          if (this.transactionDetail.info.Description == "auth") {
            this.authflag = true
          }
        } else {
          this.txflag = false;
          this.idflag = false;
        }
      }
    },
    computed: {
      ...mapState({
        transactionDetail: state => state.TransactionDetailPage.TransactionDetail,
      })
    },
    methods: {
      toMoney(txTmp) {
        if (txTmp.AssetName === 'ong') {
          return Number(txTmp.Amount)
        } else {
          let num = txTmp.Amount;
          return num.split('').reverse().join('').substr(10, 10).split('').reverse().join('')
        }
      },
      getTransactionData() {
        this.$store.dispatch('getTransactionDetailPage', this.$route.params).then()
      },
      getTransactionType($case) {
        return GetTransactionType.getTransactionType($case)
      },
      toBlockDetailPage($blockHeight) {
        if (this.$route.params.net == undefined) {
          this.$router.push({name: 'blockDetail', params: {param: $blockHeight}})
        } else {
          this.$router.push({name: 'blockDetailTest', params: {param: $blockHeight, net: "testnet"}})
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
      toOntIdDetailPage($ontid) {
        if (this.$route.params.net == undefined) {
          this.$router.push({name: 'OntIdDetail', params: {ontid: $ontid, pageSize: 10, pageNumber: 1}})
        } else {
          this.$router.push({
            name: 'OntIdDetailTest',
            params: {ontid: $ontid, pageSize: 10, pageNumber: 1, net: "testnet"}
          })
        }
      }
    }
  }
</script>

<style scoped>
  .detail-ont-id-desc-tit {
    background: white;
    margin-top: 4px;
    padding: 2rem;
    color: #AFACAC;
  }

  .detail-ont-id-desc-txt {
    color: #32A4BE;
  }

  .trans-tx-col {
    background: #32A4BE;
    color: white;
    font-size: 14px;
  }
</style>
