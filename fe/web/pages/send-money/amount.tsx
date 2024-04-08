import { AmountButtonList } from '@/components/AmountButtonList';
import KeyPad from '../_components/KeyPad';
import useSendMoneyInfo from '@/hooks/useSendMoneyStore';
import classes from '@/styles/AmountPage.module.scss';
import { useRouter } from 'next/router';
import { commaizeNumber, formatToKRW } from '@toss/utils';
import Header from '@/components/Header';
import { useQuery } from '@tanstack/react-query';
import { getBalance } from '@/api/paymentAxios';
import { useEffect } from 'react';
const MAX_TRANSFER = 2000000;
type KeyElement = string | number | JSX.Element;
function AmountPage() {
  const router = useRouter();
  const { sendMoneyInfo, pushNumber, popNumber, setBalance } =
    useSendMoneyInfo();
  const { data } = useQuery({ queryKey: ['balance'], queryFn: getBalance });
  useEffect(() => {
    console.log('setBalance');
    if (data) setBalance(data);
  }, [data]);
  function handleClickNumber(v: KeyElement) {
    pushNumber(Number(v));
  }
  function handleClickDelete(v: KeyElement) {
    popNumber();
  }
  function handleClickConfirm(v: KeyElement) {
    router.push('/send-money/confirm');
  }
  function 송금금액() {
    return (
      <p
        className={`${classes.amount} ${sendMoneyInfo.송금금액 <= 0 ? classes.empty : ''}`}
      >
        {sendMoneyInfo.송금금액 > 0
          ? commaizeNumber(sendMoneyInfo.송금금액) + '원'
          : '얼마를 보낼까요?'}
      </p>
    );
  }
  function 송금금액_설명() {
    if (sendMoneyInfo.송금금액 === 0) {
      return <p className={classes.description}>부족금액 자동충전</p>;
    }
    if (sendMoneyInfo.송금금액 > MAX_TRANSFER) {
      return (
        <p className={classes.invalid}>
          {commaizeNumber(MAX_TRANSFER)}원 송금 가능 (1회 한도 초과)
        </p>
      );
    } else {
      return (
        <p className={classes.description}>
          {formatToKRW(sendMoneyInfo.송금금액)}
        </p>
      );
    }
  }
  if (data !== undefined)
    return (
      <div className={classes.container}>
        <Header>이음페이 송금하기</Header>
        <main className={classes.main}>
          <div className={classes.wrapper}>
            <div>
              <p className={classes.receiver}>{sendMoneyInfo.수취인}</p>
              <p className={classes.receiverAccount}>
                {sendMoneyInfo.수취은행} {sendMoneyInfo.수취계좌}
              </p>
            </div>
            <div>
              <송금금액 />
              <송금금액_설명 />
            </div>

            <div>
              <div className={classes.senderInfo}>
                <span className="bank">{sendMoneyInfo.송금은행}</span>
                <span className="deposit">{commaizeNumber(data)}원</span>
              </div>
              <AmountButtonList balance={data} />
            </div>
          </div>
        </main>
        <KeyPad
          onClickNumber={handleClickNumber}
          onClickDelete={handleClickDelete}
          onClickConfirm={handleClickConfirm}
          isValid={
            sendMoneyInfo.송금금액 > 0 && sendMoneyInfo.송금금액 <= MAX_TRANSFER
          }
        />
      </div>
    );
}
export default AmountPage;
