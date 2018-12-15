import axios from 'axios'
import * as types from "../mutation-type"

export default {
  state: {
    StatisticsData: {info: ''}
  },
  mutations: {
    [types.SET_STATISTICS_DATA](state, payload) {
      state.StatisticsData = payload.info
    }
  },
  actions: {
    getStatisticsData({dispatch, commit}, $param) {
      let timestamp = (new Date()).valueOf();
      timestamp = parseInt(timestamp / 1000); // to second
      let startTimestamp = timestamp - 1209600; // 14days ago

      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;
      let url = apiUrl + '/daily/info/' + startTimestamp + '/' + timestamp;

      return axios.get(url).then(response => {
        let list = response.data.Result;

        // 表格数据格式构造
        let chartData = {
          labels:[],
          data: {
            address: {
              label: 'addressLbl',
              list: []
            },
            block: {
              label: 'blockLbl',
              list: []
            },
            ontId: {
              label: 'ontIdLbl',
              list: []
            },
            txn: {
              label: 'txnLbl',
              list: []
            },
          }
        };

        // 表格数据填充
        for(let listKey in list) {
          chartData.labels.push(list[listKey].Time);
          chartData.data.address.list.push(list[listKey].AddressCount);
          chartData.data.block.list.push(list[listKey].BlockCount);
          chartData.data.ontId.list.push(list[listKey].OntIdCount);
          chartData.data.txn.list.push(list[listKey].TxnCount)
        }

        commit({
          type: types.SET_STATISTICS_DATA,
          info: chartData
        })
      }).catch(error => {
        console.log(error)
      })
    }
  }
}
