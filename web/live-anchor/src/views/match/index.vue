<script setup lang="ts">
import { ref, reactive, toRefs } from "vue";
import dayjs from "dayjs";
import {
  matchList,
  liveOpen,
  liveClose,
  liveUpdateAddress,
  matchCompetitions,
  hotList
} from "@/api/match";

import { useNav } from "@/layout/hooks/useNav";
import MatchOpenInfo from "./components/match-open-info.vue";
import topBanner from "@/assets/match/match-top.png";
import closeIcon from "@/assets/common/close-white-icon.png";
import basketballIcon from "@/assets/match/basketball-icon.png";
import footerballIcon from "@/assets/match/footerball-icon.png";
import livingIcon from "@/assets/match/living.gif";
import closeLiveIcon from "@/assets/common/close-gray-icon.png";
import defaultIcon from "@/assets/common/default-team-logo.png";
import errorIcon from "@/assets/common/error-icon.png";
import aDownIcon from "@/assets/match/a-down-icon.png";
import callendarIcon from "@/assets/match/callendar-icon.png";

const showDrawer = ref(false);
const { username } = useNav();
const matchOpenInfoRef = ref<typeof MatchOpenInfo>(null);
const footerRotate = ref("");
const basketRotate = ref("");
const postList = ref([]);
const total = ref(0);
const showDialog = ref(false);
const isOpening = ref(false);
const showCloseLive = ref(false);
const matchStatus = ref(0);
const datePickerRef = ref();
const currentItem = ref({
  playUrl: "",
  homeTeamLogo: "",
  homeTeamName: "",
  awayTeamLog: "",
  awayTeamName: "",
  matchType: 1,
  competitionName: "",
  matchTime: "",
  matchId: ""
});
const status = { 1: "未开始", 2: "已开始" };
const matchStatusList = [
  { value: 0, label: "全部" },
  { value: 1, label: "未开始" },
  { value: 2, label: "已开始" }
];
const competitionType = ref(1);
const activeName = ref(1);
const currentData = dayjs(new Date()).format("YYYYMMDD");
const matchDate = ref(currentData);
const dateObjs = ref({});
const dateHotObjs = ref({});
const showError = ref(false);
const data = reactive({
  queryParams: {
    matchType: 1,
    matchDate: currentData,
    current: 1,
    size: 10,
    matchStatus: matchStatus.value,
    competitionIds: []
  }
});
const { queryParams } = toRefs(data);

defineOptions({
  name: "Match"
});

const loading = ref<boolean>(true);

/** 查询岗位列表 */
function getList() {
  loading.value = true;
  matchList(queryParams.value).then((res: any) => {
    loading.value = false;
    const { records } = res;
    postList.value = records;
    total.value = res.total;
  });
}

function cancel() {
  showDialog.value = false;
}
function onConfirm() {
  const { matchType, matchId, playUrl } = currentItem.value;
  if (!playUrl) {
    showError.value = true;
    return;
  }
  const success = () => {
    cancel();
    getList();
  };
  const openInfoId =
    matchOpenInfoRef.value && matchOpenInfoRef.value.getSelect();
  if (isOpening.value) {
    liveUpdateAddress({ playUrl }).then(success);
    return;
  }
  liveOpen({
    matchType: matchType || activeName.value,
    matchId,
    playUrl,
    openInfoId
  }).then(success);
}

function onPageChange(current) {
  queryParams.value.current = current;
  getList();
}
function formateTime(time) {
  return dayjs(parseFloat(time * 1000)).format("YYYY-MM-DD HH:mm");
}
function firstCloseLive() {
  showCloseLive.value = true;
}

function closeLive() {
  liveClose({}).then(() => {
    getList();
    cancleLive();
    cancel();
  });
}
function cancleLive() {
  showCloseLive.value = false;
}
function openLive(item, isOpen) {
  showError.value = false;
  if (item.liveStatus == 2 && !isOpen) {
    showCloseLive.value = true;
    return;
  }
  currentItem.value = item;
  isOpening.value = isOpen;
  showDialog.value = true;
  console.log("item", item);
}
function onRefresh() {
  queryParams.value.current = 1;
  getList();
}
function onHidePop() {
  handleClose();
}
function onSearchPop() {
  onRefresh();
  onHidePop();
}

