<template>
  <div class="e-container">
    <div class="row">
      <!--  Logo区域  -->
<!--       <div v-if="isHome" class="  index-logo-warpper index-logo-warpper-new">
        <img src="../../assets/logos/ontlogo.png" class="index-logo">
      </div>
      <div v-else class=" no-index-logo-warpper index-logo-warpper-new">
        <router-link class="navbar-brand" :to="{path: $route.params.net === 'testnet'?'/testnet':'/'}">
          <img class="index-logo" src="../../assets/logos/logo.png" alt="">
        </router-link>
      </div> -->

      <!--  Nav-Bar 点击区域  -->
      <nav class="navbar navbar-expand-lg navbar-dark col-12 padding-lr0" :class="isHome ? '' : 'navbar-no-home'">
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
          <i class="fas fa-bars"></i>
        </button>
        <div class="collapse navbar-collapse" :class="isHome ? '' : 'not-home-bar'" id="collapsibleNavbar">
          <!--  具体菜单区域  -->
          <div v-if="isHome" class=" col-2 index-logo-warpper">
            <img src="../../assets/logos/ontlogo.png" class="index-logo">
          </div>
          <div v-else class=" col-2 no-index-logo-warpper">
            <router-link class="navbar-brand" :to="{path: $route.params.net === 'testnet'?'/testnet':'/'}">
              <img class="index-logo" src="../../assets/logos/logo.png" alt="">
            </router-link>
          </div>
          <ul class="navbar-nav  navbar-center  padding-right0 col-8" :class="isHome ? '' : 'navbar-nav-no-home'">
            <li class="nav-item dropdown">
              <a class="nav-link dropdown-toggle padding-lr0" data-toggle="dropdown" href="#">
                <!-- <i class="fas fa-link"></i>&nbsp;&nbsp; -->
                <img v-if='isHome' src="../../assets/navbar/lian@2xM.png" class="nav-link-icon" />
                <img v-else src="../../assets/navbar/lian@2x.png" class="nav-link-icon" />
                {{ $t('navbar.top.blockchain') }}
              </a>
              <div class="dropdown-menu">
                <!--<router-link class="dropdown-item" :to="{ name: 'NodeStakeList'}"><i class="fab fa-linode"></i>&nbsp;&nbsp;{{ $t('navbar.top.nodes') }}</router-link>-->
                <!--<hr style="margin: 4px 1rem">-->
                <a class="dropdown-item" @click="toBlockListPage"><!-- <i class="fas fa-th"></i>&nbsp;&nbsp; -->{{ $t('navbar.top.blocks') }}</a>
                <hr style="margin: 4px 1rem">
                <a class="dropdown-item" @click="toTransactionListPage"><!-- <i class="fas fa-exchange-alt"></i>&nbsp;&nbsp; -->{{ $t('navbar.top.txns') }}</a>
                <hr style="margin: 4px 1rem">
                <a class="dropdown-item" @click="toAddressList"><!-- <i class="fas fa-university"></i>&nbsp;&nbsp; -->{{ $t('navbar.top.accounts') }}</a>
              </div>
            </li>

            <li v-if="$route.params.net !== 'testnet'" class="nav-item dropdown">
              <a class="nav-link dropdown-toggle padding-lr0" data-toggle="dropdown" href="#">
                <!-- <i class="fab fa-linode"></i>&nbsp;&nbsp; -->
                <img v-if='isHome' src="../../assets/navbar/nodes@2xM.png" class="nav-link-icon" />
                <img v-else src="../../assets/navbar/nodes@2x.png" class="nav-link-icon" />{{ $t('navbar.top.nodes') }}
              </a>
              <div class="dropdown-menu">
                <router-link class="dropdown-item nav-link" :to="{ name: 'NodeStakeList'}">
                  <!-- <i class="far fa-handshake"></i>&nbsp;&nbsp; -->{{ $t('navbar.top.stake') }}
                </router-link>
                <hr style="margin: 4px 1rem">
                <a class="dropdown-item" :href="monitor" target="_blank">
                  <!-- <i class="fas fa-map-marked-alt"></i>&nbsp;&nbsp; -->{{ $t('navbar.top.nodeMap') }}
                </a>
              </div>
            </li>

            <li class="nav-item dropdown">
              <a class="nav-link dropdown-toggle padding-lr0" data-toggle="dropdown" href="#">
                <img v-if='isHome' src="../../assets/navbar/token@2xM.png" class="nav-link-icon" />
                <img v-else src="../../assets/navbar/token@2x.png" class="nav-link-icon" />
                <!-- <i class="fas fa-coins"></i>&nbsp;&nbsp; -->{{ $t('navbar.top.tokens') }}
              </a>
              <div class="dropdown-menu">
                <a class="dropdown-item" @click="toTokenList('oep4')"><!-- <i class="fas fa-coins"></i>&nbsp;&nbsp; -->{{ $t('navbar.top.oep4') }}</a>
                <hr style="margin: 4px 1rem">
                <a class="dropdown-item" @click="toTokenList('oep5')"><!-- <i class="fas fa-coins"></i>&nbsp;&nbsp; -->{{ $t('navbar.top.oep5') }}</a>
                <hr style="margin: 4px 1rem">
                <a class="dropdown-item" @click="toTokenList('oep8')"><!-- <i class="fas fa-coins"></i>&nbsp;&nbsp; -->{{ $t('navbar.top.oep8') }}</a>
              </div>
            </li>

            <li class="nav-item">
              <a class="nav-link padding-lr0" @click="toContractList">
                <img v-if='isHome' src="../../assets/navbar/Contracts@2xM.png" class="nav-link-icon" />
                <img v-else src="../../assets/navbar/Contracts@2x.png" class="nav-link-icon" />
                <!-- <i class="far fa-file"></i>&nbsp;&nbsp; -->{{ $t('navbar.top.contracts') }}</a>
            </li>

            <li class="nav-item">
              <a class="nav-link padding-lr0" @click="toOntIdListPage">
                <img v-if='isHome' src="../../assets/navbar/ontid@2xM.png" class="nav-link-icon" />
                <img v-else src="../../assets/navbar/ontid@2x.png" class="nav-link-icon" />
                <!-- <i class="fas fa-id-card"></i>&nbsp;&nbsp; -->{{ $t('navbar.top.ontId') }}
              </a>
            </li>

            <li class="nav-item dropdown">
              <a class="nav-link dropdown-toggle padding-lr0" data-toggle="dropdown" href="#">
                <img v-if='isHome' src="../../assets/navbar/tools@2xM.png" class="nav-link-icon" />
                <img v-else src="../../assets/navbar/tools@2x.png" class="nav-link-icon" />
                <!-- <i class="fas fa-tools"></i>&nbsp;&nbsp; -->{{ $t('navbar.top.tool') }}
              </a>
              <div class="dropdown-menu">
                <a class="dropdown-item" target="_blank" :href="apiDocUrl"><!-- <i class="fas fa-book"></i>&nbsp;&nbsp; -->{{ $t('navbar.top.apis') }}</a>
                <hr style="margin: 4px 1rem">
                <a class="dropdown-item" @click="toStatistics"><!-- <i class="fas fa-table"></i>&nbsp;&nbsp; -->{{ $t('navbar.top.statistics') }}</a>
              </div>
            </li>
          </ul>

          <ul class="navbar-nav col-2 navbar-right" :class="isHome ? '' : 'navbar-nav-no-home'">

            <li class="nav-item dropdown ">
              <a v-if="$t('navbar.flag') === 'zh'" class="nav-link nav-link-r dropdown-toggle padding-lr0" data-toggle="dropdown" href="#">
                <!-- <i class="fas fa-globe"></i>&nbsp;&nbsp; -->{{ $t('language.zh') }}
              </a>
              <a v-else class="nav-link nav-link-r dropdown-toggle padding-lr0" data-toggle="dropdown" href="#">
                <!-- <i class="fas fa-globe"></i>&nbsp;&nbsp; -->{{ $t('language.en') }}
              </a>
              <div class="dropdown-menu">
                <a class="dropdown-item"
                   :class="$t('navbar.flag') === 'zh' ? '' : 'pointer-events'" href="#"
                   @click="chooseLanguage('en')">{{ $t('language.enName') }}</a>
                <hr style="margin: 4px 1rem">
                <a class="dropdown-item"
                   :class="$t('navbar.flag') === 'zh' ? 'pointer-events' : ''" href="#"
                   @click="chooseLanguage('zh')">{{ $t('language.zhName') }}</a>
              </div>
            </li>

            <li class="nav-item dropdown ">
              <a v-if="$route.params.net === 'testnet'" class="nav-link nav-link-r dropdown-toggle padding-lr0" data-toggle="dropdown" href="#">
                <!-- <i class="fas fa-tools"></i>&nbsp;&nbsp; -->{{ $t('navbar.top.testNet') }}
              </a>
              <a v-else class="nav-link nav-link-r dropdown-toggle padding-lr0" data-toggle="dropdown" href="#">
                <!-- <i class="fas fa-home"></i>&nbsp;&nbsp; -->{{ $t('navbar.top.mainNet') }}
              </a>
              <div class="dropdown-menu">
                <a class="dropdown-item"
                   :class="$route.params.net === 'testnet' ? '' : 'pointer-events'" href="#"
                   @click="changeNet()"><!-- <i class="fas fa-home"></i>&nbsp;&nbsp; -->{{ $t('navbar.top.mainNet') }}</a>
                <hr style="margin: 4px 1rem">
                <a class="dropdown-item"
                   :class="$route.params.net === 'testnet' ? 'pointer-events' : ''" href="#"
                   @click="changeNet()"><!-- <i class="fas fa-vial"></i>&nbsp;&nbsp; -->{{ $t('navbar.top.testNet') }}</a>
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
    name: "NavBar1",
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
          this.$router.push({name: 'TransactionListTest', params: {pageSize: 20, pageNumber: 1, net: "testnet"}})
        } else {
          this.$router.push({name: 'TransactionList', params: {pageSize: 20, pageNumber: 1}})
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
          this.$router.push({name: 'addressListTest', params: {token: 'ont',pageSize: 20, pageNumber: 1, net: 'testnet'}})
        } else {
          this.$router.push({name: 'addressList', params: {token: 'ont', pageSize: 20, pageNumber: 1}})
        }
      },
      toTokenList($type) {
        if (this.$route.params.net === 'testnet') {
          this.$router.push({name: 'TokenListTest', params: {type: $type, pageSize: 10, pageNumber: 1, net: 'testnet'}})
        } else {
          this.$router.push({name: 'TokenList', params: {type: $type, pageSize: 10, pageNumber: 1}})
        }
      },
      toStatistics() {
        let name = 'Statistics';
        let params = {day: '14'};

        if (this.$route.params.net === 'testnet') {
          params.net = "testnet";
          name = name + 'Test'
        }

        this.$router.push({name: name, params: params});
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
    margin-bottom: 10px;
  }
  .no-index-logo-warpper {
    margin-top: 5px;
  }
  @media screen and (min-width: 992px ) {
    .index-logo-warpper-new{
      display: none;
    }
  }
  @media screen and (max-width: 992px ) {
    .index-logo-warpper-new{
      display: block;
    }
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
  .navbar-center{
    justify-content: center;
  }
  .navbar-right{
    flex-direction: row-reverse;
  }
  .padding-right0{
    padding-right:0px;
  }
  .padding-lr0{
    padding-right:0px !important;
    padding-left:0px !important;
  }

  .padding0{
    padding:0 !important;
  }
  @media screen and (min-width: 1250px ) {
    .navbar-nav > .nav-item > .nav-link {
      margin-right: 32px;
    } 
    .navbar-nav > .nav-item > .nav-link-r {
      margin-left: 32px !important;
      margin-right: 0 !important;
    } 
    .nav-link-icon{
      width: 16px;
      margin-right:8px;
      margin-bottom: 4px;
    }
    .dropdown-toggle::after{
      margin-left: 8px !important;
    }
  }
  @media screen and (min-width: 1120px )  and (max-width: 1250px ) {
    .navbar-nav > .nav-item > .nav-link {
      margin-right: 24px;
    } 
    .navbar-nav > .nav-item > .nav-link-r {
      margin-left: 24px !important;
      margin-right: 0 !important;
    } 
    .nav-link-icon{
      width: 16px;
      margin-right:6px;
      margin-bottom: 4px;
    }
    .dropdown-toggle::after{
      margin-left: 6px !important;
    }
  }
  @media screen and (min-width: 900px )  and (max-width: 1120px ) {
    .navbar-nav > .nav-item > .nav-link {
      margin-right: 15px;
    } 
    .navbar-nav > .nav-item > .nav-link-r {
      margin-left: 15px !important;
      margin-right: 0 !important;
    } 
    .nav-link-icon{
      width: 16px;
      margin-right:4px;
      margin-bottom: 4px;
    }
    .dropdown-toggle::after{
      margin-left: 4px !important;
    }
  }
  @media screen and (max-width: 900px ) {
    .navbar-nav > .nav-item > .nav-link {
      margin-right: 15px;
    } 
    .navbar-nav > .nav-item > .nav-link-r {
      margin-left: 15px !important;
      margin-right: 0 !important;
    } 
    .nav-link-icon{
      width: 16px;
      margin-right:4px;
      margin-bottom: 4px;
    }
    .dropdown-toggle::after{
      margin-left: 4px !important;
    }
  }
</style>
