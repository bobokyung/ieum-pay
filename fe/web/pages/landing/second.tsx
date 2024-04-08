import styles from '@/styles/LandingPage.module.scss';
import { useRouter } from 'next/router';
import { useState } from 'react';

export default function LandingPageSecond() {
  const router = useRouter();
  const [state1, setState] = useState(false);

  const onClickFn = () => {
    setState(true);
    setTimeout(() => {
      router.push('/landing/third');
    }, 1000);
  };

  return (
    <div className={styles.container}>
      <div className={styles.second}>
        <div
          className={state1 ? styles.disapearsecond : ''}
          onClick={onClickFn}
        >
          <img
            className={styles.top}
            src="/landing/simplePayment.svg"
            alt="간편결제 아이콘"
          />
          <img
            className={styles.bottom}
            src="/landing/simpleTransfer.svg"
            alt="간편송금 아이콘"
          />
        </div>
        <img
          className={styles.nav}
          src="/landing/navigation1.svg"
          alt="네비게이션 아이콘 첫번째"
        />
      </div>
    </div>
  );
}
