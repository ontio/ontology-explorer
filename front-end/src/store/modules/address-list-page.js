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
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_EXPLORE_URL : process.env.EXPLORE_URL;
      let url = apiUrl + 'getAssetHolder?qid=1&contract=0100000000000000000000000000000000000000&'
        + 'from=' + ($param.pageNumber - 1) + '&count=' + $param.pageSize

      return axios.get(url).then(response => {
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
        };

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