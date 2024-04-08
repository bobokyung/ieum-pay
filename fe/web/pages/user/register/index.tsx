import { requestRandomKey } from '@/api/userAxois';
import PageTitleLeft from '@/components/PageTitleLeft';
import useUserStore from '@/stores/user-store';
import Button from '@/stories/Button';
import { useRouter } from 'next/router';
import styles from '@/styles/user.module.scss';

export default function register() {
  const { userInfo, setRandomKey } = useUserStore();
  const router = useRouter();

  async function startRegister() {
    console.log(userInfo.phoneNumber);
    const res = await requestRandomKey(userInfo.phoneNumber);
    if (res.data != undefined) {
      setRandomKey(res.data);
      router.push('register/mms');
    }
  }

  return (
    <>
      <div className={styles.container}>
        <PageTitleLeft
          title="회원가입"
          description={'회원 정보가 존재하지 않습니다.'}
          description2={'회원가입을 진행하시겠습니까?'}
        />
      </div>
      <div className={styles.imgBox}>
        <img
          className={styles.img}
          src="/icon-192x192.png"
          alt="이음페이 로고"
        />
      </div>
      <div className={styles.btnTag}>
        <Button primary size="thick" onClick={startRegister}>
          시작하기
        </Button>
      </div>
    </>
  );
}
