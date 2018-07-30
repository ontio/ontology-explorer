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
      let used_url 
      if($param.net=="testnet"){
        used_url = process.env.TEST_API_URL
      }else{
        used_url = process.env.API_URL
      }
      return axios.get(used_url + '/blocklist/5').then(response => {
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
