import { ref, provide } from "vue";
import LiveCard from "../components/list/live/index.vue";
import { type GetItemFun } from "../replay/useIndex";
export const useIndex = () => {
  const liveCardRef = ref<InstanceType<typeof LiveCard>>();
  const currentLive = ref("");
  const getItem: GetItemFun = (value: string) => {
    return liveCardRef.value?.getItem(value);
  };
  provide("mainPageGetItem", getItem);
  return {
    liveCardRef,
    currentLive
  };
};
