import ClipboardJS from 'clipboard';
export const copyText = (value: string) => {
  return new Promise<boolean>((resolve) => {
    const btn = document.createElement('div');
    let instance: any = new ClipboardJS(btn, {
      text: () => {
        return value;
      },
    });
    instance.on('success', function (e) {
      instance.destroy();
      instance = null;
      e.clearSelection();
      resolve(true);
    });

    instance.on('error', function () {
      instance.destroy();
      instance = null;
      resolve(false);
    });
    btn.click();
    btn.remove();
  });
  //   const ta = document.createElement('textarea');
  //   ta.value = value ?? '';
  //   ta.style.position = 'absolute';
  //   ta.style.opacity = '0';
  //   document.body.appendChild(ta);
  //   ta.select();
  //   document.execCommand('copy');
  //   ta.blur();
  //   ta.remove();
};
