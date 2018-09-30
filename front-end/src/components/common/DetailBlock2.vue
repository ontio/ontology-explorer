<template>
  <div class="row font-Regular normal_color">
    <div class="col">
      <div class="detail-col detail-col-left">
        <span class="f-color">{{name1}}</span>
        <span class="normal_color word-break" :class="classObject1" @click="doAction(params1)">{{val1}}</span>
      </div>
    </div>
    <div class="col">
      <div class="detail-col detail-col-right">
        <span class="f-color">{{name2}}</span>
        <span class=" word-break" :class="classObject2" @click="doAction(params2)">{{val2}}</span>

        <a v-if="tip === 'true'" href="#" data-toggle="tooltip" :title="tipTit" class="common-tooltip-style">
          <i class="fa fa-info-circle" aria-hidden="true"></i>
        </a>
      </div>
    </div>
  </div>
</template>

<script>
  import $ from 'jquery'

  //提示框
  $(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();
  });

  export default {
    /**
     * 该全局子组件提供【Details】页面内详情的【单行双列】白色数据块。
     *
     * 一般承载3类数据：
     * 1. 详情标题和内容，在块内单行展示
     * 2. 详情标题和内容，在块内双行展示
     * 3. 详情标题和内容，在块内双行展示，其中第二行可以点击跳转
     *
     * rows:
     * 1    代表一行内字体默认；
     * 1.1  代表一行内字体颜色是划重点；
     * 1.2  代表一行内可跳转，小字体
     * 2    代表2行内容点击可跳转；
     */
    name: "DetailBlock2",
    props: ['name1', 'val1', 'rows1', 'params1', 'name2', 'val2', 'rows2', 'params2', 'tip', 'tipTit'],
    computed: {
      /**
       * 是否换行，字体大小，颜色，是否可以点击跳转。
       */
      classObject1: function () {
        return {
          'd-block': this.rows1 === '2',
          'font-size14': this.rows1 === '2' || this.rows1 === '1.2',
          's_color': this.rows1 === '1.1',
          'important_color': this.params1 !== undefined,
          pointer: this.params1 !== undefined
        }
      },
      classObject2: function () {
        return {
          'd-block': this.rows2 === '2',
          'font-size14': this.rows2 === '2',
          's_color': this.rows2 === '1.1',
          'important_color': this.params2 !== undefined,
          pointer: this.params2 !== undefined
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
        if (params === undefined) return false;

        if (params[0] === 'block') {
          this.toBlockDetailPage(params[1])
        } else if (params[0] === 'ontId') {
          this.toOntIdDetailPage(params[1])
        } else if (params[0] === 'address') {
          this.goToAddressDetail(params[1])
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
      },
      toOntIdDetailPage($OntId) {
        if (this.$route.params.net === 'testnet') {
          this.$router.push({name: 'OntIdDetailTest', params: {ontid: $OntId, net: "testnet"}})
        } else {
          this.$router.push({name: 'OntIdDetail', params: {ontid: $OntId}})
        }
      },
      goToAddressDetail(address) {
        if (this.$route.params.net === 'testnet') {
          this.$router.push({
            name: 'AddressDetailTest',
            params: {address: address, pageSize: 20, pageNumber: 1, net: 'testnet'}
          })
        } else {
          this.$router.push({
            name: 'AddressDetail',
            params: {address: address, pageSize: 20, pageNumber: 1}
          })
        }
      }
    }
  }
</script>

<style scoped>
  .common-tooltip-style {
    float: right;
    color: #AAB3B4;
    font-size: 16px;
    line-height: 32px;
  }
</style>
