import axios from 'axios'
import * as types from "../mutation-type"
import HelperTool from "./../../helpers/helper"

export default {
  state: {
    List: {},
    Detail: {}
  },
  mutations: {
    [types.SET_CONTRACTS_DATA](state, payload) {
      state.List = payload.info
    },
    [types.SET_CONTRACT_DATA](state, payload) {
      state.Detail = payload.info
    }
  },
  actions: {
    /**
     * Get Contract List Data
     *
     * @param dispatch
     * @param commit
     * @param $param
     * @return {Promise<AxiosResponse | never>}
     */
    GetContracts({dispatch, commit}, $param) {
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;
      let url = apiUrl + '/contract/' + $param.pageSize + '/' + $param.pageNumber;

      return axios.get(url).then(response => {
        commit({
          type: types.SET_CONTRACTS_DATA,
          info: {
            list: response.data.Result.ContractList,
            total: response.data.Result.Total
          }
        })
      }).catch(error => {
        console.log(error)
      })
    },
    /**
     * Get Contract Detail Data
     *
     * @param dispatch
     * @param commit
     * @param $param
     * @return {Promise<AxiosResponse | never>}
     */
    GetContract({dispatch, commit}, $param) {
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;
      let url = apiUrl + '/contract/' + $param.contractHash + '/' + $param.pageSize + '/' + $param.pageNumber;

      return axios.get(url).then(response => {
        let list = response.data.Result;

        // string to json
        list.ABI = HelperTool.HelperTools.strToJson(list.ABI);
        list.Code = HelperTool.HelperTools.strToJson(list.Code);
        list.ContactInfo = HelperTool.HelperTools.strToJson(list.ContactInfo);

        commit({
          type: types.SET_CONTRACT_DATA,
          info: {
            list: list,
            total: response.data.Result.Total
          }
        })
      }).catch(error => {
        console.log(error)
      })
    }
  }
}
