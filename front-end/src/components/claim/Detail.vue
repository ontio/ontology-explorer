<template>
  <div class="container container-margin-top">
    <return-home></return-home>
    <list-title :name="$t('claimDetail.name')"></list-title>

    <div class="background-body" style="margin-top:60px;">
      <div v-for="(claim,index) in getClaim.info">
        <div class="card-cert" v-if="claim.Context =='claim:employment_authentication'">
          <div class="row card-row">
            <div class="col-sm-2 col-xs-3 img_wrap">
              <img src="../../assets/share_ec.png" class="">
            </div>
            <div class=" col-sm-7 col-xs-9 text_wrap">
              <div class="card-title">
                <b>Employment certificate</b>
              </div>
              <div class="card-info">
                <dl class="card-info-list">
                  <dt class="card-info-line">Name: {{claim.Content.Name}}</dt>
                  <dt class="card-info-line">Company:Onchain</dt>
                  <dt class="card-info-line">Position: {{claim.Content.JobTitle}}</dt>
                </dl>
              </div>
            </div>
            <div class="col-sm-3 col-xs-12 img_wrap">
              <img src="../../assets/share_onchain.png" class="card-cert-company-image" style="max-width:90px;">
              <p class="card-cert-company-text">onchain</p>
            </div>
          </div>
        </div>
        <div class="card-github" v-if="claim.Context =='claim:github_authentication'">
          <div class="row  card-row">
            <div class="col-sm-2 col-xs-3 img_wrap">
              <img src="../../assets/share_gc.png">
            </div>
            <div class=" col-sm-7 col-xs-9 text_wrap">
              <div class="card-title important_color">
                <b>Github Claim</b>
              </div>
              <div class="card-info">
                <dl class="card-info-list">
                  <dt class="card-info-line">Name: {{claim.Content.Alias}}</dt>
                  <dt class="card-info-line">Email: {{claim.Content.Email}}</dt>
                </dl>
              </div>
            </div>
            <div class="col-sm-3 col-xs-12 img_wrap">
              <img src="../../assets/share_TrustAlliance.png" class="card-cert-company-image" style="max-width:90px;">
              <p class="card-cert-company-text">Trust Alliance</p>
            </div>
          </div>
        </div>
        <div class="card-link" v-if="claim.Context =='claim:linkedin_authentication'">
          <div class="row  card-row">
            <div class="col-sm-2 col-xs-3 img_wrap">
              <img src="../../assets/share_lc.png" class="">
            </div>
            <div class=" col-sm-7 col-xs-9 text_wrap">
              <div class="card-title">
                <b>Linkedin Claim</b>
              </div>
              <div class="card-info">
                <dl class="card-info-list">
                  <dt class="card-info-line">Name: {{claim.Content.Name}}</dt>
                  <dt class="card-info-line">Bio: {{claim.Content.Bio}}</dt>

                </dl>
              </div>
            </div>
            <div class="col-sm-3 col-xs-12 img_wrap">
              <img src="../../assets/share_TrustAlliance.png" class="card-cert-company-image" style="max-width:90px;">
              <p class="card-cert-company-text">Trust Alliance</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="text-center" style="margin-top:30px;">
      <button v-if="verifyresult" class="veriy_btn1" @click="verifyClaim()">Verify Claim</button>
      <div v-else class="font-size32 important_color">Verification passed</div>
    </div>

  </div>
</template>

<script>
  import {mapState} from 'vuex'
  import ReturnHome from '../common/ReturnHome'
  import ListTitle from '../common/ListTitle'

  export default {
    name: "claim-detail-page",
    components: {ReturnHome, ListTitle},
    data() {
      return {
        Ddo: {},
        claimflag: true,
        verifyresult: true
      }
    },
    created() {
      this.getClaimDetailPage()
    },
    watch: {
      '$route': 'getgetClaimPage'
    },
    computed: {
      ...mapState({
        getClaim: state => state.ClaimDetailPage.ClaimInfo,
      })
    },
    methods: {
      verifyClaim() {
        this.$router.push({name: 'ClaimVerify'})
      },
      getClaimDetailPage() {
        this.$store.dispatch('getClaim', this.$route.params).then()
      },
      toTransactionDetailPage($TxnId) {
        if (this.$route.params.net == undefined) {
          this.$router.push({name: 'TransactionDetail', params: {txnHash: $TxnId}})
        } else {
          this.$router.push({name: 'TransactionDetailTest', params: {txnHash: $TxnId, net: 'testnet'}})
        }
      },
      togetClaimPage($OntId) {
        if (this.$route.params.net == undefined) {
          this.$router.push({name: 'getClaim', params: {ontid: $OntId}})
        } else {
          this.$router.push({name: 'getClaimTest', params: {ontid: $OntId, net: 'testnet'}})
        }
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
  html, body {
    margin: 0;
    height: 100%;
  }

  .background-body {
    max-width: 1000px;
    margin: 0 auto;
    background: #F4F4F4;
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

  .card-info-list {
    margin: 0;
  }

  .card-info-line {
    margin-top: 1.2%;
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

  /* sulingxiao code */
  .img_wrap {
    text-align: center;
    margin: auto;
  }

  .text_wrap {
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

    .text_wrap {
      padding: 20px;
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

    .text_wrap {
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
