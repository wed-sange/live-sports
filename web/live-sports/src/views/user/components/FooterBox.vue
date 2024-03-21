<template>
  <div class="footer-box px-[30px] py-[10px]">
    <template v-for="item in toolList" :key="item.key">
      <div class="footer-box__item p-[20px] mb-[30px]" v-ripple="{ color: '#3a3a3a' }">
        <div class="flex justify-between items-center w-full cursor-pointer" @click="handleClick(item)">
          <div class="footer-box__icon shrink-0 grow-0 basis-[66px] w-[66px] h-[66px]" :class="[item.icon]"> </div>
          <div class="footer-box__label flex-auto w-[calc(100%-66px-14px)] pl-[16px]">
            {{ item.label }}
          </div>
          <div class="footer-box__other shrink-0 grow-0 basis-auto">
            <!-- 新人 -->
            <van-icon name="arrow" />
          </div>
        </div>
      </div>
    </template>
  </div>
</template>
<script setup lang="ts">
  import { computed } from 'vue';
  import { showToast, showConfirmDialog } from 'vant';
  import { useUserStore } from '@/store/modules/user';
  import { copyText } from '@/utils/copyText';

  const userStore = useUserStore();
  type ToolListData = { label: string; key: string; icon: string };
  const toolList = computed(() => {
    const list: ToolListData[] = [
      // { label: '编辑资料', key: 'UserPersonalInfo', icon: 'my_feedback' },
      { label: '我的等级', key: 'UserLevel', icon: 'my_level' },
      { label: '分享好友', key: 'Invite', icon: 'my_share' },
      { label: '意见反馈', key: 'UserConcat', icon: 'my_feedback' },
    ];
    // if (userStore.token) {
    //   list.push({ label: '退出登录', key: 'Logout' });
    // }
    return list;
  });
  const logout = () => {
    showConfirmDialog({
      message: '是否确认退出登录',
      className: 'custom-dialog-confirm',
      confirmButtonText: '取消',
      cancelButtonText: '确认',
    })
      .then(async () => {
        // userStore.logout();
        // on confirm
      })
      .catch(async () => {
        userStore.logout();
        // on cancel
      });
  };
  const router = useRouter();
  const handleClick = (item: ToolListData) => {
    if (item.key === 'logout') {
      logout();
    } else if (item.key === 'Invite') {
      copyInvite();
    } else {
      router.push({ name: item.key });
    }
  };
  const copyInvite = () => {
    copyText(window.location.href).then((success) => {
      showToast({
        // icon: 'custom-icon-success',
        className: 'custom-toast',
        message: success ? '复制链接成功' : '该浏览器不支持复制功能',
        // overlay: true,
      });
    });
  };
</script>
<script lang="ts">
  export default {
    name: 'FooterBox',
  };
</script>

<style scoped lang="scss">
  .footer-box__item {
    border-radius: 16px;
    background: #fff;
    // @include use-active-style();
  }
  .footer-box__icon {
    $icon-list: 'my_feedback', 'my_level', 'my_share';
    @for $i from 1 through length($icon-list) {
      $cur: nth($icon-list, $i);
      &.#{$cur} {
        background: url('@/assets/img/user/icon_#{$cur}.png') no-repeat, transparent;
        background-size: auto 100%;
        background-position: center;
      }
    }
  }
  .footer-box__label {
    font: 400 28px / normal 'PingFang SC';
    color: #37373d;
  }
  .footer-box__other {
    color: #d7d7e7;
    .van-icon {
      transform: translateX(10px);
      font-size: 30px;
      font-weight: bold;
    }
  }
</style>
