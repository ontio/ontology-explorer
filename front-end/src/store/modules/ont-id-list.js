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
    getOntIdList({dispatch, commit},$param) {
      let used_url 
      if($param.net=="testnet"){
        used_url = process.env.TEST_API_URL
      }else{
        used_url = process.env.API_URL
      }
      return axios.get(used_url + '/ontidlist/5').then(response => {
        let msg = response.data
        /* console.log(msg.Result) */

        commit({
          type: types.SET_ONT_ID_LIST,
          info: msg.Result
        })
      }).catch(error => {
        console.log(error)
      })
    },
  }
}
