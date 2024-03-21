<template>
  <div class="activity">
    <div class="search-items">
      <div class="query-item">
        <span>标题</span>
        <el-input
          :maxlength="30"
          @keyup.enter.native="handleQuery"
          clearable
          class="input-search"
          placeholder="请输入标题"
          v-model="queryParams.title"
        />
        <span>状态</span>
        <el-select
          v-model="queryParams.status"
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
        <el-button type="primary" class="ml-3" @click="onDetail({ id: 0 })"
          >添加文章</el-button
        >
      </div>
    </div>

    <div class="flex flex-col bg-white mb-3">
      <el-table v-loading="loading" :data="postList" stripe>
        <el-table-column label="标题" align="center" prop="title" />
        <el-table-column
          label="发布时间"
          align="center"
          prop="updateTime"
          width="180px"
        >
          <template #default="scope">
            <span>{{
              scope.row.updateTime
                ? formateTimeMils(parseFloat(scope.row.updateTime))
                : ""
            }}</span>
          </template>
        </el-table-column>
        <el-table-column label="排序值" align="center" prop="sortOrder" />
        <el-table-column label="状态" align="center" prop="status">
          <template #default="scope">
            <span>{{ scope.row.status ? "已上架" : "已下架" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" prop="opt">
          <template #default="scope">
            <el-button type="text" @click="onDetail(scope.row)">编辑</el-button>
            <el-popconfirm
              @confirm="onDelete(scope.row.id)"
              title="是否删除该活动？"
            >
              <template #reference>
                <el-button class="ml-4 red-text" link type="text"
                  >删除</el-button
                >
              </template>
            </el-popconfirm>
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
  </div>
</template>

<script>
import dayjs from "dayjs";
import router from "@/router";
import { getActivities, deleteActivity } from "@/api/operations/index";
export default {
  name: "Index",
  data() {
    return {
      postList: [],
      total: 0,
      roles: [
        { label: "全部", value: "" },
        { label: "上架", value: 1 },
        { label: "下架", value: 0 },
      ],
      rowIndex: 0,
      queryParams: {
        current: 1,
        size: 10,
        title: "",
        status: "",
      },
      loading: true,
    };
  },
  created() {
    this.handleQuery();
  },
  methods: {
    /** 查询岗位列表 */
    getList() {
      const queryData = { ...this.queryParams };
      this.loading = true;
      getActivities(queryData).then((response) => {
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
      this.queryParams.title = "";
      this.queryParams.status = "";
      this.handleQuery();
    },
    onDelete(id) {
      deleteActivity(id).then(() => {
        this.getList();
      });
    },
    onDetail(item) {
      router.push({
        path: "/operations/detail",
        query: { id: item.id },
      });
    },
    formateTimeMils(time) {
      return dayjs(parseFloat(time)).format("YYYY-MM-DD HH:mm");
    },
  },
};
</script>

<style scoped lang="scss">
.flex-item {
  display: flex;
  margin-bottom: 12px;
}

.activity {
  font-size: 14px;
  border-radius: 4px;

  .search-items {
    display: flex;
    justify-content: space-between;
    padding: 16px 12px;
    margin-bottom: 10px;
    background: white;

    .input-search {
      width: 120px;
      margin-right: 12px;
      margin-left: 4px;
    }
  }
}
</style>
