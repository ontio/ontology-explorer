<template>
  <div class="row">
    <div class="d-none d-sm-block col">
<!--       <p class="float-left return-home-css" @click="goBack"><< {{ $t('all.return') }}</p> -->
      <p class="text-left list-title-css">{{name}}</p>
    </div>
<!--     <div class="col">
      <p class="text-center list-title-css">{{name}}</p>
    </div> -->
    <div class="d-none d-sm-block col">
        <div class="search-content">
            <input type="text" class="form-control input-search search-input-txt title-search-input-txt"
                   v-model="searchContent" @keyup.13="submitSearch" :placeholder="$t('searchInput.placeholder')">
            <div class=" input-submit-search  title-search-btn text-center font-weight-bold"
                 @click="submitSearch">
              {{$t('searchInput.search')}}
            </div>
        </div>
    </div>
  </div>
</template>

<script>
  import axios from 'axios'
	export default {
		name: "ListTitle",
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
      notFound(){
        //this.$toast.top(this.$t('error.format'));
        this.$message(this.$t('error.format'));
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
