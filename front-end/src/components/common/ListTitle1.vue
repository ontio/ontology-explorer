<template>
  <div class="row">
    <div class="d-none d-sm-block col" style="padding:0">
<!--       <p class="float-left return-home-css" @click="goBack"><< {{ $t('all.return') }}</p> -->
      <p class="text-left list-title-css">{{name}}</p>
    </div>
<!--     <div class="col">
      <p class="text-center list-title-css">{{name}}</p>
    </div> -->
  </div>
</template>

<script>
  import axios from 'axios'
	export default {
		name: "ListTitle1",
    data() {
      return {
        searchContent: ''
      }
    },
    methods: {
      goBack() {
        window.history.length > 1
          ? this.$router.go(-1)
          : this.$router.push('/')
      },
    },
    props: ['name']
	}
</script>

<style scoped>
  .list-title-css {
    color: #595757;
    font-size: 24px;
    font-family: "SourceSansPro-Regular", "Helvetica Neue", "Arial", sans-serif;
    margin-bottom: 13px;
    font-weight:400;
    line-height:31px;
  }

  .return-home-css {
    color: #595757;
    line-height: 36px;
    font-size: 18px;
    font-family: "SourceSansPro-Regular", "Helvetica Neue", "Arial", sans-serif;
    margin-right: 0;
    margin-bottom: 0;
    cursor: pointer;
  }

  @media screen and (max-width: 768px) {
    .list-title-css,
    .return-home-css {
      font-size: 16px;
    }
  }
  .search-content{
    height: 32px;
    display: flex;
    display: -webkit-flex;
    justify-content: flex-end;
  }

  .input-search {
    padding: 0.5rem 1.1rem;
    color: #495057;
    height: 100%;
  }

  .input-search::-webkit-input-placeholder { /* WebKit, Blink, Edge */
    color: #cacaca;
  }

  .input-search:-moz-placeholder { /* Mozilla Firefox 4 to 18 */
    color: #cacaca;
  }

  .input-search::-moz-placeholder { /* Mozilla Firefox 19+ */
    color: #cacaca;
  }

  .input-search:-ms-input-placeholder { /* Internet Explorer 10-11 */
    color: #cacaca;
  }

  .search-input-txt {
    font-size: 14px;
    border: 1px solid rgba(255, 255, 255, 1);
  }
  .input-submit-search:hover {
    cursor: pointer;
  }
  .title-search-btn {
    line-height: 32px;
    width: 128px;
    color: #fff;
    background-color:#32a4be;
    font-weight: 700;
    font-size: 14px;
    box-sizing: padding-box;
  }
  .title-search-input-txt{
    max-width: 381px;
    border:1px solid rgba(175,172,172,1);
    
  }

  .form-control:focus {
    color: none;
    background-color: none;
    border-color: #ffffff;
    outline: none;
    -webkit-box-shadow: none;
    box-shadow: none;
  }
</style>
