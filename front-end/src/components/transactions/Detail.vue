<template>
  <div class="e-container margin-top-15">
    <list-title :name="$t('txDetail.name')"></list-title>
    <detail-title :name="$t('txDetail.txHash')" :val="txData.TxnHash"></detail-title>

    <!-- Transaction Detail Basic Info: -->
    <detail-block
      :params="[{name:$t('txDetail.time'), val:$HelperTools.getTransDate(txData.TxnTime)}]"></detail-block>
    <detail-block
      :params="[{name:$t('txDetail.type'), val:txData.TxnType === 209 ? $t('txDetail.sc') : $t('txDetail.deploySC')}]"></detail-block>

    <!--PC style-->
    <div class="d-none d-sm-block">
      <div class="row">
        <div class="col" @click="toBlockDetailPage(txData.Height)">
          <div class="detail-col detail-col-left">
            <span class="f-color">{{ $t('txDetail.height') }}</span>
            <span class="pointer important_color">{{txData.Height}}</span>
          </div>
        </div>
        <div class="col">
          <div class="detail-col detail-col-middle">
            <span class="f-color">{{ $t('txDetail.fee') }}</span>
            {{Number(txData.Fee)}}
            <span class="important_color">ONG</span>
          </div>
        </div>
        <div class="col">
          <div class="detail-col detail-col-right">
            <span class="f-color">{{ $t('txDetail.status') }}</span>
            <span v-if="txData.ConfirmFlag === 1" class="s-color">{{ $t('all.confirmed') }}</span>
            <span v-else class="f-color">{{ $t('all.failed') }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="d-block d-sm-none">
      <div class="detail-col font-size14">
        <div class="row">
          <div class="col">
            <div class="f-color">{{ $t('txDetail.height') }}</div>
            <div class="important_color" @click="toBlockDetailPage(txData.Height)">{{txData.Height}}</div>
          </div>
        </div>
      </div>
      <div class="detail-col font-size14">
        <div class="row">
          <div class="col">
            <div class="f-color">{{ $t('txDetail.fee') }}</div>
            <div class="important_color">{{ Number(txData.Fee) + ' ONG'}}</div>
          </div>
        </div>
      </div>
      <div class="detail-col font-size14">
        <div class="row">
          <div class="col">
            <div class="f-color">{{ $t('txDetail.status') }}</div>
            <div v-if="txData.ConfirmFlag === 1" class="s-color">{{ $t('all.confirmed') }}</div>
            <div v-else class="normal_color">{{ $t('all.failed') }}</div>
          </div>
        </div>
      </div>
    </div>

    <div class="pc-display">

      <!-- Transaction Detail Data Info: -->
      <table v-if="authflag" class="table table-hover">
        <thead>
        <tr>
        </tr>
        </thead>
        <tbody>
        <tr>
          <td class="td11" style="padding: 34px 24px;">
            <p class="font-size24  p_margin_bottom n_color font-Regular">{{ $t('all.description') }}:
              {{txData.Description}}</p>
          </td>
        </tr>
        </tbody>
      </table>

      <!--展示Issuer OntId和Description的数据块-->
      <detail-block v-if="recordflag" :params="issuerData"></detail-block>

      <!--展示ONT ID和Description的数据块-->
      <div v-if="idflag" class="row font-Regular font-size14">
        <div class="col">
          <div class="detail-col">
            <span class="font-size24 f-color font-Regular p_margin_bottom">{{ $t('all.ontId') }}</span>
            <p class="font-size14 important_color font-Regular p_margin_bottom pointer"
               @click="toOntIdDetailPage(Detail.OntId)">
              {{Detail.OntId}}
            </p>
            <span class="font-size24 f-color font-Regular p_margin_bottom">{{ $t('all.description') }}:</span>
            <p class="font-size14 font-Regular p_margin_bottom">
              {{Detail.Description}}
            </p>
          </div>
        </div>
      </div>

      <!--展示转账金额等详情的数据块-->
      <div v-if="txflag" class="row font-size14" v-for="tx in Detail.TransferList">
        <div class="col">
          <div class="detail-col trans-tx-col">
            <div class="row">
              <div class="col pointer" @click="toAddressDetailPage(tx.FromAddress)">{{tx.FromAddress}}</div>
              <div class="col">>></div>
              <div class="col-4 text-center font-weight-bold font-size18">{{toMoney(tx)}} <span class="text-uppercase">{{getAssetName(tx.AssetName)}}</span>
              </div>
              <div class="col text-right">>></div>
              <div class="col text-right pointer" @click="toAddressDetailPage(tx.ToAddress)">{{tx.ToAddress}}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- mobile -->
    <div class="mobile-display">

      <!--展示Issuer OntId和Description的数据块-->
      <detail-block v-if="recordflag" :params="issuerData"></detail-block>

      <table v-if="authflag" class="table table-hover">
        <thead>
        <tr>
        </tr>
        </thead>
        <tbody>
        <tr>
          <td class="td11" style="padding: 34px 24px;">
            <p class="font-size24  p_margin_bottom n_color font-Regular">Description:</p>
            <p class="font-size14 f-color p_margin_bottom font-Regular">{{txData.Description}}</p>
          </td>
        </tr>
        </tbody>
      </table>

      <table v-if="idflag" class="table table-hover">
        <thead>
        <tr>
        </tr>
        </thead>
        <tbody>
        <tr>
          <td class="td11" style="padding: 34px 24px;">
            <p class="font-size24  p_margin_bottom f-color font-Regular">OntId:</p>
            <p class="font-size14 important_color p_margin_bottom font-Regular pointer">{{Detail.OntId}}</p>
            <p class="font-size24  p_margin_bottom f-color font-Regular">Description:</p>
            <p class="font-size14 f-color p_margin_bottom font-Regular">{{Detail.Description}}</p>
          </td>
        </tr>
        </tbody>
      </table>
      <table v-if="txflag" class="table table-hover">
        <thead>
        <tr>
        </tr>
        </thead>
        <tbody>
        <tr v-for="tx in Detail.TransferList">
          <td class="td11" style="background-color:#32A4BE;color:white;padding: 34px 24px;">
            <div class="row ">
              <div class="col-lg-4 padding0-right" @click="toAddressDetailPage(tx.FromAddress)">{{tx.FromAddress}}</div>
              <div class="col-lg-1 ">>></div>
              <div class="col-lg-2 ">{{toMoney(tx)}} {{getAssetName(tx.AssetName)}}</div>
              <div class="col-lg-1 ">>></div>
              <div class="col-lg-4 padding0-left" @click="toAddressDetailPage(tx.ToAddress)">{{tx.ToAddress}}</div>
            </div>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
  import {mapState} from 'vuex'
  import GetTransactionType from './../../common/OntMsg/GetTransactionType.js'

  export default {
    name: "transaction-detail-page",
    data() {
      return {
        Detail: '',
        txflag: false,
        idflag: false,
        recordflag: false,
        authflag: false
      }
    },
    created() {
      this.getTxData()
    },
    watch: {
      '$route': 'getTxData',
      'txData': function () {
        if (this.txData.ConfirmFlag === 1) {
          if (this.txData.Detail == undefined) {
            if (this.txData.Description === '') {
              this.recordflag = false;
            } else {
              this.recordflag = true;
            }
          } else {
            this.Detail = this.txData.Detail
            if (this.Detail.OntId != undefined) {
              this.idflag = true;
              this.txflag = false;
            } else {
              this.txflag = true;
              this.idflag = false;
            }
          }

          if (this.txData.Description == "auth") {
            this.authflag = true
          } else {
            this.authflag = false
          }
        } else {
          this.txflag = false;
          this.idflag = false;
        }
      }
    },
    computed: {
      ...mapState({
        txData: state => state.Transactions.Detail,
      }),
      issuerData: function () {
        return [
          {name: this.$t('txDetail.issuer'), val: this.txData.Description.substr(12, 42), rows: 2},
          {name: this.$t('all.description'), val: this.txData.Description.substr(55), rows: 2}
        ]
      }
    },
    methods: {
      /**
       * 获取资产的真实名称
       *
       * @param name
       * @return {*|string}
       */
      getAssetName(name) {
        let realname = '';

        switch (name) {
          case 'pumpkin01':
            realname = this.$t('assetName.pumpkin01');
            break;
          case 'pumpkin02':
            realname = this.$t('assetName.pumpkin02');
            break;
          case 'pumpkin03':
            realname = this.$t('assetName.pumpkin03');
            break;
          case 'pumpkin04':
            realname = this.$t('assetName.pumpkin04');
            break;
          case 'pumpkin05':
            realname = this.$t('assetName.pumpkin05');
            break;
          case 'pumpkin06':
            realname = this.$t('assetName.pumpkin06');
            break;
          case 'pumpkin07':
            realname = this.$t('assetName.pumpkin07');
            break;
          case 'pumpkin08':
            realname = this.$t('assetName.pumpkin08');
            break;
          case 'ong':
            realname = 'ONG';
            break;
          case 'ont':
            realname = 'ONT';
            break;
          default:
            realname = name
        }

        return realname;
      },
      toMoney(txTmp) {
        if (txTmp.AssetName === 'ont') {
          let num = txTmp.Amount;
          return num.split('').reverse().join('').substr(10, 10).split('').reverse().join('')
        } else {
          return Number(txTmp.Amount)
        }
      },
      getTxData() {
        this.$store.dispatch('GetTransaction', this.$route.params).then()
      },
      getTransactionType($case) {
        return GetTransactionType.getTransactionType($case)
      },
      toBlockDetailPage($blockHeight) {
        if (this.$route.params.net == undefined) {
          this.$router.push({name: 'blockDetail', params: {param: $blockHeight}})
        } else {
          this.$router.push({name: 'blockDetailTest', params: {param: $blockHeight, net: "testnet"}})
        }
      },
      toAddressDetailPage($address) {
        if (this.$route.params.net == undefined) {
          this.$router.push({name: 'AddressDetail', params: {address: $address, pageSize: 20, pageNumber: 1}})
        } else {
          this.$router.push({
            name: 'AddressDetailTest',
            params: {address: $address, pageSize: 20, pageNumber: 1, net: "testnet"}
          })
        }
      },
      toOntIdDetailPage($ontid) {
        if (this.$route.params.net == undefined) {
          this.$router.push({name: 'OntIdDetail', params: {ontid: $ontid, pageSize: 20, pageNumber: 1}})
        } else {
          this.$router.push({
            name: 'OntIdDetailTest',
            params: {ontid: $ontid, pageSize: 20, pageNumber: 1, net: "testnet"}
          })
        }
      }
    }
  }
</script>

<style scoped>
  .trans-tx-col {
    background: #32A4BE;
    color: white;
    font-size: 14px;
  }
</style>
