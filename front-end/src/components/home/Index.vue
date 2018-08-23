<template>
  <div>
    <div class="container-top">
      <div class="container">
        <div class="row">
          <div class="col-7 index-logo-warpper">
            <img src="/static/img/ontlogo.png" class="index-logo">
          </div>
          <div class="col-4 index-net-warpper testNet">
            <a class="net-ready" @click="changeNet()">{{readyNet}}</a> / <a class="net-notready" @click="changeNet()">{{notReadyNet}}</a>
          </div>
          <div class="col-1 index-lang-warpper">
            <span class="pointer" @click="chooseLanguage()">{{ $t('language.name') }}</span>
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
  import LangStorage from './../../helpers/lang'
  import SearchInput from './../home/SearchInput'
  import OntIdList from "./OntIdList";
  import TransactionList from "./TransactionList";
  import BlockList from "./BlockList";

  export default {
    name: 'Home',
    data() {
      return {
        readyNet: "MainNet",
        notReadyNet: "Polaris 1.0.0"
      }
    },
    created() {
      if (this.$route.params.net === 'testnet') {
        this.readyNet = 'Polaris 1.0.0';
        this.notReadyNet = 'MainNet'
      } else {
        this.readyNet = 'MainNet';
        this.notReadyNet = 'Polaris 1.0.0'
      }
    },
    methods: {
      changeNet() {
        if (this.$route.params.net === 'testnet') {
          this.$router.push({name: 'Home'});
          this.readyNet = 'Polaris 1.0.0';
          this.notReadyNet = 'MainNet'
        } else {
          this.$router.push({name: 'HomeTest', params: {net: 'testnet'}});
          this.readyNet = 'MainNet';
          this.notReadyNet = 'Polaris 1.0.0'
        }
        location.reload();
      },
      chooseLanguage() {
        let locale = this.$i18n.locale
        locale === 'zh' ? this.$i18n.locale = 'en' : this.$i18n.locale = 'zh'
        locale === 'zh' ? this.$validator.localize('en') : this.$validator.localize('zh')
        LangStorage.setLang(this.$i18n.locale)
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
    margin-top: 10px;
  }

  .index-lang-warpper {
    color: white;
    font-size: 14px;
    padding-top: 6px;
    text-align: right;
    margin: 10px 0 0;
  }

  .index-logo {
    height: 22px;
  }
</style>
