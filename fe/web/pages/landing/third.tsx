import styles from '@/styles/LandingPage.module.scss';
import { useRouter } from 'next/router';
import { useState } from 'react';

export default function LandingPageThird() {
  const router = useRouter();

  const [cnt, setCnt] = useState(0);
  const [state1, setState] = useState(false);
  const [state2, setState2] = useState(false);

  const onClickFn = () => {
    if (cnt == 0) {
      setState(true);
      setCnt(cnt + 1);
    } else if (cnt == 1) {
      setState2(true);
      setTimeout(() => {
        router.push('/landing/fourth ');
      }, 800);
    }
  };

  return (
    <div className={styles.container} onClick={onClickFn}>
      <div className={state2 ? styles.constainerMove : ''}>
        <div className={styles.third}>
          <div className={styles.imgContainer}>
            <img
              className={styles.img}
              src="/landing/simpleDonation.svg"
              alt="간편기부 아이콘"
            />
          </div>
          <div className={state1 ? styles.text : styles.displayNone}>
            <p>결제 후 남은 잔돈을</p>
            <p>부담없이 기부해 보세요</p>
          </div>
        </div>
      </div>
      <img
        className={styles.nav}
        src="/landing/navigation2.svg"
        alt="네비게이션 아이콘 두번째"
      />
    </div>
  );
}
