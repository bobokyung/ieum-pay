import TabBar from '@/stories/TabBar';
import HeaderMain from '@/stories/HeaderMain';
import { useEffect, useState } from 'react';
import { getUserInfo } from '@/api/userAxois';
import { commaizeNumber } from '@toss/utils';
import { useRouter } from 'next/router';
import Button from '@/stories/Button';
import styels from '@/styles/myPage.module.scss';
import { CircularProgressbarWithChildren } from 'react-circular-progressbar';
import 'react-circular-progressbar/dist/styles.css';
import GradientSVG from '@/components/gradientSVG';
import FetchError from '@/components/layouts/FetchError';

interface userInfoType {
  autoFundingId: number;
  facilityImage: string;
  facilityName: string;
  fundingTotalAmount: number;
  gradeCode: string;
  gradeName: string;
  name: string;
  nickname: string;
  totalDonationAmount: number;
  totalDonationCnt: number;
}
import { useQuery } from '@tanstack/react-query';

export default function MyPage() {
  const router = useRouter();
  const idCSS = 'hello';
  const { data, error, isError, isLoading, refetch } = useQuery({
    queryKey: ['my-page'],
    queryFn: getUserInfo,
  });

  const tearProgress = (value: number, tear: string) => {
    if (tear == 'GR001') return (value / 100) * 100;
    else if (tear == 'GR002') return (value / 5000) * 100;
    else if (tear == 'GR003') return (value / 30000) * 100;
    else if (tear == 'GR004') return (value / 100000) * 100;
    else if (tear == 'GR005') return (value / 1000000) * 100;
    else if (tear == 'GR006') return (value / 10000000) * 100;
    else if (tear == 'GR007') return 100;
  };

  const onclickFunc = () => {
    router.push({
      pathname: '/my-page/tear-list',
      query: {
        gradeCode: data.data.gradeCode,
      },
    });
  };

  const donateState = () => {
    return (
      <>
        <div className={styels.donateHistory}>
          <p>
            지금까지 <span>{data.data.totalDonationCnt}개</span>기관에
          </p>
          <p>
            총 <span>{commaizeNumber(data.data.totalDonationAmount)}원</span>을
            나눴어요
          </p>
          <div className={styels.progressBar} onClick={onclickFunc}>
            <GradientSVG />
            <CircularProgressbarWithChildren
              value={Math.floor(
                Number(
                  tearProgress(
                    data.data.totalDonationAmount,
                    data.data.gradeCode,
                  ),
                ),
              )}
              styles={{
                root: {},
                path: {
                  stroke: `url(#${idCSS})`,
                  strokeLinecap: 'butt',
                  transition: 'stroke-dashoffset 0.5s ease 0s',
                  transformOrigin: 'center center',
                },
              }}
            >
              <img
                src={`levelIcons/${data.data.gradeCode}.svg`}
                className={styels.iconImage}
              />
            </CircularProgressbarWithChildren>
          </div>
          <Button
            primary
            size="small"
            onClick={() => {
              router.push('/my-page/donation-history');
            }}
          >
            나의 기부 내역 보기
          </Button>
        </div>
        <hr />
        <div className={styels.connectedState}>
          {data.data.autoFundingId == 0 ? (
            <>
              <p>연동되어있는 모금이 없어요</p>
              <Button size="thin" onClick={() => router.push('/fundraising')}>
                모금하러가기
              </Button>
            </>
          ) : (
            <div className={styels.curConnected}>
              <p className={styels.head}>기부중인 모금</p>
              <img src={data.data.facilityImage} alt="기부중인 모금 사진" />
              <p>{data.data.facilityName}에 지금까지</p>
              <p>
                <span>{commaizeNumber(data.data.fundingTotalAmount)}원</span>
                나눴어요
              </p>
              <Button
                primary
                size="small"
                onClick={() =>
                  router.push(`/fundraising/${data.data.autoFundingId}`)
                }
              >
                모금 상세 보기
              </Button>
            </div>
          )}
        </div>
      </>
    );
  };

  return (
    <>
      <HeaderMain />

      <div className={styels.container}>
        {isLoading ? (
          <div>로딩중</div>
        ) : isError ? (
          <div className={styels.errorContainer}>
            <FetchError onClick={() => refetch()}></FetchError>
          </div>
        ) : (
          <>
            <div className={styels.nameContainer}>
              <img
                src={`levelBadges/${data.data.gradeCode}.svg`}
                className={styels.badgeImage}
                alt={`${data.data.gradeName} 등급 뱃지`}
              />
              <span>{data.data.name}</span>
              <p>{data.data.nickname}</p>
            </div>
            <hr />
            <div>{donateState()}</div>
          </>
        )}
      </div>
      <TabBar selected={'myPage'} />
    </>
  );
}
