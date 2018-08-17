import axios from 'axios'
import * as types from "../mutation-type"

export default {
  state: {
    AddressListDetail: {
      info: '',
    }
  },
  mutations: {
    [types.SET_ADDRESS_LIST_PAGE](state, payload) {
      state.AddressListDetail = payload.info
    }
  },
  actions: {
    getAddressListPage({dispatch, commit}, $param) {
      let used_url
      if ($param.net == "testnet") {
        used_url = process.env.EXPLORE_URL
      } else {
        used_url = process.env.EXPLORE_URL
      }
      return axios.get(used_url + 'getAssetHolder?qid=1&contract=0100000000000000000000000000000000000000&from=' + ($param.pageNumber - 1) + '&count=' + $param.pageSize).then(response => {
        // let msg =
        // let allPageNum = msg.Result.Total
        // let finalPageNum = parseInt(allPageNum/10)+1
        // let lastPageNum = 1
        // if ($param.pageNumber>1){
        //   lastPageNum = $param.pageNumber-1
        // }
        // let nextPageNum = finalPageNum
        // if ($param.pageNumber<finalPageNum){
        //   nextPageNum = $param.pageNumber-1+2
        // }

        let info = {
          info: response.data.result,
          allPage: 10,
          firstPage: {
            pageSize: '10',
            pageNumber: 1
          },
          lastPage: {
            pageSize: '10',
            pageNumber: 10000
          },
          nextPage: {
            pageSize: '10',
            pageNumber: $param.pageNumber + 1
          },
          finalPage: {
            pageSize: '10',
            pageNumber: 10000
          }
        }

        commit({
          type: types.SET_ADDRESS_LIST_PAGE,
          info: info
        })
      }).catch(error => {
        console.log(error)
      })
    }
  }
}
