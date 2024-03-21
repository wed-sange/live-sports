<template>
  <div class="versions">
    <div class="search-items">
      <div class="flex items-center">
        <div class="mr-4">渠道</div>
        <el-select
          @change="getList"
          v-model="queryParams.channel"
          placeholder="请选择"
        >
          <el-option
            v-for="item in channels"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </div>
      <div>
        <el-button type="primary" @click="onAddUser">新增</el-button>
      </div>
    </div>
    <div class="flex flex-col bg-white">
      <el-table v-loading="loading" :data="postList" stripe>
        <el-table-column label="渠道" prop="channel" align="center">
          <template #default="scope">
            <span>{{ scope.row.channel === 1 ? "安卓" : "ios" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="版本号" prop="version" align="center" />
        <el-table-column label="版本说明" prop="remarks" align="center" />
        <el-table-column label="是否强制更新" prop="forceUpdate" align="center">
          <template #default="scope">
            <span>{{ scope.row.forcedUpdate ? "是" : "否" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" align="center">
          <template #default="scope">
            <span>{{
              scope.row.createTime
                ? formateTimeMils(parseFloat(scope.row.createTime))
                : ""
            }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template #default="scope">
            <el-popconfirm
              @confirm="onRemoveUser(scope.row.id)"
              title="是否删除？"
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

    <input
      :value="inputValue"
      type="file"
      v-show="false"
      ref="selectRef"
      @change="changeImage"
    />
    <el-dialog
      :visible.sync="showDialog"
      title="添加"
      width="540px"
      append-to-body
    >
      <div class="flex-item">
        <span class="dialog-label">渠道</span>
        <el-select
          v-model="muteItem.channel"
          placeholder="请选择"
          class="input-dialog"
        >
          <el-option
            v-for="item in channels"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </div>
      <div class="flex-item" v-if="muteItem.channel === 1">
        <span class="dialog-label">选择文件</span>
        <div class="file-choose" @click="onSelectFile">
          {{ file ? file.name : "选择文件" }}
        </div>
      </div>
      <div class="flex-item" v-else>
        <span class="dialog-label">请输入下载地址</span>
        <el-input
          class="input-dialog"
          clearable
          placeholder="请输入"
          v-model="muteItem.sourceUrl"
        />
      </div>
      <div class="flex-item">
        <span class="dialog-label">版本号</span>
        <el-input
          class="input-dialog"
          clearable
          placeholder="请输入版本号"
          v-model="muteItem.version"
        />
      </div>
      <div class="flex-item">
        <span class="dialog-label">版本说明</span>
        <el-input
          type="textarea"
          clearable
          :row="4"
          class="input-version"
          placeholder="请输入版本说明"
          v-model="muteItem.remarks"
        />
      </div>
      <div class="flex-item">
        <span class="dialog-label">是否强制更新</span>
        <el-radio-group v-model="muteItem.forcedUpdate" size="small">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
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
import {
  getVersions,
  addVersion,
  deleteVersion,
  uploadFile,
} from "@/api/common/index";
export default {
  name: "CommonVersion",
  data() {
    return {
      postList: [],
      total: 0,
      showDialog: false,
      channels: [
        { label: "ios", value: 2 },
        { label: "安卓", value: 1 },
      ],
      muteItem: {
        channel: 1,
        version: "",
        remarks: "",
        forcedUpdate: 0,
        sourceUrl: "",
      },
      file: null,
      inputValue: "",
      activeIndex: 0,
      queryParams: {
        channel: 1,
      },
      loading: true,
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询版本列表 */
    getList() {
      this.loading = true;
      getVersions(this.queryParams).then((response) => {
        this.postList = response.data;
        this.loading = false;
      });
    },
    cancel() {
      this.showDialog = false;
    },
    onConfirm() {
      addVersion(this.muteItem).then(() => {
        this.getList();
        this.cancel();
      });
    },
    onRemoveUser(versionId) {
      deleteVersion(versionId).then(() => {
        this.getList();
      });
    },

    onAddUser() {
      (this.muteItem = {
        channel: this.queryParams.channel,
        version: "",
        remarks: "",
        forcedUpdate: 0,
        sourceUrl: "",
      }),
        (this.showDialog = true);
    },
    onCurrentChange(current) {
      this.queryParams.current = current;
      this.getList();
    },
    changeImage(e) {
      const file = e.target.files[0];
      this.file = file;
      const param = new FormData();
      param.append("file", file);
      e.srcElement.value = "";
      uploadFile(param).then((result) => {
        this.muteItem.sourceUrl = result.data;
      });
    },
    onSelectFile() {
      this.$refs.selectRef.click();
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
  align-items: center;
  margin-bottom: 12px;

  .dialog-label {
    width: 120px;
  }

  .file-choose {
    display: inline-block;
    max-width: 200px;
    padding: 3px 6px;
    overflow: hidden;
    color: white;
    text-overflow: ellipsis;
    word-break: break-all;
    white-space: nowrap;
    cursor: pointer;
    background: #1890ff;
    border-radius: 4px;
  }

  .input-dialog {
    width: 240px;
  }

  .input-version {
    width: 240px;

    :deep(.el-textarea__inner) {
      min-height: 90px !important;
    }
  }
}

.versions {
  font-size: 14px;
  border-radius: 4px;

  .search-items {
    display: flex;
    justify-content: space-between;
    padding: 16px 12px;
    margin-bottom: 12px;
    background: white;

    .input-search {
      width: 150px;
      margin-right: 12px;
      margin-left: 4px;
    }
  }
}
</style>
