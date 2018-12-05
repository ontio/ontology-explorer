import axios from 'axios'
import * as types from "../mutation-type"

export default {
  state: {
    AddressDetail: {
      info: '',
    }
  },
  mutations: {
    [types.SET_ADDRESS_DETAIL_PAGE](state, payload) {
      state.AddressDetail.info = payload.info
    }
  },
  actions: {
    getAddressDetailPage({dispatch, commit}, $param) {
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;

      return axios.get(apiUrl + '/address/' + $param.address + '/' + $param.pageSize + '/' + $param.pageNumber).then(response => {
        let msg = response.data

        // let allPageNum = msg.Result.TxnTotal
        // let finalPageNum = parseInt(allPageNum/20)+1
        let lastPageNum = 1
        if ($param.pageNumber > 1) {
          lastPageNum = $param.pageNumber - 1
        }
        let nextPageNum = $param.pageNumber - 1 + 2
        if (msg.Result.TxnList.length !== 20) { // 不足20表示是最后一页
          nextPageNum = false
        }
        // let nextPageNum = finalPageNum
        // if ($param.pageNumber<finalPageNum){
        //   nextPageNum = $param.pageNumber-1+2
        // }

        let info = {
          info: msg.Result,
          // allPage: allPageNum,
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
          // finalPage: {
          //   pageSize: '20',
          //   pageNumber: finalPageNum
          // }
        }
        commit({
          type: types.SET_ADDRESS_DETAIL_PAGE,
          info: info,
        })
      }).catch(error => {
        console.log(error)
      })
    },

    getAddressDetailAllData({dispatch}, $param) {
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;
      let allData = [];
      let pageSize = 20;
      let pageNum = 1;

      return axios.get(apiUrl + '/address/' + $param.address + '/' + pageSize + '/' + pageNum).then(response => {
        let msg = response.data.Result.TxnList;
        pageSize = msg.length;
        allData.push(msg)
      }).catch(error => {
        console.log(error)
      });
    }
  }
}
