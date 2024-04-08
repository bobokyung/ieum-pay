# 이음페이

<img src="./img/이음페이.PNG" width="100%"/>

<br/>

## 프로젝트 진행 기간

`2024.02.19 ~ 2024.04.04 (약 7주)`

<br/>

## ❤ 팀 소개

### 팀명

> 📢 안녕하세요! 핀테크 주제로 잔돈 기부와 간편 결제를 합친 서비스 **이음페이** 프로젝트를 진행한 팀《103번 버스》입니다.

### 팀원 소개

### Frontend

|                                                                                           |                                                                                     |                                                                                    |     |
| :---------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------: | :-: |
| <img src="./img/profile/김예지.jpg" width="190px" height="200px" /> <br>**[팀장] 김예지** | <img src="./img/profile/이은규.jpeg" width="190px" height="200px" /> <br>**이은규** | <img src="./img/profile/박수형.jpg" width="190px" height="200px" /> <br>**박수형** |

<br/>

### Backend

|                                                                                     |                                                                                    |                                                                                    |     |
| :---------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------: | :-: |
| <img src="./img/profile/고명성.jpeg" width="190px" height="200px" /> <br>**고명성** | <img src="./img/profile/김범수.jpg" width="190px" height="200px" /> <br>**김범수** | <img src="./img/profile/김보경.jpg" width="190px" height="200px" /> <br>**김보경** |

<br/>

## 🎉 프로젝트 요약

### 💡프로젝트 명: 이음페이

### 목적

간편결제 서비스와 잔돈 기부 기능을 통합하여, 사용자가 소비 활동을 통해 사회 공헌에 쉽게 참여할 수 있도록 함.

### 기대효과

- **사회 공헌 참여의 용이성**: 간편결제 시 발생하는 잔돈을 기부함으로써, 사용자가 일상 속에서 쉽게 사회 공헌 활동에 참여할 수 있음.
- **세액 공제 혜택**: 기부 후 발급되는 기부 영수증을 통해 연말 정산 시 세액 공제 혜택을 받을 수 있음.
- **투명성 증대**: 크라우드 펀딩 방식을 적용하여 현금이 아닌 물품을 지원함으로써 기부 과정의 불투명성을 줄임.

### 차별점

- **다기능 통합 서비스**: 잔돈 기부, 크라우드 펀딩, 간편결제 기능을 하나의 서비스에서 제공하여 사용자의 편의성을 극대화함.

<br/>

## ✨주요 기능 및 구현

### 💡 계정:

- **MMS 휴대폰 인증**: 사용자의 실제성 및 안전성을 보장하기 위해 MMS를 통한 휴대폰 인증 방식을 도입. 본인인증 과정을 통해 사용자 관리의 투명성을 높임.
- **2차 비밀번호 도입**: 결제 및 중요 계정 작업 시 추가 보안 강화를 위한 2차 비밀번호(결제 비밀번호) 기능을 구현. 사용자의 자산과 정보 보호에 중점을 둠.

### 💡 간편결제:

- **QR 코드 결제**: 사용자의 편리한 결제 경험을 위해 QR 코드를 스캔하는 방식으로 간편 결제 기능을 제공. 온오라인 환경 모두에서 손쉽게 결제가 가능하도록 설계.

### 💡 카드연동

- **카드 등록**: 사용자는 본인의 신용카드 및 체크카드를 애플리케이션에 등록하여 간편하게 결제할 수 있습니다. 안전한 카드 정보 저장 및 관리 시스템을 통해 사용자 편의성을 제공합니다.
- **자동 충전**: 사용자가 결제 기능을 이용할 경우 부족한 금액을 등록된 카드를 통해서 자동 충전합니다.

### 💡 송금

- **휴대전화 번호 송금**: 이음페이 사용자 간 휴대전화의 번호를 통해 송금 기능을 지원하여, 친구나 가족에게 손쉽게 돈을 보낼 수 있습니다.

### 💡 잔돈기부

- **자동 잔돈 기부**: 사용자는 기부 목록 중 자신이 원하는 기부에 연동 할 수 있으며, 결제 과정에서 발생하는 잔동을 자동함으로 사회 공헌 활동에 참여할 수 있게 합니다.
- **크라우드 펀딩 기부**: 기부금을 투명하게 관리하기 위해서 직접 금액을 기부하는 것이아닌 물건 구매 금액을 지원하여 물건을 구매할 수 있도록 지원합니다.

