<template>
  <div
    class="w-full py-[8px]"
    @click="
      item.anchor.liveStatus === '2' &&
        $router.push({
          path: '/roomDetail',
          query: {
            id: item.card.matchId,
            liveId: item.anchor?.liveId,
            type: item.card.matchType,
            userId: item.anchor?.id,
          },
        })
    "
  >
    <div class="sports-mini-card rounded-[16px] w-full overflow-hidden" :class="[item.card.sportsTypeData.icon]">
      <div class="sports-mini-card__cover w-full h-[184px] relative">
        <UseImg class="w-full h-full rounded-[inherit]" :src="item.card.cover" alt="cover" />
        <!-- <img class="inline-block w-full h-full rounded-[inherit] overflow-hidden" :src="item.card.cover" alt="cover" /> -->
        <div class="w-full h-full absolute top-0 left-0">
          <div class="sports-mini-card__name flex items-end justify-between h-[72px] absolute bottom-0 left-0">
            <div class="sports-mini-card__name--text grow-0 shrink-0 basis-auto truncate">{{ item.card.name }}</div>
            <div class="sports-mini-card__name--hot grow-0 shrink-0 basis-auto flex items-center">
              <span class="sports-mini-card__name--hot-icon"></span>{{ item.card.hot && item.card.hot > 9999 ? '9999+' : item.card.hot }}
            </div>
          </div>
          <div class="sports-mini-card__anchor--status absolute top-[17px] left-[16px]" v-if="item.anchor.liveStatus === '2'">
            {{ item.anchor.liveStatusText }}
          </div>
        </div>
      </div>
      <div class="w-full flex items-center h-[102px] bg-[#fff] overflow-hidden px-[16px]">
        <div class="sports-mini-card__anchor--cover grow-0 shrink-0 w-[64px] h-[64px] rounded-[100%]">
          <UseImg class="w-full h-full rounded-[inherit]" :src="item.anchor.cover" alt="cover" not-fill-icon />
          <!-- <img class="inline-block w-full h-full rounded-[inherit] overflow-hidden" :src="item.anchor.cover" alt="cover" /> -->
        </div>
        <div class="sports-mini-card__info grow-0 shrink-0 w-[calc(100%-64px)] pl-[10px]">
          <div class="sports-mini-card__team flex items-center">
            <!-- <div class="sports-mini-card__team--home grow-0 shrink-0 basis-auto max-w-[calc(50%-24px)] truncate">
              {{ item.card.homeTeamName }}
            </div>
            <div class="grow-0 shrink-0 basis-[48px] text-center">VS</div>
            <div class="sports-mini-card__team--visit grow-0 shrink-0 basis-auto max-w-[calc(50%-24px)] truncate">
              {{ item.card.visitTeamName }}
            </div> -->
            <div class="max-w-full truncate"> {{ item.card.homeTeamName }}VS{{ item.card.visitTeamName }} </div>
          </div>
          <div class="sports-mini-card__anchor--text pt-[4px]">
            {{ item.anchor.name }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { type SportsMiniCardData } from './types';
  import UseImg from '@/components/common/UseImg.vue';
  withDefaults(
    defineProps<{
      item: SportsMiniCardData;
    }>(),
    {
      item: () => {
        return {} as SportsMiniCardData;
      },
    },
  );
</script>
<script lang="ts">
  export default {
    name: 'SportsMiniCard',
  };
</script>
<style scoped lang="scss">
  .sports-mini-card {
    @include use-active-style();
  }
  .sports-mini-card__anchor--info {
    border-radius: 16px 16px 0 0;
    background: linear-gradient(180deg, rgba(8, 43, 97, 0) 0%, #072b60 100%);
    overflow: hidden;
    padding: 0 16px;
    font: 400 24px / normal 'PingFang SC';
    letter-spacing: -0.816px;
    color: #f5f5f5;
    white-space: nowrap;
  }
  .sports-mini-card__team {
    font: 400 24px / normal 'PingFang SC';
    color: #37373d;
  }
  .sports-mini-card__name {
    font: 400 22px / normal 'PingFang SC';
    padding: 0 15px 10px 16px;
    color: #fff;
    background: linear-gradient(180deg, rgba(0, 0, 0, 0) 0%, rgba(0, 0, 0, 0.8) 100%);
    width: 100%;
  }
  .sports-mini-card__name--text {
    max-width: calc(100% - 90px);
  }
  .sports-mini-card__name--hot {
    font: 400 22px / normal 'MiSans';
  }
  .sports-mini-card__anchor--status {
    border-radius: 66px;
    background: #34a853;
    box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
    padding: 8px 16px;
    display: inline-block;
    height: 50px;
    font: 500 24px / normal 'PingFang SC';
    color: #fff;
    &.close {
      background: #8a91a0;
    }
  }
  .sports-mini-card__name--hot-icon {
    display: inline-block;
    width: 16px;
    height: 18px;
    margin-right: 6px;
    margin-bottom: 2px;
    background: url('@/assets/img/sportsCard/icon_hot.png') no-repeat, transparent;
    background-size: 100% 100%;
    background-position: 0 0;
  }
  .sports-mini-card__anchor--text {
    font: 400 22px / normal 'PingFang SC';
    color: #94999f;
  }
  .sports-mini-card.basketball {
    .sports-mini-card__team {
      flex-flow: row-reverse;
      justify-content: flex-end;
    }
  }
</style>
