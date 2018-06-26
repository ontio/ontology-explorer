<template>
  <div id="addresstop" class="container container-margin-top">
    <div class="div-ont-id-detail-page form-group">
      <div class="row">
        <div class="col-lg-6">
          <p  class="title-more float-left block-detail-page-check-hand font-Regular normal_color font-size18" @click="toReturn"><< {{ $t('all.return') }}</p>
        </div>
      </div>
      <div class="row">
        <div class="col-lg-12">
          <p  class="text-center font-size40 font-ExtraLight normal_color" >ADDRESS DETAILS</p>
        </div>
      </div>
      <div class="row">
        <div class="col-lg-6">
          <p class="font-size24 important_color font-blod">ADDRESS: <span class="font-size14 important_color">{{$route.params.address}}</span></p>
        </div>
      </div>
      <table class="table table-hover">
        <thead>
        <tr>
        </tr>
        </thead>
        <tbody>
        <tr >
          <td class="td11">
            <p class="table1_item_title font-size24 font-blod normal_color" >Asset Balance</p>
            <p class="table1_item_title font-size24 font-Regular normal_color" v-for="asset in addressDetail.info.AssetBalance">{{asset.AssetName=='ont'?"ONT":asset.AssetName=='ong'?"ONG":"ONG_APPROVE"}}: <span class="important_color">{{asset.Balance}}</span></p>
          </td>
        </tr>
        </tbody>
      </table>
      <table v-if="addressDetail.info.TxnTotal != 0" class="table ">
        <thead>
        <tr style="border-bottom:0px;">
          <td class="table3_title font-size24 font-blod normal_color">
            Transactions on this Address:
          </td>
        </tr>
        </thead>
        <thead>
        <tr>
          <td class="td-tx-head table3_head font-size18 font-blod normal_color">
            HASH
          </td>
          <td class="td-tx-head table3_head font-size18 font-blod normal_color">
            STATUS
          </td>
          <td class="td-tx-head table3_head font-size18 font-blod normal_color">
            TIME
          </td>
        </tr>
        </thead>
        <tbody>
        <tr v-for="tx in TxnList">
          <td class="font-size14 font-Regular f_color click_able" style="cursor:pointer" @click="toTransactionDetailPage(tx.TxnHash)">
            {{tx.TxnHash}}
          </td>
          <td class="font-size14 font-Regular s_color" v-if="tx.ConfirmFlag == 1">
            Confirmed
          </td>
          <td class="font-size14 font-Regular f_color" v-else>
            Failed
          </td>
          <td class="font-size14 font-Regular normal_color">
            {{getDate(tx.TxnTime)}}
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

  export default {
      name: "address-detail-page",

    data() {
      return {
        Ddo:{},
        claimflag:true,
        AssetBalance:[],
        TxnList:[]

      }
    },
    created() {
      this.getAddressDetailPage()
/*       this.timeoutBlock = setTimeout(() => {
        this.scrollTo('addresstop')
        }
      ,100) */
    },
    watch: {
      '$route': 'getaddressDetailPage',
      'addressDetail.info':function(){
         this.AssetBalance = this.addressDetail.info.AssetBalance
         this.TxnList = this.addressDetail.info.TxnList
      }

    },
    computed: {
      ...mapState({
        addressDetail: state => state.AddressDetailPage.AddressDetail,
      })
    },
    methods: {
      scrollTo:function(id){
        document.getElementById(id).scrollIntoView(true); 
      },
      getGas(fee){
        return Helper.getNormalgas(fee)
      },
      getAddressDetailPage() {
        this.$store.dispatch('getAddressDetailPage',this.$route.params).then(response => {
        }).catch(error => {
          console.log(error)
        })
      },
      toReturn(){
        this.$router.go(-1)
      },
      getTime($time){
        return Helper.getDateTime($time)
      },
      getDate($time){
        return Helper.getDate($time)
      },
      toTransactionDetailPage($TxnId){
        this.$router.push({ name:'TransactionDetail', params:{txnHash:$TxnId}})
      },
    }
  }
</script>

<style scoped>
  .div-ont-id-detail-page {
/*     border: 1px solid rgba(0, 0, 0, 0.1); */
    border-radius: 0.25rem;
    padding: 15px;
  }
  .ont-id-detail-page-hr {
    height: 1px;
  }
  .ont-id-detail-page-check-hand{
    cursor: pointer;
  }
  .oid-tab-border-top-none{
    border-top: none;
  }
  .public-info{
    border: 1px solid rgba(0, 0, 0, 0.1);
  }
  .claim-info{
    border: 1px solid rgba(0, 0, 0, 0.1);
    padding:10px
  }
</style>
