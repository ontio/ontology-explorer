import Vue from 'vue'
import Vuex from 'vuex'

import BlockChain from './modules/block-chain'
import RunStatus from './modules/run-status'
import OntIdList from './modules/ont-id-list'
import OntIdListPage from './modules/ont-id-list-page'
import OntIdDetailPage from './modules/ont-id-detail-page'
import BlockList from './modules/block-list'
import BlockListPage from './modules/block-list-page'
import BlockDetailPage from './modules/block-detail-page'
import TransactionList from './modules/transaction-list'
import TransactionListPage from './modules/transaction-list-page'
import TransactionDetailPage from './modules/transaction-detail-page'
import AddressDetailPage from './modules/address-detail-page'
import AddressListPage from './modules/address-list-page'
import ClaimDetailPage from './modules/claim-verify-page'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    BlockChain,
    RunStatus,
    OntIdList,
    OntIdListPage,
    OntIdDetailPage,
    BlockList,
    BlockListPage,
    BlockDetailPage,
    TransactionList,
    TransactionListPage,
    TransactionDetailPage,
    AddressDetailPage,
    AddressListPage,
    ClaimDetailPage
  }
})
