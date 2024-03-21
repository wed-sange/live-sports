<template>
  <page-box class="message-search-box">
    <template #header>
      <NavBar title="消息搜索" transparent left-arrow @click-left="onLeftClick" @click-right="onSearch">
        <template #title>
          <div class="message-search__header w-full cursor-pointer">
            <van-field class="keyboard-abr" v-model="search.key" placeholder="搜索你感兴趣的内容">
              <template #left-icon>
                <IconSearch class="message-search__search--icon" />
              </template>
            </van-field>
          </div>
        </template>
        <template #right> <div class="w-full h-full flex items-center px-[24px]" v-ripple>搜索</div> </template>
      </NavBar>
    </template>
    <div class="w-full h-full">
      <MessageSearchList v-if="searchType === 'message'" ref="messageSearchListRef" />
      <MemberSearchList v-if="searchType === 'member'" ref="memberSearchListRef" />
    </div>
  </page-box>
</template>
<script setup lang="ts">
  import MemberSearchList from '@/views/message/components/MemberList/search.vue';
  import MessageSearchList from '@/views/message/components/MessageList/search.vue';

  import { IconSearch } from '@/views/message/components/icon';
  const route = useRoute();
  const searchType = ref(route.query.type || '');
  const router = useRouter();
  const onLeftClick = () => {
    router.go(-1);
  };
  const search = reactive({
    key: '',
  });
  const memberSearchListRef = ref<typeof MemberSearchList | null>(null);
  const messageSearchListRef = ref<typeof MessageSearchList | null>(null);
  const onSearch = () => {
    if (searchType.value === 'member') {
      memberSearchListRef.value && memberSearchListRef.value.handleSearch(search.key);
    }
    if (searchType.value === 'message') {
      messageSearchListRef.value && messageSearchListRef.value.handleSearch(search.key);
    }
  };
</script>
<script lang="ts">
  export default {
    name: 'MessageSearch',
  };
</script>

<style scoped lang="scss">
  .message-search-box {
    .message-search__header {
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
      .message-search__search--icon {
        width: 40px;
        height: 40px;
        display: inline-block;
      }
    }
    :deep().van-nav-bar__title {
      width: 538px;
      max-width: none;
    }
    :deep().van-nav-bar__right {
      padding: 0;
    }
  }
</style>
