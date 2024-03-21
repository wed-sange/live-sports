<template>
  <div class="charts">
    <div class="search-items">
      <div class="query-item">
        <span>昵称</span>
        <el-input
          :maxlength="30"
          @keyup.enter.native="handleQuery"
          clearable
          class="input-search"
          placeholder="请输入昵称"
          v-model="queryParams.nick"
        />
        <span>身份</span>
        <el-select
          @change="handleQuery"
          v-model="queryParams.identityType"
          placeholder="请选择"
          class="input-search"
        >
          <el-option
            v-for="item in roles"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <span>搜索内容</span>
        <el-input
          :maxlength="30"
          @keyup.enter.native="handleQuery"
          clearable
          class="input-search"
          placeholder="请输入关键词"
          v-model="queryParams.content"
        />
      </div>
      <div>
        <el-button type="primary" @click="handleQuery">查询</el-button>
        <el-button class="ml-3" @click="onReset">重置</el-button>
      </div>
    </div>
    <div class="flex flex-col bg-white mb-3">
      <el-table v-loading="loading" :data="postList" stripe>
        <el-table-column label="发送人" align="center" prop="nick" />
        <el-table-column label="身份" align="center" prop="identityType">
          <template #default="scope">
            <span>{{ msgTypes[scope.row.identityType] }}</span>
          </template>
        </el-table-column>
        <el-table-column label="内容" align="center" prop="content">
          <template #default="scope">
            <span v-if="scope.row.msgType === 0">{{ scope.row.content }}</span>
            <img
              target="_blank"
              class="w-12 h-12"
              v-else-if="scope.row.msgType === 1"
              :src="scope.row.content"
            />
          </template>
        </el-table-column>
        <el-table-column label="发送时间" align="center" prop="createTime">
          <template #default="scope">
            <span>{{
              scope.row.createTime
                ? formateTimeMils(parseFloat(scope.row.createTime))
                : ""
            }}</span>
          </template>
        </el-table-column>
        <el-table-column label="对象昵称" align="center" prop="toName" />
      </el-table>
    </div>
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
import { getChartList } from "@/api/chat/index";
export default {
  name: "Chats",
  data() {
    return {
      postList: [],
      total: 0,
      roles: [
        { label: "全部", value: "" },
        { label: "普通用户", value: 0 },
        // { label: "普通助理", value: 2 },
        { label: "主播", value: 1 },
        { label: "直播助理", value: 3 },
      ],
      msgTypes: { 0: "普通用户", 1: "主播", 2: "助手", 3: "运营" },
      queryParams: {
        current: 1,
        size: 10,
        nick: "",
        identityType: "",
        content: "",
      },
      loading: true,
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询岗位列表 */
    getList() {
      this.loading = true;
      getChartList(this.queryParams).then((response) => {
        this.postList = response.data.records;
        this.total = parseInt(response.data.total);
        this.loading = false;
      });
    },
    onPageChange(current) {
      this.queryParams.current = current;
      this.getList();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.current = 1;
      this.getList();
    },

    onReset() {
      this.queryParams.nick = "";
      this.queryParams.identityType = "";
      this.queryParams.content = "";
      this.handleQuery();
    },
    formateTimeMils(time) {
      return dayjs(parseFloat(time)).format("YYYY-MM-DD HH:mm");
    },
  },
};
</script>

<style lang="scss" scoped>
.charts {
  font-size: 14px;
  border-radius: 4px;

  .search-items {
    display: flex;
    justify-content: space-between;
    padding: 16px 12px;
    margin-bottom: 30px;
    background: white;

    .input-search {
      width: 150px;
      margin-right: 12px;
      margin-left: 4px;
    }
  }
}
</style>
