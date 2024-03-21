<template>
  <div class="user-box">
    <AppHeader :isImg="true" />
    <div class="user-card flex items-center px-[30px] pt-[36px] pb-[20px] cursor-pointer" @click="login">
      <div class="user-card__cover grow-0 shrink-0 basis-[100px] w-[100px] h-[100px]">
        <UseImg
          class="w-full h-full rounded-[100%]"
          :src="userStore.token ? userInfo.avatar || defaultImgSrc : defaultWImgSrc"
          alt="logo"
          not-fill-icon
        />
      </div>
      <div class="user-card__info flex-1 w-[calc(100%-100px)] pl-[20px]">
        <template v-if="!userStore.token">
          <div class="user-card__info--name">登录/注册</div>
        </template>
        <template v-else>
          <div class="user-card__info--name">{{ userInfo.nickname || '-' }}</div>
          <div class="user-card__info--level pt-[10px]">
            <div class="user-card__info--level__inner inline-flex items-center">
              <span class="level" :class="['level-' + userInfo.level]"></span>
              <span class="pl-[6px]">{{ userInfo.levelName }}</span>
            </div>
          </div>
        </template>
      </div>
    </div>
    <div :class="`user-card__setting ${appStore.immersive ? 'inapp' : ''}`" @click.stop="handleSettingClick">
      <div class="user-card__setting--icon"></div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { computed } from 'vue';
  import { useAppStore } from '@/store/modules/app';
  import { useUserStore } from '@/store/modules/user';
  import defaultWImgSrc from '@/assets/img/user/default_user_w.png';
  import defaultImgSrc from '@/assets/img/user/default_user.png';

  import UseImg from '@/components/common/UseImg.vue';
  const userStore = useUserStore();
  const appStore = useAppStore();
  const route = useRoute();
  const router = useRouter();
  const userInfo = computed(() => userStore.userInfo);
  const login = () => {
    if (!userStore.token) {
      router.push(`/login?redirect=${route.fullPath}`);
    } else {
      router.push('/user/personalInfo');
    }
  };
  const handleSettingClick = () => {
    setTimeout(() => {
      router.push({
        name: 'UserSetting',
      });
    }, 100);
  };
</script>
<script lang="ts">
  export default {
    name: 'UserBox',
  };
</script>

<style scoped lang="scss">
  .user-box {
    background: url('@/assets/img/user/user_bg.png') no-repeat, #f2f3f7;
    background-size: 100% auto, 100% 100%;
    background-position: 0 -88px, 0 0;
    position: relative;
    .user-card {
      @include use-active-style();
    }
    .user-card__setting {
      position: absolute;
      top: 0;
      right: 0;
      z-index: 2;
      box-sizing: content-box;
      padding: 36px 30px 36px 36px;
      cursor: pointer;
      &.inapp {
        top: 88px;
      }
      .user-card__setting--icon {
        width: 40px;
        height: 36px;
        background: url('@/assets/img/user/user_set_icon.png') no-repeat, transparent;
        background-size: 100% 100%;
        background-position: 0 0;
        @include use-active-style();
      }
    }
    .user-card__info--name {
      font: 500 32px / normal 'PingFang SC';
      color: #37373d;
    }
    .user-card__info--level {
      font: 400 24px / normal 'PingFang SC';
      color: #575762;
      &__inner {
        min-width: 98px;
        height: 42px;
        padding: 0 10px;
        border-radius: 400px;
        background: #e4e6ed;
      }
      .level {
        display: inline-block;
        width: 24px;
        height: 27px;
        background: url('@/assets/img/user/level/v1.png') no-repeat, transparent;
        background-size: auto 100%;
        background-position: center;
        @for $i from 1 through 6 {
          &.level-#{$i} {
            background: url('@/assets/img/user/level/v#{$i}.png') no-repeat, transparent;
            background-size: auto 100%;
            background-position: center;
          }
        }
      }
    }
  }
</style>
