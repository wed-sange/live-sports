<template>
  <div class="hot-matches">
    <div class="flex flex-col bg-white">
      <div class="flex justify-between items-center">
        <el-radio-group
          v-model="queryParams.matchType"
          style="margin: 6px"
          @change="onTypeChange"
        >
          <el-radio-button :label="1">足球</el-radio-button>
          <el-radio-button :label="2">篮球</el-radio-button>
        </el-radio-group>
        <el-button type="primary" class="mr-4" @click="createMatch"
          >添加联赛</el-button
        >
      </div>

      <el-table v-loading="loading" :data="postList" stripe>
        <el-table-column label="名称" align="center" prop="competitionName">
          <template #default="scope">
            <span>{{
              `${scope.row.competitionName}${
                scope.row.fullCompetitionName
                  ? "-" + scope.row.fullCompetitionName
                  : ""
              }`
            }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="280"
          align="center"
          class-name="small-padding fixed-width"
        >
          <template #default="scope">
            <div class="flex items-center w-full justify-center">
              <el-popconfirm
                @confirm="onRemove(scope.row.id, scope.row.index)"
                title="是否要移除？"
              >
                <template #reference>
                  <el-button class="red-text" type="text">移除</el-button>
                </template>
              </el-popconfirm>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog
      :visible.sync="showDialog"
      title="添加联赛"
      width="540px"
      append-to-body
    >
      <div class="flex flex-col">
        <div style="display: flex; margin-bottom: 12px; align-items: center">
          <div style="width: 80px">类型</div>
          <el-select
            v-model="addData.matchType"
            placeholder="请选择"
            class="input-search"
          >
            <el-option
              v-for="item in types"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </div>
        <div style="display: flex">
          <div style="width: 80px; align-items: center">选择联赛</div>
          <el-select
            filterable
            remote
            reserve-keyword
            :remote-method="ondebounceSearch"
            :loading="loading"
            v-model="addData.competitionId"
            placeholder="请选择"
            class="w-[200px]"
          >
            <el-option
              v-for="item in mathList"
              :key="item.competitionId"
              :label="`${item.competitionName}(${item.fullCompetitionName})`"
              :value="item.competitionId"
            />
          </el-select>
        </div>
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
import { matchHotList, searchMatch, hotRemove, hotAdd } from "@/api/hot/index";
import _ from "lodash";
export default {
  name: "Index",
  data() {
    return {
      ondebounceSearch: null,
      types: [
        { label: "足球", value: 1 },
        { label: "篮球", value: 2 },
      ],
      postList: [],
      mathList: [],
      addData: { matchType: 1, competitionId: null },
      showDialog: false,
      queryParams: {
        current: 1,
        size: 10,
        matchType: 1,
      },
      loading: false,
    };
  },
  created() {
    this.ondebounceSearch = _.debounce(this.onGetSearch, 400);
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      matchHotList(this.queryParams)
        .then((response) => {
          this.postList = response.data;
          this.loading = false;
        })
        .catch(() => {
          this.loading = false;
        });
    },
    onRemove(id) {
      hotRemove(`${id}`).then(() => {
        this.getList();
      });
    },
    createMatch() {
      this.addData.matchType = this.queryParams.matchType;
      this.addData.competitionId = "";
      this.mathList = [];
      this.showDialog = true;
    },
    onTypeChange() {
      this.queryParams.current = 1;
      this.getList();
    },
    cancel() {
      this.showDialog = false;
    },
    onConfirm() {
      const addItem = this.mathList.find(
        (item) => item.competitionId === this.addData.competitionId
      );
      hotAdd(addItem).then(() => {
        this.cancel();
        this.getList();
      });
    },
    onGetSearch(competitionName) {
      if (!competitionName) {
        return;
      }
      searchMatch({ competitionName, matchType: this.addData.matchType }).then(
        (res) => {
          this.mathList = res.data;
        }
      );
    },
  },
};
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }

  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }

  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>
