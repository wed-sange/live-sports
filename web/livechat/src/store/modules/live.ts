import { defineStore } from "pinia";
import { ref } from "vue";
import { LiveItemData } from "@/api/message/model";

export const useLiveStore = defineStore("live", () => {
  const currentLive = ref<LiveItemData>();

  return {
    currentLive
  };
});
