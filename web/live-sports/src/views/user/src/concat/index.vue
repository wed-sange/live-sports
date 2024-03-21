<template>
  <page-box class="concat-page">
    <template #header> <AppHeader /><NavBar title="意见反馈" left-arrow @click-left="onLeftClick" /></template>
    <div class="w-full h-full overflow-auto">
      <div class="concat-page-gap"></div>
      <div class="form-group w-full">
        <div class="from-group__label w-full">问题类型</div>
        <div class="from-group__info w-full grid grid-cols-[repeat(3,220px)] gap-x-[21px] gap-y-[30px]">
          <template v-for="item in typeList" :key="item.key">
            <div class="w-full">
              <van-button class="form-group__tag" :class="{ active: model.typeValue === item.key }" @click="handleTypeClick(item)">
                {{ item.label }}
              </van-button>
            </div>
          </template>
        </div>
      </div>
      <div class="form-group w-full">
        <div class="from-group__label w-full">反馈内容</div>
        <div class="from-group__info w-full" :class="{ zero: model.content === '' }">
          <van-field
            v-model="model.content"
            class="from-group__textarea"
            :autosize="false"
            type="textarea"
            maxlength="200"
            placeholder="请输入反馈内容"
            show-word-limit
          />
        </div>
      </div>
      <div class="form-group w-full">
        <div class="from-group__label w-full">添加图片<span class="from-group__label--tip">(最多三张)</span></div>
        <div class="from-group__info w-full">
          <van-uploader v-model="model.imgList" multiple :max-count="3" upload-icon="plus" :after-read="afterRead">
            <div class="uploader-custom-target">
              <div class="uploader-custom-target__icon"></div>
              <div class="uploader-custom-target__text">上传图片</div>
            </div>
            <template #preview-cover>
              <div v-ripple class="w-full h-full rounded-[inherit]"></div>
            </template>
          </van-uploader>
        </div>
      </div>

      <div class="form-group w-full mt-[124px]">
        <van-button class="form-group__sure" :disabled="formDisabled" @click="sure">提交</van-button>
      </div>
    </div>
  </page-box>
</template>
<script setup lang="ts">
  import { uploadCommonImg } from '@/api/user';
  import { addFeedback } from '@/api/message';
  import { showToast, type UploaderFileListItem } from 'vant';
  import { createLoading, closeLoading } from '@/components/SysLoading';

  const router = useRouter();
  const onLeftClick = () => {
    router.go(-1);
  };
  const model = reactive<{
    typeValue: string;
    content: string;
    imgList: UploaderFileListItem[];
  }>({
    typeValue: '',
    content: '',
    imgList: [],
  });
  const formDisabled = computed(() => model.typeValue === '' || model.content === '');
  const typeList = reactive([
    { label: '更新问题', key: '1' },
    { label: '直播相关', key: '2' },
    { label: '产品体验', key: '3' },
    { label: '聊天相关', key: '4' },
    { label: '比赛相关', key: '5' },
    { label: '其他', key: '6' },
  ]);
  const handleTypeClick = (item: (typeof typeList)[number]) => {
    model.typeValue = item.key;
  };
  const afterRead = async (item: UploaderFileListItem) => {
    if (item.file) {
      item.status = 'uploading';
      item.message = '上传中...';
      try {
        const response = await uploadCommonImg({
          file: item.file,
        });
        item.url = response;
        item.status = 'done';
        item.message = '';
      } catch (error) {
        item.status = 'failed';
        item.message = '上传失败';
      }
    }
  };
  const sure = async () => {
    if (formDisabled.value) return;
    if (!model.typeValue) {
      showToast({
        className: 'custom-toast',
        message: '请选择问题类型',
      });
      return;
    }
    if (!model.content) {
      showToast({
        className: 'custom-toast',
        message: '请输入反馈内容',
      });
      return;
    }
    createLoading();
    await addFeedback({
      feedbackType: model.typeValue,
      feedbackContent: model.content,
      feedbackImage: model.imgList.length > 0 ? model.imgList.map((cur) => cur.url!) : [],
    }).finally(() => {
      closeLoading();
    });
    showToast({
      className: 'custom-toast',
      message: '提交成功',
    });
    onLeftClick();
  };
</script>
<script lang="ts">
  export default {
    name: 'UserConcat',
  };
