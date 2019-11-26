import axios from 'axios'
import * as types from "../mutation-type"
import HelperTool from "./../../helpers/helper"

export default {
  state: {
    List: {},
    Detail: {}
  },
  mutations: {
    [types.SET_TOKENS_DATA](state, payload) {
      state.List = payload.info
    },
    [types.SET_TOKEN_DATA](state, payload) {
      state.Detail = payload.info
    }
  },
  actions: {
    /**
     * Get Token List Data
     *
     * @param dispatch
     * @param commit
     * @param $param
     * @return {Promise<AxiosResponse | never>}
     */
    GetTokens({dispatch, commit}, $param) {
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;
      let url = apiUrl + '/oepcontract/' + $param.type + '/' + $param.pageSize + '/' + $param.pageNumber;

      return axios.get(url).then(response => {
        commit({
          type: types.SET_TOKENS_DATA,
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
     * Get Token Detail Data
     *
     * @param dispatch
     * @param commit
     * @param $param
     * @return {Promise<AxiosResponse | never>}
     */
    GetToken({dispatch, commit}, $param) {
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;
      let url = apiUrl + '/oepcontract/' + $param.type + '/' + $param.contractHash + '/' + $param.pageSize + '/' + $param.pageNumber;

      return axios.get(url).then(response => {
        let list = response.data.Result;

        // string to json
        list.ABI = HelperTool.HelperTools.strToJson(list.ABI);
        list.Code = HelperTool.HelperTools.strToJson(list.Code);
        list.ContactInfo = HelperTool.HelperTools.strToJson(list.ContactInfo);

        // OEP-5
        if ($param.type === 'oep5') {
          for (let key in list.TxnList) {
            list.TxnList[key].Jsonurl = HelperTool.HelperTools.strToJson(list.TxnList[key].Jsonurl)
          }
        }

        commit({
          type: types.SET_TOKEN_DATA,
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
