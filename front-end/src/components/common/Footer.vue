<template>
  <div class="footer-background">

    <div class="e-container footer-container mobile-container">

      <div class="footer-logo-title">
        <img class="footer-logo-img" src="../../assets/footer/ont logo footer.png">
        <label class="footer-logo-label">></label>
        <label class="footer-logo-text">{{ $t('footer.Explorer') }}</label>
      </div>

      <div class="footer-content-info">
        <div class="footer-content">
          <div class="mobile-foot-line-developer"></div>
          <div class="footer-content-title">{{ $t('footer.Development') }}</div>
<!--           <div class="footer-content-div"><a class="footer-content-text" href="https://ontio.github.io/documentation/tutorial_for_developer_en.html">{{ $t('footer.Tutorials') }}</a></div> -->
          <div class="footer-content-div"><a class="footer-content-text" href="https://developer.ont.io/">{{ $t('footer.DeveloperCenter') }}</a></div>
          <div class="footer-content-div"><a class="footer-content-text" href="https://dev-docs.ont.io/#/">{{ $t('footer.Documentation') }}</a></div>
          <div class="footer-content-div"><a class="footer-content-text" href="https://bounty.ont.io">{{ $t('footer.Bounty') }}</a></div>
          <div class="footer-content-div"><a class="footer-content-text" href="https://smartx.ont.io">{{ $t('footer.SmartX') }}</a></div>
          <div class="footer-content-div"><a class="footer-content-text" href="https://github.com/ontio">{{ $t('footer.Github') }}</a></div>
        </div>

<!--         <div class="footer-content">
          <div class="mobile-foot-line"></div>
          <div class="footer-content-title">{{ $t('footer.Cooperation') }}</div>
          <div class="footer-content-div"><a class="footer-content-text" href="https://info.ont.io/cooperation/en">{{ $t('footer.CooperateWithOntology') }}</a></div>
          <div class="footer-content-div"><a class="footer-content-text" href="https://info.ont.io/listtriones/en">{{ $t('footer.Nodes') }}</a></div>
        </div> -->

        <div class="footer-content">
          <div class="mobile-foot-line"></div>
          <div class="footer-content-title">{{ $t('footer.About') }}</div>
<!--           <div class="footer-content-div"><a class="footer-content-text" href="https://info.ont.io/news/en">{{ $t('footer.Press') }}</a></div>
          <div class="footer-content-div" @click="toTechNews()" ><a class="footer-content-text" >{{ $t('footer.TechNews') }}</a></div>
          <div class="footer-content-div"><a class="footer-content-text" href="https://info.ont.io/team/en">{{ $t('footer.Team') }}</a></div> -->
          <div class="footer-content-div"><a class="footer-content-text" href="https://ont.io/#/aboutus">{{ $t('footer.AboutOntology') }}</a></div>
          <div class="footer-content-div"><a class="footer-content-text" href="https://ontio.github.io/documentation/wp_download_en.html">{{ $t('footer.WhitePaper') }}</a></div>
<!--           <div class="footer-content-div"><a class="footer-content-text" href="https://explorer.ont.io">{{ $t('footer.Explorer') }}</a></div> -->
        </div>

        <div class="footer-content">
          <div class="mobile-foot-line"></div>
          <div class="footer-content-title">{{ $t('footer.dApps') }}</div>
          <div class="footer-content-div"><a class="footer-content-text" href="https://dapp.ont.io"></a></div>
          <div class="footer-content-div"><a class="footer-content-text" href="https://dapp.ont.io/">{{ $t('footer.dApp') }}</a></div>
          <div class="footer-content-div"><a class="footer-content-text" href="https://ont.io/#/dapp">{{ $t('footer.SubmitAdApp') }}</a></div>
        </div>
      </div>

      <div class="footer-language">
        <div :class="$t('footer.flag') == 'en' ? 'footer-content-text-active footer-language-content':'footer-language-content footer-content-text' " @click="changeLanguage('en')">English</div>
        <div :class="$t('footer.flag') == 'zh' ? 'footer-content-text-active footer-language-content':'footer-language-content footer-content-text' " style="margin-left: 40px;" @click="changeLanguage('zh')">中文</div>
      </div>

      <div class="footer-line"></div>

      <div class="footer-link">
        <div style="display: inline-block">
          <a href="https://t.me/OntologyNetwork"><img class="footer-link-telegram footer-link-img" src="../../assets/footer/telegram@2x.png"></a>
          <a href="https://discord.gg/4TQujHj"><img class="footer-link-discord footer-link-img" src="../../assets/footer/discord@2x.png"></a>
          <a href="https://twitter.com/OntologyNetwork"><img class="footer-link-twitter footer-link-img" src="../../assets/footer/twitter@2x.png"></a>
        </div>
        <div style="display: inline-block">
          <a href="https://www.facebook.com/Ontology-Network-468098413590227/"><img class="footer-link-facebook footer-link-img" src="../../assets/footer/facebook@2x.png"></a>
          <a href="https://old.reddit.com/r/OntologyNetwork/"><img class="footer-link-reddit footer-link-img" src="../../assets/footer/reddit@2x.png"></a>
          <a href="https://medium.com/ontologynetwork"><img class="footer-link-medium footer-link-img" src="../../assets/footer/medium@2x.png"></a>
        </div>
        <div style="display: inline-block">
          <a href="mailto:contact@ont.io"><img class="footer-link-email footer-link-img" src="../../assets/footer/email@2x.png"></a>
          <a href="https://github.com/ontio"><img class="footer-link-github footer-link-img" src="../../assets/footer/github@2x.png"></a>
          <a href="https://www.linkedin.com/company/ontology-network-official/"><img class="footer-link-linkedin footer-link-img" src="../../assets/footer/linkedin@2x.png"></a>
        </div>
      </div>
      <div class="footer-copyright">Copyright © 2018 The Ontology Team</div>


    </div>

  </div>
