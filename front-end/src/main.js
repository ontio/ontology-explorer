// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './components/App'
import router from './router'
import store from './store/index'
import Axios from 'axios'
import VueAxios from 'vue-axios'
import VueI18n from 'vue-i18n'
import LangStorage from './helpers/lang'
import $ from 'jquery'
import 'font-awesome/css/font-awesome.css'
import Helper from './helpers/helper'

Vue.use(Helper);

/**
 * 全局组件注册
 */
import NavBar from './components/common/NavBar'
import ListTitle from './components/common/ListTitle'
import TurnThePage from './components/common/TurnThePage'
import DetailTitle from './components/common/DetailTitle'
import DetailTitle2 from './components/common/DetailTitle2'
import DetailBlock from './components/common/DetailBlock'
import DetailBlock2 from './components/common/DetailBlock2'
import LineChart from './components/common/LineChart'

Vue.component('nav-bar', NavBar);
Vue.component('list-title', ListTitle);
Vue.component('turn-the-page', TurnThePage);
Vue.component('detail-title', DetailTitle);
Vue.component('detail-title-2', DetailTitle2);
Vue.component('detail-block', DetailBlock);
Vue.component('detail-block-2', DetailBlock2);
Vue.component('line-chart', LineChart);

/**
 * Fixed compatibility issues with low version IE. lyx.
 */
import promise from 'es6-promise'

promise.polyfill();

/**
 * Vee Validate
 * Front-end Input need the field：data-vv-as
 */
import zh_CN from 'vee-validate/dist/locale/zh_CN'
import VeeValidate, {Validator} from 'vee-validate'
// Validator.localize('zh_CN', zh_CN);
Vue.use(VeeValidate, {
  // locale: 'zh_CN'
});

Vue.use(VueAxios, Axios);

Vue.use(VueI18n);
const i18n = new VueI18n({
  locale: LangStorage.getLang('en'),  // 语言标识
  messages: {
    'zh': require('./common/lang/zh'),
    'en': require('./common/lang/en')
  }
});

Vue.config.productionTip = false;

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  i18n,
  components: {App, ListTitle},
  template: '<App/>'
});
