import { axiosAuthApi } from '@/utils/instance';

export async function postFCMToken(token: string) {
  const data = {
    fcmToken: token,
  };
  axiosAuthApi().post('/api/auth/register-fcm-token', data);
}
