<template>
  <div class="container container-margin-top">
    <list-title :name="$t('nodes.stakeListTit')"></list-title>

    <!--To next round-->
    <div class="row justify-content-center">
      <div class="col-8 col-countdown text-center">
        <span><img class="img-timer" src="./../../assets/nodes/timer.png" alt=""></span>
        <span class="normal_color font-size18">{{ $t('nodes.toNextRound') }}</span>
        <span class="important_color font-size18 font-blod">{{countdown}}</span>
        <span class="normal_color font-size18">{{ $t('nodes.blocks') }}</span>
      </div>
    </div>

    <div class="row justify-content-center">
      <div class="col-xl-4 col-lg-5 col-md-8 col-sm-10 col-10 col-guide">
        <span style="">{{ $t('nodes.guideTxt') }}</span>
        <span class="font-blod">
          <a class="here-color pointer2"
             href="https://medium.com/ontologynetwork/owallet-stake-authorization-feature-released-11776706bb34"
             target="_blank">{{ $t('nodes.here') }}</a>
        </span>
      </div>
    </div>

    <!--Node authorization data process bar-->
    <div v-show="fetchProcess !== 100" class="node-progress-row">
      <div class="row">
        <div class="col text-center">
          <div class="important_color"><p>{{ $t('nodes.processTip') }}</p></div>
        </div>
      </div>
      <div class="row">
        <div class="col">
          <div class="progress">
            <div class="progress-bar progress-bar-striped progress-bar-animated"
                 :style="'width:' + fetchProcess + '%'"></div>
          </div>
        </div>
      </div>
    </div>

    <!--The node list-->
    <div v-show="fetchProcess === 100" class="row justify-content-center">
      <div class="col">
        <div class="table-responsive">
          <table class="table table-hover">
            <thead>
            <tr>
              <th class="trl-tab-border-top-none font-size18" scope="col">{{ $t('nodes.rank') }}</th>
              <th class="trl-tab-border-top-none font-size18" scope="col">{{ $t('nodes.name') }}</th>
              <th class="trl-tab-border-top-none font-size18" scope="col">{{ $t('all.ontId') }}</th>
              <th class="trl-tab-border-top-none font-size18" scope="col">{{ $t('all.address') }}</th>
              <th class="trl-tab-border-top-none font-size18" scope="col">{{ $t('nodes.reward') }}
                <a href="#" data-toggle="tooltip" :title="$t('nodes.tooltipTit')" class="tooltip-style">
                  <i class="fa fa-info-circle" aria-hidden="true"></i>
                </a>
              </th>
              <th class="trl-tab-border-top-none font-size18" scope="col">{{ $t('nodes.stake') }}</th>
              <th class="trl-tab-border-top-none font-size18" scope="col">{{ $t('nodes.process') }}</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="nL in nodeList" class="font-size14 font-Regular p-tb-18">
              <td><div class="rank-style font-blod">{{ nL.rank }}</div></td>
              <td class="important_color font-blod pointer" @click="toNodeDetail(nL.pk)">{{ nL.name }}</td>
              <td class="f-color pointer2" @click="toOntIdDetailPage(nL.ontId)">{{ nL.ontId.substr(0,12)}}...{{nL.ontId.substr(38) }}</td>
              <td class="f-color pointer2" @click="goToAddressDetail(nL.address)">{{ nL.address.substr(0,8)}}...{{nL.address.substr(26) }}</td>
              <td class="s_color">{{ nL.nodeProportion }}</td>
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
  import $ from 'jquery'

  //提示框
  $(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();
  });

	export default {
    name: "stake-authorization",
    created() {
      this.getNodeListInfo()
    },
    computed: {
      ...mapState({
        nodeList: state => state.NodeAuthorization.AuthorizationList,
        countdown: state => state.NodeAuthorization.Countdown,
        fetchProcess: state => state.NodeAuthorization.fetchProcess
      })
    },
    methods: {
      getNodeListInfo() {
        this.$store.dispatch('fetchNodeList', this.$route.params).then();
        this.$store.dispatch('fetchBlockCountdown', this.$route.params).then()
      },
      toNodeDetail($pk) {
        this.$router.push({name: 'NodeTeamDetail', params: {pk: $pk}})
      },
      toOntIdDetailPage($OntId) {
        if (this.$route.params.net === 'testnet') {
          this.$router.push({name: 'OntIdDetailTest', params: {ontid: $OntId, net: "testnet"}})
        } else {
          this.$router.push({name: 'OntIdDetail', params: {ontid: $OntId}})
        }
      },
      goToAddressDetail(address) {
        if (this.$route.params.net === 'testnet') {
          this.$router.push({
            name: 'AddressDetailTest',
            params: {address: address, pageSize: 20, pageNumber: 1, net: 'testnet'}
          })
        } else {
          this.$router.push({
            name: 'AddressDetail',
            params: {address: address, pageSize: 20, pageNumber: 1}
          })
        }
      }
    }
  }
</script>

<style scoped>
  .col-countdown {
    padding: 0 15px;
    margin-bottom: 24px;
  }

  .img-timer {
    width: 21px;
    margin-right: 21px;
    margin-top: -5px;
  }

  .col-guide {
    margin-bottom: 48px;
    background: #32A4BE;
    color: #BBD5DD;
    padding: 15px 30px;
  }

  .here-color {
    color: white;
  }

  .tooltip-style {
    padding-left: 6px;
    color: #AAB3B4;
    font-size: 16px;
  }

  .node-progress-row {
    margin: 30px 0;
  }

  .progress-bar {
    background-color: #32a4be;
  }

  .rank-style {
    text-align: center;
    width:45px;
    height:19px;
    color: white;
    background:rgba(50,164,190,1);
    border-radius:10px;
  }

  .p-tb-18 > td {
    padding: 18px 12px;
  }
</style>