### 💡 결제 테스트

- **시연을 위한 쇼핑몰 구현**: 프로젝트 시연 및 테스트를 위해 쇼핑몰 시스템을 구축. 삼성스토어와 같은 실제 상점을 모사하여 사용자가 이음페이를 통해 상품을 구매하고 기능을 직접 체험할 수 있도록 하였습니다.

<br/>

## 👩🏼‍💻 담당 역할

### 김예지 - Frontend

- 팀장, 프론트엔드 리더
- 인프라
- 송금, 내역, 상단하단, 간편 결제 비밀번호 페이지 구현

### 이은규 - Frontend

- UI/UX 디자인
- 결제, 설정, 버튼, 랜딩 페이지 구현

### 박수형 - Frontend

- 기능 구현
- 정보, 모금, 로그인/아웃 페이지
- 인트로, 회원 페이지 구현

### 고명성 - Backend

- 백엔드 리더
- 인프라
- 알람 서버 구현
- 에러 핸들링
- DB 설계 및 관리

### 김범수 - Backend

- 인증, 인가 관리 (MAIL, 2FA - Redis)
- 펀딩 서버 구현
- DB 설계 및 관리

### 김보경 - Backend

- 카드, 페이 서버 구현
- Feign Client 연결
- DB 설계 및 관리

## 🖥 서비스 화면

### 서비스 소개 페이지

<div markdown="1">
<img src="./img/service/service1.png"/>
<img src="./img/service/service2.png"/>
</div>

---

### 회원가입 페이지

<div markdown="1">
<img src="./img/service/회원가입1.jpg" width="200px" />
<img src="./img/service/회원가입2.jpg" width="200px" />
<img src="./img/service/회원가입3.jpg" width="200px" />
<img src="./img/service/회원가입4.jpg" width="200px" />
<img src="./img/service/회원가입5.jpg" width="200px" />
<img src="./img/service/회원가입6.jpg" width="200px" />
<img src="./img/service/회원가입7.jpg" width="200px" />
<img src="./img/service/회원가입8.jpg" width="200px" />
</div>

---

### PWA

<div markdown="1">
<img src="./img/service/메인화면_Web.png" width="200px" />
<img src="./img/service/PWA.png" width="200px" />
<img src="./img/service/PWA_App.png" width="200px" />
</div>

---

### 로그인 페이지

<div markdown="1">
<img src="./img/service/로그인1.jpg" width="200px" />
<img src="./img/service/로그인2.jpg" width="200px" />
</div>

---

### 메인 페이지

<div markdown="1">
<img src="./img/service/메인카드등록전.jpg" width="200px" />
<img src="./img/service/메인카드등록후.jpg" width="200px" />
<img src="./img/service/메인다중카드.jpg" width="200px" />
</div>

---

### 카드 연동

<div markdown="1">
<img src="./img/service/카드입력.jpg" width="200px" />
<img src="./img/service/카드ocr.jpg" width="200px" />
<img src="./img/service/카드입력후.jpg" width="200px" />
</div>

---

### 기부 목록 확인

<div markdown="1">
<img src="./img/service/기부목록.jpg" width="200px" />
<img src="./img/service/기부완료목록.jpg" width="200px" />
<img src="./img/service/기부상세.jpg" width="200px" />
<img src="./img/service/기부연동.jpg" width="200px" />
</div>

---

### 송금

<div markdown="1">
<img src="./img/service/송금.jpg" width="200px" />
<img src="./img/service/송금금액.jpg" width="200px" />
<img src="./img/service/송금결과.jpg" width="200px" />
<img src="./img/service/송금내역.jpg" width="200px" />
</div>

---

### 기부

<div markdown="1">
<img src="./img/service/직접기부금액.jpg" width="200px" />
<img src="./img/service/직접기부완료.jpg" width="200px" />
<img src="./img/service/직접기부내역.jpg" width="200px" />
<img src="./img/service/자동기부내역.jpg" width="200px" />
<img src="./img/service/영수증2.jpg" width="200px" />
<img src="./img/service/영수증공유.png" width="200px" />
</div>

---

### 결제

<div>
<img src="./img/service/shop.PNG" width="600px" />
<img src="./img/service/shop2.PNG" width="600px" />
<img src="./img/service/shop3.PNG" width="600px" />
</div>

