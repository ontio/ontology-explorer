import axios from 'axios'
import * as types from "../mutation-type"

export default {
  state: {
    BlockListDetail: {
      info: '',
    }
  },
  mutations: {
    [types.SET_BLOCK_LIST_PAGE](state, payload) {
      state.BlockListDetail = payload.info
    }
  },
  actions: {
    getBlockListPage({dispatch, commit}, $param) {
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;

      return axios.get(apiUrl + '/blocklist/' + $param.pageSize + '/' + $param.pageNumber).then(response => {
        let msg = response.data
        let blocks = msg.Result.BlockList
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

        // 将bookkeeper拆成数组
        for (let index in blocks) {
          blocks[index].BookKeeper = blocks[index].BookKeeper.split('&')
        }

        let info = {
          info: blocks,
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
          type: types.SET_BLOCK_LIST_PAGE,
          info: info
        })
      }).catch(error => {
        console.log(error)
      })
    }
  }
}
