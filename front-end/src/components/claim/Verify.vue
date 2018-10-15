<template>
  <div class="container container-margin-top">
    <div class="div-ont-id-detail-page form-group">
      <div class="row">
        <div class="col">
          <p class="font-Regular normal_color font-size18 title-more float-left pointer" @click="toReturn"><< {{
            $t('all.return') }}</p>
        </div>
      </div>
      <div class="row">
        <div class="col">
          <p class="text-center font-size40 font-ExtraLight normal_color">CLAIM VERIFY RESULT</p>
        </div>
      </div>
      <div class="background-body container-margin-top">
        <div>
          <div id="step1" class="card-cert">
            <div class="row card-row">
              <div class="col-sm-2 col-xs-3 img_wrap">
                <div class="card-title">
                  <b>STEP 1</b>
                </div>
              </div>
              <div class=" col-sm-7 col-xs-9 text_wrap_verify">
                <div class="card-title verify_text">
                  <b>Use the Merkle proof to verify</b>
                </div>
              </div>
              <div id="step1time" class="col-sm-3 col-xs-12 img_wrap">
                <img src="../../assets/gifs/timg.gif" class="card-cert-company-image" style="max-width:90px;">
              </div>
              <div id="step1result" class="col-sm-3 col-xs-12 img_wrap" style="display:none">
                <div>
                  <b>Height：8899</b>
                </div>
                <div>
                  <b class="font-size24 s-color">PASS</b>
                </div>
              </div>
            </div>
          </div>
          <div id="step2" class="card-cert" style="display:none">
            <div class="row card-row">
              <div class="col-sm-2 col-xs-3 img_wrap">
                <div class="card-title">
                  <b>STEP 2</b>
                </div>
              </div>
              <div class=" col-sm-7 col-xs-9 text_wrap_verify">
                <div class="card-title verify_text">
                  <b>Verify the signature</b>
                </div>
              </div>
              <div id="step2time" class="col-sm-3 col-xs-12 img_wrap">
                <img src="../../assets/gifs/timg.gif" class="card-cert-company-image" style="max-width:90px;">
              </div>
              <div id="step2result" class="col-sm-3 col-xs-12 img_wrap" style="display:none">
                <div>
                  <b class="font-size24 s-color">PASS</b>
                </div>
              </div>
            </div>
          </div>
          <div id="step3" class="card-cert" style="display:none">
            <div class="row card-row">
              <div class="col-sm-2 col-xs-3 img_wrap">
                <div class="card-title">
                  <b>STEP 3</b>
                </div>
              </div>
              <div class=" col-sm-7 col-xs-9 text_wrap_verify">
                <div class="card-title verify_text">
                  <b>Verify whether it has expired</b>
                </div>
              </div>
              <div id="step3time" class="col-sm-3 col-xs-12 img_wrap">
                <img src="../../assets/gifs/timg.gif" class="card-cert-company-image" style="max-width:90px;">
              </div>
              <div id="step3result" class="col-sm-3 col-xs-12 img_wrap" style="display:none">
                <div>
                  <b>Not expired</b>
                </div>
                <div>
                  <b class="font-size24 s-color">PASS</b>
                </div>
              </div>
            </div>
          </div>
          <div id="step4" class="card-cert" style="display:none">
            <div class="row card-row">
              <div class="col-sm-2 col-xs-3 img_wrap">
                <div class="card-title">
                  <b>STEP 4</b>
                </div>
              </div>
              <div class=" col-sm-7 col-xs-9 text_wrap_verify">
                <div class="card-title verify_text">
                  <b>Whether it has been revoked</b>
                </div>
              </div>
              <div id="step4time" class="col-sm-3 col-xs-12 img_wrap">
                <img src="../../assets/gifs/timg.gif" class="card-cert-company-image" style="max-width:90px;">
              </div>
              <div id="step4result" class="col-sm-3 col-xs-12 img_wrap" style="display:none">
                <div>
                  <b>Not revoked</b>
                </div>
                <div>
                  <b class="font-size24 s-color">PASS</b>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div id="return_key" class="text-center" style="margin-top:30px;display:none">
        <button v-if="verifyresult" class="veriy_btn1" @click="toMain()">Return</button>
        <div v-else class="font-size32 important_color">Verification passed</div>
      </div>

    </div>
  </div>
</template>

<script>
  import {mapState} from 'vuex'

  export default {
    name: "claim-verify-page",
    data() {
      return {
        Ddo: {},
        claimflag: true,
        verifyresult: true
      }
    },
    created() {
      this.getClaimDetailPage()
      this.setTimeoutstep2 = setTimeout(() => {

          $('#step1time').css("display", "none")
          $('#step1result').css("display", "block")
          $('#step2').css("display", "block")
        }
        , 2000)
      this.setTimeoutstep3 = setTimeout(() => {
          $('#step2time').css("display", "none")
          $('#step2result').css("display", "block")
          $('#step3').css("display", "block")
        }
        , 4000)
      this.setTimeoutstep4 = setTimeout(() => {
          $('#step3time').css("display", "none")
          $('#step3result').css("display", "block")
          $('#step4').css("display", "block")
        }
        , 6000)
      this.setTimeoutstep4 = setTimeout(() => {
          $('#step4time').css("display", "none")
          $('#step4result').css("display", "block")
          $('#return_key').css("display", "block")
        }
        , 8000)
    },
    watch: {
      '$route': 'getgetClaimPage',
      'getClaim.info': function () {
      }
    },
    computed: {
      ...mapState({
        getClaim: state => state.ClaimDetailPage.ClaimInfo,
      })
    },
    methods: {
      verifyClaim() {
        this.verifyresult = !this.verifyresult
      },
      toMain() {
        this.$router.push({name: 'Home'})
      },
      getClaimDetailPage() {
        this.$store.dispatch('getClaim', this.$route.params).then()
      },
      toReturn() {
        this.$router.go(-1)
      },
      toTransactionDetailPage($TxnId) {
        this.$router.push({name: 'TransactionDetail', params: {txnHash: $TxnId}})
      },
      togetClaimPage($OntId) {
        this.$router.push({name: 'getClaim', params: {ontid: $OntId}})
      },
      getOntIDEvent: function ($event) {
        switch ($event.substr(0, 12)) {
          case "register Ont":
            return "Register ONT ID"
          case "add publicKe":
            return "Add publickey"
          case "remove publi":
            return "Remove publickey"
          case "add attribut":
            return "Add identity attribute"
          case "update attri":
            return "Update identity attribute"
          case "delete attri":
            return "Delete identity attribute"
          case "change recov":
            return "Change recovery"
          case "add recovery":
            return "Add recovery"
          case "remove attri":
            return "Remove attribute"
        }
      }
    }
  }
