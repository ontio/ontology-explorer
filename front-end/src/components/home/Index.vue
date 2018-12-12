<template>
  <div>
    <div class="container-top">
      <div class="container">
        <div class="row">
          <div class="col index-logo-warpper">
            <img src="../../assets/logos/ontlogo.png" class="index-logo">
          </div>

          <nav-bar></nav-bar>

          <!--<div class="d-block d-sm-none">-->
            <!--<nav class="navbar navbar-expand-sm">-->
              <!--<ul class="nav navbar-nav">-->
                <!--<li class="dropdown ul-li-a">-->
                  <!--<a href="#" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">-->
                    <!--<i class="fa fa-bars ul-li-a-i" aria-hidden="true"></i>-->
                  <!--</a>-->
                  <!--<ul class="dropdown-menu">-->
                    <!--<a @click="chooseLanguage"><li>{{ $t('language.name') }}</li></a>-->
                    <!--<a @click="toBlockListPage"><li>{{ $t('navbar.blocks') }}</li></a>-->
                    <!--&lt;!&ndash;<a @click="chooseLanguage"><li>{{ $t('navbar.addrs') }}</li></a>&ndash;&gt;-->
                    <!--<a @click="toTransactionListPage"><li>{{ $t('navbar.tarns') }}</li></a>-->
                    <!--<a @click="toOntIdListPage"><li>{{ $t('navbar.ontIds') }}</li></a>-->
                  <!--</ul>-->
                <!--</li>-->
              <!--</ul>-->
            <!--</nav>-->
          <!--</div>-->
        </div>
      </div>

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
  import RunStatus from "./RunStatus";
  import LangStorage from './../../helpers/lang'
  import SearchInput from './../home/SearchInput'
  import OntIdList from "./OntIdList";
  import TransactionList from "./TransactionList";
  import BlockList from "./BlockList";

  export default {
    name: 'Home',
    methods: {
      changeNet() {
        if (this.$route.params.net === 'testnet') {
          this.$router.push({name: 'Home'});
        } else {
          this.$router.push({name: 'HomeTest', params: {net: 'testnet'}});
        }
        location.reload();
      },
      chooseLanguage() {
        let locale = this.$i18n.locale
        locale === 'zh' ? this.$i18n.locale = 'en' : this.$i18n.locale = 'zh'
        locale === 'zh' ? this.$validator.localize('en') : this.$validator.localize('zh')
        LangStorage.setLang(this.$i18n.locale)
      },
      toBlockListPage() {
        if (this.$route.params.net === 'testnet') {
          this.$router.push({name: 'blockListDetailTest', params: {pageSize: 20, pageNumber: 1, net: "testnet"}})
        } else {
          this.$router.push({name: 'blockListDetail', params: {pageSize: 20, pageNumber: 1}})
        }
      },
      toTransactionListPage() {
        if (this.$route.params.net === 'testnet') {
          this.$router.push({name: 'TransactionListDetailTest', params: {pageSize: 20, pageNumber: 1, net: "testnet"}})
        } else {
          this.$router.push({name: 'TransactionListDetail', params: {pageSize: 20, pageNumber: 1}})
        }
      },
      toOntIdListPage() {
        if (this.$route.params.net === 'testnet') {
          this.$router.push({name: 'OntIdListDetailTest', params: {pageSize: 20, pageNumber: 1, net: 'testnet'}})
        } else {
          this.$router.push({name: 'OntIdListDetail', params: {pageSize: 20, pageNumber: 1}})
        }
      }
    },
    components: {
      BlockList,
      TransactionList,
      OntIdList,
      SearchInput,
      RunStatus
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

  .net-notready {
    color: rgba(255,255,255,0.5) !important;
    cursor: pointer;
  }

  .net-ready {
    cursor: pointer;
  }

  .index-logo-warpper {
    margin-top: 10px;
  }
  .span-lang {
    margin-left: 30px;
  }

  .ul-li-a {
    margin-top: 5px;
  }

  .ul-li-a > a {
    color: transparent;
  }

  /* 固定在右上角 */
  .ul-li-a-i {
    color: white;
    font-size: 21px;
    position: absolute;
    right: 15px;
    top: 8px;
  }

  .d-block > .navbar > .nav > .dropdown > .dropdown-menu {
    margin-top: 30px;
    color: #afacac;
    background: #f4f4f4;
    font-weight: 200;
    border-radius: 0;
    padding: 0.5rem 1rem;
  }

  .index-logo {
    height: 22px;
  }
</style>
