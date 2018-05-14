<template>
  <div class="container container-margin-top">
    <div class="div-ont-id-detail-page form-group">
      <div class="row">
        <div class="col-lg-6">
          <p  class=" font-Regular normal_color font-size18 title-more float-left block-detail-page-check-hand" @click="toReturn"><< {{ $t('all.return') }}</p>
        </div>
      </div>
      <div class="row">
        <div class="col-lg-12">
          <p  class="text-center font-size40 font-ExtraLight normal_color" >ONT ID DETAILS</p>
        </div>
      </div>
      <div class="row">
        <div class="col-lg-12">
          <p class="font-size24 font-blod important_color">{{ $t('ontIdDetail.name')}}<span class="font-blod font-size14 important_color "> {{$route.params.ontid}}</span></p>
        </div>
      </div>
      <table v-if="Ddo.Owners" class="table table-hover">
        <thead>
        <tr>
          <td class="td11 font-size24 font-blod normal_color">
            Owner
          </td>
        </tr>
        </thead>
        <tbody>
        <tr v-for="owner in Ddo.Owners">
          <td class="td11">
            <p class="font-size14 font-Regular normal_color">Type: {{owner.Type}}</p>
            <p class="font-size14 font-Regular normal_color">Curve: {{owner.Curve}}</p>
            <p class="font-size14 font-Regular normal_color">Value: {{owner.Value}}</p>
            <p class="font-size14 font-Regular normal_color">PublicKeyId: {{owner.PublicKeyId}}</p>
          </td>
        </tr>
        </tbody>
      </table>
      <table v-if="Ddo.Attributes" class="table table-hover">
        <thead>
        <tr>
          <td class="td11 font-size24 font-blod normal_color">
            Claim Metadata
          </td>
        </tr>
        </thead>
        <tbody>
        <tr v-for="claim in Ddo.Attributes">
          <td class="td11" v-if="claim.Claim">
            <p class="font-size14 font-Regular normal_color">Claim Hash: {{claim.Claim.ClaimId}}</p>
            <p class="font-size14 font-Regular normal_color">Claim Context: {{claim.Claim.ClaimContext}}</p>
            <p class="font-size14 font-Regular normal_color">Context Desc: {{claim.Claim.ContextDesc}}</p>
            <p class="font-size14 font-Regular normal_color " @click="toOntIdDetailPage(claim.Claim.IssuerOntId)">Issuer: <span class="important_color click_able">{{claim.Claim.IssuerOntId}}</span></p>
          </td>
          <td class="td11" v-if="claim.SelfDefined">
            <p class="font-size14 font-Regular normal_color">Claim SelfDefined: {{claim.SelfDefined}}</p>
          </td>
        </tr>
        </tbody>
      </table>
      <table v-if="TxnTotal" class="table ">
        <thead>
        <tr style="border-bottom:0px;">
          <td class="font-size24 font-blod normal_color">
            Events on this ONT ID:
          </td>
        </tr>
        </thead>
        <thead>
        <tr>
          <td class="td-tx-head font-size18 font-blod normal_color">
            HASH
          </td>
          <td class="td-tx-head font-size18 font-blod normal_color">
            CONTENT
          </td>
          <td class="td-tx-head font-size18 font-blod normal_color">
            FEE
          </td>
          <td class="td-tx-head font-size18 font-blod normal_color">
            TIME
          </td>
        </tr>
        </thead>
        <tbody>
        <tr v-for="tx in TxnList">
          <td class="font-size14 font-Regular f_color pointer click_able" @click="toTransactionDetailPage(tx.TxnHash)">
            {{tx.TxnHash.substr(0,30)}}...
          </td>
          <td class="font-size14 font-Regular normal_color ">
            {{getOntIDEvent(tx.Description)}}
          </td>
          <td class="font-size14 font-Regular normal_color">
            {{tx.Fee}}
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
      name: "ont-id-detail-page",

    data() {
      return {
        Ddo:{},
        claimflag:true,
        TxnList:{},
        TxnTotal:''

      }
    },
    created() {
      this.getOntIdDetailPage()
    },
    watch: {
      '$route': 'getOntIdDetailPage',
      'OntIdDetail.info':function(){
          this.Ddo = this.OntIdDetail.info.Ddo
          this.TxnList = this.OntIdDetail.info.TxnList
          this.TxnTotal = this.OntIdDetail.info.TxnTotal
/*           if(this.Ddo.Attributes[0] == undefined){
            this.claimflag = false;
          }else{
            this.claimflag = true;
          } */
      }

    },
    computed: {
      ...mapState({
        OntIdDetail: state => state.OntIdDetailPage.OntIdDetail,
      })
    },
    methods: {
      getOntIdDetailPage() {
        this.$store.dispatch('getOntIdDetailPage',this.$route.params).then(response => {
          /* console.log(response) */
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
      toOntIdDetailPage($OntId){
        this.$router.push({ name:'OntIdDetail', params:{ontid:$OntId}})
      },
      getOntIDEvent:function($event){
             switch ($event.substr(0,12)) {
              case "register Ont":
                return "Register ONT ID"
              case "add publicKe":
                return "Add publickey"
              case "delete publi":
                return "Delete publickey"
              case "add attribut":
                return "Add identity attribute"
              case "update attri":
                return "Update identity attribute"
              case "delete attri":
                return "Delete identity attribute"
            }
      }
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
