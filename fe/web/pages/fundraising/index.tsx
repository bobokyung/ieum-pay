import { getFundListComplete, getFundListOnGoing } from '@/api/fundAxois';
import HeaderMain from '@/stories/HeaderMain';
import TabBar from '@/stories/TabBar';
import { useState } from 'react';
import styles from '@/styles/FundPage.module.scss';
import { CardList, CardTypeSelectTab } from '@/components/funding/CardList';
import { useQueries } from '@tanstack/react-query';
import FetchError from '@/components/layouts/FetchError';

export default function Funding() {
  const [selectedTab, setSelectedTab] = useState(true);

  const results = useQueries({
    queries: [
      {
        queryKey: ['onGoing-fund-list'],
        queryFn: getFundListOnGoing,
      },
      {
        queryKey: ['complete-fund-list'],
        queryFn: getFundListComplete,
      },
    ],
  });

  function Tab() {
    if (results[0].isLoading || results[1].isLoading) {
      return (
        <>
          <div>로딩중..</div>
        </>
      );
    } else if (results[0].isError || results[1].isError) {
      return (
        <FetchError
          onClick={() => {
            results[0].refetch();
            results[1].refetch();
          }}
        ></FetchError>
      );
    } else {
      return selectedTab ? (
        <>
          <CardTypeSelectTab
            fundingList={results[0].data.data}
            isOngoing={true}
          />
          <CardList fundingList={results[0].data.data} isOngoing={true} />
        </>
      ) : (
        <>
          <CardTypeSelectTab
            fundingList={results[1].data.data}
            isOngoing={false}
          />
          <CardList fundingList={results[1].data.data} isOngoing={false} />
        </>
      );
    }
  }

  const changeToOnGoing = () => {
    setSelectedTab(true);
  };
  const changeToComplete = () => {
    setSelectedTab(false);
  };

  return (
    <>
      <HeaderMain />

      <div>
        <nav className={styles.tabContainer}>
          <ul className={styles.ul}>
            <li
              onClick={changeToOnGoing}
              className={selectedTab ? styles.selected : ''}
            >
              <div>진행 모금</div>
            </li>
            <li
              onClick={changeToComplete}
              className={selectedTab ? '' : styles.selected}
            >
              <div>완료 모금</div>
            </li>
          </ul>
        </nav>
        <div className={styles.cardContainer}>
          <Tab />
        </div>
      </div>

      <TabBar selected={'fundraising'} />
    </>
  );
}
