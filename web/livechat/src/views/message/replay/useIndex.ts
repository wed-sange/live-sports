import { ref, provide, computed, watch } from "vue";
import AnchorCard from "../components/list/anchor/index.vue";
import UserCard from "../components/list/user/index.vue";
import { MessageCardData, MessageCardType } from "@/views/message/utils/types";
import { buildMessageItem } from "@/views/message/utils/message";
import { useUserStore } from "@/store/modules/user";
export type GetItemFun = (
  value: string,
  card: "anchor" | "user"
) => MessageCardData;
export const useIndex = () => {
  const userStore = useUserStore();
  const userInfo = computed(() => {
    return userStore.userInfo;
  });
  const anchorCardRef = ref<InstanceType<typeof AnchorCard>>();
  const userCardRef = ref<InstanceType<typeof UserCard>>();
  const currentAnchor = ref("");
  const currentUser = ref("");
  const getItem: GetItemFun = (value, card = "anchor") => {
    if (card === "anchor") {
      if (userInfo.value.identityType === 1) {
        return buildMessageItem({
          id: userInfo.value.id,
          sendUserId: "",
          anchorId: userInfo.value.id,
          name: userInfo.value.nickname,
          cover: userInfo.value.avatar,
          message: "",
          readDot: 0,
          type: MessageCardType.MESSAGE
        });
      } else {
        return anchorCardRef.value?.getItem(value);
      }
    } else {
      return userCardRef.value?.getItem(value);
    }
  };
  watch(
    () => userInfo.value.identityType,
    newValue => {
      if (newValue === 1) {
        currentAnchor.value = userInfo.value.id;
      }
    },
    {
      immediate: true
    }
  );
  provide("mainPageGetItem", getItem);
  return {
    anchorCardRef,
    userCardRef,
    currentAnchor,
    currentUser,
    userInfo
  };
};
