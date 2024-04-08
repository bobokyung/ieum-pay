'use client';
import axios, { AxiosInstance } from 'axios';
import { eraseCookie, getCookie, setCookie } from './cookie';
const api = 'https://www.ieum-pay.site/';
// const accessToken = localStorage.getItem('access_token');
function expireToken() {
  window.alert('로그인이 만료되었습니다. 다시 로그인 해주세요');
  eraseCookie('access_token');
  localStorage.removeItem('access_token');
  window.location.href = '/user';
}
const axiosAuthApi = (): AxiosInstance => {
  const accessToken = getCookie('access_token');
  const instance = axios.create({
    baseURL: api,
    headers: { Authorization: 'Bearer ' + accessToken },
    timeout: 10000,
  });

  instance.interceptors.response.use(
    (response) => {
      return response;
    },
    async (error) => {
      const {
        config,
        response: { status },
      } = error;
      console.log(error.response.data);
      console.log(error.response);
      if (error.response?.status === 401) {
        if (error.response?.data.actionRequired === 'REFRESH_TOKEN') {
          const originRequest = config;
          axiosAuthApi()
            .put('/api/auth/token-renew')
            .then((response) => {
              const newAccessToken = response.data.data;
              console.log('리프레시');
              console.log(response);
              setCookie('access_token', newAccessToken, 1);
              localStorage['access_token'] = newAccessToken;
              originRequest.headers.Authorization = `Bearer ${newAccessToken}`;
              return axios(originRequest);
            })
            .catch((e) => {
              expireToken();
            });
        } else {
          expireToken();
        }
      }
    },
  );

  return instance;
};

const axiosApi = (): AxiosInstance => {
  const instance = axios.create({
    baseURL: api,
    timeout: 1000,
  });
  return instance;
};

export { axiosApi, axiosAuthApi };
