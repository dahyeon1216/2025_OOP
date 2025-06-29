# 🎁 Java Swing 기반 기부 플랫폼
2025년 1학기 객체지향프로그래밍 캡스톤 팀 프로젝트 입니다.

사용자가 기부글을 작성하고, 포인트로 기부하며, 기부 내역을 확인할 수 있는 데스크탑 애플리케이션입니다.  
기부글 정렬(Up하기 기능), 마이페이지, 포인트 관리, 스크랩 등 다양한 기능을 제공합니다.

---

## 🎬 기능 미리보기
| 사용자 로그인/회원가입 | 포인트 충전  | 스크랩 기능 |
| :----------------------------------------------------------------------------------: | :----------------------------------------------------------------------------------: | :----------------------------------------------------------------------------------: |
| <img width="200" alt="image" src="https://github.com/user-attachments/assets/6ff10776-b31f-44b8-b114-3ebc3c4d56cf"> | <img width="200" alt="image" src="https://github.com/user-attachments/assets/3126e0f8-8474-441c-88e5-d9a3affa9b84"> | <img width="200" alt="image" src="https://github.com/user-attachments/assets/17f747a4-bf9d-47b2-a204-97b059166b31"> |

| 기부글 작성(진행 중) | 기부글 작성(진행완료)  | 기부글 수정/삭제 |
| :----------------------------------------------------------------------------------: | :----------------------------------------------------------------------------------: | :----------------------------------------------------------------------------------: |
| <img width="200" alt="image" src="https://github.com/user-attachments/assets/6c2cba0b-b4a4-4c2d-9c3a-9a63486b3911"> | <img width="200" alt="image" src="https://github.com/user-attachments/assets/68ed76fd-f015-44c7-9e8a-79863e827e22"> | <img width="200" alt="image" src="https://github.com/user-attachments/assets/87805d4c-42a3-4a48-9e86-a1f4ab5b6e52"> |

| 기부글 기부하기| 기부글 UP하기   | 기부글 정산하기 |
| :----------------------------------------------------------------------------------: | :----------------------------------------------------------------------------------: | :----------------------------------------------------------------------------------: |
| <img width="200" alt="image" src="https://github.com/user-attachments/assets/5d2c2d83-ef7a-4242-85e6-0fb49c7f3c52"> | <img width="200" alt="image" src="https://github.com/user-attachments/assets/f7bc4088-8f9a-4ab3-bf77-25a76341b8e6"> | <img width="200" alt="image" src="https://github.com/user-attachments/assets/dc23c8ab-fe71-48e7-9d73-5fe686a46c68"> |

> 💡 위 움짤은 실제 실행 화면을 기반으로 하며 핵심 기능 흐름을 보여줍니다.
<br>

## 📖 기능 상세 설명

### 1. 사용자 기능
| 기능         | 설명                                              |
|------------|-------------------------------------------------|
| 회원가입       | 프로필 사진, 사용자 ID, 비밀번호, 이름, 닉네임, 계좌번호를 입력하여 계정 생성 |
| 로그인 / 로그아웃 | 로그인 성공 시 사용자 정보를 세션처럼 유지하여 다양한 기능 사용 가능         |
| 마이페이지      | 자신의 프로필 조회 및 수정 가능 (이름, 포인트 등)                  |
| 포인트 충전     | 기본 포인트 단위로 충전 가능하며 기부 시 차감                      |
| 티어 시스템     | 누적 기부 포인트에 따라 사용자 티어 및 문구 표시                    |


### 2. 기부글 기능
| 기능           | 설명                                          |
| ------------ |---------------------------------------------|
| 기부글 작성       | 제목, 이미지, 목표 모금 포인트, 마감일, 내용 입력 가능           |
| 기부글 목록 조회    | 진행중 / 완료 상태에 따라 기부글 자동 분리                   |
| 기부글 상세보기     | 제목, 작성자, 본문, 남은 기간, 현재 모금 포인트, 상태, 이미지 등 표시 |
| 본인 글 수정 / 삭제 | 로그인한 사용자가 작성한 글만 수정 및 삭제 가능                 |
| 이미지 업로드       | 이미지 파일명을 입력하면 local 저장소에서 불러와 출력 (크기 고정)    |


### 3. Up 기능
| 기능        | 설명                                                         |
| --------- | ---------------------------------------------------------- |
| 기부글 상단 고정 | 포인트 300 사용 시 해당 기부글의 `upFuncAt` 값을 현재 시간으로 변경하여 목록 상단으로 노출 |
| 업 사용 조건   | 사용자는 보유 포인트가 300 이상일 경우에만 사용 가능                            |
| 자동 정렬     | 전체 기부글은 `upFuncAt` 기준으로 내림차순 정렬되어 표시됨                      |


### 4. 포인트 기부 기능
| 기능     | 설명                            |
| ------ |-------------------------------|
| 기부 참여  | 원하는 기부글에 원하는 포인트만큼 기부 가능      |
| 기부 제한  | 보유 포인트보다 많은 포인트를 입력하면 불가      |
| 실시간 반영 | 기부가 완료되면 기부글의 현재 모금 포인트가 즉시 갱신됨 |
| 기부 마감  | 마감일이 지나면 자동으로 `완료` 상태로 전환      |


### 5. 기부 내역 조회
| 기능       | 설명                            |
| -------- |-------------------------------|
| 나의 기부 내역 | 로그인한 사용자가 기부한 내역 리스트 확인 가능    |
| 상세 연결    | `상세보기` 버튼 클릭 시 해당 기부글 상세보기로 이동  |
| 표시 정보    | 기부한 기부글 제목, 본문 요약, 기부한 포인트 표시 |


