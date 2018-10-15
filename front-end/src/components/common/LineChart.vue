<script>
  import {Line} from 'vue-chartjs'

  export default {
    name: "LineChart",
    extends: Line,
    props: ['title', 'data', 'options'],
    mounted() {
      this.renderChart({
        labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
        datasets: [
          {
            label: 'tx counts',
            // label: this.title,
            data: [40, 20, 12, 39, 10, 40, 39, 80, 40, 20, 12, 11],
            // data: this.data,
            borderColor: '#36a3bc',
            borderWidth: 1,
            backgroundColor: 'transparent', //线性图的渐变颜色
            pointBackgroundColor: '#000', //x轴 Y轴对应圆点的颜色
            pointBorderColor: 'white', //x轴 Y轴对应圆点的圆点border的颜色
            fontSize: 12, //字体的大小
            radius: '3' //圆点的半径
          }, {
            label: 'address counts',
            // label: this.title,
            data: [1, 6, 12, 42, 10, 34, 14, 5, 40, 4, 12, 40],
            // data: this.data,
            borderColor: '#a7fe35',
            borderWidth: 1,
            backgroundColor: 'transparent', //线性图的渐变颜色
            pointBackgroundColor: '#32A4BE', //x轴 Y轴对应圆点的颜色
            pointBorderColor: 'white', //x轴 Y轴对应圆点的圆点border的颜色
            fontSize: 12, //字体的大小
            radius: '3' //圆点的半径
          }
        ]
      }, {
        responsive: true, //长宽100%
        maintainAspectRatio: false, //保持长宽比
        // events: ["mousemove", "mouseout", "touchstart", "touchmove", "touchend"], //对事件的反应，去掉了click
        scales: {
          yAxes: [{ //对Y轴进行配置
            gridLines: { //Y轴网格配置
              // display: false, //如果为false，则不显示该轴的网格线。
            }
          }],
          xAxes: [{ //对X轴进行配置
            gridLines: { //X轴网格配置
              display: false, //如果为false，则不显示该轴的网格线。
            }
          }]
        }
      })
    }
  }
</script>
