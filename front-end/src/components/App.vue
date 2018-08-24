<template>
  <div id="app" class="container-fluid container-bg-color">
    <top-nav></top-nav>
    <router-view/>
    <the-footer></the-footer>
  </div>
</template>

<script>
  import 'bootstrap/dist/css/bootstrap.min.css'
  import 'bootstrap/dist/js/bootstrap.min.js'
  import TopNav from './common/TopNav'
  import TheFooter from './common/TheFooter'

  export default {
    name: 'App',
    components: {
      TopNav,
      TheFooter
    }
  }
</script>

<style>
  @import url("https://fonts.googleapis.com/css?family=Montserrat:300,400,500,700,900");

  .container-bg-color {
    background-color: #f4f4f4;
    padding: 0 !important;
  }
</style>
