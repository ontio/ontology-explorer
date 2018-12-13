<template>
  <div>
    <div class="container-top">
      <nav-bar></nav-bar>

      <search-input></search-input>
    </div>

    <!--Test for line-chart models-->
    <!--<div class="container">-->
      <!--<div class="row" style="height: 160px; padding: 0 15px">-->
        <!--<line-chart data="" options="" style="width: 100%;"></line-chart>-->
      <!--</div>-->
    <!--</div>-->

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
  import SearchInput from './../home/SearchInput'
  import RunStatus from "./RunStatus";
  import BlockList from "./BlockList";
  import TransactionList from "./TransactionList";
  import OntIdList from "./OntIdList";

  export default {
    name: 'Home',
    components: {
      SearchInput,
      RunStatus,
      BlockList,
      TransactionList,
      OntIdList
    }
  }
</script>

<style>
  .container-top {
    padding: 0 0 90px;
    background-size: 100% 100%;
    background-image: -ms-linear-gradient(bottom, #2C92A5 0%, #37B6D3 100%);
    background-image: -moz-linear-gradient(bottom, #2C92A5 0%, #37B6D3 100%);
    background-image: -o-linear-gradient(bottom, #2C92A5 0%, #37B6D3 100%);
    background-image: -webkit-gradient(linear, left bottom, left top, color-stop(0, #2C92A5), color-stop(100, #37B6D3));
    background-image: -webkit-linear-gradient(bottom, #2C92A5 0%, #37B6D3 100%);
    background-image: linear-gradient(to top, #2C92A5 0%, #37B6D3 100%);
  }
</style>
