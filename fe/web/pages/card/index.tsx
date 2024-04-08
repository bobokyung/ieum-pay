import Header from '@/components/Header';
import Button from '@/stories/Button';
import classes from './index.module.scss';
import { useEffect, useRef, useState } from 'react';
import CameraIcon from '@/stories/icons/CameraIcon';
import Link from 'next/link';
import { GetServerSideProps } from 'next';
import {
  Input,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  useDisclosure,
} from '@nextui-org/react';
import { registerCard } from '@/api/paymentAxios';
import { useRouter } from 'next/router';
import CameraIconSmall from '@/stories/icons/CameraIconSmall';

function CardPage({
  cardNum,
  validThru,
}: {
  cardNum?: string;
  validThru?: string;
}) {
  const inputRefs = [
    useRef<HTMLInputElement>(null),
    useRef<HTMLInputElement>(null),
    useRef<HTMLInputElement>(null),
    useRef<HTMLInputElement>(null),
  ];
  const [validDate, setValidDate] = useState('');
  const [isValidDate, setIsValidDate] = useState(false);
  const [cvcValue, setCvcValue] = useState('');
  const [passwordValue, setPassword] = useState('');
  const { isOpen, onOpen, onOpenChange } = useDisclosure();
  const router = useRouter();

  const registerFn = async () => {
    console.log(
      `${inputRefs[0].current?.value}${inputRefs[1].current?.value}${inputRefs[2].current?.value}${inputRefs[3].current?.value}`,
    );
    const cardNumber = `${inputRefs[0].current?.value}${inputRefs[1].current?.value}${inputRefs[2].current?.value}${inputRefs[3].current?.value}`;

    const info = await registerCard(cardNumber);
    if (info != undefined) {
      if (info.data == -1) {
        onOpen();
      } else {
        router.push('/main');
      }
    }
  };

  useEffect(() => {
    if (cardNum != undefined) {
      if (
        inputRefs[0].current &&
        inputRefs[1].current &&
        inputRefs[2].current &&
        inputRefs[3].current
      ) {
        inputRefs[0].current.value = cardNum.substr(0, 4);
        inputRefs[1].current.value = cardNum.substr(4, 4);
        inputRefs[2].current.value = cardNum.substr(8, 4);
        inputRefs[3].current.value = cardNum.substr(12, 4);
      }
    }
    if (validThru != undefined) {
      setValidDate(validThru);
    }
  }, []);

  // 입력 값이 변경될 때 호출되는 함수
  const handleChange = (
    index: number,
    e: React.ChangeEvent<HTMLInputElement>,
  ) => {
    const currentValue = e.target.value;
    if (currentValue.length === 0 && index > 0) {
      inputRefs[index - 1].current?.focus();
    } else if (currentValue.length >= 4 && index < inputRefs.length - 1) {
      inputRefs[index + 1].current?.focus(); // 다음 input 요소로 포커스 이동
    }
  };
  const handlValidDate = (event: any) => {
    const newValue = event.target.value;
    if (newValue.length < 1) {
      setIsValidDate(false);
      setValidDate(newValue);
    }
    if (newValue.length < 6) {
      setValidDate(newValue);
      setIsValidDate(true);
    }
    if (newValue.length == 2 && validDate.length == 1) {
      setValidDate(newValue + '/');
    }
    if (newValue.length == 5) {
      if (newValue.substr(3, 2) > `24`) {
        setIsValidDate(false);
      } else if (
        newValue.substr(3, 2) == `24` &&
        newValue.substr(0, 2) > '03'
      ) {
        setIsValidDate(false);
      }
    }
  };

  const handlValidCVC = (event: any) => {
    const newValue = event.target.value;
    if (newValue.length < 4) {
      setCvcValue(newValue);
    }
  };
  const handlePassword = (event: any) => {
    const newValue = event.target.value;
    if (newValue.length < 3) {
      setPassword(newValue);
    }
  };

  return (
    <>
      <Header>카드 등록</Header>
      <div className={classes.cardContainer}>
        <div className={classes.first}>
          <form>
            <label>카드번호</label>
            <div className={classes['card-number']}>
              {inputRefs.map((ref, index) => (
                <>
                  <input
                    key={index}
                    type="number"
                    ref={ref}
                    maxLength={8}
                    onChange={(e) => handleChange(index, e)}
                  />
                  {index < 3 && <span>-</span>}
                </>
              ))}
            </div>
            <hr />
          </form>
          <Link href="/card/scan">
            <div className={classes.camera}>
              <CameraIconSmall />
            </div>
          </Link>
        </div>
        <div className={classes.dateCvcContainer}>
          <div className={classes.validDate}>
            <p>유효기간</p>
            <Input
              // pattern="\d*"
              value={validDate}
              isInvalid={isValidDate}
              onChange={handlValidDate}
              classNames={{ base: classes.input }}
              variant="underlined"
            />
          </div>
          <div className={classes.cvc}>
            <p>cvc</p>
            <Input
              variant="underlined"
              pattern="\d*"
              classNames={{ base: classes.input }}
              value={cvcValue}
              onChange={handlValidCVC}
            />
          </div>
        </div>
        <p>비밀번호 앞 두자리</p>
        <div className={classes.password}>
          <Input
            variant="underlined"
            pattern="\d*"
            classNames={{ base: classes.input }}
            value={passwordValue}
            onChange={handlePassword}
          />
          <p>**</p>
        </div>

        <div className={classes.btnCont}>
          <p className={classes.subtext}>
            가상 서비스이므로 CVC와 비밀번호는 입력하지 않아도 됩니다.
          </p>
          <Button primary onClick={registerFn}>
            등록
          </Button>
        </div>
      </div>
      <Modal
        className={classes.modalComp}
        isOpen={isOpen}
        onOpenChange={onOpenChange}
      >
        <ModalContent>
          {(onClose) => (
            <>
              <ModalBody className={classes.modalContainer}>
                <div>유효하지 않은 카드 정보 입니다</div>
              </ModalBody>
              <ModalFooter className={classes.modalFooter}>
                <Button
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
    </>
  );
}
export default CardPage;
export const getServerSideProps: GetServerSideProps = async ({ query }) => {
  // query 객체 사용
  return {
    props: {
      cardNum: query.cardNum != undefined ? query.cardNum : '',
      validThru: query.validThru != undefined ? query.validThru : '',
    },
  };
};
