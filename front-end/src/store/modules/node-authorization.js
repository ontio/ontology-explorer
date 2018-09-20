import axios from 'axios'
import * as types from "../mutation-type"
import numeral from 'numeral'

export default {
  state: {
    AuthorizationList: {},
    Countdown: 0
  },
  mutations: {
    [types.UPDATE_NODE_LIST](state, payload) {
      state.AuthorizationList = payload.info
    },
    [types.UPDATE_COUNTDOWN_BLOCK](state, payload) {
      state.Countdown = payload.info;
    }
  },
  actions: {
    async fetchNodeList({commit}, params) {
      let url = (params.net === 'testnet') ? process.env.TEST_DAPP_NODE_URL : process.env.DAPP_NODE_URL;

      try {
        const peerMap = await Ont.GovernanceTxBuilder.getPeerPoolMap(url);
        const list = [];

        for (let k in peerMap) {
          let item = peerMap[k];
          const attr = await Ont.GovernanceTxBuilder.getAttributes(item.peerPubkey, url);
          item.maxAuthorize = attr.maxAuthorize;
          item.maxAuthorizeStr = numeral(item.maxAuthorize).format('0,0');
          item.totalPosStr = numeral(item.totalPos).format('0,0');
          const nodeProportion = attr.newPeerCost + '%';
          const userProportion = (100 - attr.newPeerCost) + '%';
          item.nodeProportion = nodeProportion + ' / ' + userProportion;

          // 只有1和2显示
          if(item.status === 1 || item.status === 2) {
            list.push(item)
          }
        }

        list.sort((v1, v2) => {
          if ((v2.initPos + v2.totalPos) > (v1.initPos + v1.totalPos)) {
            return 1;
          } else if ((v2.initPos + v2.totalPos) < (v1.initPos + v1.totalPos)) {
            return -1;
          } else {
            return 0;
          }
        });

        list.forEach((item, index) => {
          item.rank = index + 1;
          item.currentStake = item.initPos + item.totalPos;
          item.process = Number((item.totalPos + item.initPos) * 100 / (item.initPos + item.maxAuthorize)).toFixed(2) + '%';
          item.pk = item.peerPubkey;
          item.name = 'Node No.' + (index + 1);
          if (item.peerPubkey === '02f4c0a18ae38a65b070820e3e51583fd3aea06fee2dc4c03328e4b4115c622567') {//for test
            item.name = 'Node1 To Authorize'
          }
          if (item.pk === '03f6149b3a982c046912731d6374305e2bc8e278fa90892f6f20a8ee85c1d5443f') {//for test
            item.name = 'Node2 To Authorize'
          }
        });

        // console.log(list)

        commit({
          type: types.UPDATE_NODE_LIST,
          info: list,
        })
      } catch (err) {
        console.log(err);
      }
    },
    async fetchBlockCountdown({commit}, params) {
      let url = (params.net === 'testnet') ? process.env.TEST_DAPP_NODE_URL : process.env.DAPP_NODE_URL;

      const rest = new Ont.RestClient(url);
      try {
        const view = await Ont.GovernanceTxBuilder.getGovernanceView(url);
        const blockRes = await rest.getBlockHeight();
        const blockHeight = blockRes.Result;
        const countdown = 120000 - (blockHeight - view.height);

        commit({
          type: types.UPDATE_COUNTDOWN_BLOCK,
          info: countdown,
        })
      } catch (err) {
        console.log(err)
      }
    }
  }
}
