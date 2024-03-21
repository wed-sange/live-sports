<template>
  <page-box class="flex flex-col h-full bg-[#F2F3F7] search-page">
    <template #header>
      <AppHeader />
      <div class="flex flex-col bg-white">
        <div class="flex w-full h-[114px] border-b-[#F2F3F7] border-b-[1px] text-[#f5f5f5] text-[28px]">
          <div class="flex ml-[24px] bg-[#F2F3F7] rounded-[60px] w-[608px] px-[20px] h-[76px] mt-[16px]">
            <img :src="searchIcon" class="w-[40px] h-[40px] mt-[18px] flex-shrink-0 bg-contain mr-[20px]" />
            <input
              id="myInput"
              type="search"
              enterkeyhint="search"
              @keydown.enter="onInputSearch"
              v-model="searchContent"
              placeholder="搜索感兴趣的主播"
              :autofocus="true"
              @input="onInputChange"
              :class="`w-full pt-[2px] text-[32px] bg-[#F2F3F7] rounded-tr-[60px] rounded-br-[60px] h-full text-[#37373D] opacity-${opacity}`"
            />
            <img
              @click="onClearSearch"
              v-if="searchContent"
              class="w-[40px] mt-[18px] cursor-pointer h-[40px] bg-contain click-style"
              :src="closeIcon"
            />
          </div>
          <span
            class="click-style flex pl-[30px] mt-[32px] text-[32px] font-medium text-[#34A853] cursor-pointer flex-shrink-0"
            @click="onGoBack"
          >
            取消
          </span>
        </div>
        <div class="flex items-center mt-[30px] ml-[24px]" v-if="showHot && hotList && hotList.length > 0">
          <span class="text-[30px] font-medium text-[#37373D]">热门比赛</span>
        </div>
      </div>
    </template>
    <div class="flex flex-col pt-[30px] px-[26px] bg-white max-h-[80vh] overflow-y-scroll" v-if="showHot && hotList && hotList.length > 0">
      <div @click="onSelectHot(item)" class="flex items-center click-style" v-for="(item, index) in hotList" :key="index">
        <span class="text-[28px] mb-[40px] font-normal text-[#575762] flex items-center">
          <span :class="`mr-[19px] default-text color-${index}`">{{ index + 1 }}</span>
          {{ item.competitionName }}
          {{ item.homeName }}
          <span class="font-medium mx-[8px]"> VS</span>
          <span class="mr-[16px]">{{ item.awayName }}</span>
          <img v-if="item.newest || item.hottest" class="w-[36px] h-[36px] bg-contain" :src="item.hottest ? hotIcon : newIcon" />
        </span>
      </div>
    </div>
    <PullRefresh :disabled="!isScrollTop" class="px-[24px] h-full" v-else-if="!showHot" v-model="refreshing" @refresh="onRefresh">
      <PageVirtualScroller
        v-show="list.length > 0"
        v-model="loading"
        :finished="finished"
        :finished-text="list.length > 0 ? '' : ''"
        @load="onLoad"
        class="pt-[24px]"
        @scroll="onScroll"
        :list="list"
      >
        <template #default="{ item }">
          <div class="grid grid-cols-2 pb-[16px] gap-x-[14px]">
            <template v-for="subItem in item.data" :key="subItem.id">
              <room textColor="#37373D" :data="subItem" />
            </template>
          </div>
        </template>
      </PageVirtualScroller>
      <empty-data v-if="list.length == 0 && !loading" text="暂无搜索内容" icon="search" />
    </PullRefresh>
  </page-box>
</template>

