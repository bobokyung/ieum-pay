import { axiosAuthApi } from '@/utils/instance';

export async function getHistory() {
  return axiosAuthApi()
    .get('/api/pay/history/1')
    .then((response) => {
      console.log(response.data.data);
      return response.data.data;
    })
    .catch((error) => {
      console.log(error.message);
    });
}
export async function getReceipt(historyId: string | string[] | undefined) {
  return axiosAuthApi()
    .get(`/api/funding/receipt/${historyId}`)
    .then((res) => {
      console.log(res.data.data);
      return res.data.data;
    });
}
