import React, { useEffect, useState } from 'react';
import mainStyles from './main.module.scss';
import TabBar from '@/stories/TabBar';
import Money from '@/components/Money';
import HeaderMain from '@/stories/HeaderMain';
import Button from '@/stories/Button';
import { useRouter } from 'next/router';
import MainPageDropdown from '@/components/MainPageDropdown';
import { getMainData } from '@/api/userAxois';
import { useQueries } from '@tanstack/react-query';
import Card from '@/components/Card';
import Link from 'next/link';
import { PlusIcon } from '@/components/icons/PlusIcon';
import { deleteCard, setMainCard } from '@/api/paymentAxios';
import FetchError from '@/components/layouts/FetchError';
import {
  CircularProgress,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  useDisclosure,
} from '@nextui-org/react';

interface cardType {
  cardId: number;
  cardIssuer: string;
  cardNickname: string;
  mainCard: boolean;
  registeredCardId: number;
}
interface infoType {
  cardList: cardType[];
  linked: boolean;
  paymentAmount: number;
  totalDonation: number;
}

export default function Home() {
  const router = useRouter();
  const { isOpen, onOpen, onOpenChange } = useDisclosure();
  const [cardState, setCardState] = useState<number[]>([]);
  const [prevCardState, setPrevCardState] = useState<number[]>([]);
  const [mainCardId, setMainCardId] = useState<number>(0);
  const [focusedCardId, setFocusedCardId] = useState<number>(0);
  const [deletedCardId, setDeletedCardId] = useState<number>(0);
  const [cardId, setCardId] = useState<number>(0);
  const [info, setInfo] = useState<infoType>({
    cardList: [
      {
        cardId: -1,
        cardIssuer: '로딩',
        cardNickname: 'Loading...',
        mainCard: false,
        registeredCardId: -1,
      },
      {
        cardId: -1,
        cardIssuer: '로딩',
        cardNickname: 'Loading...',
        mainCard: false,
        registeredCardId: -1,
      },
      {
        cardId: -1,
        cardIssuer: '로딩',
        cardNickname: 'Loading...',
        mainCard: false,
        registeredCardId: -1,
      },
      {
        cardId: -1,
        cardIssuer: '로딩',
        cardNickname: 'Loading...',
        mainCard: false,
        registeredCardId: -1,
      },
    ],
    linked: false,
    paymentAmount: 0,
    totalDonation: 0,
  });

  const goFund = () => {
    router.push('/fundraising');
  };

  useEffect(() => {
    async function fetchBalance() {
      try {
        const mainData = await getMainData();
        setInfo(mainData.data);
        const firstMainCardId = mainData.data.cardList.find(
          (card: any) => card.mainCard,
        )?.registeredCardId;
        if (firstMainCardId) {
          setMainCardId(firstMainCardId);
          setFocusedCardId(firstMainCardId);
        }
      } catch (e) {
        console.log(e);
      }
    }
    fetchBalance();
  }, []);

  const setMain = (id: number) => {
    setMainCard(id);
    setMainCardId(id);
  };

  const callDeleteCard = async (id: number) => {
    if (id == mainCardId) {
      onOpen();
      console.log('대표 카드는 삭제가 불가능합니다.');
    } else {
      deleteCard(id);
      setDeletedCardId(id);
      window.location.reload();
    }
  };

  const nextCard = () => {
    setFocusedCardId(
      results[0].data.data.cardList[
        (cardState.length - cardState[0] + 1) % cardState.length
      ].registeredCardId,
    );
    // console.log((cardState.length - cardState[0] + 1) % cardState.length);
    // console.log(focusedCardId);
    const updatedCardState = [...cardState];
    updatedCardState.unshift(updatedCardState.pop()!);
    setPrevCardState([...cardState]);
    setCardState(updatedCardState);
  };

  const results = useQueries({
    queries: [
      {
        queryKey: ['get-main'],
        queryFn: getMainData,
      },
    ],
  });

  useEffect(() => {
    if (!results[0].data) return;
    const initializeCards = () => {
      const initialCardState = Array.from(
        { length: Math.min(4, results[0].data.data.cardList.length) },
        (v, k) => k,
      );
      setCardState(initialCardState);
      setPrevCardState([...initialCardState]);
    };

    initializeCards();
  }, [results[0].data]);
  if (results[0].isError)
    return (
      <>
        <HeaderMain />
        <main className={mainStyles.errorContainer}>
          <FetchError onClick={() => results[0].refetch()} />
        </main>
        <TabBar selected="payment" />
      </>
    );
  if (!results[0].data) return null;

  return (
    <>
      <HeaderMain />
      <main className={mainStyles.main}>
        <div className={mainStyles.top}>
          <MainPageDropdown
            focused={focusedCardId}
            setMain={setMain}
            callDeleteCard={callDeleteCard}
            cardLength={results[0].data?.data.cardList.length}
          ></MainPageDropdown>
        </div>
        <div className={mainStyles.cardsContainer}>
          <div className={mainStyles.cardStack} onClick={nextCard}>
            {results[0].isLoading ? (
              <>안녕</>
            ) : (
              cardState.map((card, index) => (
                <Card
                  key={index}
                  index={index}
                  card={card}
                  bank={info.cardList[index]?.cardIssuer}
                  nickname={info.cardList[index]?.cardNickname}
                  isMain={info.cardList[index]?.mainCard}
                  cardId={info.cardList[index]?.registeredCardId}
                  mainCardId={mainCardId}
                />
              ))
            )}
          </div>
          {results[0].data.data.cardList.length === 0 && (
            <Link href="/card">
              <div className={mainStyles.empty}>
                <PlusIcon />
                카드 등록하기
              </div>
            </Link>
          )}
        </div>

        <div className={mainStyles.moneyContainer}>
          <Money
            text={'이음페이머니'}
            amount={results[0].data.data.paymentAmount}
            destination={'/history'}
          />
          <hr />
          <Money
            text={'기부총액'}
            amount={results[0].data.data.totalDonation}
            destination={'/my-page'}
          />
        </div>
      </main>
      <div className={mainStyles.btnContainer}>
        <Button size="thick" onClick={goFund}>
          모금 연동하러 가기
        </Button>
      </div>
      <TabBar
        selected="payment"
        cardLength={results[0].data?.data.cardList.length}
      />
      <Modal
        className={mainStyles.modalComp}
        isOpen={isOpen}
        onOpenChange={onOpenChange}
      >
        <ModalContent>
          {(onClose) => (
            <>
              <ModalBody className={mainStyles.modalContainer}>
                <div>
                  <p>대표 카드는 삭제할 수 없습니다.</p>
                </div>
              </ModalBody>
              <ModalFooter className={mainStyles.modalFooter}>
                <Button
                  primary
                  size="thin"
                  onClick={() => {
                    onClose();
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
