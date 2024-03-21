/**
 * 是否到底部了
 *
 * @author
 * @return {*}
 */
export const isScrollBottom = (): boolean => {
  const scrollRef = document.querySelector(
    "#messgaeScroll .el-scrollbar__wrap"
  );
  if (!scrollRef) {
    return false;
  }
  const { clientHeight, scrollTop, scrollHeight } = scrollRef;

  // 存在小数直接进一
  return Math.ceil(scrollTop + clientHeight) >= scrollHeight;
};

/**
 * 安全区域，继续聊天
 *
 * @author
 * @return {*}
 */
export const isSafeRegion = (): boolean => {
  const scrollRef = document.querySelector(
    "#messgaeScroll .el-scrollbar__wrap"
  );
  if (!scrollRef) {
    return false;
  }
  const { clientHeight, scrollTop, scrollHeight } = scrollRef;

  // 存在小数直接进一
  return (
    Math.ceil(scrollTop + clientHeight) + window.screen.height / 3 >=
    scrollHeight
  );
};

/**
 * 改变滚动条位置
 *
 * @author
 * @return {*}
 */
export const changeScollTopForFetchFinished = (mode, offsetId: string) => {
  // const scrollRef = document.querySelector(
  //   "#messgaeScroll .el-scrollbar__wrap"
  // );
  // if (!scrollRef) {
  //   return false;
  // }
  // const { clientHeight, scrollHeight } = scrollRef;

  // 往上滚需要重新定位，否则scrollTop会变为0
  if (mode === "up") {
    document.getElementsByClassName(offsetId)?.[0]?.scrollIntoView({
      block: "start"
    });
  }
  // else {
  // scrollRef.scrollTo({
  //   top: Math.ceil(scrollHeight - clientHeight - 31)
  // });
  // }
};
