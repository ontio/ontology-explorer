<template>
  <div class="container margin-top-15 explorer-detail-tab">
    <list-title :name="$t('tokens.detail.name')"></list-title>
    <detail-title :name="$t('tokens.detail.hash')" :val="$route.params.contractHash"></detail-title>

    <detail-block-2 :name1="$t('tokens.detail.creator')" :val1="tokenData.info.Creator" :rows1="'1.2'"
                    :params1="['address', tokenData.info.Creator]"
                    :name2="$t('tokens.detail.createdTime')"
                    :val2="$HelperTools.getTransDate(tokenData.info.CreateTime)" :rows2="'1.1'">
    </detail-block-2>

    <div class="detail-col font-Regular detail-col-fix">
      <div class="row">
        <div class="col">
          <div class="d-flex">
            <div style="width: 106px; height: 106px;">
              <img v-if="tokenData.info.Logo !== ''" class="img-sc-detail" :src="tokenData.info.Logo" alt="">
              <div v-else class="token-no-logo-detail">
                {{ $route.params.type === 'oep4' ? tokenData.info.Symbol : tokenData.info.Name.substr(0, 2) }}
              </div>
            </div>

            <div class="token-detail-desc">
              <h4>{{ tokenData.info.Name }}
                <span v-if="$route.params.type === 'oep4'">&nbsp;&nbsp;{{ '(' + tokenData.info.Symbol + ')' }}</span>
              </h4>
              <div class="f-color word-break d-block height-100 font-size14">
                <p>{{ tokenData.info.Description }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!--oep-8 资产-->
      <div class="row" v-if="$route.params.type === 'oep8'">
        <div class="b-detail-divider-line"></div>
        <div class="col-12 f-color">{{ $t('addressDetail.oep8Assets') }}</div>
        <div class="col-3 text-center symbol-top" v-for="(item, index) in tokenData.info.Symbol">
          <div class="font-blod">{{ item }}</div>
          <div class="important_color font-size24">{{ $HelperTools.toFinancialVal(tokenData.info.TotalSupply[index]) }}</div>
        </div>
      </div>

      <!--<div class="b-detail-divider-line"></div>-->

      <div class="row font-size14" v-for="(scVal, scKey, scIndex) in tokenData.info.ContactInfo">
        <div v-if="scIndex !== tokenData.info.ContactInfo.length" class="sc-detail-divider-line"></div>

        <div class="col-2"><span class="normal_color">{{ scKey }}</span></div>
        <div class="col-10">
          <span class="f-color word-break d-block height-100 font-size14">
            <div class="height-100">{{ scVal }}</div>
          </span>
        </div>
      </div>
    </div>

    <!--TotalSupply & Decimals-->
    <detail-block-2 :name1="$t('tokens.detail.totalSupply')"
                    :val1="$HelperTools.toFinancialVal(tokenData.info.TotalSupply)"
                    :rows1="'1.3'"
                    :name2="$t('tokens.detail.decimals')"
                    :val2="tokenData.info.Decimals"
                    :rows2="'1.3'">
    </detail-block-2>

    <!--addresses & transactions & volume-->
    <div class="row">
      <div class="col">
        <div class="detail-col detail-col-left">
          <div class="f-color">{{ $t('tokens.list.tab.addressCount') }}&nbsp;
            <a href="#" data-toggle="tooltip" class="tooltip-style" :title="$t('tokens.detail.tip')">
              <i class="fa fa-info-circle" aria-hidden="true"></i>
            </a>
          </div>
          <div class="important_color font-size24 text-center">{{ $HelperTools.toFinancialVal(tokenData.info.AddressCount) }}</div>
        </div>
      </div>
      <div class="col">
        <div class="detail-col detail-col-middle">
          <div class="f-color">{{ $t('tokens.detail.txn') }}</div>
          <div class="important_color font-size24 text-center">{{ $HelperTools.toFinancialVal(tokenData.info.Total) }}</div>
        </div>
      </div>
      <div class="col">
        <div class="detail-col detail-col-right">
          <div class="f-color">{{ $t('tokens.detail.volume') }}&nbsp;
            <a href="#" data-toggle="tooltip" class="tooltip-style" :title="$t('tokens.detail.tip')">
              <i class="fa fa-info-circle" aria-hidden="true"></i>
            </a>
          </div>
          <div class="important_color font-size24 text-center">
            {{ $HelperTools.toFinancialVal(parseInt(tokenData.info.OntCount)) + ' ONT, ' +
            $HelperTools.toFinancialVal(tokenData.info.OngCount) + ' ONG'}}
          </div>
        </div>
      </div>
    </div>

    <!--更明显的展示方式，后期开放-->
    <!--<detail-block-2 :name1="$t('tokens.detail.ontFlow')" :val1="tokenData.info.OntCount" :rows1="'1.1'"-->
    <!--:name2="$t('tokens.detail.ongFlow')" :val2="tokenData.info.OngCount" :rows2="'1.1'">-->
    <!--</detail-block-2>-->

    <!-- Tab Control -->
    <ul class="nav nav-tabs f-color" role="tablist" style="margin-top: 4px;">
      <li class="nav-item">
        <a class="nav-link active" data-toggle="tab" href="#scTxn">{{ $t('all.txns') }}</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" data-toggle="tab" href="#scCode">{{ $t('tokens.detail.code') }}</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" data-toggle="tab" href="#scABI">{{ $t('tokens.detail.abi') }}</a>
      </li>
    </ul>

    <!-- Tab panes -->
    <div class="tab-content">
      <div id="scTxn" class="container tab-pane active">
        <div class="row" v-if="tokenData.Total !== 0">
          <div class="col">
            <div class="detail-col">
              {{ tokenData.info.Total }}<span class="f-color"> {{ $t('tokens.detail.txOn') }}</span>
              <div class="table-responsive">
                <table class="table">
                  <thead>
                  <tr class="f-color">
                    <th class="td-tx-head font-size18 font-Blod">{{ $t('all.hash') }}</th>
                    <th class="td-tx-head font-size18 font-Blod">{{ $t('all.fee') }}</th>
                    <th v-if="$route.params.type === 'oep5'"
                         class="td-tx-head font-size18 font-Blod">{{ $t('tokens.detail.tokenLbl') }}</th>
                    <th v-if="$route.params.type === 'oep5'"
                        class="td-tx-head font-size18 font-Blod">{{ $t('tokens.detail.tokenImg') }}</th>
                    <th class="td-tx-head font-size18 font-Blod">{{ $t('all.status') }}</th>
                    <th class="td-tx-head font-size18 font-Blod">{{ $t('all.block') }}</th>
                    <th class="td-tx-head font-size18 font-Blod">{{ $t('all.time') }}</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="tx in tokenData.info.TxnList">
                    <td class="font-size14 important_color font-Regular pointer" @click="toTransDetailPage(tx.TxnHash)">
                      {{tx.TxnHash.substr(0,4) + '...' + tx.TxnHash.substr(60)}}
                    </td>
                    <td class="normal_color">{{Number(tx.Fee).toString()}}</td>

                    <td v-if="$route.params.type === 'oep5'"
                        class="normal_color">{{ typeof(tx.Jsonurl) === 'undefined' ? '' : tx.Jsonurl.name }}</td>
                    <td v-if="$route.params.type === 'oep5'"
                        class="normal_color">
                      <img width="100px" :src="typeof(tx.Jsonurl) === 'undefined' ? '' : tx.Jsonurl.image" alt="">
                    </td>

                    <td class="font-size14 s-color font-Regular" v-if="tx.ConfirmFlag === 1">Confirmed</td>
                    <td class="font-size14 f-color font-Regular" v-else>Failed</td>

                    <td class="font-size14 normal_color font-Regular">{{tx.Height}}</td>
                    <td class="font-size14 normal_color font-Regular">{{$HelperTools.getTransDate(tx.TxnTime)}}</td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>

        <turn-the-page v-if="tokenData.allPage > 1"
                       :pagesInfo="tokenData" :pagesName="'TokenDetail'"
                       :who="'contractHash'" :paramVal="$route.params.contractHash"></turn-the-page>
      </div>
      <div id="scCode" class="container tab-pane">
        <div class="row">
          <div class="col">
            <div class="detail-col">
              <div class="copy-bottom">
                <span class="pull-right pointer font-size14">
                  <i @click="copyDetailVal('scCodeData')"
                     data-clipboard-target="#scCodeData"
                     class="copy-success l-25px fa fa-clone"
                     aria-hidden="true"></i>
                </span>
                <span class="pull-right font-size14 font-Regular copied-right" v-show="showCodeCopied">Copied!</span>
              </div>
              <textarea id="scCodeData" readonly rows="6">{{ tokenData.info.Code }}</textarea>
            </div>
          </div>
        </div>
      </div>
      <div id="scABI" class="container tab-pane">
        <div class="row">
          <div class="col">
            <div class="detail-col"><div class="copy-bottom">
                <span class="pull-right pointer font-size14">
                  <i @click="copyDetailVal('scABIData')"
                     data-clipboard-target="#scABIData"
                     class="copy-success l-25px fa fa-clone"
                     aria-hidden="true"></i>
                  </span>
              <span class="pull-right font-size14 font-ExtraLight copied-right" v-show="showABICopied">Copied!</span>
            </div>
              <textarea id="scABIData" readonly rows="6">{{tokenData.info.ABI}}</textarea>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapState} from 'vuex'
  import Clipboard from 'clipboard';

  export default {
    name: "Token-Detail",
    created() {
      this.getTokenData()
    },
    watch: {
      '$route': 'getTokenData'
    },
    computed: {
      ...mapState({
        tokenData: state => state.TokenData.Token
      })
    },
    data() {
      return {
        showCodeCopied: false,
        showABICopied: false
      }
    },
    methods: {
      getTokenData() {
        this.$store.dispatch('getToken', this.$route.params).then();
      },
      toTransDetailPage($TxnId) {
        if (this.$route.params.net === 'testnet') {
          this.$router.push({name: 'TransactionDetailTest', params: {txnHash: $TxnId, net: 'testnet'}})
        } else {
          this.$router.push({name: 'TransactionDetail', params: {txnHash: $TxnId}})
        }
      },
      copyDetailVal($id) {
        let clipboard = new Clipboard('.copy-success');

        clipboard.on('success', function(e) {
          e.clearSelection();
        });

        if($id === 'scCodeData') {
          this.showCodeCopied = true
        } else {
          this.showABICopied = true
        }
      }
    }
  }
