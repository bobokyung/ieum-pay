import styles from '@/styles/LandingPage.module.scss';
import { useRouter } from 'next/router';
import { useEffect, useState } from 'react';
import Button from '@/stories/Button';

export default function LandingPageFourth() {
  const router = useRouter();

  const [changeCircle, setChangeCircle] = useState(false);
  const [changeToLogoWire, setChangeToLogoWire] = useState(false);
  const [changeLogo, setChangeLogo] = useState(false);

  useEffect(() => {
    const timeout1 = setTimeout(() => {
      setChangeLogo(true);
    }, 700);

    return () => {
      clearTimeout(timeout1);
    };
  }, []);

  return (
    <div className={styles.container}>
      <div className={styles.start}>
        <div className={styles.imgContainer}>
          <img
            className={styles.imgElem}
            src="/landing/logoEllipse5.svg"
            alt="이음페이 로고"
          />
        </div>
        <div className={styles.imgContainer}>
          <img
            className={styles.imgMove1}
            src="/landing/logoEllipse6.svg"
            alt="작은 이음페이 로고"
          />
          <img
            className={styles.logo}
            src="/landing/ieumpay-logo-black.svg"
            alt="이음페이 로고 글자"
          />
        </div>
      </div>

      <div className={styles.btn}>
        <Button
          primary
          onClick={() => {
            router.push('/user');
          }}
        >
          시작하기
        </Button>
      </div>
    </div>
  );
}
