<template>
  <div class="container container-margin-top">
    <return-home></return-home>
    <list-title :name="$t('blockList.name')"></list-title>

    <div class="row justify-content-center">
      <table class="table table-hover">
        <thead>
        <tr>
          <th class="font-size18" scope="col">{{ $t('all.height') }}</th>
          <th class="font-size18" scope="col">{{ $t('blockList.TxnNum') }}</th>
          <th class="font-size18" scope="col">{{ $t('blockList.BlockSize') }}( {{ $t('all.byte') }} )</th>
          <th class="font-size18" scope="col">{{ $t('all.time') }}</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="block in blockListDetail.info">
          <td class="font-size14 font-Regular important_color td_height3 click_able"  @click="toBlockDetailPage(block.Height)">{{block.Height}}</td>
          <td class="font-size14 font-Regular normal_color td_height3">{{block.TxnNum}}</td>
          <td class="font-size14 font-Regular normal_color td_height3">{{block.BlockSize}}</td>
          <td class="font-size14 font-Regular normal_color td_height3">{{$HelperTools.getTransDate(block.BlockTime)}}</td>
        </tr>
        </tbody>
      </table>
    </div>

    <turn-the-page :pagesInfo="blockListDetail" :pagesName="'blockListDetail'"></turn-the-page>
  </div>
</template>

<script>
  import {mapState} from 'vuex'
  import ReturnHome from '../common/ReturnHome'
  import ListTitle from '../common/ListTitle'
  import TurnThePage from '../common/TurnThePage'

  export default {
    name: "block-list-page",
    components: {ReturnHome, ListTitle, TurnThePage},
    created() {
      this.getBlockListPage()
    },
    watch: {
      '$route': 'getBlockListPage'
    },
    computed: {
      ...mapState({
        blockListDetail: state => state.BlockListPage.BlockListDetail,
      }),
    },
    methods: {
      getBlockListPage() {
        this.$store.dispatch('getBlockListPage', this.$route.params).then()
      },
      toBlockDetailPage($blockHeight) {
        if (this.$route.params.net == undefined) {
          this.$router.push({name: 'blockDetail', params: {param: $blockHeight}})
        } else {
          this.$router.push({name: 'blockDetailTest', params: {param: $blockHeight, net: 'testnet'}})
        }
      }
    }
  }
</script>

<style scoped>
</style>