function onChangeTab(value) {
  activeName.value = value;
  queryParams.value.matchType = value;
  onRefresh();
}
function onChangeStatus(value) {
  matchStatus.value = value;
  queryParams.value.matchStatus = value;
  onRefresh();
}
function onChangeDate() {
  queryParams.value.matchDate = dayjs(matchDate.value).format("YYYYMMDD");
  onRefresh();
}
function getCompetition() {
  const { matchType, matchDate } = queryParams.value;
  dateObjs.value = {};
  matchCompetitions({
    matchType,
    matchTime: dayjs(dayjs(matchDate).toDate()).format("YYYY-MM-DD HH:mm:ss"),
    noProgress: true
  }).then((res: any) => {
    const collator = new Intl.Collator("en");
    res.sort(function (a, b) {
      return collator.compare(a.shortName, b.shortName);
    });
    res &&
      res.map(item => {
        if (dateObjs.value[item.shortName]) {
          dateObjs.value[item.shortName].list.push(item);
        } else {
          dateObjs.value[item.shortName] = { list: [item] };
        }
      });
    showDrawer.value = true;
  });
}
function getHotCoppetitions() {
  dateHotObjs.value = {};
  const { matchType } = queryParams.value;
  hotList({ matchType, noProgress: true }).then((res: any) => {
    const collator = new Intl.Collator("en");
    res.sort(function (a, b) {
      return collator.compare(a.shortName, b.shortName);
    });
    res &&
      res.map(item => {
        item.shortName = item.shortName || "热门赛事";
        if (dateHotObjs.value[item.shortName]) {
          dateHotObjs.value[item.shortName].list.push(item);
        } else {
          dateHotObjs.value[item.shortName] = { list: [item] };
        }
      });
  });
}
function onChangeComType(value) {
  competitionType.value = value;
}
function isSelected(id) {
  return !!queryParams.value.competitionIds.find(item => item == id);
}
function onSelectCompId(id) {
  const findIndex = queryParams.value.competitionIds.findIndex(
    item => item == id
  );
  if (findIndex != -1) {
    queryParams.value.competitionIds.splice(findIndex, 1);
  } else {
    queryParams.value.competitionIds.push(id);
  }
}
function footerEnter() {
  footerRotate.value = "trans-icon-enter";
}
function footerLeave() {
  footerRotate.value = "trans-icon-leave";
}
function basketEnter() {
  basketRotate.value = "trans-icon-enter";
}
function basketLeave() {
  basketRotate.value = "trans-icon-leave";
}
function getMatchSelect() {
  getHotCoppetitions();
  getCompetition();
}
function onShowDate() {
  datePickerRef.value.focus();
}
const tableRowClassName = ({ row }) => {
  if (row.liveStatus === 2) {
    return "living-bg";
  }
  return "";
};

const handleClose = () => {
  showDrawer.value = false;
};
getList();
getHotCoppetitions();
</script>

