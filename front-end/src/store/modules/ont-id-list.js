import axios from 'axios'
import * as types from "../mutation-type"

export default {
  state: {
    LatestOntIdList: {
      info: ''
    }
  },
  mutations: {
    [types.SET_ONT_ID_LIST](state, payload) {
      state.LatestOntIdList.info = payload.info
    }
  },
  actions: {
    getOntIdList({dispatch, commit}, $param) {
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;

      return axios.get(apiUrl + '/ontidlist/5').then(response => {
        commit({
          type: types.SET_ONT_ID_LIST,
          info: response.data.Result
        })
      }).catch(error => {
        console.log(error)
      })
    },
  }
}
