<template>
  <div>
    <div class="container">
      <div class="row">
        <div class="col form-group search-area text-center">
          <h2 class="font-ExtraLight font-size40" style="color:white;font-weight:100;">{{$t('searchInput.slogan')}}</h2>
          <div class="input-group">
            <input type="text" class="form-control input-search search-input-txt"
                   v-model="searchContent" @keyup.13="submitSearch" :placeholder="$t('searchInput.placeholder')">
            <div class="search-cut-wrapper"><div class="search-cut"></div></div>
            <div class="input-group-addon input-submit-search search-input-txt search-btn text-center font-weight-bold"
                 @click="submitSearch">
              {{$t('searchInput.search')}}
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h4 class="modal-title" id="myModalLabel">Verify claim</h4>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
              aria-hidden="true">&times;</span></button>
          </div>
          <div class="modal-body">
            <div>
              <p><input class="font-Regular font-size14" type="text" style="width:100%;padding:12px"
                        placeholder="Input Your Claim URI"/></p>
            </div>
            <div>
              <p class="font-Regular font-size24" style="text-align: center;width:100%">Or</p>
            </div>
            <div>
              <p>
                <button type="button" class="upload_btn btn-primary font-Regular" data-dismiss="modal"
                        style="margin:auto"><i class="fa fa-lg fa-folder-open-o"></i> Choose Your Claim File
                </button>
              </p>
            </div>
          </div>
          <div class="modal-footer text-center" style="display:block">
            <div>
              <button type="button" class="veriy_btn btn-primary" disabled>Verify Your Claim on Blockchain</button>
            </div>
            <div><p class="font-Regular">Coming Soon</p></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import axios from 'axios'
  export default {

    components: {},
    name: "search-input",
    data() {
      return {
        searchContent: ''
      }
    },
    methods: {
      notFound(){
       // this.$toast.center(this.$t('error.format'));
       this.$message({message: this.$t('error.format')});
      },
      searchHash($searchContent){
        let apiUrl = (this.$route.params.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;
        axios.get(apiUrl + '/transaction/' + $searchContent).then(res=>{
            if(res.data.Error != 0){
                this.notFound()
            }else{
                if (this.$route.params.net == undefined){
                    this.$router.push({name: 'TransactionDetail', params: {txnHash: this.searchContent}})
                }else{
                    this.$router.push({
                      name: 'TransactionDetailTest',
                      params: {txnHash: this.searchContent, net: 'testnet'}
                    })
                }
            }          
        })
      },
      searchAddress($searchContent){
        if (this.$route.params.net == undefined) {
          this.$router.push({
            name: 'AddressDetail',
            params: {address: this.searchContent, pageSize: 20, pageNumber: 1}
          })
        } else {
          this.$router.push({
            name: 'AddressDetailTest',
            params: {address: this.searchContent, pageSize: 20, pageNumber: 1, net: 'testnet'}
          })
        }

      },
      searchContract($searchContent){
        let apiUrl = (this.$route.params.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;
        let url = apiUrl + '/contract/' + $searchContent + '/10/1';
        axios.get(url).then(response => {
            if(response.data.Error != 0){
                this.notFound()
            }else{
              console.log(response)
                if(response.data.Result == null){
                  this.notFound()
                }else{
                  if (this.$route.params.net == undefined){
                    this.$router.push({
                      name: 'ContractDetail',
                      params: {contractHash: this.searchContent, pageSize: 10, pageNumber: 1}
                    })
                  }else{
                    this.$router.push({
                      name: 'ContractDetailTest',
                      params: {contractHash: this.searchContent, pageSize: 10, pageNumber: 1, net: 'testnet'}
                    })
                  }
                }
            }  
        })
      },
      searchONTID($searchContent){
        let apiUrl = (this.$route.params.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;
        axios.get(apiUrl + '/ontid/'+$searchContent+'/20/1').then(response => {
            if(response.data.Error != 0){
              
                this.notFound()
            }else{
              if (this.$route.params.net == undefined) {
                this.$router.push({
                  name: 'OntIdDetail',
                  params: {ontid: this.searchContent, pageSize: 20, pageNumber: 1}
                })
              } else {
                this.$router.push({
                  name: 'OntIdDetailTest',
                  params: {ontid: this.searchContent, pageSize: 20, pageNumber: 1, net: 'testnet'}
                })
              }        
            }      
        })
      },
      searchHeight($searchContent){
        let apiUrl = (this.$route.params.net === "testnet") ? process.env.TEST_API_URL : process.env.API_URL;
        axios.get(apiUrl + '/block/' + $searchContent).then(response => {
            if(response.data.Error != 0){
                this.notFound()
            }else{
              if (this.$route.params.net == undefined) {
                this.$router.push({name: 'blockDetail', params: {param: this.searchContent}})
              } else {
                this.$router.push({name: 'blockDetailTest', params: {param: this.searchContent, net: 'testnet'}})
              }              
            }          
        })
      },
      submitSearch() {
        if (this.searchContent !== '') {
          switch (this.searchContent.length) {
            /* txhash */
            case 64:
              this.searchHash(this.searchContent)
              break;
            /* address */
            case 34:
              this.searchAddress(this.searchContent)
              break;
            /* contract hash */
            case 40:
              this.searchContract(this.searchContent)
              break;
            /* ontid */
            case 42:
              this.searchONTID(this.searchContent)
              break;
            /* block height */
            case 1:
              this.searchHeight(this.searchContent)
              break;
            case 2:
              this.searchHeight(this.searchContent)
              break;
            case 3:
              this.searchHeight(this.searchContent)
              break;
            case 4:
              this.searchHeight(this.searchContent)
              break;
            case 5:
              this.searchHeight(this.searchContent)
              break;
            case 6:
              this.searchHeight(this.searchContent)
              break;
            case 7:
              this.searchHeight(this.searchContent)
              break;
            case 8:
              this.searchHeight(this.searchContent)
              break;
            case 9:
              this.searchHeight(this.searchContent)
              break;
            case 10:
              this.searchHeight(this.searchContent)
              break;
            default:
              this.notFound();
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

  .input-group {
    height: 48px;
    max-width: 620px;
    margin: 60px auto 0;
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

  .search-btn {
    padding: 12px 1.1rem;
    line-height: 20px;
    max-width: 150px;
    width: 150px;
    color: #32a4be;
    font-weight: 700;
    font-size: 18px;
  }

  .form-control:focus {
    color: none;
    background-color: none;
    border-color: #ffffff;
    outline: none;
    -webkit-box-shadow: none;
    box-shadow: none;
  }

  .TSE-text {
    color: white;
    font-weight: 100;
    margin-top: 10px;
    max-width: 670px;
    text-align: left;
    margin-left: auto;
    margin-right: auto;
  }

  .TSE-text-link {
    cursor: pointer;
    font-weight: 600;
    text-decoration: underline !important;
  }

  .veriy_btn {
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

  .upload_btn {
    height: 49px;
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
    color: #32A4BE;
    /*  background-color: #AFACAC; */
    -webkit-transition: background-color 0.15s ease-in-out, border-color 0.15s ease-in-out, -webkit-box-shadow 0.15s ease-in-out;
    transition: background-color 0.15s ease-in-out, border-color 0.15s ease-in-out, -webkit-box-shadow 0.15s ease-in-out;
    transition: background-color 0.15s ease-in-out, border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
    transition: background-color 0.15s ease-in-out, border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out, -webkit-box-shadow 0.15s ease-in-out;
  }

  .upload_btn:hover {
    color: white;
    background: #32A4BE;
    border-color: #32A4BE !important;
    box-shadow: 0 0 0 0rem rgba(69, 130, 236, 0) !important;
  }

  .upload_btn:focus {
    color: white;
    background: #32A4BE;
    border-color: #32A4BE !important;
    box-shadow: 0 0 0 0rem rgba(69, 130, 236, 0) !important;
  }

  .upload_btn:active {
    color: white;
    background-color: #32A4BE !important;
    border-color: #32A4BE !important;
    box-shadow: 0 0 0 0rem rgba(69, 130, 236, 0) !important;
  }
  .search-cut-wrapper{
    width: 1px;
    padding:9px 0;
    background: #fff;
  }
  .search-cut{
    border-left:1px solid rgba(50,164,190,1);
    height: 30px;
  }
</style>
