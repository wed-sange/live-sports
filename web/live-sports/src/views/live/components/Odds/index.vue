<template>
  <div class="mt-[24px] odds-page">
    <van-tabs
      background="transparent"
      title-active-color="#37373D"
      title-inactive-color="#94999F"
      shrink
      animated
      line-height="3px"
      line-width="23px"
      swipeable
      v-model:active="active"
    >
      <van-tab v-for="(item, index) in tabList" :key="item.title">
        <template #title>
          <span :class="`${index == 0 ? '' : ''} `">{{ item.title }}</span>
        </template>
        <keep-alive>
          <component :thTitle="item.thTitle" :is="item.component" :data="item.list" />
        </keep-alive>
      </van-tab>
    </van-tabs>
  </div>
</template>

<script setup>
  import { onMounted, shallowRef } from 'vue';
  import apis from '@/api/room';
  import { createLoading, closeLoading } from '@/components/SysLoading';
  import { useAppStore } from '@/store/modules/app';

  const appStore = useAppStore();
  const JQS = defineAsyncComponent(() => import('./JQSTable.vue'));
  const SPF = defineAsyncComponent(() => import('./SPFTable.vue'));
  const active = ref('');
  const tabList = ref([]);
  let timer;

  const props = defineProps({
    matchId: {
      type: String,
    },
    matchType: {
      type: String,
    },
    matchName: {
      type: Object,
    },
  });

  const titls = {
    euInfo: { title: '胜平负', item: shallowRef(SPF), thTitle: '即盘' },
    asiaInfo: { title: '让球', item: shallowRef(JQS), thTitle: '即盘' },
    bsInfo: { title: '进球数', item: shallowRef(JQS), thTitle: '即盘' },
  };

  onMounted(async () => {
    createLoading({ overlay: false });
    getOdds(false);
    onStartTimeOut();
  });
  onDeactivated(() => {
    if (appStore.clearRoom) {
      onRemoveTimer();
    }
  });
  const onStartTimeOut = () => {
    onRemoveTimer();
    timer = setTimeout(() => {
      getOdds(true);
      onStartTimeOut();
    }, 20000);
  };
  onUnmounted(() => {
    onRemoveTimer();
  });
  const onRemoveTimer = () => {
    if (timer) {
      clearTimeout(timer);
      timer = null;
    }
  };
  const getOdds = (isReload) => {
    const { matchId } = props;
    apis
      .getFootOdds(matchId)
      .then((res) => {
        if (res) {
          Object.keys(res).map((key) => {
            if (!titls[key]) {
              delete res[key];
            }
          });
          if (!isReload) {
            active.value = Object.keys(res)[0];
            if (Object.keys(res).length != 0) {
              Object.keys(res).forEach((key) => {
                tabList.value.push({ key, title: titls[key].title, component: titls[key].item, list: res[key] });
              });
            }
          } else {
            if (Object.keys(res).length != 0) {
              Object.keys(res).forEach((key) => {
                const index = tabList.value.findIndex((item) => item.key == key);
                if (index != -1) {
                  tabList.value[index].list = res[key];
                } else {
                  tabList.value.push({ title: titls[key].title, component: titls[key].item, list: res[key] });
                }
              });
            }
          }
        }
        closeLoading();
      })
      .catch(() => {
        closeLoading();
      });
  };
</script>
<style scoped lang="scss">
  .odds-page {
    font-family: PingFang SC;
    line-height: normal;
    font-weight: 400;
    height: 100%;
    :deep().van-tab {
      justify-content: baseline;
      align-items: baseline;
      width: 180px;
      height: 60px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-top: 12px;
      margin-left: 12px;
    }
    :deep().van-tab:active {
      opacity: 0.2;
    }
    :deep().van-tab--shrink {
      padding: 0;
    }
    --van-padding-xs: 24px;

    :deep().van-tabs__line {
      border-radius: 80px;
      background: #34a853;
    }
    :deep().van-tab__text {
      font-size: 26px;
      line-height: 36px;
      color: #94999f;
      margin-top: 0.5px;
      margin-left: 0;
    }
    :deep().van-tab--active {
      font-weight: 400;
      line-height: normal;
      border-radius: 48px;
      background: #323235;
      width: 180px;
      height: 60px;
      display: flex;
      align-items: center;
      justify-content: center;
      .van-tab__text {
        font-size: 26px;
        line-height: normal;
        font-weight: 500;
        font-family: 'PingFang SC';
        color: #fff;
        margin-left: 0px;
        margin-top: 0;
      }
    }
    :deep().van-tabs__line {
      display: none;
    }
    :deep().van-tabs__wrap {
      height: 84px;
      background: transparent;
      border-radius: 52px;
      background: #1f1f20;
      margin-bottom: 30px;
      border: none;
      width: 588px;
      align-self: center;
      padding: 0;
      flex-shrink: 0;
    }
    :deep().van-tabs__nav--complete {
      padding: 0 0 0 0;
      display: flex;
      // justify-content: space-around;
    }
    :deep(.van-tab__panel-wrapper--inactive) {
      height: 100%;
    }
    :deep(.van-tabs) {
      height: 100%;
      display: flex;
      flex-flow: column;

      .van-tabs__content {
        flex: 1 1 auto;
        .van-tab__panel {
          height: 100%;
          padding-left: 0;
        }
      }
    }
  }
</style>