<template>
  <div class="match pb-[40px]">
    <div class="relative flex h-[201px] p-[24px] line-clamp-2">
      <img
        class="bg-contain mb-[19px] absolute left-0 top-0"
        :src="topBanner"
      />
      <span
        class="text-[#FEFDFE] leading-[64px] max-h-[140px] line-clamp-2 text-[44px] font-medium z-[1]"
        >{{ username }}</span
      >
    </div>

    <div
      class="w-full bg-white px-[26px] rounded-tl-[8px] rounded-tr-[8px] mt-[24px]"
    >
      <div
        class="flex border-b-[#EBEBEB] border-b-[1px] items-center pt-[23px]"
      >
        <div
          @click="onChangeTab(1)"
          :class="`flex items-center text-[18px] pb-[10.5px] cursor-pointer ${
            activeName == 1
              ? 'text-[#34A853] font-semibold border-b-[2px] border-b-[#34A853]'
              : 'text-[#676666]'
          }`"
        >
          <img
            @mouseenter="footerEnter"
            @mouseleave="footerLeave"
            :class="`w-[20px] h-[20px] mr-[5px] ${footerRotate}`"
            :src="footerballIcon"
          />
          足球赛事
        </div>
        <div class="w-[1px] h-[14px] bg-[#ebebeb] mx-[24px]" />
        <div
          @click="onChangeTab(2)"
          :class="`flex items-center text-[18px] pb-[10.5px] cursor-pointer ${
            activeName == 2
              ? 'text-[#34A853] font-semibold border-b-[2px] border-b-[#34A853]'
              : 'text-[#676666]'
          }`"
        >
          <img
            @mouseenter="basketEnter"
            @mouseleave="basketLeave"
            :class="`w-[20px] h-[20px] mr-[5px] bg-contain ${basketRotate}`"
            :src="basketballIcon"
          />
          篮球赛事
        </div>
      </div>
    </div>
    <div class="py-[18px] justify-between flex items-center bg-white px-[26px]">
      <div class="flex items-center">
        <span class="text-[#666] font-medium">比赛状态：</span>
        <div
          :class="`mr-[12px]  cursor-pointer font-medium flex items-center justify-center h-[32px] w-[72px] ${
            matchStatus == item.value
              ? 'active-select  rounded-[4px] text-[#34A853]  text-[16px] '
              : 'text-[14px] text-[#858485] font-normal'
          }`"
          v-for="(item, index) in matchStatusList"
          :key="index"
          @click="onChangeStatus(item.value)"
        >
          {{ item.label }}
        </div>
      </div>
      <div class="flex items-center">
        <div
          class="w-[150px] mr-[29px] cursor-pointer relative"
          @click="onShowDate"
        >
          <img
            :src="aDownIcon"
            class="right-0 absolute top-[6px] w-[20px] h-[20px] z-10"
          />
          <img
            :src="callendarIcon"
            class="left-[11px] absolute top-[6px] w-[20px] h-[20px] z-10"
          />
          <el-date-picker
            ref="datePickerRef"
            v-model="matchDate"
            type="date"
            placeholder="请选择日期"
            format="YYYY年MM月DD日"
            @change="onChangeDate"
            size="large"
            :clearable="false"
            prefix-icon="prefix-icon"
          />
        </div>
        <div
          @click="getMatchSelect"
          class="w-[99px] cursor-pointer text-[#666665] h-[31px] flex items-center font-medium text-[12px] bg-[#F5F5F8] rounded-[4px]"
        >
          <span class="ml-[16px]">赛事筛选</span>
          <img :src="aDownIcon" class="w-[20px] ml-[6px] h-[20px]" />
        </div>
      </div>
    </div>

    <div class="flex flex-col bg-white">
      <el-table
        v-loading="loading"
        :data="postList"
        :row-class-name="tableRowClassName"
        row-key="matchId"
      >
        <el-table-column
          label-class-name="first-label-class"
          label="赛事"
          width="180px"
          prop="competitionName"
        >
          <template #default="scope">
            <div class="flex items-center relative pl-2">
              <span v-if="scope.row.liveStatus === 2" class="live-dot" />
              <span class="ml-4">{{ scope.row.competitionName }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          label-class-name="common-label-class"
          label="时间"
          align="center"
          prop="matchTime"
        >
          <template #default="scope">
            <span>{{ formateTime(scope.row.matchTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label-class-name="common-label-class"
          label="状态"
          align="center"
          prop="liveStatus"
        >
          <template #default="scope">
            <span>{{
              status[scope.row.matchStatus || 1] || "已完赛"
            }}</span></template
          >
        </el-table-column>
        <el-table-column
          label-class-name="common-label-class"
          label="对阵"
          align="center"
          prop="email"
          width="400px"
        >
          <template #default="scope">
            <div class="flex items-center text-[#666]">
              <div class="w-[74px] text-left mr-[12px] flex-shrink-0 truncate">
                <span class="w-[74px] truncate">{{
                  scope.row.homeTeamName
                }}</span>
              </div>
              <div class="w-[56px] h-[56px] flex self-center flex-shrink-0">
                <img
                  class="object-contain w-full h-full rounded-full"
                  :src="scope.row.homeTeamLogo || defaultIcon"
                />
              </div>
              <div
                class="ml-[27px] text-[#595959] text-[12px] font-medium w-[62px] rounded-[34px] flex-shrink-0 h-[30px] flex items-center justify-center bg-opacity-[0.04] bg-[#008C26]"
              >
                VS
              </div>
              <div
                class="w-[56px] ml-[27px] h-[56px] flex self-center flex-shrink-0"
              >
                <img
                  class="object-contain w-full h-full rounded-full"
                  :src="scope.row.awayTeamLog || defaultIcon"
                />
              </div>
              <div class="w-[74px] flex-shrink-0 truncate text-left pl-[18px]">
                <span class="w-[74px] truncate">{{
                  scope.row.awayTeamName
                }}</span>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          label-class-name="end-label-class"
          label="直播"
          prop="openLive"
          width="214px"
          ><template #default="scope">
            <div class="flex items-center justify-center">
              <div
                class="w-[80px] h-[32px] mr-[16px]"
                v-if="scope.row.liveStatus == 2"
              >
                <img
                  @click="openLive(scope.row, true)"
                  class="w-[80px] h-[32px] mr-[16px] cursor-pointer"
                  :src="livingIcon"
                />
              </div>
              <div class="w-[80px] h-[32px] mr-[16px]" v-else />
              <div
                @click="openLive(scope.row, false)"
                class="w-[76px] cursor-pointer hover:bg-[#34A853] hover:text-[#f5f5f5] h-[32px] text-[#666] rounded-[4px] flex items-center justify-center border-[1px] border-[#E2E2E2]"
              >
                {{ scope.row.liveStatus == 2 ? "关播" : "开播" }}
              </div>
            </div>
          </template></el-table-column
        >
      </el-table>
    </div>
    <el-pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.current"
      v-model:limit="queryParams.size"
      background
      class="flex items-center justify-center h-10 mt-3"
      @current-change="onPageChange"
    />
    <el-dialog
      v-model="showDialog"
      class="open-live-modal"
      width="601px"
      append-to-body
      :show-close="false"
    >
      <template #header>
        <div
          :class="`dialog-header-${activeName} flex items-center justify-between`"
        >
          <div class="flex items-center text-[#F5F5F5]">
            <img
              class="w-[25px] h-[25px] bg-contian ml-[33px]"
              :src="
                currentItem.matchType == 1 ? footerballIcon : basketballIcon
              "
            />
            <span class="text-[20px] font-medium ml-[6px] mr-[16px]">{{
              currentItem.competitionName
            }}</span>
            <span class="text-[16px]">
              {{ formateTime(currentItem.matchTime) }}</span
            >
          </div>
          <img
            :src="closeIcon"
            @click="cancel"
            class="w-[16px] cursor-pointer h-[16px] bg-contain z-40 mr-[23px]"
          />
        </div>
      </template>
      <div class="dialog-body">
        <div class="flex flex-col mt-[-14px] ml-[77px] items-center">
          <div class="team-logo rounded-full">
            <img
              class="w-[52px] h-[52px] bg-contain rounded-full"
              :src="currentItem.homeTeamLogo || defaultIcon"
            />
          </div>
          <span class="mt-[8px] text-[18px] text-white">{{
            currentItem.homeTeamName
          }}</span>
        </div>
        <div class="flex-grow flex ml-[-20px] justify-center">
          <div
            class="vs-item flex items-center justify-center text-[40px] font-medium text-white"
          >
            VS
          </div>
        </div>
        <div class="flex flex-col mt-[-14px] mr-[77px] items-center">
          <div class="team-logo rounded-full">
            <img
              class="w-[52px] h-[52px] bg-contain rounded-full"
              :src="currentItem.awayTeamLog || defaultIcon"
            />
          </div>
          <span class="mt-[8px] text-[18px] text-white">{{
            currentItem.awayTeamName
          }}</span>
        </div>
      </div>
      <div class="w-[601px] ml-[-20px] items-center bg-white flex flex-col">
        <div class="flex-item w-[601px] items-center justify-center mt-[17px]">
          <MatchOpenInfo ref="matchOpenInfoRef" :disabled="isOpening" />
        </div>
        <div class="flex-item w-[601px] items-center justify-center mt-[17px]">
          <span
            class="w-[94px] font-medium text-[14px] rounded-tl-[4px] rounded-bl-[4px] border-[1px] text-[#262626] border-[#D0D5DD] h-[50px] bg-[#f7f7f7] flex items-center justify-center"
            >推流地址</span
          >
          <el-input
            class="h-[50px]"
            placeholder="请填写推流地址"
            v-model="currentItem.playUrl"
          />
        </div>
        <div
          class="flex w-full pl-[72px] pt-[9px] items-center text-[14px] text-[#FE4848]"
          v-if="showError"
        >
          <img :src="errorIcon" class="w-[20px] h-[20px]" />
          请填写推流地址后开启直播
        </div>
        <div
          class="w-[457px] cursor-pointer mt-[20px] h-[50px] rounded-[8px] open-live-btn font-medium text-[16px] flex items-center justify-center text-[#F5F5F5]"
          @click="onConfirm"
        >
          {{ isOpening ? "更新地址" : "开启直播" }}
        </div>
        <div
          v-if="isOpening"
          class="w-[457px] mt-[16px] cursor-pointer h-[50px] rounded-[8px] bg-[#EBEBEB] font-medium text-[16px] flex items-center justify-center text-[#434343]"
          @click="firstCloseLive"
        >
          关闭直播
        </div>
      </div>
    </el-dialog>
    <el-dialog
      v-model="showCloseLive"
      width="504px"
      height="188px"
      append-to-body
      title="确定关闭直播间？"
      class="close-live-dialog"
      :show-close="false"
    >
      <img
        @click="cancleLive"
        class="w-[16px] cursor-pointer absolute h-[16px] bg-contain right-[16px] top-[16px]"
        :src="closeLiveIcon"
      />
      <div class="flex items-center justify-center text-[16px] font-medium">
        <div
          @click="closeLive"
          class="bg-[#34A853] text-[#F5F5F5] rounded-[8px] mr-[24px] px-[54px] py-[13px] cursor-pointer"
        >
          确定
        </div>
        <div
          @click="cancleLive"
          class="bg-white rounded-[8px] border-[1px] px-[54px] py-[13px] border-[#D0D5DD] cursor-pointer"
        >
          取消
        </div>
      </div>
    </el-dialog>
    <el-drawer
      v-model="showDrawer"
      direction="rtl"
      :with-header="false"
      :before-close="handleClose"
      wrap-class="drawer-item"
      :size="582"
    >
      <div class="flex flex-col bg-[#F0F0F0]">
        <div
          class="flex items-center h-[72px] bg-white pl-[49px] border-[1px] border-[#EBEBEB]"
        >
          <span class="text-[#1A1A1A] text-[22px] font-semibold">赛事筛选</span>
          <span class="ml-[8px] flex-grow text-[#999] text-[16px]"
            >请选择所需赛事</span
          >
          <img
            @click="handleClose"
            class="w-[24px] h-[24px] bg-contain mr-[23px] cursor-pointer"
            src="@/assets/match/close-drawer.png"
          />
        </div>
        <div
          class="flex items-center pl-[61px] cursor-pointer h-[61px] pt-[23.5px]"
        >
          <div class="flex items-center flex-col">
            <span
              @click="onChangeComType(1)"
              :class="`font-medium  w-[61px] mb-[9.5px] text-[18px]  flex items-center justify-center ${
                competitionType == 1
                  ? 'text-[#34A853] font-medium '
                  : ' text-[#676666] font-normal'
              }`"
              >全部</span
            >
            <span
              v-if="competitionType == 1"
              class="w-[61px] min-h-[2px] bg-[#34A853]"
            />
          </div>
          <span class="w-[1px] h-[13.5px] mt-[-10px] bg-[#D0D5DD] mx-[32px]" />
          <div class="flex items-center flex-col justify-center">
            <span
              @click="onChangeComType(2)"
              :class="`font-medium mb-[9.5px] text-[18px] flex items-center justify-center ${
                competitionType == 2
                  ? 'text-[#34A853] font-medium '
                  : ' text-[#676666] font-normal'
              }`"
              >热门赛事</span
            >
            <span
              v-if="competitionType == 2"
              class="w-[61px] min-h-[2px] ml-[4px] bg-[#34A853]"
            />
          </div>
        </div>
        <div class="select-list overflow-y-scroll">
          <div
            class="flex flex-col"
            v-for="(val, key, i) in competitionType == 1
              ? dateObjs
              : dateHotObjs"
            :key="i"
          >
            <span
              class="w-full pl-[20px] flex bg-[#F8F9FB] items-center h-[52px]"
              >{{ key }}</span
            >
            <div
              class="flex items-center flex-wrap pt-[7.5px] pl-[20px] bg-white"
            >
              <div
                :class="`py-[4px] min-w-[190px] cursor-pointer mb-[16px] flex items-center justify-center  mr-[24px]  flex-shrink-0 rounded-[4px]  ${
                  isSelected(item.competitionId)
                    ? 'bg-[#34A853] border-[1px] border-[#34A853] text-white font-medium'
                    : 'text-[#434343] border-[1px] border-[#D0D5DD]'
                }`"
                v-for="(item, index) in val.list"
                :key="index"
                @click="onSelectCompId(item.competitionId)"
              >
                {{ item.fullCompetitionName || item.competitionName }}
              </div>
            </div>
          </div>
        </div>
        <div
          class="h-[70px] border-t-[1px] border-t-[#EBEBEB] w-full text-[16px] font-medium flex items-center justify-end bg-white rounded-bl-[16px] rounded-br-[16px]"
        >
          <div
            @click="onHidePop"
            class="cursor-pointer hover:border-[#4f4f4f] hover:border-[1px] text-[#1A1A1A] flex items-center justify-center w-[90px] h-[40px] bg-white rounded-[8px] border-[1px] border-[#d0d5dd]"
          >
            取消
          </div>
          <div
            @click="onSearchPop"
            class="bg-[#34A853] hover:bg-[#128831] cursor-pointer flex items-center justify-center text-white rouned-[8px] w-[90px] h-[40px] rounded-[8px] ml-[16px] mr-[36px]"
          >
            确定
          </div>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<style lang="scss">
@keyframes aniro {
  from {
    transform: rotateZ(0deg);
  }

  to {
    transform: rotateZ(360deg);
  }
}

@keyframes aniro-leave {
  from {
    transform: rotateZ(0deg);
  }

  to {
    transform: rotateZ(-360deg);
  }
}

@keyframes animations {
  0% {
    opacity: 1;
  }

  100% {
    opacity: 0;
  }
}

.active-select {
  background: rgb(52 168 83 / 10%);
  border: 1px solid #34a853;
}

.el-table .living-bg {
  --el-table-tr-bg-color: #f1f7f3;
}

.el-drawer__body {
  padding: 0 !important;
}

.select-list {
  height: calc(100vh - 210px);
}

.close-live-dialog {
  position: relative;
  background: white;
  border-radius: 16px;

  .el-dialog__header {
    display: flex;
    align-items: center;
    justify-content: center;
    font-family: "PingFang SC";
    font-size: 22px;
    font-style: normal;
    font-weight: 600;
    line-height: 24px;
    color: #1a1a1a;
  }
}

.open-live-modal {
  background: transparent;

  .flex-item {
    display: flex;
    align-items: center;
  }

  .el-input__wrapper {
    flex-grow: 0;
    width: 360px;
    box-shadow: none;
  }

  .el-input {
    width: 364px !important;
    border-top: 1px solid #d0d5dd;
    border-right: 1px solid #d0d5dd;
    border-bottom: 1px solid #d0d5dd;
    border-radius: 0 4px 4px 0;
  }

  .dialog-header-2 {
    position: relative;
    width: 601px;
    height: 40px;
    padding: 0;
    margin-top: -20px;
    margin-left: -20px;
    background: url("../../assets/match/basketball-header.png") no-repeat center;
    background-size: cover;
    border-radius: 16px 16px 0 0;
    opacity: 1;
  }

  .dialog-header-1 {
    position: relative;
    width: 601px;
    height: 40px;
    padding: 0;
    margin-top: -20px;
    margin-left: -20px;
    background: url("../../assets/match/footerball-header.png") no-repeat center;
    background-size: cover;
    border-radius: 16px 16px 0 0;
    opacity: 1;
  }

  .el-dialog__body {
    background: white !important;
    border-radius: 0 0 16px 16px;
  }

  .dialog-body {
    display: flex;
    align-items: center;
    width: 601px;
    height: 156px;
    margin-top: -40px;
    margin-left: -20px;
    background: url("../../assets/match/live-body.png") no-repeat center;
    background-size: cover;

    .team-logo {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 72px;
      height: 72px;
      background: url("../../assets/match/team-logo-bk.png") no-repeat center;

      .logo {
        width: 52px;
        height: 52px;
        background-size: contain;
      }
    }

    .vs-item {
      width: 102px;
      height: 50px;
      background: rgb(255 255 255 / 30%);
      backdrop-filter: blur(3px);
      border-radius: 43px;
    }
  }
}

.el-table thead {
  --el-table-header-bg-color: #fafafa;
  --el-table-border: 1px solid #ebebeb;

  height: 54px;
}

.el-table .cell {
  padding: 0 !important;
}

.common-label-class {
  .cell {
    border-right: 1px solid #ebebeb !important;
  }
}

.end-label-class {
  position: relative;
  padding-left: 40px !important;

  .cell {
    position: absolute;
    top: 18px;
    right: 48px;
    border-right: none !important;
  }
}

.first-label-class {
  padding-left: 26px !important;

  .cell {
    border-right: 1px solid #ebebeb !important;
  }
}

// .el-table__header {
//   background: red !important;
// }
// .el-table tr {
//   background: red !important;
// }
.live-dot {
  position: absolute;
  left: 12px;
  width: 6px;
  height: 6px;
  background: #ffb55d;
  border-radius: 50%;
  animation: animations 1s linear infinite;
}

.pop-tooltip {
  padding: 0 !important;
  margin: 0 !important;
  background-color: #f0f0f0 !important;
}

.open-live-btn {
  background: url("../../assets/match/open-live-bg.svg");
}

.trans-icon-enter {
  animation-name: aniro;
  animation-duration: 1s;
  animation-fill-mode: forwards;
}

.trans-icon-leave {
  animation-name: aniro-leave;
  animation-duration: 1s;
  animation-fill-mode: backwards;
}

.rotate-icon-enter-to,
.rotate-icon-leave {
  transform: rotateZ(0deg);
}

.rotate-icon-enter-active,
.rotate-icon-leave-active {
  transition: 1s linear;
}

.rotate-icon-enter,
.rotate-icon-leave-to {
  transform: rotateZ(360deg);
}

.prefix-icon {
  display: none;
  width: 20px;
  height: 20px;
  background: url("../../assets/match/callendar-icon.png") no-repeat center;
  background-size: contain;
}

.match {
  font-size: 14px;
  border-radius: 4px;

  .el-date-editor.el-input,
  .el-date-editor.el-input__wrapper {
    --el-input-bg-color: #f5f5f8;
    --el-input-border-color: #f5f5f8;

    width: 160px !important;
    height: 32px !important;
  }

  .el-input__inner {
    font-size: 12px !important;
    font-weight: 500 !important;
    color: #666665 !important;
  }

  .search-items {
    display: flex;
    justify-content: space-between;
    padding: 16px 12px;
    margin-bottom: 30px;
    background: white;

    .input-search {
      width: 120px;
      margin-right: 12px;
      margin-left: 4px;
    }
  }
}
</style>
