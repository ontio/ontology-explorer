/**
 * The lang saved to the localStorage.
 */
export default {
  getDateTime(inputTime) {
    let date = new Date(inputTime * 1000);
    let Y = date.getFullYear() + '-';
    let M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    let D = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
    let h = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
    let m = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
    let s = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds();
    let NowTime = new Date()
    let showtime = (NowTime - date)/1000
    /* return Y + M + D + ' ' + h + ':' + m + ':' + s; */
    return parseInt(showtime);
  },

  getshowDate(inputTime) {
    if(inputTime <=60){
      return inputTime+"s"
    }
    if(60< inputTime &&  inputTime <=3600){
      return parseInt(inputTime/60)+"m"
    }
    if(3600 < inputTime &&  inputTime  <=86400){
      return parseInt(inputTime/3600)+"h"
    }
    if(inputTime >86400){
      return parseInt(inputTime/86400)+"d"
    }
  },

  getDate(inputTime) {
    let date = new Date(inputTime * 1000);
    var utctime = date.getUTCHours()
    /* console.log("utctime1",date.getUTCHours()+":"+date.getUTCMinutes()+":"+date.getUTCSeconds()) */
    let Y = date.getUTCFullYear() ;
    let M = (date.getUTCMonth() + 1 < 10 ? '0' + (date.getUTCMonth() + 1) : date.getUTCMonth() + 1);
    var mouth = ''
    switch(M){
      case "01":
        mouth = 'Jan-'
      break;
      case "02":
        mouth = 'Feb-'
      break;
      case "03":
        mouth = 'Mar-'
      break;
      case "04":
        mouth = 'Apr-'
      break;
      case "05":
        mouth = 'May-'
      break;
      case "06":
        mouth = 'Jun-'
      break;
      case "07":
        mouth = 'Jul-'
      break;
      case "08":
        mouth = 'Aug-'
      break;
      case "09":
        mouth = 'Sep-'
      break;
      case "10":
        mouth = 'Oct-'
      break;
      case "11":
        mouth = 'Nov-'
      break;
      case "12":
        mouth = 'Dec-'
      break;
      default:
      break;
    }
    let D = date.getUTCDate() < 10 ? '0' + date.getUTCDate() + '-': date.getUTCDate()+'-';
    let h = date.getUTCHours() < 10 ? '0' + date.getUTCHours() : date.getUTCHours();
    let m = date.getUTCMinutes() < 10 ? '0' + date.getUTCMinutes() : date.getUTCMinutes();
    let s = date.getUTCSeconds() < 10 ? '0' + date.getUTCSeconds() : date.getUTCSeconds();
    let NowTime = new Date()
    let showtime = (NowTime - date)/1000
    return mouth+D+Y +' ' + h + ':' + m + ':' + s+" UTC";
    /* return parseInt(showtime); */
  },

  getDayfunction(second_time) {
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

  getNormalgas(gas){
    let showGas = gas*0.000000001
    return showGas
  }
}