</script>

<style scoped>
  .div-ont-id-detail-page {
    /*     border: 1px solid rgba(0, 0, 0, 0.1); */
    border-radius: 0.25rem;
    padding: 15px;
  }

  .ont-id-detail-page-hr {
    height: 1px;
  }

  .ont-id-detail-page-check-hand {
    cursor: pointer;
  }

  .oid-tab-border-top-none {
    border-top: none;
  }

  .public-info {
    border: 1px solid rgba(0, 0, 0, 0.1);
  }

  .claim-info {
    border: 1px solid rgba(0, 0, 0, 0.1);
    padding: 10px
  }

  html, body {
    margin: 0;
    height: 100%;

  }

  .background-head {
    max-width: 1000px;
    margin: 0 auto;
    background: linear-gradient(#125BA5, #249ED9); /* 标准的语法（必须放在最后） */
    text-align: center;
  }

  .background-body {
    max-width: 1000px;
    margin: 0 auto;
    background: #F4F4F4;
  }

  .head-logo {
    margin-top: 6.4%;
  }

  .head-info {
    color: #8DD7FE;
    margin-top: 0.5%;
    font-size: 24px;
    padding-bottom: 12%;
  }

  .card-cert {
    width: 94%;
    border: 1px solid #FFFFFF;
    border-radius: 15px;
    background: #FFFFFF;
    margin: -10% 3% 2% 3%;
    display: inline-flex;
    margin-top: 1.06%;
  }

  .card-cert-logo {
    margin-top: 8.7%;
    margin-left: 5%;
  }

  .card-line {
    border-left: 1px solid #F0F0F0;
    margin-left: 5%;
  }

  .card-content {
    margin: 3.7% 0 3.7% 5%;
    width: 55%;
  }

  .card-info-list {
    margin: 0;
  }

  .card-info-line {
    margin-top: 1.2%;
  }

  .card-cert-company {
    float: right;
    margin-top: 3.7%;
    width: 15%;
    margin-left: 2.5%;
    text-align: center;
  }

  .card-cert-company-image {
    width: 75%;
  }

  .card-link {
    width: 94%;
    border: 1px solid #FFFFFF;
    border-radius: 15px;
    background: #FFFFFF;
    margin: -10% 3% 2% 3%;
    display: inline-flex;
    margin-top: 1.06%;
  }

  .card-github {
    width: 94%;
    border: 1px solid #FFFFFF;
    border-radius: 15px;
    background: #FFFFFF;
    margin: -10% 3% 2% 3%;
    display: inline-flex;
    margin-top: 1.06%;
  }

  .card-github-logo {
    margin-top: 10.7%;
    margin-left: 5%;
  }

  /* sulingxiao code */
  .img_wrap {
    text-align: center;
    margin: auto;
  }

  .text_wrap_verify {
    padding: 20px;
    border-left: 1px solid #f0f0f0
  }

  .card-row {
    width: 100% !important;
    margin-left: 0px !important;
    margin-right: 0px !important;
  }

  @media screen and (max-width: 768px) {
    .mobile-display {
      display: block !important
    }

    .pc-display {
      display: none !important
    }

    .card-info {
      color: #C3C1C7;
      font-size: 18px;
      margin-top: 5.3%
    }

    .card-row {
      width: 100%;
      margin-left: 0px !important;
      margin-right: 0px !important;
      padding: 36px 10px 10px !important;
    }

    .head-title {
      margin-top: 1.9%;
      color: white;
      font-size: 22px !important;
    }

    .text_wrap_verify {
      padding: 10px 0px;
      border-left: 0px solid #f0f0f0
    }

    .card-title {
      color: #32A4BE;
      font-size: 22px;
    }

    .card-cert-company-text {
      color: #32A4BE;
      text-align: center;
      margin-top: 1%;
      font-size: 22px;
    }

    .verify_text {
      text-align: center;
    }
  }

  @media screen and (min-width: 769px) {
    .mobile-display {
      display: none !important
    }

    .pc-display {
      display: block !important
    }

    .card-info {
      color: #C3C1C7;
      font-size: 26px;
      margin-top: 5.3%
    }

    .card-row {
      width: 100% !important;
      margin-left: 0px !important;
      margin-right: 0px !important;
    }

    .head-title {
      margin-top: 1.9%;
      color: white;
      font-size: 30px;
    }

    .text_wrap_verify {
      padding: 20px;
      border-left: 1px solid #f0f0f0
    }

    .card-title {
      color: #32A4BE;
      font-size: 28px;
    }

    .card-cert-company-text {
      color: #32A4BE;
      text-align: center;
      margin-top: 1%;
      font-size: 24px;
    }
  }
</style>
