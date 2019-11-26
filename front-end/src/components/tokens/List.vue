<template>
  <div class="e-container margin-top-15">
    <list-title :name="$t('tokens.list.name')"></list-title>

    <div class="row token-count-view">
<!--       <div class="col text-right">
        <a :href="applyForUrl" target="_blank" class="font-size18 font-blod important_color pointer2">
          <i class="far fa-hand-point-right"></i>&nbsp;&nbsp;{{ $t('contracts.list.tit.checkIn') }}
        </a>
      </div> -->
    </div>

    <ont-pagination :total="tokens.total"></ont-pagination>

    <div class="row justify-content-center">
      <div class="col">
        <div class="table-responsive" style="padding: 24px; background: white">
          <table class="table table-hover">
            <thead>
            <tr>
              <th class="font-size18" scope="col"></th>
              <th class="font-size18" scope="col">{{ $t('all.token') }}</th>
              <th v-if="$route.params.type === 'oep4'"
                  class="font-size18" scope="col">{{ $t('tokens.list.tab.totalSupply') }}</th>
              <th class="font-size18" scope="col">{{ $t('tokens.list.tab.addressCount') }}</th>
              <th class="font-size18" scope="col">{{ $t('tokens.list.tab.hash') }}</th>
              <th class="font-size18" scope="col">{{ $t('tokens.list.tab.creator') }}</th>
              <th class="font-size18" scope="col">{{ $t('tokens.list.tab.txns') }}</th>
              <!--<th class="font-size18" scope="col">{{ $t('tokens.list.tab.time') }}</th>-->
            </tr>
            </thead>
            <tbody>
            <tr v-for="token in tokens.list" class="sc-have-img-line-height">
              <td class="font-size14 font-Regular normal_color">
                <img v-if="token.Logo !== ''" class="sc-list-img" :src="token.Logo" alt="">
                <div v-else class="sc-no-logo">{{ $route.params.type === 'oep4' ? token.Symbol : token.Name.substr(0, 2) }}</div>
              </td>
              <td class="font-size14 font-Regular normal_color sc-pointer" style="max-width:360px"
                  @click="goToTokenDetail(token)">
                <div class=" font-blod font-size16">
                  {{ token.Name }}
                  <span v-if="$route.params.type === 'oep4' && token.Symbol !== ''">&nbsp;&nbsp;{{ ' ( ' + token.Symbol + ' )' }}</span>
                </div>
                <div class="token-td" v-if="$route.params.type === 'oep8'">
                  <b class="col" v-for="tS in token.Symbol">
                    <span class="symbol-name-list">
                      {{ tS }}
                    </span>
                  </b>
                </div>
                <div class="f-color font-size14 token-td">{{ token.Description.substr(0,128) + '...' }}</div>
              </td>

              <td v-if="$route.params.type === 'oep4'"
                  class="font-size14 font-Regular important_color">
                {{ $HelperTools.toFinancialVal(token.TotalSupply) }}
              </td>
              <td class="font-size14 font-Regular important_color">{{ token.Addresscount }}</td>
              <td class="font-size14 font-Regular important_color pointer"
                  @click="goToTokenDetail(token)">
                {{ token.ContractHash.substr(0,8) + '...' + token.ContractHash.substr(32)}}
              </td>
              <td class="font-size14 font-Regular important_color pointer"
                  @click="goToAddressDetail(token.Creator)">
                {{ token.Creator.substr(0,4) + '...' + token.Creator.substr(30)}}
              </td>
              <td class="font-size14 font-Regular normal_color">{{ token.TxCount }}</td>
              <!--<td class="font-size14 font-Regular normal_color">{{ $HelperTools.getTransDate(token.CreateTime) }}</td>-->
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <ont-pagination :total="tokens.total"></ont-pagination>
  </div>
</template>

<script>
  import {mapState} from 'vuex'

  export default {
    name: "Token-List",
    data() {
      return {
        applyForUrl: 'https://docs.google.com/forms/d/e/1FAIpQLSdszQp1BbviS83psIZUZYMKoNkn0e4zcYxrVqM6v5Qbmzby3g/viewform?vc=0&c=0&w=1'
      }
    },
    created() {
      this.getTokensData()
    },
    watch: {
      '$route': 'getTokensData'
    },
    computed: {
      ...mapState({
        tokens: state => state.Tokens.List
      }),
    },
    methods: {
      getTokensData() {
        this.tokens.list = ''; // 清空内容
        this.testNetPageSizeCheck()
        this.$store.dispatch('GetTokens', this.$route.params).then()
      },
      
      testNetPageSizeCheck(){
        if(this.$route.params.net == "testnet"){
          if(this.$route.params.pageSize > 30){
            this.$message({message: this.$t('error.pagesize')});
            this.$route.params.pageSize = 30
            this.$router.push({name: this.$route.name, params: this.$route.params})
            return
          }
        }
      },
      goToTokenDetail(token) {
        if (this.$route.params.net == undefined) {
          this.$router.push({
            name: 'TokenDetail', params: {
              type: this.$route.params.type,
              contractHash: token.ContractHash,
              pageSize: 10,
              pageNumber: 1
            }
          })
        } else {
          this.$router.push({
            name: 'TokenDetailTest', params: {
              type: this.$route.params.type,
              contractHash: token.ContractHash,
              pageSize: 10,
              pageNumber: 1,
              net: 'testnet'
            }
          })
        }
      },
      goToAddressDetail(address) {
        if (this.$route.params.net === undefined) {
          this.$router.push({
            name: 'AddressDetail',
            params: {address: address, pageSize: 20, pageNumber: 1}
          })
        } else {
          this.$router.push({
            name: 'AddressDetailTest',
            params: {address: address, pageSize: 20, pageNumber: 1, net: 'testnet'}
          })
        }
      }
    }
  }
</script>

<style scoped>
  .token-count-view {
    margin-bottom: 15px;
  }

  .sc-list-img {
    width: 32px;
    height: 32px;
    border-radius: 16px;
  }

  .sc-no-logo {
    width: 32px;
    height: 32px;
    border-radius: 16px;
    background-color: #32A4BE;
    color: white;
    font-weight: bold;
    line-height: 32px;
    text-align: center;
  }

  .token-td > b {
    margin-left: 0;
    padding-left: 0;
  }

  .symbol-name-list {
    width: 51px;
    height: 20px;
    background: rgba(175, 172, 172, 1);
    border-radius: 3px;
    color: white;
    padding: 3px 5px;
    line-height: 30px;
  }

  .sc-pointer:hover {
    cursor: pointer;
    color: #32A4BE;
    text-decoration: underline;
  }

  .sc-have-img-line-height {
    line-height: 100%;
  }

  .token-td {
    margin-top: 6px;
  }
</style>
