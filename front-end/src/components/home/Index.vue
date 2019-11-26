<template>
  <div>
    <div class="container-top">
      <nav-bar-1 class="nav-pc-display"></nav-bar-1>
      <nav-bar  class="nav-mobile-display"></nav-bar>

      <search-input></search-input>
    </div>

    <run-status></run-status>

    <div class="e-container">
      <div class="row list-wrapper">
        <div class="col-sm-12 col-md-6 col-lg-4 col-xl-4 list-item">
          <block-list></block-list>
        </div>
        <div class="col-sm-12 col-md-6 col-lg-4 col-xl-4 list-item">
          <transaction-list></transaction-list>
        </div>
        <div class="col-sm-12 col-md-6 col-lg-4 col-xl-4 list-item">
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
  .list-wrapper{
    padding:0 3px;
  }
  .list-item{
    padding:0 12px;
  }
</style>
