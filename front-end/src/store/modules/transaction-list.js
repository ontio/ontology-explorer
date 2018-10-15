import axios from 'axios'
import * as types from "../mutation-type"

export default {
  state: {
    LatestTransactionList: {
      info: ''
    }
  },
  mutations: {
    [types.SET_TRANSACTION_LIST](state, payload) {
      state.LatestTransactionList.info = payload.info
    }
  },
  actions: {
    getTransactionList({dispatch, commit},$param) {
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;

      return axios.get(apiUrl + '/transactionlist/5').then(response => {
        var msg = JSON.parse(response.request.response)
        //console.log(msg.Result.TxnList)

        commit({
          type: types.SET_TRANSACTION_LIST,
          info: msg.Result.TxnList
        })
      }).catch(error => {
        console.log(error)
      })
    },
  }
}
