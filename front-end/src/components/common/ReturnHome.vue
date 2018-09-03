<template>
  <div class="row">
    <div class="col">
      <p class="float-left return-home-css" @click="goBack"><< {{ $t('all.return') }}</p>
    </div>
  </div>
</template>

<script>
	export default {
    name: "ReturnHome",
    methods: {
      goBack() {
        window.history.length > 1
          ? this.$router.go(-1)
          : this.$router.push('/')
      }
    }
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
