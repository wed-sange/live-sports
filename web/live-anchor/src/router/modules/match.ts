import { $t } from "@/plugins/i18n";
import { home } from "@/router/enums";
const Layout = () => import("@/layout/index.vue");

export default {
  path: "/",
  name: "Home",
  component: Layout,
  redirect: "/match/index",
  meta: {
    icon: "homeIcon",
    title: $t("menus.competitionInformation"),
    rank: home
  },
  children: [
    {
      path: "/match/index",
      name: "User",
      component: () => import("@/views/match/index.vue"),
      meta: {
        title: $t("menus.competitionInformation")
      }
    }
  ]
} as RouteConfigsTable;
