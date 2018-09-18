<template>
  <div class="container container-margin-top">
    <return-home></return-home>
    <list-title :name="$t('nodes.stakeListTit')"></list-title>

    <div class="row justify-content-center">
      <div class="col-8 col-countdown text-center">
        <img class="img-timer" src="./../../assets/nodes/timer.png" alt="">

        <div class="row">
          <div class="col font-size18 normal_color">{{ $t('nodes.toNextRound') }}</div>
        </div>
        <div class="row">
          <div class="col font-size24">
            {{countdown}}<span class="font-size18 normal_color margin-left-20">{{ $t('nodes.blocks') }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="row justify-content-center">
      <div class="col">
        <div class="table-responsive">
          <table class="table table-hover">
            <thead>
            <tr>
              <th class="trl-tab-border-top-none font-size18" scope="col">{{ $t('nodes.rank') }}</th>
              <th class="trl-tab-border-top-none font-size18" scope="col">{{ $t('nodes.name') }}</th>
              <th class="trl-tab-border-top-none font-size18" scope="col">{{ $t('nodes.currentStake') }}</th>
              <th class="trl-tab-border-top-none font-size18" scope="col">{{ $t('nodes.process') }}</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="nL in nodeList" class="font-size14 font-Regular">
              <td class="normal_color">{{ nL.rank }}</td>
              <td class="normal_color">{{ nL.name }}</td>
              <td class="normal_color">{{ nL.currentStake }}</td>
              <td class="normal_color">{{ nL.process }}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapState} from 'vuex'

	export default {
    name: "stake-authorization",
    created() {
      this.getNodeListInfo()
    },
    computed: {
      ...mapState({
        nodeList: state => state.NodeAuthorization.AuthorizationList,
        countdown: state => state.NodeAuthorization.Countdown
      })
    },
    methods: {
      getNodeListInfo() {
        this.$store.dispatch('fetchNodeList', this.$route.params).then();
        this.$store.dispatch('fetchBlockCountdown', this.$route.params).then()
      }
    }
  }
</script>

<style scoped>
  .col-countdown {
    padding: 15px;
    border: #ebebeb solid 1px;
    background: white;
    margin-bottom: 30px;
  }

  .margin-left-20 {
    margin-left: 20px;
  }

  .img-timer {
    float: left;
    margin: 15px;
  }
</style>
