<template>
  <div class="px-[24px] news">
    <VirtualListScroller
      v-show="showList"
      :finished="finished"
      finished-text=""
      v-model="loading"
      ref="virtListRef"
      :list="list"
      itemKey="id"
      @load="onLoad"
      :minSize="20"
    >
      <template #default="{ itemData, index }">
        <div v-if="index > 0" :class="`${index == 0 ? 'mt-[3px]' : 'mt-[25px]'}`">
          <NewsItem :data="itemData" @click="goDetail(itemData.id)" />
        </div>
        <div v-else class="relative w-[702px] h-[380px] flex-shrink-0 mt-[24px] rounded-[20px]" @click="goDetail(itemData.id)">
          <div class="w-full h-full flex justify-center items-center rounded-[20px]">
            <UseImg :src="itemData.pic" class="w-[702px] h-[380px] object-cover rounded-[20px]" />
          </div>
          <div class="absolute left-[16px] bottom-[20px] w-[662px]">
            <div class="text-[28px] content-baseline leading-[42px] break-words break-all w-[662px]">
              <span class="hot-icon absolute left-0 bottom-[47px]"></span>
              <span class="hot-item"></span>
              <span class="text-[30px] font-medium whitespace-pre-wrap break-words text-white">{{ itemData.title }} </span>
            </div>
          </div>
        </div>
      </template>
    </VirtualListScroller>

    <empty-data v-if="showList && list.length == 0" text="暂无数据" />
  </div>
</template>
<script setup>
  import { useRouter } from 'vue-router';
  import apis from '@/api/home/index';
  import UseImg from '@/components/common/UseImg.vue';
  import { createLoading, closeLoading } from '@/components/SysLoading';
  import { useMessageStore } from '@/store/modules/message';
  import { MessageCommand } from '@/views/message/utils/types';
  import { VirtualListScroller } from '@/components/common/VirtualListScroller';

  const NewsItem = defineAsyncComponent(() => import('./NewsItem.vue'));
  const EmptyData = defineAsyncComponent(() => import('@/components/common/EmptyData.vue'));

  const router = useRouter();
  const messageStore = useMessageStore();
  const list = ref([]);
  const loading = ref(true);
  const finished = ref(false);
  const refreshing = ref(false);
  const queryParams = ref({ current: 1, size: 30 });
  const showList = ref(false);
  const scrollRef = ref();
  const loaded = ref(false);
  const virtListRef = ref();
  // useRestoreScroll({
  //   el: scrollRef,
  // });

  const onRefresh = () => {
    scrollRef.value.scrollTop = 0;
    list.value = [];
    refreshing.value = true;
    // 清空列表数据
    finished.value = false;
    // 重新加载数据
    queryParams.value.current = 1;
    loading.value = true;
    onLoad();
  };
  const onLoad = () => {
    const offsetHeight = virtListRef.value.getScrollOffset();
    getNews(offsetHeight);
  };
  const getNews = (offsetHeight) => {
    // 将 loading 设置为 true，表示处于加载状态
    apis
      .getNews(queryParams.value)
      .then((res) => {
        closeLoading();
        const { total, records } = res;
        if (queryParams.value.current == 1) {
          list.value = records;
        } else {
          list.value = list.value.concat(records);
          if (offsetHeight) {
            virtListRef.value.setScrollOffset(offsetHeight);
          }
        }
        loading.value = false;
        refreshing.value = false;
        finished.value = total < queryParams.value.size || list.value.length === total;
        queryParams.value.current++;
        showList.value = true;
        loaded.value = true;
      })
      .catch(() => {
        showList.value = true;
        loading.value = false;
        refreshing.value = false;
        loaded.value = true;
      });
  };

  const goDetail = (id) => {
    router.push({ path: '/newsDetail', query: { id } });
  };

  onMounted(() => {
    showList.value = false;
    createLoading({ overlay: false });
    getNews();
    onWsListen();
  });
  const onWsListen = () => {
    const onCheck = () => {
      const messageServer = messageStore.getMessageServer();
      if (!messageServer) {
        setTimeout(() => {
          onCheck();
        }, 1000);
        return;
      }
      messageServer.chatEmitter.on('messageAll', (res) => {
        setTimeout(() => {
          if (res.command === MessageCommand.messageNotify) {
            onRefresh();
          }
        }, 200);
      });
    };
    onCheck();
  };
  defineExpose({
    onRefresh,
  });
</script>
<script>
  export default {
    name: 'News',
  };
</script>
<style scoped lang="scss">
  .news {
    height: 100%;
    min-height: calc(100vh - 360px);
    background: linear-gradient(180deg, rgba(255, 255, 255, 0) 0%, #fff 52.41%);

    .hot-item {
      display: inline-flex;
      width: 112px;
    }

    .hot-icon {
      width: 110px;
      height: 31.4px;
      background-size: contain !important;
      background: url('../../../assets/img//home/today-hot-icon.png') no-repeat center;
    }
  }
</style>
