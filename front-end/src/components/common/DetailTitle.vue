<template>
  <div class="row">
    <div class="col detail-tit-name font-size24 font-blod important_color">
      <span>{{name}}</span>
      <span :class="fontSizeVal">{{val}}</span>
    </div>
  </div>
</template>

<script>
	export default {
    name: "DetailTitle",
    props: ['name', 'val'],
    computed: {
      fontSizeVal: function () {
        return (typeof(this.val) !== 'undefined' && this.val.length > 16) ? 'font-size14' : ''
      }
    }
  }
</script>

<style scoped>

</style>
