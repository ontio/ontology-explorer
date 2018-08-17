<template>
  <div class="container container-margin-top">
    <div class="div-block-list-page form-group">
      <div class="row">
        <div class="col-lg-6">
          <p  class="title-more  float-left font-Regular normal_color font-size18 block-detail-page-check-hand" @click="toReturn"><< {{ $t('all.return') }}</p>
        </div>
      </div>
      <div class="row">
        <div class="col-lg-12">
          <p  class="text-center font-size40 font-ExtraLight p_margin_bottom_L normal_color">ADDRESSES - Position Ranking</p>
        </div>
      </div>

      <div class="row justify-content-center">
        <div class="col-lg-12">
          <table class="table table-hover">
            <thead>
            <tr>
              <th class="blp-ab-border-top-none font-size18" style="padding-top:34px;" scope="col">{{ $t('addressList.name') }}</th>
              <th class="blp-ab-border-top-none font-size18" scope="col">{{ $t('addressList.balance') }}</th>
              <th class="blp-ab-border-top-none font-size18" scope="col">{{ $t('addressList.percent') }}</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="address in addressList.info">
              <td class="font-size14 font-Regular important_color td_height3 click_able" @click="goToAddressDetail(address.address)">{{address.address}}</td>
              <td class="font-size14 font-Regular normal_color td_height3">{{address.balance}}</td>
              <td class="font-size14 font-Regular normal_color td_height3">{{(address.percent * 100).toFixed(2)}}%</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <div class="row justify-content-center">
      <!--<div id="page" v-show="addressList.allPage!=1">-->
      <div id="page">
        <ul class="pagination"  >
          <li class="transaction-list-page-check-hand padding0" @click="goToPage(addressList.firstPage)" ><button class="goto_btn"><a>{{$t('page.First')}}</a> </button></li>
          <li class="transaction-list-page-check-hand padding0" @click="goToPage(addressList.lastPage)"><button style="border-left:0px" class="goto_btn"><a>{{$t('page.PreviousPage')}}</a></button></li>
          <li class="transaction-list-page-check-hand padding0" @click="goToPage(addressList.nextPage)"><button style="border-left:0px" class="goto_btn"><a>{{$t('page.NextPage')}}</a></button></li>
          <li class="transaction-list-page-check-hand padding0" @click="goToPage(addressList.finalPage)" ><button style="border-left:0px" class="goto_btn"><a>{{$t('page.Last')}}</a></button> </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapState} from 'vuex'
  import Helper from './../../helpers/helper.js'

  export default {
      name: "address-list-page",

    data() {
      return {
      }
    },
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
        this.$store.dispatch('getAddressListPage',this.$route.params).then(response => {
          /* console.log(response) */
        }).catch(error => {
          console.log(error)
        })
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
      },
      toReturn(){
        if(this.$route.params.net == undefined){
          this.$router.push({ name:'Home'})
        }else{
          this.$router.push({ name:'HomeTest', params:{net:'testnet'}})
        }
      },
      goToPage($Page){
        if(this.$route.params.net == undefined){
          this.$router.push({ name:'addressList', params:{pageSize:$Page.pageSize,pageNumber:$Page.pageNumber}})
        }else{
          this.$router.push({ name:'addressListTest', params:{pageSize:$Page.pageSize,pageNumber:$Page.pageNumber,net:'testnet'}})
        }
      },
      getTime($time){
        return Helper.getDateTime($time)
      },
      getDate($time){
        return Helper.getDate($time)
      },
    }
  }
</script>

<style scoped>
  .div-block-list-page {
    /* border: 1px solid rgba(0, 0, 0, 0.1); */
    border-radius: 0.25rem;
    padding: 15px;
  }
  .block-list-page-hr {
    height: 1px;
  }
  .blp-ab-border-top-none{
    border-top: none;
  }
  .block-list-page-underline{
    cursor: pointer;
    text-decoration:underline;
  }
  .block-list-page-check-hand{
    cursor: pointer;
  }
</style>
