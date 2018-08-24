<template>
  <div class="row">
    <div class="col detail-tit-name font-size24 font-blod important_color">
      <span>{{name}}</span>
      <span :class="val.length > 16 ? 'font-size14' : ''">{{val}}</span>
    </div>
  </div>
</template>

<script>
	export default {
		name: "DetailTitle",
    props: ['name', 'val']
	}
</script>

<style scoped>

</style>
