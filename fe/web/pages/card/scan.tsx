import { useEffect, useRef, useState } from 'react';
import classes from './card.module.scss';
import Button from '@/stories/Button';
import { postCardImage } from '@/api/paymentAxios';
import CloseIcon from '@/components/icons/CloseIcon';
import ZapIcon from '@/components/icons/ZapIcon';
import ZapOffIcon from '@/components/icons/ZapOffIcon';
import { useRouter } from 'next/router';
import {
  CircularProgress,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  useDisclosure,
} from '@nextui-org/react';
import { getOSByUserAgent } from '@toss/utils';
import Link from 'next/link';

function ScanCardPage() {
  let videoRef = useRef<HTMLVideoElement>(null);
  let myStream = useRef<MediaStream>();
  const [isFlashOn, setIsFlashOn] = useState(false);
  const [hasFlash, setHasFlash] = useState(false);
  const router = useRouter();
  const [modalValue, setModalValue] = useState(false);
  const [data, setData] = useState({
    cardNumber: 'dummy',
    validThru: '',
  });

  const { isOpen, onOpen, onOpenChange } = useDisclosure();

  function toggleFlash() {
    //@ts-expect-error
    const newVal = !myStream.current?.getVideoTracks()[0].getSettings()?.torch;
    myStream.current?.getVideoTracks()[0].applyConstraints({
      torch: newVal,
    } as MediaTrackConstraints);
    setIsFlashOn(newVal);
  }
  function takePicture() {
    let rem = parseFloat(
      window.getComputedStyle(document.documentElement).fontSize,
    );
    const canvas = document.createElement('canvas');
    const ctx = canvas.getContext('2d');
    if (!videoRef.current) return;
    if (!ctx) return;
    canvas.width = videoRef.current.getBoundingClientRect().width;
    canvas.height = videoRef.current.getBoundingClientRect().height;
    let img = videoRef.current;

    let x = 0;
    let y = 0;
    let w = ctx.canvas.width;
    let h = ctx.canvas.height;

    console.log('ctx', ctx.canvas.width);

    // default offset is center
    let offsetX = 0.5;
    let offsetY = 0.5;

    // keep bounds [0.0, 1.0]
    if (offsetX < 0) offsetX = 0;
    if (offsetY < 0) offsetY = 0;
    if (offsetX > 1) offsetX = 1;
    if (offsetY > 1) offsetY = 1;

    var iw = img.videoWidth,
      ih = img.videoHeight,
      r = Math.min(w / iw, h / ih),
      nw = iw * r, // new prop. width
      nh = ih * r, // new prop. height
      cx,
      cy,
      cw,
      ch,
      ar = 1;

    // decide which gap to fill
    if (nw < w) ar = w / nw;
    if (Math.abs(ar - 1) < 1e-14 && nh < h) ar = h / nh; // updated
    nw *= ar;
    nh *= ar;

    // calc source rectangle
    cw = iw / (nw / w);
    ch = ih / (nh / h);

    cx = (iw - cw) * offsetX;
    cy = (ih - ch) * offsetY;

    // make sure source rectangle is valid
    if (cx < 0) cx = 0;
    if (cy < 0) cy = 0;
    if (cw > iw) cw = iw;
    if (ch > ih) ch = ih;

    // fill image in dest. rectangle
    ctx.drawImage(img, cx, cy, cw, ch, x, y, w, h);
    let cropWidth = 19 * rem;
    let cropHeight = 12.667 * rem;
    let startX = (w - cropWidth) / 2;
    let startY = (h - cropHeight) / 2;
    let imgData = ctx.getImageData(startX, startY, cropWidth, cropHeight);

    // 새 Canvas 요소 생성
    var croppedCanvas = document.createElement('canvas');
    var croppedCtx = croppedCanvas.getContext('2d');

    // 새 Canvas에 crop한 이미지 데이터를 그림
    croppedCanvas.width = cropWidth;
    croppedCanvas.height = cropHeight;
    croppedCtx?.putImageData(imgData, 0, 0);

    const link = document.createElement('a');
    link.download = 'card.png';
    link.href = croppedCanvas.toDataURL('image/png');
    var file = new File([link.href], 'card.png');

    // Data URL을 Blob으로 변환
    function dataURLtoBlob(dataURL: string): Blob {
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
    const blob: Blob = dataURLtoBlob(link.href);

    // FormData에 Blob 추가
    const formData: FormData = new FormData();
    formData.append('img', blob, 'card.png');

    // link.click();

    onOpen();
    postCardImage(formData).then((res) => {
      console.log(res.data.data);
      setData(res.data.data);
    });
  }
  function handleClick() {
    takePicture();
  }
  async function getUserMedia() {
    try {
      let devices = await navigator.mediaDevices.enumerateDevices();
      const camera = devices
        .filter((device) => device.kind === 'videoinput')
        .map((e) => e.deviceId);
      if (getOSByUserAgent() === 'ios') {
        myStream.current = await navigator.mediaDevices.getUserMedia({
          // OCR 성능 향상을 위한 옵션들 (마지막 카메라가 대부분의 디바이스에서 광각이 아니므로 선명하게 찍을 수 있음)
          video: {
            facingMode: { ideal: 'environment' },
          } as MediaTrackConstraints,
        });
      } else {
        myStream.current = await navigator.mediaDevices.getUserMedia({
          // OCR 성능 향상을 위한 옵션들 (마지막 카메라가 대부분의 디바이스에서 광각이 아니므로 선명하게 찍을 수 있음)
          video: {
            deviceId: camera.length
              ? { ideal: camera[camera.length - 1] }
              : undefined,
            width: { ideal: 1920 },
            height: { ideal: 1920 },
            facingMode: { ideal: 'environment' },
            focusMode: { ideal: 'continuous' },
            whiteBalanceMode: { ideal: 'continuous' },
            zoom: { ideal: 1 },
          } as MediaTrackConstraints,
        });
      }

      if (
        //@ts-expect-error
        myStream.current.getVideoTracks()[0].getCapabilities().torch !==
        undefined
      ) {
        setHasFlash(true);
      }
      if (videoRef.current) {
        videoRef.current.srcObject = myStream.current;
      }
    } catch (e) {
      console.log(e);
    }
  }

  const stopStream = (stream: MediaStream | null) => {
    if (stream) {
      stream.getTracks().forEach((track) => {
        stream.removeTrack(track);
        track.stop();
      });
    }
  };

  useEffect(() => {
    getUserMedia();
    return () => {
      if (myStream.current) {
        stopStream(myStream.current);
      }
    };
  }, []);
  return (
    <>
      <video
        className={classes.video}
        autoPlay
        playsInline
        ref={videoRef}
      ></video>
      <div className={classes.card}>
        <p>카드를 사각형 안에 맞춰주세요</p>
        <div className={classes['link-wrapper']}>
          <Link href="/card" className={classes.link}>
            번호 직접입력
          </Link>
        </div>
      </div>
      <div className={classes.overlay}>
        <header>
          <button onClick={toggleFlash}>
            {hasFlash && (isFlashOn ? <ZapIcon /> : <ZapOffIcon />)}
          </button>
          <h1>카드 스캔</h1>
          <CloseIcon color="white" />
        </header>

        <Button primary onClick={handleClick}>
          촬영하기
        </Button>
      </div>
      <Modal
        className={classes.modalComp}
        isOpen={isOpen}
        onOpenChange={onOpenChange}
      >
        <ModalContent>
          {(onClose) => (
            <>
              <ModalBody className={classes.modalContainer}>
                <div>
                  {data.cardNumber != 'dummy' ? (
                    data.cardNumber == '' ? (
                      <div>인식 오류가 발생했습니다 다시 시도해주세요</div>
                    ) : (
                      <>
                        {router.push(
                          {
                            pathname: '/card',
                            query: {
                              cardNum: data.cardNumber,
                              validThru: data.validThru,
                            },
                          },
                          '/card',
                        )}
                        {onClose()}
                      </>
                    )
                  ) : (
                    <CircularProgress
                      color="secondary"
                      aria-label="Loading..."
                    />
                  )}
                </div>
              </ModalBody>
              <ModalFooter className={classes.modalFooter}>
                {data.cardNumber != 'dummy' ? (
                  data.cardNumber == '' ? (
                    <>
                      <Button
                        size="thin"
                        onClick={() => {
                          setData({ cardNumber: 'dummy', validThru: '' });
                          onClose();
                        }}
                      >
                        다시 촬영하기
                      </Button>
                    </>
                  ) : (
                    <Button
                      size="thin"
                      onClick={() => {
                        onClose;
                        router.push(
                          {
                            pathname: '/card',
                            query: {
                              cardNum: data.cardNumber,
                              validThru: data.validThru,
                            },
                          },
                          '/card',
                        );
                      }}
                    >
                      확인
                    </Button>
                  )
                ) : (
                  ''
                )}
              </ModalFooter>
            </>
          )}
        </ModalContent>
      </Modal>
    </>
  );
}

export default ScanCardPage;
