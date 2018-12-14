<template>
  <div class="row">

    <!-- PC Styles -->
    <div class="d-none d-sm-block col">
      <!-- styleVal === new时为新样式 -->
      <div v-if="(typeof(styleVal) !== 'undefined')" class="detail-col font-Regular detail-col-fix">
        <div v-for="(item,index) in params">
          <div v-if="index > 0" class="b-detail-divider-line"></div>
          <div class="row font-size14">
            <div class="col-2"><span class="normal_color">{{item.name}}</span></div>
            <div class="col-10">
            <span class="f-color word-break" :class="(item.rows === 2) ? 'd-block height-100 font-size14' :''">
              <div v-if="typeof(item.val) === 'object'" class="height-100" v-html="calcVal(item.val)"></div>
              <span v-else>{{item.val}}</span>
            </span>
            </div>
          </div>
        </div>
      </div>

      <div v-else class="detail-col font-Regular">
        <div v-for="(item,index) in params">
          <p v-if="index > 0"></p>
          <span class="f-color">{{item.name}}</span>
          <span class="normal_color word-break" :class="(item.rows === 2) ? 'd-block height-100 font-size14' :''">
            <div v-if="typeof(item.val) === 'object'" class="height-100" v-html="calcVal(item.val)"></div>
            <span v-else>{{item.val}}</span>
          </span>
        </div>
      </div>
    </div>

    <!-- Mobile Styles -->
    <div class="d-block d-sm-none col">
      <div v-if="(typeof(styleVal) !== 'undefined')" class="detail-col font-Regular detail-col-fix">
        <div v-for="(item,index) in params" class="font-size14">
          <div v-if="index > 0" class="b-detail-divider-line"></div>
          <div class="row">
            <div class="col">
              <div class="normal_color font-blod font-size14">{{item.name}}</div>
            </div>
          </div>
          <div class="row">
            <div class="col f-color word-break" :class="(item.rows === 2) ? 'd-block height-100 font-size14' :''">
              <div v-if="typeof(item.val) === 'object'" class="height-100" v-html="calcVal(item.val)"></div>
              <span v-else>{{item.val}}</span>
            </div>
          </div>
        </div>
      </div>

      <div v-else class="font-Regular">
        <div v-for="(item,index) in params" class="detail-col font-size14">
          <p v-if="index > 0"></p>
          <div class="row">
            <div class="col f-color font-blod">
              {{ item.name }}
            </div>
          </div>
          <div class="row">
            <div class="col s-color word-break"
                 :class="(item.rows === 2) ? 'd-block height-100 font-size14' :''">
              <div v-if="typeof(item.val) === 'object'" class="height-100" v-html="calcVal(item.val)"></div>
              <span v-else>{{item.val}}</span>
            </div>
          </div>
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
    props: ['params', 'name', 'val', 'rows', 'styleVal'],
    methods: {
      calcVal(values) {
        let retHtml = '';
        for(let index in values) {
          retHtml += "<div>" + values[index] + "</div>"
        }

        return retHtml
      }
    }
  }
</script>

<style scoped>
  .detail-col-fix {
    padding: 32px 24px 34px !important;;
  }

  .height-100 {
    height: 100%;
  }

  .b-detail-divider-line {
    background: #e5e4e4;
    height: 1px;
    margin: 15px 0;
  }
</style>
