<template>
  <div class="row">
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
  </div>
</template>

<script>
  import Clipboard from 'clipboard';

	export default {
    name: "DetailTitle",
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

        this.showCopied = true
      }
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
</style>