### 6. 스크랩 기능
| 기능     | 설명                                |
| ------ |-----------------------------------|
| 스크랩 추가 | 기부글 리스트에서 스크랩 버튼 클릭 시 스크랩 기부글로 등록 |
| 스크랩 해제 | 스크랩된 글에서 같은 버튼 클릭 시 스크랩 해제        |
| 스크랩 목록 | 나의 스크랩 보기 메뉴에서 스크랩한 글 전체 확인 가능    |


### 7. 정산 및 사용내역 관리
| 기능       | 설명                               |
| -------- |----------------------------------|
| 정산하기     | 기부글이 완료되면 `정산하기` 버튼 활성화           |
| 사용 내역 추가 | 정산 후 사용 내역 입력 폼이 활성화되며 작성자만 입력 가능 |
| 기록 항목    | 사용 포인트, 사용 내역 입력 가능              |
| 사용 내역 보호 | 비작성자는 입력폼 및 버튼이 보이지 않음 (읽기 전용)   |

---

## 🛠️ 기술 스택

| 구성 요소 | 기술 |
|-----------|------|
| 언어 | Java 17 |
| GUI | Swing |
| 디자인 패턴 | MVC 패턴 기반 |
| 빌드 도구 | Gradle |
| 데이터 저장 | Java Collection 기반 |

<br>

## 📂 프로젝트 구조
```
oop/                                
├── fonts/                          # 외부 폰트 사용
│   └── font1.ttf                   
├── icons/                          # 아이콘 디렉토리
├── resources/                      # 정적 리소스 디렉토리
│   └── images/                     # 이미지 리소스
│       ├── profile/                # 사용자 업로드 프로필 이미지
│       │   └── default-profile.png # 기본 프로필 이미지
│       └── donation/               # 기부글 업로드 이미지 (썸네일)
└── src/                            
    └── capstone/                   # 주요 패키지
        ├── controller/             
        │   ├── DonationPostController.java    # 기부글 등록, 수정, 정산 처리
        │   ├── ScrapController.java           # 스크랩 추가/삭제 처리
        │   └── UserController.java            # 로그인, 회원가입, 사용자 관리
        │
        ├── dto/
        │   └── DonatedPostInfo.java           # 기부 내역 정보 DTO
        │
        ├── model/                
        │   ├── BankType.java                  # 은행 타입 Enum (가상 계좌용)
        │   ├── DonationPost.java              # 기부글 데이터 구조
        │   ├── DonationRecord.java            # 기부 내역 객체
        │   ├── ScrapRecord.java               # 스크랩 내역 객체
        │   ├── Tier.java                      # 사용자 티어 Enum
        │   ├── User.java                      # 사용자 계정 정보 객체
        │   └── VirtualAccount.java            # 사용자 가상 계좌 정보
        │
        ├── service/  
        │   ├── DonationPostService.java       # 기부글 등록/수정/정산 서비스
        │   ├── ScrapService.java              # 스크랩 관련 로직 처리
        │   └── UserService.java               # 로그인/회원가입/충전/티어 갱신
        │
        ├── view/                   # 사용자 인터페이스 (Swing UI)
        │   ├── donation/                          # 기부글 관련 화면
        │   │   ├── DonateActionView.java            # 기부 버튼 실행 창
        │   │   ├── DonateActonCompleteView.java     # 기부 완료 알림 창
        │   │   ├── DonationPostCompleteView.java    # 완료된 기부글 보기
        │   │   ├── DonationPostDetailView.java      # 기부글 상세보기
        │   │   ├── DonationPostEditView.java        # 기부글 수정 화면
        │   │   ├── DonationPostListView.java        # 전체 기부글 목록
        │   │   ├── DonationPostUpView.java          # 기부글 업 처리 창
        │   │   └── DonationPostWriteView.java       # 기부글 작성 화면
        │
        │   ├── receipt/                           # 기부금 정산 관련 화면
        │   │   ├── ReceiptListView.java             # 정산 내역 목록
        │   │   └── ReceiptWriteView.java            # 정산 등록/작성 화면
        │
        │   ├── scrap/                             # 스크랩 관련 화면
        │   │   └── ScrappedPostListView.java        # 스크랩한 기부글 목록
        │
        │   ├── style/                             # 공통 UI 컴포넌트 스타일링
        │   │   ├── RoundedBorder.java               # 둥근 테두리 스타일
        │   │   └── RoundedButton.java               # 둥근 버튼 스타일
        │
        │   ├── user/                              # 사용자 계정 관련 화면
        │   │   ├── LoginView.java                   # 로그인 화면
        │   │   ├── MyDonationHistoryView.java       # 내가 기부한 내역 보기
        │   │   ├── MyDonationPostListView.java      # 내가 작성한 글 목록
        │   │   ├── MyPageMainView.java              # 마이페이지 홈 화면
        │   │   ├── MyProfileEditView.java           # 프로필 수정 화면
        │   │   ├── PointChargeView.java             # 포인트 충전 화면
        │   │   └── SignupView.java                  # 회원가입 화면
        │
        │   ├── BaseView.java                      # 공통 View 추상 클래스
        └── App.java                           # 프로그램 실행 진입점 (main 포함)

```

## 🙌 함께한 팀원

| 박소윤 | 백다현 | 김예린 |
| :---------:|:----------:| :----------:|
|<img width="300" alt="image" src="https://avatars.githubusercontent.com/u/102134838?v=4"> | <img width="300" alt="image" src="https://avatars.githubusercontent.com/u/110024521?v=4"> | <img width="300" alt="image" src="https://avatars.githubusercontent.com/u/165559356?v=4"> | 
| [happine2s](https://github.com/happine2s) | [dahyeon1216](https://github.com/dahyeon1216) | [yerinni](https://github.com/yerinni) |
