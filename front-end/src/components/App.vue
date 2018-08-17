<template>
  <div id="app" class="container-bg-color">
    <top-nav></top-nav>
    <router-view/>
    <bottom></bottom>
  </div>
</template>

<script>
  import 'bootstrap/dist/css/bootstrap.min.css'
  import 'bootstrap/dist/js/bootstrap.min.js'
  import TopNav from './common/TopNav'
  import bottom from './common/bottom'

  export default {
    name: 'App',
    components: {
      TopNav,
      bottom
    }
  }
</script>

<style>
  @import url("https://fonts.googleapis.com/css?family=Montserrat:300,400,500,700,900");

  .container-bg-color {
    background-color: #f4f4f4;
  }
</style>
