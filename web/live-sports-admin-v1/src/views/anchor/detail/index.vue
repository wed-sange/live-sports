<template>
  <div class="-detail">
    <div class="header-item">
      <div class="flex flex-row-reverse w-full">
        <el-button type="primary" class="ml-3" @click="goBack()"
          >返回</el-button
        >
      </div>
    </div>
    <div class="flex flex-col bg-white p-3">
      <span class="font-bold">基本信息</span>
      <div class="info-items">
        <div class="basic-info-item" v-for="(k, v) in infos" v-bind:key="v">
          <span style="flex-shrink: 0">{{ k }}：</span>
          <div v-if="v == 'notice'" v-html="result[v]"></div>
          <span v-else-if="v != 'head'">{{ result[v] }}</span>
          <img
            v-else
            :src="
              result[v] ||
              'https://d3ty9kq80i16x4.cloudfront.net/common-image/20231129/d-mrbGppcb0uls_i5RpDv.jpg'
            "
            class="avatar-item"
          />
        </div>
      </div>
    </div>
    <div class="flex flex-col bg-white p-3 mt-4">
      <div class="flex justify-between">
        <span class="font-bold pb-3 border-b-[#eaeaea] border-b-[0.06rem]"
          >直播统计</span
        >
        <el-radio-group
          v-model="searchType"
          style="margin: 6px"
          @change="getStaticList"
        >
          <el-radio-button :label="1">本月</el-radio-button>
          <el-radio-button :label="2">近三月</el-radio-button>
          <el-radio-button :label="3">全部</el-radio-button>
        </el-radio-group>
      </div>

      <div class="info-items">
        <div
          class="info-item w-[25%] mb-6"
          v-for="(k, v) in liveStatic"
          v-bind:key="v"
        >
          <span>{{ k }}：</span>
          <span>{{ liveStatics[v] }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import router from "@/router";
import dayjs from "dayjs";
import { getAnchorStatistics, getAnchorDetail } from "@/api/anchor/index";

export default {
  name: "AnchorDetail",
  data() {
    return {
      infos: {
        nickName: "主播昵称",
        head: "头像",
        account: "账号",
        createTime: "创建时间",
        notice: "主播公告",
      },
      liveStatic: {
        totalLiveCount: "总开播次数",
        totalLiveHours: "直播时长",
        maxLiveUserCount: "单次最高用户数",
        totalLiveUsers: "累计用户数",
      },
      result: {
        ynForbidden: 0,
      },
      liveStatics: {},
      searchType: 1,
      id: "",
    };
  },
  created() {
    const { query } = router.currentRoute;
    this.id = query.id;
    this.getDetail();
  },
  methods: {
    getDetail() {
      this.loading = true;
      getAnchorDetail(this.id).then((res) => {
        const data = res.data;
        if (data && data.createTime) {
          data.createTime = dayjs(data.createTime).format(
            "YYYY-MM-DD HH:mm:ss"
          );
        }
        this.result = data;
      });
      this.getStaticList();
    },
    getStaticList() {
      getAnchorStatistics(this.id, this.searchType).then((res) => {
        this.liveStatics = res.data;
        this.loading = false;
      });
    },
    goBack() {
      this.$router.push("/anchor/list");
    },
  },
};
</script>

<style scoped lang="scss">
.anchor-detail {
  background: #f5f5f5;
  height: 100vh;

  .header-item {
    padding: 12px;
    background: white;
    margin-bottom: 12px;
    margin-top: 12px;
  }

  .info-items {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    width: 100%;
    margin-top: 12px;
    color: #666;
    font-size: 15px;

    .basic-info-item {
      display: flex;
      align-items: center;
      margin-bottom: 12px;
      width: 30%;

      .avatar-item {
        max-width: 64px;
        height: 64px;
        background-size: contain;
        border-radius: 32px;
      }
    }

    .info-item {
      display: flex;
      margin-bottom: 12px;
      width: 50%;
    }
  }

  .basic-item {
    display: flex;
    flex-direction: column;
    background: white;
    border-radius: 8px;
    padding: 12px;
    margin: 20px;
    font-size: 16px;

    .font-bold {
      font-weight: bold;
      color: #333;
    }
  }
}
</style>
