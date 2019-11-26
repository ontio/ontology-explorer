import axios from 'axios'
import * as types from "../mutation-type"

/**
 * 设置区块链全部统计数据
 *
 * @param list
 * @return {{labels: Array, data: {block: {label: string, list: Array}, txn: {label: string, list: Array}, newAddress: {label: string, list: Array}, activeAddress: {label: string, list: Array}, newOntId: {label: string, list: Array}, activeOntId: {label: string, list: Array}, sumAddress: {label: string, list: Array}, sumOntId: {label: string, list: Array}, ont: {label: string, list: Array}, ong: {label: string, list: Array}}}}
 */
function setAllData(list) {
  // 表格数据格式构造
  let chartData = {
    labels: [],
    data: {
      block: {label: 'blockLbl', list: []},
      txn: {label: 'txnLbl', list: []},
      newAddress: {label: 'newAddressLbl', list: []},
      activeAddress: {label: 'activeAddressLbl', list: []},
      newOntId: {label: 'newOntIdLbl', list: []},
      activeOntId: {label: 'activeOntIdLbl', list: []},
      sumAddress: {label: 'sumAddressLbl', list: []},
      sumOntId: {label: 'sumOntIdLbl', list: []},
      ont: {label: 'ontLbl', list: []},
      ong: {label: 'ongLbl', list: []}
    }
  };

  // 表格数据填充
  for (let listKey in list) {
    chartData.labels.push(list[listKey].Time.length > 5 ? list[listKey].Time.substr(5) : list[listKey].Time);
    chartData.data.block.list.push(list[listKey].BlockCount);
    chartData.data.txn.list.push(list[listKey].TxnCount);
    chartData.data.newAddress.list.push(list[listKey].NewAddress);
    chartData.data.activeAddress.list.push(list[listKey].ActiveAddress);
    chartData.data.newOntId.list.push(list[listKey].OntIdNewCount);
    chartData.data.activeOntId.list.push(list[listKey].OntIdActiveCount);
    chartData.data.sumAddress.list.push(list[listKey].AddressSum);
    chartData.data.sumOntId.list.push(list[listKey].OntIdSum);
    chartData.data.ont.list.push(list[listKey].OntCount);
    chartData.data.ong.list.push(list[listKey].OngCount)
  }

  return chartData;
}

/**
 * 设置合约统计数据
 *
 * @param url
 * @return {{labels: Array, data: {newAddress: {label: string, list: Array}, activeAddress: {label: string, list: Array}, ont: {label: string, list: Array}, ong: {label: string, list: Array}, txn: {label: string, list: Array}}}}
 */
function setContractData(list) {
  // 表格数据格式构造
  let chartData = {
    labels: [],
    data: {
      newAddress: {label: 'newAddressLbl', list: []},
      activeAddress: {label: 'activeAddressLbl', list: []},
      ont: {label: 'ontLbl', list: []},
      ong: {label: 'ongLbl', list: []},
      txn: {label: 'txnLbl', list: []}
    }
  };

  // 表格数据填充
  for (let listKey in list) {
    chartData.labels.push(list[listKey].Time);
    chartData.data.newAddress.list.push(list[listKey].NewAddress);
    chartData.data.activeAddress.list.push(list[listKey].ActiveAddress);
    chartData.data.ont.list.push(list[listKey].OntCount);
    chartData.data.ong.list.push(list[listKey].OngCount);
    chartData.data.txn.list.push(list[listKey].TxnCount)
  }

  return chartData;
}



export default {
  state: {
    StatisticsData: {info: ''},
    ScList: {}
  },
  mutations: {
    [types.SET_STATISTICS_DATA](state, payload) {
      state.StatisticsData = payload.info
    },
    [types.SET_SC_LIST](state, payload) {
      state.ScList = payload.info
    }
  },
  actions: {
    /**
     * 获取统计数据
     *
     * @param dispatch
     * @param commit
     * @param $param
     * @return {Promise<AxiosResponse | never>}
     */
    getStatisticsData({dispatch, commit}, $param) {
      let timestamp = (new Date()).valueOf();
      timestamp = parseInt(timestamp / 1000); // to second
      let days = typeof($param.day) !== "undefined" ? $param.day : '14';
      let startTimestamp = timestamp - (86400 * days) - 86400; // 14 days or 30 days ago

      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;
      let url = apiUrl + '/summary/daily/' + startTimestamp + '/' + timestamp;

      // 如果有hash，说明是合约信息
      if (typeof($param.contractHash) !== "undefined") {
        url = apiUrl + '/summary/contract/' + $param.contractHash + '/daily/' + startTimestamp + '/' + timestamp;
      }

      return axios.get(url).then(response => {
        let list = response.data.Result.SummaryList;
        let chartData = list;

        if (typeof($param.contractHash) !== "undefined") {
          chartData = setContractData(list)
        } else {
          chartData = setAllData(list)
        }

        commit({
          type: types.SET_STATISTICS_DATA,
          info: chartData
        })
      }).catch(error => {
        console.log(error)
      })
    },

    /**
     * 获取合约列表
     *
     * @param dispatch
     * @param commit
     * @param $param
     * @return {Promise<AxiosResponse | never>}
     */
    getContractList({dispatch, commit}, $param) {
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;
      let url = apiUrl + '/contract/100/1';

      return axios.get(url).then(response => {
        commit({
          type: types.SET_SC_LIST,
          info: response.data.Result.ContractList
        })
      }).catch(error => {
        console.log(error)
      })
    }
  }
}
