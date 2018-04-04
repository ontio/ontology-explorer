import Vue from 'vue'
import Router from 'vue-router'

import Home from '@/components/home/Index'
import BlockListPage from '@/components/BlockListDetail/BlockListPage'
import BlockDetailPage from '@/components/BlockListDetail/BlockDetailPage'
import OntIdListPage from '@/components/OntIdListDetail/OntIdListPage'
import OntIdDetailPage from '@/components/OntIdListDetail/OntIdDetailPage'
import AddressDetailPage from '@/components/AddressDetail/AddressDetailPage'
import TransactionPage from '@/components/TransactionListDetail/TransactionListPage'
import TransactionDetailPage from '@/components/TransactionListDetail/TransactionDetailPage'
import ClaimDetailPage from '@/components/claim/ClaimDetailPage'

Vue.use(Router)
let routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/BlockListDetail/:pageSize/:pageNumber',
    name: 'blockListDetail',
    component: BlockListPage
  },
  {
    path: '/block/:param',
    name: 'blockDetail',
    component: BlockDetailPage
  },
  {
    path: '/ontidlist/:pageSize/:pageNumber',
    name: 'OntIdListDetail',
    component: OntIdListPage
  },
  {
    path: '/ontid/:ontid',
    name: 'OntIdDetail',
    component: OntIdDetailPage
  },
  {
    path: '/address/:address/:pageSize/:pageNumber',
    name: 'AddressDetail',
    component: AddressDetailPage
  },
  {
    path: '/transactionlist/:pageSize/:pageNumber',
    name: 'TransactionListDetail',
    component: TransactionPage
  },
  {
    path: '/transaction/:txnHash',
    name: 'TransactionDetail',
    component: TransactionDetailPage
  },
  {
    path: '/claimverify/:cardId/:ownerOntId',
    name: 'ClaimDetai',
    component: ClaimDetailPage
  },
]

const router = new Router({
  mode: 'history',
  routes: routes
})

export default router
