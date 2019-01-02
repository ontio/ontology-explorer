'use strict';

const merge = require('webpack-merge');
const prodEnv = require('./prod.env');

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',

  // 单独提供给addresses页面的接口，golang实现的
  EXPLORE_URL: '"https://explorer.ont.io/"',
  TEST_EXPLORE_URL: '"https://polarisexplorer.ont.io/"',

  // 基础API接口
  API_URL: '"https://explorer.ont.io/api/v1/explorer"',
  TEST_API_URL: '"https://polarisexplorer.ont.io/api/v1/explorer"',

  // 提供节点统计信息计算查询的接口：
  DAPP_NODE_URL: '"https://dappnode1.ont.io:10334"',
  TEST_DAPP_NODE_URL: '"http://139.219.128.220:20334"',

  NET:true
});