<div markdown="1">
<img src="./img/service/QR스캔.jpg" width="200px" />
<img src="./img/service/결제진행.jpg" width="200px" />
<img src="./img/service/결제결과.jpg" width="200px" />
</div>

---

## 🛠 기술 스택

<div align=center>
<!-- 백엔드 -->
<img src="https://img.shields.io/badge/-Java-007396?style=flat-square&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/-SpringBoot-6DB33F?style=flat-square&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/-JPA-FFCA28?style=flat-square&logo=jpa&logoColor=white">
<img src="https://img.shields.io/badge/-Kafka-231F20?style=flat-square&logo=Apache Kafka&logoColor=white">
<img src="https://img.shields.io/badge/-Elasticsearch-005571?style=flat-square&logo=Elasticsearch&logoColor=white"> <br/>

<!-- 데이터베이스 -->
<img src="https://img.shields.io/badge/-MariaDB-003545?style=flat-square&logo=MariaDB&logoColor=white">
<img src="https://img.shields.io/badge/-Redis-DC382D?style=flat-square&logo=redis&logoColor=white">
<img src="https://img.shields.io/badge/-MongoDB-47A248?style=flat-square&logo=mongodb&logoColor=white"> <br/>

<!-- 프론트엔드 -->
<img src="https://img.shields.io/badge/-TypeScript-3178C6?style=flat-square&logo=typescript&logoColor=white">
<img src="https://img.shields.io/badge/-React-61DAFB?style=flat-square&logo=React&logoColor=white">
<img src="https://img.shields.io/badge/-Next.js-000000?style=flat-square&logo=next.js&logoColor=white">
<img src="https://img.shields.io/badge/-React Query-FF4154?style=flat-square&logo=React Query&logoColor=white">
<img src="https://img.shields.io/badge/-Next.js-000000?style=flat-square&logo=next.js&logoColor=white">
<img src="https://img.shields.io/badge/-PWA-5A0FC8?style=flat-square&logo=pwa&logoColor=white"> <br/>

<!-- 인프라 -->
<img src="https://img.shields.io/badge/-EC2-FF9900?style=flat-square&logo=AmazonEC2&logoColor=white">
<img src="https://img.shields.io/badge/-Docker-2496ED?style=flat-square&logo=docker&logoColor=white">
<img src="https://img.shields.io/badge/-Jenkins-D24939?style=flat-square&logo=jenkins&logoColor=white">
<img src="https://img.shields.io/badge/-nginx-009639?style=flat-square&logo=nginx&logoColor=white">
</div>

<br/>

## 📝 설계 문서

### 🏗️ 아키텍쳐

<img alt="Architecture" src="./img/architecture.png" />

### 🏗️ ERD

<img alt="ERD" src="./img/ieumpay.png" />

### API 명세서

[API 명세 노션 링크](https://www.notion.so/ieum-pay/API-a95a0fc62f714a6c808d767088e480e4)

---

## 📚 컨벤션

### Git Commit

<details>
  <summary>클릭하여 내용 표시/숨기기</summary>
    
> COMMIT CONVENTION
>

- **Commit 메세지 구조**
  - ex) ✨ feat : Add sign in page #S09P11A308-52

</details>

## Git flow

- ex) **feature/{이슈 요약}**

- **master** / **main** - 제품으로 출시 및 배포가 가능한 상태인 브랜치 → 최종 결과물 제출 용도
- **develop** - 다음 출시 버전을 개발하는 브랜치 → 기능 완성 후 중간에 취합하는 용도
- **feature** - 각종 기능을 개발하는 브랜치 → feat/login, feat/join 등으로 기능 분류 후 작업
- **hotfix** - 출시 버전에서 발생한 버그를 수정하는 브랜치

</details>

### Codding

[FRONT CODING CONVENTION](https://www.notion.so/ieum-pay/FE-38af95dd0b6d47aa8ad3182d3b45fdcd)
<br/>

[BACK CODING CONVENTION](https://www.notion.so/ieum-pay/Restful-API-79fd07f7b4e44da8a9246045757e7727)

## 💻 구동 방법

[포팅메뉴얼 - 빌드 및 배포](./exec/빌드%20및%20배포%20정리.pdf) <br/>
[포팅매뉴얼 - 외부 서비스](./exec/외부%20서비스%20정리.pdf)
