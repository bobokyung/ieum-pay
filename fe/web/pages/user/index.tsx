import { useState, useMemo } from 'react';
import { useRouter } from 'next/router';
import { Input } from '@nextui-org/react';
import styles from '@/styles/user.module.scss';
import useUserStore from '@/stores/user-store';
import Button from '@/stories/Button';
import { IsRegister } from '@/api/userAxois';
import PageTitleLeft from '@/components/PageTitleLeft';

export default function User() {
  const { userInfo, setPhoneNumber } = useUserStore();
  const [inputValue, setValue] = useState('');
  const [chngeBtn, setChangeBtn] = useState(false);
  const router = useRouter();

  const isInvalid = useMemo(() => {
    if (inputValue == '') return false;

    inputValue.length >= 11 ? setChangeBtn(true) : '';

    return inputValue.length <= 11 ? false : true;
  }, [inputValue]);

  /**
   * 입력된 항목이랑 input feild 값 동기화 하는 함수
   * @param event
   */
  const handleInputValue = (event: any) => {
    // console.log(event);
    const newValue = event.target.value;
    setValue(newValue);
  };

  /**
   * 기존에 이미 가입한 회원 여부 판단 후 로그인으로 보낼지 회원가입으로 보낼지 판단하는 함수
   * @param phoneNumber 전화번호
   */
  async function checkIsRegister(phoneNumber: string) {
    setPhoneNumber(phoneNumber);
    const check = await IsRegister(phoneNumber);
    console.log(check.data);
    if (check != undefined) {
      router.push(check.data ? '/user/login' : '/user/register');
    } else {
      console.log('time out or error');
    }
  }

  return (
    <>
      <div className={styles.container}>
        <PageTitleLeft title="시작하기" />
        <Input
          type="tel"
          label="휴대폰 번호"
          variant="underlined"
          pattern="\d*"
          value={inputValue}
          isInvalid={isInvalid}
          errorMessage={isInvalid && '올바른 휴대전화번호를 입력해주세요'}
          onChange={handleInputValue}
          className={styles.inputTag}
          classNames={{ label: styles.labelTag }}
          required
          maxLength={11}
          autoFocus
        />
        <div className={styles.box}></div>
        <div className={chngeBtn ? styles.startBtnTag : styles.btnTagnone}>
          <Button primary onClick={() => checkIsRegister(inputValue)}>
            시작하기
          </Button>
        </div>
      </div>
    </>
  );
}
