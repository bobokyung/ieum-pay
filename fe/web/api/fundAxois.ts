import { axiosAuthApi } from '@/utils/instance';

/**
 * 진행중인 펀딩 목록 불러오는 함수
 * @returns 아이디, 개요, 제목, 날짜, 이미지, 사람 수, 목표금, 현재 모금액
 */
export const getFundListOnGoing = async () => {
  const local = axiosAuthApi();

  return await local
    .get('api/funding/list/ongoing')
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      console.log(error.message);
    });
};

/**
 * 완료된 펀딩 목록 불러오는 함수
 * @returns 아이디, 개요, 제목, 날짜, 이미지, 사람 수, 목표금
 */
export const getFundListComplete = async () => {
  const local = axiosAuthApi();

  return await local
    .get('api/funding/list/complete')
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      console.log(error.message);
    });
};

/**
 * 선택한 모금에 대한 자세한 정보를 불러오는 함수
 * @param fundId 불러올 funding id
 * @returns
 */
export const getFundDetail = async (fundId: string | string[] | undefined) => {
  const local = axiosAuthApi();

  return await local
    .get(`api/funding/${fundId}/complete`)
    .then((res) => {
      return res.data;
    })
    .catch((error) => {
      console.log(error.message);
    });
};

/**
 * 연결 하거나 끊는 요청하는 함수
 * @param currentLink 현재 연결 상태
 * @param fundId 연결할 펀딩 아이디
 * @returns boolean
 * @Auth 박수형
 */
export const setConnectState = async (currentLink: boolean, fundId: number) => {
  const local = axiosAuthApi();

  console.log(fundId);

  return await local
    .post(currentLink ? 'api/funding/unlink' : 'api/funding/linkup', {
      fundingId: fundId,
    })
    .then((response) => {
      console.log(response.data);
      return true;
    })
    .catch((error) => {
      console.log(error.message);
      return false;
    });
};

/**
 * 직접 기부하는 함수
 * @param fundId 기부할 펀딩 아이디
 * @param amount 기부할 금액
 * @param paymoneyAmount 기부금을 인출할 곳의 잔고
 * @returns 성공 실패 여부 boolean
 */
export const directDonate = async (
  fundId: number,
  amount: number,
  paymoneyAmount: number,
  key: string,
) => {
  const local = axiosAuthApi();

  return await local
    .post('api/funding/donation', {
      fundingId: fundId,
      amount: amount,
      paymoneyAmount: paymoneyAmount,
      authenticationKey: key,
    })
    .then((response) => {
      console.log(response);
      return response.data.data;
    })
    .catch((error) => {
      console.log('직접기부 실패');
      console.log(error.message);
      return 0;
    });
};

/**
 * 직접 기부 완료 페이지 함수
 * @param fundId 직접결제 기록 조회용 아이디 값
 * @returns
 */
export const getDirectDonateRes = async (fundId: number | undefined) => {
  const local = axiosAuthApi();

  return await local
    .get(`api/funding/donation/directly/result/${fundId}`)
    .then((response) => {
      console.log(response.data);
      return response.data;
    })
    .catch((error) => {
      console.log('에러뜸');
      console.log(error);
    });
};
