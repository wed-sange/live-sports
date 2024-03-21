<template>
  <page-box class="level-page" :class="['lv-' + userLevel]">
    <template #header> <AppHeader /><NavBar title="我的等级" transparent left-arrow @click-left="onLeftClick" /></template>
    <div class="w-full h-full">
      <div class="user-box px-[24px]">
        <div class="user-card p-[40px] cursor-pointer">
          <div class="user-card__info flex items-center">
            <div class="user-card__info--cover grow-0 shrink-0 basis-[60px] w-[60px] h-[60px]">
              <img class="w-full h-full rounded-[100%] object-cover overflow-hidden" :src="userInfo.avatar || defaultImgSrc" alt="logo" />
            </div>
            <div class="user-card__info--name grow-0 shrink-0 basis-[calc(100%-60px)] w-[calc(100%-60px)] pl-[20px] truncate">
              {{ userInfo.nickname || '-' }}
            </div>
          </div>
          <div class="user-card__level flex items-end pt-[24px]">
            <div class="user-card__level--icon grow-0 shrink-0 basis-[104px] w-[104px] h-[64px]"></div>
            <div class="user-card__level--text grow-0 shrink-0 basis-[calc(100%-104px)] w-[calc(100%-104px)] truncate">
              距离升级：{{ growthNextDiff }}成长值
            </div>
          </div>
          <div class="user-card__growth">
            <div class="user-card__growth--progress pt-[18px]">
              <van-progress :percentage="growthProgress" :show-pivot="false" />
            </div>
          </div>
        </div>
        <div class="user-level-flag"></div>
        <div class="user-level-list__outer w-full overflow-hidden">
          <div class="user-level-list">
            <div class="user-level-list__line">
              <div class="user-level-list__line--inner" :style="lineInnerStyle"></div>
            </div>
            <div class="user-level-list__box flex items-center">
              <template v-for="item in levelArr" :key="item.key">
                <div class="user-level-list__item grow-0 shrink-0 basis-1/5 w-1/5" :class="{ active: userLevel === item.val }">
                  <div class="user-level-list__item--text">{{ item.label }}</div>
                </div>
              </template>
            </div>
          </div>
        </div>
      </div>
      <div class="footer-panel h-[calc(100%-470px)]">
        <div class="user-task h-full overflow-auto">
          <div class="user-task-header">我的任务</div>
          <div class="user-task-body">
            <template v-for="item in taskList" :key="item.key">
              <div class="user-task__item py-[30px] van-hairline--bottom h-[146px]">
                <div class="flex justify-between items-center w-full cursor-pointer" @click="handleClick()">
                  <div class="user-task__icon grow-0 shrink-0 w-[74px] h-[74px] mb-[2px]" :class="[item.icon]"></div>
                  <div class="user-task__label flex-auto w-full pl-[20px] mt-[-0.5px]">
                    {{ item.task }}
                    <div class="user-task__label--small pt-[11px]"> +{{ item.result }}成长值 </div>
                  </div>
                  <div class="user-task__other shrink-0 grow-0 basis-auto">
                    <van-button>去完成<van-icon name="arrow" /></van-button>
                  </div>
                </div>
              </div>
            </template>
          </div>
        </div>
      </div>
    </div>
  </page-box>
