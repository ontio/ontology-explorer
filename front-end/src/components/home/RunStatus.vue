<template>
  <div class="e-container div-run-status">
    <div class="row">
      <div class="col col-click" @click="toBlockListPage">
        <span class="run-status-label">{{ $t('runStatus.CurrentHeight') }}</span>
        <span class="view-go-to">>></span>
        <span class="d-block run-status-p font-ExtraLight font-size48">
          <span>{{ $HelperTools.toFinancialVal(blockStatus.info.CurrentHeight) }}</span>
        </span>
      </div>
      <div class="col col-click" @click="toTransactionListPage">
        <span class="run-status-label">{{ $t('runStatus.TxnCount') }}</span>
        <span class="view-go-to">>></span>
        <span class="d-block run-status-p font-ExtraLight font-size48">
          <span>{{ $HelperTools.toFinancialVal(blockStatus.info.TxnCount) }}</span>
        </span>
      </div>
      <div class="col col-click" @click="toOnlineNodes" v-if="$route.params.net !== 'testnet'">
        <span class="run-status-label">{{ $t('runStatus.NodeCount') }}</span>
        <span class="view-go-to">>></span>
        <span class="d-block run-status-p font-ExtraLight font-size48">
          <span>{{ $HelperTools.toFinancialVal(blockStatus.info.NodeCount) }}</span>
        </span>
      </div>
      <!--<div class="col col-click" @click="toAddressListPage">-->
      <div class="col col-no-click-fix">
        <span class="run-status-label">{{ $t('runStatus.addressCount') }}</span>
        <!--<span class="view-go-to">>></span>-->
        <span class="d-block run-status-p font-ExtraLight font-size48">
          <span>{{ $HelperTools.toFinancialVal(blockStatus.info.AddressCount) }}</span>
        </span>
      </div>
      <div class="col col-click" @click="toOntIdListPage">
        <span class="run-status-label">{{ $t('runStatus.ontid') }}</span>
        <span class="view-go-to">>></span>
        <span class="d-block run-status-p font-ExtraLight font-size48">
          <span>{{ $HelperTools.toFinancialVal(blockStatus.info.OntIdCount) }}</span>
        </span>
      </div>
    </div>

    <div class="row chart-margin-top">
      <div class="col">
        <div id="chartwrapper" class="chart-container">
          <canvas id="myChart" class="mycanvas" height="90"></canvas>
        </div>
      </div>
    </div>
    <!--line-chart models-->
<!--     <div v-if="data" class="row">
      <div class="col-xl-6 col-lg-6 col-md-12 col-sm-12" >
        <line-chart class="line-chart-style"
          :labels="data.labels"
          :label=" '14' + $t('statistics.day') + $t('statistics.' + data.data.newAddress.label)"
          :data="data.data.newAddress.list"
          options="">
        </line-chart>
      </div>
      <div class="col-xl-6 col-lg-6 col-md-12 col-sm-12" >
        <line-chart class="line-chart-style"
          :labels="data.labels"
          :label=" '14' + $t('statistics.day') + $t('statistics.' + data.data.txn.label)"
          :data="data.data.txn.list"
          options="">
        </line-chart>
      </div>
    </div> -->
  </div>
</template>

