import { defineStore } from "pinia";
import { useMessage } from "@/views/message/utils/useMessage";
import { ref } from "vue";

export const useMessageStore = defineStore("app-message", () => {
  let messageServer: ReturnType<typeof useMessage> | null = null;
  const readDotAll = ref(0);
  const createMessageServer = () => {
    messageServer = useMessage();
    messageServer.chatEmitter.on("readDotAll", async res => {
      const data = res.data || ({} as any);
      readDotAll.value = Number(data.totalCount || "");
    });
  };
  const getMessageServer = () => {
    return messageServer;
  };
  const destroyMessageServer = () => {
    messageServer?.destroy();
    messageServer = null;
    readDotAll.value = 0;
  };
  return {
    createMessageServer,
    destroyMessageServer,
    getMessageServer,
    readDotAll
  };
});
