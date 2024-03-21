<template>
  <div class="operations">
    <input
      :value="data.inputValue"
      type="file"
      ref="selectRef"
      v-show="false"
      @change="changeImage"
    />
    <div class="search-items">
      <div class="query-item">
        <span>渠道</span>
        <el-select
          v-model="queryParams.channel"
          placeholder="请选择"
          class="input-search"
          @change="handleQuery"
        >
          <el-option
            v-for="item in channelTypes"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </div>
      <div>
        <el-button type="primary" @click="onAddAd">添加广告</el-button>
      </div>
    </div>
    <div class="flex flex-col bg-white mb-3">
      <el-table v-loading="loading" :data="postList" stripe>
        <el-table-column label="名称" align="center" prop="name">
          <template #default="scope">
            <div
              class="flex items-center w-full justify-center"
              v-if="scope.row.channel != 1"
            >
              <img
                style="max-width: 120px; background-size: contain"
                class="bg-contain"
                :src="scope.row.imgUrl"
              />
            </div>
            <span v-else>{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="渠道" align="center" prop="channel">
          <template #default="scope">
            <span>{{ msgTypes[scope.row.channel] }}</span>
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column label="状态" align="center" prop="status">
          <template #default="scope">
            <span>{{ scope.row.status == 1 ? "已上架" : "已下架" }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="280"
          align="center"
          class-name="small-padding fixed-width"
        >
          <template #default="scope">
            <div>
              <el-button
                type="text"
                @click="onEdit(scope.row)"
                style="margin-right: 12px"
                >编辑</el-button
              >
              <el-popconfirm
                @confirm="onDelete(scope.row.id)"
                title="你确定要删除此消息？"
              >
                <template #reference>
                  <el-button class="red-text" type="text">删除</el-button>
                </template>
              </el-popconfirm>
            </div>
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
    <el-dialog
      :visible.sync="showDialog"
      title="添加/编辑"
      width="540px"
      append-to-body
    >
      <el-form ref="form" :model="adItem" :rules="rules" label-width="80px">
        <el-form-item label="渠道" prop="channel">
          <el-select
            v-model="adItem.channel"
            placeholder="请选择"
            class="input-search"
          >
            <el-option
              v-for="item in addChannelTypes"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-if="adItem.channel != 1" label="展示图片" prop="imgUrl">
          <img
            v-if="adItem.imgUrl"
            :style="`max-width: 120px;
              border-radius: ${adItem.channel == 4 ? '8px' : ''};
              background-size: contain;
              background-repeat: no-repeat;
              margin-right: 8px;
              margin-bottom:12px;`"
            :src="adItem.imgUrl"
          />
          <el-button type="primary" @click="onSelectFile">上传图片</el-button>
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="adItem.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="跳转地址" prop="targetUrl">
          <el-input v-model="adItem.targetUrl" placeholder="请输入跳转地址" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select
            v-model="adItem.status"
            placeholder="请选择"
            class="input-search"
            @change="handleQuery"
          >
            <el-option
              v-for="item in status"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            :maxlength="200"
            :rows="4"
            clearable
            show-count
            v-model="adItem.remark"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      <!-- <div class="flex flex-col">
        <div class="flex-item items-center">
          <span class="w-20">渠道</span>
          <el-select
            v-model="adItem.channel"
            placeholder="请选择"
            class="input-search"
          >
            <el-option
              v-for="item in addChannelTypes"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </div>
        <div class="flex-item items-center" v-if="adItem.channel != 1">
          <span class="w-20">展示图片</span>
          <img
            v-if="adItem.imgUrl"
            :style="`max-width: 120px;
              border-radius: ${adItem.channel == 4 ? '8px' : ''};
              background-size: contain;
              background-repeat: no-repeat;
              margin-right: 8px;`"
            :src="adItem.imgUrl"
          />
          <el-button type="primary" @click="onSelectFile">上传图片</el-button>
        </div>
        <div class="flex-item" v-else>
          <span class="w-20">名称</span>
          <el-input class="input-search" v-model="adItem.name" />
        </div>
        <div class="flex-item">
          <span class="w-20">跳转地址</span>
          <el-input
            type="textarea"
            class="input-search"
            :rows="2"
            v-model="adItem.targetUrl"
          />
        </div>
        <div class="flex-item">
          <span class="w-20">状态</span>
          <el-select
            v-model="adItem.status"
            placeholder="请选择"
            class="input-search"
            @change="handleQuery"
          >
            <el-option
              v-for="item in status"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </div>
        <div class="flex-item">
          <span class="w-20">备注</span>
          <el-input
            type="textarea"
            class="input-search"
            :rows="4"
            v-model="adItem.remark"
          />
        </div>
      </div> -->
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">取 消</el-button>
          <el-button type="primary" @click="onConfirm">确 定</el-button>
        </div>
      </template>
    </el-dialog>
    <ImgCroper
      v-if="showImgCroper"
      :imgOptions="options"
      @close="onCloseImgCrop"
      @onImg="onSetImg"
    />
  </div>
</template>

<script>
import { getAdList, updateAd, deleteAd, addAd } from "@/api/operations/index";
import ImgCroper from "@/components/Common/cropper";
export default {
  name: "OperationAd",
  components: { ImgCroper },
  data() {
    return {
      cropRate: {
        2: {
          autoCropWidth: 375, // 默认生成截图框宽度
          autoCropHeight: 48, // 默认生成截图框高度
        },
        3: {
          autoCropWidth: 345, // 默认生成截图框宽度
          autoCropHeight: 80, // 默认生成截图框高度
        },
        4: {
          autoCropWidth: 351, // 默认生成截图框宽度
          autoCropHeight: 165, // 默认生成截图框高度
        },
      },
      options: {
        img: "",
        autoCrop: true, // 是否默认生成截图框
        fixedBox: true, // 固定截图框大小 不允许改变
        outputType: "png", // 默认生成截图为PNG格式
      },
      showImgCroper: false,
      channelTypes: [
        { label: "全部", value: 0 },
        { label: "直播间滚动条", value: 1 },
        { label: "直播间展示位", value: 2 },
        { label: "个人中心", value: 3 },
        { label: "banner", value: 4 },
      ],
      addChannelTypes: [
        { label: "直播间滚动条", value: 1 },
        { label: "直播间展示位", value: 2 },
        { label: "个人中心", value: 3 },
        { label: "banner", value: 4 },
      ],
      status: [
        { label: "上架", value: 1 },
        { label: "下架", value: 2 },
      ],
      msgTypes: {
        0: "全部",
        1: "直播间滚动条",
        2: "直播间展示位",
        3: "个人中心",
        4: "banner",
      },
      showDialog: false,
      adItem: {
        remark: "",
        channel: 1,
        imgUrl: "",
        text: "",
        targetUrl: "",
        name: "",
        status: 1,
        id: null,
      },
      data: {
        file: "",
        inputValue: "",
      },
      total: 0,
      queryParams: {
        current: 1,
        size: 10,
        channel: 0,
      },
      postList: [],
      // 表单校验
      rules: {
        channel: [{ required: true, message: "请选择渠道", trigger: "blur" }],
        name: [
          {
            required: true,
            message: "请输入名称",
            trigger: "blur",
          },
        ],
        imgUrl: [
          {
            required: true,
            message: "请选择图片",
            trigger: "blur",
          },
        ],
        status: [
          {
            required: true,
            message: "请选择状态",
            trigger: "blur",
          },
        ],
        remark: [
          {
            required: true,
            message: "请输入备注",
            trigger: "blur",
          },
        ],
        targetUrl: [
          {
            required: true,
            message: "请输入跳转链接",
            trigger: "blur",
          },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询岗位列表 */
    getList() {
      this.loading = true;
      const queryData = { ...this.queryParams };
      if (!queryData.channel) {
        delete queryData.channel;
      }
      getAdList(queryData).then((response) => {
        this.postList = response.data.records;
        this.total = parseInt(response.data.total);
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.current = 1;
      this.getList();
    },
    onDelete(id) {
      deleteAd(id).then(() => {
        this.$message.success("删除成功");
        this.handleQuery();
      });
    },
    onEdit(item) {
      this.adItem = JSON.parse(JSON.stringify(item));
      this.showDialog = true;
    },
    onConfirm() {
      const onSubmit = () => {
        if (this.adItem.id) {
          updateAd(this.adItem).then(() => {
            this.$message.success("修改成功");
            this.handleQuery();
            this.cancel();
          });
        } else {
          addAd(this.adItem).then(() => {
            this.$message.success("创建成功");
            this.handleQuery();
            this.cancel();
          });
        }
      };
      this.$refs["form"].validate((valid) => {
        if (valid) {
          onSubmit();
        }
      });
    },
    cancel() {
      this.showDialog = false;
    },
    onAddAd() {
      this.adItem = {
        remark: "",
        channel: 1,
        imgUrl: "",
        text: "",
        targetUrl: "",
        name: "",
        status: 1,
        id: null,
      };
      this.showDialog = true;
    },
    onSetImg(url) {
      this.adItem.imgUrl = url;
      this.onCloseImgCrop();
    },
    onCloseImgCrop() {
      this.showImgCroper = false;
    },
    changeImage(e) {
      const file = e.target.files[0];
      this.imageToBase64(file).then((result) => {
        const rateWh = this.cropRate[this.adItem.channel];
        this.options = { ...this.options, ...rateWh };
        this.options.img = result;
        this.showImgCroper = true;
      });
    },
    imageToBase64(file) {
      return new Promise((resolve, reject) => {
        var reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => {
          resolve(reader.result);
        };
        reader.onerror = function (error) {
          reject(error);
        };
      });
    },

    onSelectFile() {
      const selectRef = this.$refs.selectRef;
      selectRef.click();
    },
  },
};
</script>

<style scoped lang="scss">
.flex-item {
  display: flex;
  margin-bottom: 8px;

  .input-search {
    width: 200px;
  }
}

.operations {
  font-size: 14px;
  border-radius: 4px;

  .search-items {
    display: flex;
    justify-content: space-between;
    padding: 16px 12px;
    margin-bottom: 15px;
    background: white;

    .input-search {
      width: 140px;
      margin-right: 12px;
      margin-left: 4px;
    }
  }
}
</style>
