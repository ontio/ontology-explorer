import axios from 'axios'
import * as types from "../mutation-type"

export default {
  state: {
    BlockDetail: {
      info: '',
      lastBlockHeight: '',
      nextBlockHeight: '',
    }
  },
  mutations: {
    [types.SET_BLOCK_DETAIL_PAGE](state, payload) {
      state.BlockDetail.info = payload.info
      state.BlockDetail.lastBlockHeight = payload.lastBlockHeight
      state.BlockDetail.nextBlockHeight = payload.nextBlockHeight
    }
  },
  actions: {
    getBlockDetailPage({dispatch, commit},$param) {
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;

      return axios.get(apiUrl + '/block/'+$param.param).then(response => {
        let msg = response.data
        let blockData = msg.Result

        // 将bookkeeper拆成数组
        blockData.BookKeeper = blockData.BookKeeper.split('&')

        let blockHeight = msg.Result.Height
        let nextBlock
        if (blockHeight>1){
          nextBlock = blockHeight-1
        }else{
          nextBlock = 1
        }
        let lastBlock = blockHeight+1

        commit({
          type: types.SET_BLOCK_DETAIL_PAGE,
          info: blockData,
          lastBlockHeight: lastBlock,
          nextBlockHeight: nextBlock,
        })
      }).catch(error => {
        console.log(error)
      })
    },
  }
}
