import { useRouter } from 'next/router';
import useUserStore from '@/stores/user-store';
import Button from '@/stories/Button';
import PageTitleLeft from '@/components/PageTitleLeft';
import styles from '@/styles/user.module.scss';
import { isMobileWeb } from '@toss/utils';

export default function Mms() {
  const router = useRouter();
  const { userInfo } = useUserStore();

  console.log(userInfo.randomKey);
  function handleClick() {
    if (isMobileWeb()) {
      const msgLink = `sms:b103ieumpay@gmail.com?body=${encodeURIComponent(userInfo.randomKey)}`;
      window.location.href = msgLink;
      router.push({
        pathname: '/user/register/input-info',
      });
    } else {
      alert('모바일환경에서만 인증 메시지를 보낼 수 있습니다.');
    }
  }

  return (
    <>
      <div className={styles.container}>
        <PageTitleLeft
          title="사용자 인증"
          description={'문자 메시지를 통해 휴대폰 본인확인을 진행합니다'}
          description2={'문자의 내용을 수정없이 보내주세요'}
        />
        <div className={styles.imageContainer}>
          <img
            src="/mms_image.png"
            alt="mss 문자 예시 이미지"
            className={styles.image}
          />
        </div>
      </div>
      <div className={styles.btnTag}>
        <Button primary size="thick" onClick={handleClick}>
          확인
        </Button>
      </div>
    </>
  );
}
