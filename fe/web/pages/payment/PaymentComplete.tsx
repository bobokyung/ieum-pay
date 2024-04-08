import PageTitleCenter from '@/components/PageTitleCenter';
import styles from './paymentComplete.module.scss';
import LineBox from '@/components/LineBox';
import Button from '@/stories/Button';
import { commaizeNumber } from '@toss/utils';
import CompleteBadge from '@/public/pageBadges/CompleteBadge';
import { useRouter } from 'next/router';
import { GetServerSideProps } from 'next';
import { useEffect, useState } from 'react';
import { getPaymentHistory } from '@/api/paymentAxios';

interface PaymentComplete {
  donationAmount: number | null;
  facilityName: string | null;
  paymentAmount: number;
  storeName: string;
}
export default function CompletePage({ historyId }: { historyId: string }) {
  const router = useRouter();

  // 여기 수정 해야함
  const [info, setInfo] = useState<PaymentComplete>({
    donationAmount: 400,
    facilityName: 'BTC 아동센터',
    paymentAmount: 5600,
    storeName: '쿠팡',
  });

  useEffect(() => {
    async function getData() {
      const res = await getPaymentHistory(historyId);
      res != undefined ? setInfo(res.data) : '';
      console.log(res);
    }
    getData();
  }, []);

  const goMain = () => {
    router.push('/');
  };
  return (
    <div className={styles.container}>
      <PageTitleCenter title={'결제 완료'} description={''} />
      <div className={styles.badge}>
        <CompleteBadge />
      </div>
      <div className={styles.LineBoxes}>
        <LineBox
          main={`구매 결제 : ${commaizeNumber(info.paymentAmount)}원`}
          sub={info.storeName}
        />
        {info.donationAmount != null ? (
          <LineBox
            main={`잔돈 기부 : ${commaizeNumber(info.donationAmount)}원`}
            sub={info.facilityName != null ? info.facilityName : ''}
          />
        ) : (
          ''
        )}
      </div>
      <div className={styles.btnContainer}>
        <Button primary size="thick" onClick={goMain}>
          확인
        </Button>
      </div>
    </div>
  );
}
export const getServerSideProps: GetServerSideProps = async ({ query }) => {
  // query 객체 사용
  return {
    props: {
      historyId: query.historyId,
    },
  };
};
