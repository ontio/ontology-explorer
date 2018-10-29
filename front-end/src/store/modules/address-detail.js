import axios from 'axios'
import * as types from "../mutation-type"
import Decimal from 'decimal.js';

/**
 * 计算总共多少ONT和ONG，lyx。
 *
 * @param myAddress
 * @param trans
 * @return {*}
 */
function getTransAmount(myAddress, trans) {
  let txnLists = trans.TxnList

  for (let val in txnLists) {
    let txnList = txnLists[val].TransferList;
    let ont = 0;
    let ong = 0;
    let pumpkin = 0; // OEP-8资产：2018年万圣节南瓜活动

    // 计算各种资产的实际收入
    for (let tx in txnList) {
      // from地址和我一样，那就是支出
      if (txnList[tx].FromAddress === myAddress) {
        if (txnList[tx].AssetName === 'ont') {
          ont = Decimal.sub(ont, Number(txnList[tx].Amount))
        } else if (txnList[tx].AssetName === 'ong') {
          ong = Decimal.sub(ong, Number(txnList[tx].Amount)).toFixed(9)
        } else if (txnList[tx].AssetName.indexOf('pumpkin') > -1) { // OEP-8资产：2018年万圣节南瓜活动
          pumpkin = Decimal.sub(pumpkin, Number(txnList[tx].Amount)).toFixed()
        }
      } else { // 否则都是我的收入~
        if (txnList[tx].AssetName === 'ont') {
          ont = Decimal.add(ont, Number(txnList[tx].Amount))
        } else if (txnList[tx].AssetName === 'ong') {
          ong = Decimal.add(ong, Number(txnList[tx].Amount)).toFixed(9)
        } else if (txnList[tx].AssetName.indexOf('pumpkin') > -1) { // OEP-8资产：2018年万圣节南瓜活动
          pumpkin = Decimal.add(pumpkin, Number(txnList[tx].Amount)).toFixed()
        }
      }
    }

    txnLists[val].amount = {
      ont: ont,
      ong: ong,
      pumpkin: pumpkin
    }
  }

  trans.TxnList = txnLists;

  return trans
}

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
      let apiUrl = ($param.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;

      return axios.get(apiUrl + '/address/'+$param.address+'/'+$param.pageSize+'/'+$param.pageNumber).then(response => {
        let msg = response.data

        // let allPageNum = msg.Result.TxnTotal
        // let finalPageNum = parseInt(allPageNum/20)+1
        let lastPageNum = 1
        if ($param.pageNumber>1){
          lastPageNum = $param.pageNumber-1
        }
        let nextPageNum = $param.pageNumber-1+2
        if(msg.Result.TxnList.length !== 20) { // 不足20表示是最后一页
          nextPageNum = false
        }
        // let nextPageNum = finalPageNum
        // if ($param.pageNumber<finalPageNum){
        //   nextPageNum = $param.pageNumber-1+2
        // }

        let info={
          info: getTransAmount($param.address, msg.Result),
          // allPage: allPageNum,
          firstPage: {
            pageSize: '20',
            pageNumber: 1
          },
          lastPage:{
            pageSize: '20',
            pageNumber: lastPageNum
          },
          nextPage:{
            pageSize: '20',
            pageNumber: nextPageNum
          },
          // finalPage: {
          //   pageSize: '20',
          //   pageNumber: finalPageNum
          // }
        }
        commit({
          type: types.SET_ADDRESS_DETAIL_PAGE,
          info: info,
        })
      }).catch(error => {
        console.log(error)
      })
    }
  }
}
