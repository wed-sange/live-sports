<template>
  <teleport to="body">
    <Transition name="live-start-notify-toggle-transition">
      <div class="live-start-box" v-if="show">
        <div class="live-start__wrap" @click.stop="handleClick">
          <div class="live-start__cover">
            <UseImg class="w-full h-full rounded-[100%]" :src="liveInfo.cover" alt="logo" not-fill-icon>
              <template #error>
                <img class="w-full h-full" :src="defaultAnchorLogo" alt="error" />
              </template>
            </UseImg>
            <div class="live-start__ripple"> <IconRipple /></div>
          </div>
          <div class="live-start__info">
            <div class="live-start__info--title">{{ liveInfo.name }}</div>
            <div class="live-start__info--team">
              <div class="live-start__info--state">正在直播</div>
              <div class="live-start__info--desc">{{ liveInfo.notice }}</div>
            </div>
          </div>
          <div class="live-start__other">
            <van-button class="live-start__btn"> 前往 </van-button>
          </div>
        </div>
        <div class="live-start__close" @click.stop="close">
          <IconClose @an-complete="onCloseAnimationEnd" />
        </div>
      </div>
    </Transition>
  </teleport>
</template>
<script setup lang="ts">
  import defaultAnchorLogo from '@/assets/img/user/default_anchor.png';
  import { useMessageStore } from '@/store/modules/message';
  import IconClose from './IconClose/index.vue';
  import IconRipple from './IconRipple/index.vue';

  const liveInfo = reactive({
    type: 'football', // basketball
    id: '',
    cover: '',
    name: '',
    notice: '',
    matchId: '',
    liveId: '',
    matchType: '',
  });
  const router = useRouter();
  const handleClick = () => {
    if (!liveInfo.liveId) return;
    router.push({
      path: '/roomDetail',
      query: { id: liveInfo.matchId, liveId: liveInfo.liveId, type: liveInfo.matchType, userId: liveInfo.id },
    });
    close();
  };
  const route = useRoute();
  const disabledOpenNotify = computed(() => {
    let res = true;
    if (route.name === 'Home' || route.name === 'Match' || route.name === 'Message') {
      res = false;
    }
    return res;
  });
  watch(disabledOpenNotify, () => {
    if (disabledOpenNotify.value) {
      close();
    }
  });
  const show = ref(false);
  // let timer: null | number = null;
  const close = () => {
    show.value = false;
    // timer && clearTimeout(timer);
    // timer = null;

    liveInfo.type = 'football'; // basketball
    liveInfo.id = '';
    liveInfo.cover = '';
    liveInfo.name = '';
    liveInfo.notice = '';
    liveInfo.matchId = '';
    liveInfo.liveId = '';
    liveInfo.matchType = '';
  };

  const open = () => {
    // timer && clearTimeout(timer);
    // timer = null;
    if (disabledOpenNotify.value) return;
    show.value = true;
    // timer = setTimeout(() => {
    //   close();
    // }, 3200) as unknown as number;
  };
  const onCloseAnimationEnd = () => {
    close();
  };
  const messageStore = useMessageStore();
  const createListener = async () => {
    const messageServer = await messageStore.getMessageServerAsync();
    messageServer.chatEmitter.on('liveStartNotify', async (res) => {
      const data = res.data || {};
      if (!data.userId || disabledOpenNotify.value) return;
      const type = data.matchType === 1 ? 'football' : 'basketball';
      liveInfo.type = type;
      liveInfo.id = data.userId + '';
      liveInfo.cover = data.userLogo || '';
      liveInfo.name = data.nickName || '';
      const homeTeamName = data.homeTeamName || '';
      const visitTeamName = data.awayTeamName || '';
      liveInfo.notice = type === 'football' ? homeTeamName + 'VS' + visitTeamName : visitTeamName + 'VS' + homeTeamName;
      liveInfo.matchId = data.matchId ? data.matchId + '' : '';
      liveInfo.liveId = data.id ? data.id + '' : '';
      liveInfo.matchType = data.matchType ? data.matchType + '' : '';
      open();
    });
  };

  createListener();
</script>
<script lang="ts">
  export default {
    name: 'LiveStartNotify',
  };
</script>
<style scoped lang="scss" src="./index.scss"></style>
