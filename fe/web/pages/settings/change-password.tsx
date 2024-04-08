import HeaderHome from '@/components/HeaderHome';
import {
  Input,
  Modal,
  ModalContent,
  ModalBody,
  ModalFooter,
  useDisclosure,
} from '@nextui-org/react';
import styles from './settings.module.scss';
import { useState } from 'react';
import {
  EyeFilledIcon,
  EyeSlashFilledIcon,
} from '@/components/icons/PasswordIcon';
import Button from '@/stories/Button';
import { chagePassword } from '@/api/userAxois';
import { useRouter } from 'next/router';

export default function ChangePassword() {
  const [password1, setPassword1] = useState('');
  const [password2, setPassword2] = useState('');
  const [password3, setPassword3] = useState('');

  const [isInValid1, setIsInValid1] = useState(false);
  const [isInValid2, setIsInValid2] = useState(false);
  const [isInValid3, setIsInValid3] = useState(false);

  const [isValid1, setIsValid1] = useState(false);
  const [isValid2, setIsValid2] = useState(false);

  const idRegExp =
    /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,20}$/;

  const handlePasswordInput1 = (event: any) => {
    const newValue = event.target.value;
    setPassword1(newValue);

    if (newValue.length == 0) setIsInValid1(false);
    else if (!idRegExp.test(newValue)) {
      setIsInValid1(true);
    } else {
      setIsInValid1(false);
    }
  };

  const handlePasswordInput2 = (event: any) => {
    const newValue = event.target.value;
    setPassword2(newValue);

    if (newValue.length == 0) setIsInValid2(false);
    else if (!idRegExp.test(newValue)) {
      setIsInValid2(true);
      setIsValid1(false);
    } else {
      setIsInValid2(false);
      setIsValid1(true);
    }
  };
  const handlePasswordInput3 = (event: any) => {
    const newValue = event.target.value;
    setPassword3(newValue);

    if (password2 != newValue) {
      setIsInValid3(true);
      setIsValid2(false);
    } else {
      setIsInValid3(false);
      setIsValid2(true);
    }
  };

  const [isVisible1, setIsVisible1] = useState(false);
  const [isVisible2, setIsVisible2] = useState(false);
  const [isVisible3, setIsVisible3] = useState(false);

  const toggleVisibility1 = () => setIsVisible1(!isVisible1);
  const toggleVisibility2 = () => setIsVisible2(!isVisible1);
  const toggleVisibility3 = () => setIsVisible3(!isVisible1);

  const router = useRouter();

  const { isOpen, onOpen, onOpenChange } = useDisclosure();

  async function passwordApi() {
    const res = await chagePassword(password1, password2);
    return res?.status == 'SUCCESS' ? true : false;
  }

  const handleClick = async () => {
    const res = passwordApi();
    (await res) ? router.push('/main') : onOpen();
  };

  return (
    <>
      <HeaderHome>비밀번호 변경</HeaderHome>
      <div className={styles.passwordConatiner}>
        <Input
          label="기존 비밀번호"
          variant="underlined"
          classNames={{
            base: styles.inputWrapper,
          }}
          value={password1}
          onChange={handlePasswordInput1}
          isInvalid={isInValid1}
          errorMessage={
            isInValid1 &&
            '8~20자의 영문 대소문자, 숫자, 특수문자를 모두 포함한 비밀번호를 입력해주세요'
          }
          endContent={
            <button
              className="focus:outline-none"
              type="button"
              onClick={toggleVisibility1}
            >
              {isVisible1 ? (
                <EyeFilledIcon className="text-2xl text-default-400 pointer-events-none" />
              ) : (
                <EyeSlashFilledIcon className="text-2xl text-default-400 pointer-events-none" />
              )}
            </button>
          }
          type={isVisible1 ? 'text' : 'password'}
        />

        <Input
          label="새로운 비밀번호"
          variant="underlined"
          classNames={{
            base: styles.inputWrapper,
          }}
          value={password2}
          onChange={handlePasswordInput2}
          isInvalid={isInValid2}
          description={isValid1 && '사용 가능 합니다'}
          errorMessage={
            isInValid2 &&
            '8~20자의 영문 대소문자, 숫자, 특수문자를 모두 포함한 비밀번호를 입력해주세요'
          }
          endContent={
            <button
              className="focus:outline-none"
              type="button"
              onClick={toggleVisibility2}
            >
              {isVisible2 ? (
                <EyeFilledIcon className="text-2xl text-default-400 pointer-events-none" />
              ) : (
                <EyeSlashFilledIcon className="text-2xl text-default-400 pointer-events-none" />
              )}
            </button>
          }
          type={isVisible2 ? 'text' : 'password'}
        />

        <Input
          label="새로운 비밀번호 확인"
          variant="underlined"
          classNames={{
            base: styles.inputWrapper,
          }}
          value={password3}
          onChange={handlePasswordInput3}
          isInvalid={isInValid3}
          description={isValid2 && '일치합니다'}
          errorMessage={isInValid3 && '새 비밀번호가 일치하지 않습니다.'}
          endContent={
            <button
              className="focus:outline-none"
              type="button"
              onClick={toggleVisibility3}
            >
              {isVisible3 ? (
                <EyeFilledIcon className="text-2xl text-default-400 pointer-events-none" />
              ) : (
                <EyeSlashFilledIcon className="text-2xl text-default-400 pointer-events-none" />
              )}
            </button>
          }
          type={isVisible3 ? 'text' : 'password'}
        />
        <div className={styles.btn}>
          <Button primary size="thin" onClick={handleClick}>
            확인
          </Button>
        </div>
      </div>
      <Modal
        className={styles.modalComp}
        isOpen={isOpen}
        onOpenChange={onOpenChange}
      >
        <ModalContent>
          {(onClose) => (
            <>
              <ModalBody className={styles.modalContainer}>
                <div>
                  <p>비밀번호를 한번 더 확인해주세요!</p>
                </div>
              </ModalBody>
              <ModalFooter className={styles.modalFooter}>
                <Button primary size="thin" onClick={onClose}>
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
