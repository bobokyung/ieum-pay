import useUserStore from '@/stores/user-store';
import { Input } from '@nextui-org/react';
import { useState } from 'react';
import {
  EyeFilledIcon,
  EyeSlashFilledIcon,
} from '@/components/icons/PasswordIcon';
import styes from '@/styles/input-info.module.scss';
import { useRouter } from 'next/router';
import Button from '@/stories/Button';
import PageTitleLeft from '@/components/PageTitleLeft';
import styles from '@/styles/user.module.scss';

export default function inputInfo() {
  const [userPassword, setPasswordValue] = useState('');
  const [userNickName, setNickNameValue] = useState('');
  const [userName, setNameValue] = useState('');
  const [isVisible, setIsVisible] = useState(false);
  const [isInValid, setIsInValid] = useState(false);
  const [isValid, setIsValid] = useState(false);
  const [nicknameVisible, setNicknameVisible] = useState(false);
  const [nicknameIsValid, setNicknameIsValid] = useState(false);
  const [nameVisible, setNameVisible] = useState(false);
  const [nameIsValid, setNameIsValid] = useState(false);
  const [cnt, setCnt] = useState(0);
  const { userInfo, setPassword, setUserName, setUserNickname } =
    useUserStore();
  const router = useRouter();

  const toggleVisibility = () => setIsVisible(!isVisible);

  const handlNameValue = (evnet: any) => {
    const newValue = evnet.target.value;
    setNameValue(newValue);

    // 한글만 허용/^[가-힣]+$/
    const idRegExp = /^(?=.*[가-힣])[가-힣]{2,10}$/;
    if (newValue.length > 2 && !idRegExp.test(newValue)) {
      setNameIsValid(true);
    } else {
      setNameIsValid(false);
    }
  };

  const handleNicknameValue = (event: any) => {
    const newValue = event.target.value;
    setNickNameValue(newValue);
    // 특수문자 허용 안함
    const idRegExp = /[`ㄱ-ㅎ|ㅏ-ㅣ|~!@#$%^&*()_|+\-=?;:'" ,.<>\{\}\[\]\\\/]/;
    if (
      idRegExp.test(newValue) &&
      newValue.length < 11 &&
      newValue.length >= 2
    ) {
      setNicknameIsValid(true);
    } else {
      setNicknameIsValid(false);
    }
  };

  const handlePasswordInput = (event: any) => {
    // console.log(event);
    const newValue = event.target.value;
    // 영어소문자, 숫자, 특수문자 포함 여부 확인 8 자 이상 20자 이하
    const idRegExp =
      /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,20}$/;

    setPasswordValue(newValue);

    if (!idRegExp.test(newValue)) {
      setIsInValid(true);
      setIsValid(true);
    } else {
      setIsInValid(false);
      setIsValid(false);
    }
  };

  const handleClick = () => {
    if (cnt == 0 && !isValid) {
      setCnt(1);
      setNicknameVisible(true);
    } else if (cnt == 1 && !nicknameIsValid) {
      setCnt(2);
      setNameVisible(true);
    } else if (cnt == 2 && !nameIsValid) {
      setPassword(userPassword);
      setUserName(userName);
      setUserNickname(userNickName);
      router.push({
        pathname: '/password',
        query: {
          id: 1,
        },
      });
    }
  };

  return (
    <div className={styles.container}>
      <PageTitleLeft
        title="회원가입"
        description={'회원 정보를 입력해주세요'}
      />
      <div className={nameVisible ? '' : styes.isVisible}>
        <Input
          label="이름"
          classNames={{
            inputWrapper: styes.inputWrapper,
          }}
          variant="underlined"
          onChange={handlNameValue}
          value={userName}
          isInvalid={nameIsValid}
          errorMessage={nameIsValid && '이름을 올바른 형식으로 입력해주세요'}
        ></Input>
      </div>
      <div className={nicknameVisible ? '' : styes.isVisible}>
        <Input
          label="닉네임"
          variant="underlined"
          classNames={{
            inputWrapper: styes.inputWrapper,
          }}
          onChange={handleNicknameValue}
          value={userNickName}
          isInvalid={nicknameIsValid}
          errorMessage={
            nicknameIsValid &&
            '특수문자를 제외한 2글자 이상 10자 이하로 해주세요'
          }
        ></Input>
      </div>
      <Input
        label="비밀번호"
        variant="underlined"
        classNames={{
          inputWrapper: styes.inputWrapper,
        }}
        value={userPassword}
        onChange={handlePasswordInput}
        isInvalid={isInValid}
        description={isValid && '사용 가능 합니다'}
        errorMessage={
          isInValid &&
          '8~20자의 영문 대소문자, 숫자, 특수문자를 모두 포함한 비밀번호를 입력해주세요'
        }
        endContent={
          <button
            className="focus:outline-none"
            type="button"
            onClick={toggleVisibility}
          >
            {isVisible ? (
              <EyeFilledIcon className="text-2xl text-default-400 pointer-events-none" />
            ) : (
              <EyeSlashFilledIcon className="text-2xl text-default-400 pointer-events-none" />
            )}
          </button>
        }
        type={isVisible ? 'text' : 'password'}
      />
      <Input
        isDisabled
        type="text"
        label="휴대폰 번호"
        classNames={{
          inputWrapper: styes.inputWrapper,
        }}
        variant="underlined"
        value={userInfo.phoneNumber}
      />
      <Button primary size="thin" onClick={handleClick}>
        {cnt == 2 ? '확인' : '다음'}
      </Button>
    </div>
  );
}
