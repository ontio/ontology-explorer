'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  BASE_URL: '"https://localhost/"',
  BC_URL: '"https://localhost:10443/api/v1/"',

  EXPLORE_URL: '"https://explorer.ont.io/"',
  TEST_EXPLORE_URL: '"https://polarisexplorer.ont.io/"',

  API_URL: '"https://explorer.ont.io/api/v1/explorer"',
  TEST_API_URL: '"https://polarisexplorer.ont.io/api/v1/explorer"',

  DAPP_NODE_URL: '"https://dappnode1.ont.io:10334"',
  TEST_DAPP_NODE_URL: '"http://139.219.128.220:20334"',

  NET:true
})
