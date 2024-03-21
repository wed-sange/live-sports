<template>
  <teleport to="body">
    <div class="custom-popup-box">
      <van-popup
        v-model:show="show"
        :overlay-style="{ background: 'transparent' }"
        class="custom-popup"
        ref="rootRef"
        position="right"
        @opened="opened"
        @closed="afterClose"
      >
        <page-box>
          <template #header>
            <NavBar title="选择国家和地区" left-arrow @click-left="onLeftClick" />
          </template>
          <div class="w-full h-full overflow-auto">
            <EmptyData v-if="isEmpty" />
            <van-index-bar v-else :sticky="false" :index-list="indexList">
              <template v-for="item in countryData" :key="item.index">
                <van-index-anchor :index="item.index" />
                <template v-for="subItem in item.data" :key="subItem.code">
                  <div class="country-popup__item" @click="sure(subItem)">
                    <div class="country-popup__icon">
                      <img :src="subItem.icon" alt="icon" />
                    </div>
                    <div class="country-popup__info">
                      <div class="country-popup__name">{{ subItem.name }}</div>
                      <div class="country-popup__code">+{{ subItem.code }}</div>
                    </div>
                  </div>
                </template>
              </template>
            </van-index-bar>
          </div>
        </page-box>
      </van-popup>
    </div>
  </teleport>
</template>

<script lang="ts" setup>
  import { ref, reactive, nextTick } from 'vue';
  import mitt from 'mitt';
  import { showLoadingToast, allowMultipleToast, type PopupInstance, IndexBar as VanIndexBar, IndexAnchor as VanIndexAnchor } from 'vant';
  import { ApiFormEvents, CountryRenderItemData, CountryItemData } from './types';
  const rootRef = ref<PopupInstance | null>(null);
  const show = ref(false);
  const formMitt = reactive(mitt());
  const afterClose = () => {
    nextTick(() => {
      formMitt.emit(ApiFormEvents.afterClose);
      if (rootRef.value) {
        rootRef.value.$el.remove && rootRef.value.$el.remove();
      }
    });
  };
  const opened = () => {
    nextTick(() => {
      formMitt.emit(ApiFormEvents.opened);
    });
  };
  const onLeftClick = () => {
    show.value = false;
  };
  const close = () => {
    show.value = false;
  };

  const sure = async (item: CountryItemData) => {
    current.value = item.code || '';
    formMitt.emit(ApiFormEvents.sure, item);
  };
  const isEmpty = ref(true);
  const indexList = ref<string[]>([]);
  const countryData = ref<CountryRenderItemData[]>([]);
  const current = ref('');
  const init = async (
    currentValue: string,
    getCountryData: () => Promise<{
      indexList: string[];
      list: CountryRenderItemData[];
    }>,
  ) => {
    current.value = currentValue || '';
    allowMultipleToast();
    const toast = showLoadingToast({
      duration: 0,
      forbidClick: true,
      message: '加载中...',
    });
    try {
      const response = await getCountryData();
      countryData.value = response.list;
      indexList.value = response.indexList;
      isEmpty.value = countryData.value.length === 0;
    } finally {
      toast.close();
    }
  };
  defineExpose({
    show,
    close,
    formMitt,
    init,
  });
</script>

<script lang="ts">
  export default {
    name: 'CountryPopup',
  };
</script>
<style scoped lang="scss">
  .custom-popup-box {
    :deep().van-popup.custom-popup {
      width: 100%;
      height: 100%;
    }
    :deep(.van-index-bar) {
      .van-index-anchor {
        height: 46px;
        font: 500 26px / 46px 'PingFang SC';
        color: #37373d;
        padding: 0 24px;
      }
      .van-index-bar__index {
        font: 400 22px / normal 'PingFang SC';
        color: #94999f;
        padding-right: 24px;
        &.van-index-bar__index--active {
          font-weight: 400;
          color: #94999f;
        }
      }
    }
    .country-popup__item {
      padding: 30px 24px;
      background: #fff;
      position: relative;
      display: flex;
      align-items: center;
      & + .country-popup__item::after {
        content: '';
        display: block;
        width: calc(100% - 48px);
        height: 2px;
        background: #f2f3f7;
        position: absolute;
        left: 24px;
        top: -2px;
        z-index: 1;
        pointer-events: none;
      }

      @include use-active-style();
    }
    .country-popup__icon {
      flex: 0 0 60px;
      width: 60px;
      height: 40px;
      background: #f4f4f5;
      border-radius: 2px;
      border: 2px solid #f4f4f5;
      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }
    .country-popup__info {
      flex: 0 0 calc(100% - 60px);
      width: calc(100% - 60px);
      display: flex;
      justify-content: space-between;
      padding: 0 64px 0 20px;
      .country-popup__name {
        font: 400 30px / normal 'PingFang SC';
        color: #000;
        white-space: nowrap;
        flex: 0 0 auto;
      }
      .country-popup__code {
        font: 400 30px / normal 'PingFang SC';
        color: #94999f;
        white-space: nowrap;
        flex: 0 0 auto;
      }
    }
  }
</style>
