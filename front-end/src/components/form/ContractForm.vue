<template>
  <div class="e-container margin-top-15 explorer-detail-tab">
    <list-title-1 :name="$t('form.contractForm')"></list-title-1>
    <div class="row justify-content-center">

      <div class=" form-submit-border">
      <div class="form-submit-border-responsive">
        <div class="form-top-line"></div>
        <div class="form-submit-input-wrapper">
          <div class="form-submit-input-content">
              <div class="form-submit-input-requiredtext">{{$t('form.requiredtext')}}</div>
          </div>
        </div>        
        <div class="form-submit-input-wrapper">
          <div class="form-submit-input-content">
              <div class="form-submit-input-title">{{$t('form.name')}}<span class="form-submit-input-requiredstar"> {{$t('form.requiredstar')}}</span></div>
              <input :class="errors.has('Name')?'form-submit-text-errer':'form-submit-text'" v-model="Name" name="Name" v-validate="'required'"/>
              <div class="form-submit-input-text-error" v-show="errors.has('Name')">Here is can't be empty</div>
          </div>
        </div>
        <div class="form-submit-input-wrapper">
          <div class="form-submit-input-content">
              <div class="form-submit-input-title">{{$t('form.logo')}}<span class="form-submit-input-requiredstar"> {{$t('form.requiredstar')}}</span></div>
              <div class="form-submit-file-name-wrapper row" v-if="logoFlag" >
                <div class="form-submit-file-name col"  v-model="LogoName" >{{LogoName}}</div>
                <div class="form-submit-file-delete col" style="cursor:pointer;" @click="logoDelete()"><img class="form-submit-file-delete-img" src="../../assets/shares/close@2x.png" /></div>
              </div>
              <div class="form-submit-file"  v-if="!logoFlag" @click="uploadDAppLogo" >{{$t('form.addFile')}}</div>
              <input id="dapp-submit-upload-Logo" type="file" style="display: none" @change="getUploadLogoFile"   name="Logo" v-validate="'required'">
              <div class="form-submit-input-text-error" v-show="errors.has('Logo')">Here is can't be empty</div>
          </div>
        </div>
        <div class="form-submit-input-wrapper">
          <div class="form-submit-input-content">
              <div class="form-submit-input-title">{{$t('form.description')}}<span class="form-submit-input-requiredstar"> {{$t('form.requiredstar')}}</span></div>
              <textarea class="form-submit-desc" v-model="Description" name="Description" v-validate="'required'" />
              <div class="form-submit-input-text-error" v-show="errors.has('Description')">Here is can't be empty</div>
          </div>
        </div>
        <div class="form-submit-input-wrapper">
          <div class="form-submit-input-content">
              <div class="form-submit-input-title">{{$t('form.webLink')}}<span class="form-submit-input-requiredstar"> {{$t('form.requiredstar')}}</span></div>
              <input class="form-submit-text" v-model="WebLink" name="WebLink" v-validate="'required'"/>
              <div class="form-submit-input-text-error" v-show="errors.has('WebLink')">Here is can't be empty</div>
          </div>
        </div>
        <div class="form-submit-input-wrapper">
          <div class="form-submit-input-content">
              <div class="form-submit-input-title">{{$t('form.gitHubURL')}}<span class="form-submit-input-requiredstar"> {{$t('form.requiredstar')}}</span></div>
              <input class="form-submit-text" v-model="GitHubURL" name="GitHubURL" v-validate="'required'"/>
              <div class="form-submit-input-text-error" v-show="errors.has('GitHubURL')">Here is can't be empty</div>
          </div>
        </div>
        <div class="form-submit-input-wrapper">
          <div class="form-submit-input-content">
              <div class="form-submit-input-title">{{$t('form.ONTID')}}<span class="form-submit-input-requiredstar"> {{$t('form.requiredstar')}}</span></div>
              <input class="form-submit-text" v-model="ONTID" name="ONTID" v-validate="'required'"/>
              <div class="form-submit-input-text-error" v-show="errors.has('ONTID')">Here is can't be empty</div>
          </div>
        </div>
        <div class="form-submit-input-wrapper">
          <div class="form-submit-input-content">
              <div class="form-submit-input-title">{{$t('form.contractHash')}}1<span class="form-submit-input-requiredstar"> {{$t('form.requiredstar')}}</span></div>
              <input class="form-submit-text" v-model="ContractHash1" name="ContractHash1" v-validate="'required'"/>
              <div class="form-submit-input-text-error" v-show="errors.has('ContractHash1')">Here is can't be empty</div>
          </div>
        </div>
        <div class="form-submit-input-wrapper">
          <div class="form-submit-input-content">
              <div class="form-submit-input-title">{{$t('form.contractABI')}}1<span class="form-submit-input-requiredstar"> {{$t('form.requiredstar')}}</span></div>
              <input class="form-submit-text" v-model="ContractABI1" name="ContractABI1" v-validate="'required'"/>
              <div class="form-submit-input-text-error" v-show="errors.has('ContractABI1')">Here is can't be empty</div>
          </div>
        </div>
        <div class="form-submit-input-wrapper">
          <div class="form-submit-input-content">
              <div class="form-submit-input-title">{{$t('form.contractByteCode')}}1<span class="form-submit-input-requiredstar"> {{$t('form.requiredstar')}}</span></div>
              <div class="form-submit-file-name-wrapper row" v-if="byteCodeFlag[0]">
                <div class="form-submit-file-name col"  >{{ContractByteCodeName[0]}}</div>
                <div class="form-submit-file-delete col" style="cursor:pointer;" @click="ByteCodeDelete(0)"><img class="form-submit-file-delete-img" src="../../assets/shares/close@2x.png" /></div>
              </div>
              <div class="form-submit-file"  v-if="!byteCodeFlag[0]" @click="uploadByteCode(0)" >{{$t('form.addFile')}}</div>
              <input id="dapp-submit-upload-ByteCode-0" type="file" style="display: none" @change="getUploadByteCodeFile(0)"   name="ContractByteCodeName1" v-validate="'required'">
              <div class="form-submit-input-text-error" v-show="errors.has('ContractByteCodeName1')">Here is can't be empty</div>
          </div>
        </div>
        <div v-for="(item,index) in ContractByteCodeList" v-if='item.val' class="form-submit-input-list">
          <div class="form-submit-input-wrapper">
            <div class="form-submit-input-content">
              <div class="contract-line"></div>
            </div>
          </div>
          <div class="form-submit-input-wrapper form-submit-input-list">
            <div class="form-submit-input-content">
                <div class="form-submit-input-title">{{$t('form.contractHash')}}{{index+2}}</div>
                <input class="form-submit-text"/>
            </div>
          </div>
          <div class="form-submit-input-wrapper">
            <div class="form-submit-input-content">
                <div class="form-submit-input-title">{{$t('form.contractABI')}}{{index+2}}</div>
                <input class="form-submit-text"/>
            </div>
          </div>
          <div class="form-submit-input-wrapper">
            <div class="form-submit-input-content">
                <div class="form-submit-input-title">{{$t('form.contractByteCode')}}{{index+2}}</div>
                <div class="form-submit-file-name-wrapper row" v-if="byteCodeFlag[0]">
                  <div class="form-submit-file-name col"  v-model="ContractByteCodeName[0]" :class="[errors.has(ContractByteCodeName[0])?'dapp-submit-input-error':'']" :name="ContractByteCodeName[0]" v-validate="'required'">{{ContractByteCodeName[0]}}</div>
                  <div class="form-submit-file-delete col" style="cursor:pointer;" @click="ByteCodeDelete(0)"><img class="form-submit-file-delete-img" src="../../assets/shares/close@2x.png" /></div>
                </div>
                <div class="form-submit-file"  v-if="!byteCodeFlag[0]" @click="uploadByteCode(0)" >{{$t('form.addFile')}}</div>
                <input id="dapp-submit-upload-ByteCode-0" type="file" style="display: none" @change="getUploadByteCodeFile(0)" >
                <div class="form-submit-input-text-error" v-show="errors.has('LogoName')">Here is can't be empty</div>
            </div>
          </div>


        </div>

        <div class="form-submit-input-wrapper">
          <div class="form-submit-input-content form-submit-input-btn-wrapper">
            <div class=" form-submit-input-btn-content-left">
              <div class="form-submit-input-btn" @click="addItem()">
                <span class="form-submit-input-btn-text">
                  {{$t('form.add')}}
                </span>
              </div>
            </div>
            <div class=" form-submit-input-btn-content">
              <div class="form-submit-delete-btn " @click="deleteItem()">
                <span class="form-submit-delete-btn-text">
                  {{$t('form.delete')}}
                </span>
              </div>
            </div>
          </div>
        </div>

       <div class="dapp-submit-button-submit" >
          <button class="dapp-submit-button" @click="submit">Submit</button>
        </div>
      </div>


      </div>

    </div>
  </div>

