<template>
  <div class="container container-margin-top">
    <return-home></return-home>
    <list-title :name="$t('addressList.nickname')"></list-title>

    <div class="row justify-content-center table-margin-bottom">
      <div class="col-12">
        <table class="table table-hover">
          <thead>
          <tr>
            <th class="blp-ab-border-top-none font-size18" scope="col" style="padding-top:34px;" >{{ $t('addressList.rank') }}</th>
            <th class="blp-ab-border-top-none font-size18" scope="col">{{ $t('addressList.name') }}</th>
            <th class="blp-ab-border-top-none font-size18" scope="col">{{ $t('addressList.balance') }}</th>
            <th class="blp-ab-border-top-none font-size18" scope="col">{{ $t('addressList.percent') }}</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(address,index) in addressList.info">
            <td class="font-size14 font-Regular normal_color td_height3">{{Number(addressList.basicRank) + index}}</td>
            <td class="font-size14 font-Regular important_color td_height3 click_able" @click="goToAddressDetail(address.address)">{{address.address}}</td>
            <td class="font-size14 font-Regular normal_color td_height3">{{address.balance}}</td>
            <td class="font-size14 font-Regular normal_color td_height3">
              <div class="progress" style="position: relative">
                <div class="progress-bar bg-info" :style="'width:'+ (address.percent * 100).toFixed(6) + '%'">
                  <span class="black-color" style="position: absolute; right: 5px;">{{(address.percent * 100).toFixed(4)}} %</span>
                </div>
              </div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    <turn-the-page :pagesInfo="addressList" :pagesName="'AddressDetail'"></turn-the-page>
  </div>
</template>

<script>
  import {mapState} from 'vuex'
  import ReturnHome from '../common/ReturnHome'
  import ListTitle from '../common/ListTitle'
  import TurnThePage from '../common/TurnThePage'

  export default {
    name: "address-list-page",
    components: {ReturnHome, ListTitle, TurnThePage},
    created() {
      this.getAddressListInfo()
    },
    watch: {
      '$route': 'getAddressListInfo'
    },
    computed: {
      ...mapState({
        addressList: state => state.AddressListPage.AddressListDetail,
      }),
    },
    methods: {
      getAddressListInfo() {
        this.$store.dispatch('getAddressListPage', this.$route.params).then()
      },
      goToAddressDetail(address) {
        if (this.$route.params.net == undefined) {
          this.$router.push({
            name: 'AddressDetail',
            params: {address: address, pageSize: 10, pageNumber: 1}
          })
        } else {
          this.$router.push({
            name: 'AddressDetailTest',
            params: {address: address, pageSize: 10, pageNumber: 1, net: 'testnet'}
          })
        }
      }
    }
  }
</script>

<style scoped>
  .blp-ab-border-top-none{
    border-top: none;
  }

  .table-margin-bottom {
    margin-bottom: 16px;
  }
</style>
