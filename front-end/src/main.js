// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './components/App'
import "element-ui/lib/theme-chalk/index.css"; // element-ui css
import router from './router'
import store from './store/index'
import Axios from 'axios'
import VueAxios from 'vue-axios'
import VeeValidate from 'vee-validate';
import i18n from "./common/lang"; // Internationalization
import $ from 'jquery'
import 'font-awesome/css/font-awesome.css'
import './components/Toast/toast.css';

Vue.use(VeeValidate);
import Toast from './components/Toast/index';
Vue.use(Toast);

import Helper from './helpers/helper'
Vue.use(Helper);

import {
  Pagination
} from "element-ui";
Vue.component(Pagination.name, Pagination);

import Highcharts from 'highcharts/highstock';
Vue.use(Highcharts);
import HighchartsVue from 'highcharts-vue';
Vue.use(HighchartsVue)

/**
 * 全局组件注册
 */
import NavBar from './components/common/NavBar'
import NavBar1 from './components/common/NavBar1'
import ListTitle from './components/common/ListTitle'
import ListTitle1 from './components/common/ListTitle1'
import OntPagination from './components/common/OntPagination'
import DetailTitle from './components/common/DetailTitle'
import DetailTitle1 from './components/common/DetailTitle1'
import DetailTitle2 from './components/common/DetailTitle2'
import DetailBlock from './components/common/DetailBlock'
import DetailBlock2 from './components/common/DetailBlock2'
import DetailBlock3 from './components/common/DetailBlock3'
import LineChart from './components/common/LineChart'
import HiChart from './components/common/HiChart'

Vue.component('nav-bar', NavBar);
Vue.component('nav-bar-1', NavBar1);
Vue.component('list-title', ListTitle);
Vue.component('list-title-1', ListTitle1);
Vue.component('ont-pagination', OntPagination);
Vue.component('detail-title', DetailTitle);
Vue.component('detail-title-1', DetailTitle1);
Vue.component('detail-title-2', DetailTitle2);
Vue.component('detail-block', DetailBlock);
Vue.component('detail-block-2', DetailBlock2);
Vue.component('detail-block-3', DetailBlock3);
Vue.component('line-chart', LineChart);
Vue.component('hi-chart', HiChart);

/**
 * Fixed compatibility issues with low version IE. lyx.
 */
import promise from 'es6-promise'
promise.polyfill();

Vue.use(VueAxios, Axios);

Vue.config.productionTip = false;

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  i18n,
  components: {App, ListTitle},
  template: '<App/>',
});
