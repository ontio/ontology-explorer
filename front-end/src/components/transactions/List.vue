<template>
  <div class="container container-margin-top">
    <list-title :name="$t('transList.name')"></list-title>

    <ont-pagination :total="transactions.total"></ont-pagination>

    <div class="row justify-content-center">
      <div class="col">
        <div class="table-responsive">
          <table class="table table-hover">
            <thead>
            <tr>
              <th class="trl-tab-border-top-none font-size18" scope="col">{{ $t('all.hash') }}</th>
              <th class="trl-tab-border-top-none font-size18" scope="col">{{ $t('all.status') }}</th>
              <!--<th class="trl-tab-border-top-none font-size18" scope="col">txn type</th>-->
              <!--<th class="trl-tab-border-top-none font-size18" scope="col">block index</th>-->
              <th class="trl-tab-border-top-none font-size18" scope="col">{{ $t('all.height') }}</th>
              <th class="trl-tab-border-top-none font-size18" scope="col">{{ $t('all.fee') }}</th>
              <th class="trl-tab-border-top-none font-size18" scope="col">{{ $t('all.time') }}</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="tx in transactions.list" class="font-size14 font-Regular">
              <td class="important_color pointer" @click="toTransactionDetailPage(tx.TxnHash)">
                {{tx.TxnHash.substr(0,8) + '...' + tx.TxnHash.substr(56)}}
              </td>
              <td class="s-color">{{ tx.ConfirmFlag === 1 ? 'Confirmed' : 'Failed' }}</td>
              <!--<td class="s-color">{{ tx.TxnType === 208 ? 'Deploy' : 'Run' }}</td>-->
              <!--<td class="s-color">{{ tx.BlockIndex }}</td>-->
              <td class="normal_color">{{tx.Height}}</td>
              <td class="normal_color">{{$HelperTools.toFinancialVal(tx.Fee)}}</td>
              <td class="normal_color">{{$HelperTools.getTransDate(tx.TxnTime)}}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <ont-pagination :total="transactions.total"></ont-pagination>
  </div>
</template>

<script>
  import {mapState} from 'vuex'
  import GetTransactionType from './../../common/OntMsg/GetTransactionType.js'

  export default {
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
      this.getTransactions()
    },
    watch: {
      '$route': 'getTransactions'
    },
    computed: {
      ...mapState({
        transactions: state => state.Transactions.List
      })
    },
    methods: {
      getTransactions() {
        this.$store.dispatch('GetTransactions', this.$route.params).then()
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
