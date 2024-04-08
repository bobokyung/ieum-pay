import { axiosApi, axiosAuthApi } from '@/utils/instance';
import { setCookie } from '@/utils/cookie';

/**
 * login 후 토큰 저장후 true 리턴
 */
export const customlogin = async (phoneNumber: string, password: string) => {
  const local = axiosApi();

  return await local
    .post('api/member/login', {
      phoneNumber: phoneNumber,
      password: password,
    })
    .then((response) => {
      console.log(response.data.data);
      // cookies().set('access_token', response.data.data);
      setCookie('access_token', response.data.data, 1);
      localStorage['access_token'] = response.data.data;
      localStorage['phone_number'] = phoneNumber;
      return true;
    })
    .catch((error) => {
      console.log(error.message);
      return false;
    });
};

/**
 * 랜덤 키 요청하는 함수
 */
export const requestRandomKey = async (phoneNumber: string) => {
  const local = axiosApi();
  return await local
    .get(`api/auth?phone-number=${phoneNumber}`)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      console.log(error.message);
    });
};

/**
 * 유저 정보를 불러오는 함수
 * @returns reponse data 값 or error
 */
export const getUserInfo = async () => {
  const local = axiosAuthApi();

  return await local
    .get('api/member/summary')
    .then((response) => {
      console.log(response);
      return response.data;
    })
    .catch((error) => {
      console.log(error.message);
    });
};

/**
 * 해당 번호로 가입한 적 있는지 판단
 * @param nunmber 전화번호
 * @returns response data
 */
export const IsRegister = async (nunmber: string) => {
  const local = axiosApi();

  return await local
    .get(`api/member/exist?phone-number=${nunmber}`)
    .then((response) => {
      console.log(response);
      return response.data;
    })
    .catch((error) => {
      console.log('에러뜸');
      console.log(error.message);
    });
};

interface registerType {
  phoneNumber: string;
  name: string;
  nickName: string;
  password: string;
  passwordConfirm: string;
  paymentPassword: string;
}
/**
 * 회원가입 함수
 * @param param0 회원가입에 필요한 정보
 * @returns boolean value
 */
export const register = async ({
  phoneNumber,
  name,
  nickName,
  password,
  passwordConfirm,
  paymentPassword,
}: registerType) => {
  const local = axiosApi();

  return await local
    .post('api/member', {
      phoneNumber: phoneNumber,
      name: name,
      nickname: nickName,
      password: password,
      passwordConfirm: passwordConfirm,
      paymentPassword: paymentPassword,
    })
    .then((response) => {
      console.log(response);
      return true;
    })
    .catch((error) => {
      console.log(error);
      return false;
    });
};

/**
 * 해당 유저의 지난 기부내역 확인
 * @returns 지난 기부내역 리스트
 */
export const participatedList = async () => {
  const local = axiosAuthApi();

  return await local
    .get('api/funding/list/participation')
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      console.log(error);
    });
};

/**
 *
 * @returns 카드 정보, 현재잔액, 기부금 총액
 */
export const getMainData = async () => {
  const local = axiosAuthApi();

  return await local
    .get('api/main/summary')
    .then((response) => {
      console.log(response.data);
      return response.data;
    })
    .catch((error) => {
      console.log(error);
    });
};

export function deleteMember() {
  // let data = {
  //   AuthenticationKey: key,
  // };
  return axiosAuthApi().delete('/api/member/delete');
}

export const chagePassword = async (
  prePassword: string,
  newPassword: string,
) => {
  const local = axiosAuthApi();

  return await local
    .put('api/member/password', {
      prevPassword: prePassword,
      newPassword: newPassword,
    })
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      console.log(error);
    });
};

export async function logout() {
  return axiosAuthApi().post('/api/member/logout');
}
