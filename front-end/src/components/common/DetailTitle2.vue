<template>
  <div class="row">
    <div class="col detail-tit-name font-size24 font-blod">
      <span class="f-color">{{name1}}</span>
      <span class="important_color">{{val}}</span>
    </div>

    <!--Skip links-->
    <div class="col text-right">
      <a :href="url" target="_blank" class="font-size18 important_color lh-36 pointer2">{{name2}}</a>
    </div>
  </div>
</template>

<script>
	export default {
    name: "DetailTitle2",
    props: ['name1', 'val', 'name2', 'url'],
    computed: {
      fontSizeVal: function () {
        return (typeof(this.val) !== 'undefined' && this.val.length > 16) ? 'font-size14' : ''
      }
    }
  }
</script>

<style scoped>
  .lh-36 {
    line-height: 36px;
  }
</style>
