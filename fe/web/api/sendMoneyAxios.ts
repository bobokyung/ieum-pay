'use-clinet';
import { axiosAuthApi } from '@/utils/instance';

export async function getMemberByPhoneNumber(phoneNumber: number | string) {
  return axiosAuthApi()
    .post('/api/member/search?phone-number=' + phoneNumber)
    .then((response) => {
      console.log(response.data.data);
      return response.data.data;
    })
    .catch((error) => {
      console.log(error.message);
    });
}

export async function sendPayMoney(
  phoneNumber: string,
  amount: number,
  key: string,
) {
  let data = {
    phoneNumber: phoneNumber,
    amount: amount,
    authenticationKey: key,
  };
  return axiosAuthApi().post('/api/pay/remittance/paymoney', data);
}
