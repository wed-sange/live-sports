<template>
  <page-box class="setting-page">
    <template #header> <AppHeader /><NavBar title="设置" left-arrow @click-left="onLeftClick" /></template>

    <div class="setting-tool-box p-[30px]">
      <template v-for="item in toolList" :key="item.key">
        <div class="setting-tool-box__item p-[20px] mb-[30px]" v-ripple="{ color: '#3a3a3a' }">
          <div class="flex justify-between items-center w-full cursor-pointer" @click="handleClick(item)">
            <div class="setting-tool-box__icon shrink-0 grow-0 basis-[66px] w-[66px] h-[66px]" :class="[item.icon]"> </div>
            <div class="setting-tool-box__label flex-auto w-[calc(100%-66px-14px)] pl-[16px]">
              {{ item.label }}
            </div>
            <div class="setting-tool-box__other shrink-0 grow-0 basis-auto">
              <!-- 新人 -->
              <van-icon name="arrow" />
            </div>
          </div>
        </div>
      </template>
    </div>
    <template #footer>
      <div class="w-full px-[24px] pb-[14px]">
        <van-button class="setting-page-btn click-style" @click="openAction">退出登录</van-button>
      </div>
    </template>
  </page-box>
</template>
<script setup lang="ts">
  import { computed } from 'vue';
  import { useUserStore } from '@/store/modules/user';
  import { floatingActionSheet } from '@/components/FloatingActionSheet';

  const userStore = useUserStore();
  const router = useRouter();
  type ToolListData = { label: string; key: string; icon: string };
  const toolList = computed(() => {
    const list: ToolListData[] = [
      { label: '编辑资料', key: 'UserPersonalInfo', icon: 'my_edit' },
      // { label: '推送设置', key: 'UserPushSetting', icon: 'my_notify' },
    ];
    return list;
  });
  const actionList = [{ name: '退出登录', key: 'logout', class: 'error' }];
  const openAction = () => {
    setTimeout(() => {
      floatingActionSheet({
        actions: actionList,
        cancelText: '取消',
        closeOnClickAction: true,
        select: (action) => {
          if (action.key === 'logout') {
            userStore.logout();
            router.push({
              name: 'User',
            });
          }
        },
      });
    }, 100);
  };
  const handleClick = (item: ToolListData) => {
    router.push({ name: item.key });
  };
  const onLeftClick = () => {
    router.go(-1);
  };
</script>
<script lang="ts">
  export default {
    name: 'UserSetting',
  };
</script>
<style scoped lang="scss">
  .setting-tool-box__item {
    border-radius: 16px;
    background: #fff;
    // @include use-active-style();
  }
  .setting-tool-box__icon {
    $icon-list: 'my_edit', 'my_notify';
    @for $i from 1 through length($icon-list) {
      $cur: nth($icon-list, $i);
      &.#{$cur} {
        background: url('@/assets/img/user/icon_#{$cur}.png') no-repeat, transparent;
        background-size: auto 100%;
        background-position: center;
      }
    }
  }
  .setting-tool-box__label {
    font: 400 28px / normal 'PingFang SC';
    color: #37373d;
  }
  .setting-tool-box__other {
    color: #d7d7e7;
    .van-icon {
      transform: translateX(10px);
      font-size: 30px;
      font-weight: bold;
    }
  }
  .setting-page-btn {
    --van-button-default-height: 80px;
    --van-button-radius: 48px;
    --van-button-default-background: #fff;
    background: #fff;
    width: 100%;
    border: none;
    font: 400 30px / normal 'PingFang SC';
    color: #d63823;
    .van-button__text {
      font: inherit;
      color: inherit;
    }
  }
</style>
