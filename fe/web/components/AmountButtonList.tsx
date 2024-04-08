import useSendMoneyInfo from '@/hooks/useSendMoneyStore';
import AmountButton from './AmountButton';
import classes from './AmountButtonList.module.css';
import useDonateMoneyInfo from '@/hooks/useDirectDonationStore';
function AmountButtonList({ balance }: { balance?: number }) {
  const { sendMoneyInfo, addAmount, setFullAmount, setAmount } =
    useSendMoneyInfo();
  const buttons = [
    {
      text: '+1천',
      onClick: () => {
        addAmount(1000);
      },
    },
    {
      text: '+5천',
      onClick: () => {
        addAmount(5000);
      },
    },
    {
      text: '+1만',
      onClick: () => {
        addAmount(10000);
      },
    },
    {
      text: '+5만',
      onClick: () => {
        addAmount(50000);
      },
    },
    {
      text: '+10만',
      onClick: () => {
        addAmount(100000);
      },
    },
    {
      text: '전액',
      onClick: () => {
        balance ? setAmount(balance) : setFullAmount();
      },
    },
  ];
  return (
    <ul className={classes.ul}>
      {buttons.map((button, i) => (
        <li key={i}>
          <AmountButton text={button.text} onClick={button.onClick} />
        </li>
      ))}
    </ul>
  );
}

function AmountDonateButtonList() {
  const { addAmount, setFullAmount, setLastAmount } = useDonateMoneyInfo();
  const buttons = [
    {
      text: '+1천',
      onClick: () => {
        addAmount(1000);
      },
    },
    {
      text: '+5천',
      onClick: () => {
        addAmount(5000);
      },
    },
    {
      text: '+1만',
      onClick: () => {
        addAmount(10000);
      },
    },
    {
      text: '+5만',
      onClick: () => {
        addAmount(50000);
      },
    },
    {
      text: '+10만',
      onClick: () => {
        addAmount(100000);
      },
    },
    {
      text: '전액',
      onClick: () => {
        setFullAmount();
      },
    },
    {
      text: '채움',
      onClick: () => {
        setLastAmount();
      },
    },
  ];
  return (
    <ul className={classes.ul}>
      {buttons.map((button, i) => (
        <li key={i}>
          <AmountButton text={button.text} onClick={button.onClick} />
        </li>
      ))}
    </ul>
  );
}
export { AmountButtonList, AmountDonateButtonList };