<script>
  import {mapState, mapGetters} from 'vuex'
  import Helper from './../../helpers/helper.js'

  export default {
    name: "run-status",
    mounted: function () {
      this.createAChart()
    },
    data() {
      return {
        num: 1,
        runTime: 0,
        blockHeight: 0,
        blockTime: 0,
        dealNum: 0,
        node: 0,
        lastheight: 0,
        chartData: [],
        chartLabels: [],
        chartbackgroundColor: this.generateBgColor()
      }
    },
    created() {
      this.getTableData()
      this.getRunStatus()
      this.generateTime("76")
      this.intervalBlock = setInterval(() => {
        this.generateTime(1)
        this.getRunStatus()
      }, 3000)
    },
    watch: {
      '$route': 'getRunStatus',
      'getTime.info': function () {
        if (this.getTime.info.length > 1) {
          for (var i = 0; i < this.getTime.info.length; i++) {
            this.chartData[75 - i] = this.getTime.info[i].GenerateTime
            this.chartLabels[75 - i] = this.getTime.info[i].Height
            this.lastheight = this.getTime.info[i].Height
          }
          this.myChart.update();
        } else {
          if (this.getTime.info[0].Height !== this.lastheight) {
            this.chartData.splice(0, 1)
            this.chartLabels.splice(0, 1)
            this.chartData.push(this.getTime.info[0].GenerateTime)
            this.chartLabels.push(this.getTime.info[0].Height)
            this.removeData(this.myChart)
            this.addData(this.myChart, this.getTime.info[0].GenerateTime)
            this.lastheight = this.getTime.info[0].Height
          }
        }
      },
    },
    computed: {
      ...mapState({
        blockStatus: state => state.RunStatus.BlockStatus,
        getTime: state => state.RunStatus.GenerateTime,
        /* data: state => state.Statistics.StatisticsData, */
      })
    },
    methods: {
      getTableData() {
        var params={}
        params.day = 14
        this.$store.dispatch('getStatisticsData', params).then()
      },
      generateBgColor() {
        let retData = []
        for (let i = 0; i < 76; i++) {
          retData[i] = 'rgba(228, 228, 228, 1)'
        }
        retData.splice(75, 0, 'rgba(50, 164, 190, 1)')

        return retData
      },
      toBlockListPage() {
        if (this.$route.params.net == undefined) {
          this.$router.push({name: 'blockListDetail', params: {pageSize: 20, pageNumber: 1}})
        } else {
          this.$router.push({name: 'blockListDetailTest', params: {pageSize: 20, pageNumber: 1, net: "testnet"}})
        }
      },
      toTransactionListPage() {
        if (this.$route.params.net == undefined) {
          this.$router.push({name: 'TransactionList', params: {pageSize: 20, pageNumber: 1}})
        } else {
          this.$router.push({name: 'TransactionListTest', params: {pageSize: 20, pageNumber: 1, net: "testnet"}})
        }
      },
      toAddressListPage() {
        if (this.$route.params.net == undefined) {
          this.$router.push({name: 'addressList', params: {pageSize: 20, pageNumber: 1}})
        } else {
          this.$router.push({name: 'addressListTest', params: {pageSize: 20, pageNumber: 1, net: "testnet"}})
        }
      },
      toOntIdListPage() {
        if (this.$route.params.net == undefined) {
          this.$router.push({name: 'OntIdListDetail', params: {pageSize: 20, pageNumber: 1}})
        } else {
          this.$router.push({name: 'OntIdListDetailTest', params: {pageSize: 20, pageNumber: 1, net: 'testnet'}})
        }
      },
      toOnlineNodes() {
        this.$router.push({ name: 'NodeStakeList'})
        // window.location.href = 'https://monitor.ont.io/'
      },
      getRunStatus() {
        this.$store.dispatch('getRunStatus', this.$route.params).then()
      },
      generateTime(amount) {
        this.$route.params.amount = amount;
        this.$store.dispatch('generateTime', this.$route.params).then()
      },
      getDay($time) {
        return Helper.getDayFunc($time)
      },
      createAChart: function () {
        var ctx = document.getElementById("myChart");
        this.myChart = new Chart(ctx, {
          type: 'bar',
          data: {
            labels: this.chartLabels,
            datasets: [{
              label: 'BlockInterval',
              data: this.chartData,
              backgroundColor: this.chartbackgroundColor,
              borderColor: this.chartbackgroundColor,
              borderWidth: 1,
            }]
          },
          options: {
            showScale: false,
            responsive: true,
            maintainAspectRatio: false,
            scaleOverride: true, // 是否用硬编码重写y轴网格线
            scaleSteps: 0, //y轴刻度的个数
            scaleStepWidth: 10, //y轴每个刻度的宽度
            scaleStartValue: 0,  //y轴的起始值
            scaleLineColor: "rgba(255,255,255,1)",// x轴y轴的颜
            scaleLineWidth: 0,
            scaleShowHorizontalLines: false,
            scaleShowVerticalLines: false,

            scales: {
              yAxes: [{
                ticks: {
                  beginAtZero: true,
                  display: false
                },
                gridLines: {
                  offsetGridLines: true,
                  display: false
                },
                display: false
              }],
              xAxes: [{
                ticks: {
                  beginAtZero: true,
                  display: false
                },
                gridLines: {
                  offsetGridLines: true,
                  display: false
                },
                display: false
              }]
            },
            scaleShowGridLines: false,
            title: {
              dispaly: false
            },
            legend: {
              display: false,
              labels: {
                fontColor: 'rgb(151,187,205)'
              }
            }
          }
        });
      },
      addData: function (chart, data) {
        chart.data.datasets.forEach((dataset) => {
          dataset.data.push(data);
        });
        chart.update();
      },
      removeData: function (chart) {
        chart.data.datasets.forEach((dataset) => {
          dataset.data.pop();
        });
        chart.update();
      },
      numberAnimate: function (setting, className) {
        var defaults = {
          speed: 1000,//动画速度
          num: "", //初始化值
          iniAnimate: true, //是否要初始化动画效果
          symbol: '',//默认的分割符号，千，万，千万
          dot: 0 //保留几位小数点
        }

        //如果setting为空，就取default的值
        var setting = $.extend(defaults, setting);

        //如果对象有多个，提示出错
        if ($(className).length > 1) {
          alert("just only one obj!");
          return;
        }

        //如果未设置初始化值。提示出错
        if (setting.num == "") {
          alert("must set a num!");
          return;
        }
        var nHtml = '<div class="mt-number-animate-dom" data-num="{{num}}">\
                       <span class="mt-number-animate-span">0</span>\
                       <span class="mt-number-animate-span">1</span>\
                       <span class="mt-number-animate-span">2</span>\
                       <span class="mt-number-animate-span">3</span>\
                       <span class="mt-number-animate-span">4</span>\
                       <span class="mt-number-animate-span">5</span>\
                       <span class="mt-number-animate-span">6</span>\
                       <span class="mt-number-animate-span">7</span>\
                       <span class="mt-number-animate-span">8</span>\
                       <span class="mt-number-animate-span">9</span>\
                       <span class="mt-number-animate-span">.</span>\
                     </div>';
        //数字处理
        var numToArr = function (num) {
          num = parseFloat(num).toFixed(setting.dot);
          if (typeof(num) == 'number') {
            var arrStr = num.toString().split("");
          } else {
            var arrStr = num.split("");
          }

          return arrStr;
        }

        //设置DOM symbol:分割符号
        var setNumDom = function (arrStr) {
          var shtml = '<div class="mt-number-animate">';
          for (var i = 0, len = arrStr.length; i < len; i++) {
            if (i != 0 && (len - i) % 3 == 0 && setting.symbol != "" && arrStr[i] != ".") {
              shtml += '<div class="mt-number-animate-dot">' + setting.symbol + '</div>' + nHtml.replace("{{num}}", arrStr[i]);
            } else {
              shtml += nHtml.replace("{{num}}", arrStr[i]);
            }
          }
          shtml += '</div>';
          return shtml;
        }

        //执行动画
        var runAnimate = function ($parent) {
          $parent.find(".mt-number-animate-dom").each(function () {
            var num = $(this).attr("data-num");
            num = (num == "." ? 10 : num);
            var spanHei = $(this).height() / 11; //11为元素个数
            var thisTop = -1 - num * spanHei + "px"; //如果 -num*spanHei == 0,动画不会加载，表现为不会从9跳回0，所以让其最大为-1 ~~~LingXiaoSu
            if (thisTop != $(this).css("top")) {
              if (setting.iniAnimate) {
                //HTML5不支持
                if (!window.applicationCache) {
                  $(this).animate({
                    top: thisTop
                  }, setting.speed);
                } else {
                  $(this).css({
                    'transform': 'translateY(' + thisTop + ')',
                    '-ms-transform': 'translateY(' + thisTop + ')', /* IE 9 */
                    '-moz-transform': 'translateY(' + thisTop + ')', /* Firefox */
                    '-webkit-transform': 'translateY(' + thisTop + ')', /* Safari 和 Chrome */
                    '-o-transform': 'translateY(' + thisTop + ')',
                    '-ms-transition': setting.speed / 1000 + 's',
                    '-moz-transition': setting.speed / 1000 + 's',
                    '-webkit-transition': setting.speed / 1000 + 's',
                    '-o-transition': setting.speed / 1000 + 's',
                    'transition': setting.speed / 1000 + 's'
                  });
                }
              } else {
                setting.iniAnimate = true;
                $(this).css({
                  top: thisTop
                });
              }
            }
          });
        };

        //初始化
        var init = function ($parent) {
          //初始化
          $parent.html(setNumDom(numToArr(setting.num)));
          runAnimate($parent);
        };

        //重置参数
        this.resetData = function (num) {
          var newArr = numToArr(num);
          var $dom = $(className).find(".mt-number-animate-dom");
          if ($dom.length < newArr.length) {
            $(className).html(setNumDom(numToArr(num)));
          } else {
            $dom.each(function (index, el) {
              $(this).attr("data-num", newArr[index]);
            });
          }
          runAnimate($(className));
        }

        //init
        init($(className));
        return this;
      },
    },
    beforeDestroy() {
      clearInterval(this.intervalBlock)
    }
  }
