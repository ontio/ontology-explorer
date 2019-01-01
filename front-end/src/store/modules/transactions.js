import axios from 'axios'
import * as types from "../mutation-type"

export default {
  state: {
    List: {},
    Detail: {}
  },
  mutations: {
    [types.SET_TRANSACTION_LIST_PAGE](state, payload) {
      state.List = payload.info
    },
    [types.SET_TRANSACTION_DETAIL_PAGE](state, payload) {
      state.Detail = payload.info
    }
  },
  actions: {
    GetTransactions({dispatch, commit}, $param) {
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;

      return axios.get(apiUrl + '/transactionlist/' + $param.pageSize + '/' + $param.pageNumber).then(response => {
        commit({
          type: types.SET_TRANSACTION_LIST_PAGE,
          info: {
            list: response.data.Result.TxnList,
            total: response.data.Result.Total
          }
        })
      }).catch(error => {
        console.log(error)
      })
    },
    GetTransaction({dispatch, commit}, $param) {
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;

      return axios.get(apiUrl + '/transaction/' + $param.txnHash).then(res => {
        commit({
          type: types.SET_TRANSACTION_DETAIL_PAGE,
          info: res.data.Result,
        })
      }).catch(err => {
        console.log(err)
      })
    },
  }
}
