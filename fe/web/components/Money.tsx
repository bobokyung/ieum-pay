import { commaizeNumber } from '@toss/utils';
import ChevronRightIcon from './icons/ChevronRightIcon';
import styles from './money.module.scss';
import React from 'react';
import Link from 'next/link';
interface MoneyProps {
  text: string;
  amount: string | number | undefined;
  destination: string;
}
export default function Money({ text, amount, destination }: MoneyProps) {
  return (
    <div className={styles.container}>
      <p className={styles.text}>{text}</p>
      <div className={styles.amountContainer}>
        <p className={styles.amount}>{amount && commaizeNumber(amount)}원</p>
        <Link href={destination}>
          <ChevronRightIcon />
        </Link>
      </div>
    </div>
  );
}
