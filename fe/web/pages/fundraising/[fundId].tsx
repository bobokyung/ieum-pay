import { getFundDetail, setConnectState } from '@/api/fundAxois';
import { useRouter } from 'next/router';
import { useEffect, useState } from 'react';
import styles from '@/styles/FundDetailPage.module.scss';
import { commaizeNumber } from '@toss/utils';
import {
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader,
  useDisclosure,
} from '@nextui-org/modal';
import HalfButton from '@/components/HalfButton';
import HeaderHome from '@/components/HeaderHome';
import useDonateMoneyInfo from '@/hooks/useDirectDonationStore';
import useUserStore from '@/stores/user-store';
import Button from '@/stories/Button';
import { Progress } from '@nextui-org/react';
import { josa } from '@toss/hangul';
import { GetServerSideProps } from 'next/types';
import { getBalance } from '@/api/paymentAxios';

interface peopleType {
  nickname: string;
  memberId: number;
  gradeCode: string;
  fundingTotalAmount: number;
}

interface productType {
  productName: string;
  sponsorName: string;
  fundingProductQuantity: number;
  fundingProductPrice: number;
  productImage: string;
  price: number;
}

interface dataType {
  fundingId: number;
  facilityName: string;
  facilityAddress: string;
  facilityPhoneNumber: string;
  facilityRepresentativeName: string;
  facilityRepresentativePhoneNumber: string;
  facilityCapacity: number;
  facilityImage: string;
  fundingOpenDate: string;
  fundingPeopleCnt: number;
  fundingTitle: string;
  goalAmount: number;
  currentAmount: number;
  currentLink: boolean;
  people: peopleType[];
  // 일단 임의 지정
  content: string;
  products: productType[];
  fundingFinishDate: string | null;
}

