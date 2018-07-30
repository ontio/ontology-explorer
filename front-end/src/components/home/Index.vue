<template>
  <div class="container-fluid">
    <router-view></router-view>
    <div class="container-div">
      <div class="row container " style="margin:auto;">
        <div class="index-logo-warpper col-lg-8 "  style="padding-left:24px;">
            <img  src="/static/img/ontlogo.png" class="index-logo">
        </div>
        <div class="index-net-warpper col-lg-4 testNet" >
            <a class="net-ready" @click="changeNet()">{{readynet}}</a> / <a class="net-notready" @click="changeNet()">{{notreadynet}}</a>
        </div>
<!--         <div class="index-net-warpper-change col-lg-2 col-sm-2 col-xs-2 col-4" >
          <ul style="padding-left:10px;">
                    <li role="presentation " class=" dropdown" style="font-weight: 100;list-style-type:none">
                        <a class="dropdown-toggle nav-ul-item-a" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false" >
                        testnet
                        </a>
                        <ul class="dropdown-menu dropdown-menu-back" style="font-weight: 100;">
                           <li class="nav-ul-li"><a class=" nav-ul-a "style="font-weight: 100;"   >test</a></li>
                           <li class="nav-ul-li"><a class=" nav-ul-a " style="font-weight: 100;" >main</a></li>
                        </ul>
                    </li>
          </ul>
        </div> -->
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
        if(this.$route.params.net == undefined){
          this.readynet = 'MainNet'
          this.notreadynet = 'Polaris 1.0.0'
        }else{
          this.readynet = 'Polaris 1.0.0'
          this.notreadynet = 'MainNet'
        }
    },
    watch: {
      'height': function(){
         /* console.log(this.height) */
      } 
    },
    methods:{
      changeNet(){
        
        if(this.$route.params.net == undefined){
          this.$router.push({ name:'HomeTest', params:{net:'testnet'}})
          this.readynet = 'MainNet'
          this.notreadynet = 'Polaris 1.0.0'
        }else{
          this.$router.push({ name:'Home'})
          this.readynet = 'Polaris 1.0.0'
          this.notreadynet = 'MainNet'
        }
        location.reload();
      }
    },
    data() {
      return {
        readynet:"MainNet",
        notreadynet:"Polaris 1.0.0"
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
.net-notready{
  color:#817f7c !important;
  cursor: pointer;
}
.net-ready{
  cursor: pointer;
}
</style>
