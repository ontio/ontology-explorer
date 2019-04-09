import axios from 'axios'
import * as types from "../mutation-type"

export default {
  state: {
    List: {},
    Detail: {}
  },
  mutations: {
    [types.SET_ADDRESS_LIST_PAGE](state, payload) {
      state.List = payload.info
    },
    [types.SET_ADDRESS_DETAIL_PAGE](state, payload) {
      state.Detail = payload.info
    }
  },
  actions: {
    GetAddressList({dispatch, commit}, $param) {
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_EXPLORE_URL : process.env.EXPLORE_URL;
      let token = $param.token === "ont" ? "0100000000000000000000000000000000000000" : "0200000000000000000000000000000000000000";
      let url = apiUrl + 'getAssetHolder?qid=1&contract=' + token + '&'
        + 'from=' + (($param.pageNumber - 1) * $param.pageSize) + '&count=' + $param.pageSize;

      return axios.get(url).then(response => {
        commit({
          type: types.SET_ADDRESS_LIST_PAGE,
          info: {
            list: response.data.result,
            total: 1000,
            basicRank: (Number($param.pageNumber) - 1) * $param.pageSize + 1
          }
        })
      }).catch(error => {
        console.log(error)
      })
    },
    GetAddressDetail({dispatch, commit}, $param) {
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;

      return axios.get(apiUrl + '/address/' + $param.address + '/' + $param.pageSize + '/' + $param.pageNumber).then(response => {
        commit({
          type: types.SET_ADDRESS_DETAIL_PAGE,
          info: {
            list: response.data.Result,
            total:
              response.data.Result.TxnList.length < Number($param.pageSize)
                ? (Number($param.pageSize) * (Number($param.pageNumber) - 1)) + response.data.Result.TxnList.length
                : Number($param.pageSize) * (Number($param.pageNumber) + 1)
          }
        })
      }).catch(error => {
        console.log(error)
      })
    },
    getAddressDetailAllData({dispatch}, $param) {
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;
      let url = apiUrl + '/address/queryaddressinfo/' + $param.address;

      return axios.get(url).then(response => {
        return response.data.Result.TxnList
      }).catch(error => {
        console.log(error);
        return false
      });
    }
  }
}
