<template>
  <div class="container container-padding container-margin-top">
    <div class="div-transaction-detail-page form-group pc-display">
      <div class="row">
        <div class="col-lg-6">
          <p  class=" font-Regular normal_color font-size18 title-more float-left block-detail-page-check-hand" @click="toReturn"><< {{ $t('all.return') }}</p>
        </div>
      </div>
      <div class="row">
        <div class="col-lg-12">
          <p  class="text-center font-size40 font-ExtraLight p_margin_bottom_L normal_color" >TRANSACTION DETAILS</p>
        </div>
      </div>
      <div class="row">
        <div class="col-lg-12">
          <p class="wordbreak font-size24 color32a4be font-blod important_color">Transaction Hash: <span style="font-size:14px;">{{  transactionDetail.info.TxnHash }}</span></p>
        </div>
      </div>
      <table class="table table-hover">
        <thead>
        <tr>
        </tr>
        </thead>
        <tbody>
        <tr >
          <td class="td11 table1_item_title td_height font-size24 normal_color">
            Transaction Time: {{getDate(transactionDetail.info.TxnTime)}}
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
          <td class="td11 table1_item_title td_height font-size24 normal_color" v-if="transactionDetail.info.TxnType != 209">
            Type: Deploy Smart Contract
          </td>
          <td class="td11 table1_item_title td_height font-size24 normal_color" v-else>
            Type: Smart Contract
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
          <td class="td13 table1_item_title td_height font-size24 normal_color pointer" @click="toBlockDetailPage(transactionDetail.info.Height)">
            Block Height: <span class="click_able important_color">{{transactionDetail.info.Height}}</span>
          </td>
          <td class="td13 table1_item_title td_height font-size24 normal_color">
            Fee: {{transactionDetail.info.Fee}}
          </td>
          <td class="td13 table1_item_title td_height font-size24 normal_color"  v-if="transactionDetail.info.ConfirmFlag == 1">
            Status: <span style="color:#00AE1D">Confirmed</span>
          </td>
          <td class="td13 table1_item_title td_height font-size24 normal_color" v-else>
            Status: <span style="color:#AFACAC" >Failed</span>
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
            <p class="font-size14 important_color p_margin_bottom font-Regular pointer click_able" @click="toOntIdDetailPage(Detail.OntId)">{{Detail.OntId}}</p>
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
              <div class="col-lg-4 padding0-right pointer " @click="toAddressDetailPage(tx.FromAddress)">{{tx.FromAddress}}</div>
              <div class="col-lg-1 ">sent</div>
              <div class="col-lg-2 ">{{tx.Amount}} {{transactionDetail.info.Detail.AssetName}}</div>
              <div class="col-lg-1 ">to</div>
              <div class="col-lg-4 padding0-left pointer " @click="toAddressDetailPage(tx.ToAddress)">{{tx.ToAddress}}</div>
            </div>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
<!-- mobile -->
    <div class="div-transaction-detail-page form-group mobile-display">
      <div class="row">
        <div class="col-lg-12">
          <p  class="text-center font-size28 font-ExtraLight p_margin_bottom_L normal_color" >TRANSACTION DETAILS</p>
        </div>
      </div>
      <div class="row">
        <div class="col-lg-12">
          <p class="wordbreak font-size18 color32a4be font-blod important_color">Transaction Hash: <span style="font-size:14px;">{{  transactionDetail.info.TxnHash }}</span></p>
        </div>
      </div>
      <table class="table table-hover">
        <thead>
        <tr>
        </tr>
        </thead>
        <tbody>
        <tr >
          <td class="td11 table1_item_title td_height3 font-size16 normal_color">
            Transaction Time: {{getDate(transactionDetail.info.TxnTime)}}
          </td>
        </tr>
        <tr >
          <td class="td11 table1_item_title td_height3 font-size16 normal_color" v-if="transactionDetail.info.TxnType != 209">
            Type: Deploy Smart Contract
          </td>
          <td class="td11 table1_item_title td_height3 font-size16 normal_color" v-else>
            Type: Smart Contract
          </td>
        </tr>
        <tr >
          <td class="td11 table1_item_title td_height3 font-size16 normal_color" >
            Block Height: <span class=" important_color">{{transactionDetail.info.Height}}</span>
          </td>
        </tr>
        <tr >
          <td class="td11 table1_item_title td_height3 font-size16 normal_color" >
            Fee: {{transactionDetail.info.Fee}}
          </td>
        </tr>
        <tr >
          <td class="td11 table1_item_title td_height3 font-size16 normal_color"  v-if="transactionDetail.info.ConfirmFlag == 1">
            Status: <span style="color:#00AE1D">Confirmed</span>
          </td>
          <td class="td11 table1_item_title td_height3 font-size16 normal_color" v-else>
            Status: <span style="color:#AFACAC" >Failed</span>
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
            <p class="font-size14 important_color p_margin_bottom font-Regular pointer click_able" >{{Detail.OntId}}</p>
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
              <div class="col-lg-1 ">sent</div>
              <div class="col-lg-2 ">{{tx.Amount}} {{transactionDetail.info.Detail.AssetName}}</div>
              <div class="col-lg-1 ">to</div>
              <div class="col-lg-4 padding0-left pointer " >{{tx.ToAddress}}</div>
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
  import Helper from './../../helpers/helper.js'
  import GetTransactionType from './../../common/OntMsg/GetTransactionType.js'

  export default {
      name: "transaction-detail-page",

    data() {
      return {
        Detail:'',
        txflag:false,
        idflag:false
      }
    },
    created() {
      this.getTransactionDetailPage()
    },
    watch: {
      '$route': 'getTransactionDetailPage',
      'transactionDetail.info':function(){
        if(this.transactionDetail.info.ConfirmFlag ==1){
           this.Detail = this.transactionDetail.info.Detail
           if(this.Detail.OntId != undefined){
             this.idflag = true;
             this.txflag = false;
           }else{
             this.txflag = true;
             this.idflag = false;
           }
        }else{
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
      getTransactionDetailPage() {
        this.$store.dispatch('getTransactionDetailPage',this.$route.params).then(response => {
          /* console.log(response) */
        }).catch(error => {
          console.log(error)
        })
      },
      toReturn(){
        this.$router.go(-1)
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
      toBlockDetailPage($blockHeight){
        this.$router.push({ name:'blockDetail', params:{param:$blockHeight}})
      },
      toAddressDetailPage($address){
        this.$router.push({ name:'AddressDetail', params:{address:$address,pageSize:100,pageNumber:1}})
      },
      toOntIdDetailPage($ontid){
        this.$router.push({ name:'OntIdDetail', params:{ontid:$ontid,pageSize:100,pageNumber:1}})
      },
    }
  }
</script>

<style scoped>
  .div-transaction-detail-page {
    /* border: 1px solid rgba(0, 0, 0, 0.1); */
    border-radius: 0.25rem;
    /* padding: 15px; */
  }
  .div-transaction-detail-page-msg {
    border-radius: 0.25rem;
    padding: 15px;
  }
  .transaction-detail-page-txn {
    margin-left: 15px;
    margin-bottom: 14px;
    line-height:1.6;
  }
  .transaction-detail-page-hr {
    height: 1px;
  }
  .transaction-detail-page-check-hand{
    cursor: pointer;
  }
@media screen and (max-width:768px){
    .mobile_display{
        display: block !important
    }
    .pc_display{
        display: none !important
    }
}
@media screen and (min-width:769px){
    .mobile_display{
        display: none !important
    }
    .pc_display{
        display: block !important
    }
}
</style>
