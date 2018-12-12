<template>
  <div class="row">
    <div class="col">
      <p class="float-left return-home-css" @click="goBack"><< {{ $t('all.return') }}</p>
    </div>
    <div class="col">
      <p class="text-center list-title-css">{{name}}</p>
    </div>
    <div class="col"></div>
  </div>
</template>

<script>
	export default {
		name: "ListTitle",
    methods: {
      goBack() {
        window.history.length > 1
          ? this.$router.go(-1)
          : this.$router.push('/')
      }
    },
    props: ['name']
	}
</script>

<style scoped>
  .list-title-css {
    color: #595757;
    font-size: 24px;
    font-family: "SourceSansPro-Regular", "Helvetica Neue", "Arial", sans-serif;
    margin-bottom: 32px;
  }

  .return-home-css {
    color: #595757;
    line-height: 36px;
    font-size: 18px;
    font-family: "SourceSansPro-Regular", "Helvetica Neue", "Arial", sans-serif;
    margin-right: 0;
    margin-bottom: 0;
    cursor: pointer;
  }
</style>