</script>

<style scoped>
  .div-run-status {
    border-radius: 0.25rem;
    padding: 15px;
  }

  label {
    font-size: 16px;
  }

  .view-go-to {
    color: #afacac;
    margin-left: 6px;
  }

  .col-click,
  .col-no-click-fix{
    padding-top: 15px;
  }

  .col-click:hover {
    cursor: pointer;
    color: #32A4BE;
  }

  .col-click:hover .run-status-p > span {
    border-bottom: 2px solid #32A4BE;
  }

  .col-click:hover > .run-status-label {
    color: #32A4BE;
  }

  .col-click:hover > .view-go-to {
    color: #32A4BE;
  }

  .col-click:hover > .d-block {
    color: #32A4BE;
  }

  .run-status-label {
    margin-top: 10px;
    margin-bottom: 0px;
    color: rgba(89,87,87,1);
    font-weight:400;
  }

  .run-status-p {
    margin-top: 0px;
    margin-bottom: 0px;
    color: #595757;
  }

  .chart-margin-top {
    margin-top: 30px;
  }

  .div-run-status {
    margin-bottom: 0px;
  }

  /*数字滚动插件的CSS可调整样式*/
  .mt-number-animate {
    font-family: '微软雅黑', sans-serif;
    width: 140px;
    line-height: 40px;
    height: 40px; /*设置数字显示高度*/;
    font-size: 30px; /*设置数字大小*/
    overflow: hidden;
    display: inline-block;
    position: relative;
  }

  .mt-number-animate .mt-number-animate-dot {
    width: 15px; /*设置分割符宽度*/
    line-height: 40px;
    float: left;
    text-align: center;
  }

  .mt-number-animate .mt-number-animate-dom {
    width: 20px; /*设置单个数字宽度*/
    text-align: center;
    float: left;
    position: relative;
    top: 0;
  }

  .mt-number-animate .mt-number-animate-dom .mt-number-animate-span {
    width: 100%;
    float: left;
    color: #b8b8c2;
  }
</style>
