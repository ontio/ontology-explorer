import axios from 'axios'
import * as types from "../mutation-type"

export default {
  state: {
    List: {},
    Detail: {}
  },
  mutations: {
    [types.SET_BLOCK_LIST_PAGE](state, payload) {
      state.List = payload.info
    },
    [types.SET_BLOCK_DETAIL_PAGE](state, payload) {
      state.Detail = payload.info;
    }
  },
  actions: {
    GetBlocks({dispatch, commit}, $param) {
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;

      return axios.get(apiUrl + '/blocklist/' + $param.pageSize + '/' + $param.pageNumber).then(response => {
        let msg = response.data;
        let list = msg.Result.BlockList;

        // 将bookkeeper拆成数组
        for (let index in list) {
          list[index].BookKeeper = list[index].BookKeeper.split('&')
        }

        commit({
          type: types.SET_BLOCK_LIST_PAGE,
          info: {
            info: list,
            total: msg.Result.Total
          }
        })
      }).catch(error => {
        console.log(error)
      })
    },
    GetBlock({dispatch, commit}, $param) {
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;

      return axios.get(apiUrl + '/block/' + $param.param).then(response => {
        let msg = response.data;
        let blockData = msg.Result;

        // 将bookkeeper拆成数组
        blockData.BookKeeper = blockData.BookKeeper.split('&');

        commit({
          type: types.SET_BLOCK_DETAIL_PAGE,
          info: blockData
        })
      }).catch(error => {
        console.log(error)
      })
    }
  }
}
