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
    getBlockList({dispatch, commit}) {
      
      return axios.get(process.env.API_URL + '/blocklist/5').then(response => {
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