<script setup name="Search">
  import apis from '@/api/home/index';
  import searchIcon from '@/assets/img/home/search.png';
  import closeIcon from '@/assets/img/home/search-close.png';
  import { showToast } from 'vant';
  import { useRouter } from 'vue-router';
  import hotIcon from '@/assets/img/home/search-hot.png';
  import newIcon from '@/assets/img/home/search-new.png';
  import { createLoading, closeLoading } from '@/components/SysLoading';
  import { PageVirtualScroller, usePageVirtualScroller } from '@/components/common/PageVirtualScroller';

  const { onScroll, isScrollTop } = usePageVirtualScroller();
  const Room = defineAsyncComponent(() => import('@/components/common/RoomItem.vue'));
  const EmptyData = defineAsyncComponent(() => import('@/components/common/EmptyData.vue'));
  const router = useRouter();
  const finished = ref(false);
  const refreshing = ref(false);
  const list = ref([]);
  const loading = ref(false);
  const searchContent = ref('');
  const hotList = ref([]);
  const showHot = ref(true);
  const queryParams = ref({ current: 1, size: 50 });
  const opacity = ref(1);
  let searchTimeout;
  let historyId = 0;
  onMounted(() => {
    // nextTick(() => {
    //   const input = document.getElementById('myInput');
    //   input && input.focus();
    //   setTimeout(() => {
    //     opacity.value = 1;
    //   }, 600);
    // });
    createLoading({ overlay: false });
    apis.getHotSearch({}).then((res) => {
      res &&
        res.map((item) => {
          if (item.matchType == 2) {
            item.homeName = item.awayTeamName || '';
            item.awayName = item.homeTeamName || '';
          } else {
            item.homeName = item.homeTeamName || '';
            item.awayName = item.awayTeamName || '';
          }
          return item;
        });
      hotList.value = res;
      closeLoading();
    });
  });
  const onInputChange = () => {
    if (searchTimeout) {
      clearTimeout(searchTimeout);
      searchTimeout = null;
    }
    searchTimeout = setTimeout(() => {
      if (searchContent.value) {
        list.value = [];
        onRefresh(true);
      } else {
        onClearSearch();
      }
    }, 500);
  };
  const onClearSearch = () => {
    setTimeout(() => {
      searchContent.value = '';
      showHot.value = true;
    }, 100);
  };
  const onGoBack = () => {
    setTimeout(() => {
      router.push({
        path: '/home',
      });
    }, 200);
  };
  const onRefresh = (isShowLoading) => {
    // 清空列表数据
    finished.value = false;
    // 重新加载数据
    // 将 loading 设置为 true，表示处于加载状态
    loading.value = true;
    queryParams.value.current = 1;
    onLoad(isShowLoading);
  };
  const onLoad = (isShowLoading) => {
    if (refreshing.value) {
      list.value = [];
      refreshing.value = false;
    }
    onSearch(isShowLoading);
  };
  const onInputSearch = () => {
    if (!searchContent.value || !searchContent.value.trim()) {
      showToast('请输入搜索内容');
      return;
    }
    createLoading({ overlay: false });
    list.value = [];
    onRefresh('onSearch');
  };

  const onSelectHot = (item) => {
    setTimeout(() => {
      router.push({
        path: '/roomDetail',
        query: {
          id: item.matchId,
          liveId: item.id,
          type: item.matchType,
          userId: item.userId,
        },
      });
    }, 100);
  };
  const onSearch = (isShowLoading) => {
    showHot.value = false;
    apis
      .getHomeLiving({ name: searchContent.value, ...queryParams.value })
      .then((res) => {
        let { total, records } = res;
        const oldIndex = list.value.length - 1;
        const lastItem = oldIndex >= 0 ? list.value[oldIndex] : {};
        if (lastItem.data && lastItem.data.length === 1 && records.length > 0) {
          lastItem.data.push(records[0]);
          records = records.slice(1);
        }
        let newIndex = 0;
        const newList = [];
        records.forEach((cur) => {
          if (newList[newIndex]) {
            newList[newIndex].data.push(cur);
            newIndex++;
          } else {
            newList[newIndex] = {
              id: historyId + '',
              data: [cur],
            };
            historyId++;
          }
        });
        list.value.push(...newList);
        loading.value = false;
        refreshing.value = false;
        queryParams.value.current++;
        if (queryParams.value.current * queryParams.value.size >= total || total === undefined) {
          finished.value = true;
        }
        if (isShowLoading && isShowLoading == 'onSearch') {
          closeLoading();
        }
      })
      .catch(() => {
        if (isShowLoading && isShowLoading == 'onSearch') {
          closeLoading();
        }
        loading.value = false;
        finished.value = true;
      });
  };
</script>
<style scoped lang="scss">
  .search-page {
    line-height: normal;
    font-family: PingFang SC;

    input {
      caret-color: #37373d;
      caret-width: 3px;
      caret-shape: auto; /* 修改光标形状 */
    }
    input {
      -webkit-appearance: none;
    }
    input[type='search']::-webkit-search-decoration,
    input[type='search']::-webkit-search-cancel-button {
      display: none;
    }

    ::-webkit-input-placeholder {
      /* WebKit browsers */
      color: #94999f;
    }
    :-moz-placeholder {
      /* Mozilla Firefox 4 to 18 */
      color: #94999f;
    }
    ::-moz-placeholder {
      /* Mozilla Firefox 19+ */
      color: #94999f;
    }
    :-ms-input-placeholder {
      /* Internet Explorer 10+ */
      color: #94999f;
    }
    .default-text {
      color: #c7c7de;
      font-family: 'MiSans';
      font-weight: bold;
      line-height: normal;
      transform: skew(-7.8deg);
    }
    .color-0 {
      color: #d63823;
      font-family: 'MiSans';
    }
    .color-1,
    .color-2 {
      color: #f69521;
      font-weight: bold;
    }
  }
</style>
