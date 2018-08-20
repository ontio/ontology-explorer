import axios from 'axios'
import * as types from "../mutation-type"

export default {
  state: {
    BlockStatus: {
      info: ''
    },
    GenerateTime: {
      info: ''
    }
  },
  mutations: {
    [types.SET_RUN_STATUS](state, payload) {
      state.BlockStatus.info = payload.info
    },
    [types.SET_GENERATE_TIME](state, payload) {
      state.GenerateTime.info = payload.info
    }
  },
  actions: {
    getRunStatus({dispatch, commit}, $param) {
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL

      return axios.get(apiUrl + '/summary').then(response => {
        let msg = JSON.parse(response.request.response);

        // 增加持仓地址数量查询，lyx
        return axios.get(process.env.EXPLORE_URL + 'getAssetHolderCount?qid=1&contract=0100000000000000000000000000000000000000').then(res => {
          msg.Result['AddressCount'] = res.data.result;
          commit({
            type: types.SET_RUN_STATUS,
            info: msg.Result
          })
        })
      }).catch(error => {
        console.log(error)
      })
    },
    generateTime({dispatch, commit}, $param) {
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL

      return axios.get(apiUrl + '/block/generatetime/' + $param.amount).then(response => {
        let msg = JSON.parse(response.request.response);

        commit({
          type: types.SET_GENERATE_TIME,
          info: msg.Result
        })
      }).catch(error => {
        console.log(error)
      })
    }
  }
}
