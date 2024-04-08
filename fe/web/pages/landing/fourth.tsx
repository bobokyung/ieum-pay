import styles from '@/styles/LandingPage.module.scss';
import { useRouter } from 'next/router';
import { useState } from 'react';

export default function LandingPageFourth() {
  const router = useRouter();

  const [cnt, setCnt] = useState(0);
  const [state1, setState] = useState(false);
  const [state2, setState2] = useState(false);

  const onClickFn = () => {
    if (cnt == 0) setState(true);
    else if (cnt == 1) {
      setState2(true);
      setTimeout(() => {
        router.push('/landing/fifth ');
      }, 800);
    }

    setCnt(cnt + 1);
  };

  return (
    <div className={styles.container} onClick={onClickFn}>
      <div className={styles.fourth}>
        <div className={styles.imgContainer}>
          <div className={styles.img1}>
            <img
              className={state2 ? styles.disapearGlobal : styles.imgMove1}
              src="/landing/payServiceIcon.svg"
              alt="페이서비스 아이콘"
            />
          </div>
          <div className={styles.img2}>
            <img
              className={state2 ? styles.disapearGlobal : styles.imgMove2}
              src="/landing/donationServiceIcon.svg"
              alt="도네이션 서비스 아이콘"
            />
          </div>
        </div>
        <div className={state1 ? styles.text : styles.displayNone}>
          <div className={state2 ? styles.disapearGlobal : ''}>
            <p>간편 결제/송금 페이 서비스</p>
            <p>+</p>
            <p>부담 없는 기부</p>
          </div>
        </div>
      </div>
      <img
        className={styles.nav}
        src="/landing/navigation3.svg"
        alt="네비게이션 아이콘 세번째"
      />
    </div>
  );
}
