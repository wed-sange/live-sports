import { MessageCardData } from "@/views/message/utils/types";
import { defineStore } from "pinia";
import { Ref, ref } from "vue";

/**
 * 记录用户的未读条数
 * @type {*}
 */
export const useMessageUnReadCountStore = defineStore(
  "app-message-unreadcount",
  () => {
    const unReadRecord: Ref<Record<string, number>> = ref({} as any);
    /**
     * 设置单条条数
     *
     * @author
     * @param userId
     * @param count
     */
    const setCount = (userId, count) => {
      unReadRecord.value[userId] = count;
    };
    /**
     * 初始化条数
     *
     * @author
     * @param value
     */
    const initCount = (list: MessageCardData[]) => {
      unReadRecord.value = {} as any;
      list.forEach(item => {
        unReadRecord.value[item.sendUserId] = item.readDot;
      });
    };

    return {
      setCount,
      initCount,
      unReadRecord
    };
  }
);
