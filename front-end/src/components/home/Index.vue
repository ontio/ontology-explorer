<template>
  <div>
    <div class="container-div">
      <div class="container">
        <div class="row">
          <div class="index-logo-warpper col-8">
            <img src="/static/img/ontlogo.png" class="index-logo">
          </div>
          <div class="index-net-warpper col-4 testNet">
            <a class="net-ready" @click="changeNet()">{{readynet}}</a> / <a class="net-notready" @click="changeNet()">{{notreadynet}}</a>
          </div>
        </div>
      </div>

      <search-input></search-input>
    </div>

    <run-status></run-status>

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
    mounted: function () {
    },
    computed: {
      height: function () {

      }
    },
    created() {
      if (this.$route.params.net == undefined) {
        this.readynet = 'MainNet'
        this.notreadynet = 'Polaris 1.0.0'
      } else {
        this.readynet = 'Polaris 1.0.0'
        this.notreadynet = 'MainNet'
      }
    },
    watch: {
      'height': function () {
        /* console.log(this.height) */
      }
    },
    methods: {
      changeNet() {

        if (this.$route.params.net == undefined) {
          this.$router.push({name: 'HomeTest', params: {net: 'testnet'}})
          this.readynet = 'MainNet'
          this.notreadynet = 'Polaris 1.0.0'
        } else {
          this.$router.push({name: 'Home'})
          this.readynet = 'Polaris 1.0.0'
          this.notreadynet = 'MainNet'
        }
        location.reload();
      }
    },
    data() {
      return {
        readynet: "MainNet",
        notreadynet: "Polaris 1.0.0"
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
  .net-notready {
    color: #817f7c !important;
    cursor: pointer;
  }

  .net-ready {
    cursor: pointer;
  }

  .index-logo-warpper {
    width: 100%;
    text-align: left;
    margin-top: 10px;
    padding-left:24px;
  }

  .index-logo {
    height: 22px;
  }
</style>
