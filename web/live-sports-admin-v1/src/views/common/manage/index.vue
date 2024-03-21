<template>
  <div class="user-manage">
    <div class="search-items">
      <div class="query-item">
        <span>登录账户</span>
        <el-input class="input-search" placeholder="请输入账户名称" v-model="queryParams.account" @change="handleQuery" />
      </div>
      <div>
        <el-button type="primary" @click="onAddUser">添加用户</el-button>
      </div>
    </div>
    <div class="flex flex-col bg-white">
      <el-table v-loading="loading" :data="postList" stripe>
        <el-table-column label="id" align="center" prop="id" />
        <el-table-column label="登录账号" align="center" prop="account" />
        <el-table-column label="姓名" align="center" prop="name" />
        <el-table-column label="创建时间" align="center" prop="createTime">
          <template #default="scope">
            <span>{{
              scope.row.createTime
              ? formateTimeMils(parseFloat(scope.row.createTime))
              : ""
            }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <div class="flex items-center w-full justify-center">
              <el-popconfirm @confirm="resetPsw(scope.row.id)" title="是否重置该用户密码？">
                <template #reference>
                  <el-button link type="primary">重置密码</el-button>
                </template>
              </el-popconfirm>

              <el-popconfirm @confirm="onRemoveUser(scope.row.id)" title="是否注销该用户？">
                <template #reference>
                  <el-button class="ml-4" link type="primary">注销</el-button>
                </template>
              </el-popconfirm>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-pagination class="mt-3" v-show="total > 0" :total="total" v-model:page="queryParams.current"
      v-model:limit="queryParams.size" @current-change="onCurrentChange" />
    <el-dialog :visible.sync="showDialog" title="添加用户" width="540px" append-to-body>
      <div class="flex-item">
        <span class="dialog-label">登录账号</span>
        <el-input class="input-dialog" clearable placeholder="请输入登录账号" v-model="muteItem.account" />
      </div>
      <div class="flex-item">
        <span class="dialog-label">姓名</span>
        <el-input class="input-dialog" clearable placeholder="请输入姓名" v-model="muteItem.name" />
      </div>
      <div class="flex-item">
        <span class="dialog-label">密码</span>
        <el-input class="input-dialog" clearable placeholder="请输入密码" v-model="muteItem.passwd" />
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">取 消</el-button>
          <el-button type="primary" @click="onConfirm">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import dayjs from "dayjs";
import { userPages, resetUserPsw, userAdd, removeUser } from "@/api/common/index";
export default {
  name: "CommonManage",
  data() {
    return {
      postList: [],
      total: 0,
      showDialog: false,
      muteItem: {
        account: "",
        name: "",
        passwd: ""
      },
      queryParams: {
        current: 1,
        size: 10,
        account: ""
      },
      activeIndex: 0,
      loading: true

    };
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询岗位列表 */
    getList() {
      this.loading = true;
      userPages(this.queryParams).then((response) => {
        this.postList = response.data.records;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.current = 1;
      this.getList();
    },

    cancel() {
      this.showDialog = false;
    },
    onConfirm() {
      const { passwd } = this.muteItem;
      if (passwd.length < 6) {
        this.$message.error("密码6-16个字以内")
        return;
      }
      userAdd(this.muteItem).then(() => {
        this.getList();
        this.cancel();
      });
    },
    onRemoveUser(id) {
      removeUser(id).then(() => {
        this.getList();
      });
    },
    resetPsw(id) {
      resetUserPsw(id).then(res => {
        this.$message.success("重置成功,密码:" + res)
      });
    },
    onAddUser() {
      this.showDialog = true;
    },
    onCurrentChange(current) {
      this.queryParams.current = current;
      this.getList();
    },
    formateTimeMils(time) {
      return dayjs(parseFloat(time)).format("YYYY-MM-DD HH:mm");
    }
  }
};
</script>

<style scoped lang="scss">
.flex-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;

  .dialog-label {
    width: 80px;
  }

  .input-dialog {
    width: 180px;
    margin-left: 1rem;
  }
}

.user-manage {
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

