# 푸드트럭 정보 공유 애플리케이션, EatGo

<!-- 목차 -->
<details>
  <summary>목차</summary>
  <ol>
    <li><a href="#1-서비스-실행">서비스 실행</a></li>
    <li>
        <a href="#2-프로젝트-소개">프로젝트 소개</a>
        <ul>
            <li>1) 프로젝트 주제</li>
            <li>2) 서비스 개요 및 배경</li>
            <li>3) 서비스 목표</li>
        </ul>
    </li>
    <li>
        <a href="#3-서비스-기능-소개">서비스 기능 소개</a>
        <ul>
            <li>1) 메인 기능</li>
            <li>2) 서브 기능</li>
            <li>3) 관련 문서</li>
        </ul>
    </li>
    <li>
        <a href="#4-사용된-데이터셋과-기술스택">사용된 데이터셋과 기술스택</a>
        <ul>
            <li>1) 어떤 데이터셋을 어떻게 전처리하고 사용할것인지</li>
            <li>2) 어떤 방법, 라이브러리나 알고리즘을 사용할것인지</li>
        </ul>
    </li>
    <li>
        <a href="#5-시스템-아키텍쳐">시스템 아키텍쳐</a>
        <ul>
            <li>1) 개발 구조</li>
        </ul>
    </li>
    <li><a href="#6-프로젝트-팀원-소개">프로젝트 팀원 소개</a></li>
  </ol>
</details>

<h2 id="1-서비스-실행">1. 서비스 실행</h2>

* Android (API Level 33 이상)

1. git clone
```
git clone https://github.com/kinopioB/EatGo-Frontend.git
```
2. 실행
Android Studio Project Open -> File -> Sync Project with Gradle Files -> Run app

* Back-End (STS 기준)
1. git clone
```
git clone https://github.com/kinopioB/EatGo-Backend.git
```
2. Server Tomcat v9.0 추가
Servers -> New -> Server -> Apache -> Tomcat v9.0 Server선택 -> Finish
3. Maven Update
Project -> Maven -> Update Project
4. Start the server

<h2 id="2-프로젝트-소개">2. 프로젝트 소개</h2>

---
### 2-1) 프로젝트 주제
“EatGo”는 푸드트럭, 포장마차 음식을 좋아하고 찾는 소비자들에게 정보를 제공해 주는 애플리케이션이다. 

### 2-2) 서비스 개요 및 배경
푸드트럭의 경우 매일 같은 위치에서 영업하지 않는 경우가 대부분이며, 포장마차의 경우 지도에서 실제로 확인하기 어렵다. 그렇기 때문에 소비자들은 영업 위치나 시간을 직접 사장님에게 물어보고 기억하거나 우연히 지나가다 발견하고 이용하게 된다. 또한 각종 동네 커뮤니티에서 푸드트럭, 포장마차 정보에 대한 질의가 있긴 하지만 정확한 정보를 기대하기 어렵다. 이러한 불편을 해소하고 정확한 정보 제공을 위해 직접 사용해 본 사용자 또는 사장님이 가게 위치, 영업시간 등을 직접 입력해 다른 소비자들이 편하게 정보를 제공받을 수 있도록 서비스를 제공한다.

### 2-3) 서비스 목표
푸드트럭 사장님이 직접 정확한 영업 정보를 등록해 소비자에게 정확한 정보를 제공함과 동시에 홍보를 할 수 있도록 도와주며, 소비자는 푸드트럭에 대한 정확한 정보를 쉽게 얻을 수 있다.

* 주 사용자 : 푸드트럭 or 포장마차 사장님, 소비자

<h2 id="3-서비스-기능-소개">3. 서비스 기능 소개</h2>

---

### 3-1) 메인 기능
* 푸드트럭 검색
  * 등록된 가게 전체 조회
  * 특정 필터를 적용한 가게 검색 기능
  * 가게 히스토리 검색
* 가게 등록
  * 사장님이 자신의 가게 정보(영업 위치, 영업 시간, 메뉴)를 직접 등록 가능
  * 소비자가 자신이 이용한 가게의 위치, 영업 시간과 같은 정보를 등록 가능
* 리뷰 등록
  * 사장님이 리뷰를 위한 QR 코드 생성 가능
  * 생성한 QR 코드를 소비자가 스캔해 가게에 대한 리뷰 등록 가능
  * 리뷰 등록시 가게 정보를 등록한 사장님에게 알림

