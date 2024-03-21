<template>
  <div class="w-full px-[24px] py-[12px] flex flex-col relative z-0 sports-card-box" @click="handleItemClick(item)">
    <span class="sports-card__tip self-center pb-[24px]" v-if="showDate && item.matchDate">{{ item.matchDate }}</span>
    <div
      class="sports-card rounded-[20px] w-full py-[30px]"
      @touchend="removeClickStyle"
      @touchcancel="removeClickStyle"
      @touchstart="addClickStyle"
      :style="{ opacity: itemOpacity }"
      :class="[item.sportsTypeData.icon]"
    >
      <div class="sports-card__bar flex items-center h-[30px] px-[30px] mb-[2px]">
        <div class="grow-0 shrink-0 basis-[29px] w-[29px] h-[29px] mb-[2px]">
          <span class="sports-card__type inline-block w-full h-full overflow-hidden" :class="[item.sportsTypeData.icon]"></span>
        </div>
        <div class="sports-card__info flex items-center flex-auto">
          <span class="ml-[8px] sports-card__info--name truncate">{{ item.name }}</span>
          <span class="ml-[10px]">{{ item.time }}</span>
        </div>
        <div class="sports-card__info--star rounded-[50%] cursor-pointer mb-[2px]" @touchstart.stop @click.stop="handleFollowClick(item)">
          <FollowStar
            class="sports-card__star inline-block w-full h-full overflow-hidden"
            ref="followStarRef"
            :class="{ active: item.isFollow }"
          />
        </div>
        <span class="sports-card__info--state" :class="[item.eventStateClass]">
          {{ item.eventStateText }}{{ item.runTime || ''
          }}<span class="sports-card__info--state__dot" v-show="item.eventStateClass === 'run'">'</span>
        </span>
      </div>
      <div class="sports-card__group flex items-center justify-between px-[46px] pt-[4px] w-full">
        <div class="sports-card__name sports-card__name--home grow-0 shrink-0 w-[216px] pt-[28px]">
          <div class="w-[60px] h-[60px] mx-auto mb-[6px]">
            <span class="sports-card__country inline-block w-full h-full overflow-hidden">
              <UseImg class="w-full h-full rounded-[inherit]" :src="item.homeTeamNoc" alt="logo" not-fill-icon />
              <!-- <img class="inline-block w-full h-full rounded-[inherit] object-contain overflow-hidden" :src="item.homeTeamNoc" alt="logo" /> -->
            </span>
          </div>
          <div class="sports-card__name--text text-center line-clamp-2 break-all pt-[1px]">{{ item.homeTeamName }}</div>
        </div>
        <div
          class="sports-card__space grow-0 shrink-0 basis-[106px] w-[106px] flex items-center justify-center self-stretch"
          :class="{ none: item.eventState === '0' || item.eventState === '1' }"
        >
          <template v-if="item.eventState === '0' || item.eventState === '1'"> VS </template>
          <template v-else>
            <span>{{ item.homeTeamScore }}</span>
            <span>-</span>
            <span>{{ item.visitTeamScore }}</span>
          </template>
        </div>
        <div class="sports-card__name sports-card__name--visit grow-0 shrink-0 w-[216px] pt-[28px]">
          <div class="w-[60px] h-[60px] mx-auto mb-[6px]">
            <span class="sports-card__country inline-block w-full h-full overflow-hidden">
              <UseImg class="w-full h-full rounded-[inherit]" :src="item.visitTeamNoc" alt="logo" not-fill-icon />
              <!-- <img
                class="inline-block w-full h-full rounded-[inherit] object-contain overflow-hidden"
                :src="item.visitTeamNoc"
                alt="logo"
              /> -->
            </span>
          </div>
          <div class="sports-card__name--text text-center line-clamp-2 break-all pt-[1px]">{{ item.visitTeamName }}</div>
        </div>
      </div>
      <div class="sports-card__score flex justify-center items-center h-[28px] mt-[8px]">
        {{ item.eventActionText }}
        <div class="sports-card__score--value grow-0 shrink-0 flex justify-center">
          <span>{{ item.homeTeamHalfScore }}</span>
          <span>-</span>
          <span>{{ item.visitTeamHalfScore }}</span>
        </div>
      </div>
      <slot name="bottom"></slot>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { addSubscribe, removeSubscribe } from '@/api/user';
  import { type SportsCardData } from './types';
  // import { showLoadingToast } from 'vant';
  import { useUserStore } from '@/store/modules/user';
  import FollowStar from './FollowStar/index.vue';
  import UseImg from '@/components/common/UseImg.vue';
  import { useAppStore } from '@/store/modules/app';

  const appStore = useAppStore();
  withDefaults(
    defineProps<{
      item: SportsCardData;
      showDate?: boolean;
    }>(),
    {
      item: () => {
        return {} as SportsCardData;
      },
      showDate: true,
    },
  );
  const userStore = useUserStore();
  const route = useRoute();
  const router = useRouter();
  const followStarRef = ref<typeof FollowStar | null>(null);
  const followLoading = ref(false);
  let moveTimer;
  const handleFollowClick = async (item: SportsCardData) => {
    if (!userStore.token) {
      router.push({
        path: '/login',
        query: {
          redirect: route.fullPath,
        },
      });
      return;
    }
    if (followLoading.value || !followStarRef.value || followStarRef.value.getAnimationState()) {
      return;
    }
    followLoading.value = true;
    followStarRef.value?.createAnimation(item.isFollow ? 'cancel' : 'ok');
    // const toast = showLoadingToast({
    //   duration: 0,
    //   forbidClick: true,
    //   message: '加载中...',
    // });
    const submitInfo: {
      request: typeof addSubscribe | typeof removeSubscribe | null;
    } = {
      request: null,
    };
    if (item.isFollow) {
      submitInfo.request = removeSubscribe;
    } else {
      submitInfo.request = addSubscribe;
    }
    try {
      await submitInfo.request({
        matchId: item.matchId,
        matchType: item.sportsTypeData.value,
      });
      item.isFollow = !item.isFollow;
    } finally {
      followLoading.value = false;
      // toast.close();
    }
  };
  const handleItemClick = (item: SportsCardData) => {
    router.push({ path: '/roomDetail', query: { id: item.matchId, type: item.matchType } });
  };
  let itemOpacity = ref(1);
  const addClickStyle = () => {
    itemOpacity.value = 0.3;
    clearTimeout(moveTimer); //再次清空定时器，防止重复注册定时器
    moveTimer = setTimeout(() => {
      appStore.matchLongClick = true;
    }, 200);
  };
  const removeClickStyle = () => {
    clearTimeout(moveTimer);
    appStore.matchLongClick = false;
    itemOpacity.value = 1;
  };
</script>
<script lang="ts">
  export default {
    name: 'SportsCard',
  };
</script>
<style scoped lang="scss" src="./index.scss"></style>
