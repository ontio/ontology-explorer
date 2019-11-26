<template>
<!--   <div class="row">
    <div class="col detail-tit-name font-size24 font-blod important_color">
      <span>{{name}}</span>
      <span id="detailVal" :class="fontSizeVal" class="txt-overflow">{{val}}</span>
      <span class="pointer font-size14">
        <i @click="copyDetailVal"
           data-clipboard-target="#detailVal"
           class="copy-success l-25px fa fa-clone"
           aria-hidden="true"></i>
      </span>
      <span class="font-size14 font-ExtraLight" v-show="showCopied">Copied!</span>
    </div>
  </div> -->

  <div class="row font-size14" >

    <div class="col-2"><span class="normal_color">{{name}}</span></div>
    <div class="col-10">
      <span class="f-color word-break d-block height-100 font-size14">
        <span id="detailVal" class="height-100">{{val}}</span>

        <span class="pointer font-size14">
          <i @click="copyDetailVal"
            data-clipboard-target="#detailVal"
            class="copy-success l-25px fa fa-clone"
            aria-hidden="true"></i>
        </span>
        <span class="font-size14 font-ExtraLight" v-show="showCopied">Copied!</span>
      </span>
    </div>
  </div>
</template>

<script>
  import Clipboard from 'clipboard';

	export default {
    name: "DetailTitle1",
    props: ['name', 'val'],
    computed: {
      fontSizeVal: function () {
        return (typeof(this.val) !== 'undefined' && this.val.length > 16) ? 'font-size14' : ''
      }
    },
    data() {
      return {
        showCopied: false
      }
    },
    methods: {
      copyDetailVal() {
        let clipboard = new Clipboard('.copy-success');

        clipboard.on('success', function(e) {
          e.clearSelection();
        });

        //this.showCopied = true
        this.copied()
      },
      copied(){
        this.$toast.top(this.$t('error.copied'));
      },
    }
  }
</script>

<style scoped>
  .l-25px {
    margin-left: 25px;
  }

  @media screen and (max-width: 768px) {
    .detail-tit-name > span,
    .return-home-css {
      font-size: 16px;
    }
  }

  .sc-detail-divider-line {
    background: #e5e4e4;
    height: 1px;
    margin: 10px;
    width: 100%;
  }
</style>
