import useSendMoneyInfo from '@/hooks/useSendMoneyStore';
import classes from '@/styles/ConfirmPage.module.scss';
import SendMoneyGradientIcon from '@/components/icons/SendMoneyGradientIcon';
import Header from '@/components/Header';
import Button from '@/stories/Button';
import { commaizeNumber } from '@toss/utils';
import { sendPayMoney } from '@/api/sendMoneyAxios';
import { useRouter } from 'next/router';
import Image from 'next/image';

function SuccessPage() {
  const { sendMoneyInfo } = useSendMoneyInfo();
  const router = useRouter();
  function handleClick() {
    router.push('/main');
  }
  return (
    <>
      <div className={classes.container}>
        <Header>이음페이 송금하기</Header>
        <main>
          <img src="/SuccessIcon.svg" alt="보라색 체크표시 아이콘"></img>
          <div className={classes.msg}>
            <p>
              <strong className={classes.name}>{sendMoneyInfo.수취인}</strong>
              님에게
              <br />
              {commaizeNumber(sendMoneyInfo.송금금액)}원을
              <br />
              보냈어요
            </p>
          </div>
        </main>
        <div className={classes['btn-wrapper']}>
          <Button primary size="thick" onClick={handleClick}>
            완료
          </Button>
        </div>
      </div>
    </>
  );
}
export default SuccessPage;
