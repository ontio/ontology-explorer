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
      return axios.get(process.env.API_URL + '/transaction/'+$param.txnHash).then(response => {
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
