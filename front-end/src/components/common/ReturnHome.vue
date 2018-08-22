<template>
  <div class="row">
    <p class="float-left return-home-css" @click="$router.go(-1)"><< {{ $t('all.return') }}</p>
  </div>
</template>

<script>
	export default {
    name: "ReturnHome"
  }
</script>

<style scoped>
  .return-home-css {
    color: #595757;
    font-size: 18px;
    font-family: "SourceSansPro-Regular", "Helvetica Neue", "Arial", sans-serif;
    margin-right: 0;
    margin-bottom: 0;
    cursor: pointer;
  }
</style>
