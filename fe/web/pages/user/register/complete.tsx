import { register, customlogin } from '@/api/userAxois';
import useUserStore from '@/stores/user-store';
import Button from '@/stories/Button';
import { useRouter } from 'next/router';
import styles from '@/styles/user.module.scss';
import {
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader,
  useDisclosure,
} from '@nextui-org/modal';

export default function RegisterEnd() {
  const { userInfo } = useUserStore();
  const router = useRouter();
  const { isOpen, onOpen, onOpenChange } = useDisclosure();

  const registerInfo = {
    phoneNumber: userInfo.phoneNumber,
    name: userInfo.userName,
    nickName: userInfo.userNickname,
    password: userInfo.userPassword,
    passwordConfirm: userInfo.userPassword,
    paymentPassword: userInfo.paymentPassword,
  };
  const startRegister = async () => {
    if (await register(registerInfo)) {
      (await customlogin(userInfo.phoneNumber, userInfo.userPassword))
        ? router.push('/user/register/notification')
        : '';
    } else {
      onOpen();
    }
  };

  return (
    <>
      <div className={styles.container}>
        <div className={styles.complete}>
          <img src="/icon-192x192.png" alt="이음페이 로고" />
          <p>회원가입 완료</p>
          <div className={styles.strongs}>
            <strong>{userInfo.userNickname}님</strong>
            <strong>환영해요!</strong>
          </div>
          <div className={styles.btnComp}>
            <Button primary onClick={startRegister}>
              시작하기
            </Button>
          </div>
        </div>
      </div>
      <Modal
        className={styles.modalComp}
        isOpen={isOpen}
        onOpenChange={onOpenChange}
      >
        <ModalContent>
          {(onClose) => (
            <>
              <ModalBody>
                <div className={styles.modalContainer}>
                  <p>네트워크 오류가 발생했습니다</p>
                  <p>처음 페이지로 돌아갑니다.</p>
                </div>
              </ModalBody>
              <ModalFooter className={styles.modalFooter}>
                <Button
                  primary
                  size="thin"
                  onClick={() => {
                    onClose();
                    router.push('/user');
                  }}
                >
                  확인
                </Button>
              </ModalFooter>
            </>
          )}
        </ModalContent>
      </Modal>
    </>
  );
}
