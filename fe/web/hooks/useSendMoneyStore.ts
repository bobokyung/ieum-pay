import { create } from 'zustand';

const MAX_TRANSFER = 2000000;

interface sendMoneyInfoType {
  수취인: string;
  수취은행: string;
  수취계좌: string;
  송금금액: number;
  송금은행: string;
  잔액: number;
}

interface SendMoneyInfoState {
  sendMoneyInfo: sendMoneyInfoType;
}

interface SendMoneyInfoActions {
  setSendMoneyInfo: (sendMoneyInfo: sendMoneyInfoType) => void;
  pushNumber: (amount: number) => void;
  popNumber: () => void;
  addAmount: (amount: number) => void;
  setAmount: (amount: number) => void;
  setFullAmount: () => void;
  setBalance: (balance: number) => void;
  deleteSendMoneyInfo: () => void;
  setReceiverInfo: (name: string, phone: string, bank: string) => void;
}

const defaultState = {
  수취인: '',
  수취은행: '',
  수취계좌: '',
  송금금액: 0,
  송금은행: '이음페이',
  잔액: 30000,
};

const useSendMoneyInfo = create<SendMoneyInfoState & SendMoneyInfoActions>(
  (set) => ({
    sendMoneyInfo: defaultState,
    pushNumber: (amount: number) => {
      set((state) => ({
        sendMoneyInfo: {
          수취인: state.sendMoneyInfo.수취인,
          수취은행: state.sendMoneyInfo.수취은행,
          수취계좌: state.sendMoneyInfo.수취계좌,
          송금금액: Math.min(
            MAX_TRANSFER,
            state.sendMoneyInfo.송금금액 * 10 + amount,
          ),
          송금은행: state.sendMoneyInfo.송금은행,
          잔액: state.sendMoneyInfo.잔액,
        },
      }));
    },
    popNumber: () => {
      set((state) => ({
        sendMoneyInfo: {
          수취인: state.sendMoneyInfo.수취인,
          수취은행: state.sendMoneyInfo.수취은행,
          수취계좌: state.sendMoneyInfo.수취계좌,
          송금금액: Math.trunc(state.sendMoneyInfo.송금금액 / 10),
          송금은행: state.sendMoneyInfo.송금은행,
          잔액: state.sendMoneyInfo.잔액,
        },
      }));
    },
    addAmount: (amount: number) => {
      set((state) => ({
        sendMoneyInfo: {
          수취인: state.sendMoneyInfo.수취인,
          수취은행: state.sendMoneyInfo.수취은행,
          수취계좌: state.sendMoneyInfo.수취계좌,
          송금금액: Math.min(
            state.sendMoneyInfo.송금금액 + amount,
            MAX_TRANSFER,
          ),
          송금은행: state.sendMoneyInfo.송금은행,
          잔액: state.sendMoneyInfo.잔액,
        },
      }));
    },
    setAmount: (amount: number) => {
      set((state) => ({
        sendMoneyInfo: {
          수취인: state.sendMoneyInfo.수취인,
          수취은행: state.sendMoneyInfo.수취은행,
          수취계좌: state.sendMoneyInfo.수취계좌,
          송금금액: Math.min(amount, MAX_TRANSFER),
          송금은행: state.sendMoneyInfo.송금은행,
          잔액: state.sendMoneyInfo.잔액,
        },
      }));
    },
    setBalance: (balance: number) => {
      set((state) => ({
        sendMoneyInfo: {
          수취인: state.sendMoneyInfo.수취인,
          수취은행: state.sendMoneyInfo.수취은행,
          수취계좌: state.sendMoneyInfo.수취계좌,
          송금금액: state.sendMoneyInfo.송금금액,
          송금은행: state.sendMoneyInfo.송금은행,
          잔액: balance,
        },
      }));
    },
    setFullAmount: () => {
      set((state) => ({
        sendMoneyInfo: {
          수취인: state.sendMoneyInfo.수취인,
          수취은행: state.sendMoneyInfo.수취은행,
          수취계좌: state.sendMoneyInfo.수취계좌,
          송금금액: state.sendMoneyInfo.잔액,
          송금은행: state.sendMoneyInfo.송금은행,
          잔액: state.sendMoneyInfo.잔액,
        },
      }));
    },
    setSendMoneyInfo: (sendMoneyInfo: sendMoneyInfoType) => {
      set({ sendMoneyInfo });
    },
    deleteSendMoneyInfo: () => {
      set({ sendMoneyInfo: defaultState });
    },
    setReceiverInfo: (name: string, phone: string, bank: string) => {
      set((state) => ({
        sendMoneyInfo: {
          수취인: name,
          수취은행: state.sendMoneyInfo.수취은행,
          수취계좌: phone,
          송금금액: state.sendMoneyInfo.송금금액,
          송금은행: bank,
          잔액: state.sendMoneyInfo.잔액,
        },
      }));
    },
  }),
);

export default useSendMoneyInfo;
