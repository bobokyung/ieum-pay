import Link from 'next/link';
import classes from './tabbar.module.scss';
import CreditCardIcon from './icons/CreditCardIcon';
import HistoryIcon from './icons/HistoryIcon';
import UserIcon from './icons/UserIcon';
import SendMoneyIcon from './icons/SendMoneyIcon';
import FundraisingIcon from './icons/FundraisingIcon';
import HistoryIconActive from './icons/HistoryIconActive';
import FundraisingIconActive from './icons/FundraisingIconActive';
import UserIconActive from './icons/UserIconActive';
import CreditCardIconActive from './icons/CreditCardIconActive';
import SendMoneyIconActive from './icons/SendMoneyIconActive';
import CameraIcon from './icons/CameraIcon';
import Vibrate from '@/utils/vibrate';
import Button from './Button';
import {
  CircularProgress,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  useDisclosure,
} from '@nextui-org/react';

//object literal
export const tabBarElementCode = {
  history: 'history',
  fundraising: 'fundraising',
  payment: 'payment',
  sendMoney: 'sendMoney',
  myPage: 'myPage',
  none: 'none',
} as const;

export type TabBarElementCodeValue =
  (typeof tabBarElementCode)[keyof typeof tabBarElementCode];

interface TabBarProps {
  selected: TabBarElementCodeValue;
  cardLength?: any;
}

function TabBar({
  selected = tabBarElementCode.history,
  cardLength,
}: TabBarProps) {
  function vibrate() {
    Vibrate(10);
  }
  const { isOpen, onOpen, onOpenChange } = useDisclosure();

  return (
    <nav className={classes.container}>
      <ul className={classes.ul}>
        <li>
          <Link
            href="/history"
            className={classes.link}
            onClick={() => vibrate()}
          >
            <div
              className={`${classes.wrapper} ${selected === tabBarElementCode.history ? classes.active : ''}`}
            >
              {selected === tabBarElementCode.history ? (
                <HistoryIconActive />
              ) : (
                <HistoryIcon />
              )}
              내역
            </div>
          </Link>
        </li>
        <li>
          <Link
            href="/fundraising"
            className={classes.link}
            onClick={() => vibrate()}
          >
            <div
              className={`${classes.wrapper} ${selected === tabBarElementCode.fundraising ? classes.active : ''}`}
            >
              {selected === tabBarElementCode.fundraising ? (
                <FundraisingIconActive />
              ) : (
                <FundraisingIcon />
              )}
              모금
            </div>
          </Link>
        </li>
        <li>
          {selected == 'payment' ? (
            cardLength == 0 ? (
              <div className={classes.link} onClick={() => onOpen()}>
                <div
                  className={`${classes.wrapper} ${selected === tabBarElementCode.payment ? classes.active : ''}`}
                >
                  <div className={classes.cameraWrapper}>
                    <CameraIcon />
                  </div>
                  <CreditCardIcon />
                  결제
                </div>
              </div>
            ) : (
              <Link
                href="/payment/qrReader"
                className={classes.link}
                onClick={() => vibrate()}
              >
                <div
                  className={`${classes.wrapper} ${selected === tabBarElementCode.payment ? classes.active : ''}`}
                >
                  <div className={classes.cameraWrapper}>
                    <CameraIcon />
                  </div>
                  <CreditCardIcon />
                  결제
                </div>
              </Link>
            )
          ) : (
            <Link
              href="/main"
              className={classes.link}
              onClick={() => vibrate()}
            >
              <div className={`${classes.wrapper}`}>
                <CreditCardIcon />
                결제
              </div>
            </Link>
          )}
        </li>
        <li>
          <Link
            href="/send-money"
            className={classes.link}
            onClick={() => vibrate()}
          >
            <div
              className={`${classes.wrapper} ${selected === tabBarElementCode.sendMoney ? classes.active : ''}`}
            >
              {selected === tabBarElementCode.sendMoney ? (
                <SendMoneyIconActive />
              ) : (
                <SendMoneyIcon />
              )}
              송금
            </div>
          </Link>
        </li>
        <li>
          <Link
            href="/my-page"
            className={classes.link}
            onClick={() => vibrate()}
          >
            <div
              className={`${classes.wrapper} ${selected === tabBarElementCode.myPage ? classes.active : ''}`}
            >
              {selected === tabBarElementCode.myPage ? (
                <UserIconActive />
              ) : (
                <UserIcon />
              )}
              정보
            </div>
          </Link>
        </li>
      </ul>
      <Modal
        className={classes.modalComp}
        isOpen={isOpen}
        onOpenChange={onOpenChange}
      >
        <ModalContent>
          {(onClose) => (
            <>
              <ModalBody className={classes.modalContainer}>
                <div>
                  <p>결제하시려면 카드를 등록해주세요.</p>
                </div>
              </ModalBody>
              <ModalFooter className={classes.modalFooter}>
                <Button
                  primary
                  size="thin"
                  onClick={() => {
                    onClose();
                  }}
                >
                  확인
                </Button>
              </ModalFooter>
            </>
          )}
        </ModalContent>
      </Modal>
    </nav>
  );
}
export default TabBar;
