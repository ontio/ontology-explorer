<template>
  <div id="app">
    <top-nav></top-nav>
    <!-- <div class="container-div"></div> -->

    <router-view/>
  </div>
</template>

<script>
  import TopNav from './common/TopNav'

  export default {
    name: 'App',
    components: {
      TopNav
    }
  }
</script>

<style>
  @import url("https://fonts.googleapis.com/css?family=Montserrat:300,400,500,700,900");

/*   body {
    font-family: "PingFang SC", Verdana, "Microsoft YaHei", "微软雅黑", sans-serif;
    background-color: #fcfcfc;
  }
  p{
    font-family: "PingFang SC", Verdana, "Microsoft YaHei", "微软雅黑", sans-serif;
    font-size:13px;
  } */

</style>
