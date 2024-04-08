import useSendMoneyInfo from '@/hooks/useSendMoneyStore';
import classes from '@/styles/ConfirmPage.module.scss';
import SendMoneyGradientIcon from '@/components/icons/SendMoneyGradientIcon';
import Header from '@/components/Header';
import Button from '@/stories/Button';
import { ceilToUnit, commaizeNumber, formatPhoneNumber } from '@toss/utils';
import { sendPayMoney } from '@/api/sendMoneyAxios';
import { useRouter } from 'next/router';

function ConfirmPage() {
  const { sendMoneyInfo, pushNumber, popNumber } = useSendMoneyInfo();
  const router = useRouter();
  function handleClick() {
    router.push(`/password?id=0&pushUrl=send-money`);
  }
  return (
    <>
      <div className={classes.container}>
        <Header>이음페이 송금하기</Header>
        <main>
          <SendMoneyGradientIcon />
          <div className={classes.msg}>
            <p>
              <strong className={classes.name}>{sendMoneyInfo.수취인}</strong>
              님에게
              <br />
              {commaizeNumber(sendMoneyInfo.송금금액)}원을
              <br />
              보낼까요?
            </p>
          </div>
          <ul>
            <li>
              <span>출금</span>
              <span>이음페이머니</span>
            </li>
            <li>
              <span>충전금액</span>
              <span>
                {commaizeNumber(
                  Math.max(
                    ceilToUnit(
                      sendMoneyInfo.송금금액 - sendMoneyInfo.잔액,
                      10000,
                    ),
                    0,
                  ),
                )}
                원
              </span>
            </li>
            <li>
              <span>입금할 연락처</span>
              <span>{formatPhoneNumber(sendMoneyInfo.수취계좌)}</span>
            </li>
          </ul>
        </main>
        <div className={classes['btn-wrapper']}>
          <Button primary size="thick" onClick={handleClick}>
            확인
          </Button>
        </div>
      </div>
    </>
  );
}
export default ConfirmPage;
