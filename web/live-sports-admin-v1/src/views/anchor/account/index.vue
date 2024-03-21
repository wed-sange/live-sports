<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="105px">
      <el-form-item :label="`${queryName}账号/昵称`" prop="name">
        <el-input v-model="queryParams.nickName" :placeholder="`请输入${queryName}账号或昵称`" clearable style="width: 240px"
          @keyup.enter.native="handleQuery" />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        <el-button type="primary" @click="onCreate()">添加账号</el-button>
      </el-form-item>
    </el-form>

    <el-tabs v-model="queryParams.identityType" @tab-click="handleQuery">
      <el-tab-pane v-for="item in types" :label="item.label" :name="item.value" />
    </el-tabs>

    <el-radio-group v-model="queryParams.ynForbidden" style="margin-bottom: 15px" @change="handleQuery">
      <el-radio-button :label="0">正常</el-radio-button>
      <el-radio-button :label="1">封禁</el-radio-button>
    </el-radio-group>

    <el-table v-loading="loading" :data="postList">
      <el-table-column label="账号" align="center" prop="account" key="muteAccount" />
      <el-table-column label="昵称" align="center" prop="nickName" key="muteNickName" />

      <!--  助理账号需要显示关联主播-->
      <template v-if="queryParams.identityType == 3">
        <el-table-column label="关联主播" align="center" prop="relationLiveUsers">
          <template #default="scope">
            <el-tooltip v-if="scope.row.relationLiveUsers"
                        effect="dark"
                        :content="filterRelationLiveUsers(scope.row.relationLiveUsers, true)"
                        placement="top-start">
              <el-button class="border-0">
                {{ filterRelationLiveUsers(scope.row.relationLiveUsers) }}
              </el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </template>

      <!--  账号状态正常-->
      <el-table-column label="状态" align="center" prop="ynForbidden">
        <template #default="scope">
          <span v-if="scope.row.ynForbidden === 0" style="color: #67c23a">正常</span>
          <span v-else style="color: #f56c6c">封禁中</span>
        </template>
      </el-table-column>

      <template v-if="queryParams.ynForbidden > 0">
        <el-table-column label="封禁时长" align="center" prop="forbiddenDay" key="forbiddenDay">
          <template #default="scope">
            <span>{{ scope.row.ynForbidden === 2 ? "永久封禁" : `${scope.row.forbiddenDay}天` }}</span>
          </template>
        </el-table-column>

        <el-table-column label="剩余时长" align="center" prop="restTime" key="restTime">
          <template #default="scope">
            <span>{{ getRestTime(scope.row) }}</span>
          </template>
        </el-table-column>

        <el-table-column label="原因" align="center" prop="forbiddenDescp" key="forbiddenDescp" />
      </template>

      <el-table-column label="备注信息" align="center" prop="remarks" key="muteRemarks">
        <template #default="scope">
          <span>{{ scope.row.remarks.substr(0, 20) }}{{ scope.row.remarks.length > 20 ? '...' : '' }}</span>
        </template>
      </el-table-column>

