import styles from '@/styles/LandingPage.module.scss';
import { useRouter } from 'next/router';
import { useEffect, useState } from 'react';

export default function LandingPageFourth() {
  const router = useRouter();

  useEffect(() => {
    const timeout1 = setTimeout(() => {
      router.push('/landing/start');
    }, 4000);
    return () => {
      console.log('test');
    };
  }, []);

  return (
    <div className={styles.container}>
      <div className={styles.fifth}>
        <div className={styles.imgContainer}>
          <img
            className={styles.disapear1}
            src="/landing/logoEllipse.svg"
            alt="원"
          />
          <img
            className={styles.disapear1}
            src="/landing/logoEllipse.svg"
            alt="원"
          />
        </div>
        <div className={styles.imgContainer}>
          <img
            className={styles.imgMove1}
            src="/landing/logoEllipse2.svg"
            alt="조각케익 빠진 원"
          />

          <img
            className={styles.imgMove2}
            src="/landing/logoEllipse3.svg"
            alt="조각케익 빠진 원"
          />
        </div>
        <div className={styles.imgContainer}>
          <img
            className={styles.appear2}
            src="/landing/logoEllipse4.svg"
            alt="로고 뼈대"
          />
        </div>
        <div className={styles.imgContainer}>
          <img
            className={styles.appear3}
            src="/landing/logoEllipse5.svg"
            alt="이음페이 로고"
          />
        </div>
      </div>
    </div>
  );
}
