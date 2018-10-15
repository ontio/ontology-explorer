<template>
  <div class="container margin-top-60">
    <return-home></return-home>
    <list-title :name="$t('nodes.detailTit')"></list-title>
    <detail-title-2 :name1="$t('nodes.nodeName')" :val="nodeInfo.nodename"
                    :name2="$t('nodes.stakeOWallet')" url="https://github.com/ontio/OWallet/releases">
    </detail-title-2>

    <!--The ONT-ID and Reward-rate-->
    <detail-block-2 :name1="$t('nodes.address')" :val1="nodeInfo.address" :rows1="'1.2'" :params1="['address', nodeInfo.address]"
                    :name2="$t('nodes.rewardRate')" :val2="nodeDetailInfo.nodeProportion" :rows2="'1.1'" :tip="'true'" :tipTit="$t('nodes.tooltipTit')">
    </detail-block-2>

    <!--The Rank, Stake and Process.-->
    <div class="row">
      <div class="col">
        <div class="detail-col detail-col-left">
          <span class="f-color">{{ $t('nodes.rank2') }}</span>
          <span class="pointer important_color">{{nodeDetailInfo.rank}}</span>
        </div>
      </div>
      <div class="col">
        <div class="detail-col detail-col-middle">
          <span class="f-color">{{ $t('nodes.stake2') }}</span>
          <span class="pointer normal_color">{{nodeDetailInfo.currentStake}}</span>
        </div>
      </div>
      <div class="col">
        <div class="detail-col detail-col-right">
          <span class="f-color">{{ $t('nodes.process2') }}</span>
          <span class="pointer normal_color">{{nodeDetailInfo.process}}</span>
        </div>
      </div>
    </div>

    <!--The ONT-ID and Reward-rate-->
    <detail-block-2 :name1="$t('nodes.ip')" :val1="nodeInfo.ip"
                    :name2="$t('nodes.website')" :val2="nodeInfo.Website">
    </detail-block-2>

    <!--The Intro, Vision and Website.-->
    <detail-block :params="detailParams" :styleVal="'new'"></detail-block>
  </div>
</template>

<script>
  import {mapState} from 'vuex'

	export default {
    name: "node-team-detail",
    data() {
      return {
        nodeDetailInfo: {}
      }
    },
    created() {
      this.getNodeInfo()
    },
    watch: {
      'nodeList': function () {
        for (let i in this.nodeList) {
          if (this.nodeList[i].pk === this.$route.params.pk) {
            this.nodeDetailInfo = this.nodeList[i];
            break
          }
        }
      }
    },
    computed: {
      ...mapState({
        nodeList: state => state.NodeAuthorization.AuthorizationList,
        nodeInfo: state => state.NodeAuthorization.NodeInfo
      }),
      detailParams: function () {
        return [
          {name: this.$t('nodes.intro'), val: this.nodeInfo.Intro, rows: 2},
          {name: this.$t('nodes.vision'), val: this.nodeInfo.Vision, rows: 2}
        ]
      }
    },
    methods: {
      getNodeInfo() {
        this.$store.dispatch('fetchNodeList', this.$route.params).then();

        this.$store.dispatch('getNodeInfo', this.$route.params.pk).then()
      }
    }
  }
</script>

<style scoped>

</style>
