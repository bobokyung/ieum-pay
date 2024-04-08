import PageTitleLeft from '@/components/PageTitleLeft';
import styles from './settings.module.scss';
import SettingsMenu from '@/components/settingsMenu';
import ChevronRightIcon from '@/components/icons/ChevronRightIcon';
import HeaderMain from '@/stories/HeaderMain';
import TabBar from '@/stories/TabBar';
import { eraseCookie } from '@/utils/cookie';
import { useRouter } from 'next/router';
import {
  Modal,
  ModalContent,
  ModalBody,
  ModalFooter,
  useDisclosure,
} from '@nextui-org/react';
import Button from '@/stories/Button';
import useUserStore from '@/stores/user-store';
import { useQuery } from '@tanstack/react-query';
import { getUserInfo, logout } from '@/api/userAxois';
import FetchError from '@/components/layouts/FetchError';
function Layout({ children }: { children: React.ReactNode }) {
  return (
    <>
      <HeaderMain />
      <div className={styles.container}>{children}</div>
    </>
  );
}
export default function Settings() {
  const router = useRouter();
  const { isOpen, onOpen, onOpenChange } = useDisclosure();
  const { userInfo } = useUserStore();

  const { data, isLoading, isError, refetch } = useQuery({
    queryKey: ['userinfo'],
    queryFn: getUserInfo,
  });
  if (isLoading) return <Layout>로딩중</Layout>;
  else if (isError)
    return (
      <Layout>
        <FetchError onClick={() => refetch()} />
      </Layout>
    );
  else if (data)
    return (
      <>
        <HeaderMain />
        <div className={styles.container}>
          <PageTitleLeft title={'설정'} description={''} />
          <div className={styles.myBox} onClick={() => router.push('/my-page')}>
            <div className={styles.myBoxText}>
              <p className={styles.name}>{data.data.name}</p>
              <p className={styles.subText}>내 정보</p>
            </div>
            <ChevronRightIcon />
          </div>
          <div className={styles.menuBox}>
            <SettingsMenu
              onClick={() => {
                router.push('/settings/change-password');
              }}
            >
              비밀번호 변경
            </SettingsMenu>
            <SettingsMenu
              onClick={() => router.push('/user/register/notification')}
            >
              알림
            </SettingsMenu>
            <SettingsMenu
              onClick={() => {
                router.push('/settings/policy');
              }}
            >
              약관 및 정책
            </SettingsMenu>
            <SettingsMenu onClick={() => onOpen()}>로그아웃</SettingsMenu>
            <SettingsMenu onClick={() => router.push('/user/delete')}>
              탈퇴하기
            </SettingsMenu>
          </div>
        </div>
        <TabBar selected={'none'} />
        <Modal
          className={styles.modalComp}
          isOpen={isOpen}
          onOpenChange={onOpenChange}
        >
          <ModalContent>
            {(onClose) => (
              <>
                <ModalBody className={styles.modalContainer}>
                  <div>
                    <p>정말 로그아웃 할까요?</p>
                  </div>
                </ModalBody>
                <ModalFooter className={styles.modalFooter}>
                  <Button size="thin" onClick={onClose}>
                    취소
                  </Button>
                  <Button
                    primary
                    size="thin"
                    onClick={() => {
                      logout();
                      eraseCookie('access_token');
                      router.push('/user');
                      onClose;
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
