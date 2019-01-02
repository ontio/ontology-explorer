import axios from 'axios'
import * as types from "../mutation-type"

export default {
  state: {
    List: {},
    Detail: {}
  },
  mutations: {
    [types.SET_ONT_ID_LIST_PAGE](state, payload) {
      state.List = payload.info
    },
    [types.SET_ONT_ID_DETAIL_PAGE](state, payload) {
      state.Detail = payload.info
    }
  },
  actions: {
    GetOntIdList({dispatch, commit},$param) {
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;

      return axios.get(apiUrl + '/ontidlist/'+$param.pageSize+'/'+$param.pageNumber).then(response => {
        commit({
          type: types.SET_ONT_ID_LIST_PAGE,
          info: {
            list: response.data.Result.OntIdList,
            total: response.data.Result.Total
          }
        })
      }).catch(error => {
        console.log(error)
      })
    },
    GetOntIdDetail({dispatch, commit},$param) {
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;

      return axios.get(apiUrl + '/ontid/'+$param.ontid+'/20/1').then(response => {
        let msg = response.data

        commit({
          type: types.SET_ONT_ID_DETAIL_PAGE,
          info: msg.Result,
        })
      }).catch(error => {
        console.log(error)
      })
    }
  }
}