</script>
<style scoped lang="scss">
  .concat-page.page-box {
    background: #fff;
  }
  .concat-page-gap {
    width: 100%;
    height: 24px;
    background: #f2f3f7;
    margin-bottom: 30px;
  }
  .form-group {
    padding: 0 24px 40px;
  }
  .from-group__label {
    font: 500 30px / normal 'PingFang SC';
    color: #37373d;
    padding-bottom: 30px;
    padding-left: 2px;
  }
  .from-group__label--tip {
    font: 500 30px / normal 'PingFang SC';
    color: #94999f;
    padding-left: 12px;
  }
  :deep().van-button.form-group__tag {
    --van-button-default-background: #fff;
    --van-button-default-color: #94999f;
    --van-button-radius: 8px;
    border: 2px solid #d7d7e7;
    width: 100%;
    height: 80px;
    // box-sizing: content-box;
    padding: 0;
    font: 400 28px / 80px 'PingFang SC';
    .van-button__text {
      font: inherit;
      color: inherit;
    }
    &.active {
      --van-button-default-background: #f7fff9;
      --van-button-default-color: #34a853;
      border-color: #34a853;
      &::after {
        content: '';
        width: 42px;
        height: 32px;
        position: absolute;
        bottom: -2px;
        right: -2px;
        z-index: 1;
        background: url('@/assets/img/user/icon_concat_active.png') no-repeat, transparent;
        background-size: auto 100%;
        background-position: 0 0;
        pointer-events: none;
      }
    }
  }
  .from-group__info {
    &.zero {
      :deep().van-field.from-group__textarea {
        .van-field__word-limit {
          .van-field__word-num {
            color: #94999f;
          }
        }
      }
    }
    :deep().van-field.from-group__textarea {
      --van-cell-background: #f2f3f7;
      --van-field-input-text-color: #37373d;
      --van-field-placeholder-text-color: #94999f;
      --van-cell-vertical-padding: 30px;
      --van-cell-horizontal-padding: 30px;
      font: 400 28px / normal 'PingFang SC';
      border-radius: 20px;
      .van-field__control {
        min-height: 256px;
      }
      .van-field__word-limit {
        font: 400 28px / var(--van-field-word-limit-line-height) 'PingFang SC';
        color: #94999f;
        margin-top: 4px;
        margin-bottom: 4px;
        .van-field__word-num {
          color: #37373d;
        }
      }
    }
    :deep().van-uploader {
      --van-uploader-upload-background: #f2f3f7;
      --van-uploader-upload-active-color: var(--van-uploader-upload-background);
      .van-uploader__upload {
        border-radius: 20px;
        .van-icon {
          font-size: 48px;
          color: #8a91a0;
        }
      }
      .uploader-custom-target {
        width: 220px;
        height: 220px;
        background: #f2f3f7;
        border-radius: 20px;
        display: flex;
        flex-flow: column;
        align-items: center;
        justify-content: center;

        @include use-active-style();
        .uploader-custom-target__icon {
          width: 58px;
          height: 58px;
          background: url('@/assets/img/user/icon_concat_upload.png') no-repeat, transparent;
          background-size: 100% 100%;
          background-position: 0 0;
        }
        .uploader-custom-target__text {
          font: 400 24px / normal 'PingFang SC';
          color: #94999f;
          padding-top: 21px;
        }
      }
      .van-uploader__preview {
        --van-padding-xs: 20px;
        --van-uploader-size: 220px;
        &:last-child {
          margin-right: 0;
        }
      }
      .van-image {
        border-radius: 20px;
        .van-uploader__preview-cover {
          border-radius: inherit;
        }
        .van-image__img {
          border-radius: inherit;
          overflow: hidden;
        }
      }
      .van-uploader__preview-delete {
        top: 20px;
        right: 20px;
      }
      .van-uploader__preview-delete--shadow {
        width: 40px;
        height: 40px;
        background: url('@/assets/img/user/icon_concat_delete.png') no-repeat, transparent;
        background-size: 100% 100%;
        background-position: 0 0;
        border-radius: 100%;
        .van-uploader__preview-delete-icon {
          display: none;
          opacity: 0;
        }
      }
    }
  }
  .form-group__sure.van-button {
    --van-button-default-background: #34a853;
    --van-button-default-color: #fff;
    --van-button-radius: 48px;
    border: none;
    width: 100%;
    height: 80px;
    font: 500 30px / 44px 'PingFang SC';
    .van-button__text {
      font: inherit;
      color: inherit;
    }
    &.van-button--disabled {
      --van-button-default-background: #f2f3f7;
      --van-button-default-color: #94999f;
      --van-button-disabled-opacity: 1;
    }
  }
</style>
