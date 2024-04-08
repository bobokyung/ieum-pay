import styles from '@/styles/FundPage.module.scss';
import { Progress } from '@nextui-org/react';
import dayjs from 'dayjs';
import Link from 'next/link';
interface fundingList {
  fundingId: number;
  facilityName: string;
  fundingTitle: string;
  fundingOpenDate: string;
  facilityImage: string;
  fundingPeopleCnt: number;
  goalAmount: number;
  currentAmount?: number;
}
interface CardListProps {
  fundingList: fundingList[];
  isOngoing: boolean;
}

export function CardTypeSelectTab({ fundingList, isOngoing }: CardListProps) {
  return (
    <>
      <div>
        {isOngoing
          ? `진행중인 모금 ${fundingList.length}`
          : `완료된 모금 ${fundingList.length}`}
      </div>
    </>
  );
}

export function CardList({ fundingList, isOngoing }: CardListProps) {
  return (
    <>
      <div className={styles.cardContainer}>
        {fundingList.map((unit) => (
          <Link href={`/fundraising/${unit.fundingId}`} key={unit.fundingId}>
            <div className={styles.card}>
              <div className={styles.imageBox}>
                <img
                  className={styles.cardImage}
                  src={unit.facilityImage}
                  alt={`${unit.facilityName} 이미지`}
                />
              </div>
              <div className={styles.cardTextContiner}>
                <div>{unit.facilityName}</div>
                <div>
                  {dayjs(unit.fundingOpenDate).format('YYYY년 M월 D일')}~
                </div>
                <div>
                  <div className={styles.cntProgressText}>
                    <div>{unit.fundingPeopleCnt}명 참여중</div>
                    <div>
                      {isOngoing
                        ? `${unit.currentAmount}`
                        : `${unit.goalAmount}`}
                      /{unit.goalAmount}
                    </div>
                  </div>
                  <Progress
                    size="sm"
                    aria-label="기부 모금 정도 표기"
                    classNames={{
                      indicator: `${styles.progressBar}`,
                    }}
                    value={Math.floor(
                      (unit.currentAmount != undefined
                        ? unit.currentAmount / unit.goalAmount
                        : unit.goalAmount / unit.goalAmount) * 100,
                    )}
                  />
                </div>
              </div>
            </div>
          </Link>
        ))}
      </div>
    </>
  );
}

// export default { CardList, CardTypeSelectTab };
