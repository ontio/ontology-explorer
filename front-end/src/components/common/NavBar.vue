<template>
  <div class="container">
    <div class="row">
      <!--  Logo区域  -->
      <div v-if="isHome" class="col index-logo-warpper">
        <img src="../../assets/logos/ontlogo.png" class="index-logo">
      </div>
      <div v-else class="col no-index-logo-warpper">
        <router-link class="navbar-brand" :to="{path: $route.params.net === 'testnet'?'/testnet':'/'}">
          <img class="index-logo" src="../../assets/logos/logo.png" alt="">
        </router-link>
      </div>

      <!--  Nav-Bar 点击区域  -->
      <nav class="navbar navbar-expand-lg navbar-dark" :class="isHome ? '' : 'navbar-no-home'">
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
          <i class="fas fa-bars"></i>
        </button>
        <div class="collapse navbar-collapse" :class="isHome ? '' : 'not-home-bar'" id="collapsibleNavbar">

          <!--  具体菜单区域  -->
          <ul class="navbar-nav" :class="isHome ? '' : 'navbar-nav-no-home'">
            <li class="nav-item dropdown">
              <a class="nav-link dropdown-toggle" data-toggle="dropdown" href="#">
                <i class="fas fa-link"></i>&nbsp;&nbsp;{{ $t('navbar.top.blockchain') }}
              </a>
              <div class="dropdown-menu">
                <!--<router-link class="dropdown-item" :to="{ name: 'NodeStakeList'}"><i class="fab fa-linode"></i>&nbsp;&nbsp;{{ $t('navbar.top.nodes') }}</router-link>-->
                <!--<hr style="margin: 4px 1rem">-->
                <a class="dropdown-item" @click="toBlockListPage"><i class="fas fa-th"></i>&nbsp;&nbsp;{{ $t('navbar.top.blocks') }}</a>
                <hr style="margin: 4px 1rem">
                <a class="dropdown-item" @click="toTransactionListPage"><i class="fas fa-exchange-alt"></i>&nbsp;&nbsp;{{ $t('navbar.top.txns') }}</a>
                <!--<hr style="margin: 4px 1rem">-->
                <!--<a class="dropdown-item" @click="toAddressList"><i class="fas fa-university"></i>&nbsp;&nbsp;{{ $t('navbar.top.accounts') }}</a>-->
              </div>
            </li>

            <li v-if="$route.params.net !== 'testnet'" class="nav-item dropdown">
              <a class="nav-link dropdown-toggle" data-toggle="dropdown" href="#">
                <i class="fab fa-linode"></i>&nbsp;&nbsp;{{ $t('navbar.top.nodes') }}
              </a>
              <div class="dropdown-menu">
                <router-link class="dropdown-item nav-link" :to="{ name: 'NodeStakeList'}">
                  <i class="far fa-handshake"></i>&nbsp;&nbsp;{{ $t('navbar.top.stake') }}
                </router-link>
                <hr style="margin: 4px 1rem">
                <a class="dropdown-item" :href="monitor" target="_blank">
                  <i class="fas fa-map-marked-alt"></i>&nbsp;&nbsp;{{ $t('navbar.top.nodeMap') }}
                </a>
              </div>
            </li>

            <li class="nav-item dropdown">
              <a class="nav-link dropdown-toggle" data-toggle="dropdown" href="#">
                <i class="fas fa-coins"></i>&nbsp;&nbsp;{{ $t('navbar.top.tokens') }}
              </a>
              <div class="dropdown-menu">
                <a class="dropdown-item" @click="toTokenList('oep4')"><i class="fas fa-coins"></i>&nbsp;&nbsp;{{ $t('navbar.top.oep4') }}</a>
                <hr style="margin: 4px 1rem">
                <a class="dropdown-item" @click="toTokenList('oep8')"><i class="fas fa-coins"></i>&nbsp;&nbsp;{{ $t('navbar.top.oep8') }}</a>
              </div>
            </li>

            <li class="nav-item">
              <a class="nav-link" @click="toContractList"><i class="far fa-file"></i>&nbsp;&nbsp;{{ $t('navbar.top.contracts') }}</a>
            </li>

            <li class="nav-item">
              <a class="nav-link" @click="toOntIdListPage"><i class="fas fa-id-card"></i>&nbsp;&nbsp;{{ $t('navbar.top.ontId') }}</a>
            </li>

            <li class="nav-item">
              <a class="nav-link" :href="apiDocUrl" target="_blank"><i class="fas fa-book"></i>&nbsp;&nbsp;{{ $t('navbar.top.apis') }}</a>
            </li>

            <li class="nav-item dropdown">
              <a v-if="$route.params.net === 'testnet'" class="nav-link dropdown-toggle" data-toggle="dropdown" href="#">
                <i class="fas fa-tools"></i>&nbsp;&nbsp;{{ $t('navbar.top.testNet') }}
              </a>
              <a v-else class="nav-link dropdown-toggle" data-toggle="dropdown" href="#">
                <i class="fas fa-home"></i>&nbsp;&nbsp;{{ $t('navbar.top.mainNet') }}
              </a>
              <div class="dropdown-menu">
                <a class="dropdown-item"
                   :class="$route.params.net === 'testnet' ? '' : 'pointer-events'" href="#"
                   @click="changeNet()"><i class="fas fa-home"></i>&nbsp;&nbsp;{{ $t('navbar.top.mainNet') }}</a>
                <hr style="margin: 4px 1rem">
                <a class="dropdown-item"
                   :class="$route.params.net === 'testnet' ? 'pointer-events' : ''" href="#"
                   @click="changeNet()"><i class="fas fa-vial"></i>&nbsp;&nbsp;{{ $t('navbar.top.testNet') }}</a>
              </div>
            </li>

            <li class="nav-item dropdown">
              <a v-if="language === 'zh'" class="nav-link dropdown-toggle" data-toggle="dropdown" href="#">
                <i class="fas fa-globe"></i>&nbsp;&nbsp;{{ $t('language.zh') }}
              </a>
              <a v-else class="nav-link dropdown-toggle" data-toggle="dropdown" href="#">
                <i class="fas fa-globe"></i>&nbsp;&nbsp;{{ $t('language.en') }}
              </a>
              <div class="dropdown-menu">
                <a class="dropdown-item"
                   :class="language === 'zh' ? '' : 'pointer-events'" href="#"
                   @click="chooseLanguage('en')">{{ $t('language.enName') }}</a>
                <hr style="margin: 4px 1rem">
                <a class="dropdown-item"
                   :class="language === 'zh' ? 'pointer-events' : ''" href="#"
                   @click="chooseLanguage('zh')">{{ $t('language.zhName') }}</a>
              </div>
            </li>
          </ul>

        </div>
      </nav>
    </div>
  </div>
