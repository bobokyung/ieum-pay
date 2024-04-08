import styles from '@/styles/LandingPage.module.scss';
import { useRouter } from 'next/router';
import { useEffect } from 'react';

export default function LandingPage() {
  const router = useRouter();

  useEffect(() => {
    const timeout = setTimeout(() => {
      router.push('/landing/second');
    }, 3700);
    return () => clearTimeout(timeout);
  }, []);

  return (
    <div className={styles.constainer}>
      <div className={styles.first}>
        <div className={styles.logoContainer}>
          <img
            className={styles.logo}
            src="/icon-512x512.png"
            alt="이음페이 로고 그림"
          />
          <img
            className={styles.logoLan}
            src="/landing/ieumpay-logo.svg"
            alt="이음페이 로고 글자"
          />
        </div>
      </div>
    </div>
  );
}
