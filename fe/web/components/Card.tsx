import styles from './card.module.scss';
import bankColor from './bank.module.scss';

interface CardProps {
  index: number;
  card: number;
  bank: any;
  nickname: string;
  isMain: boolean;
  cardId: number;
  mainCardId: number;
}
export default function Card({
  index,
  card,
  bank,
  nickname,
  isMain,
  cardId,
  mainCardId,
}: CardProps) {
  const logoUrl: any = {
    신한카드: 'https://www.shinhancard.com/pconts/images/shcard/ci-shinhan.png',
    KB국민카드: 'https://img1.kbcard.com/LT/images_r/common/kbcard_Logo_v3.png',
    광주은행:
      'https://i.namu.wiki/i/nvBUwz04V7Nku4grdLmnwCPlpM_Feo5k7TZeL_ulRUT6Ic1qIn_R8WuFkyO9xA5BVWzL8iF2tJXxLtOgnaiLHg.svg',
    삼성카드: 'https://www.inicis.com/apple/popup/img/card_ss.png',
    수협은행:
      'https://i.namu.wiki/i/aGBgqIBemO3l45_2-CL6CMxF8DNXNZHRwkchw_b-byNVPmitL1UC6Ydv-9-6MVQdJWCttJvFwGdiIWEaBwp1ww.svg',
    NH농협카드: 'https://card.nonghyup.com/images/IP/cc/layout/n_logo.png',
    BC카드:
      'https://i.namu.wiki/i/8C2vbjyHD6CuR-QXhE5rFxRUDbANfr5OrSNxW_24bihqOYM1VUxR84cwTtXZ_3RE7x4qqb2Ald_YDBUiiF9Wag.svg',
    우리카드:
      'https://biz.chosun.com/resizer/YR43ZY7Vjg3tSGXvdOD4T0SLayM=/616x0/smart/cloudfront-ap-northeast-1.images.arcpublishing.com/chosunbiz/QVXYKQKAHJEGJFI2SNI3HZTKZM.png',
    롯데카드:
      'https://i.namu.wiki/i/KPgfJH2zZxOvGgO_6QU-XUZptxNfiZXyCMoeMu_p1PKM2WT_XG2j-C84wft7fE02rQYzsadt1dh8GdobWwq7dw.svg',
    현대카드:
      'https://upload.wikimedia.org/wikipedia/commons/f/f0/Hyundai_card_CI_new.png',
    하나카드:
      'https://i.namu.wiki/i/8JEi0JqCKkxGgCNptgwW8idqtPlZzc7RnuD4cbojlt1D45iybb0qQwQtXnJECnCG0ZUdPqK_pog0DQjNYVv1Mg.svg',
    전북은행:
      'https://i.namu.wiki/i/cxJG39QcJ8i5P4dznfK-ELik8uaNIWnvTrLv40_xe9JT2ENcSFfjJ1PGutPPU0JyYnxW5LVoZJYjUtFfjgHF6g.svg',
    제주은행:
      'https://www.shinhangroup.com/kr/asset/images/introduce/shinhan_CI_kr_2022-08.png',
    씨티카드: 'https://rasta.co.kr/web/upload/img/card/card_ct.png',
    이음페이: '/longLogo.svg',
    로딩: '/longLogoBlack.svg',
  };
  return (
    <div
      key={index}
      className={`${styles.card} ${styles['card' + card]} ${bankColor[bank]}`}
    >
      <div className={styles.container}>
        <div className={styles.top}>
          <img src={logoUrl[bank]} alt="bankLogo" className={styles.logo} />
        </div>
        {cardId == mainCardId ? (
          <div className={styles.mainMark}>대표 카드</div>
        ) : null}
        <div className={styles.bottom}>
          <div>
            <div className={styles.nicknameContainer}>{nickname}</div>
          </div>
        </div>
      </div>
    </div>
  );
}
