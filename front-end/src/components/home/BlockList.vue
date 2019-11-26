<template>
  <div class="div-block-list">
    <div class="row title-color title-hover" @click="toBlockListPage">
      <div class="col-8 block-title-wrapper">
        <p class="title font-blod">{{ $t('blockList.name') }}</p>
      </div>
      <div class="col-4 block-title-wrapper">
        <p class="title-more float-right">{{ $t('all.more') }}</p>
      </div>
    </div>

    <div class="row">
      <div v-for="(block,index) in latestBlockList.info" class="col-12 block-item-wrapper">
        <div class="divider-line"></div>
        <div class="row block-item-sub-wrapper">
          <div class="block-item col-6 text-left padding0 block-item-height font700 font-size22 pointer" @click="toBlockDetailPage(block.Height)" style="color:#32a4be;line-height: 27px;">{{block.Height}}</div>
          <div v-if="block.TxnNum ==1" class="block-item col-6 text-right padding0 font-size14">{{block.TxnNum}} Txns</div>
          <div v-else class="block-item col-6 text-right padding0 font-size14">{{block.TxnNum}} Txns</div>
        </div>
        <div class="row block-item-sub-wrapper-s">
          <span class="block-item col-6 text-left padding0 font-size14">{{block.BlockSize}} byte</span>
          <span v-if="$HelperTools.getDateTime(block.BlockTime) < 60" class="block-item col-6 text-right padding0 font-size14 ">{{showtime[index]}}s ago</span>
          <span v-else class="block-item col-6 text-right padding0 font-size14 ">{{getShowDate(block.BlockTime)}} ago</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapState} from 'vuex'

  export default {
    name: "block-list",
    data() {
      return {
        info: [],
        blocktime: [0, 0, 0, 0, 0],
        timestamp: (new Date()).valueOf(),
        showtime: [0, 0, 0, 0, 0]
      }
    },
    created() {
      this.getBlockList()
      this.intervalBlock = setInterval(() => {
        this.getBlockList()
      }, 6000)
      this.intervalBlockstandard = setInterval(() => {
        for (var i = 0; i < 5; i++) {
          var time = this.showtime[i] + 1
          this.$set(this.showtime, i, time)
        }
      }, 1000)
    },
    watch: {
      '$route': 'getBlockList',
      'latestBlockList.info': function () {
        for (var i = 0; i < 5; i++) {
          this.showtime[i] = this.$HelperTools.getDateTime(this.latestBlockList.info[i].BlockTime)
        }
      },
    },
    computed: {
      ...mapState({
        latestBlockList: state => state.BlockList.LatestBlockList
      }),
    },
    methods: {
      getBlockList() {
        this.$store.dispatch('getBlockList', this.$route.params).then()
      },
      toBlockListPage() {
        if (this.$route.params.net == undefined) {
          this.$router.push({name: 'blockListDetail', params: {pageSize: 20, pageNumber: 1}})
        } else {
          this.$router.push({name: 'blockListDetailTest', params: {pageSize: 20, pageNumber: 1, net: "testnet"}})
        }
      },
      toBlockDetailPage($blockHeight) {
        if (this.$route.params.net == undefined) {
          this.$router.push({name: 'blockDetail', params: {param: $blockHeight}})
        } else {
          this.$router.push({name: 'blockDetailTest', params: {param: $blockHeight, net: "testnet"}})
        }
      },
      getShowDate($time) {
        var time = this.$HelperTools.getDateTime($time)
        return this.$HelperTools.getShowDate(time)
      },
      countDownTime: function () {

        for (var i = 0; i < this.info.length; i++) {
          this.info[i].showtime = this.info[i].showtime + 1
        }
      },
      getNewTime: function () {
        let timestamp = (new Date()).valueOf();
        for (let i = 0; i < this.latestBlockList.info.length; i++) {
          this.latestBlockList.info[i].showtime = timestamp
        }
      },
    },
    beforeDestroy() {
      clearInterval(this.intervalBlock)
      clearInterval(this.intervalBlockstandard)
    }
  }
</script>

<style scoped>

</style>
