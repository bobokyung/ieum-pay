import TabBar from '@/stories/TabBar';
import {
  Tabs,
  Tab,
  Input,
  Select,
  SelectItem,
  Button,
  Modal,
  ModalContent,
  ModalHeader,
  ModalBody,
  ModalFooter,
  useDisclosure,
} from '@nextui-org/react';
import { useRouter } from 'next/router';
import { useState } from 'react';
import classes from './index.module.scss';
import HeaderMain from '@/stories/HeaderMain';
import { getMemberByPhoneNumber } from '@/api/sendMoneyAxios';
import useSendMoneyInfo from '@/hooks/useSendMoneyStore';
import { useQueries } from '@tanstack/react-query';
import { getMainData, getUserInfo } from '@/api/userAxois';
import ChevronRightIcon from '@/components/icons/ChevronRightIcon';
import Link from 'next/link';
import useUserStore from '@/stores/user-store';
import FetchError from '@/components/layouts/FetchError';

interface Member {
  memberId: number;
  name: string;
  phoneNumber: string;
}
function Layout({ children }: { children?: React.ReactNode }) {
  return (
    <>
      <HeaderMain />
      <main className={classes.main}>{children}</main>
      <TabBar selected="sendMoney" />
    </>
  );
}

function WherePage() {
  const router = useRouter();
  const [query, setQuery] = useState('');
  const { sendMoneyInfo, setReceiverInfo, setAmount } = useSendMoneyInfo();
  const [자가송금모달_열렸는가, 자가송금모달_열렸는가_설정] = useState(false);
  const results = useQueries({
    queries: [
      {
        queryKey: [query],
        queryFn: () => getMemberByPhoneNumber(query),
        enabled: query.length >= 11,
      },
      {
        queryKey: ['main-data'],
        queryFn: getMainData,
      },
      {
        queryKey: ['user-info'],
        queryFn: getUserInfo,
        enabled: false,
      },
    ],
  });
  function handleClick() {
    if (localStorage.getItem('phone_number') === results[0].data.phoneNumber) {
      자가송금모달_열렸는가_설정(true);
      return;
    }
    console.log(results[2].data);
    setReceiverInfo(
      results[0].data.name,
      results[0].data.phoneNumber,
      '이음페이',
    );
    setAmount(0);
    router.push('/send-money/amount');
  }
  function SearchResult() {
    if (results[0].isError)
      return <p>해당 번호는 이음페이에 가입한 적이 없네요 ㅠㅠ</p>;
    else if (results[0].data) {
      return (
        <button className={classes.send} onClick={handleClick}>
          {results[0].data?.name}님에게 송금하기
          <ChevronRightIcon />
        </button>
      );
    } else null;
  }

  // if (results[1].isLoading) return null;
  // if (results[1].data && results[1].data.data.cardList.length === 0)
  //   return (
  //     <>
  //       <HeaderMain />
  //       <main className={classes.main}>
  //         <h1>송금하려면 카드를 먼저 등록 해주세요</h1>
  //       </main>
  //       <TabBar selected="sendMoney" />
  //     </>
  //   );
  const [isOpen, setIsOpen] = useState(false);
  if (results[1].isLoading) return <Layout></Layout>;
  else if (results[1].isError || results[2].isError) {
    return (
      <Layout>
        <div className={classes.errorContainer}>
          <FetchError onClick={() => results[1].refetch()} />
        </div>
      </Layout>
    );
  } else if (results[1].data.data.cardList.length === 0) {
    return (
      <Layout>
        <h1>어디로 돈을 보낼까요?</h1>
        <div onClick={() => setIsOpen(true)}>
          <Input
            isDisabled
            variant="underlined"
            label="휴대폰 번호"
            type="tel"
            inputMode="decimal"
            pattern="[0-9]*"
            className="w-100"
            autoComplete="tel"
          />
        </div>
        {results[1].data.data.cardList.length === 0 && (
          <Modal
            isOpen={isOpen}
            placement="center"
            isDismissable={false}
            hideCloseButton={true}
          >
            <ModalContent>
              {(onClose) => (
                <>
                  <ModalHeader className="flex flex-col gap-1"></ModalHeader>
                  <ModalBody>
                    <p>송금하려면 먼저 카드를 등록하세요</p>
                  </ModalBody>
                  <ModalFooter className="w-100 flex-col">
                    <Button variant="light" onClick={() => setIsOpen(false)}>
                      나중에 다시하기
                    </Button>
                    <Button color="primary" as={Link} href="/card">
                      등록하기
                    </Button>
                  </ModalFooter>
                </>
              )}
            </ModalContent>
          </Modal>
        )}
      </Layout>
    );
  }

  return (
    <Layout>
      <h1>어디로 돈을 보낼까요?</h1>

      <Input
        variant="underlined"
        label="휴대폰 번호"
        type="tel"
        inputMode="decimal"
        pattern="[0-9]*"
        className="w-100"
        onValueChange={setQuery}
        autoComplete="tel"
      />
      <SearchResult />
      <Modal isOpen={자가송금모달_열렸는가} placement="center" hideCloseButton>
        <ModalContent>
          {(onClose) => (
            <>
              <ModalHeader className="flex flex-col gap-1"></ModalHeader>
              <ModalBody>
                <p>자신에게는 송금할 수 없어요</p>
              </ModalBody>
              <ModalFooter className="w-100 flex-col">
                <Button
                  color="primary"
                  onClick={() => 자가송금모달_열렸는가_설정(false)}
                >
                  확인
                </Button>
              </ModalFooter>
            </>
          )}
        </ModalContent>
      </Modal>
    </Layout>
  );
}
export default WherePage;
