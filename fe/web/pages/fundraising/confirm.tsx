import useDonateMoneyInfo from '@/hooks/useDirectDonationStore';
import styles from '@/styles/DirectDonationConfirmPage.module.scss';
import Header from '@/components/Header';
import Button from '@/stories/Button';
import { commaizeNumber } from '@toss/utils';
import DirectDonationIcon from '@/components/icons/DirectDonationIcon';
import { useRouter } from 'next/router';

function ConfirmPage() {
  const { donateMoneyInfo } = useDonateMoneyInfo();
  const router = useRouter();
  const onClickFunc = () => {
    router.push({
      pathname: '/password',
      query: {
        id: 0,
        pushUrl: 'fundraising-complete',
      },
    });
  };
  return (
    <>
      <Header>직접 기부하기</Header>
      <main className={styles.main}>
        <div className={styles.icon}>
          <DirectDonationIcon />
        </div>
        <div className={styles.msg}>
          <p>
            <strong className={styles.name}>{donateMoneyInfo.기관명}</strong>
            에
            <br />
            {commaizeNumber(donateMoneyInfo.송금금액)}원 기부하기
          </p>
        </div>
        <div className={styles.btn}>
          <Button primary size="thick" onClick={onClickFunc}>
            기부하기
          </Button>
        </div>
      </main>
    </>
  );
}
export default ConfirmPage;
