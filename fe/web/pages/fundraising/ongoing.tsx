import HeaderMain from '@/stories/HeaderMain';
import TabBar from '@/stories/TabBar';
import styles from '@/styles/FundPage.module.scss';
import Link from 'next/link';
import { useQuery } from '@tanstack/react-query';
import { CardList } from '@/components/funding/CardList';
import { axiosAuthApi } from '@/utils/instance';

export default function Funding() {
  async function getFundListOnGoing() {
    return axiosAuthApi().get('api/funding/list/ongoing');
  }
  // This query was not prefetched on the server and will not start
  // fetching until on the client, both patterns are fine to mix
  const { data, isError, isLoading } = useQuery({
    queryKey: ['fund-ongoing'],
    queryFn: getFundListOnGoing,
  });

  return (
    <>
      <HeaderMain />

      <div>
        <nav className={styles.tabContainer}>
          <ul className={styles.ul}>
            <Link href="/fundraising/ongoing">
              <span>진행 중</span>
            </Link>
            <Link href="/fundraising/outgoing">
              <span>완료</span>
            </Link>
          </ul>
        </nav>
        {data && (
          <div className={styles.cardContainer}>
            <CardList fundingList={data.data} isOngoing={true}></CardList>
          </div>
        )}
      </div>

      <TabBar selected={'fundraising'} />
    </>
  );
}
