import { create } from 'zustand';

interface paymentInfoType {
  storeId: number;
  cardNickname: string;
  price: number;
  storeName: string;
  paymoneyAmount: number;
  chargeAmount: number;
  donationMoney: number;
  authenticationKey: string;
}

interface PaymentInfoState {
  paymentInfo: paymentInfoType;
}

interface PaymentInfoActions {
  setPaymentInfo: (paymentInfo: paymentInfoType) => void;
}

const defaultState = {
  storeId: 0,
  cardNickname: '삼성카드',
  price: 0,
  storeName: 'string',
  paymoneyAmount: 0,
  chargeAmount: 0,
  donationMoney: 0,
  authenticationKey: '',
};

const usePaymentInfo = create<PaymentInfoState & PaymentInfoActions>((set) => ({
  paymentInfo: defaultState,
  setPaymentInfo: (paymentInfo: paymentInfoType) => {
    set({ paymentInfo });
  },
}));

export default usePaymentInfo;
