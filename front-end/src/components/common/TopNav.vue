<template>
  <nav v-if="routeDisplay" class="navbar nav-background navbar-expand fixed-top">
    <div class="container fix-no-row-col">
      <router-link class="navbar-brand" :to="{path: $route.params.net === 'testnet'?'/testnet':'/'}">
        <img class="navbar-logo" src="./../../assets/logo.png" alt="">
      </router-link>

      <!-- 只有sm屏幕隐藏的 -->
      <div class="d-none d-sm-block">
        <ul class="navbar-nav mr-auto mr-fix">
          <li class="nav-item nav-search-fix">
            <div class="input-group-top">
              <input type="text" class="form-control-top search-input-txt search-input"
                     v-model="searchContent" @keyup.13="submitSearch" :placeholder="'On the ' + net">
              <div class="input-group-addon-top input-submit-search search-input-txt search-btn text-center font-blod"
                   @click="submitSearch">
                <i class="searchfa fa fa-search" aria-hidden="true"></i>
              </div>
            </div>
          </li>
          <li class="nav-item">
            <span class="pointer nav-lang-fix" @click="chooseLanguage()">{{ $t('language.name') }}</span>
          </li>
        </ul>
      </div>

      <!-- 只有sm屏幕展示的 -->
      <div class="d-block d-sm-none">
        <!--<nav class="navbar navbar-expand-sm">-->
        <ul class="nav navbar-nav">
          <li class="dropdown ul-li-a-2">
            <a href="#" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
              <i class="fa fa-bars ul-li-a-i-2" aria-hidden="true"></i>
            </a>
            <ul class="dropdown-menu dropdown-menu-2">
              <a @click="chooseLanguage"><li>{{ $t('language.name') }}</li></a>
              <a @click="toBlockListPage"><li>{{ $t('navbar.blocks') }}</li></a>
              <!--<a @click="chooseLanguage"><li>{{ $t('navbar.addrs') }}</li></a>-->
              <a @click="toTransactionListPage"><li>{{ $t('navbar.tarns') }}</li></a>
              <a @click="toOntIdListPage"><li>{{ $t('navbar.ontIds') }}</li></a>
            </ul>
          </li>
        </ul>
        <!--</nav>-->
      </div>
    </div>
  </nav>
</template>

