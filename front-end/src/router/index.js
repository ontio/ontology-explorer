import Vue from 'vue'
import Router from 'vue-router'

import Home from '@/components/home/Index'
import BlockListPage from '@/components/blocks/List'
import BlockDetailPage from '@/components/blocks/Detail'
import OntIdListPage from '@/components/ontId/List'
import OntIdDetailPage from '@/components/ontId/Detail'
import AddressDetailPage from '@/components/addresses/Detail'
import AddressList from '@/components/addresses/List'
import TransactionPage from '@/components/transactions/List'
import TransactionDetailPage from '@/components/transactions/Detail'
import ClaimDetailPage from '@/components/claim/Detail'
import ClaimVerifyPage from '@/components/claim/Verify'
import NodeStakeAuthorization from '@/components/nodes/StakeAuthorization'

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
    path: '/addresses/:pageSize/:pageNumber',
    name: 'addressList',
    component: AddressList
  },
  {
    path: '/addresses/:pageSize/:pageNumber/:net',
    name: 'addressListTest',
    component: AddressList
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
  {
    path: '/nodes/list',
    name: 'NodeStakeList',
    component: NodeStakeAuthorization
  },
  {
    path: '/nodes/list/:net',
    name: 'NodeStakeListTestNet',
    component: NodeStakeAuthorization
  }
]

const router = new Router({
  mode: 'history',
  routes: routes
})

export default router
