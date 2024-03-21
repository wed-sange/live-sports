const loadVideo: (file: File) => Promise<HTMLVideoElement> = function (
  file: File
) {
  return new Promise(function (resolve, reject) {
    const videoElem = document.createElement("video");
    const dataUrl = URL.createObjectURL(file);
    // 当前帧的数据是可用的
    videoElem.onloadeddata = function () {
      resolve(videoElem);
    };
    videoElem.onerror = function () {
      reject("video 后台加载失败");
    };
    // 设置 auto 预加载数据， 否则会出现截图为黑色图片的情况
    videoElem.setAttribute("preload", "auto");
    videoElem.src = dataUrl;
  });
};

const toThumbFile = blob => new File([blob], "thumb__img.png");

export default async (video: File): Promise<File> => {
  return await loadVideo(video).then(video => {
    return new Promise(resolve => {
      // // 时长
      // const duration = video.duration;
      // // 宽
      // const width = video.videoWidth;
      // // 高
      // const height = video.videoHeight;

      const canvasElem = document.createElement("canvas");
      const { videoWidth, videoHeight } = video;
      canvasElem.width = videoWidth;
      canvasElem.height = videoHeight;
      canvasElem
        .getContext("2d")
        .drawImage(video, 0, 0, videoWidth, videoHeight);

      // 导出成blob文件
      canvasElem.toBlob(blob => {
        // 将blob文件转换成png文件
        resolve(toThumbFile(blob));
      }, "image/png");
    });
  });
};
