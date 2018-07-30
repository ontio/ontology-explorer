import axios from 'axios'
import * as types from "../mutation-type"

export default {
  state: {
    OntIdDetail: {
      info: '',
    }
  },
  mutations: {
    [types.SET_ONT_ID_DETAIL_PAGE](state, payload) {
      state.OntIdDetail.info = payload.info
    }
  },
  actions: {
    getOntIdDetailPage({dispatch, commit},$param) {
      let used_url 
      if($param.net=="testnet"){
        used_url = process.env.TEST_API_URL
      }else{
        used_url = process.env.API_URL
      }
      return axios.get(used_url + '/ontid/'+$param.ontid+'/20/1').then(response => {
        let msg = response.data

        commit({
          type: types.SET_ONT_ID_DETAIL_PAGE,
          info: msg.Result,
        })
      }).catch(error => {
        console.log(error)
      })
    },
  }
}
