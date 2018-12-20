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
      let url = apiUrl + '/summary/daily/' + startTimestamp + '/' + timestamp;

      return axios.get(url).then(response => {
        let list = response.data.Result.SummaryList;

        // 表格数据格式构造
        let chartData = {
          labels:[],
          data: {
            block: {
              label: 'blockLbl',
              list: []
            },
            txn: {
              label: 'txnLbl',
              list: []
            },
            newAddress: {
              label: 'newAddressLbl',
              list: []
            },
            activeAddress: {
              label: 'activeAddressLbl',
              list: []
            },
            newOntId: {
              label: 'newOntIdLbl',
              list: []
            },
            activeOntId: {
              label: 'activeOntIdLbl',
              list: []
            },
            sumAddress: {
              label: 'sumAddressLbl',
              list: []
            },
            sumOntId: {
              label: 'sumOntIdLbl',
              list: []
            },
            ont: {
              label: 'ontLbl',
              list: []
            },
            ong: {
              label: 'ongLbl',
              list: []
            }
          }
        };

        // 表格数据填充
        for(let listKey in list) {
          chartData.labels.push(list[listKey].Time);
          chartData.data.block.list.push(list[listKey].BlockCount);
          chartData.data.txn.list.push(list[listKey].TxnCount);
          chartData.data.newAddress.list.push(list[listKey].ActiveAddress);
          chartData.data.activeAddress.list.push(list[listKey].NewAddress);
          chartData.data.newOntId.list.push(list[listKey].OntIdNewCount);
          chartData.data.activeOntId.list.push(list[listKey].OntIdActiveCount);
          chartData.data.sumAddress.list.push(list[listKey].AddressSum);
          chartData.data.sumOntId.list.push(list[listKey].OntIdSum);
          chartData.data.ont.list.push(list[listKey].OntCount)
          chartData.data.ong.list.push(list[listKey].OngCount)
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
