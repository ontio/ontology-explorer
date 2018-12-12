import axios from 'axios'
import * as types from "../mutation-type"
import HelperTool from "./../../helpers/helper"

export default {
  state: {
    Contracts: {info: ''},
    Contract: {info: ''}
  },
  mutations: {
    [types.SET_CONTRACTS_DATA](state, payload) {
      state.Contracts = payload.info
    },
    [types.SET_CONTRACT_DATA](state, payload) {
      state.Contract = payload.info
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
    getContracts({dispatch, commit}, $param) {
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;
      let url = apiUrl + '/contract/' + $param.pageSize + '/' + $param.pageNumber;

      return axios.get(url).then(response => {
        let list = response.data.Result.ContractList;
        let totalPage = Math.ceil(parseFloat(response.data.Result.Total) / $param.pageSize);

        let info = {
          info: list,
          totalCount: response.data.Result.Total,
          allPage: totalPage,
          firstPage: {
            pageSize: $param.pageSize,
            pageNumber: 1
          },
          lastPage: {
            pageSize: $param.pageSize,
            pageNumber: Number($param.pageNumber) - 1
          },
          nextPage: {
            pageSize: $param.pageSize,
            pageNumber: Number($param.pageNumber) + 1
          },
          finalPage: {
            pageSize: $param.pageSize,
            pageNumber: totalPage
          },
          basicRank: (Number($param.pageNumber) - 1) * $param.pageSize + 1
        };

        commit({
          type: types.SET_CONTRACTS_DATA,
          info: info
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
    getContract({dispatch, commit}, $param) {
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;
      let url = apiUrl + '/contract/' + $param.contractHash + '/' + $param.pageSize + '/' + $param.pageNumber;

      return axios.get(url).then(response => {
        let list = response.data.Result;
        let totalPage = Math.ceil(parseFloat(response.data.Result.Total) / $param.pageSize);

        // string to json
        list.ABI = HelperTool.HelperTools.strToJson(list.ABI);
        list.Code = HelperTool.HelperTools.strToJson(list.Code);
        list.ContactInfo = HelperTool.HelperTools.strToJson(list.ContactInfo);

        let info = {
          info: list,
          totalCount: response.data.Result.Total,
          allPage: totalPage,
          firstPage: {
            pageSize: $param.pageSize,
            pageNumber: 1
          },
          lastPage: {
            pageSize: $param.pageSize,
            pageNumber: Number($param.pageNumber) - 1
          },
          nextPage: {
            pageSize: $param.pageSize,
            pageNumber: Number($param.pageNumber) + 1
          },
          finalPage: {
            pageSize: $param.pageSize,
            pageNumber: totalPage
          },
          basicRank: (Number($param.pageNumber) - 1) * $param.pageSize + 1
        };

        commit({
          type: types.SET_CONTRACT_DATA,
          info: info
        })
      }).catch(error => {
        console.log(error)
      })
    }
  }
}
