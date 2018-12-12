<template>
  <div class="container margin-top-15">
    <list-title :name="$t('contracts.list.name')"></list-title>

    <div class="row contract-count-view">
      <div class="col text-left font-size18">
        {{ $t('contracts.list.tit.currently') }}
        <span class="important_color"> {{ contracts.totalCount }} </span>
        {{ $t('contracts.list.tit.contracts') }}
      </div>
      <div class="col text-right">
        <a :href="applyForUrl" target="_blank" class="font-size18 important_color pointer2">{{ $t('contracts.list.tit.checkIn') }} →</a>
      </div>
    </div>

    <div class="row justify-content-center">
      <div class="col">
        <div class="table-responsive">
          <table class="table table-hover">
            <thead>
            <tr>
              <th class="font-size18" scope="col"></th>
              <th class="font-size18" scope="col">{{ $t('contracts.list.tab.name') }}</th>
              <th class="font-size18" scope="col">{{ $t('contracts.list.tab.hash') }}</th>
              <th class="font-size18" scope="col">{{ $t('contracts.list.tab.txns') }}</th>
              <th class="font-size18" scope="col">{{ $t('contracts.list.tab.time') }}</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="contract in contracts.info">
              <td class="font-size14 font-Regular normal_color logo-td-width">
                <img v-if="contract.Logo !== ''" class="sc-list-img" :src="contract.Logo" alt="">
                <div v-else class="sc-no-logo">C</div>
              </td>
              <td class="font-size14 font-Regular normal_color sc-pointer"
                  @click="goToContractDetail(contract)">
                <div class="font-blod font-size16">{{ contract.Name }}</div>
                <div class="f-color font-size14 token-td">{{ contract.Description.substr(0,128) + '...' }}</div>
              </td>
              <td class="font-size14 font-Regular important_color pointer"
                  @click="goToContractDetail(contract)">
                {{ contract.ContractHash.substr(0,8) + '...' + contract.ContractHash.substr(32)}}
              </td>
              <td class="font-size14 font-Regular normal_color" style="width: 100px">{{ contract.TxCount }}</td>
              <td class="font-size14 font-Regular normal_color" style="width: 180px">{{ $HelperTools.getTransDate(contract.CreateTime) }}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <turn-the-page v-if="contracts.info.allPage > 1" :pagesInfo="contracts" :pagesName="'Contract'"></turn-the-page>
  </div>
</template>

<script>
  import {mapState} from 'vuex'

  export default {
    name: "Contracts-List",
    data() {
      return {
        applyForUrl: 'https://docs.google.com/forms/d/e/1FAIpQLSdszQp1BbviS83psIZUZYMKoNkn0e4zcYxrVqM6v5Qbmzby3g/viewform?vc=0&c=0&w=1'
      }
    },
    created() {
      this.getContractsData()
    },
    watch: {
      '$route': 'getContractsData'
    },
    computed: {
      ...mapState({
        contracts: state => state.ContractData.Contracts,
      }),
    },
    methods: {
      getContractsData() {
        this.$store.dispatch('getContracts', this.$route.params).then()
      },
      goToContractDetail(contract) {
        if (this.$route.params.net == undefined) {
          this.$router.push({
            name: 'ContractDetail', params: {
              contractHash: contract.ContractHash,
              pageSize: 10,
              pageNumber: 1
            }
          })
        } else {
          this.$router.push({
            name: 'ContractDetailTest', params: {
              contractHash: contract.ContractHash,
              pageSize: 10,
              pageNumber: 1,
              net: 'testnet'
            }
          })
        }
      }
    }
  }
</script>

<style scoped>
  .contract-count-view {
    margin-bottom: 15px;
  }

  .sc-list-img {
    width: 32px;
    height: 32px;
  }

  .logo-td-width {
    width: 32px;
  }

  .sc-no-logo {
    width: 32px;
    height: 32px;
    background-color: #32A4BE;
    color: white;
    font-weight: bold;
    line-height: 32px;
    text-align: center;
  }

  .sc-pointer:hover {
    cursor: pointer;
    color: #32A4BE;
    text-decoration: underline;
  }

  .token-td {
    margin-top: 6px;
  }
</style>