</template>
<script setup lang="ts">
  import { useUserStore } from '@/store/modules/user';
  import defaultImgSrc from '@/assets/img/user/default_user.png';

  const levelArr = ref([
    { label: '黄铜', val: 1 },
    { label: '白银', val: 2 },
    { label: '黄金', val: 3 },
    { label: '铂金', val: 4 },
    { label: '钻石', val: 5 },
    { label: '星耀', val: 6 },
  ]);
  type TaskListData = { task: string; key: string; result: string; icon: string; state: 'unexecuted' | 'executed' };
  const taskList = ref<TaskListData[]>([
    { task: '每日直播聊天室互动', result: '2', key: '1', icon: 'msg', state: 'unexecuted' },
    { task: '每日观看直播10分钟以上', result: '5', key: '2', icon: 'live', state: 'unexecuted' },
  ]);
  const handleClick = () => {
    router.push({ path: '/home' });
  };
  const userStore = useUserStore();
  onMounted(() => {
    userStore.getUserInfo();
  });

  const userInfo = computed(() => userStore.userInfo);
  const userLevel = computed(() => Number(userInfo.value.level));
  const lineInnerStyle = computed(() => {
    let diff = userLevel.value <= 1 ? 0 : userLevel.value - 1;

    return {
      transform: `scaleX(${(diff * 20) / 100})`,
    };
  });
  const growthNextDiff = computed(() => {
    const diff = userStore.userInfo.growthNextTotal - userStore.userInfo.growth;
    return diff < 0 ? 0 : diff;
  });
  const growthProgress = computed(() => {
    const progress = (userStore.userInfo.growth / userStore.userInfo.growthNextTotal) * 100;
    return progress >= 100 ? 100 : progress;
  });
  const router = useRouter();
  const onLeftClick = () => {
    router.go(-1);
  };
</script>
<script lang="ts">
  export default {
    name: 'UserLevel',
  };