</template>

<script>
  import {mapState} from 'vuex'
  import $ from 'jquery'

export default {
  name: 'HelloWorld',
  components: {


  },
  data(){
    return{
      Name:'',
      Logo:'',
      LogoName:'',
      logoFlag:false,
      Description:'',
      DAppLogoName:'',
      WebLink:'',
      GitHubURL:'',
      ONTID:'',
      ContractHash1:'',
      ContractHash2:'',
      ContractHash3:'',
      ContractHash4:'',
      ContractHash5:'',
      ContractHash6:'',
      ContractHash7:'',
      ContractHash8:'',
      ContractHash9:'',
      ContractHash10:'',
      ContractABI1:'',
      ContractABI2:'',
      ContractABI3:'',
      ContractABI4:'',
      ContractABI5:'',
      ContractABI6:'',
      ContractABI7:'',
      ContractABI8:'',
      ContractABI9:'',
      ContractABI10:'',
      ContractByteCode:['','','','','','','','','',''],
      ContractByteCodeName:['','','','','','','','','',''],
      ContractByteCodeName1:'',
      ContractByteCode1:'',
      ContractByteCode2:'',
      ContractByteCode3:'',
      ContractByteCode4:'',
      ContractByteCode5:'',
      ContractByteCode6:'',
      ContractByteCode7:'',
      ContractByteCode8:'',
      ContractByteCode9:'',
      ContractByteCode10:'',
      byteCodeFlag:[false,false,false,false,false,false,false,false,false,false,],
      ContractByteCodeList:[{val:false},{val:false},{val:false},{val:false},{val:false},{val:false},{val:false},{val:false},{val:false},]


    }
  },
  props: {

  },
  computed: {
    ...mapState({
    })
  },
  created(){

  },
  methods:{
    toUrl($url){
      if($url){
        window.location.href = $url
      }
    },
    toBack(){
      this.$router.go(-1);
    },
    submit(){
      this.$validator.validateAll().then($result => {
/*         if ($result){
          var formData = new window.FormData()
          formData.append('name', )
          formData.append('url', )
          formData.append('img_url', )
          formData.append('summary', )
          formData.append('content', )
          formData.append('ont_id', )
          formData.append('dapp_screen_urls', )
          formData.append('telegram', )
          formData.append('twitter', )
          formData.append('discord', )
          formData.append('qq', )
          formData.append('github_url', )
          formData.append('contract_hash', )
          formData.append('abi', )
          formData.append('byte_code', )
          formData.append('token_name', )
          formData.append('token_type', )
          formData.append('donate_address', )
          formData.append('type', )
          formData.append('schedule', )
          this.$store.dispatch('uploadDappLogo',this.DAppLogo).then(res => {
            this.$store.dispatch('uploadDappshot',this.DAppShot).then(res => {
               this.$store.dispatch('submitDApp',this.DAppShot).then(res => {
               
               })
            })
          })
        } */
      })
    },
    uploadDAppLogo(){
      $('#dapp-submit-upload-Logo').click();
    },
    getUploadLogoFile(){
      let files = document.getElementById("dapp-submit-upload-Logo").files
      console.log(files)
      this.Logo = files
      this.LogoName = this.Logo[0].name
      this.logoFlag = !this.logoFlag
    },
    logoDelete(){
      this.logo = ''
      this.LogoName = ''
      this.logoFlag = !this.logoFlag
    },
    
    uploadByteCode($index){
      $('#dapp-submit-upload-ByteCode-'+$index).click();
    },
    getUploadByteCodeFile($index){
      let files = document.getElementById('dapp-submit-upload-ByteCode-'+$index).files
      this.ContractByteCode[$index] = files
      this.ContractByteCodeName[$index] = files[0].name
      console.log(this.ContractByteCodeName[$index])
      this.byteCodeFlag[$index] = !this.byteCodeFlag[$index]
      this.ContractByteCode.reverse().reverse()
      this.ContractByteCodeName.reverse().reverse()
      this.byteCodeFlag.reverse().reverse()
    },
    ByteCodeDelete($index){
      this.logo = ''
      this.LogoName = ''
      this.logoFlag = !this.logoFlag
    },
    addItem(){
      for(var i =0;i<9;i++){
        if(!this.ContractByteCodeList[i].val){
          this.ContractByteCodeList[i].val = true
          break
        }
      }
      this.ContractByteCodeList.reverse().reverse()
    },
    deleteItem(){
      this.ContractByteCodeList.reverse()
      for(var i =0;i<9;i++){
        if(this.ContractByteCodeList[i].val){
          this.ContractByteCodeList[i].val = false
          break
        }
      }      
      this.ContractByteCodeList.reverse()
    },
    uploadDAppShot(){
      $('#dapp-submit-upload-dapp-shot').click();
    },
    getUploadDappShot(){
      let files = document.getElementById("dapp-submit-upload-dapp-shot").files
      console.log(files)
      this.DAppShot = files
      this.DAppShotName = this.DAppShot[0].name
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .dapp-submit-background {
    padding-top: 80px;
    padding-bottom: 173px;
    background:rgba(245,242,242,1);
  }
  .dapp-submit-container {
    width: 100%;
    padding-right: 15px;
    padding-left: 15px;
    margin-right: auto;
    margin-left: auto;
  }
  .dapp-submit-back{
    margin-bottom: 24px;
    font-size:16px;
    font-family:SourceSansPro-Regular,sans-serif;
    font-weight:400;
    color:rgba(0,0,0,1);
    line-height:20px;
    cursor: pointer;
  }
  .dapp-submit-title-border{
    background:rgba(255,255,255,1);
    padding: 57px 80px;
  }
  .dapp-submit-title{
    font-size:32px;
    font-family:PingFangSC-Semibold,sans-serif;
    font-weight:600;
    color:rgba(0,0,0,1);
    line-height:45px;
    text-align: center;
  }
  .form-submit-border{
    /* background:rgba(255,255,255,1); */
    width: 100%;
    margin-top: 6px;
    padding: 0px 0px;
  }
  .dapp-submit-border-responsive{
    background:rgba(255,255,255,1);
    margin-top: 16px;
    padding: 0px 24px;    
  }
  .dapp-submit-border-title{
    font-size:24px;
    font-family:SourceSansPro-Semibold,sans-serif;
    font-weight:600;
    color:rgba(0,0,0,1);
    line-height:31px;
    text-align: center;
  }
  .dapp-submit-border-subtitle{
    font-size:18px;
    font-family:SourceSansPro-Regular,sans-serif;
    font-weight:400;
    color:rgba(0,0,0,1);
    line-height:23px;
    text-align: center;
    margin-top: 24px;
    margin-bottom: 16px;
  }
  .dapp-submit-input-group{
    margin-top: 40px;
  }
  .dapp-submit-input-title{
    font-size:14px;
    font-family:SourceSansPro-Semibold,sans-serif;
    font-weight:600;
    color:rgba(43,64,69,1);
    line-height:18px;
  }
  .dapp-submit-input{
    width:320px;
    height:42px;
    border-radius:2px;
    border:1px solid rgba(170,179,180,1);
    margin-top: 19px;
    font-size:16px;
    font-family:SourceSansPro-Regular,sans-serif;
    font-weight:400;
    color:rgba(43,64,69,1);
    line-height:20px;
    padding: 11px;
  }
  .dapp-submit-input:hover,
  .dapp-submit-input:active,
  .dapp-submit-input:focus{
    box-shadow:none !important;
    outline:none !important;
  }
  .dapp-submit-input-error{
    border:1px solid rgba(203,126,0,1);
  }
  .dapp-submit-input-desc{
    font-size:14px;
    font-family:SourceSansPro-Regular,sans-serif;
    font-weight:400;
    color:rgba(43,64,69,1);
    line-height:18px;
    margin-top: 10px;
  }
  .dapp-submit-input-text-error{
    font-size:14px;
    font-family:SourceSansPro-Semibold,sans-serif;
    font-weight:600;
    color:rgba(203,126,0,1);
    line-height:18px;
    margin-top: 10px;
  }
  .dapp-submit-input-group-inline{
    display: inline-block;
    vertical-align: top;
  }
  .dapp-submit-upload-button{
    cursor: pointer;
    font-size:16px;
    font-family:SourceSansPro-Semibold,sans-serif;
    font-weight:600;
    color:rgba(50,164,190,1);
    line-height:20px;
    margin-left: 16px;
    display: inline-block;
  }
  .dapp-submit-input-group-margin-left{
    margin-left: 80px;
  }
  .dapp-submit-select{
    width:320px;
    height:42px;
    border-radius:2px;
    border:1px solid rgba(170,179,180,1);
    background: white;
    margin-top: 19px;
    font-size:16px;
    font-family:SourceSansPro-Regular,sans-serif;
    font-weight:400;
    color:rgba(43,64,69,1);
    line-height:20px;
    padding: 11px 12px;
    cursor: pointer;
  }
  .dapp-submit-select:hover,
  .dapp-submit-select:active,
  .dapp-submit-select:focus{
    box-shadow:none !important;
    outline:none !important;
  }
  select {
    appearance:none;
    -moz-appearance:none;
    -webkit-appearance:none;
  }
  .dapp-submit-input-icon{
    margin-left: -26px;
    font-size:16px;
    cursor: pointer;
  }
  .dapp-submit-textare{
    padding: 11px 12px;
    margin-top: 19px;
    width:480px;
    height:168px;
    border-radius:2px;
    border:1px solid rgba(170,179,180,1);
    resize:none;
  }
  .dapp-submit-textare:hover,
  .dapp-submit-textare:active,
  .dapp-submit-textare:focus{
    box-shadow:none !important;
    outline:none !important;
  }
  .dapp-submit-button-submit{
    margin-top: 52px;
    text-align: center;
  }
  .dapp-submit-button{
    width:160px;
    height:38px;
    background:rgba(50,164,190,1);
    border-radius:3px;
    font-size:18px;
    font-family:SourceSansPro-Semibold,sans-serif;
    font-weight:600;
    color:rgba(255,255,255,1);
    line-height:23px;
    padding: 8px 52px;
    cursor: pointer;
  }
  .dapp-submit-button:hover,
  .dapp-submit-button:active,
  .dapp-submit-button:focus{
    box-shadow:none !important;
    outline:none !important;
  }
  .dapp-submit-content{
    padding: 48px 80px 54px 80px;
    background: rgba(255,255,255,1);
  }
  .dapp-submit-content-group{
    display: inline-block;
    vertical-align: top;
    width: 50%;
  }
  .dapp-submit-content-img{
    width:80px;
    height:80px;
  }
  .dapp-submit-content-img-title{
    font-size:18px;
    font-family:SourceSansPro-Semibold,sans-serif;
    font-weight:600;
    color:rgba(43,64,69,1);
    line-height:23px;
    margin-top: 32px;
    margin-bottom: 14px;
  }
  .dapp-submit-content-text{
    font-size:16px;
    font-family:SourceSansPro-Regular,sans-serif;
    font-weight:400;
    color:rgba(0,0,0,1);
    line-height:20px;
    display: inline-block;
  }
  .form-top-line{
    border-bottom:1px solid #d8d8d87a;
    padding: 39px 0 0 0;
  }

  @media screen and (max-width:482px) {
    .dapp-submit-container{
      max-width: 100%;
    }
  }
  @media screen and (min-width:482px) and (max-width:768px) {
    .dapp-submit-container{
      max-width: 450px;
    }
  }
  @media screen and (min-width:768px) and (max-width:1100px) {
    .dapp-submit-container{
      max-width: 736px;
    }
  }
  @media screen and (min-width:1100px) and (max-width:1444px) {
    .dapp-submit-container{
      max-width: 1068px;
    }
  }
  @media screen and (min-width:1444px) {
    .dapp-submit-container{
      max-width: 1068px;
    }
  }
  .form-submit-border-responsive{
    background:rgba(255,255,255,1);
    padding: 0px 24px;    
  }
  .form-submit-input-wrapper{
    margin-top:23px;
    text-align: center;
  }
  .form-submit-input-content{
    margin: auto;
    text-align: left;
    max-width: 570px;
  }
  .form-submit-input-title{
    height:23px;
    font-size:18px;
    font-family:SourceSansPro-Regular;
    font-weight:400;
    color:rgba(89,87,87,1);
    line-height:23px;
    margin-bottom: 8px;
  }
  .form-submit-text{
    width:100%;
    height:40px;
    background:rgba(255,255,255,1);
    border:1px solid rgba(175,172,172,1);
  }
  .form-submit-text-errer{
    width:100%;
    height:40px;
    background:rgba(255,255,255,1);
    border:1px solid rgba(185,0,36,1);
  }
  .form-submit-file{
    height:22px;
    font-size:18px;
    font-family:SourceSansPro-Regular;
    font-weight:400;
    color:rgba(50,164,190,1);
    line-height:25px;
    cursor: pointer;
  }
  .form-submit-desc{
    width:100%;
    height:130px;
    background:rgba(255,255,255,1);
    border:1px solid rgba(175,172,172,1);
  }
  .form-submit-file-name{
    height:20px;
    font-size:14px;
    font-family:OpenSans;
    color:rgba(0,174,29,1);
    line-height:19px;
  }
  .form-submit-file-delete-img{
    width:12px;
    margin-bottom: 2px;
  }
  .form-submit-input-btn{
      width:140px;
      height:40px;
      background:rgba(255,255,255,1);
      border:1px solid rgba(50,164,190,1);
      text-align: center;
      cursor: pointer;
  }
  .form-submit-input-btn:hover{
      background:rgba(50,164,190,0.1);
  }
  .form-submit-input-btn-text{
    width:76px;
    height:22px;
    font-size:18px;
    font-family:SourceSansPro-Regular;
    font-weight:400;
    color:rgba(50,164,190,1);
    line-height:40px;
  }
  .contract-line{
    border-bottom:1px solid rgba(216,216,216,1);
  }
  .form-submit-delete-btn{
    width:140px;
    height:40px;
    background:rgba(255,255,255,1);
    border:1px solid rgba(89,87,87,1);
    text-align: center;
    cursor: pointer;
  }
  .form-submit-delete-btn:hover{
    background:rgba(89,87,87,0.1);
    border:1px solid rgba(89,87,87,1);
  }
  .form-submit-delete-btn-text{
    width:76px;
    height:22px;
    font-size:18px;
    font-family:SourceSansPro-Regular;
    font-weight:400;
    color:rgba(89,87,87,1);
    line-height:40px;
  }
  .form-submit-input-btn-wrapper{
    display: flex;
  }
  .form-submit-input-btn-content-left{
     padding-right:32px;
  }
  .form-submit-input-list{
    transition: width 2s, height 2s, transform 2s;
    -moz-transition: width 2s, height 2s, -moz-transform 2s;
    -webkit-transition: width 2s, height 2s, -webkit-transform 2s;
    -o-transition: width 2s, height 2s,-o-transform 2s;
  }
  .form-submit-input-requiredtext{
    width:100px;
    font-size:14px;
    font-family:SourceSansPro-Regular;
    font-weight:400;
    color:rgba(185,0,36,1);
    line-height:18px;
    padding-bottom:17px;
  }
  .form-submit-input-requiredstar{
    width:8px;
    height:7px;
    color:rgba(185,0,36,1);
  }
  .form-submit-input-text-error{
    height:15px;
    font-size:12px;
    font-family:SourceSansPro-Regular;
    font-weight:400;
    color:rgba(185,0,36,1);
    line-height:15px;
  }
</style>
