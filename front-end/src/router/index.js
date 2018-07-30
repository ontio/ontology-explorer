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
import ClaimVerifyPage from '@/components/claim/ClaimVerifyPage'

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
    path: '/address/:address',
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
  {
    path: '/claimverifyresult',
    name: 'ClaimVerify',
    component: ClaimVerifyPage
  },
  {
    path: '/:net',
    name: 'HomeTest',
    component: Home
  },
  {
    path: '/BlockListDetail/:pageSize/:pageNumber/:net',
    name: 'blockListDetailTest',
    component: BlockListPage
  },
  {
    path: '/block/:param/:net',
    name: 'blockDetailTest',
    component: BlockDetailPage
  },
  {
    path: '/ontidlist/:pageSize/:pageNumber/:net',
    name: 'OntIdListDetailTest',
    component: OntIdListPage
  },
  {
    path: '/ontid/:ontid/:net',
    name: 'OntIdDetailTest',
    component: OntIdDetailPage
  },
  {
    path: '/address/:address/:pageSize/:pageNumber/:net',
    name: 'AddressDetailTest',
    component: AddressDetailPage
  },
  {
    path: '/address/:address/:net',
    name: 'AddressDetailTest',
    component: AddressDetailPage
  },
  {
    path: '/transactionlist/:pageSize/:pageNumber/:net',
    name: 'TransactionListDetailTest',
    component: TransactionPage
  },
  {
    path: '/transaction/:txnHash/:net',
    name: 'TransactionDetailTest',
    component: TransactionDetailPage
  },
  {
    path: '/claimverify/:cardId/:ownerOntId/:net',
    name: 'ClaimDetaiTest',
    component: ClaimDetailPage
  },
  {
    path: '/claimverifyresult/:net',
    name: 'ClaimVerifyTest',
    component: ClaimVerifyPage
  },
]

const router = new Router({
  mode: 'history',
  routes: routes
})

export default router
