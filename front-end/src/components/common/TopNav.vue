<template>
  <nav v-if="routeDisplay" class="navbar nav-background navbar-expand fixed-top navbar-light navbar-elevation">
    <div class="container">
      <router-link class="navbar-brand" :to="{path: $route.params.net == 'testnet'?'/testnet':'/'}">
        <img class="navbar-logo" src="./../../assets/logo.png" alt="">
      </router-link>

      <div class="collapse navbar-collapse">
        <ul class="navbar-nav ml-auto">
          <li class="nav-item" style="margin-top: 3px;">
            {{net}}
          </li>
          <li class="nav-item">
            <div class="input-group-top" style="margin-right: -8px">
              <input type="text" class="form-control-top search-input-txt search-input" v-model="searchContent">
              <div class="input-group-addon-top input-submit-search search-input-txt search-btn text-center font-blod"
                   @click="submitSearch">
                <i class="searchfa fa fa-search" aria-hidden="true"></i>
              </div>
            </div>
          </li>
        </ul>
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
      if (this.$route.path == '/' || this.$route.path == '/testnet') {
        this.routeDisplay = false
      } else {
        this.routeDisplay = true
      }
      /* console.log("route",this.$route) */
      this.net = this.$route.params.net == 'testnet' ? 'Polaris 1.0.0' : 'MainNet'
    },
    watch: {
      /* '$route': 'getRunStatus' */
      '$route': function () {
        if (this.$route.path == '/' || this.$route.path == '/testnet') {
          this.routeDisplay = false
        } else {
          this.routeDisplay = true
        }
        /* console.log("new route",this.$route) */
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
        if (this.$route.params.net == undefined) {
          this.$router.push({name: 'Home'})
        } else {
          this.$router.push({name: 'HomeTest', params: {net: 'testnet'}})
        }
      },
      changeLocale() {
        let locale = this.$i18n.locale
        locale === 'zh' ? this.$i18n.locale = 'en' : this.$i18n.locale = 'zh'
        LangStorage.setLang(this.$i18n.locale)
      },
      getRunStatus() {
        // do something
        this.$store.dispatch('getRunStatus', this.$route.params).then(response => {
          //console.log(response)
        }).catch(error => {
          console.log(error)
        })
      },
      submitSearch() {
        if (this.searchContent !== '') {
          // debug
          // do something
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
          /*           if(this.searchContent.length==64){//hash
                      this.$router.push({ name:'TransactionDetail', params:{txnHash:this.searchContent}})
                    }else{//blockHeight
                      this.$router.push({ name:'blockDetail', params:{param:this.searchContent}})
                    } */
        }
      },
    }
  }
</script>

<style>
  .navbar-logo {
    height: 22px;
  }

  .navbar-elevation {
    height: 50px;
    box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.05);
  }

  .container-padding-fix {
    padding: 30px;
  }

  .navbar {
    font-size: 1.1em;
  }

  .nav-item > input::-webkit-input-placeholder {
    color: #cacaca;
  }

  .nav-item > input::-moz-placeholder {
    color: #cacaca;
  }

  /* firefox 19+ */
  .nav-item > input:-ms-input-placeholder {
    color: #cacaca;
  }

  /* ie */
  .nav-item > input:-moz-placeholder {
    color: #cacaca;
  }
</style>
