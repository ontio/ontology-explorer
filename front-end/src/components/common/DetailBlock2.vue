<template>
  <div class="row font-Regular">
    <div class="col">
      <div class="detail-col detail-col-left">
        <span class="f-color">{{name1}}</span>
        <span class="normal_color word-break" :class="classObject" @click="doAction(params1)">{{val1}}</span>
      </div>
    </div>
    <div class="col">
      <div class="detail-col detail-col-right">
        <span class="f-color">{{name2}}</span>
        <span class="normal_color word-break" :class="classObject" @click="doAction(params2)">{{val2}}</span>
      </div>
    </div>
  </div>
</template>

<script>
	export default {
    /**
     * 该全局子组件提供【Details】页面内详情的【单行双列】白色数据块。
     *
     * 一般承载3类数据：
     * 1. 详情标题和内容，在块内单行展示
     * 2. 详情标题和内容，在块内双行展示
     * 3. 详情标题和内容，在块内双行展示，其中第二行可以点击跳转
     */
    name: "DetailBlock2",
    props: ['name1', 'val1', 'rows1', 'params1', 'name2', 'val2', 'rows2', 'params2'],
    computed: {
      /**
       * 是否换行，字体大小，颜色，是否可以点击跳转。
       */
      classObject: function () {
        return {
          'd-block': this.rows1 === '2',
          'font-size14': this.rows1 === '2',
          'important_color': this.params1 !== undefined,
          pointer: this.params1 !== undefined
        }
      }
    },
    methods: {
      /**
       * 通过传参判断执行什么函数和函数参数是什么。
       *
       * @param params
       * @return {boolean}
       */
      doAction(params) {
        if(params === undefined) return false;

        if (params[0] === 'block') {
          this.toBlockDetailPage(params[1])
        } else {

        }
      },
      toBlockDetailPage($blockHeight) {
        if (this.$route.params.net === 'testnet') {
          this.$router.push({name: 'blockDetailTest', params: {param: $blockHeight, net: 'testnet'}})
        } else {
          this.$router.push({name: 'blockDetail', params: {param: $blockHeight}})
        }
      },
      toTransDetailPage($TxnId) {
        if (this.$route.params.net === 'testnet') {
          this.$router.push({name: 'TransactionDetailTest', params: {txnHash: $TxnId, net: 'testnet'}})
        } else {
          this.$router.push({name: 'TransactionDetail', params: {txnHash: $TxnId}})
        }
      }
    }
  }
</script>

<style scoped>

</style>