</template>

<script>
  import LangStorage from './../../helpers/lang'

  export default {
    name: "NavBar",
    data() {
      return {
        isHome: true,
        monitor: 'https://monitor.ont.io/',
        apiDocUrl: 'https://dev-docs.ont.io/#/docs-en/explorer/overview',
        language: 'en'
      }
    },
    created() {
      this.changeView();
      this.language = this.$i18n.locale
    },
    watch: {
      '$route': 'changeView'
    },
    methods: {
      changeView() {
        this.isHome = (this.$route.path === '/' || this.$route.path === '/testnet');
      },
      changeNet() {
        if (this.$route.params.net === 'testnet') {
          this.$router.push({name: 'Home'});
        } else {
          this.$router.push({name: 'HomeTest', params: {net: 'testnet'}});
        }
        location.reload();
      },
      chooseLanguage($lang) {
        this.language = $lang;
        this.$i18n.locale = $lang;
        this.$validator.localize($lang);

        LangStorage.setLang(this.$i18n.locale)
      },
      toContractList() {
        if (this.$route.params.net === 'testnet') {
          this.$router.push({name: 'ContractListTest', params: {pageSize: 20, pageNumber: 1, net: "testnet"}})
        } else {
          this.$router.push({name: 'ContractList', params: {pageSize: 20, pageNumber: 1}})
        }
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
      },
      toAddressList() {
        if (this.$route.params.net === 'testnet') {
          this.$router.push({name: 'addressListTest', params: {pageSize: 20, pageNumber: 1, net: 'testnet'}})
        } else {
          this.$router.push({name: 'addressList', params: {pageSize: 20, pageNumber: 1}})
        }
      },
      toTokenList($type) {
        if (this.$route.params.net === 'testnet') {
          this.$router.push({name: 'TokenListTest', params: {type: $type, pageSize: 20, pageNumber: 1, net: 'testnet'}})
        } else {
          this.$router.push({name: 'TokenList', params: {type: $type, pageSize: 20, pageNumber: 1}})
        }
      }
    }
  }
