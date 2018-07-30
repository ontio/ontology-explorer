import axios from 'axios'
import * as types from "../mutation-type"

export default {
  state: {
    OntIdListDetail: {
    }
  },
  mutations: {
    [types.SET_ONT_ID_LIST_PAGE](state, payload) {
      state.OntIdListDetail = payload.info
    }
  },
  actions: {
    getOntIdListPage({dispatch, commit},$param) {
      let used_url 
      if($param.net=="testnet"){
        used_url = process.env.TEST_API_URL
      }else{
        used_url = process.env.API_URL
      }
      return axios.get(used_url + '/ontidlist/'+$param.pageSize+'/'+$param.pageNumber).then(response => {
        let msg = response.data
        let allPageNum = msg.Result.Total
        let finalPageNum = parseInt(allPageNum/10)+1
        let lastPageNum = 1
        if ($param.pageNumber>1){
          lastPageNum = $param.pageNumber-1
        }
        let nextPageNum = finalPageNum
        if ($param.pageNumber<finalPageNum){
          nextPageNum = $param.pageNumber-1+2
        }

        let info={
          info: msg.Result.OntIdList,
          allPage: allPageNum,
          firstPage: {
            pageSize: '10',
            pageNumber: 1
          },
          lastPage:{
            pageSize: '10',
            pageNumber: lastPageNum
          },
          nextPage:{
            pageSize: '10',
            pageNumber: nextPageNum
          },
          finalPage: {
            pageSize: '10',
            pageNumber: finalPageNum
          }
        }

        commit({
          type: types.SET_ONT_ID_LIST_PAGE,
          info: info
        })
      }).catch(error => {
        console.log(error)
      })
    },
  }
}
