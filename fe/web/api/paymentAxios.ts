'use-clinet';
import { axiosApi, axiosAuthApi } from '@/utils/instance';

export async function getBalance() {
  return axiosAuthApi()
    .get('/api/pay/balance')
    .then((res) => res.data.data);
}

export const getPaymentInfo = async (store: string, price: string) => {
  const local = axiosAuthApi();
  return await local
    .get(`/api/payment/info/${store}/${price}`)
    .then((response) => {
      console.log(response);
      return response.data;
    })
    .catch((error) => {
      console.log(error.message);
    });
};

export const confirmPassword = async (password: string) => {
  const local = axiosAuthApi();
  return await local
    .post('/api/auth/password', { paymentPassword: password })
    .then((response) => {
      console.log(response);
      return response.data;
    })
    .catch((error) => {
      console.log(error.message);
      return error;
    });
};

interface paymentInfo {
  storeId: number;
  price: number;
  cardNickname: string;
  storeName: string;
  paymoneyAmount: number;
  chargeAmount: number;
  donationMoney: number;
  authenticationKey: string;
}

export const payment = async (paymentInfo: paymentInfo, key: string) => {
  const local = axiosAuthApi();
  paymentInfo.authenticationKey = key;
  return await local
    .post('api/payment', paymentInfo)
    .then((response) => {
      console.log(response);
      return response.data;
    })
    .catch((error) => {
      console.log(error);
    });
};

export async function postCardImage(formData: FormData) {
  let axiosConfig = {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  };
  return axiosAuthApi().post('/api/card/ocr', formData, axiosConfig);
}

export async function getPaymentHistory(historyId: string) {
  const local = axiosAuthApi();
  return await local
    .get(`api/payment/${historyId}`)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      console.log(error);
    });
}

export async function registerCard(cardNumber: string) {
  const local = axiosAuthApi();

  return await local
    .post('api/card/register', {
      cardNumber: cardNumber,
      cardNickname: '',
    })
    .then((response) => {
      console.log(response);
      return response.data;
    })
    .catch((error) => {
      console.log(error);
    });
}

export async function setMainCard(id: number) {
  const local = axiosAuthApi();

  return await local
    .put('api/card/main', { registerCardId: id })
    .then((response) => {
      console.log(response);
      return response.data;
    })
    .catch((error) => {
      console.log(error);
    });
}

export async function deleteCard(id: number) {
  const local = axiosAuthApi();

  return await local
    .put('api/card/update', { registeredCardId: id })
    .then((response) => {
      console.log(response);
      return response.data;
    })
    .catch((error) => {
      console.log(error);
    });
}