![단어검색](https://github.com/kinopioB/EatGo-Frontend/assets/55628887/96705eb1-a273-4d37-90ce-87bbb4b05b5e)
![가게등록위치설정](https://github.com/kinopioB/EatGo-Frontend/assets/55628887/778cfe4a-e7c5-4394-8cf4-bf6479d88918)


### 3-2) 서브 기능
* 로그인, 회원가입
  * 네이버, 카카오 OAuth를 사용한 로그인 및 회원가입
  * 사장님, 소비자 로그인 구분
* 가게 정보 조회
  * 지도의 마커 클릭시 요약 정보 확인 가능
  * 요약 정보 클릭시 상세 정보로 이동
  * 가게 리뷰 확인
* 가게 영업 관리
  * 사장님 유저가 등록한 가게에 대해 영업 시작, 영업 종료를 통해 영업 상태 관리 가능
  * 영업 시작전 영업 위치 수정 가능

![마커클릭및페이지이동](https://github.com/kinopioB/EatGo-Frontend/assets/55628887/7227832a-1fc2-4a16-9566-a9307c3628a9)
![영업시작위치설정](https://github.com/kinopioB/EatGo-Frontend/assets/55628887/36c7b06c-e954-4093-9a51-1da9b38e5671)

### 3-3) 관련 문서
- 와이어프레임
  [EatGo 피그마](https://www.figma.com/file/a6cFVUaw3ZbNe8NuT67fr8/EatGo?type=design&node-id=4-2&mode=design&t=57DrC0J2zENJNhtd-0)

<h2 id="4-사용된-데이터셋과-기술스택">4. 사용된 데이터셋과 기술스택</h2>

### 4-1) ERD
![erd](https://github.com/kinopioB/EatGo-Frontend/assets/55628887/2a3830b1-8d15-4670-a6ac-6ec69086f297)

### 4-2) 기술 스택

| 파트                         | 기술                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
| ---------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Team** :metal:             | ![image](https://img.shields.io/badge/GitLab-330F63?style=for-the-badge&logo=gitlab&logoColor=white) ![image](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white) ![image](https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=discord&logoColor=white) ![image](https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white)                                                                                                                                                                                                                                                                                                                                                         |
| **Android** :ok_hand:             | ![image](https://img.shields.io/badge/kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white) ![image](https://img.shields.io/badge/android-3DDC84?style=for-the-badge&logo=android&logoColor=white) ![image](https://img.shields.io/badge/firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=white) |
| **BE** :raised_back_of_hand: | ![image](https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white) ![image](https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white) ![image](https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)|
| **DB** :raised_back_of_hand: | ![image](https://img.shields.io/badge/oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white)|

<h2 id="5-시스템-아키텍쳐">5. 시스템 아키텍쳐</h2>

### 5-1) 개발 구조
![image](https://github.com/kinopioB/EatGo-Frontend/assets/55628887/8a75e92a-a62f-486c-b893-b3a2327d1727)

<h2 id="6-프로젝트-팀원-소개">6. 프로젝트 팀원 소개</h2>

| 이름 | 구현 화면 | 개발 기능 |
| ---- | --------- | -------- |
| 양석진 | 1. QR 스캐너 <br> 2. QR 코드 생성 페이지 <br> 3. 마이 페이지 <br> 4. Dialog| 1. QR 코드 촬영 <br> 2. QR 코드 생성(가게 정보, 리뷰 등록) <br> 3. 리뷰 등록 <br> 4. 영업 상태 변경 <br> 5. 영업 히스토리 등록(영업 시작시 위치 저장) <br> 6. 푸드 트럭 관리 <br> 7. 리뷰 등록 알림(Firebase 사용, 리뷰 등록시 가게 사장님에게 알림)|
| 김승주 | 1. 앱 시작 페이지(splash screen) <br> 2. 가게 등록 페이지 <br> 3. 푸드 트럭 상세 페이지 <br> 4. 가게 메뉴, 정보, 리뷰 페이지 <br> 5. 푸드 트럭 목록 페이지 <br> 6. 로딩바 | 1. 푸드 트럭 정보 등록 기능 <br> 2. 푸드 트럭 상세 정보, 목록 보기 기능 <br> 3. 가메 메뉴, 정보, 리뷰 관리 기능 <br> 4. 리뷰 등록 알림 기능(Firebase Cloud Messaging, 리뷰 등록 시 사장님에게 알림) <br> 5. 이미지 업로드(Firebase Storage, 메뉴, 가게 사진 등록) <br> 6. 이미지 불러오기(Firebase, 메뉴 가게 사진 불러오기) <br> 7. 전반적인 백엔드 API, SQL 구현 및 데이터베이스 설계, 관리 |
| 박보선 | 1. 로그인 페이지 <br> 2. 메인 페이지 <br> 3. 네비게이션바 <br> 4. 요약 정보 | 1. 로그인, 회원가입(OAuth : Naver, Kakao) <br> 2. 지도 및 카테고리별, 영업 상태별 마커 그리기(naver map api) <br> 3. 요약 정보 불러오기 <br> 4. 특정 단어 또는 카테고리 검색 기능 <br> 5. 지도 이벤트 커스텀 (scrollview, viewpager와 지도의 이벤트 충돌 해결) <br> 6. 현재 위치에서 가게까지 거리 계산(google map api) <br> 7. (위도, 경도) -> 주소 변환, 주소 -> (위도, 경도) 변환 |
