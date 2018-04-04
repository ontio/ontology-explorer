import axios from 'axios'
import * as types from "../mutation-type"

export default {
  state: {
    TransactionListDetail: {
    }
  },
  mutations: {
    [types.SET_TRANSACTION_LIST_PAGE](state, payload) {
      state.TransactionListDetail = payload.info
    }
  },
  actions: {
    getTransactionListPage({dispatch, commit},$param) {
      return axios.get(process.env.API_URL + '/transactionlist/'+$param.pageSize+'/'+$param.pageNumber).then(response => {
        let msg = response.data
        let allPageNum = msg.Result.Total
        let finalPageNum = parseInt(allPageNum/10)+1
        let lastPageNum = 1
        if ($param.pageNumber>1){
          lastPageNum = $param.pageNumber-1
        }
        let nextPageNum = finalPageNum
        if ($param.pageNumber<finalPageNum){
          nextPageNum = $param.pageNumber+1
        }

        let info={
          info: msg.Result.TxnList,
          allPage: allPageNum,
          firstPage: {
            pageSize: '10',
            pageNumber: 1
          },
          lastPage:{
            pageSize: '10',
            pageNumber: lastPageNum
          },
          nextPage:{
            pageSize: '10',
            pageNumber: nextPageNum
          },
          finalPage: {
            pageSize: '10',
            pageNumber: finalPageNum
          }
        }

        commit({
          type: types.SET_TRANSACTION_LIST_PAGE,
          info:info
        })
      }).catch(error => {
        console.log(error)
      })
    },
  }
}
