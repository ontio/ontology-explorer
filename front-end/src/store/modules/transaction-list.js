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
      let used_url 
      if($param.net=="testnet"){
        used_url = process.env.TEST_API_URL
      }else{
        used_url = process.env.API_URL
      }
      return axios.get(used_url + '/transactionlist/5').then(response => {
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
