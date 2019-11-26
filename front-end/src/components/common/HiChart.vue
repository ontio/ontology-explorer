<template>
    <highcharts class="chart" :options="chartOptions"></highcharts>
</template>
<script>
    export default {

        props: ['label','labels','data'],
        mounted(){
                if(typeof(this.data[0]) == "string"){
                    for(var i=0;i<this.data.length;i++){
                        /* this.data[i] = parseFloat(this.data[i]) */
                    this.$set(this.data,i,parseFloat(this.data[i]))
                    }
                    this.data.reverse.reverse
                    console.log(this.data)
                }
                this.$set(this.chartOptions.series,0,[{name:'',data:this.data,color:'#6fcd98'}])
        },
        data() {
            return {
                chartOptions: {
                    chart: {
                        type: 'area',
                        height:240
                    },
                    title: {
                        text: this.label
                    },
                    credits: {
                        enabled: false
                    },
                    xAxis: {
			            type: 'datetime',
                        categories: this.labels,
                        tickLength:0,
                        labels:{
                            rotation:0,
                            style:{
                                'font-size':'10px'
                            }
                        }
                    },
                    yAxis: {
                        title: {
                            text: ''
                        },
                        gridLineWidth:1,
                    },
                    legend:{
                        enabled: false
                    },
                    plotOptions: {
                        area: {
                            fillColor: {
                                linearGradient: {
                                    x1: 0,
                                    y1: 0,
                                    x2: 0,
                                    y2: 1
                                },
                                stops: [
                                    [0, 'rgba(54, 163 ,188, 0.5)'],
                                    [0.5, 'rgba(54, 163, 188, 0.25)'],
                                    [1, 'rgba(54, 163, 188, 0)']
                                ]
                            },
                            marker: {
                                radius: 2
                            },
                            lineWidth: 1,
                            states: {
                                hover: {
                                    lineWidth: 1
                                }
                            },
                            threshold: null
                        }
                    },
                    series: [{
                        name: this.label,
                        data: this.data,
                    }]
                },
                newChartOptions:{}
            }
        },
    }
</script>