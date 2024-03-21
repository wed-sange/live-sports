<template>
  <div class="tool-box px-[30px] py-[20px]">
    <div class="tool-box__outer p-[30px] pb-[29px]">
      <div class="tool-box__title">常用功能</div>
      <div class="flex justify-between items-center w-full">
        <template v-for="item in toolList" :key="item.key">
          <div
            class="tool-box__item shrink-0 grow-0 basis-[104px] w-[104px] flex flex-col justify-center items-center cursor-pointer pt-[24px]"
            @click="handleClick(item)"
          >
            <div class="shrink-0 grow-0 basis-[70px] h-[70px] w-[70px]" :class="item.key == selectKey ? 'tab-active-item' : ''">
              <div class="tool-icon inline-block w-full h-full overflow-hidden" :class="['icon-' + item.icon]"></div>
            </div>
            <div class="tool-label shrink-0 grow-0 basis-auto w-full text-center pt-[16px]">
              {{ item.label }}
            </div>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { ref } from 'vue';
  type ToolListData = { label: string; key: string; icon: string };

  const toolList = ref<ToolListData[]>([
    { label: '我的订阅', key: 'UserSubscribe', icon: 'subscribe' },
    { label: '活动中心', key: 'UserActivity', icon: 'activity' },
    { label: '我的关注', key: 'UserFollowing', icon: 'following' },
    { label: '观看历史', key: 'UserHistory', icon: 'history' },
  ]);
  const selectKey = ref();
  const router = useRouter();
  const handleClick = (item: ToolListData) => {
    selectKey.value = item.key;
    setTimeout(() => {
      selectKey.value = '';
      router.push({ name: item.key });
    }, 250);
  };
</script>
<script lang="ts">
  export default {
    name: 'ToolBox',
  };
</script>

<style scoped lang="scss">
  @keyframes bounce-in {
    0% {
      transform: scale(0);
    }
    50% {
      transform: scale(1.4);
    }
    100% {
      transform: scale(1);
    }
  }

  /* 2️⃣把样式中的类名进行更改。 */

  // .tool-box__item {
  //   // @include use-active-style();
  // }
  .tab-active-item {
    transform-origin: center;
    animation: bounce-in 0.25s;
  }
  .tool-box__outer {
    background: #fff;
    border-radius: 20px;
    overflow: hidden;
  }
  .tool-box__title {
    font: 500 28px / normal 'PingFang SC';
    color: #37373d;
  }
  .tool-icon {
    $icon-list: 'subscribe', 'activity', 'following', 'history';
    @for $i from 1 through length($icon-list) {
      $cur: nth($icon-list, $i);
      &.icon-#{$cur} {
        background: url('@/assets/img/user/icon_#{$cur}.png') no-repeat, #f7f8fb;
        background-size: 100% 100%;
        background-position: center;
        border-radius: 100%;
      }
    }
  }
  .tool-label {
    font: 400 26px / normal 'PingFang SC';
    color: #37373d;
    white-space: nowrap;
  }
</style>