</template>

<script>
  import LangStorage from './../../helpers/lang'
export default {
  name: 'HelloWorld',
  props: {

  },
  methods:{
    changeLanguage($lang){
      this.$i18n.locale = $lang
      LangStorage.setLang(this.$i18n.locale)
    },
    toTechNews(){
      let lang = LangStorage.getLang('en')
      let url
      switch (lang) {
        case 'en':
          url = 'https://medium.com/ontologynetwork/tagged/tech'
          break
        case 'zh':
          url = 'https://medium.com/ontologynetwork/tagged/tech'
          break
        case 'jp':
          url = 'https://medium.com/ontologynetwork/tagged/tech'
          break
        case 'ko':
          url = 'https://medium.com/ontologynetwork/tagged/tech'
          break
        default:
          url = 'https://medium.com/ontologynetwork/tagged/tech'
          break;
      }
      window.location.href = url
    },
  }
}
</script>

<style scoped>
  .footer-copyright{
    margin-top: 20px;
  }
  .footer-background{
    background:#fff;
    padding-top: 40px;
    margin-top:64px;
  }

  .footer-width{
    width: 944px;
  }
  .footer-logo-title{
    margin-bottom: 25px;
  }
  .footer-logo-img{
    height: 18px;
  }
  .footer-logo-text{
    font-size:18px;
    font-family:SourceSansPro-Regular;
    font-weight:400;
    color:#595757;
    line-height:23px;
  }
  .footer-logo-label{
    opacity:0.2;
    color: black;
    margin-left: 16px;
    margin-right: 16px;
  }

  .footer-content{
/*     width: 224px; */
    width: 24%;
    display: inline-block;
    vertical-align: top;
  }
  .footer-content-title{
    font-size:16px;
    font-family:SourceSansPro-Bold;
    font-weight:600;
    color:#595757;
    line-height:20px;
  }
  .footer-content-div{
    margin-top: 14px;
  }
  .footer-content-text{
    font-size:14px;
    font-family:SourceSansPro-Regular;
    font-weight:400;
    color:#AFACAC !important;
    line-height:18px;
    cursor: pointer;
  }
  .footer-content-text-active{
    font-size:14px;
    font-family:SourceSansPro-Regular;
    font-weight:400;
    color:#595757 !important;
    line-height:18px;
    cursor: pointer;
  }
  .footer-content-text:hover{
    color:#32A4BE !important;;
    text-decoration:underline !important;
  }
  .footer-content-text:active{
    color:rgba(50,164,190,1) !important;;
    text-decoration:underline !important;
  }
  .footer-language{
    margin-top: 34px;
    text-align: center;
  }
  .footer-language-content{
    display: inline-block;
    vertical-align: top;
  }
  .footer-line{
    height:1px;
    border:1px solid #AFACAC;
    margin-top: 25px;
  }
  .footer-link{
    text-align: center;
    margin-bottom: 24px;
  }
  .footer-link-telegram{
    width: 32px;
    cursor: pointer;
  }
  .footer-link-discord{
    width: 27px;
  }
  .footer-link-twitter{
    width: 32px;
  }
  .footer-link-facebook{
    width: 26px;
  }
  .footer-link-reddit{
    width: 32px;
  }
  .footer-link-medium{
    width: 26px;
  }
  .footer-link-email{
    width: 30px;
  }
  .footer-link-github{
    width: 32px;
  }
  .footer-link-linkedin{
    width: 26px;
  }
  .footer-link-img{
    opacity: 0.4;
    cursor: pointer;
    margin-top: 23px;
    margin-left:22px;
    margin-right: 22px;
  }
  .footer-link-img:hover,
  .footer-link-img:active{
    opacity: 1;
  }
  .footer-copyright{
    font-size:14px;
    font-family:SourceSansPro-Regular;
    font-weight:400;
    color:rgba(170,179,180,1);
    line-height:18px;
    text-align: center;
    padding-bottom: 40px;
  }
  /*手机*/
  @media screen and (max-width:576px) {
    .footer-content{
      display: unset;
    }
    .mobile-foot-line-developer{
      height: 1px;
      opacity: 0.2;
      border: 1px solid #ffffff;
      margin-top: -17px;
      margin-bottom: 8px;
    }
    .mobile-foot-line {
      height: 1px;
      opacity: 0.2;
      border: 1px solid #ffffff;
      margin-top: 16px;
      margin-bottom: 8px;
    }
    .mobile-container{
      padding-left: 24px;
      padding-right: 24px;
    }
    .footer-background{
      background:#ffffff;
      padding-top: 22px
    }
    .footer-link-img{
      opacity: 0.4;
      cursor: pointer;
      margin-top: 23px;
      margin-left:37px;
      margin-right: 37px;
    }
  }
  @media screen and (min-width:576px) and (max-width:768px) {
    .footer-content{
      width: 110px;
    }

  }
  @media screen and (min-width:769px) and (max-width:992px)  {
    .footer-content{
      width: 160px;
    }
  }

</style>
