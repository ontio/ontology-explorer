import Vue from 'vue'
import Router from 'vue-router'

import Home from '@/components/home/Index'
import BlockListPage from '@/components/blocks/List'
import BlockDetailPage from '@/components/blocks/Detail'
import OntIdListPage from '@/components/ontId/List'
import OntIdDetailPage from '@/components/ontId/Detail'
import AddressDetailPage from '@/components/addresses/Detail'
import AddressList from '@/components/addresses/List'
import TransactionList from '@/components/transactions/List'
import TransactionDetail from '@/components/transactions/Detail'
import ClaimDetailPage from '@/components/claim/Detail'
import ClaimVerifyPage from '@/components/claim/Verify'
import NodeStakeAuthorization from '@/components/nodes/List'
import NodeTeamDetail from '@/components/nodes/Detail'
import ContractList from '@/components/contracts/List'
import ContractDetail from '@/components/contracts/Detail'
import TokenList from '@/components/tokens/List'
import TokenDetail from '@/components/tokens/Detail'
import StatisticsTable from '@/components/statistics/Table'
import ContractForm from '@/components/form/ContractForm'

Vue.use(Router);

let routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/blocks/:pageSize/:pageNumber',
    name: 'blockListDetail',
    component: BlockListPage
  },
  {
    path: '/block/:param',
    name: 'blockDetail',
    component: BlockDetailPage
  },
  {
    path: '/blocks/:pageSize/:pageNumber/:net',
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
    path: '/addresses/:token/:pageSize/:pageNumber',
    name: 'addressList',
    component: AddressList
  },
  {
    path: '/addresses/:token/:pageSize/:pageNumber/:net',
    name: 'addressListTest',
    component: AddressList
  },
  {
    path: '/transactions/:pageSize/:pageNumber',
    name: 'TransactionList',
    component: TransactionList
  },
  {
    path: '/transactions/:pageSize/:pageNumber/:net',
    name: 'TransactionListTest',
    component: TransactionList
  },
  {
    path: '/transaction/:txnHash',
    name: 'TransactionDetail',
    component: TransactionDetail
  },
  {
    path: '/transaction/:txnHash/:net',
    name: 'TransactionDetailTest',
    component: TransactionDetail
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
    path: '/nodes/detail/:pk',
    name: 'NodeTeamDetail',
    component: NodeTeamDetail
  },
  {
    path: '/nodes/list/:net',
    name: 'NodeStakeListTestNet',
    component: NodeStakeAuthorization
  },
  {
    path: '/contract/list/:pageSize/:pageNumber',
    name: 'ContractList',
    component: ContractList
  },
  {
    path: '/contract/list/:pageSize/:pageNumber/:net',
    name: 'ContractListTest',
    component: ContractList
  },
  {
    path: '/contract/:contractHash/:pageSize/:pageNumber',
    name: 'ContractDetail',
    component: ContractDetail
  },
  {
    path: '/contract/:contractHash/:pageSize/:pageNumber/:net',
    name: 'ContractDetailTest',
    component: ContractDetail
  },
/*   {
    path: '/contract/RegistrationForm',
    name: 'ContractRegistrationForm',
    component: ContractRegistrationForm
  },
  {
    path: '/contract/RegistrationForm/:net',
    name: 'ContractRegistrationFormTest',
    component: ContractRegistrationForm
  }, */
  {
    path: '/token/list/:type/:pageSize/:pageNumber',
    name: 'TokenList',
    component: TokenList
  },
  {
    path: '/token/list/:type/:pageSize/:pageNumber/:net',
    name: 'TokenListTest',
    component: TokenList
  },
  {
    path: '/token/detail/:type/:contractHash/:pageSize/:pageNumber',
    name: 'TokenDetail',
    component: TokenDetail
  },
  {
    path: '/token/detail/:type/:contractHash/:pageSize/:pageNumber/:net',
    name: 'TokenDetailTest',
    component: TokenDetail
  },
  {
    path: '/statistics/:day',
    name: 'Statistics',
    component: StatisticsTable
  },
  {
    path: '/statistics/:day/:net',
    name: 'StatisticsTest',
    component: StatisticsTable
  },
  {
    path: '/statistics/contract/:contractHash/:day',
    name: 'StatisticsContract',
    component: StatisticsTable
  },
  {
    path: '/statistics/contract/:contractHash/:day/:net',
    name: 'StatisticsContractTest',
    component: StatisticsTable
  },
  {
    path: '/form/contractform',
    name: 'ContractForm',
    component: ContractForm
  },
  {
    path: '/addresses/:pageSize/:pageNumber/:net',
    name: 'ContractFormTest',
    component: ContractForm
  },
];

const router = new Router({
  mode: 'history',
  routes: routes
});

export default router
