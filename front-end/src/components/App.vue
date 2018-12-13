<template>
  <div id="app" class="container-fluid container-bg-color">
    <nav-bar v-if="!inHomePage"></nav-bar>

    <router-view/>

    <the-footer></the-footer>
  </div>
</template>

<script>
  import 'bootstrap/dist/css/bootstrap.min.css'
  import 'bootstrap/dist/js/bootstrap.min.js'
  import 'chart.js'
  import TheFooter from './common/TheFooter'

  export default {
    name: 'App',
    data() {
      return {
        inHomePage: true
      }
    },
    created() {
      this.isDisplay();
    },
    watch: {
      '$route': 'isDisplay'
    },
    methods: {
      isDisplay() {
        this.inHomePage = (this.$route.path === '/' || this.$route.path === '/testnet');
      }
    },
    components: {TheFooter}
  }
</script>

<style>
  .container-bg-color {
    background-color: #f4f4f4;
    padding: 0 !important;
  }
</style>
