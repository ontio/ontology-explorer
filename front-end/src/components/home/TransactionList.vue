<template>
  <div class="container">
    <div class="div-block-list form-group">
      <div class="row block-title-content">
        
        <div class="col-lg-8 block-title-wrapper">
          <p class="title font-blod">{{ $t('transactionList.name') }}</p>
        </div>
        <div class="col-lg-4 block-title-wrapper">
          <p class="title-more float-right transaction-list-check-hand" @click="toTransactionListPage">{{ $t('all.more') }}</p>
        </div>
      </div>
      <div class="row">
            <div v-for="(tx,index) in latestTransactionList.info" class="col-lg-12 block-item-wrapper2">
              <div class="col-lg-12  block-item-sub-wrapper">
                <div :class="( index <1) ?'block-item col-lg-8 text-left padding0 font-size14':' font-size14 block-item col-lg-8 text-left padding0 block-item-top'" @click="toTransactionDetailPage(tx.TxnHash)"><span class="txhash-text font700">{{tx.TxnHash.substr(0,12)}}...{{tx.TxnHash.substr(55,10)}}</span></div>
                  <span  v-if="getTime(tx.TxnTime) < 60" class="font-size14 block-item col-lg-4 text-right padding0 block-item-top">{{showtime[index]}}s ago</span>
                  <span  v-else class="font-size14 block-item col-lg-4 text-right padding0 block-item-top">{{getShowDate(tx.TxnTime)}} ago</span>
              </div>
              <div class="col-lg-12  block-item-sub-wrapper">
                <span :class="( index >4) ? ' block-item col-lg-12 text-left padding0  font-size14':'block-item col-lg-12 text-left padding0 block-item-bottom font-size14  '">{{getTxtype(tx.TxnType)}}</span>
              </div>
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
    name: "transaction-list",
    data() {
      return {
          info:[],
        showtime:[0,0,0,0,0]
      }
    },
    created() {
      this.getTransactionList()
      this.intervalBlock2 = setInterval(() => {
        this.getTransactionList()
      }, 6000)
      this.intervalBlockstandard = setInterval(() => {
        for(var i =0;i<5;i++){
          var time = this.showtime[i] + 1
          this.$set(this.showtime,i,time)
        }
      }, 1000)
    },
    watch: {
      '$route': 'getTransactionList',
      'latestTransactionList.info':function(){
        for(var i =0;i<5;i++){
          this.showtime[i] = this.getTime(this.latestTransactionList.info[i].TxnTime)
        }
      }
    },
    computed: {
      ...mapState({
        latestTransactionList: state => state.TransactionList.LatestTransactionList
      })
    },
    methods: {
      getTransactionList() {
        // do something
        this.$store.dispatch('getTransactionList',this.$route.params).then(response => {
          //console.log(response)
        }).catch(error => {
          console.log(error)
        })
      },
      toTransactionListPage(){
        
        if(this.$route.params.net == undefined){
          this.$router.push({ name:'TransactionListDetail', params:{pageSize:10,pageNumber:1}})
        }else{
          this.$router.push({ name:'TransactionListDetailTest', params:{pageSize:10,pageNumber:1,net:"testnet"}})
        }
      },
      toTransactionDetailPage($TxnId){
        if(this.$route.params.net == undefined){
          this.$router.push({ name:'TransactionDetail', params:{txnHash:$TxnId}})
        }else{
          this.$router.push({ name:'TransactionDetailTest', params:{txnHash:$TxnId,net:"testnet"}})
        }
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
      getShowDate($time){
        var time = this.getTime($time)
        return Helper.getshowDate(time)
      },
      countDownTime:function(){
          for(var i=0;i<this.info.length;i++){
            this.info[i].showtime = this.info[i].showtime+1
          }
      },
      getTxtype:function($type){
             switch ($type) {
              case 208:
                return "Deploy Smart Contract"
              case 209:
                return "Invoke Smart Contract"
            }
      }
    },
    beforeDestroy () {
      clearInterval(this.intervalBlock2)
      clearInterval(this.intervalBlockstandard)
    },
    }
</script>

<style scoped>
  .div-transaction-list {
    border: 1px solid rgba(0, 0, 0, 0.1);
    border-radius: 0.25rem;
    padding: 15px;
  }
  .transaction-list-hr {
    height: 1px;
  }
  .tl-tab-border-top-none{
    border-top: none;
  }
  .transaction-list-underline{
    cursor: pointer;
    text-decoration: underline;
  }
  .transaction-list-check-hand{
    cursor: pointer;
  }
  .txhash-text{
    background-color: #32a4be;
    color:white;
    cursor: pointer;
    padding: 4px;
  }
</style>
