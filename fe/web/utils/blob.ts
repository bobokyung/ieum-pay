export function dataURLtoBlob(dataURL: string): Blob {
  const arr: string[] = dataURL.split(',');
  const mime: RegExpMatchArray | null = arr[0].match(/:(.*?);/);
  if (!mime) {
    throw new Error('Invalid data URL.');
  }
  const mimeType: string = mime[1];
  const bstr: string = atob(arr[1]);
  let n: number = bstr.length;
  const u8arr: Uint8Array = new Uint8Array(n);
  while (n--) {
    u8arr[n] = bstr.charCodeAt(n);
  }
  return new Blob([u8arr], { type: mimeType });
}