<!--      <el-table-column label="助手" align="center" prop="assistantCount" key="muteAssistantCount">-->
<!--        <template #default="scope">-->
<!--          <el-button type="text" @click="onAddAssit(scope.row)">-->
<!--            {{ scope.row.assistantCount ? scope.row.assistantCount : "添加助手" }}-->
<!--          </el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->

      <el-table-column label="操作" width="280" align="center" key="muteOption" class-name="small-padding fixed-width">
        <template #default="scope">
          <div class="flex items-center w-full justify-center">
            <el-link type="primary" @click="onCreate(scope.row)">详情</el-link>

            <el-link type="primary" class="ml-4" @click="onResetPsw(scope.row.id)">重置密码</el-link>

            <el-link v-if="scope.row.ynForbidden === 0"
                       link
                       type="danger"
                       @click="onMute(scope.row, false)"
                       class="ml-4">封禁账号
            </el-link>
            <el-popconfirm v-else @confirm="onunMute(scope.row.id, false)" title="是否要解封该账号？">
              <template #reference>
                <el-link class="ml-4" link type="danger">解封</el-link>
              </template>
            </el-popconfirm>

            <el-popconfirm @confirm="onDelete(scope.row.id, false)" title="是否要注销该账号？">
              <template #reference>
                <el-link class="ml-4" type="danger">注销</el-link>
              </template>
            </el-popconfirm>

          </div>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.current" :limit.sync="queryParams.size"
      @pagination="getList" />

    <el-dialog :visible.sync="showDialog" :title="muteItem.belongLive ? '账号详情' : '添加账号'"
               width="720px" append-to-body>
      <div class="flex-item">
        <span style="width: 70px" class="flex-shrink-0">账号</span>
        <el-input v-model="muteItem.account" style="width: 60%" :disabled="!!muteItem.belongLive" />
      </div>
      <div v-if="!muteItem.belongLive" class="flex-item">
        <span style="width: 70px" class="flex-shrink-0">密码</span>
        <el-input v-model="muteItem.passwd" show-password style="width: 31.5%" />
        <el-button class="flex-shrink-0 ml-4" type="primary" @click="onCopy()">复制</el-button>
        <el-button v-if="!muteItem.belongLive" class="flex-shrink-0 ml-4" type="primary" plain @click="onRandomCode()">随机密码</el-button>
      </div>
      <div class="flex-item">
        <span style="width: 70px" class="flex-shrink-0">身份类型</span>
        <el-select v-model="muteItem.identityType"
                   placeholder="请选择"
                   style="width: 60%">
          <el-option v-for="item in identityTypes"
                     :key="item.value"
                     :label="item.label"
                     :value="item.value" />
        </el-select>
      </div>
      <div v-if="muteItem.identityType === 3" class="flex-item">
        <span style="width: 70px" class="flex-shrink-0">关联主播</span>
        <el-transfer :titles="['所有主播', '已关联主播']"
                     :props="{ key: 'id', label: 'nickName' }"
                     v-model="muteItem.liveUserId"
                     :left-default-checked="liveUserIdChecked"
                     :data="liveUsersList"
                     filterable />
      </div>
      <div class="flex-item">
        <span style="width: 70px"  class="flex-shrink-0">备注信息</span>
        <el-input type="textarea" :rows="3" v-model="muteItem.remarks" :maxlength="300" style="width: 60%" />
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">取 消</el-button>
          <el-button type="primary" @click="onConfirm" :loading="confirmLoading">{{ muteItem.belongLive ? '保 存' : '确 定' }}</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog :visible.sync="showForbidden" title="禁封操作" width="540px" append-to-body>
      <div class="flex flex-col">
        <div class="flex items-center">
          <span class="input-dialog mr-4">期限</span>
          <el-radio-group v-model="forbiddenItem.ynForbidden">
            <el-radio :label="1" size="large">有效期 </el-radio>
            <el-radio :label="2" size="large">永久</el-radio>
          </el-radio-group>
        </div>
        <div class="flex-item flex items-center pl-7" v-show="forbiddenItem.ynForbidden === 1">
          <el-input-number class="input-dialog mr-2" :min="1" v-model="forbiddenItem.forbiddenDay" placeholder="请输入" />天
        </div>
      </div>
      <div class="flex-item" style="margin-top: 8px;">
        <span class="w-12"><span class="text-red-600">*</span>原因</span>
        <el-input type="textarea" :rows="4" v-model="forbiddenItem.forbiddenDescp" />
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancelForbidden">取 消</el-button>
          <el-button type="primary" @click="onForbiddenConfirm">确 定</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog :visible.sync="showHelpers" title="助手列表" width="660px" append-to-body>
      <div class="flex flex-col">
        <div class="flex flex-row-reverse">
          <el-button type="primary" style="margin-bottom: 12px;" class="w-[120px] float-right"
            @click="onShowAddAssit(currentLiveId)">添加助手</el-button>
        </div>
        <el-table :data="helperList" stripe>
          <el-table-column label="助手账号" align="center" prop="account" />
          <el-table-column label="备注" align="center" prop="remarks" />
          <el-table-column label="身份" align="center" prop="identityType">
            <template #default="scope">
              <span>{{
                findIndentify(scope.row.identityType)
              }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="280" align="center" class-name="small-padding fixed-width">
            <template #default="scope">
              <div class="flex items-center w-full justify-center">
                <a class="mr-3" @click="onResetPsw(scope.row.id)">重置密码</a>
                <el-popconfirm @confirm="onDelete(scope.row.id, true)" title="是否要注销该账号？">
                  <template #reference>
                    <el-button class="ml-4" link type="text">注销</el-button>
                  </template>
                </el-popconfirm>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import dayjs from 'dayjs'
import defalutImg from "../../../assets/images/default-img.png";
import {
  getAnchorList,
  creatAnchor,
  resetPasswd,
  forbiddenLiveUser,
  cancelLiveUser,
  uncancelLiveUser,
  getAnchorHelpers,
  getAllLiveUsers,
  updateAnchor
} from "@/api/anchor/index";
import copy from 'copy-to-clipboard'

export default {
  name: "AnchorCreate",
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
        identityType: '1',
        nickName: "",
        ynForbidden: 0,
      },
      showDialog: false,
      showForbidden: false,
      showHelpers: false,
      currentLiveId: "",
      helperList: [],
      muteItem: {
        account: "",
        passwd: "",
        remarks: "",
        identityType: 1,
        belongLive: null,
        liveUserId: []
      },
      forbiddenItem: {
        ynForbidden: 1,
        forbiddenDescp: "",
        forbiddenDay: 0,
        id: ""
      },
      identityTypes: [
        { label: "主播", value: 1 },
        // { label: "助手", value: 2 },
        { label: "直播助理", value: 3 }
      ],
      isAssist: false,
      activeIndex: 0,
      types: [
        { label: "主播账号", value: '1' },
        { label: "助理账号", value: '3' }
      ],
      liveUsersList: [],
      relationLiveUsersLen: 18,
      confirmLoading: false,
      liveUserIdChecked: []
    };
  },
  created() {
    this.getList();
  },
  computed: {
    queryName() {
      return this.types.find(v => v.value === this.queryParams.identityType).label.substr(0, 2)
    }
  },
  methods: {
    filterRelationLiveUsers(list, isAll) {
      const str = list.map(v => v.account).join(',')
      if (isAll) {
        return str
      }
      const someStr = str.substr(0, this.relationLiveUsersLen)
      return `${someStr}${str.length > this.relationLiveUsersLen && someStr.length === this.relationLiveUsersLen ? '...' : ''}`
    },
    findIndentify(identityType) {
      const findItem = this.identityTypes.find(item => item.value == identityType)
      return findItem ? findItem.label : ""
    },
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
      this.$router.push({ path: `/anchor/details`, query: { id } })
    },
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      const reqObj = { ...this.queryParams };
      reqObj.identityType = +reqObj.identityType
      if (!reqObj.identityType) {
        delete reqObj.identityType;
      }
      getAnchorList(reqObj).then(
        (response) => {
          const { records, total } = response.data
          this.postList = records;
          this.total = total;
          this.loading = false;
        }
      );
    },
    onMute(item, assist) {
      this.isAssist = assist;
      if (!this.activeIndex || assist) {
        const { forbiddenDescp, forbiddenDay, id } = item;
        let { ynForbidden } = item;
        ynForbidden = ynForbidden || 1;
        this.forbiddenItem = {
          ynForbidden,
          forbiddenDescp,
          forbiddenDay,
          id
        };
        this.showForbidden = true;
      }
    },
    onCreate(row) {
      this.liveUsersList = []
      this.getAllUserList(row)

      this.muteItem.account = ""
      this.muteItem.passwd = ""
      this.muteItem.remarks = ""
      this.muteItem.identityType = 1
      this.muteItem.belongLive = null

      if (row) {
        const { account, remarks, identityType, id } = row
        this.muteItem.account = account
        this.muteItem.passwd = ""
        this.muteItem.remarks = remarks
        this.muteItem.identityType = identityType
        this.muteItem.belongLive = id
      }
      this.showDialog = true;
    },
    cancel() {
      this.showDialog = false;
    },
    onCloseLive(id) {
      closeLive(id).then(() => {
        this.$message.success("已关播")
        this.getList();
      });
    },
    onConfirm() {
      this.confirmLoading = true

      const params = { ...this.muteItem }
      if (this.muteItem.identityType !== 3) {
        delete params.liveUserId
      }
      delete params.belongLive

      if (this.muteItem.belongLive) {
        params.id = this.muteItem.belongLive
        delete params.passwd
        delete params.account
        updateAnchor(params)
          .then(() => {
            this.$message.success('编辑成功')
            this.getList();
            this.cancel();
          })
          .finally(() => {
            this.confirmLoading = false
          })
      } else {
        creatAnchor(params)
          .then(() => {
            this.$message.success('添加成功')
            this.getList();
            this.cancel();
          })
          .finally(() => {
            this.confirmLoading = false
          })
      }
    },
    onResetPsw(id) {
      resetPasswd(id).then(res => {
        this.$message.success(`重置成功，新密码：${res.data}`)
      });
    },
    getRestTime(item) {
      if (item.ynForbidden === 2) {
        return "永久";
      }
      const { leaveDay } = item;
      return leaveDay + "天";
    },
    cancelForbidden() {
      this.showForbidden = false;
    },
    onForbiddenConfirm() {
      const { forbiddenDescp, ynForbidden, forbiddenDay } = this.forbiddenItem
      if (ynForbidden == 1 && !forbiddenDay) {
        this.$message.error("请输入封禁时间")
        return
      }
      if (!forbiddenDescp) {
        this.$message.error("请输入封禁原因")
        return
      }
      forbiddenLiveUser(this.forbiddenItem).then(() => {
        this.$message.success("封禁成功")
        this.showForbidden = false;
        if (this.isAssist) {
          // this.getHelpers();
        } else {
          this.getList();
        }
      });
    },
    onDelete(id, assist) {
      this.isAssist = assist;
      cancelLiveUser(id).then(() => {
        this.$message.success("注销成功")
        if (assist) {
          // this.getHelpers();
        } else {
          this.getList();
        }
      });
    },
    onunMute(id, assist) {
      this.isAssist = assist;
      uncancelLiveUser(id).then(() => {
        this.$message.success("解封成功")
        if (assist) {
          // this.getHelpers();
        } else {
          this.getList();
        }
      });
    },
    onRandomCodeNumber(length) {
      const chars = "0123456789";
      let password = "";
      for (let i = 0; i < length; i++) {
        password += chars.charAt(Math.floor(Math.random() * chars.length));
      }
      return password;
    },
    onRandomCodeSmall(length) {
      const chars = "abcdefghijklmnopqrstuvwxyz";
      let password = "";
      for (let i = 0; i < length; i++) {
        password += chars.charAt(Math.floor(Math.random() * chars.length));
      }
      return password;
    },
    onRandomCode() {
      const numberStr = this.onRandomCodeNumber(10);
      const chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
      let password = "";
      for (let i = 0; i < 3; i++) {
        password += chars.charAt(Math.floor(Math.random() * chars.length));
      }
      const smallChars = this.onRandomCodeSmall(3);
      this.muteItem.passwd = password + smallChars + numberStr;
    },
    onCopy() {
      try {
        copy(this.muteItem.passwd)
        this.$message.success('复制成功')
      } catch (e) {
        this.$message.error('复制失败')
      }
      copy(this.muteItem.passwd)
    },

    // onAddAssit(item) {
    //   if (!item.assistantCount) {
    //     this.onShowAddAssit(item.id);
    //   } else {
    //     this.currentLiveId = item.id;
    //     this.getHelpers();
    //   }
    // },
    onShowAddAssit(id) {
      this.muteItem.account = "";
      this.muteItem.passwd = "";
      this.muteItem.remarks = "";
      this.muteItem.belongLive = id;
      this.muteItem.identityType = 2;
      this.showDialog = true;
    },
    // getHelpers() {
    //   if (!this.currentLiveId) {
    //     return
    //   }
    //   getAnchorHelpers(this.currentLiveId).then((res) => {
    //     this.helperList = res.data;
    //     this.showHelpers = true;
    //   });
    // },

    // 表单重置
    reset() {
      this.form = {
        nickName: '',
        identityType: 0,
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
      this.queryParams.nickName = ""
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 获取所有主播
    getAllUserList(row) {
      getAllLiveUsers()
        .then(({ data }) => {
          this.liveUsersList = data

          if (row) {
            if (row.relationLiveUsers && row.relationLiveUsers.length) {
              this.muteItem.liveUserId = row.relationLiveUsers.map(v => v.id)
            } else {
              this.muteItem.liveUserId = []
            }
            this.liveUserIdChecked = []
          } else {
            this.muteItem.liveUserId = []
            this.liveUserIdChecked = data.map(v => v.id)
          }
        })
    }
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
<style>
.el-tooltip__popper {
  max-width: 300px;
}
</style>
