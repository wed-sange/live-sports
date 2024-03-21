import { $t } from "@/plugins/i18n";
import { list } from "@/router/enums";

export default {
  path: "/broadcast",
  meta: {
    icon: "broadcastIcon",
    title: $t("menus.broadcastInformation"),
    rank: list
  },
  children: [
    {
      path: "/broadcast/list",
      name: "BroadcastList",
      component: () => import("@/views/broadcast/index.vue"),
      meta: {
        title: $t("menus.broadcastInformation")
      }
    },
    {
      path: "/broadcast/form",
      name: "BroadcastForm",
      component: () => import("@/views/broadcast/src/form/index.vue"),
      meta: {
        title: $t("menus.broadcastFormInformation"),
        showLink: false
      }
    }
  ]
} as RouteConfigsTable;
