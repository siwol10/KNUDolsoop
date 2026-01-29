# Dolsoop 

2025.03~2025.06(약 10주)

보육 서비스 통합 플랫폼 ‘돌숲’은 **양육 부담 및 돌보미 부족, 시간제 서비스 수요의 증가**를 해결하고자 합니다.
## 주요 기능
1. **웹 기반 예약 서비스**를 통해 부모와 돌보미 간 매칭을 지원합니다.
2. **통합 보육 정보를 제공**하여 보호자의 보육 서비스에 대한 접근성을 늘립니다.
3. **신속한 매칭 시스템**을 구축해 긴급 돌보미 호출을 지원합니다.

## 팀원 소개
- 송은경(팀장,BE,FE) - 돌봄 신청서/이력서 개발, 개발 환경 설정, 아키텍쳐 및 레이아웃 설계, 서버 관리 및 배포, 메인/레이아웃 작업


- 이혜수(BE,FE) - 웹 디자인, 로그인/회원가입과 보호자/돌보미 게시판 개발


- 정인서(BE,FE) - 웹 디자인, 마이페이지 및 긴급돌봄서비스 개발, DB 설계

## 개발 환경
- Language : Java(17)

 - Backend Framework : Spring Boot 3.4.5
 
 - Database : MySQL (AWS EC2 instance)
 
 - ORM : MyBatis 
 
 - Cloud Hosting & Deployment: : AWS EC2
 
 - Frontend : Thymeleaf , HTML/CSS , JavaScript
 
 - Cloud Storage: AWS S3 (이미지 파일 저장)
 
 - SMS 전송 API : Solapi (긴급 돌봄 호출 시 실시간 문자 전송)

 ## 사용자 별 기능
 - 사용자 구분 : 돌보미 / 보호자
 - 공통 기능
    - 회원가입 및 로그인, 회원 정보 수정
    - 보육 정보 둘러보기
    - 예약 신청/취소/수락/거절
- 보호자 회원 기능
    - 돌봄 신청서 작성/수정/삭제
    - 긴급 돌봄 신청서 작성/삭제
    - 돌보미에 대한 후기 작성
- 돌보미 회원 기능
    - 이력서 작성/수정/삭제
    - 보호자에 대한 후기 작성
 
## 구현 결과
### 게시판 - <돌봄 요청 공고 확인>
<img width="781" height="318" alt="image" src="https://github.com/user-attachments/assets/3f0aa8cd-d839-41a6-b8a1-9e73dff5b95f" />



### 예약
#### 돌봄 신청서 작성
<img width="941" height="475" alt="image" src="https://github.com/user-attachments/assets/745a8b5f-75fb-41be-a666-7be0d59386a2" />


#### 예약 확인 화면 - 위: 보호자 회원/ 아래: 돌보미 회원


<img width="637" height="606" alt="image" src="https://github.com/user-attachments/assets/9e144ddb-1a62-49a4-b914-6219fb774243" />



### 알림장
#### 알림장 작성
<img width="862" height="324" alt="image" src="https://github.com/user-attachments/assets/8cc928c4-dbdc-4269-8848-483b7f1471da" />


#### 알림장 확인

<img width="636" height="473" alt="image" src="https://github.com/user-attachments/assets/e9b64e73-85a9-45f7-b6ad-25ba79149694" />





### 긴급 돌봄
#### 긴급 돌봄 게시판 - 지역필터링
<img width="827" height="346" alt="image" src="https://github.com/user-attachments/assets/107f8597-a314-4f4b-b2ed-f2230068b936" />


#### 긴급 돌봄 공고 등록 시 동일 지역 돌보미에게 발송되는 메시지

<img width="669" height="402" alt="image" src="https://github.com/user-attachments/assets/a3cf34de-86aa-49e5-bcc4-1c1fb3591a44" />





