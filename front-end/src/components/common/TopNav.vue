<template  >
  <nav v-if="routeDisplay" style="height: 50px;" class="padding0 navbar nav-background navbar-expand-lg navbar-expand-md navbar-expand-sm navbar-expand-xs fixed-top navbar-light bg-light navbar-elevation">
    <div class="container">
    <router-link style="padding-top:4px;" class="navbar-brand" :to="{path: '/'}"><img class="navbar-logo" src="./../../assets/logo.png" alt=""></router-link>

    <div class="collapse navbar-collapse">
      <ul class="navbar-nav mr-auto"></ul>

      <ul class="navbar-nav">
        <li class="nav-item">
          <div class="input-group-top">
            <input type="text" class="form-control-top search-input-txt search-input"  v-model="searchContent" >
            <div class="input-group-addon-top input-submit-search search-input-txt search-btn text-center font-blod"  @click="submitSearch">
              <i class="searchfa fa fa-search" aria-hidden="true" ></i> <!-- <span style="margin:auto">{{$t('searchInput.search')}}</span> -->
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
        routeDisplay:false,
        searchContent: ''
      }
    },
    created() {
      this.getRunStatus()
        if(this.$route.path=='/'){
          this.routeDisplay = false
        }else{
          this.routeDisplay = true
        }
      /* console.log("route",this.$route) */
    },
    watch: {
      /* '$route': 'getRunStatus' */
      '$route': function(){
        if(this.$route.path=='/'){
          this.routeDisplay = false
        }else{
          this.routeDisplay = true
        }
        /* console.log("new route",this.$route) */
      }
    },
    computed: {
      ...mapState({
        blockInfo: state => state.RunStatus.BlockStatus
      })
    },
    methods: {
      changeLocale() {
        let locale = this.$i18n.locale
        locale === 'zh' ? this.$i18n.locale = 'en' : this.$i18n.locale = 'zh'
        LangStorage.setLang(this.$i18n.locale)
      },
      getRunStatus() {
        // do something
        this.$store.dispatch('getRunStatus').then(response => {
          //console.log(response)
        }).catch(error => {
          console.log(error)
        })
      },
      submitSearch() {
      	if(this.searchContent !== '') {
          // debug
          // do something
          switch(this.searchContent.length){
            /* txhash */
            case 64:
              this.$router.push({ name:'TransactionDetail', params:{txnHash:this.searchContent}})
            break;
            /* address */
            case 34:
              this.$router.push({ name:'AddressDetail', params:{address:this.searchContent,pageSize:10,pageNumber:1}})
            break;
            /* ontid */
            case 42:
              this.$router.push({ name:'OntIdDetail', params:{ontid:this.searchContent,pageSize:100,pageNumber:1}})
            break;
            /* block height */
            /* block height */
            case 1:
              this.$router.push({ name:'blockDetail', params:{param:this.searchContent}})
            break;
            case 2:
              this.$router.push({ name:'blockDetail', params:{param:this.searchContent}})
            break;
            case 3:
              this.$router.push({ name:'blockDetail', params:{param:this.searchContent}})
            break;
            case 4:
              this.$router.push({ name:'blockDetail', params:{param:this.searchContent}})
            break;
            case 5:
              this.$router.push({ name:'blockDetail', params:{param:this.searchContent}})
            break;
            case 6:
              this.$router.push({ name:'blockDetail', params:{param:this.searchContent}})
            break;
            case 7:
              this.$router.push({ name:'blockDetail', params:{param:this.searchContent}})
            break;
            case 8:
              this.$router.push({ name:'blockDetail', params:{param:this.searchContent}})
            break;
            case 9:
              this.$router.push({ name:'blockDetail', params:{param:this.searchContent}})
            break;
            case 10:
              this.$router.push({ name:'blockDetail', params:{param:this.searchContent}})
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
    height: 35px;
    margin-right: 5px;
    padding: 6px;
    padding-left:24px;
  }

  .navbar-elevation {
    box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.05);
  }

  .nav-item {
    margin-left: 10px;
  }

  .navbar {
    font-size: 1.1em;
  }

  .change-locale:hover {
    cursor: pointer;
  }

  .nav-item > input::-webkit-input-placeholder { color:#cacaca; }
  .nav-item > input::-moz-placeholder { color:#cacaca; } /* firefox 19+ */
  .nav-item > input:-ms-input-placeholder { color:#cacaca; } /* ie */
  .nav-item > input:-moz-placeholder { color:#cacaca; }
</style>
