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
    getTransactionDetailPage({dispatch, commit}, $param) {
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;

      return axios.get(apiUrl + '/transaction/' + $param.txnHash).then(res => {
        console.log(res.data.Result)
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
