<template>
  <div
    class="broadcast-box w-full h-[calc(100vh-78px)] mx-0 px-[18px] pb-[24px]"
  >
    <div class="broadcast-inner-box w-full h-full" v-loading="loading">
      <div class="page-header p-[24px] flex items-center">
        <div class="page-header-title">开播信息</div>
        <div class="page-header-other ml-[auto]">
          <el-button
            class="page-header-btn"
            type="primary"
            :icon="Plus"
            @click="add"
          >
            新增
          </el-button>
        </div>
      </div>
      <div class="page-body h-[calc(100%-80px)] overflow-auto">
        <div class="page-body-inner">
          <el-table class="page-table" :data="tableData">
            <template v-for="item in tableColumns" :key="item.prop">
              <el-table-column
                :prop="item.prop"
                :label="item.label"
                :width="item.width"
                :min-width="item.minWidth"
                align="left"
              >
                <template #default="scope">
                  <template v-if="item.prop === 'cover'">
                    <div class="table-img">
                      <img :src="scope.row[item.prop]" alt="cover" />
                    </div>
                  </template>
                  <template v-else-if="item.prop === 'notice'">
                    <el-tooltip
                      effect="dark"
                      :content="scope.row[item.prop] || '-'"
                      popper-class="table-tooltip-popper"
                      placement="top"
                    >
                      <div class="table-tooltip">
                        {{ scope.row[item.prop] || "-" }}
                      </div>
                    </el-tooltip>
                  </template>
                  <template v-else>
                    {{ scope.row[item.prop] || "-" }}
                  </template>
                </template>
              </el-table-column>
            </template>
            <el-table-column
              class-name="option"
              label="操作"
              width="124"
              align="center"
              fixed="right"
            >
              <template #default="scope">
                <div class="table-option-box">
                  <!-- <el-button
                    class="table-option-btn"
                    link
                    type="primary"
                    @click="query(scope.row)"
                  >
                    查看
                  </el-button> -->
                  <el-button
                    class="table-option-btn"
                    link
                    type="primary"
                    @click="edit(scope.row)"
                  >
                    编辑
                  </el-button>
                  <el-button
                    class="table-option-btn"
                    link
                    type="primary"
                    @click="remove(scope.row)"
                  >
                    删除
                  </el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import { useUserStore } from "@/store/modules/user";
const userStore = useUserStore();
export default {
  beforeRouteEnter(to, from, next) {
    if (userStore.userInfo.neverCreateOpenInfo) {
      next({ name: "BroadcastForm" });
    } else {
      next();
    }
  }
};
</script>
<script setup lang="ts">
import dayjs from "dayjs";
import { ref } from "vue";
import { Plus } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { getBroadcastList, removeBroadcast } from "@/api/broadcast";
import { useRouter } from "vue-router";
import { extractRichText } from "@/utils/tool.kit";
defineOptions({
  name: "BroadcastList"
});
const router = useRouter();
const loading = ref(false);
type TableColumnsData = {
  prop: string;
  label: string;
  width?: number | string;
  minWidth?: number | string;
  type?: string;
};
const tableData = ref<any[]>([]);
const tableColumns = ref<TableColumnsData[]>([
  { prop: "remark", label: "备注名称", minWidth: "247px" },
  { prop: "cover", label: "封面", width: "167px" },
  { prop: "notice", label: "主播公告", minWidth: "385px" },
  { prop: "createTime", label: "创建时间", minWidth: "230px" }
]);
const initData = async () => {
  loading.value = true;
  try {
    const response = await getBroadcastList();
    const data = response || [];
    const renderData = data.map((cur, i) => {
      return {
        id: "" + cur.id, // ID
        remark: cur.remark || "",
        cover: cur.titlePage || "",
        notice: extractRichText(cur.notice || ""),
        createTime: cur.createTime
          ? dayjs(cur.createTime || "").format("YYYY/MM/DD HH:mm")
          : "" // 创建
      };
    });
    tableData.value = renderData;
  } finally {
    loading.value = false;
  }
};
initData();
const add = () => {
  if (tableData.value.length >= 10) {
    ElMessage.error("最多只能创建10条消息");
    return;
  }
  router.push({ name: "BroadcastForm" });
};
const edit = (item: any) => {
  router.push({ name: "BroadcastForm", query: { id: item.id } });
};
const remove = (item: any) => {
  ElMessageBox.confirm(
    // `确认删除备注名称: ${item.remark || "-"} 此条表单吗？`,
    `确认删除此条信息？`,
    "提示",
    {
      customClass: "custom-confirm",
      confirmButtonText: "确认",
      cancelButtonText: "取消"
    }
  )
    .then(async () => {
      loading.value = true;
      try {
        await removeBroadcast(item.id);
        ElMessage.success("删除成功");
        initData();
      } finally {
        loading.value = false;
      }
    })
    .catch(() => {});
};
</script>

