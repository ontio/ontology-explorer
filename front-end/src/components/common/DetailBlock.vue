<template>
  <div class="row">
    <div class="col">
      <div class="detail-col font-Regular">
        <div v-for="(item,index) in params">
          <p v-if="index > 0"></p>
          <!--<div class="row">-->
            <!--<div class="col-3 text-right">-->
              <!--<span class="f-color">{{item.name}}</span>-->
            <!--</div>-->
            <!--<div class="col-9">-->
              <!--<span class="normal_color word-break font-size14" :class="(item.rows === 2) ? 'd-block font-size14' :''">{{item.val}}</span>-->
            <!--</div>-->
          <!--</div>-->
          <span class="f-color">{{item.name}}</span>
          <span class="normal_color word-break" :class="(item.rows === 2) ? 'd-block font-size14' :''">{{item.val}}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
	export default {
    /**
     * 该全局子组件提供【Details】页面内详情的【单行单列】白色数据块。
     *
     * 一般承载3类数据：
     * 1. 单条详情标题和内容，在块内单行展示
     * 2. 单条详情标题和内容，在块内双行展示
     * 3. 最后的多条详情标题和内容，在块内双行展示
     */
		name: "DetailBlock",
    props: ['params', 'name', 'val', 'rows']
	}
</script>

<style scoped>
</style>
