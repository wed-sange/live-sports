<template>
  <div ref="appRef" class="text-[#F5F5F5] text-[12px] bg-white h-full main-page">
    <router-view v-slot="{ Component, route }">
      <transition :name="route.meta.transitionName || 'page-normal'">
        <keep-alive :max="120" :include="appStore.routerPaths">
          <component :is="Component" :key="route.fullPath" />
        </keep-alive>
      </transition>

      <!-- <transition :name="route.meta.transitionName || 'page-normal'">
        <component v-if="!route.meta.keepAlive" :is="Component" :key="route.fullPath" />
      </transition> -->
    </router-view>
    <van-tabbar
      active-color="#37373D"
      inactive-color="#AEB4BA"
      safe-area-inset-bottom
      :border="false"
      :fixed="true"
      :swipe-threshold="4"
      route
      v-if="showTab"
      class="bottom-bar"
      v-show="!showCalendar"
    >
      <van-tabbar-item
        :class="`flex-shrink-0 click-style ${index == 3 ? 'mr-[84px]' : ''} ${index == 0 ? 'ml-[84px]' : ''}  ${
          index < 3 ? 'mr-[130px]' : ''
        }`"
        v-for="(item, index) in state.tarContext.items"
        :key="item.key"
        replace
        :to="item.path"
        @click="onTabClick(index)"
      >
        <template #icon="props">
          <div :class="`${props.active ? item.activeClass : item.defaultClass} tab-icon `"></div>
        </template>
        <template v-if="item.key === 'message' && readDotAll">
          <div class="message-badge" :class="{ large: readDotAll > 9 }">
            <i>{{ readDotAll > 99 ? '99+' : readDotAll }}</i>
          </div>
        </template>
        <span class=""> {{ item.name }}</span>
      </van-tabbar-item>
    </van-tabbar>
  </div>
  <div v-show="shouldRotate" class="w-screen z-[11] h-screen text-white bg-black items-center flex flex-col fixed top-0 left-0">
    <div class="flex flex-col justify-center items-center" v-show="showRoToast">
      <span class="rotate-icon mt-[40px] ml-[30px] mb-[20px]"></span>
      <span>为了您的良好体验</span>
      <span>请将手机/平板竖屏操作</span>
    </div>
  </div>
  <LiveStartNotify />
</template>

<script lang="ts" setup name="App">
  import { useRoute } from 'vue-router';
  import lottie from 'lottie-web';
  import { useI18n } from 'vue-i18n';

  import { useMessageStore } from '@/store/modules/message';
  import { useMatchStore } from '@/store/modules/match';
  import { loadResources } from './utils/loadStaticUtils';
  import { useAppStore } from '@/store/modules/app';

  import { useImmersive, judgeIosBar } from './utils/deviceAbnormal';
  import { initTouches } from './utils/touchUtils';
  import { onClosePage } from './utils/closeStartPage';
  import { isIos } from './utils/common';

  //动画json
  import homeJson from '@/assets/animation/home.json';
  import matchJson from '@/assets/animation/match.json';
  import messageJson from '@/assets/animation/massage.json';
  import mineJson from '@/assets/animation/mine.json';

  //直播中通知导航条
  import LiveStartNotify from '@/components/LiveStartNotify/index.vue';
  // import eruda from 'eruda';

  //常用的动画css
  import '@/assets/animation.scss';

  const animationRef = ref();
  const i18n = useI18n();
  const appStore = useAppStore();
  const matchStore = useMatchStore();
  const messageStore = useMessageStore();
  const readDotAll = computed(() => messageStore.readDotAll);
  //展示选择日历隐藏tab
  const showCalendar = computed(() => matchStore.showCalendar);
  const routeItem = useRoute();
  //是否旋转
  const shouldRotate = ref(false);
  //显示旋转提示
  const showRoToast = ref(false);
  const appRef = ref();
  let timeout;
  let timeout2;
  const state = reactive({
    jsonList: [homeJson, matchJson, messageJson, mineJson],
    tarContext: {
      items: [
        {
          key: 'home',
          path: '/home',
          name: '首页',
          activeClass: 'home-active',
          defaultClass: 'home-icon',
          //  activeImg: homeActiveIcon, img: homeIcon
        },
        {
          key: 'match',
          path: '/match',
          name: '赛程',
          activeClass: 'match-active',
          defaultClass: 'match-icon',
          //  activeImg: matchActiveIcon, img: matchIcon
        },
        {
          key: 'message',
          path: '/message',
          name: '消息',
          activeClass: 'message-active',
          defaultClass: 'message-icon',
          //  activeImg: messageActiveIcon, img: messageIcon
        },
        {
          key: 'user',
          path: '/user',
          name: '我的',
          activeClass: 'mine-active',
          defaultClass: 'mine-icon',
          //  activeImg: mineActiveIcon, img: mineIcon
        },
      ],
    },
  });
  //有底部菜单的路由页面
  const showTab = computed(() => {
    return state.tarContext.items.find((item) => item.path == routeItem.path) && !shouldRotate.value;
  });
  onMounted(() => {
    //加载项目里甲方确定的静态资源
    initLoadResources();
    //初始化手势事件
    initTouches();
    //原生壳包获取头部距离
    useImmersive();
    //初始化底部菜单动画
    initTabLotty();
    //监听屏幕旋转，横屏直接显示一个蒙层不让点击使用
    initOnResize();
    //判断关闭启动页
    initCloseStartPage();
    //调用
    console.log('18xxx', i18n.t('tab.home'));
  });

  //底部菜单点击
  const onTabClick = (index) => {
    if (timeout) {
      clearTimeout(timeout);
      timeout = null;
      animationRef.value && animationRef.value.destroy();
      animationRef.value = null;
    }
    timeout = setTimeout(() => {
      if (timeout2) {
        clearTimeout(timeout2);
        timeout2 = null;
        animationRef.value && animationRef.value.destroy();
        animationRef.value = null;
      }
      nextTick(() => {
        const { activeClass } = state.tarContext.items[index];
        const className = '.' + activeClass;
        animationRef.value = lottie.loadAnimation({
          container: document.querySelector(className)!, // 需要渲染动画的dom元素
          animationData: state.jsonList[index], // 动画文件
          renderer: 'svg', // 渲染方式
          autoplay: true, // 是否自动播放
          loop: false,
          name: 'tabLoading', // 动画参考名称
        });
        timeout2 = setTimeout(
          () => {
            animationRef.value && animationRef.value.destroy();
          },
          index ? 300 : 800,
        );
      });
    }, 200);
  };
  const initCloseStartPage = () => {
    const hashStr = window.location.hash;
    //首页单独处理接口加载逻辑关闭启动页
    if (hashStr.indexOf('#/home') != -1) {
      return;
    }
    onClosePage();
  };
  const initLoadResources = () => {
    loadResources().then(() => {
      if (isIos()) {
        judgeIosBar();
      }
      appStore.loadStaticResource = true;
    });
  };
  const initOnResize = () => {
    screen.orientation.addEventListener('change', (event) => {
      if (!event || !event.target) {
        return;
      }
      const { angle } = event.target as any;
      shouldRotate.value = angle == 90 || angle == -90;
      if (!appRef.value) {
        return;
      }
      if (angle == 90 || angle == -90) {
        appRef.value.style.display = 'none';
        showRoToast.value = true;
      } else {
        appRef.value.style.display = 'block';
        showRoToast.value = false;
      }
    });
    //通用input弹起键盘滚动到底部
    window.addEventListener('resize', function () {
      if (!document.activeElement) {
        return;
      }
      if (document.activeElement.tagName === 'INPUT' || document.activeElement.tagName === 'TEXTAREA') {
        window.setTimeout(function () {
          if (!document.activeElement) {
            return;
          }
          const suggestObj: any = ref({});
          suggestObj.value = document.activeElement;
          if ('scrollIntoView' in document.activeElement) {
            suggestObj.value.scrollIntoView(false);
          } else {
            suggestObj.value.scrollIntoViewIfNeeded(false);
          }
        }, 0);
      }
    });
  };
  const initTabLotty = () => {
    const list = state.tarContext.items;
    let index = 0;
    const queryDiv = (i) => {
      const item = list[i];
      const { defaultClass } = item;
      let orgClassName = '.' + defaultClass;
      const playContainer = (container, animationData) => {
        animationRef.value = lottie.loadAnimation({
          container: container!, // 需要渲染动画的dom元素
          animationData, // 动画文件
          renderer: 'svg', // 渲染方式
          autoplay: false, // 是否自动播放
          loop: false,
          name: 'tabLoading', // 动画参考名称
        });
        animationRef.value && animationRef.value.destroy();
        index++;
        if (index < list.length) {
          queryDiv(index);
        }
      };
      const onQueryElement = (className) => {
        const container = document.querySelector(className);
        if (container) {
          playContainer(container, state.jsonList[i]);
        } else {
          className = className.replace('-icon', '-active');
          const container = document.querySelector(className);
          if (!container) {
            setTimeout(() => {
              onQueryElement(orgClassName);
            }, 10);
            return;
          }
          playContainer(container, state.jsonList[i]);
        }
      };
      onQueryElement(orgClassName);
    };
    queryDiv(index);
  };
  let currentBg = '';
  //设置背景直播间里面是黑色，别的页面是白色
  watch(
    routeItem,
    () => {
      let setBg = '#fff';
      if (routeItem.path === '/roomDetail') {
        setBg = '#181819';
      } else {
        setBg = '#fff';
      }
      if (currentBg !== setBg) {
        currentBg = setBg;
        document.body.style.background = currentBg;
      }
    },
    { immediate: true },
  );
</script>

<style lang="scss" scoped>
  .main-page {
    -webkit-overflow-scrolling: touch;
    -webkit-text-size-adjust: none;
    font-family: PingFang SC;
  }

  .tab-icon {
    margin-top: 8px;
    width: 48px;
    height: 48px;
    margin-bottom: 2px;
  }
  .home-active {
    background: url('./assets/img/tab/tab-home-active.png') no-repeat center;
    background-size: contain;
  }
  .home-icon {
    background: url('./assets/img/tab/tab-home.png') no-repeat center;
    background-size: contain;
  }
  .match-active {
    background: url('./assets/img/tab/tab-match-active.png') no-repeat center;
    background-size: contain;
  }
  .match-icon {
    background: url('./assets/img/tab/tab-match.png') no-repeat center;
    background-size: contain;
  }
  .message-active {
    background: url('./assets/img/tab/tab-message-active.png') no-repeat center;
    background-size: contain;
  }
  .message-icon {
    background: url('./assets/img/tab/tab-message.png') no-repeat center;
    background-size: contain;
  }
  .mine-active {
    background: url('./assets/img/tab/tab-mine-active.png') no-repeat center;
    background-size: contain;
  }
  .mine-icon {
    background: url('./assets/img/tab/tab-mine.png') no-repeat center;
    background-size: contain;
  }

  .rotate-icon {
    width: 100px;
    height: 100px;
    background-repeat: no-repeat;
    background-size: contain;
    background-image: url('data:image/png;base64,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');
  }

  .bottom-bar {
    display: flex;
    align-items: center;
    height: 112px;
    background: white;
    border-top: 2px solid #f4f4f4;
  }

  .main-page {
    .immersive & {
      .van-tabbar {
        :deep(.van-tabbar-item__text) {
          min-width: 50px;
        }
      }
    }
  }

  .van-tabbar {
    z-index: 1;
    :deep(.van-tabbar-item__text) {
      font-size: 20px;
      font-weight: 400;
    }
    :deep(.van-tabbar-item--active) {
      .van-tabbar-item__text {
        font-weight: 500;
        font-size: 20px;
      }
    }
  }
  :deep(.van-tabbar-item__icon) {
    margin-bottom: 7px;
  }
  .van-tabbar-item {
    display: flex;
    justify-content: center;
    background: #fff;
    height: 97px;
    padding-bottom: 11px;
    position: relative;
    // min-width: 50px;
  }

  .message-badge {
    position: absolute;
    top: 0;
    left: 30px;
    display: flex;
    width: 29px;
    height: 29px;
    border-radius: 200px;
    overflow: hidden;
    text-align: center;
    justify-content: center;
    align-items: center;
    font: 400 20px / normal 'MiSans';
    color: #fff;
    background: #ff5e2a;
    z-index: 4;
    pointer-events: none;
    border: 2px solid #fff;
    vertical-align: middle;
    &.large {
      width: auto;
      padding: 0 8px;
    }
    & > i {
      display: inline-block;
      font-style: normal;
    }
  }
</style>
