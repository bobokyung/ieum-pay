import Button from '@/stories/Button';
import classes from './receipt.module.scss';
import PageTitleCenter from '@/components/PageTitleCenter';
import CustomDashedBorder from '@/components/CustomDashedBorder';
import { commaizeNumber } from '@toss/utils';
import { useRef } from 'react';
import html2canvas from 'html2canvas';
import { useRouter } from 'next/router';
import { useQuery } from '@tanstack/react-query';
import { getReceipt } from '@/api/historyAxios';
import FetchError from '@/components/layouts/FetchError';
import { dataURLtoBlob } from '@/utils/blob';
import DashedUnderLine from '@/components/DashedUnderLine';

function ReceiptElement({ children }: { children: React.ReactNode }) {
  return <div className={classes.element}>{children}</div>;
}

export default function ReceiptPage() {
  const router = useRouter();
  const receiptId = router.query.receiptId;
  const { data, isError, isLoading, refetch } = useQuery({
    queryKey: [receiptId],
    queryFn: () => getReceipt(receiptId),
  });
  const receiptRef = useRef<HTMLDivElement>(null);
  function createShareData(dataURL: string) {
    const blob = dataURLtoBlob(dataURL);
    const data = {
      files: [
        new File([blob], 'image.png', {
          type: blob.type,
        }),
      ],
      title: '이음페이 기부 영수증',
      text: 'My text',
    };
    return data;
  }
  function downloadLink(dataURL: string) {
    const link = document.createElement('a');
    link.download = 'receipt.png';
    link.href = dataURL;
    link.click();
  }
  function handleClick() {
    if (receiptRef.current) {
      html2canvas(receiptRef.current).then(function (canvas) {
        const dataURL = canvas.toDataURL('image/png');
        const data = createShareData(dataURL);
        if (navigator.canShare(data)) {
          navigator.share(data);
        } else {
          downloadLink(dataURL);
        }
      });
    }
  }
  if (isError) {
    <FetchError onClick={() => refetch} />;
  }
  if (data)
    return (
      <>
        <main className={classes.container}>
          <PageTitleCenter title={''} description={''}></PageTitleCenter>
          <div className={classes.receipt} ref={receiptRef}>
            <h1>이음페이 기부 영수증</h1>
            <DashedUnderLine />
            <h2>{data.fundingTitle}</h2>
            <DashedUnderLine />
            <ReceiptElement>
              <b>기부자</b>
              <b>{data.name}</b>
            </ReceiptElement>
            <ReceiptElement>
              <b>배송지</b>
              <b>{data.facilityName}</b>
            </ReceiptElement>
            <ReceiptElement>
              <span>기부일시</span>
              <span>{data.historyDate}</span>
            </ReceiptElement>
            <DashedUnderLine />
            <ReceiptElement>
              <span>배송품목</span>
              <span>{data.fundingSummary}</span>
            </ReceiptElement>
            <DashedUnderLine />
            <ReceiptElement>
              <span>기부금</span>
              <span>{commaizeNumber(data.donationAmount)}원</span>
            </ReceiptElement>
          </div>
          <Button primary onClick={handleClick}>
            영수증 공유하기
          </Button>
        </main>
      </>
    );
}
