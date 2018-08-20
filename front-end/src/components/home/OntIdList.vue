<template>
  <div class="div-ont-id-list">
    <div class="row block-title-content">
      <div class="col-8 block-title-wrapper">
        <p class="title font-blod">{{ $t('ontIdList.name') }}</p>
      </div>
      <div class="col-4 block-title-wrapper">
        <p class="float-right title-more ont-id-list-check-hand" @click="toOntIdListPage">{{ $t('all.more') }}</p>
      </div>
    </div>
    <div class="row">
      <div v-for="(OntId,index) in latestOntIdList.info" class="col-12 block-item-wrapper2">
        <div class="row padding0 block-item-sub-wrapper">
          <div :class="( index <1) ?'block-item col-8 text-left padding0 font-size14':'block-item col-8 text-left padding0 block-item-top font-size14'" @click="toOntIdDetailPage(OntId.OntId)"><span class="ontID-text font700">{{OntId.OntId.substr(0,12)}}...{{OntId.OntId.substr(32,45)}}</span></div>
          <span v-if="getTime(OntId.TxnTime) <60" class="font-size14 block-item col-4 text-right padding0 block-item-top">{{showtime[index]}}s ago</span>
          <span v-else class="font-size14 block-item col-4 text-right padding0 block-item-top">{{getShowDate(OntId.TxnTime)}} ago</span>
        </div>
        <div class="row padding0 block-item-sub-wrapper">
          <!-- <span :class="( index >4) ? ' block-item col-12 text-left padding0 font-size14':'block-item col-12 text-left padding0 block-item-bottom font-size14'">{{OntId.Description.substr(0,30)}}...</span> -->
          <span :class="( index >4) ? ' block-item col-12 text-left padding0 font-size14 click_able':'block-item col-12 text-left padding0 block-item-bottom font-size14 click_able'" @click="toTransactionDetailPage(OntId.TxnHash)">{{getOntIDEvent(OntId.Description)}}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapState} from 'vuex'
  import Helper from './../../helpers/helper.js'
  import GetTransactionType from './../../common/OntMsg/GetTransactionType.js'

  export default {
    name: "ont-id-list",
    data() {
      return {
          info:[],
          showtime:[0,0,0,0,0]
      }
    },
    created() {
      this.getOntIdList()
      this.intervalBlock1 = setInterval(() => {
        this.getOntIdList()
      }, 6000)
      this.intervalBlockstandard = setInterval(() => {
        for(var i =0;i<5;i++){
          var time = this.showtime[i] + 1
          this.$set(this.showtime,i,time)
        }
      }, 1000)
    },
    watch: {
      '$route': 'getOntIdList',
      'latestOntIdList.info':function(){
        for(var i =0;i<this.latestOntIdList.info.length;i++){
          this.showtime[i] = this.getTime(this.latestOntIdList.info[i].TxnTime)
        }
      }
    },
    computed: {
      ...mapState({
        latestOntIdList: state => state.OntIdList.LatestOntIdList
      })
    },
    methods: {
      getOntIdList() {
        // do something
        this.$store.dispatch('getOntIdList',this.$route.params).then(response => {
          //console.log(response)
        }).catch(error => {
          console.log(error)
        })
      },
      toOntIdListPage(){
        if(this.$route.params.net == undefined){
          this.$router.push({ name:'OntIdListDetail', params:{pageSize:10,pageNumber:1}})
        }else{
          this.$router.push({ name:'OntIdListDetailTest', params:{pageSize:10,pageNumber:1,net:'testnet'}})
        }
      },
      getTime($time){
        return Helper.getDateTime($time)
      },
      getDate($time){
        return Helper.getDate($time)
      },
      getShowDate($time){
        var time = this.getTime($time)
        return Helper.getshowDate(time)
      },
      getTransactionType($case){
        return GetTransactionType.getTransactionType($case)
      },
      toTransactionDetailPage($TxnId){
        if(this.$route.params.net == undefined){
          this.$router.push({ name:'TransactionDetail', params:{txnHash:$TxnId}})
        }else{
          this.$router.push({ name:'TransactionDetailTest', params:{txnHash:$TxnId,net:'testnet'}})
        }
      },
      toOntIdDetailPage($OntId){
        if(this.$route.params.net == undefined){
          this.$router.push({ name:'OntIdDetail', params:{ontid:$OntId}})
        }else{
          this.$router.push({ name:'OntIdDetailTest', params:{ontid:$OntId,net:'testnet'}})
        }
      },
      countDownTime:function(){
          for(var i=0;i<this.info.length;i++){
            this.info[i].showtime = this.info[i].showtime+1
          }
      },
      getOntIDEvent:function($event){
             switch ($event.substr(0,12)) {
              case "register Ont":
                return "Register ONT ID"
              case "add publicKe":
                return "Add publickey"
              case "remove publi":
                return "Remove publickey"
              case "add attribut":
                return "Add identity attribute"
              case "update attri":
                return "Update identity attribute"
              case "delete attri":
                return "Delete identity attribute"
              case "change recov":
                return "Change recovery"
              case "add recovery":
                return "Add recovery"
              case "remove attri":
                return "Remove attribute"
            }
      }
    },
    beforeDestroy () {
      clearInterval(this.intervalBlock1)
      clearInterval(this.intervalBlockstandard)
    },
  }
</script>

<style scoped>
  .div-ont-id-list {
    /* border: 1px solid rgba(0, 0, 0, 0.1); */
    border-radius: 0.25rem;
    padding: 15px;
  }
  .ont-id-list-hr {
    height: 1px;
  }
  .oil-tab-border-top-none{
    border-top: none;
  }
  .ont-id-list-underline{
    cursor: pointer;
    text-decoration:underline;
  }
  .ont-id-list-check-hand{
    cursor: pointer;
  }
  .ontID-text{
    background-color: #32a4be;
    color:white;
    cursor: pointer;
    padding: 4px;
  }
</style>
