<template>
  <div class="text-[26px] pb-[52px] text-[#f5f5f5] mt-[24px] bs-lineup">
    <van-tabs
      background="transparent"
      title-active-color="#37373D"
      title-inactive-color="#94999F"
      shrink
      animated
      line-height="0"
      line-width="0"
      swipeable
      v-model:active="active"
    >
      <van-tab v-for="(tabItem, tabIndex) in tabList" :key="tabItem.title" :title="tabItem.title">
        <template #title>
          <span :class="`${tabIndex == 0 ? 'ml-[20px]' : ''}  line-clamp-1 max-w-[220px]`">{{ tabItem.title }}</span>
        </template>
        <component :is="tabItem.component" :list="tabItem.list" />
      </van-tab>
    </van-tabs>
  </div>
</template>

<script setup>
  import { onMounted } from 'vue';
  import { createLoading, closeLoading } from '@/components/SysLoading';
  import apis from '@/api/room';
  import BsLineUpTable from './BsLineUpTable.vue';
  const active = ref(1);
  const tabList = ref([]);

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

  onMounted(async () => {
    createLoading({ overlay: false });
    getLineupData();
  });

  const getLineupData = () => {
    const { matchId } = props;
    apis
      .getBasketballLineup(matchId)
      .then((res) => {
        const { home, away } = res;
        home.sort((a, b) => {
          return b.data.score - a.data.score;
        });
        away.sort((a, b) => {
          return b.data.score - a.data.score;
        });
        active.value = props.matchName.awayName;
        tabList.value.push({ title: props.matchName.awayName, list: away, component: shallowRef(BsLineUpTable) });
        tabList.value.push({ title: props.matchName.homeName, list: home, component: shallowRef(BsLineUpTable) });
        closeLoading();
      })
      .catch(() => {
        closeLoading();
      });
  };
</script>
<style scoped lang="scss">
  .bs-lineup {
    font-family: PingFang SC;
    line-height: normal;
    font-weight: 400;
    :deep().van-tab {
      justify-content: baseline;
      align-items: baseline;
      width: 240px;
      height: 60px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-top: 12px;
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
    }
    :deep().van-tab--active {
      font-weight: 400;
      line-height: normal;
      border-radius: 48px;
      background: #323235;
      width: 240px;
      height: 60px;
      margin-left: 4px;
      display: flex;
      align-items: center;
      justify-content: center;
      .van-tab__text {
        font-size: 26px;
        line-height: normal;
        font-weight: 500;
        font-family: 'PingFang SC';
        color: #fff;
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
      border: none;
      width: 516px;
      align-self: center;
      padding: 0;
      flex-shrink: 0;
    }
    :deep().van-tabs__nav--complete {
      padding: 0 0 0 0;
      display: flex;
      justify-content: space-around;
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
