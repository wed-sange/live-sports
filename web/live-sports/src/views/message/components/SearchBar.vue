<template>
  <div class="search-bar-box w-full">
    <div class="search-bar__header w-full px-[32px] pt-[36px] cursor-pointer" @click="handleClick">
      <van-field v-model="search.key" placeholder="搜索你感兴趣的内容" class="pointer-events-none">
        <template #left-icon>
          <IconSearch class="search-bar__search--icon" />
        </template>
      </van-field>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { IconSearch } from './icon';
  const props = withDefaults(
    defineProps<{
      type?: string;
    }>(),
    {
      type: 'member',
    },
  );
  const search = reactive({
    key: '',
  });
  const router = useRouter();
  const handleClick = () => {
    router.push({
      name: 'MessageSearch',
      query: {
        type: props.type,
      },
    });
  };
</script>
<script lang="ts">
  export default {
    name: 'SearchBar',
  };
</script>

<style scoped lang="scss">
  .search-bar-box {
    .search-bar__header {
      :deep().van-field {
        --van-cell-background: #18152b;
        --van-field-input-text-color: #fff;
        --van-field-placeholder-text-color: #8a91a0;
        font: 400 24px / 48px 'PingFang HK';
        letter-spacing: -0.816px;
        border-radius: 16px;
        .van-field__left-icon {
          align-self: center;
          height: 44px;
        }
      }
    }
    .search-bar__search--icon {
      width: 40px;
      height: 40px;
      display: inline-block;
      svg {
        width: 100%;
        height: 100%;
      }
    }
  }
</style>
