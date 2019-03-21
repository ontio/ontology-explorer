<template>
  <div class="e-container container-margin-top">
    <list-title :name="$t('addressList.nickname')"></list-title>

    <ont-pagination :total="addressList.total"></ont-pagination>

    <div class="row justify-content-center">
      <div class="col">
        <div class="table-responsive">
          <table class="table table-hover">
            <thead>
            <tr>
              <th class="font-size18" scope="col">{{ $t('addressList.rank') }}</th>
              <th class="font-size18" scope="col">{{ $t('addressList.name') }}</th>
              <th class="font-size18" scope="col">{{ $t('addressList.balance') }}</th>
              <th class="font-size18" scope="col">{{ $t('addressList.percent') }}</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(address,index) in addressList.list">
              <td class="font-size14 font-Regular normal_color">{{Number(addressList.basicRank) + index}}</td>
              <td class="font-size14 font-Regular important_color pointer"
                  @click="goToAddressDetail(address.address)">
                {{address.address.substr(0,6) + '...' + address.address.substr(28)}}
              </td>
              <td class="font-size14 font-Regular normal_color">{{address.balance}}</td>
              <td class="font-size14 font-Regular normal_color">{{(address.percent * 100).toFixed(4)}}%</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <ont-pagination :total="addressList.total"></ont-pagination>
  </div>
</template>

<script>
  import {mapState} from 'vuex'

  export default {
    created() {
      this.getAddressListInfo()
    },
    watch: {
      '$route': 'getAddressListInfo'
    },
    computed: {
      ...mapState({
        addressList: state => state.Addresses.List
      })
    },
    methods: {
      getAddressListInfo() {
        this.$store.dispatch('GetAddressList', this.$route.params).then()
      },
      goToAddressDetail(address) {
        if (this.$route.params.net == undefined) {
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
</style>
