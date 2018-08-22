<template>
  <div class="row">
    <div class="col">
      <p class="text-center list-title-css">{{name}}</p>
    </div>
  </div>
</template>

<script>
	export default {
		name: "ListTitle",
    props: ['name']
	}
</script>

<style scoped>
  .list-title-css {
    color: #595757;
    font-size: 40px;
    font-weight: 100;
    font-family: "SourceSansPro-ExtraLight", "Helvetica Neue", "Arial", sans-serif;
    margin-bottom: 32px;
  }
</style>
