import { useState } from 'react';
import classes from './Accordion.module.scss';
import ChevronDownIcon from './icons/ChevronDownIcon';
import DonationIcon from './icons/DonationIcon';
import { commaizeNumber } from '@toss/utils';
import { useRouter } from 'next/router';
import Detail from '@/pages/fundraising/[fundId]';
interface Detail {
  type: string;
  name: string;
  price: number;
}
interface History {
  historyId: number;
  historyDate: string;
  type: string;
  title: string;
  amount: number;
  detail: Detail[];
}
interface AccordionProps {
  history: History;
  children?: React.ReactNode;
}
function Accordion({ history }: AccordionProps) {
  const router = useRouter();
  const [isOpen, setIsOpen] = useState(false);
  let canOpen = history.detail.filter((e) => e.type === '기부').length > 0;
  function handleClick() {
    if (canOpen) setIsOpen((prev) => !prev);
  }

  function hasDonation() {
    let has = false;
    if (history.type === '기부') return true;
    else if (
      history.detail.filter((e) => e.type === '기부').length > 0 &&
      !isOpen
    )
      return true;
  }
  function AccordionTitle({
    history,
    isOpen,
  }: {
    history: History;
    isOpen: boolean;
  }) {
    if (isOpen && history.type === '결제') {
      return history.detail
        .filter((e) => e.type === '결제')
        .map((v) => <>{'-' + commaizeNumber(v.price)}원</>);
    } else {
      if (
        history.type === '결제' ||
        history.type === '기부' ||
        history.type === '출금'
      )
        return <>-{commaizeNumber(history.amount)}원</>;
      else return <>+{commaizeNumber(history.amount)}원</>;
    }
  }

  return (
    <div className={`${classes.accordion} ${isOpen && classes.open}`}>
      <button className={classes.button} onClick={handleClick}>
        <div>
          <p className={classes.title}>
            <span className={classes.price}>
              <AccordionTitle history={history} isOpen={isOpen} />
            </span>
            {hasDonation() && (
              <span className={classes.donation}>
                <DonationIcon />
              </span>
            )}
          </p>
          <p className={classes.detail}>
            {history.type} | {history.title}
          </p>
        </div>
        {canOpen && (
          <div className={`${classes.chevron} ${isOpen ? classes.open : ''}`}>
            <ChevronDownIcon />
          </div>
        )}
      </button>
      {isOpen && (
        <section>
          {history.detail
            .filter((e) => e.type === '기부')
            .map((e) => (
              <>
                {history.type !== '기부' && (
                  <div className={classes['donation-wrapper']}>
                    -{commaizeNumber(e.price)}원
                    <span className={classes.donation}>
                      <DonationIcon />
                    </span>
                  </div>
                )}
                <div className={classes['donation-detail']}>
                  <span className={classes['donation-detail-text']}>
                    기부 | {e.name}
                  </span>
                  <button
                    onClick={() => router.push('/history/' + history.historyId)}
                    className={classes.btn}
                  >
                    기부 영수증
                  </button>
                </div>
              </>
            ))}
          {history.detail
            .filter((e) => e.type === '충전')
            .map((e) => (
              <>
                <hr />
                <div key={history.historyId + e.type}>
                  +{commaizeNumber(e.price)}원 이음페이머니 충전
                </div>
              </>
            ))}
        </section>
      )}
    </div>
  );
}
export default Accordion;
