// /src/pages/index.js
import React, { useEffect, useState } from 'react';
import { initializeApp } from 'firebase/app';
import { getMessaging, onMessage, getToken } from 'firebase/messaging';
import Button from '@/stories/Button';
import classes from './notification.module.scss';
import HeaderMain from '@/stories/HeaderMain';
import Header from '@/components/Header';
import { useRouter } from 'next/router';
import Lottie from 'react-lottie-player';
import notificationLottie from '@/public/lottie/notification.json';
import { postFCMToken } from '@/api/notificationAxios';
import {
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader,
  Button as NextUIButton,
} from '@nextui-org/react';
import dayjs from 'dayjs';
const Index = () => {
  const router = useRouter();
  const [isOpen, setIsOpen] = useState(false);
  function handleClick() {
    // 브라우저에 알림 권한을 요청합니다.
    async function getPermission() {
      const permission = await Notification.requestPermission();
      if (permission !== 'granted') {
        alert('알림을 수동으로 허용해야 하는 기기입니다다');
      } else {
        await onMessageFCM();
      }
    }
    getPermission();
  }
  const onMessageFCM = async () => {
    // 이곳에도 아까 위에서 앱 등록할때 받은 'firebaseConfig' 값을 넣어주세요.
    const firebaseApp = initializeApp({
      apiKey: 'AIzaSyBcJe5zVv9psAQ3QrSY3_ePlUUodHnPR3w',
      authDomain: 'ieum-pay.firebaseapp.com',
      projectId: 'ieum-pay',
      storageBucket: 'ieum-pay.appspot.com',
      messagingSenderId: '991121266334',
      appId: '1:991121266334:web:e359ed60dbe2c9798f8d8f',
    });

    const messaging = getMessaging(firebaseApp);

    // 이곳 vapidKey 값으로 아까 토큰에서 사용한다고 했던 인증서 키 값을 넣어주세요.
    getToken(messaging, {
      vapidKey:
        'BFWLEt5TzuQgvi0kpKOb6JGRLQ0jzHt6dWXY2vXC3GY53FT9KMHvsC0peUGwY6fFgg7yu6k_FVpcuDRm0jzfxos',
    })
      .then((currentToken) => {
        if (currentToken) {
          // 정상적으로 토큰이 발급되면 콘솔에 출력합니다.
          console.log(currentToken);
          postFCMToken(currentToken);
        } else {
          console.log(
            'No registration token available. Request permission to generate one.',
          );
        }
      })
      .catch((err) => {
        console.log('An error occurred while retrieving token. ', err);
      });

    // 메세지가 수신되면 역시 콘솔에 출력합니다.
    onMessage(messaging, (payload) => {
      console.log('Message received. ', payload);
    });
  };

  return (
    <>
      <main className={classes.main}>
        <h1>
          입출금 및 모금 정보를 받으려면
          <br /> 알림 권한이 필요해요
        </h1>
        <Lottie
          loop
          animationData={notificationLottie}
          play
          style={{ width: '20rem', height: '20rem' }}
        />
        <div className={classes['btn-group']}>
          <Button
            primary
            onClick={() => {
              setIsOpen(true);
              handleClick();
            }}
          >
            동의해요
          </Button>
          <button
            className={classes['btn-negative']}
            onClick={() => router.push('/main')}
          >
            나중에 할게요
          </button>
        </div>
      </main>
      <Modal isOpen={isOpen} placement="center">
        <ModalContent>
          {(onClose) => (
            <>
              <ModalHeader className="flex flex-col gap-1"></ModalHeader>
              <ModalBody>
                <p>알림에 동의했어요</p>
                <p>동의 일시: {dayjs().format('YYYY-MM-DD HH:mm')}</p>
              </ModalBody>
              <ModalFooter className="w-100 flex-col">
                <NextUIButton
                  color="primary"
                  onPress={() => router.push('/main')}
                >
                  확인
                </NextUIButton>
              </ModalFooter>
            </>
          )}
        </ModalContent>
      </Modal>
    </>
  );
};

export default Index;
