<template>
  <div class="container margin-top-15">
    <list-title :name="$t('statistics.name')"></list-title>

    <!--line-chart models-->
    <div class="container">
      <div class="row">
        <div class="col-xl-6 col-lg-6 col-md-12 col-sm-12" v-for="item in data.data">
          <line-chart class="line-chart-style"
            :labels="data.labels"
            :label="$t('statistics.' + item.label)"
            :data="item.list"
            options="">
          </line-chart>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
  import {mapState} from 'vuex'

  export default {
		name: "StatisticsTable",
    created() {
      this.getTableData()
    },
    watch: {
      '$route': 'getTableData'
    },
    computed: {
      ...mapState({
        data: state => state.Statistics.StatisticsData
      }),
    },
    methods: {
      getTableData() {
        this.$store.dispatch('getStatisticsData', this.$route.params).then()
      }
    }
	}
</script>

<style scoped>
  .line-chart-style {
    width: 100%;
    height: 240px;
    margin: 30px 0;
    background-color: white;
  }
</style>
