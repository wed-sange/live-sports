<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="用户id" prop="id">
        <el-input
          type="number"
          v-model="queryParams.id"
          placeholder="请输入用户id"
          clearable
          style="width: 240px"
          :maxlength="30"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户昵称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入用户昵称"
          clearable
          style="width: 240px"
          :maxlength="30"
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
      v-model="activeIndex"
      style="margin-bottom: 6px"
      @change="onTypeChange"
    >
      <el-radio-button :label="0">正常</el-radio-button>
      <el-radio-button :label="1">禁言</el-radio-button>
    </el-radio-group>

    <el-table v-if="!activeIndex" v-loading="loading" :data="userList">
      <el-table-column
        label="用户id"
        align="center"
        key="id"
        prop="id"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="用户昵称"
        align="center"
        key="name"
        prop="name"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="手机号码" align="center" key="tel" prop="tel" />
      <el-table-column label="邮箱" align="center" prop="email" key="email" />
      <el-table-column
        label="用户等级"
        align="center"
        prop="lvNum"
        key="lvNum"
      />

      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope" v-if="scope.row.userId !== 1">
          <el-button size="mini" type="text" @click="onDetail(scope.row.id)"
            >查看</el-button
          >
          <el-button
            size="mini"
            class="red-text"
            type="text"
            @click="onMute(scope.row.id)"
            >禁言</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <el-table v-else v-loading="loading" :data="userList">
      <el-table-column
        label="用户id"
        :show-overflow-tooltip="true"
        align="center"
        prop="id"
        key="id"
      />
      <el-table-column label="用户昵称" align="center" prop="name" key="name" />
      <el-table-column
        label="用户等级"
        align="center"
        prop="lvNum"
        key="lvNum"
      />
      <el-table-column
        label="禁封时长"
        align="center"
        prop="muteTime"
        key="muteTime"
      >
        <template #default="scope">
          <span>{{ forbiddenDay(scope.row) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="剩余时长"
        align="center"
        prop="restTime"
        key="restTime"
      >
        <template #default="scope">
          <span>{{ restTime(scope.row) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="原因"
        align="center"
        prop="forbiddenDescp"
        key="forbiddenDescp"
      />
      <el-table-column
        label="操作"
        align="center"
        key="muteOperation"
        prop="forbiddenDescp"
      >
        <template #default="scope">
          <div class="flex items-center w-full justify-center">
            <el-button
              size="mini"
              type="text"
              @click="onDetail(scope.row.id)"
              style="margin-right: 10px"
              >查看</el-button
            >
            <el-popconfirm
              @confirm="unForbidden(scope.row.id)"
              title="是否解封该用户？"
            >
              <template #reference>
                <el-button size="mini" class="red-text" type="text"
                  >解封</el-button
                >
              </template>
            </el-popconfirm>
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
    <el-dialog
      :visible.sync="showDialog"
      title="禁言操作"
      width="540px"
      append-to-body
    >
      <div class="flex flex-col">
        <div class="flex items-center mb-4">
          <span class="input-dialog mr-4">期限</span>
          <el-radio-group v-model="muteItem.muteForever" style="margin: 6px">
            <el-radio :label="false" size="large">有效期 </el-radio>
            <el-radio :label="true" size="large">永久</el-radio>
          </el-radio-group>
        </div>
        <div
          class="flex-item flex items-center pl-7 mt-[12px]"
          v-show="!muteItem.muteForever"
        >
          <el-input-number
            class="input-dialog mr-2"
            :min="1"
            :step="1"
            step-strictly
            v-model="muteItem.forbiddenDay"
            placeholder="请输入"
          />天
        </div>
      </div>
      <div class="flex-item">
        <span class="w-12">原因<span class="text-red-600">*</span></span>
        <el-input
          type="textarea"
          placeholder="请输入原因"
          :rows="4"
          :maxlength="200"
          v-model="muteItem.forbiddenDescp"
        />
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="onCancel()">取 消</el-button>
          <el-button type="primary" @click="onConfirm()">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import dayjs from "dayjs";
import {
  listUser,
  appUserForbidden,
  appUserUnForbidden,
} from "@/api/manager/user";

export default {
  name: "Manager",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      activeIndex: 0,
      // 总条数
      total: 0,
      // 用户表格数据
      userList: null,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 表单参数
      form: {},
      // 查询参数
      queryParams: {
        current: 1,
        size: 10,
        id: "",
        name: "",
        account: "",
      },
      showDialog: false,
      muteItem: {
        forbiddenDescp: "",
        id: "",
        ynForbidden: 1,
        forbiddenDay: 1,
        muteForever: false,
      },
      // 表单校验
      rules: {
        name: [
          { required: true, message: "用户昵称不能为空", trigger: "blur" },
        ],
        email: [
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur", "change"],
          },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    onCancel() {
      this.showDialog = false;
    },
    onConfirm() {
      const { id, forbiddenDescp, muteForever, forbiddenDay } = this.muteItem;
      if (!forbiddenDescp) {
        this.$message.error("请输入封禁原因");
        return;
      }
      appUserForbidden({
        id,
        ynForbidden: 1,
        forbiddenDescp,
        forbiddenDay: muteForever ? 365 * 1000 : forbiddenDay,
      }).then(() => {
        this.onCancel();
        this.getList();
      });
    },
    unForbidden(id) {
      appUserUnForbidden(id).then(() => {
        this.getList();
      });
    },
    onTypeChange() {
      this.onReset();
    },
    onReset() {
      this.reset();
      this.handleQuery();
    },
    //禁言用户
    onMute(id) {
      if (!this.activeIndex) {
        this.muteItem.forbiddenDay = 1;
        this.muteItem.forbiddenDescp = "";
        this.muteItem.muteForever = false;
        this.muteItem.id = id;
        this.showDialog = true;
      }
    },
    //查看用户详情
    onDetail(id) {
      this.$router.push({ path: `/manager/detail`, query: { id } });
    },
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listUser({ ...this.queryParams, ynForbidden: this.activeIndex }).then(
        (response) => {
          const { records, total } = response.data;
          this.userList = records;
          this.total = total;
          this.loading = false;
        }
      );
    },
    restTime(item) {
      if (!item.forbiddenDay) {
        return "永久";
      }
      const time = dayjs(item.forbiddenTime).diff(new Date().getTime(), "day");
      return time > 100 * 365 ? "永久" : time + "天";
    },
    forbiddenDay(item) {
      if (!item.forbiddenDay) {
        return "永久";
      }
      const time = dayjs(item.forbiddenTime).diff(item.updateTime, "day");
      return time > 100 * 365 ? "永久" : time + "天";
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
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
      if (this.queryParams.id.length > 19) {
        return this.$message.error("用户id不能超过19位");
      }
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },

    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.userId != undefined) {
            // this.$modal.msgSuccess("修改成功");
            //   this.open = false;
            //   this.getList();
          } else {
          }
        }
      });
    },
  },
};
</script>
<style lang="scss">
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
