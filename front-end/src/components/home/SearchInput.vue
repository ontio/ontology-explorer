<template>
  <div>
    <div class="container">
      <div class="row">
        <div class="col-lg-12 form-group search-area text-center">
          <h2 class="font-ExtraLight font-size40" style="color:white;font-weight:100;">Blockchain Explorer & Trust Verifier</h2>
          <div class="input-group ">
            <input type="text" class="form-control search-input-txt search-input"  v-model="searchContent" :placeholder="$t('searchInput.placeholder')">
            <div class="input-group-addon input-submit-search search-input-txt search-btn text-center font-blod"  @click="submitSearch">
              <!-- <i class="fa fa-search" aria-hidden="true" ></i> --> <span class="font-size18"style="margin:auto">{{$t('searchInput.search')}}</span>
            </div>
          </div>
          <div class="TSE-text font-size14">Based on Ontology TSE . Go to <a class="font-blod TSE-text-link" >Verify a claim</a>. </div>
        </div>
      </div>
    </div>
<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h4 class="modal-title" id="myModalLabel">Verify claim</h4>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
      </div>
      <div class="modal-body">
        <div>
          <!-- <p class="font-Regular" style="width:100%">Please Input Claim URL</p> -->
          <p><input class="font-Regular font-size14" type="text"style="width:100%;padding:12px"  placeholder="Input Your Claim URI" /></p>
        </div>
        <div>
          <p class="font-Regular font-size24 " style="text-align: center;width:100%">Or</p>
        </div>
        <div>
          <!-- <p class="font-Regular">Upload Claim File</p> -->
          <p>
            <button type="button" class="upload_btn btn-primary font-Regular" data-dismiss="modal" style="margin:auto" ><i class="fa fa-lg fa-folder-open-o"></i> Choose Your Claim File</button>
          </p>
        </div>
      </div>
      <div class="modal-footer text-center" style="display:block">
        <div><button type="button" class="veriy_btn btn-primary" disabled >Verify Your Claim on Blockchain</button></div>
        <div><p class="font-Regular">Coming Soon</p></div>
      </div>
    </div>
  </div>
</div>
  </div>
</template>

<script>

  export default {
    components: {},
    name: "search-input",
    data() {
      return {
        searchContent: ''
      }
    },
    methods: {
      submitSearch() {
      	if(this.searchContent !== '') {
          // debug
          /* console.log(this.searchContent) */
          // do something
          switch(this.searchContent.length){
            /* txhash */
            case 64:
              this.$router.push({ name:'TransactionDetail', params:{txnHash:this.searchContent}})
            break;
            /* address */
            case 34:
              this.$router.push({ name:'AddressDetail', params:{address:this.searchContent,pageSize:100,pageNumber:1}})
            break;
            /* ontid */
            case 42:
              this.$router.push({ name:'OntIdDetail', params:{ontid:this.searchContent,pageSize:100,pageNumber:1}})
            break;
            /* block height */
            case 1:
              this.$router.push({ name:'blockDetail', params:{param:this.searchContent}})
            break;
            case 2:
              this.$router.push({ name:'blockDetail', params:{param:this.searchContent}})
            break;
            case 3:
              this.$router.push({ name:'blockDetail', params:{param:this.searchContent}})
            break;
            case 4:
              this.$router.push({ name:'blockDetail', params:{param:this.searchContent}})
            break;
            case 5:
              this.$router.push({ name:'blockDetail', params:{param:this.searchContent}})
            break;
            case 6:
              this.$router.push({ name:'blockDetail', params:{param:this.searchContent}})
            break;
            case 7:
              this.$router.push({ name:'blockDetail', params:{param:this.searchContent}})
            break;
            case 8:
              this.$router.push({ name:'blockDetail', params:{param:this.searchContent}})
            break;
            case 9:
              this.$router.push({ name:'blockDetail', params:{param:this.searchContent}})
            break;
            case 10:
              this.$router.push({ name:'blockDetail', params:{param:this.searchContent}})
            break;
          }
        }
      },
    }
  }
</script>

<style scoped>
  .search-area {
    margin-top: 52px;
  }
  .input-submit-search:hover {
    cursor: pointer;
  }
  .fa-search {
    margin-left: 30%;
    margin-right: 5px;
  }
  .search-input-txt{
    font-size: 14px;
        border: 1px solid rgba(255, 255, 255, 1);
  }
  .search-btn{
    max-width: 150px;
    width: 150px;
    color:#32a4be;
    font-weight:700;
    font-size:16px;
  }
  .form-control:focus {
      color: none;
      background-color: none;
      border-color: #ffffff;
      outline: none;
      -webkit-box-shadow: none;
      box-shadow:none;
  }
  .TSE-text{
      color:white;
      font-weight:100;
      margin-top: 10px;
      max-width: 670px;
      text-align: left;
      margin-left: auto;
      margin-right: auto;
  }
  .TSE-text-link{
    cursor:pointer;
    font-weight: 600;
    text-decoration:underline !important;
  }
  .veriy_btn{
    width: 100%;
        display: inline-block;
    font-weight: 400;
    text-align: center;
    white-space: nowrap;
    vertical-align: middle;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
    border: 1px solid transparent;
    padding: 0.5rem 1.1rem;
    font-size: 1.063rem;
    line-height: 1.5;
    border-radius: 0;
    background: #AFACAC;
   /*  background-color: #AFACAC; */
    -webkit-transition: background-color 0.15s ease-in-out, border-color 0.15s ease-in-out, -webkit-box-shadow 0.15s ease-in-out;
    transition: background-color 0.15s ease-in-out, border-color 0.15s ease-in-out, -webkit-box-shadow 0.15s ease-in-out;
    transition: background-color 0.15s ease-in-out, border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
    transition: background-color 0.15s ease-in-out, border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out, -webkit-box-shadow 0.15s ease-in-out;
  }

  .upload_btn{
    height:49px;
    width: 100%;
        display: inline-block;
    font-weight: 400;
    text-align: center;
    white-space: nowrap;
    vertical-align: middle;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
    border: 1px solid transparent;
    padding: 0.5rem 1.1rem;
    font-size: 1.063rem;
    line-height: 1.5;
    border-radius: 0;
    border-color: #32A4BE;
    background: #ffffff;
    color:#32A4BE;
   /*  background-color: #AFACAC; */
    -webkit-transition: background-color 0.15s ease-in-out, border-color 0.15s ease-in-out, -webkit-box-shadow 0.15s ease-in-out;
    transition: background-color 0.15s ease-in-out, border-color 0.15s ease-in-out, -webkit-box-shadow 0.15s ease-in-out;
    transition: background-color 0.15s ease-in-out, border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
    transition: background-color 0.15s ease-in-out, border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out, -webkit-box-shadow 0.15s ease-in-out;
  }
  .upload_btn:hover{
    color:white;
    background: #32A4BE;
    border-color: #32A4BE !important;
    box-shadow: 0 0 0 0rem rgba(69,130,236,0) !important;
  }
  .upload_btn:focus{
    color:white;
    background: #32A4BE;
    border-color: #32A4BE !important;
    box-shadow: 0 0 0 0rem rgba(69,130,236,0) !important;
  }
  .upload_btn:active{
    color:white;
    background-color: #32A4BE !important;
    border-color: #32A4BE !important;
    box-shadow: 0 0 0 0rem rgba(69,130,236,0) !important;
  }
</style>
