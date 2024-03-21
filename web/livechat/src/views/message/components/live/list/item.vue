<script setup lang="ts">
import type { LiveItemData } from "@/api/message/model";
import { LiveStatus } from "@/api/message/enums";
import { importAssets } from "@/utils/import";
import IconOnline from "@/components/iconOnline.vue";

interface Props {
  item: LiveItemData;
}

defineOptions({ name: "LiveCardItem" });
defineProps<Props>();
</script>

<template>
  <div
    class="message-card-box w-full flex items-center p-[14px] relative cursor-pointer rounded-[10px]"
  >
    <div class="grow-0 shrink-0 w-[50px] h-[50px] rounded-[100%]">
      <img
        class="inline-block w-full h-full rounded-[inherit] overflow-hidden"
        :src="item.avatar || importAssets('message/default_anchor.png')"
        alt="logo"
      />
    </div>
    <div
      class="flex-grow gap-[8px] flex flex-col justify-between pl-[8px] truncate"
    >
      <div class="flex items-center gap-[8px]">
        <div
          class="text-[14px] text-[#37373D] font-medium truncate max-w-[50%]"
        >
          {{ item.nick }}
        </div>
        <div
          class="flex-shrink-0 text-[12px] px-[8px] py-[4px] rounded-full inline-flex items-center gap-[2px] select-none"
          :class="[
            item.liveStatus === LiveStatus.Living
              ? 'bg-white text-[#34A853]'
              : 'bg-[#E0E3E8] text-[#94999F]'
          ]"
        >
          <template v-if="item.liveStatus === LiveStatus.Living">
            <el-icon>
              <IconOnline />
            </el-icon>
            直播中
          </template>

          <template v-else>未开播</template>
        </div>
      </div>
      <div
        v-if="item.liveStatus === LiveStatus.Living"
        class="text-[12px] text-[#94999F] truncate"
      >
        {{ item?.homeTeamName }}VS{{ item?.awayTeamName }}
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.message-card-box {
  position: relative;
  transition-timing-function: linear;
  transition-duration: 300ms;
  transition-property: background;

  &::after {
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    z-index: 1;
    display: block;
    width: 4px;
    height: 40px;
    margin: auto;
    content: "";
    background: #34a853;
    border-radius: 0 37px 30px 0;
    transition-timing-function: linear;
    transition-duration: 300ms;
    transition-property: transform;
    transform: scaleX(0);
    transform-origin: 0 50%;
  }

  &:hover,
  &.active {
    background: #e0e3ea;

    &::after {
      transform: scaleX(1);
    }
  }
}
</style>
