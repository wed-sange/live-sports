<template>
  <div class="user-detail">
    <div class="header-item">
      <div class="flex flex-row-reverse w-full">
        <el-button type="primary" class="ml-3" @click="onBack()"
          >返回</el-button
        >
        <el-popconfirm
          v-if="result.ynForbidden"
          @confirm="onMute()"
          title="是否解封该用户？"
        >
          <template #reference>
            <el-button type="primary">解封</el-button>
          </template>
        </el-popconfirm>
        <div class="red-btn-item" v-else type="primary" @click="onMute()">
          封禁
        </div>
      </div>
    </div>
    <div class="basic-item">
      <span class="font-bold">基本信息</span>
      <div class="info-items">
        <div class="info-item" v-for="(k, v) in infos" v-bind:key="v">
          <span>{{ k }}：</span>
          <span>{{ result[v] }}</span>
        </div>
      </div>
    </div>

    <el-dialog
      :visible.sync="showDialog"
      title="禁封操作"
      width="540px"
      append-to-body
    >
      <div class="flex flex-col">
        <div class="flex items-center">
          <span class="input-dialog mr-4">期限</span>
          <el-radio-group
            style="margin-left: 6px"
            v-model="muteItem.muteForever"
          >
            <el-radio :label="false" size="large">有效期 </el-radio>
            <el-radio :label="true" size="large">永久</el-radio>
          </el-radio-group>
        </div>
        <div
          class="user-detail-dialog flex items-center pl-7"
          style="margin-left: 24px; margin-top: 6px"
          v-show="!muteItem.muteForever"
        >
          <el-input-number
            class="input-dialog mr-2"
            type="number"
            v-model="muteItem.forbiddenDay"
            :min="1"
            :step="1"
            step-strictly
            placeholder="请输入"
          />天
        </div>
      </div>
      <div style="display: flex; margin-top: 20px">
        <span class="w-12" style="width: 60px"
          ><span class="text-red-600">*</span>原因</span
        >
        <el-input type="textarea" :rows="4" v-model="muteItem.forbiddenDescp" />
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="onCancel">取 消</el-button>
          <el-button type="primary" @click="onConfirm">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import router from "@/router";
import {
  appUserDetail,
  appUserForbidden,
  appUserUnForbidden,
} from "@/api/manager/user";
import dayjs from "dayjs";

export default {
  name: "UserDetail",
  data() {
    return {
      showDialog: false,
      result: {},
      infos: {
        id: "用户id",
        lvNum: "用户等级",
        name: "用户昵称",
        growthValue: "当前成长值",
        tel: "手机号",
        email: "邮箱号",
        createTime: "注册时间",
      },
      muteItem: {
        id: "",
        ynForbidden: 1,
        forbiddenDescp: "",
        forbiddenDay: 1,
        muteForever: false,
      },
    };
  },
  created() {
    const { query } = router.currentRoute;
    this.muteItem.id = query.id;
    this.getDetail(query.id);
  },
  methods: {
    getDetail(id) {
      this.loading = true;
      appUserDetail(id).then((res) => {
        const data = res.data;
        if (data.createTime) {
          data.createTime = dayjs(parseFloat(data.createTime)).format(
            "YYYY-MM-DD HH:mm:ss"
          );
        }
        this.loading = false;
        this.result = data;
      });
    },
    onConfirm() {
      const { forbiddenDescp, muteForever, forbiddenDay, id } = this.muteItem;
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
        this.result.ynForbidden = 1;
        this.$message({
          message: "已封禁",
          type: "success",
        });
        this.onCancel();
      });
    },
    onCancel() {
      this.showDialog = false;
    },
    onMute() {
      if (this.result.ynForbidden) {
        this.unForbidden(this.muteItem.id);
      } else {
        this.showDialog = true;
      }
    },
    onBack() {
      this.$router.push({ path: "/manager/list" });
    },
    unForbidden(id) {
      appUserUnForbidden(id).then(() => {
        this.result.ynForbidden = 0;
        this.$message({
          message: "已解封",
          type: "success",
        });
      });
    },
  },
};
</script>

<style scoped lang="scss">
.user-detail {
  background: #f5f5f5;
  height: 100vh;

  .header-item {
    padding: 12px;
    background: white;
    margin-bottom: 12px;
    margin-top: 12px;
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

    .info-items {
      display: flex;
      align-items: center;
      flex-wrap: wrap;
      width: 100%;
      margin-top: 12px;
      color: #666;
      font-size: 15px;

      .info-item {
        margin-bottom: 12px;
        width: 50%;
      }
    }
  }
}
</style>
