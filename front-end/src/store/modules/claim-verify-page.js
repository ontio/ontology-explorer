import axios from 'axios'
import * as types from "../mutation-type"

export default {
  state: {
    ClaimInfo: {
      info: ''
    }
  },
  mutations: {
    [types.GET_CLAIM](state, payload) {
      state.ClaimInfo.info = payload.info
    }
  },
  actions: {
    getClaim({dispatch, commit},$param) {
      let formData = {
        Action: "sendrawtransaction",
        Version: "1.0.0",
        Type: ""
      }
      return axios.post( 'https://dev.ont.io/ont/claim/verify/'+$param.cardId+"/"+$param.ownerOntId,formData).then(response => {
        var msg = JSON.parse(response.request.response)
        commit({
          type: types.GET_CLAIM,
          info: msg.Result
        })
      }).catch(error => {
        console.log(error)
      })
    }
  }
}