<script>
  import {mapState} from 'vuex'
  import LangStorage from './../../helpers/lang'

  export default {
    name: 'TopNav',
    data() {
      return {
        noName: 'No Name',
        routeDisplay: false,
        searchContent: ''
      }
    },
    created() {
      this.getRunStatus()
      if (this.$route.path === '/' || this.$route.path === '/testnet') {
        this.routeDisplay = false
      } else {
        this.routeDisplay = true
      }
      this.net = this.$route.params.net === 'testnet' ? 'Polaris 1.0.0' : 'MainNet'
    },
    watch: {
      '$route': function () {
        if (this.$route.path === '/' || this.$route.path === '/testnet') {
          this.routeDisplay = false
        } else {
          this.routeDisplay = true
        }
        this.getRunStatus()
      }
    },
    computed: {
      ...mapState({
        blockInfo: state => state.RunStatus.BlockStatus
      })
    },
    methods: {
      toReturn() {
        if (this.$route.params.net === 'testnet') {
          this.$router.push({name: 'HomeTest', params: {net: 'testnet'}})
        } else {
          this.$router.push({name: 'Home'})
        }
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
      },
      getRunStatus() {
        this.$store.dispatch('getRunStatus', this.$route.params).then()
      },
      submitSearch() {
        if (this.searchContent !== '') {
          switch (this.searchContent.length) {
            /* txhash */
            case 64:
              if (this.$route.params.net == undefined) {
                this.$router.push({name: 'TransactionDetail', params: {txnHash: this.searchContent}})
              } else {
                this.$router.push({
                  name: 'TransactionDetailTest',
                  params: {txnHash: this.searchContent, net: 'testnet'}
                })
              }
              break;
            /* address */
            case 34:
              if (this.$route.params.net == undefined) {
                this.$router.push({
                  name: 'AddressDetail',
                  params: {address: this.searchContent, pageSize: 10, pageNumber: 1}
                })
              } else {
                this.$router.push({
                  name: 'AddressDetailTest',
                  params: {address: this.searchContent, pageSize: 10, pageNumber: 1, net: 'testnet'}
                })
              }
              break;
            /* ontid */
            case 42:
              if (this.$route.params.net == undefined) {
                this.$router.push({
                  name: 'OntIdDetail',
                  params: {ontid: this.searchContent, pageSize: 10, pageNumber: 1}
                })
              } else {
                this.$router.push({
                  name: 'OntIdDetailTest',
                  params: {ontid: this.searchContent, pageSize: 10, pageNumber: 1, net: 'testnet'}
                })
              }
              break;
            /* block height */
            /* block height */
            case 1:
              if (this.$route.params.net == undefined) {
                this.$router.push({name: 'blockDetail', params: {param: this.searchContent}})
              } else {
                this.$router.push({name: 'blockDetailTest', params: {param: this.searchContent, net: 'testnet'}})
              }
              break;
            case 2:
              if (this.$route.params.net == undefined) {
                this.$router.push({name: 'blockDetail', params: {param: this.searchContent}})
              } else {
                this.$router.push({name: 'blockDetailTest', params: {param: this.searchContent, net: 'testnet'}})
              }
              break;
            case 3:
              if (this.$route.params.net == undefined) {
                this.$router.push({name: 'blockDetail', params: {param: this.searchContent}})
              } else {
                this.$router.push({name: 'blockDetailTest', params: {param: this.searchContent, net: 'testnet'}})
              }
              break;
            case 4:
              if (this.$route.params.net == undefined) {
                this.$router.push({name: 'blockDetail', params: {param: this.searchContent}})
              } else {
                this.$router.push({name: 'blockDetailTest', params: {param: this.searchContent, net: 'testnet'}})
              }
              break;
            case 5:
              if (this.$route.params.net == undefined) {
                this.$router.push({name: 'blockDetail', params: {param: this.searchContent}})
              } else {
                this.$router.push({name: 'blockDetailTest', params: {param: this.searchContent, net: 'testnet'}})
              }
              break;
            case 6:
              if (this.$route.params.net == undefined) {
                this.$router.push({name: 'blockDetail', params: {param: this.searchContent}})
              } else {
                this.$router.push({name: 'blockDetailTest', params: {param: this.searchContent, net: 'testnet'}})
              }
              break;
            case 7:
              if (this.$route.params.net == undefined) {
                this.$router.push({name: 'blockDetail', params: {param: this.searchContent}})
              } else {
                this.$router.push({name: 'blockDetailTest', params: {param: this.searchContent, net: 'testnet'}})
              }
              break;
            case 8:
              if (this.$route.params.net == undefined) {
                this.$router.push({name: 'blockDetail', params: {param: this.searchContent}})
              } else {
                this.$router.push({name: 'blockDetailTest', params: {param: this.searchContent, net: 'testnet'}})
              }
              break;
            case 9:
              if (this.$route.params.net == undefined) {
                this.$router.push({name: 'blockDetail', params: {param: this.searchContent}})
              } else {
                this.$router.push({name: 'blockDetailTest', params: {param: this.searchContent, net: 'testnet'}})
              }
              break;
            case 10:
              if (this.$route.params.net == undefined) {
                this.$router.push({name: 'blockDetail', params: {param: this.searchContent}})
              } else {
                this.$router.push({name: 'blockDetailTest', params: {param: this.searchContent, net: 'testnet'}})
              }
              break;
          }

          this.searchContent = ''
        }
      }
    }
  }
</script>

<style>
  .navbar {
    padding: 0 !important;
    font-size: 1.1em;
  }

  .navbar-logo {
    height: 22px;
  }

  .fix-no-row-col {
    padding: 0 15px !important;
  }

  .nav-background {
    background: #f4f4f4 !important;
    border: 0 !important;
    box-shadow: 0 0 0 0 rgba(0, 0, 0, 0) !important;
  }

  .mr-fix {
    margin-top: 10px;
  }

  .search-input::-webkit-input-placeholder { /* WebKit, Blink, Edge */
    color: #cacaca;
  }

  .search-input:-moz-placeholder { /* Mozilla Firefox 4 to 18 */
    color: #cacaca;
  }

  .search-input::-moz-placeholder { /* Mozilla Firefox 19+ */
    color: #cacaca;
  }

  .search-input:-ms-input-placeholder { /* Internet Explorer 10-11 */
    color: #cacaca;
  }

  .nav-search-fix {
    width: 100%;
    margin-right: -15px;
  }

  .nav-item > input::-webkit-input-placeholder {
    color: #cacaca;
  }

  .nav-item > input::-moz-placeholder {
    color: #cacaca;
  }

  .nav-item > input:-ms-input-placeholder {
    color: #cacaca;
  }

  .nav-item > input:-moz-placeholder {
    color: #cacaca;
  }

  .nav-lang-fix {
    margin-left: 30px;
    line-height: 35px;
  }

  .ul-li-a-2 {
    margin-top: 5px;
  }

  .ul-li-a-2 > a {
    color: transparent;
  }

  /* 固定在右上角 */
  .ul-li-a-i-2 {
    color: #36a3bc;
    font-size: 21px;
    position: absolute;
    right: 0;
    top: -12px;
  }

  .dropdown-menu-2 {
    margin-top: 15px;
    margin-left: -160px;
    color: #afacac;
    background: #f4f4f4;
    font-weight: 200;
    border-radius: 0;
    padding: 0.5rem 1rem;
  }

  .dropdown-menu-2 > a > li {
    padding: 0.3rem;
  }
</style>