</script>
<style scoped lang="scss">
  .level-page {
    background: url('@/assets/img/user/level/v1_bg.png') no-repeat, #181819;
    background-size: 100% auto, 100% 100%;
    background-position: 0 0, 0 0;
    $card-color: (
      1: linear-gradient(103deg, #fff7f6 7.31%, #fffcf9 9.36%, #ffd9c9 98.96%),
      2: linear-gradient(103deg, #f7f7f7 7.31%, #d4d4d4 98.96%),
      3: linear-gradient(103deg, #fff9e8 7.31%, #fbd8a3 98.96%),
      4: linear-gradient(103deg, #eafdff 7.31%, #a7d0e2 98.96%),
      5: linear-gradient(103deg, #faf3ff 7.31%, #e2bdff 98.96%),
      6: linear-gradient(103deg, #fff 7.31%, #c8d4ff 98.96%),
    );
    $level-text-color: (
      1: #a5502f,
      2: #727272,
      3: #8f612c,
      4: #51858c,
      5: #865aa8,
      6: #5a64b7,
    );
    $level-progress-color: (
      1: #cb6741,
      2: #929292,
      3: #f0a248,
      4: #62aeb8,
      5: #a474c8,
      6: #737ed6,
    );
    $level-circle-color: (
      1: #ffdccd,
      2: #fff,
      3: #fcdfb2,
      4: #afd6e5,
      5: #e5c4ff,
      6: #c2c8ff,
    );
    @for $i from 1 through 6 {
      &.lv-#{$i} {
        background: url('@/assets/img/user/level/v#{$i}_bg.png') no-repeat, #181819;
        background-size: 100% auto, 100% 100%;
        background-position: 0 0, 0 0;
        .user-box {
          .user-card {
            background: map-get($card-color, $i);
          }
          .user-card__level--icon {
            background: url('@/assets/img/user/level/v#{$i}_text.png') no-repeat, transparent;
            background-size: auto 64px;
            background-position: -4.5px center;
          }
          .user-card__level--text {
            color: map-get($level-text-color, $i);
          }
          .user-card__growth {
            .user-card__growth--progress {
              :deep() .van-progress {
                .van-progress__portion {
                  background: map-get($level-progress-color, $i);
                }
              }
            }
          }
          .user-level-flag {
            background: url('@/assets/img/user/level/v#{$i}_flag.png') no-repeat, transparent;
            background-size: auto 100%;
            background-position: center;
          }

          .user-level-list {
            .user-level-list__item {
              &.active {
                &::after {
                  background: map-get($level-circle-color, $i);
                }
              }
            }
          }
        }
      }
    }
  }
  .user-box {
    position: relative;
    padding-top: 72px;
    .user-card {
      background: linear-gradient(103deg, #fff7f6 7.31%, #fffcf9 9.36%, #ffd9c9 98.96%);
      border-radius: 40px;
    }
    .user-card__info--cover {
      border: 2px solid #fff;
      border-radius: 100%;
      overflow: hidden;
    }
    .user-card__info--name {
      font: 500 30px / normal 'PingFang SC';
      color: #37373d;
    }
    .user-card__level--icon {
      background: url('@/assets/img/user/level/v1_text.png') no-repeat, transparent;
      background-size: auto 70%;
      background-position: center;
    }
    .user-card__level--text {
      font: 400 24px / normal 'PingFang SC';
      color: #a5502f;
      padding: 0 0 8px 6px;
    }
    .user-card__growth {
      .user-card__growth--progress {
        :deep() .van-progress {
          height: 6px;
          border-radius: 52px;

          background: #fff;
          .van-progress__portion {
            background: #cb6741;
          }
        }
      }
    }
    .user-level-flag {
      width: 284px;
      height: 264px;
      background: url('@/assets/img/user/level/v1_flag.png') no-repeat, transparent;
      background-size: auto 100%;
      background-position: center;
      position: absolute;
      top: 2.38px;
      right: 45px;
      z-index: 2;
    }
    .user-level-list {
      width: 546.4px;
      padding-top: 50px;
      padding-bottom: 71px;
      margin: 0 auto 0 78px;
      .user-level-list__line {
        width: 100%;
        height: 2px;
        background: rgba(255, 255, 255, 0.1);
        position: relative;
        .user-level-list__line--inner {
          position: absolute;
          top: 0;
          left: 0;
          z-index: 1;
          width: 100%;
          height: 100%;
          background: linear-gradient(270deg, #fff 9.59%, rgba(255, 255, 255, 0) 96.48%);
          transform-origin: 0 50%;
          transform: scaleX(0);
        }
      }
      .user-level-list__item {
        position: relative;
        height: 23px;
        &::after {
          content: '';
          display: block;
          width: 10px;
          height: 10px;
          border-radius: 100%;
          background: #fff;
          position: absolute;
          top: 0;
          left: 0;
          z-index: 2;
          transform: translate(-54%, -60%);
        }
        .user-level-list__item--text {
          position: absolute;
          top: 23px;
          left: 0;
          transform: translateX(-50.5%);
          font: 400 24px / normal 'MiSans';
          color: #fff;
        }
        &.active {
          &::after {
            background: #5b5b61;
          }
          &::before {
            content: '';
            display: block;
            width: 22px;
            height: 22px;
            border-radius: 100%;
            background: #5c5c5c;
            position: absolute;
            top: 0;
            left: 0;
            z-index: 1;
            transform: translate(-55%, -55%);
          }
        }
        &.active ~ .user-level-list__item {
          &::after {
            background: #5b5b61;
          }
          .user-level-list__item--text {
            color: #94999f;
          }
        }
      }
    }
  }
  .footer-panel {
    background: #fff;
    border-radius: 30px 30px 0px 0px;
  }
  .user-task {
    padding: 30px 24px;
    .user-task-header {
      font: 500 32px / normal 'PingFang SC';
      color: #37373d;
    }
    .user-task__icon {
      background: url('@/assets/img/user/level/icon_live.png') no-repeat, transparent;
      background-size: 100% 100%;
      background-position: center;
      &.live {
        background: url('@/assets/img/user/level/icon_live.png') no-repeat, transparent;
        background-size: 100% 100%;
        background-position: center;
      }
      &.msg {
        background: url('@/assets/img/user/level/icon_msg.png') no-repeat, transparent;
        background-size: 100% 100%;
        background-position: center;
      }
    }
    .user-task__label {
      font: 400 30px / normal 'PingFang SC';
      color: #37373d;
    }
    .user-task__label--small {
      font: 400 24px / normal 'PingFang SC';
      color: #94999f;
    }
    .user-task__other {
      .van-button {
        background: transparent;
        border: none;
        border-radius: 0;
        color: #94999f;
        font: 400 26px / normal 'PingFang HK';
        box-shadow: none;
        height: 26px;
        padding: 0 20px 0 0;
        .van-button__text {
          font: inherit;
          color: inherit;
          position: relative;
        }
      }
      .van-icon {
        color: #d7d7e7;
        font-size: 30px;
        font-weight: bold;
        position: absolute;
        top: 50%;
        right: -10px;
        margin-top: -0.5px;
        transform: translateY(-50%);
      }
    }
    .user-task__item {
      @include use-active-style();
    }
  }
</style>
