import axios from 'axios'
import * as types from "../mutation-type"

export default {
  state: {
    TransactionDetail: {
      info: '',
    }
  },
  mutations: {
    [types.SET_TRANSACTION_DETAIL_PAGE](state, payload) {
      state.TransactionDetail.info = payload.info
    }
  },
  actions: {
    getTransactionDetailPage({dispatch, commit},$param) {
      let used_url 
      if($param.net=="testnet"){
        used_url = process.env.TEST_API_URL
      }else{
        used_url = process.env.API_URL
      }
      return axios.get(used_url + '/transaction/'+$param.txnHash).then(response => {
        let msg = response.data

        commit({
          type: types.SET_TRANSACTION_DETAIL_PAGE,
          info: msg.Result,
        })
      }).catch(error => {
        console.log(error)
      })
    },
  }
}
