import axios from 'axios'
import * as types from "../mutation-type"

export default {
  state: {
    TransactionListDetail: {}
  },
  mutations: {
    [types.SET_TRANSACTION_LIST_PAGE](state, payload) {
      state.TransactionListDetail = payload.info
    }
  },
  actions: {
    getTransactionListPage({dispatch, commit}, $param) {
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;

      return axios.get(apiUrl + '/transactionlist/' + $param.pageSize + '/' + $param.pageNumber).then(response => {
        let msg = response.data
        let allPageNum = msg.Result.Total
        let finalPageNum = parseInt(allPageNum / 20) + 1
        let lastPageNum = 1
        if ($param.pageNumber > 1) {
          lastPageNum = $param.pageNumber - 1
        }
        let nextPageNum = finalPageNum
        if ($param.pageNumber < finalPageNum) {
          nextPageNum = $param.pageNumber - 1 + 2
        }

        let info = {
          info: msg.Result.TxnList,
          allPage: allPageNum,
          firstPage: {
            pageSize: '20',
            pageNumber: 1
          },
          lastPage: {
            pageSize: '20',
            pageNumber: lastPageNum
          },
          nextPage: {
            pageSize: '20',
            pageNumber: nextPageNum
          },
          finalPage: {
            pageSize: '20',
            pageNumber: finalPageNum
          }
        }

        commit({
          type: types.SET_TRANSACTION_LIST_PAGE,
          info: info
        })
      }).catch(error => {
        console.log(error)
      })
    },
  }
}
