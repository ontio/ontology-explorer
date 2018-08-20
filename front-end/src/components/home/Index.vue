<template>
  <div>
    <div class="container-top">
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

    <div class="container">
      <div class="row">
        <div class="col-sm-12 col-md-6 col-lg-4 col-xl-4">
          <block-list></block-list>
        </div>
        <div class="col-sm-12 col-md-6 col-lg-4 col-xl-4">
          <transaction-list></transaction-list>
        </div>
        <div class="col-sm-12 col-md-6 col-lg-4 col-xl-4">
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
  .container-top {
    padding: 0 0 60px;
    background-size: 100% 100%;
    background-image: -ms-linear-gradient(bottom, #2C92A5 0%, #37B6D3 100%);
    background-image: -moz-linear-gradient(bottom, #2C92A5 0%, #37B6D3 100%);
    background-image: -o-linear-gradient(bottom, #2C92A5 0%, #37B6D3 100%);
    background-image: -webkit-gradient(linear, left bottom, left top, color-stop(0, #2C92A5), color-stop(100, #37B6D3));
    background-image: -webkit-linear-gradient(bottom, #2C92A5 0%, #37B6D3 100%);
    background-image: linear-gradient(to top, #2C92A5 0%, #37B6D3 100%);
  }

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
  }

  .index-logo {
    height: 22px;
  }
</style>
