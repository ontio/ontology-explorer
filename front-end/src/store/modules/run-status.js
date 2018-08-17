import axios from 'axios'
import * as types from "../mutation-type"

export default {
  state: {
    BlockStatus: {
      info: ''
    },
    GenerateTime: {
      info: ''
    }
  },
  mutations: {
    [types.SET_RUN_STATUS](state, payload) {
      state.BlockStatus.info = payload.info
    },
    [types.SET_GENERATE_TIME](state, payload) {
      state.GenerateTime.info = payload.info
    }
  },
  actions: {
    getRunStatus({dispatch, commit},$param) {
      let used_url 
      if($param.net=="testnet"){
        used_url = process.env.TEST_API_URL
      }else{
        used_url = process.env.API_URL
      }
      return axios.get(used_url + '/summary').then(response => {
        var msg = JSON.parse(response.request.response)

        return axios.get(process.env.EXPLORE_URL + 'getAssetHolderCount?qid=1&contract=0100000000000000000000000000000000000000').then(res => {
          // 增加持仓地址数量查询，lyx
          msg.Result['AddressCount'] = res.data.result

          commit({
            type: types.SET_RUN_STATUS,
            info: msg.Result
          })
        })
      }).catch(error => {
        console.log(error)
      })
    },
    generateTime({dispatch, commit},$param){
      let used_url 
      if($param.net=="testnet"){
        used_url = process.env.TEST_API_URL
      }else{
        used_url = process.env.API_URL
      }      
      return axios.get(used_url + '/block/generatetime/'+$param.amount).then(response => {
        var msg = JSON.parse(response.request.response)
        /* console.log("time",msg) */
        commit({
          type: types.SET_GENERATE_TIME,
          info: msg.Result
        })
      }).catch(error => {
        console.log(error)
      })      
    }
  }
}
