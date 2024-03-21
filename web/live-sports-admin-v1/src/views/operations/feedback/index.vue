<template>
  <div class="feedback">
    <div class="search-items">
      <div class="query-item">
        <span>用户id</span>
        <el-input
          :maxlength="30"
          class="input-search"
          @keyup.enter.native="handleQuery"
          clearable
          placeholder="请输入用户id"
          v-model="queryParams.feedbackUserId"
          type="number"
        />
        <span>类型</span>
        <el-select
          v-model="queryParams.feedbackType"
          placeholder="请选择"
          class="input-search"
          @change="handleQuery"
        >
          <el-option
            v-for="item in roles"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </div>
      <div>
        <el-button type="primary" @click="handleQuery">查询</el-button>
        <el-button class="ml-3" @click="onReset">重置</el-button>
      </div>
    </div>
    <div class="w-full bg-white border-b-gray-300 border-b-[0.06rem]">
      <el-radio-group
        class="m-3"
        v-model="queryParams.feedbackStatus"
        @change="handleQuery"
      >
        <el-radio-button :label="1">未处理</el-radio-button>
        <el-radio-button :label="2">已处理</el-radio-button>
      </el-radio-group>
    </div>
    <div class="flex flex-col bg-white mb-3">
      <el-table v-loading="loading" :data="postList" stripe>
        <el-table-column label="用户id" align="center" prop="feedbackUserId" />
        <el-table-column
          label="用户昵称"
          align="center"
          prop="feedbackUserName"
        />
        <el-table-column label="类型" align="center" prop="feedbackType">
          <template #default="scope">
            <span>{{ feedbackTypes[scope.row.feedbackType] }}</span>
          </template>
        </el-table-column>
        <el-table-column label="反馈内容" align="center" prop="feedbackContent">
          <template #default="scope">
            <span class="line-2">{{ scope.row.feedbackContent }}</span>
          </template>
        </el-table-column>
        <el-table-column label="图片" align="center" prop="feedbackImage">
          <template #default="scope">
            <div class="flex flex-wrap items-center w-full justify-center">
              <a
                :key="item"
                class="max-h-10 bg-contain mr-2 cursor-pointer"
                target="_blank"
                v-for="item in scope.row.feedbackImage"
                :href="item"
              >
                <img class="max-h-10 bg-contain" :src="item" />
              </a>
            </div>
            <span />
          </template>
        </el-table-column>
        <el-table-column label="反馈时间" align="center" prop="createTime">
          <template #default="scope">
            <span>{{
              scope.row.feedbackTime
                ? formateTimeMils(parseFloat(scope.row.feedbackTime))
                : ""
            }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" prop="opt">
          <template #default="scope">
            <el-button
              type="text"
              @click="onShowDetail(scope.row, scope.$index)"
              >{{
                scope.row.ignoreFlag
                  ? "已忽略"
                  : feedbackStatus[scope.row.feedbackStatus]
              }}</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.current"
      :limit.sync="queryParams.size"
      @pagination="getList"
    />
    <el-dialog :visible.sync="showDialog" width="540px" append-to-body>
      <div class="flex flex-col">
        <div class="flex-item">
          <span style="width: 80px">反馈人</span>
          <span>{{ feedbackItem.feedbackUserName }}</span>
        </div>
        <div class="flex-item">
          <span style="width: 80px; min-width: 80px">反馈内容</span>
          <span>{{ feedbackItem.feedbackContent }}</span>
        </div>
        <div class="flex-item">
          <span style="width: 80px">反馈图片</span>
          <div class="flex flex-wrap">
            <a
              :key="item"
              class="w-12 h-12 mr-3 cursor-pointer"
              target="_blank"
              v-for="item in feedbackItem.feedbackImage"
              :href="item"
            >
              <img class="w-12 h-12 bg-contain" :src="item" />
            </a>
          </div>
        </div>
        <div class="flex-item">
          <span style="width: 80px">反馈时间</span>
          <span>{{
            feedbackItem.feedbackTime
              ? formateTimeMils(parseFloat(feedbackItem.feedbackTime))
              : ""
          }}</span>
        </div>

        <div class="flex-item flex">
          <span style="width: 80px"
            ><span class="text-red-600">*</span>回复内容</span
          >
          <el-input
            :disabled="feedbackItem.feedbackStatus === 2"
            type="textarea"
            style="width: 400px"
            class="input-search"
            :rows="4"
            v-model="feedbackItem.feedbackResult"
          />
        </div>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">取 消</el-button>
          <el-button
            type="primary"
            v-if="feedbackItem.feedbackStatus != 2"
            @click="onIgnore"
            >忽略</el-button
          >
          <el-button
            type="primary"
            v-if="feedbackItem.feedbackStatus != 2"
            @click="onConfirm"
            >确 定</el-button
          >
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import dayjs from "dayjs";
import {
  getFeedbacks,
  feedbackReply,
  feedbackIgnore,
} from "@/api/operations/index";
export default {
  name: "Feedback",
  data() {
    return {
      postList: [],
      total: 0,
      roles: [
        { label: "全部", value: "" },
        { label: "更新问题", value: 1 },
        { label: "直播相关", value: 2 },
        { label: "产品体验", value: 3 },
        { label: "聊天相关", value: 4 },
        { label: "比赛相关", value: 5 },
        { label: "其他", value: 6 },
      ],
      feedbackTypes: {
        1: "更新问题",
        2: "直播相关",
        3: "产品体验",
        4: "聊天相关",
        5: "比赛相关",
        6: "其他",
      },
      showDialog: false,
      feedbackStatus: { 1: "前往处理", 2: "查看处理结果" },
      rowIndex: 0,
      queryParams: {
        current: 1,
        size: 10,
        feedbackUserId: "",
        feedbackType: "",
        feedbackStatus: 1,
      },
      feedbackItem: {
        id: "",
        feedbackUserName: "",
        feedbackTime: "",
        feedbackContent: "",
        feedbackResult: "",
        feedbackImage: [],
        feedbackStatus: 1,
      },
    };
  },
  created() {
    this.handleQuery();
  },
  methods: {
    /** 查询岗位列表 */
    getList() {
      const queryData = { ...this.queryParams };
      if (!queryData.feedbackStatus) {
        delete queryData.feedbackStatus;
      }
      if (!queryData.feedbackUserId) {
        delete queryData.feedbackUserId;
      }
      if (!queryData.feedbackType) {
        delete queryData.feedbackType;
      }
      this.loading = true;
      getFeedbacks(queryData).then((response) => {
        this.postList = response.data.records;
        this.total = parseInt(response.data.total);
        this.loading = false;
      });
    },
    onPageChange(current) {
      this.queryParams.current = current;
      getList();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      if (this.queryParams.feedbackUserId.length > 19) {
        this.$message.error("用户id不能超过19位");
        return;
      }
      this.queryParams.current = 1;
      this.getList();
    },

    onReset() {
      this.queryParams.feedbackUserId = "";
      this.handleQuery();
    },
    cancel() {
      this.showDialog = false;
    },
    onConfirm() {
      if (!this.feedbackItem.feedbackResult) {
        this.$message.error("请输入回复内容");
        return;
      }
      feedbackReply({ ...this.feedbackItem, feedbackStatus: 2 }).then(() => {
        this.$message.success("操作成功");
        this.postList[this.rowIndex].feedbackStatus = 2;
        this.cancel();
      });
    },
    onIgnore() {
      feedbackIgnore({ id: this.feedbackItem.id }).then(() => {
        this.$message.success("操作成功");
        this.postList[this.rowIndex].ignoreFlag = true;
        this.cancel();
        this.getList();
      });
    },
    onShowDetail(item, index) {
      // if (item.ignoreFlag) {
      //   return;
      // }
      this.rowIndex = index;
      this.feedbackItem = item;
      this.showDialog = true;
    },
    formateTimeMils(time) {
      return dayjs(parseFloat(time)).format("YYYY-MM-DD HH:mm");
    },
  },
};
</script>

<style lang="scss">
.line-2 {
  text-overflow: -o-ellipsis-lastline;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
}

input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none !important;
}

.el-input__inner {
  line-height: 1px !important;
}

// or

input[type="number"] {
  line-height: 1px !important;
}

.flex-item {
  display: flex;
  margin-bottom: 12px;
}

.feedback {
  font-size: 14px;
  border-radius: 4px;
  padding-bottom: 40px;

  .search-items {
    display: flex;
    justify-content: space-between;
    padding: 16px 12px;
    margin-bottom: 10px;
    background: white;

    .input-search {
      width: 160px;
      margin-right: 12px;
      margin-left: 4px;
    }
  }
}
</style>
