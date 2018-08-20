import axios from 'axios'
import * as types from "../mutation-type"

export default {
  state: {
    LatestBlockList: {
      info: ''
    }
  },
  mutations: {
    [types.SET_BLOCK_LIST](state, payload) {
      state.LatestBlockList.info = payload.info
    }
  },
  actions: {
    getBlockList({dispatch, commit},$param) {
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;

      return axios.get(apiUrl + '/blocklist/5').then(response => {
        var msg = JSON.parse(response.request.response)
        commit({
          type: types.SET_BLOCK_LIST,
          info: msg.Result
        })
      }).catch(error => {
        console.log(error)
      })
    },
  }
}
