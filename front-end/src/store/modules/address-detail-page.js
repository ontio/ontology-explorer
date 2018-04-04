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
    getAddressDetailPage({dispatch, commit},$param) {
      return axios.get(process.env.API_URL + '/address/'+$param.address+'/'+$param.pageSize+'/'+$param.pageNumber).then(response => {
        let msg = response.data

        commit({
          type: types.SET_ADDRESS_DETAIL_PAGE,
          info: msg.Result,
        })
      }).catch(error => {
        console.log(error)
      })
    },
  }
}
