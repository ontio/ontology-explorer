<script>
  import {Line} from 'vue-chartjs'

  export default {
    name: "LineChart",
    extends: Line,
    props: ['labels', 'label', 'data', 'options'],
    mounted() {
      // 使用本体蓝做渐变色
      this.gradient = this.$refs.canvas.getContext('2d').createLinearGradient(0, 0, 0, 450);
      this.gradient.addColorStop(0, 'rgba(54, 163 ,188, 0.5)');
      this.gradient.addColorStop(0.5, 'rgba(54, 163, 188, 0.25)');
      this.gradient.addColorStop(1, 'rgba(54, 163, 188, 0)');

      this.renderChart({
        labels: this.labels,
        datasets: [
          {
            label: this.label,
            data: this.data,
            // borderColor: '#36a3bc',
            borderWidth: 1,
            backgroundColor: this.gradient, //线性图的渐变颜色
            pointBackgroundColor: '#36a3bc', //x轴 Y轴对应圆点的颜色
            pointBorderColor: 'white', //x轴 Y轴对应圆点的圆点border的颜色
            fontSize: 12, //字体的大小
            radius: '2' //圆点的半径
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
            },
            ticks: {
              beginAtZero: true,
              userCallback: function (label, index, labels) {
                // when the floored value is the same as the value we have a whole number
                if (labels[0] > 1000 && Math.floor(label) === label) {
                  return label / 1000 + 'k';
                } else {
                  return label
                }
              }
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
