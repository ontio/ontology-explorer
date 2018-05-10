<template>
  <div class="container-fluid">
    <router-view></router-view>
    <div class="container-div">
      <div class="row container " style="margin:auto;">
        <div class="index-logo-warpper col-lg-10"  style="padding-left:24px;">
            <img  src="/static/img/ontlogo.png" class="index-logo">
        </div>
        <div class="index-net-warpper col-lg-2 testNet" >
            <a class="" >Polaris</a>
        </div>
      </div>
        <search-input></search-input>
    </div>
    <div class="container-status">
        <run-status></run-status>
    </div>
    <div class="container-row">
      <div class="row container " style="margin:auto;padding:50px 0 0 0 ">
          <div class="col-lg-4" style="padding:0">
              <block-list></block-list>
          </div>

          <div class="col-lg-4" style="padding:0">
              <transaction-list></transaction-list>
          </div>
          <div class="col-lg-4" style="padding:0">
              <OntIdList></OntIdList>
          </div>
      </div>
    </div>
    <!-- <address-msg></address-msg> -->
  </div>
</template>

<script>
  import RunStatus from "./RunStatus";
  import SearchInput from './../home/SearchInput'
  import OntIdList from "./OntIdList";
  import TransactionList from "./TransactionList";
  import AddressMsg from "./AddressMsg";
  import BlockList from "./BlockList";

  export default {
    name: 'Home',
    mounted: function() {
    },
    computed:{
        height:function(){

        }
    },
    created() {
    },
    watch: {
      'height': function(){
         /* console.log(this.height) */
      } 
    },
    methods:{
    },
    data() {
      return {
      }
    },
    components: {
      BlockList,
      AddressMsg,
      TransactionList,
      OntIdList,
      SearchInput,
      RunStatus
    }
  }
</script>

<style>
</style>
