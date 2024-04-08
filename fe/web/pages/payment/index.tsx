import PageTitleCenter from '@/components/PageTitleCenter';
import styles from './payment.module.scss';
import LongLogo from '@/components/logo/LongLogo';
import { commaizeNumber } from '@toss/utils';
import Button from '@/stories/Button';
import { getPaymentInfo } from '@/api/paymentAxios';
import { useEffect, useState } from 'react';
import { useRouter } from 'next/router';
import { GetServerSideProps } from 'next';
import usePaymentInfo from '@/hooks/usePayStore';

function Payment({ storeId, Qprice }: { storeId: string; Qprice: string }) {
  const [info, setInfo] = useState({
    storeId: 1,
    cardNickname: '삼성카드',
    price: 0,
    storeName: '삼성몰',
    paymoneyAmount: 0,
    chargeAmount: 0,
    donationMoney: 0,
    authenticationKey: '',
  });
  const { paymentInfo, setPaymentInfo } = usePaymentInfo();

  console.log('payment :', storeId, Qprice);

  useEffect(() => {
    async function getdata() {
      try {
        const res = await getPaymentInfo(storeId, Qprice);
        setInfo(res.data);
      } catch (e) {
        console.log(e);
      }
    }
    getdata();
  }, []);

  const router = useRouter();

  const handleClick = () => {
    setPaymentInfo(info);
    router.push({
      pathname: '/password',
      query: {
        id: 0,
        pushUrl: 'payment',
      },
    });
  };

  return (
    <>
      <div className={styles.container}>
        <PageTitleCenter title={'결제하기'} description={''} />
        <div className={styles.storeContainer}>
          <p className={styles.store}>{info.storeName}</p>
          <p className={styles.price}>{commaizeNumber(info.price)}원</p>
        </div>
        <div className={styles.box}>
          <div className={styles.upperBox}>
            <div className={styles.ieumPayMoney}>
              <LongLogo />
              <p>머니</p>
            </div>
            <div>
              <p className={styles.cardNickname}>{info.cardNickname}</p>
            </div>
            <div className={styles.balanceContainer}>
              <p className={styles.balanceText}>현재잔액</p>
              <p className={styles.balance}>
                {commaizeNumber(info.paymoneyAmount)}원
              </p>
            </div>
            {info.chargeAmount ? (
              <div className={styles.balanceContainer}>
                <p className={styles.balanceText}>자동충전</p>
                <p className={styles.balance}>
                  {commaizeNumber(info.chargeAmount)}원
                </p>
              </div>
            ) : null}
          </div>
          <hr className={styles.divide} />
          <div className={styles.downerBox}>
            <p className={styles.donationText}>잔돈기부</p>
            <p className={styles.donationMoney}>{info.donationMoney}원</p>
          </div>
        </div>
      </div>
      <div className={styles.bottom}>
        <div className={styles.validationContainer}>
          <p className={styles.validaition}>
            결제조건 확인 및 개인정보 제공에 동의합니다
          </p>
          <hr className={styles.validationLine} />
        </div>
        <Button
          primary
          size="thick"
          onClick={handleClick}
        >{`${commaizeNumber(info.price)}원 결제하기`}</Button>
      </div>
    </>
  );
}
export default Payment;
export const getServerSideProps: GetServerSideProps = async ({ query }) => {
  // query 객체 사용
  return {
    props: { storeId: query.storeId, Qprice: query.Qprice },
  };
};
