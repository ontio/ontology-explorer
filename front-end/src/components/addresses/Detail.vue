<template>
  <div class="container container-margin-top">
    <list-title :name="$t('addressDetail.name')"></list-title>
    <detail-title :name="$t('addressDetail.address')" :val="$route.params.address"></detail-title>

    <!--主要余额显示-->
    <detail-block-2 :name1="$t('addressDetail.ontBalance')" :val1="$HelperTools.toFinancialVal(assetsVal.ont)" :rows1="'1.3'"
                    :name2="$t('addressDetail.ongBalance')" :val2="$HelperTools.toFinancialVal(assetsVal.ong)" :rows2="'1.3'">
    </detail-block-2>

    <!--可领取和未领取的ONG显示-->
    <div class="detail-col">
      <div class="row">
        <div class="col table1_item_title">
          <span class="f-color">{{ $t('addressDetail.claimable') }}</span>
          <span class="important_color">{{assetsVal.unboundong}}</span>
        </div>
      </div>
      <div class="row table1_item_title">
        <div class="col">
          <span class="f-color">{{ $t('addressDetail.unbound') }}</span>
          <span class="important_color">{{assetsVal.waitboundong}}</span>
        </div>
      </div>
    </div>

    <!--2018年万圣节南瓜活动资产-->
    <div class="row" v-if="havePumpkin">
      <div class="col">
        <div class="detail-col">
          {{ $t('addressDetail.oep8Assets') }}
          <div class="row pumpkin-color font-size14 text-center" style="margin-top: 20px">
            <div class="col">
              <div>{{ $t('assetName.pumpkin08' ) }}</div>
              <div class="font-size24">{{assetsVal.pumpkin08}}</div>
            </div>
            <div class="col">
              <div>{{ $t('assetName.pumpkin01' ) }}</div>
              <div class="font-size24">{{assetsVal.pumpkin01}}</div>
            </div>
            <div class="col">
              <div>{{ $t('assetName.pumpkin02' ) }}</div>
              <div class="font-size24">{{assetsVal.pumpkin02}}</div>
            </div>
            <div class="col">
              <div>{{ $t('assetName.pumpkin03' ) }}</div>
              <div class="font-size24">{{assetsVal.pumpkin03}}</div>
            </div>
          </div>

          <div class="row pumpkin-color font-size14 text-center" style="margin-top: 20px">
            <div class="col">
              <div>{{ $t('assetName.pumpkin04' ) }}</div>
              <div class="font-size24">{{assetsVal.pumpkin04}}</div>
            </div>
            <div class="col">
              <div>{{ $t('assetName.pumpkin05' ) }}</div>
              <div class="font-size24">{{assetsVal.pumpkin05}}</div>
            </div>
            <div class="col">
              <div>{{ $t('assetName.pumpkin06' ) }}</div>
              <div class="font-size24">{{assetsVal.pumpkin06}}</div>
            </div>
            <div class="col">
              <div>{{ $t('assetName.pumpkin07' ) }}</div>
              <div class="font-size24">{{assetsVal.pumpkin07}}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!--有其他的OEP-4/5资产-->
    <div class="row" v-if="haveOtherOep">
      <div class="col">
        <div class="detail-col">
          <span class="font-blod table1_item_title">{{ $t('addressDetail.oepOtherAssets') }}</span>
          <div v-for="(asset,index) in AssetBalance" v-if="index > 12" class="row font-size14 oep-4-5-div">
            <div class="table1_item_title font-Regular">
              <span class="f-color">{{ asset.AssetName.toUpperCase() + ": " }}</span>
              <span class="important_color">{{parseFloat(asset.Balance)}}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!--交易历史-->
    <div class="row">
      <div class="col">
        <div class="detail-col">
          <div class="row font-size18 font-blod normal_color">
            <div class="col">
              <!--{{ addressDetail.info.allPage }} {{ $t('addressDetail.txOnAddr') }}-->
              {{ $t('addressDetail.txns') }}
            </div>
            <!--<div class="col">-->
              <!--<button @click="downloadExcel()">导出</button>-->
              <!--<a href="" :download="'AllTxData-' + $route.params.address + '.xlsx'" id="hf"></a>-->
            <!--</div>-->
          </div>
          <div class="row table-responsive">
            <div class="col">
              <table v-if="info.TxnTotal !== 0" class="table">
                <thead>
                <tr>
                  <th class="td-tx-head table3_head font-size18 font-blod normal_color">{{$t('all.hash')}}</th>
                  <th class="td-tx-head table3_head font-size18 font-blod normal_color">{{$t('all.amount')}}</th>
                  <th class="td-tx-head table3_head font-size18 font-blod normal_color">{{$t('all.status')}}</th>
                  <th class="td-tx-head table3_head font-size18 font-blod normal_color">{{$t('all.time')}}</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="tx in TxnList">
                  <td class="font-size14 font-Regular f-color pointer" @click="toTransactionDetailPage(tx.TxnHash)">
                    {{tx.TxnHash.substr(0,16) + '...'}}
                  </td>

                  <td class="font-size14 font-Regular">
                    <span v-for="(tl,index) in tx.TransferList">
                      <!--支出-->
                      <span class="expenditure-color" v-if="tl.FromAddress === $route.params.address">
                        <span v-if="tl.AssetName.indexOf('pumpkin') > -1">
                          {{ '-' + parseFloat(tl.Amount) + ' ' + $t('assetName.' + tl.AssetName ) }}
                        </span>
                        <span v-else>
                          {{ '-' + parseFloat(tl.Amount) + ' ' + tl.AssetName.toUpperCase() }}
                        </span>
                      </span>
                      <!--收入-->
                      <span class="income-color" v-else>
                        <span v-if="tl.AssetName.indexOf('pumpkin') > -1">
                          {{ parseFloat(tl.Amount) + ' ' + $t('assetName.' + tl.AssetName ) }}
                        </span>
                        <span v-else>
                          {{ parseFloat(tl.Amount) + ' ' + tl.AssetName.toUpperCase() }}
                        </span>
                      </span>

                      <!--逗号分隔符-->
                      <span v-if="index !== tx.TransferList.length - 1"
                            :class="tl.FromAddress === $route.params.address ? 'expenditure-color' : 'income-color'">
                        {{ ', ' }}
                      </span>
                    </span>
                  </td>
                  <td class="font-size14 font-Regular s-color">{{ tx.ConfirmFlag === 1 ? 'Confirmed' : 'Failed' }}</td>
                  <td class="font-size14 font-Regular normal_color">{{$HelperTools.getTransDate(tx.TxnTime)}}</td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="row justify-content-center" style="margin-top: 30px;">
      <!--<div id="page" v-show="addressDetail.info.allPage > 10">-->
      <div id="page">
        <ul class="pagination">
          <li class="transaction-list-page-check-hand padding0" @click="goToPage(addressDetail.info.firstPage)" ><button class="goto_btn"><a>{{$t('page.First')}}</a> </button></li>
          <li class="transaction-list-page-check-hand padding0" @click="goToPage(addressDetail.info.lastPage)"><button style="border-left:0px" class="goto_btn"><a>{{$t('page.PreviousPage')}}</a></button></li>
          <li v-show="haveNext" class="transaction-list-page-check-hand padding0"
              @click="goToPage(addressDetail.info.nextPage)">
            <button style="border-left:0px" class="goto_btn"><a>{{$t('page.NextPage')}}</a></button></li>
          <!--<li class="transaction-list-page-check-hand padding0" @click="goToPage(addressDetail.info.finalPage)" ><button style="border-left:0px" class="goto_btn"><a>{{$t('page.Last')}}</a></button> </li>-->
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapState} from 'vuex'
  import XLSX from 'xlsx'

  export default {
    name: "address-detail-page",
    data() {
      return {
        Ddo: {},
        claimflag: true,
        AssetBalance: [],
        havePumpkin: false, // 标识是否显示2018年万圣节南瓜资产
        haveOtherOep: false, // 标识是否显示OEP-4/5资产
        TxnList: [],
        info: [],
        haveNext: false, // 标识是否显示下一页的导航按钮
        tmpDown: ''
      }
    },
    created() {
      if (this.$route.params.pageSize == undefined || this.$route.params.pageNumber == undefined) {
        this.toAddressDetailPage(this.$route.params.address)
      } else {
        this.getAddressDetailData()
      }
    },
    watch: {
      '$route': 'getAddressDetailData',
      'addressDetail.info.info': function () {
        this.info = this.addressDetail.info.info;
        this.AssetBalance = this.info.AssetBalance;
        if(this.info.AssetBalance.length > 4) { // 有南瓜资产
          this.havePumpkin = (this.info.AssetBalance[12].Balance !== '0' && this.info.AssetBalance[12].Balance !== 0 )
        }
        if(this.info.AssetBalance.length > 13) { // 有OEP4、5的其他资产
          this.haveOtherOep = true
        }
        this.TxnList = this.info.TxnList;
        this.haveNext = this.addressDetail.info.nextPage.pageNumber
      }
    },
    computed: {
      ...mapState({
        addressDetail: state => state.AddressDetailPage.AddressDetail,
      }),
      /**
       * 取出全部资产名称和值
       */
      assetsVal: function () {
        let retAssets = {};

        for(let index in this.AssetBalance) {
          retAssets[this.AssetBalance[index].AssetName] = this.AssetBalance[index].Balance
        }

        return retAssets
      }
    },
    methods: {
      getAddressDetailData() {
        this.$store.dispatch('getAddressDetailPage', this.$route.params).then()
      },
      downloadExcel() {
        this.$store.dispatch('getAddressDetailAllData', this.$route.params).then(res => {
          // console.log(res)
          // this.generatingExcelData(res)
        })
      },
      generatingExcelData(json) {
        let tmpJson = json[0];
        let keyMap = []; // 获取keys
        let saveJson = []; // 用来保存转换好的json

        // 解构Json数据
        json.unshift({});
        for (let k in tmpJson) {
          keyMap.push(k);
          json[0][k] = k;
        }
        json.map((v, i) => keyMap.map((k, j) => Object.assign({}, {
          v: v[k],
          position: (j > 25 ? this.getCharCol(j) : String.fromCharCode(65 + j)) + (i + 1)
        }))).reduce((prev, next) => prev.concat(next)).forEach((v, i) => saveJson[v.position] = {v: v.v});

        // 设置区域,比如表格从A1到D10
        let outputPos = Object.keys(saveJson);

        // 保存表标题、设置填充区域
        let tmpWB = {
          SheetNames: ['mySheet'],
          Sheets: {
            'mySheet': Object.assign(
              {},
              saveJson,
              {'!ref': outputPos[0] + ':' + outputPos[outputPos.length - 1]}
            )
          }
        };

        // 创建二进制对象写入转换好的字节流
        this.tmpDown = new Blob([this.s2ab(XLSX.write(tmpWB, {
          bookType: 'xlsx',
          bookSST: false,
          type: 'binary'
        }))], {type: ""});

        let href = URL.createObjectURL(this.tmpDown); // 创建对象超链接
        let hfCTX = document.getElementById('hf');
        hfCTX.href = href; // 绑定a标签
        hfCTX.click(); // 模拟点击实现下载
        setTimeout(function () { //延时释放
          URL.revokeObjectURL(this.tmpDown); // 用URL.revokeObjectURL()来释放这个object URL
        }, 100);
      },
      // 字符串转字符流
      s2ab(s) {
        let buf = new ArrayBuffer(s.length);
        let view = new Uint8Array(buf);
        for (let i = 0; i !== s.length; ++i) view[i] = s.charCodeAt(i) & 0xFF;
        return buf;
      },
      getCharCol(n) {
        let s = '', m = 0;
        while (n > 0) {
          m = n % 26 + 1;
          s = String.fromCharCode(m + 64) + s;
          n = (n - m) / 26
        }
        return s
      },
      toReturn() {
        this.$router.go(-1)
      },
      toTransactionDetailPage($TxnId) {
        if (this.$route.params.net == undefined) {
          this.$router.push({name: 'TransactionDetail', params: {txnHash: $TxnId}})
        } else {
          this.$router.push({name: 'TransactionDetailTest', params: {txnHash: $TxnId, net: "testnet"}})
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
      goToPage($Page) {
        var address = this.$route.params.address
        if (this.$route.params.net == undefined) {
          this.$router.push({
            name: 'AddressDetail',
            params: {address: address, pageSize: $Page.pageSize, pageNumber: $Page.pageNumber}
          })
        } else {
          this.$router.push({
            name: 'AddressDetailTest',
            params: {address: address, pageSize: $Page.pageSize, pageNumber: $Page.pageNumber, net: 'testnet'}
          })
        }
        this.getAddressDetailData()
      }
    }
  }
</script>

<style scoped>
  .no-margin-bottom {
    margin-bottom: 0 !important;
  }

  .income-color {
    color: #00AE1D;
  }

  .expenditure-color {
    color: #32A4BE;
  }

  .oep-4-5-div {
    margin-top: 15px;
    padding: 0 15px;
  }

  .table1_item_title {
    font-size: 18px;
  }

  @media screen and (max-width: 768px) {
    .table1_item_title {
      font-size: 14px;
    }
  }
</style>
