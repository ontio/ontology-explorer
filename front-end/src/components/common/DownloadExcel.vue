<template>
  <div v-show="isShow">
    <button class="download-excel-btn" @click="downloadExcel()">
      <i class="far fa-file-excel"></i>
    </button>
    <a href="" :download="excelFileName" id="hf"></a>
  </div>
</template>

<script>
  import XLSX from 'xlsx'

	export default {
    name: "DownloadExcel",
    props: ['address'],
    data() {
      return {
        excelFileName: 'AllTxnData-' + this.address,
        isShow: true
      }
    },
    created() {
      let nowDate = new Date();
      this.excelFileName = this.excelFileName + '-' + nowDate.toLocaleDateString() + '.xlsx';
    },
    methods: {
      downloadExcel() {
        this.isShow = false;

        this.$store.dispatch('getAddressDetailAllData', this.$route.params).then(res => {
          if (res !== false) this.generatingExcelData(res)
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
      }
    }
  }
</script>

<style scoped>
  .download-excel-btn {
    font-weight: 100;
    background-color: transparent;
    float: right;
    border: 0;
    color: transparent;
  }

  .download-excel-btn:hover {
    cursor: pointer;
    color: #32A4BE;
  }
</style>
