import dayjs from 'dayjs';

export function formateNum(num) {
  return num > 9999 ? '9999+' : num;
}
export function formateDate(mils, format) {
  return dayjs(mils).format(format);
}
// 根据数组长度返回评论条数
export function currencyNum(num) {
  if (num > 0 && num < 1000) {
    return num;
  } else if (num > 1000 && num < 10000) {
    return (Math.floor(num) / 1000).toFixed(0) + '千';
  } else if (num > 10000) {
    return (Math.floor(num) / 10000).toFixed(0) + '万';
  }
}

export function getCustomerId() {
  let customerId = localStorage.getItem('customerId');
  if (!customerId) {
    customerId = Math.round(Math.random() * 18000 + new Date().getTime()) + '';
    localStorage.setItem('customerId', customerId);
  }
  return customerId;
}
//是否是iOS设备
export function isIos() {
  const ua = window.navigator.userAgent.toLocaleLowerCase();
  const isIOS = /iphone|ipad|ipod/.test(ua);
  return isIOS ? true : false;
}
//获取hash  key-value
export function getHashParam(name) {
  const pageInfo = window.location.hash.split('?');
  if (pageInfo.length > 1) {
    const res = {};
    const search = pageInfo[1];
    const strList = search.split('&');
    for (let i = 0; i < strList.length; i++) {
      res[strList[i].split('=')[0]] = decodeURIComponent(strList[i].split('=')[1]);
    }
    return res[name];
  } else {
    return null;
  }
}
