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
    getRunStatus({dispatch, commit}) {
      return axios.get(process.env.API_URL + '/summary').then(response => {
        var msg = JSON.parse(response.request.response)

        commit({
          type: types.SET_RUN_STATUS,
          info: msg.Result
        })
      }).catch(error => {
        console.log(error)
      })
    },
    generateTime({dispatch, commit},$param){
      
      return axios.get(process.env.API_URL + '/block/generatetime/'+$param.amount).then(response => {
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
