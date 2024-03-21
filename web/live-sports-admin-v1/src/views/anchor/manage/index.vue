<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="主播昵称" prop="name">
        <el-input
          :maxlength="30"
          v-model="queryParams.nickName"
          placeholder="请输入主播昵称"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >查询</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>
    <el-radio-group
      v-model="queryParams.liveStatus"
      style="margin-bottom: 6px"
      @change="onTypeChange"
    >
      <el-radio-button :label="2">正在直播</el-radio-button>
      <el-radio-button :label="1">暂未开播</el-radio-button>
    </el-radio-group>

    <el-table
      stripe
      v-if="queryParams.liveStatus === 2"
      v-loading="loading"
      :data="postList"
    >
      <el-table-column label="主播昵称" align="center" prop="nickName" />
      <el-table-column label="房间封面" align="center" prop="titlePage">
        <template #default="scope">
          <img
            class="max-h-24 rounded-sm bg-contain w-24"
            :src="getImgsrc(scope.row.titlePage)"
          />
        </template>
      </el-table-column>
      <el-table-column label="类型" align="center" prop="matchType">
        <template #default="scope">
          <span>{{ scope.row.matchType == 1 ? "足球" : "篮球" }}</span>
        </template>
      </el-table-column>
      <el-table-column label="联赛信息" align="center" prop="competitionName" />
      <el-table-column
        label="比赛信息"
        align="center"
        prop="matchInfo"
        width="200"
      >
        <template #default="scope">
          <span
            >{{ scope.row.homeTeamName }}vs{{ scope.row.awayTeamName }}</span
          >
        </template>
      </el-table-column>
      <el-table-column
        label="比赛时间"
        align="center"
        width="160"
        prop="matchTime"
      >
        <template #default="scope">
          <span>{{ formateTime(scope.row.matchTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="在线人数" align="center" prop="onlineUser" />
      <el-table-column
        label="操作"
        width="180"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template #default="scope">
          <div class="flex items-center w-full justify-center text-[#1890ff]">
            <el-button @click="onDetail(scope.row.userId)" type="text"
              >主播信息</el-button
            >
            <el-popconfirm
              @confirm="onCloseLive(scope.row.id)"
              title="是否关播？"
            >
              <template #reference>
                <el-button class="ml-4 red-text" type="text">关播</el-button>
              </template>
            </el-popconfirm>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <el-table v-else v-loading="loading" key="unAnchor" :data="postList" stripe>
      <el-table-column label="主播昵称" align="center" prop="nickName" />
      <el-table-column label="上次开播信息" align="center" prop="id">
        <template #default="scope">
          <div class="flex items-center w-full justify-center">
            <span v-if="scope.row.matchType == 1">{{
              scope.row.homeTeamName + "vs" + scope.row.awayTeamName
            }}</span>
            <span v-else>{{
              scope.row.awayTeamName + "vs" + scope.row.homeTeamName
            }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="上次开播时间" align="center" prop="openTime">
        <template #default="scope">
          <span>{{
            scope.row.openTime
              ? formateTimeMils(parseFloat(scope.row.openTime))
              : ""
          }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        key="anchorOption"
        class-name="small-padding fixed-width"
      >
        <template #default="scope">
          <div class="flex items-center w-full justify-center">
            <el-button @click="onDetail(scope.row.userId)" type="text"
              >主播信息</el-button
            >
          </div>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.current"
      :limit.sync="queryParams.size"
      @pagination="getList"
    />
  </div>
</template>

<script>
import dayjs from "dayjs";
import defalutImg from "../../../assets/images/default-img.png";
import { liveUsers, closeLive } from "@/api/anchor/index";

export default {
  name: "AnchorManage",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 用户表格数据
      postList: null,
      // 表单参数
      form: {},
      // 查询参数
      queryParams: {
        current: 1,
        size: 10,
        nickName: "",
        liveStatus: 2,
      },
      showDialog: false,
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getImgsrc(img) {
      if (!img || img.indexOf("http") == -1) {
        return defalutImg;
      }
      return img;
    },
    formateTime(time) {
      return dayjs(parseFloat(time * 1000)).format("YYYY-MM-DD HH:mm");
    },
    formateTimeMils(time) {
      return dayjs(parseFloat(time)).format("YYYY-MM-DD HH:mm");
    },
    onTypeChange() {
      this.onReset();
    },
    onReset() {
      this.reset();
      this.handleQuery();
    },
    //查看用户详情
    onDetail(id) {
      this.$router.push({ path: `/anchor/details`, query: { id } });
    },
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      liveUsers(this.queryParams).then((response) => {
        const { records, total } = response.data;
        this.postList = records;
        this.total = total;
        this.loading = false;
      });
    },
    onCloseLive(id) {
      closeLive(id).then(() => {
        this.$message.success("已关播");
        this.getList();
      });
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        name: undefined,
        account: "",
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.current = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
  },
};
</script>
<style lang="scss" scoped>
.flex-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;

  .dialog-label {
    width: 60px;
  }

  .input-dialog {
    width: 120px;
    margin-left: 1rem;
  }
}
</style>
