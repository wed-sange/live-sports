<template>
  <img class="object-cover" :src="src || ''" alt="" @error="setErrorImg" />
</template>
<script>
  import default_Img from '@/assets/img/common/default_Img.png'; // 地区
  import defaultMatch from '@/assets/img/common/default-match-img.png';

  export default {
    name: 'DefaultImg',
    props: {
      type: {
        type: String,
        default: '',
      },
      src: {
        type: String,
        default: '',
      },
    },
    setup(props) {
      const setErrorImg = (event) => {
        switch (props.type) {
          case 'default':
            event.target.src = default_Img;
            break;
          default:
            event.target.src = defaultMatch;
            break;
        }
      };
      return {
        setErrorImg,
      };
    },
  };
</script>
<style scoped></style>