</script>

<style scoped>
  .tab-content > .container {
    padding: 0;
  }

  .tab-content > .container .detail-col {
    margin-top: 0;
  }

  .tab-content textarea {
    border: none;
    width: 100%;
    padding: 0 10px 0 0;
    font-size: 14px;
    color: #595757;
    background-color: #edf2f5;
  }

  .symbol-top {
    margin-top: 15px;
  }

  .detail-col-fix {
    padding: 32px 24px 34px !important;;
  }

  .height-100 {
    height: 100%;
  }

  .b-detail-divider-line {
    background: #e5e4e4;
    height: 1px;
    margin: 15px;
    width: 100%;
  }

  .img-sc-detail {
    height: 106px;
    width: 106px;
    border-radius: 53px;
  }

  .token-detail-desc {
    margin-left: 30px;
  }

  .token-no-logo-detail {
    width: 106px !important;
    height: 106px !important;
    border-radius: 53px;
    background-color: #32A4BE;
    color: white;
    font-size: 24px;
    font-weight: bold;
    line-height: 106px;
    text-align: center;
  }

  .sc-detail-divider-line {
    background: #e5e4e4;
    height: 1px;
    margin: 15px;
    width: 100%;
  }

  .copied-right {
    margin-right: 10px;
  }

  .copy-bottom {
    margin-bottom: 5px;
  }
</style>
