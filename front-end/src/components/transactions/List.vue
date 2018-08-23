<template>
  <div class="container container-margin-top">
    <return-home></return-home>
    <list-title :name="$t('transactionList.name')"></list-title>

    <div class="row justify-content-center ">
      <div class="table-responsive">
        <table class="table table-hover">
          <thead>
          <tr>
            <th class="trl-tab-border-top-none font-size18" scope="col">{{ $t('transactionList.TxnId') }}</th>
            <th class="trl-tab-border-top-none font-size18" scope="col">STAUTS</th>
            <th class="trl-tab-border-top-none font-size18" scope="col">HEIGHT</th>
            <th class="trl-tab-border-top-none font-size18" scope="col">{{ $t('transactionList.Fee') }}</th>
            <th class="trl-tab-border-top-none font-size18" scope="col">{{ $t('transactionList.TxnTime') }}</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="transaction in transactionListDetail.info" class="font-size14 font-Regular">
            <td class="important_color td_height3 click_able" @click="toTransactionDetailPage(transaction.TxnHash)">
              {{transaction.TxnHash.substr(0,16)}}...
            </td>
            <td class="s_color td_height3">{{ transaction.ConfirmFlag === 1 ? 'Confirmed' : 'Failed' }}</td>
            <td class="normal_color td_height3">{{transaction.Height}}</td>
            <td class="normal_color td_height3">{{transaction.Fee}}</td>
            <td class="normal_color td_height3">{{$HelperTools.getTransDate(transaction.TxnTime)}}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    <turn-the-page :pagesInfo="transactionListDetail" :pagesName="'TransactionListDetail'"></turn-the-page>
  </div>
</template>

<script>
  import {mapState} from 'vuex'
  import GetTransactionType from './../../common/OntMsg/GetTransactionType.js'
  import ReturnHome from '../common/ReturnHome'
  import ListTitle from '../common/ListTitle'
  import TurnThePage from '../common/TurnThePage'

  export default {
    name: "transaction-list-page",
    components: {ReturnHome, ListTitle, TurnThePage},
    data() {
      return {
        current: 1,
        showItem: 5,
        allpage: 1,
        allnum: '',
        size: 0,
      }
    },
    created() {
      this.getTransactionListPage()
    },
    watch: {
      '$route': 'getTransactionListPage',
    },
    computed: {
      ...mapState({
        transactionListDetail: state => state.TransactionListPage.TransactionListDetail,
      }),
    },
    methods: {
      getTransactionListPage() {
        this.$store.dispatch('getTransactionListPage', this.$route.params).then()
      },
      toTransactionDetailPage($TxnId) {
        if (this.$route.params.net == undefined) {
          this.$router.push({name: 'TransactionDetail', params: {txnHash: $TxnId}})
        } else {
          this.$router.push({name: 'TransactionDetailTest', params: {txnHash: $TxnId, net: 'testnet'}})
        }
      },
      getTransactionType($case) {
        return GetTransactionType.getTransactionType($case)
      }
    }
  }
</script>

<style scoped>
</style>
