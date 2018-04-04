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
    getTransactionList({dispatch, commit}) {
      return axios.get(process.env.API_URL + '/transactionlist/5').then(response => {
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
