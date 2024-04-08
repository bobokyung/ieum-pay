import Button from '@/stories/Button';
import classes from './index.module.scss';
import { useQueries, useQuery } from '@tanstack/react-query';
import { getBalance } from '@/api/paymentAxios';
import { deleteMember, getMainData, getUserInfo } from '@/api/userAxois';
import useUserStore from '@/stores/user-store';
import { useRouter } from 'next/router';
import { eraseCookie } from '@/utils/cookie';
import Header from '@/components/Header';

export default function MemberDeletePage() {
  const { userInfo } = useUserStore();
  const router = useRouter();
  const results = useQueries({
    queries: [
      { queryKey: ['balance'], queryFn: getBalance },
      { queryKey: ['userinfo'], queryFn: getUserInfo },
    ],
  });
  function handleClick() {
    deleteMember()
      .then(() => {
        eraseCookie('access_token');
        eraseCookie('refresh-token');
        localStorage.removeItem('access_token');
        router.push('/user/delete/complete');
      })
      .catch((e) => alert(e));
  }
  if (results[1].data && results[0].data !== undefined)
    return (
      <main className={classes.main}>
        <h1>{results[1].data.data.name}님, 탈퇴하기 전에 확인해주세요</h1>
        <ul>
          <li>
            이음에서 관리했던 {results[1].data.data.name}님의 모든 개인정보를
            다시 볼 수 없어요.
          </li>
          <li>
            다양한 혜택과 이벤트 기회, 상품권 내역, 할인권이 모두 사라져요.
          </li>
          <li>
            이음 페이 머니를 다른 계좌로 옮겨주세요.100원보다 적은 금액은
            자동으로 기부돼요.
          </li>
        </ul>
        <p>{results[0].data}원이 잔고에 남아있습니다</p>
        <footer>
          {results[0].data > 100 ? (
            <div style={{ display: 'flex', gap: '1rem' }}>
              <Button onClick={() => router.push('/send-money')}>송금</Button>
              <Button onClick={() => router.push('/fundraising')}>기부</Button>
            </div>
          ) : (
            <Button primary onClick={() => handleClick()}>
              탈퇴하기
            </Button>
          )}
        </footer>
      </main>
    );
}