<style scoped lang="scss">
.broadcast-box.main-content {
  margin: 0;

  .broadcast-inner-box {
    overflow: hidden;
    background: #fff;
    border-radius: 16px;
  }

  .page-header-title {
    font: 500 18px / normal "PingFang SC";
    color: #000;
  }

  .page-header-other {
    .page-header-btn.el-button {
      --el-button-text-color: rgb(255 255 255 / 90%);
      --el-button-bg-color: #34a853;
      --el-button-hover-bg-color: #128831;
      --el-button-active-bg-color: #128831;
      --el-border-radius-base: 2px;

      width: 102px;
      padding: 8px 0;
      border: none;

      :deep() [class*="el-icon"] + span {
        margin-left: 16px;
      }
    }
  }
  // .page-body-inner {
  //   padding-left: 26px;
  //   position: relative;
  //   &::after {
  //     content: "";
  //     display: block;
  //     background: #f7f7f9;
  //     width: 26px;
  //     height: 55px;
  //     position: absolute;
  //     left: 0;
  //     top: 0;
  //     z-index: 1;
  //     pointer-events: none;
  //   }
  // }
}

:deep(.page-table.el-table) {
  .el-table__header-wrapper {
    th.el-table__cell {
      --el-table-header-bg-color: #f7f7f9;

      padding: 16px 0;
      font: 400 14px / normal "PingFang SC";
      color: #333332;
      border: none;

      .cell {
        padding: 0 !important;
      }

      &:nth-child(1) {
        .cell {
          padding-left: 26px !important;
        }
      }
    }
  }

  .el-table__body-wrapper {
    td.el-table__cell {
      padding: 16px 0;
      font: 400 14px / normal "PingFang SC";
      color: #191e24;
      word-break: break-all;

      .cell {
        padding: 0 107px 0 0 !important;
      }

      &:nth-child(1) {
        .cell {
          padding-left: 26px !important;
        }
      }

      &.option {
        .cell {
          padding: 24px !important;
        }
      }
    }
  }

  .el-table-fixed-column--right.is-first-column::before {
    box-shadow: var(--el-table-fixed-right-column);
  }

  .table-img {
    width: 60px;
    height: 60px;
    overflow: hidden;
    border-radius: 4px;

    img {
      width: 100%;
      height: 100%;
      overflow: hidden;
      object-fit: cover;
      border: inherit;
    }
  }

  .table-tooltip {
    width: 100%;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .table-option-box {
    white-space: nowrap;
  }

  .table-option-btn.el-button {
    --el-button-text-color: #406af6;
    --el-button-hover-text-color: #406af6;
    --el-button-active-text-color: #406af6;

    font: 400 14px / normal "PingFang SC";

    & + .el-button {
      position: relative;

      &::after {
        position: absolute;
        top: 50%;
        left: -8px;
        z-index: 1;
        display: block;
        width: 1px;
        height: 8px;
        margin-top: -4px;
        pointer-events: none;
        content: "";
        background: #e6e6e6;
      }
    }
  }
}
</style>