</script>

<style scoped>
  .navbar {
    font-size: 15px;
  }

  .navbar-toggler {
    border: 0;
    color: white;
  }

  .index-logo {
    height: 22px;
  }

  .navbar-no-home > .navbar-toggler {
    border: 0;
    color: #32a4be;
  }

  .index-logo-warpper {
    margin-top: 10px;
  }
  .no-index-logo-warpper {
    margin-top: 5px;
  }

  /* 通过:class判断的参数 */
  .not-home-bar {
    background-color: transparent;
  }

  .navbar-nav > .dropdown > .dropdown-menu {
    border: 0;
    border-radius: 0;
    background: #f4f4f4;
  }
  .navbar-nav-no-home > .dropdown > .dropdown-menu {
    border: 0;
    border-radius: 0;
    background: white;
  }

  .navbar-nav > .nav-item > .nav-link {
    margin-left: 15px;
  }

  .navbar-nav > .dropdown > .dropdown-menu > .dropdown-item {
    font-size: 14px;
  }

  .navbar-nav > .dropdown > .dropdown-menu > .dropdown-item {
    padding: .25rem 1rem;
  }

  .navbar-nav > .dropdown > .dropdown-menu > .dropdown-menu {
    padding: 0.3rem 0;
  }

  .navbar-nav > .nav-item > a:not([href]):not([tabindex]) {
    color: white;
  }
  .navbar-nav-no-home > .nav-item > a:not([href]):not([tabindex]) {
    color: #595757;
  }

  .navbar-nav > .nav-item > a:not([href]):not([tabindex]):hover,
  .navbar-nav > .dropdown > .dropdown-menu > .dropdown-item:hover {
    cursor:pointer
  }

  .navbar-nav > .nav-item > .nav-link {
    background: transparent;
    color: white;
  }
  .navbar-nav-no-home > .nav-item > .nav-link {
    background: transparent;
    color: #595757;
  }

  .navbar-nav > .dropdown > .dropdown-toggle:hover,
  .navbar-nav > .nav-item > .nav-link:hover {
    color: white;
    background-color: transparent;
    -moz-opacity: 0.7;
    opacity: .70;
    filter: alpha(opacity=70);
  }
  .navbar-nav-no-home > .dropdown > .dropdown-toggle:hover,
  .navbar-nav-no-home > .nav-item > .nav-link:hover {
    color: #595757;
    background-color: transparent;
    -moz-opacity: 0.7;
    opacity: .70;
    filter: alpha(opacity=70);
  }

  .navbar-nav > .dropdown > .dropdown-menu > .dropdown-item {
    color: #32A4BE;
    background: #f4f4f4;
  }

  .navbar-nav-no-home > .dropdown > .dropdown-menu > .dropdown-item {
    color: #595757;
    background: white;
  }

  .navbar-nav > .dropdown > .dropdown-menu > .dropdown-item:hover {
    font-weight: bold;
  }

  .navbar-nav > .dropdown > .dropdown-menu > a > li {
    padding: 0.3rem;
  }

  .navbar-nav > .nav-item > .nav-link.active, .nav-pills .show > .nav-link {
    border-radius: 0;
    background-color: transparent;
    -moz-opacity: 0.7;
    opacity: .70;
    filter: alpha(opacity=70);
  }

  .pointer-events{
    pointer-events: none;
    color: #e4e4e4 !important;
  }
</style>
