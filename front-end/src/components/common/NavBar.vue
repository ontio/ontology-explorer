<template>
  <div class="index-logo-nav">
    <nav v-if="isHome" class="navbar navbar-expand-md navbar-dark">
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
        <i class="fas fa-bars"></i>
      </button>
      <div class="collapse navbar-collapse" id="collapsibleNavbar"
           :class="isHome ? '': 'not-home-bar'">
        <nar-bar-nav></nar-bar-nav>
      </div>
    </nav>

    <nav v-else class="navbar navbar-expand-md navbar-dark mr-auto mr-fix navbar-no-home">
      <button class="navbar-toggler" type="button"
              data-toggle="collapse" data-target="#collapsibleNavbarSm">
        <i class="fas fa-bars"></i>
      </button>
      <div class="collapse navbar-collapse not-home-bar" id="collapsibleNavbarSm">
        <nar-bar-nav></nar-bar-nav>
      </div>
    </nav>
  </div>
</template>

<script>
  import NarBarNav from './NarBarNav'
  import LangStorage from './../../helpers/lang'

  export default {
    name: "NavBar",
    data() {
      return {
        isHome: true,
        apiDocUrl: 'https://github.com/ontio/documentation/tree/master/dev-website-docs/docs-en/explorer'
      }
    },
    created() {
      this.changeView()
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
      chooseLanguage() {
        let locale = this.$i18n.locale
        locale === 'zh' ? this.$i18n.locale = 'en' : this.$i18n.locale = 'zh'
        locale === 'zh' ? this.$validator.localize('en') : this.$validator.localize('zh')
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
      }
    },
    components: {NarBarNav}
  }
</script>

<style scoped>
  .navbar {
    margin-top: 5px;
    font-size: 15px;
  }

  .navbar-toggler {
    border: 0;
    color: white;
  }
  .navbar-no-home > .navbar-toggler {
    border: 0;
    color: #32a4be;
  }

  .index-logo-nav {
    border-radius: 0;
  }

  .not-home-bar {
    background-color: transparent;
    padding: 0 15px;
  }
</style>
