<template>
  <div class="container margin-top-15 explorer-detail-tab">
    <list-title :name="$t('contracts.detail.name')"></list-title>
    <detail-title :name="$t('contracts.detail.hash')" :val="$route.params.contractHash"></detail-title>

    <detail-block-2 :name1="$t('contracts.detail.creator')" :val1="contractData.info.Creator" :rows1="'1.2'"
                    :params1="['address', contractData.info.Creator]"
                    :name2="$t('contracts.detail.createdTime')"
                    :val2="$HelperTools.getTransDate(contractData.info.CreateTime)" :rows2="'1.1'">
    </detail-block-2>

    <div class="detail-col font-Regular detail-col-fix">
      <div class="row">
        <div class="col">
          <div class="d-flex">
            <div class="img-sc-detail">
              <img v-if="contractData.info.Logo !== ''" :src="contractData.info.Logo" alt="">
              <div v-else class="sc-no-logo-detail">C</div>
            </div>
            <div class="sc-detail-desc">
              <h4>{{ contractData.info.Name }}</h4>
              <div class="f-color word-break d-block height-100 font-size14">
                <p>{{ contractData.info.Description }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="row font-size14" v-for="(scVal, scKey, scIndex) in contractData.info.ContactInfo">
        <div v-if="scIndex !== contractData.info.ContactInfo.length" class="sc-detail-divider-line"></div>

        <div class="col-2"><span class="normal_color">{{ scKey }}</span></div>
        <div class="col-10">
          <span class="f-color word-break d-block height-100 font-size14">
            <div class="height-100">{{ scVal }}</div>
          </span>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col">
        <div class="detail-col detail-col-left">
          <div class="f-color">{{ $t('tokens.list.tab.addressCount') }}&nbsp;
            <a href="#" data-toggle="tooltip" class="tooltip-style" :title="$t('tokens.detail.tip')">
              <i class="fa fa-info-circle" aria-hidden="true"></i>
            </a>
          </div>
          <div class="important_color font-size24 text-center">{{ $HelperTools.toFinancialVal(contractData.info.AddressCount) }}</div>
        </div>
      </div>
      <div class="col">
        <div class="detail-col detail-col-middle">
          <div class="f-color">{{ $t('tokens.detail.txn') }}</div>
          <div class="important_color font-size24 text-center">{{ $HelperTools.toFinancialVal(contractData.info.Total) }}</div>
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
            {{ $HelperTools.toFinancialVal(parseInt(contractData.info.OntCount)) + ' ONT, ' +
            $HelperTools.toFinancialVal(contractData.info.OngCount) + ' ONG'}}
          </div>
        </div>
      </div>
    </div>

    <!--更明显的展示方式，后期开放-->
    <!--<detail-block-2 :name1="$t('contracts.detail.ontFlow')" :val1="contractData.info.OntCount" :rows1="'1.1'"-->
    <!--:name2="$t('contracts.detail.ongFlow')" :val2="contractData.info.OngCount" :rows2="'1.1'">-->
    <!--</detail-block-2>-->

    <!-- Tab Control -->
    <ul class="nav nav-tabs f-color" role="tablist" style="margin-top: 4px;">
      <li class="nav-item">
        <a class="nav-link active" data-toggle="tab" href="#scTxn">{{ $t('all.txns') }} </a>
      </li>
      <li class="nav-item">
        <a class="nav-link" data-toggle="tab" href="#scCode">{{ $t('tokens.detail.code') }}</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" data-toggle="tab" href="#scABI">{{ $t('tokens.detail.abi') }}</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" data-toggle="tab" href="#scStatistic">{{ $t('tokens.detail.statistic') }}&nbsp;
          <a href="#" data-toggle="tooltip" class="tooltip-style" :title="$t('tokens.detail.tip')">
            <i class="fa fa-info-circle" aria-hidden="true"></i>
          </a>
        </a>
      </li>
    </ul>

    <!-- Tab panes -->
    <div class="tab-content">
      <div id="scTxn" class="container tab-pane active">
        <div class="row" v-if="contractData.Total !== 0">
          <div class="col">
            <div class="detail-col">
              {{ contractData.info.Total }}<span class="f-color"> {{ $t('contracts.detail.txOn') }}</span>
              <div class="table-responsive">
                <table class="table">
                  <thead>
                  <tr class="f-color">
                    <th class="td-tx-head font-size18 font-Blod">{{ $t('all.hash') }}</th>
                    <th class="td-tx-head font-size18 font-Blod">{{ $t('all.fee') }}</th>
                    <th class="td-tx-head font-size18 font-Blod">{{ $t('all.status') }}</th>
                    <th class="td-tx-head font-size18 font-Blod">{{ $t('all.block') }}</th>
                    <th class="td-tx-head font-size18 font-Blod">{{ $t('all.time') }}</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="tx in contractData.info.TxnList">
                    <td class="font-size14 important_color font-Regular pointer" @click="toTransDetailPage(tx.TxnHash)">
                      {{tx.TxnHash.substr(0,4) + '...' + tx.TxnHash.substr(60)}}
                    </td>
                    <td class="normal_color">{{Number(tx.Fee).toString()}}</td>
                    <td class="font-size14 s-color font-Regular" v-if="tx.ConfirmFlag === 1">
                      Confirmed
                    </td>
                    <td class="font-size14 f-color font-Regular" v-else>
                      Failed
                    </td>
                    <td class="font-size14 normal_color font-Regular">
                      {{tx.Height}}
                    </td>
                    <td class="font-size14 normal_color font-Regular">
                      {{$HelperTools.getTransDate(tx.TxnTime)}}
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>

        <turn-the-page v-if="contractData.allPage > 1"
                       :pagesInfo="contractData" :pagesName="'ContractDetail'"
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
                <span class="pull-right font-size14 font-ExtraLight copied-right" v-show="showCodeCopied">Copied!</span>
              </div>
              <textarea id="scCodeData" readonly rows="6">{{ contractData.info.Code }}</textarea>
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
              <textarea id="scABIData" readonly rows="6">{{contractData.info.ABI}}</textarea>
            </div>
          </div>
        </div>
      </div>
      <div id="scStatistic" class="container tab-pane">
        <div class="row">
          <div class="col">
            <div class="detail-col">
              <div class="row">
                <div class="col-xl-6 col-lg-6 col-md-12 col-sm-12" v-for="item in statisticsData.data">
                  <line-chart v-if="hackReset"
                              class="line-chart-style"
                              :labels="statisticsData.labels"
                              :label="14 + $t('statistics.day') + $t('statistics.' + item.label)"
                              :data="item.list"
                  >
                  </line-chart>
                </div>
              </div>
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
    name: "Contracts-Detail",
    created() {
      this.getContractData();
      this.getStatisticsData();
    },
    watch: {
      '$route': ['getContractData', 'getStatisticsData']
    },
    computed: {
      ...mapState({
        contractData: state => state.ContractData.Contract,
        statisticsData: state => state.Statistics.StatisticsData
      })
    },
    data() {
      return {
        showCodeCopied: false,
        showABICopied: false,
        hackReset: false
      }
    },
    methods: {
      getContractData() {
        this.contractData.info = '';

        this.$store.dispatch('getContract', this.$route.params).then();
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

        clipboard.on('success', function (e) {
          e.clearSelection();
        });

        if ($id === 'scCodeData') {
          this.showCodeCopied = true
        } else {
          this.showABICopied = true
        }
      },
      getStatisticsData() {
        this.statisticsData.data = '';

        this.hackReset = false;
        this.$nextTick(() => {
          this.hackReset = true
        });

        this.$store.dispatch('getStatisticsData', this.$route.params).then()
      }
    }
  }
</script>

<style scoped>
  .nav-tabs .nav-link {
    border: 0;
    border-radius: 0;
  }

  .tab-content > .container {
    padding: 0;
  }

  .tab-content > .container .detail-col {
    margin-top: 0;
  }

  .nav-item > a {
    color: #32A4BE;
  }

  .tab-content textarea {
    border: none;
    width: 100%;
    padding: 24px;
    font-size: 14px;
    color: #595757;
    background-color: #edf2f5;
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
    margin: 15px 0;
    width: 100%;
  }

  .img-sc-detail,
  .img-sc-detail > img {
    height: 106px;
    width: 106px;
  }

  .sc-detail-desc {
    margin-left: 30px;
  }

  .sc-no-logo-detail {
    width: 106px;
    height: 106px;
    background-color: #32A4BE;
    color: white;
    font-size: 32px;
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

  .line-chart-style {
    width: 100%;
    height: 240px;
    margin: 0 0 15px;
    background-color: transparent;
    border: 1px solid #edf2f5;
  }
</style>