export default function Detail({ fundId }: { fundId: string }) {
  const router = useRouter();
  const donateInfo = useDonateMoneyInfo();
  const { userInfo, setBalance } = useUserStore();

  const [detailData, setData] = useState<dataType>({
    fundingId: 1,
    facilityName: '은혜노인복지센터',
    facilityAddress: '부산광역시 해운대구 우동 123-45',
    facilityPhoneNumber: '051-1234-5678',
    facilityRepresentativeName: '김은혜',
    facilityRepresentativePhoneNumber: '010-9876-5432',
    facilityCapacity: 100,
    facilityImage:
      'https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20200124_169%2F1579861444105ksyLS_JPEG%2FHR80O6nA89Q9ZWBHq4KZQL_-.jpeg.jpg',
    fundingOpenDate: '2024-04-01 00:00:00',
    fundingPeopleCnt: 2,
    fundingFinishDate: null,
    fundingTitle: '은혜노인복지센터 식사 지원',
    goalAmount: 817600,
    currentAmount: 400800,
    currentLink: false,
    people: [
      {
        nickname: 'nick1',
        memberId: 1,
        gradeCode: 'GR005',
        fundingTotalAmount: 201600,
      },
      {
        nickname: 'nick2',
        memberId: 2,
        gradeCode: 'GR005',
        fundingTotalAmount: 199200,
      },
    ],
    content:
      '안녕하세요, 은혜노인복지센터에서 따뜻한 겨울나기 프로젝트를 진행 중입니다. 겨울철 어르신들께 따뜻한 식사를 제공하기 위한 우리의 노력은 여러분의 손길을 기다리고 있습니다.  여러분의 소중한 기부가 어르신들에게 라면, 햇반, 스팸, 참치, 김자반 등 따뜻한 식사로 전달될 예정입니다. 어려운 시기, 한 분 한 분의 따스한 마음이 모여 큰 힘이 됩니다.\n\n주말에 어르신들께 제공할 컵라면과 짜파게티 준비에도 여러분의 기부가 큰 도움이 될 것입니다. 어르신들이 스스로 끓여 드실 수 있는 따뜻한 식사는 겨울철 추위를 이겨내는 데 중요한 역할을 합니다.\n\n우리 센터는 어르신들이 스스로 식사를 준비하며 일상생활에서의 작은 자립을 경험할 수 있도록 돕고자 합니다. 김치볶음밥과 같은 따뜻한 식사를 통해 어르신들께 겨울철 영양을 제공하고자 합니다.\n\n여러분의 지속적인 관심과 기부를 통해 은혜노인복지센터의 어르신들이 더욱 건강하고 행복한 겨울을 보낼 수 있습니다. 이번 기회에 작은 기부로도 큰 변화를 만들어낼 수 있다는 것을 보여주세요. 여러분의 따뜻한 마음이 어르신들에게 큰 힘이 됩니다.\n\n감사합니다. 함께해 주실 것을 기대하며, 좋은 하루 되세요!',
    products: [
      {
        fundingProductQuantity: 4,
        fundingProductPrice: 76560,
        productName: '농심 올리브짜파게티 140g X 20입',
        productImage:
          'https://sitem.ssgcdn.com/68/58/64/item/0000008645868_i1_140.jpg',
        price: 19140,
        sponsorName: 'SSG.COM',
      },
      {
        fundingProductQuantity: 3,
        fundingProductPrice: 70400,
        productName: '농심 신라면 120g X 30입',
        productImage:
          'https://sitem.ssgcdn.com/79/44/63/item/0000008634479_i1_140.jpg',
        price: 23480,
        sponsorName: 'SSG.COM',
      },
      {
        fundingProductQuantity: 20,
        fundingProductPrice: 85800,
        productName: '새우와 멸치를 섞어 바삭바삭 고소한 파래김자반 (65g)',
        productImage:
          'https://sitem.ssgcdn.com/37/63/00/item/2097001006337_i1_140.jpg',
        price: 4290,
        sponsorName: '이마트',
      },
      {
        fundingProductQuantity: 10,
        fundingProductPrice: 184800,
        productName: '동원 참치살코기 135g*8',
        productImage:
          'https://sitem.ssgcdn.com/86/84/32/item/1000005328486_i1_140.jpg',
        price: 18480,
        sponsorName: '이마트',
      },
      {
        fundingProductQuantity: 10,
        fundingProductPrice: 209800,
        productName: '스팸 마일드 라벨프리 200g*6',
        productImage:
          'https://sitem.ssgcdn.com/98/88/00/item/1000519008898_i1_140.jpg',
        price: 20980,
        sponsorName: '이마트',
      },
      {
        fundingProductQuantity: 10,
        fundingProductPrice: 50400,
        productName: '농심 육개장 사발면 (86gx6입)',
        productImage:
          'https://sitem.ssgcdn.com/10/36/33/item/0000008333610_i1_140.jpg',
        price: 5040,
        sponsorName: 'SSG.COM',
      },
      {
        fundingProductQuantity: 10,
        fundingProductPrice: 139800,
        productName: 'CJ 햇반(210g*12입)',
        productImage:
          'https://sitem.ssgcdn.com/69/86/21/item/0000008218669_i1_140.jpg',
        price: 13980,
        sponsorName: 'CJ제일제당',
      },
    ],
  });

  const productList = () => {
    return (
      <div className={styles.fundingState}>
        <div className={styles.title}>제품 목록</div>
        <hr className={styles.hr} />
        <div className={styles.info}>
          <div className={styles.cardContainer}>
            {detailData.products.map((unit) => (
              <>
                <div className={styles.card}>
                  <div className={styles.imageBox}>
                    <img
                      className={styles.image}
                      src={unit.productImage}
                      alt="물품 이미지"
                    />
                  </div>
                  <div className={styles.productInfo}>
                    <div>{unit.sponsorName}</div>
                    <div className={styles.sponsorName}>{unit.productName}</div>
                    <div className={styles.unit}>
                      개당 {commaizeNumber(unit.price)}
                    </div>
                    <div className={styles.products}>
                      <div>{unit.fundingProductQuantity}개</div>
                      <div>{commaizeNumber(unit.fundingProductPrice)}원</div>
                    </div>
                  </div>
                </div>
                <hr className={styles.cardHr} />
              </>
            ))}
          </div>
        </div>
      </div>
    );
  };
  const donatorList = () => {
    return (
      <div className={styles.fundingState}>
        <div className={styles.title}>기부자 목록</div>
        <div>
          {detailData.people.map((unit) => (
            <div className={styles.donatorContainer}>
              <div className={styles.donatorInfo}>
                <img
                  className={styles.donatorBadge}
                  src={`/levelBadges/${unit.gradeCode}.svg`}
                  alt="도네이터 등급"
                />
                <div className={styles.donatorName}>{unit.nickname}</div>
              </div>
              <div className={styles.donateAmount}>
                {commaizeNumber(unit.fundingTotalAmount)}원
              </div>
            </div>
          ))}
        </div>
      </div>
    );
  };

  const setdirectDonateInfo = () => {
    donateInfo.setDonateMoneyInfo({
      기관아이디: detailData.fundingId,
      기관명: detailData.facilityName,
      남은금액: detailData.goalAmount - detailData.currentAmount,
      송금금액: 0,
      송금은행: '이음페이',
      잔액: userInfo.balance,
    });
    router.push('/fundraising/direct-donation');
  };

  const mamgeLinking = async () => {
    if (await setConnectState(detailData.currentLink, detailData.fundingId)) {
      setData({
        fundingId: detailData.fundingId,
        facilityName: detailData.facilityName,
        facilityAddress: detailData.facilityAddress,
        facilityPhoneNumber: detailData.facilityPhoneNumber,
        facilityRepresentativeName: detailData.facilityRepresentativeName,
        facilityRepresentativePhoneNumber:
          detailData.facilityRepresentativePhoneNumber,
        facilityCapacity: detailData.facilityCapacity,
        facilityImage: detailData.facilityImage,
        fundingOpenDate: detailData.fundingOpenDate,
        fundingPeopleCnt: detailData.fundingPeopleCnt,
        fundingTitle: detailData.fundingTitle,
        goalAmount: detailData.goalAmount,
        currentAmount: detailData.currentAmount,
        // 여기 바꾸는거
        currentLink: !detailData.currentLink,
        people: detailData.people,
        content: detailData.content,
        products: detailData.products,
        fundingFinishDate: detailData.fundingFinishDate,
      });
      onOpen();
    }
  };

  const { isOpen, onOpen, onOpenChange } = useDisclosure();

  const btnProps = {
    text: detailData.currentLink ? '기부 그만하기' : '기부 연동하기',
    text2: '직접 후원하기',
    btnStyle: 'recThinFill',
    btnStyle2: 'recThinFill',
    btnFunction: mamgeLinking,
    btnFunction2: setdirectDonateInfo,
  };

  useEffect(() => {
    async function getData() {
      try {
        const fundDetailData = await getFundDetail(fundId);
        const userBalance = await getBalance();
        fundDetailData != undefined ? setData(fundDetailData.data) : '';
        setBalance(userBalance);
      } catch (e) {
        console.log(e);
      }
    }
    getData();
  }, []);

  return (
    <>
      <HeaderHome>모금 상세</HeaderHome>
      <div className={styles.detailContainer}>
        <div className={styles.imageTag}>
          <img
            src={detailData.facilityImage}
            className={styles.facilityImage}
            alt="기관 이미지"
          />
        </div>
        <div className={styles.fundingState}>
          <div className={styles.title}>{detailData.facilityName}</div>
          <div className={styles.info}>
            <div>
              마감까지{' '}
              {commaizeNumber(detailData.goalAmount - detailData.currentAmount)}
            </div>
            <div>
              {(
                (detailData.currentAmount / detailData.goalAmount) *
                100
              ).toFixed(2)}
              %
            </div>
          </div>
          <Progress
            size="sm"
            aria-label="기부 모금 정도 표기"
            classNames={{
              indicator: `${styles.progressBar}`,
            }}
            value={Math.floor(
              (detailData.currentAmount / detailData.goalAmount) * 100,
            )}
          />
        </div>
        {productList()}
        {donatorList()}
        <div className={styles.content}>
          <p>기관으로부터의 편지</p>
          {detailData.content}
        </div>
      </div>
      {detailData.fundingFinishDate == null ? (
        <div className={styles.footer}>{HalfButton(btnProps)}</div>
      ) : (
        <div></div>
      )}

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
                  {detailData.currentLink ? (
                    <>
                      <img
                        className={styles.modalImage}
                        src="/icon-256x256.png"
                        alt="이어졌다는 뜻의 이미지"
                      />
                      <div className={styles.modalMessage}>
                        {josa(detailData.facilityName, '와/과')} 이어졌어요!
                      </div>
                    </>
                  ) : (
                    <>
                      <img
                        className={styles.modalImage}
                        src="/disconnect-icon.svg"
                        alt="끊어졌다는 뜻의 로고 이미지"
                      />
                      <div className={styles.modalMessage}>
                        {josa(detailData.facilityName, '와/과')}의 연결을
                        끊었어요
                      </div>
                    </>
                  )}
                </div>
              </ModalBody>
              <ModalFooter className={styles.modalFooter}>
                <Button primary size="thin" onClick={onClose}>
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

export const getServerSideProps: GetServerSideProps = async (context) => {
  const { fundId } = context.query;

  return {
    props: {
      fundId: fundId,
    },
  };
};
