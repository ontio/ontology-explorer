/**
 * 一些工具函数，自定义名称为：$HelperTools。
 * 在main.js import和use可全局使用。
 */
import DecimalMath from 'decimal.js';

const HelperTools = {
  getDateTime(inputTime) {
    let date = new Date(inputTime * 1000);
    let NowTime = new Date();
    let showtime = (NowTime - date) / 1000;

    return parseInt(showtime);
  },

  getShowDate(inputTime) {
    if (inputTime <= 60) {
      return inputTime + "s"
    }
    if (60 < inputTime && inputTime <= 3600) {
      return parseInt(inputTime / 60) + "m"
    }
    if (3600 < inputTime && inputTime <= 86400) {
      return parseInt(inputTime / 3600) + "h"
    }
    if (inputTime > 86400) {
      return parseInt(inputTime / 86400) + "d"
    }
  },

  getTransDate(inputTime) {
    inputTime = inputTime * 1000;

    if (window.localStorage.getItem('user_lang') === 'zh') {
      return this.getPRCTime(inputTime)
    } else {
      return this.getUTCTime(inputTime)
    }
  },

  /**
   * 获取UTC标准时间
   *
   * @param inputTime
   * @return {string}
   */
  getUTCTime(inputTime) {
    let date = new Date(inputTime);
    let Y = date.getUTCFullYear();
    let M = (date.getUTCMonth() + 1 < 10 ? '0' + (date.getUTCMonth() + 1) : date.getUTCMonth() + 1);
    let mouth = '';

    switch (M.toString()) {
      case "01":
        mouth = 'Jan-';
        break;
      case "02":
        mouth = 'Feb-';
        break;
      case "03":
        mouth = 'Mar-';
        break;
      case "04":
        mouth = 'Apr-';
        break;
      case "05":
        mouth = 'May-';
        break;
      case "06":
        mouth = 'Jun-';
        break;
      case "07":
        mouth = 'Jul-';
        break;
      case "08":
        mouth = 'Aug-';
        break;
      case "09":
        mouth = 'Sep-';
        break;
      case "10":
        mouth = 'Oct-';
        break;
      case "11":
        mouth = 'Nov-';
        break;
      case "12":
        mouth = 'Dec-';
        break;
      default:
        break;
    }

    let D = date.getUTCDate() < 10 ? '0' + date.getUTCDate() + '-' : date.getUTCDate() + '-';
    let h = date.getUTCHours() < 10 ? '0' + date.getUTCHours() : date.getUTCHours();
    let m = date.getUTCMinutes() < 10 ? '0' + date.getUTCMinutes() : date.getUTCMinutes();
    let s = date.getUTCSeconds() < 10 ? '0' + date.getUTCSeconds() : date.getUTCSeconds();

    return mouth + D + Y + ' ' + h + ':' + m + ':' + s + " UTC";
  },

  /**
   * 获取PRC地区的时间
   *
   * @param inputTime
   * @return {string}
   */
  getPRCTime(inputTime) {
    let date = new Date(inputTime);
    let y = date.getFullYear();
    let m = date.getMonth() + 1;
    m = m < 10 ? ('0' + m) : m;
    let d = date.getDate();
    d = d < 10 ? ('0' + d) : d;
    let h = date.getHours();
    h = h < 10 ? ('0' + h) : h;
    let minute = date.getMinutes();
    let second = date.getSeconds();
    minute = minute < 10 ? ('0' + minute) : minute;
    second = second < 10 ? ('0' + second) : second;
    return y + '-' + m + '-' + d + ' ' + h + ':' + minute + ':' + second;
  },

  getDayFunc(second_time) {
    let time = parseInt(second_time);

    if (parseInt(second_time) > 60) {
      let second = parseInt(second_time) % 60;
      let min = parseInt(second_time / 60);
      time = min + ":" + second;

      if (min > 60) {
        min = parseInt(second_time / 60) % 60;
        let hour = parseInt(parseInt(second_time / 60) / 60);
        time = hour + ":" + min + ":" + second;

        if (hour > 24) {
          hour = parseInt(parseInt(second_time / 60) / 60) % 24;
          let day = parseInt(parseInt(parseInt(second_time / 60) / 60) / 24);
          time = day + "day" + hour + ":" + min + ":" + second;
        }
      }
    }
    return time;
  },

  /**
   * 金融化数值显示
   *
   * @param value
   * @return {*}
   */
  toFinancialVal(value) {
    if (typeof(value) !== 'undefined' && !isNaN(value)) {
      value = new DecimalMath(value).toString();

      if (value === '0') return value;

      let dotIndex = value.indexOf('.');
      let valueLast = '';

      // 没有小数点的处理
      if (dotIndex !== -1) {
        valueLast = value.substr(dotIndex);
        value = value.substr(0, dotIndex);
      }

      let tmpVal = value.split('').reverse();
      let retStr = '';

      for (let i = 0; i < tmpVal.length; i++) {
        if ((i + 1) % 4 === 0) {
          tmpVal.splice(i, 0, ',');
        }
      }
      tmpVal.reverse();

      for (let i = 0; i < tmpVal.length; i++) {
        retStr += tmpVal[i]
      }

      return retStr + valueLast;
    } else {
      return 0
    }
  },

  /**
   * 处理string to json
   *
   * @param data
   * @return {any}
   */
  strToJson(data) {
    if (typeof(data) !== 'object' && typeof(data) !== 'undefined' && data !== null && data !== '') {
      if (data.substr(0, 1) === '"') {
        data = data.substr(1, data.length() - 1)
      }

      try {
        data = JSON.parse(data);
      } catch (e) {

      }
    }

    return data
  }
};

export default {
  install: function (Vue) {
    Vue.prototype.$HelperTools = HelperTools
  },
  HelperTools
}
