import { $t } from "@/plugins/i18n";
import { list } from "@/router/enums";
const Layout = () => import("@/layout/index.vue");

export default {
  path: "/",
  name: "Home",
  component: Layout,
  redirect: "/message/live",
  meta: {
    icon: "msgIcon",
    title: $t("menus.informationCenter"),
    rank: list
  },
  children: [
    {
      path: "/message/live",
      name: "MessageLive",
      component: () => import("@/views/message/live/index.vue"),
      meta: {
        title: $t("menus.liveBroadcastRoomNews")
      }
    },
    {
      path: "/message/replay",
      name: "Replay",
      component: () => import("@/views/message/replay/index.vue"),
      meta: {
        title: $t("menus.privateChatReply")
      }
    }
  ]
} as RouteConfigsTable;
