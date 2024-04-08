import {
  Dropdown,
  DropdownItem,
  DropdownMenu,
  DropdownTrigger,
} from '@nextui-org/react';
import { useRouter } from 'next/router';
import DropdownMenuIcon from './icons/DropdownMenuIcon';
import {
  CircularProgress,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  useDisclosure,
} from '@nextui-org/react';
import styles from './MainPageDropdown.module.scss';
import Button from '@/stories/Button';

interface CardManageProps {
  focused: number;
  setMain: (id: number) => void;
  callDeleteCard: (id: number) => void;
  cardLength?: any;
}
function MainPageDropdown({
  focused,
  setMain,
  callDeleteCard,
  cardLength,
}: CardManageProps) {
  const router = useRouter();
  const { isOpen, onOpen, onOpenChange } = useDisclosure();

  return (
    <>
      <Dropdown>
        <DropdownTrigger>
          <button>
            <DropdownMenuIcon />
          </button>
        </DropdownTrigger>
        <DropdownMenu aria-label="Static Actions">
          {cardLength == 4 ? (
            <DropdownItem key="new" onClick={onOpen}>
              카드 등록
            </DropdownItem>
          ) : (
            <DropdownItem key="new" onClick={() => router.push('/card')}>
              카드 등록
            </DropdownItem>
          )}

          <DropdownItem key="copy" onClick={() => setMain(focused)}>
            대표 카드 등록
          </DropdownItem>
          <DropdownItem
            key="delete"
            onClick={() => callDeleteCard(focused)}
            className="text-danger"
            color="danger"
          >
            카드 삭제
          </DropdownItem>
        </DropdownMenu>
      </Dropdown>
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
                  <p>카드는 4장까지 등록 가능합니다.</p>
                </div>
              </ModalBody>
              <ModalFooter className={styles.modalFooter}>
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
    </>
  );
}
export default MainPageDropdown;
