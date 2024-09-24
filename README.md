# DATEROAD-SERVER
탄탄데로 Server Repository🩷

<br>

## 💘 Date Road
![dateroad](https://github.com/user-attachments/assets/19abcdff-47ee-41f8-b2ee-d345884ff83a)

<aside>
데이트로드는 ‘장소 중심’이 아닌 ‘코스 중심’ 데이트 공유 서비스로 사용자가 직접 데이트 코스를 등록하고 공유합니다.
</aside>
<br/>

<details>
  <summary><h2>주요 기능</h2></summary>
  <h3>1️⃣ 코스 등록하기 및 열람</h3>
  <img src="https://github.com/user-attachments/assets/00dd114b-81bd-4431-8162-b5a796537176" alt="기능 설명1" width="49%">
  <img src="https://github.com/user-attachments/assets/0a36c960-a846-48a2-ba52-f3a5bda0a03f" alt="기능 설명2" width="49%">

  - 내가 한 데이트 코스를 등록하고 포인트를 획득할 수 있습니다.
  - 다른 커플들이 한 데이트를 포인트를 사용해 열람할 수 있습니다.
  - 코스 상세 페이지에서 ‘내 일정에 추가하기’ 버튼을 눌러 내 데이트 일정으로 불러올 수 있습니다.

  <h3>2️⃣ 일정 등록하기 및 열람</h3>
  <img src="https://github.com/user-attachments/assets/486822c4-6b02-4778-b9e4-70af58f355be" alt="기능 설명3" width="49%">
  <img src="https://github.com/user-attachments/assets/25ef0708-e718-4859-9c7f-e184fe375a42" alt="기능 설명4" width="49%">

  - 내 데이트 일정을 등록할 수 있습니다.
  - 내 데이트 일정을 확인할 수 있습니다.
  - 지난 데이트는 코스 등록하기로 연동해 등록하고 포인트를 받을 수 있습니다.
  - 카카오톡 공유하기를 통해 데이트 일정을 연인에게 공유할 수 있습니다.
</details>
<br/>

## Contributors ✨

<div align=center>
  
| [@gardening-y](https://github.com/gardening-y) | [@sjk4618](https://github.com/sjk4618) | [@rlarlgnszx](https://github.com/rlarlgnszx) | 
| :---: | :---: | :---: |
|<img width="300" src="https://github.com/user-attachments/assets/353cfd1a-045f-4ff2-8c4e-03ba3c0982bf">|<img width="300" src="https://github.com/user-attachments/assets/e183a62a-13fd-4455-af0e-76bd90d517ca">|<img width="300" src="https://github.com/user-attachments/assets/5424e84a-6a4b-4e72-856c-61fcfcea91d3">|
  
| | [@gardening-y](https://github.com/gardening-y) | [@sjk4618](https://github.com/sjk4618) | [@rlarlgnszx](https://github.com/rlarlgnszx) | 
|-------|--------------|----------|-------------|
| 역할 | 팀 리더 | 백엔드 개발 | 백엔드 개발 |
| 담당 API | <li>데이트 코스 좋아요 해제 API</li><li>데이트 코스 좋아요 등록 API</li><li>유저 등록 데이트 코스 조회 API (내가 등록한 코스)</li><li>데이트 일정 등록 API</li><li>데이트 일정 삭제 API</li><li>지난 & 다가올 데이트 일정 전체 조회 API</li><li>데이트 일정 상세 조회 API</li><li>데이트 코스 필터링 조회 API</li> | <li>메인 다가올 일정(1개) 조회 API</li><li>JWT 재발급 API</li><li>유저 프로필 조회 API (마이페이지)</li><li>유저 프로필 수정 API</li><li>유저 포인트 조회 API (메인)</li><li>탈퇴하기 API</li><li>로그아웃 API</li><li>로그인 API</li><li>회원가입 API</li><li>닉네임 중복확인 API</li> | <li>포인트 사용 [유저 데이트 코스 열람] API</li><li>데이트 코스 삭제 API</li><li>데이트 코스 상세 조회 API</li><li>데이트 코스 등록 API</li><li>데이트 코스 전체 조회 API</li><li>열람 데이트 코스 전체 조회 API</li><li>포인트 내역 조회 API</li><li>광고 상세 조회 API</li><li>광고 전체 조회 API</li> |

</div>
<hr></hr>

## API 명세서
[API 명세서](https://hooooooni.notion.site/API-872d0ad3c313452a8452997b0a193015)
<br>

<hr></hr>

## Architecture
![DateRoad_Server_Arch](https://github.com/user-attachments/assets/2e040d8f-bc44-4c24-ad17-8f4bccb9af4c)

<br>

<hr></hr>

## 📁 폴더 구조

```
├── 📁 dateroad-api
│   └── src
│       └── main
│           └── java
│               └── org
│                   └── 📁 dateroad
│                       ├── 📁 auth
│                       │   ├── argumentresolve
│                       │   ├── config
│                       │   ├── exception
│                       │   ├── filter
│                       │   └── jwt
│                       ├── 📁 common
│                       ├── 📁 config
│                       └── 📁 domain
│                           ├── api
│                           ├── dto
│                           │   ├── request
│                           │   └── response
│                           └── service
├── dateroad-common
│   └── src
│       └── main
│           └── java
│               └── org
│                   └── 📁 dateroad
│                       ├── 📁 code
│                       ├── 📁 common
│                       └── 📁 exception
├── dateroad-domain
│   └── src
│       └── main
│           └── java
│               └── org
│                   └── 📁 dateroad
│                       ├── 📁 advertisement
│                       │   └── domain
│                       ├── 📁 common
│                       ├── 📁 config
│                       ├── 📁 date
│                       │   ├── domain
│                       │   └── repository
│                       ├── 📁 dataAccess
│                       │   ├── domain
│                       │   └── repository
│                       ├── 📁 image
│                       │   ├── domain
│                       │   └── repository
│                       ├── 📁 like
│                       │   ├── domain
│                       │   └── repository
│                       ├── 📁 place
│                       │   ├── domain
│                       │   └── repository
│                       ├── 📁 point
│                       │   ├── domain
│                       │   └── repository
│                       ├── 📁 tag
│                       │   ├── domain
│                       │   └── repository
│                       ├── 📁 user
│                           ├── domain
│                           └── repository
├── dateroad-external
│   └── src
│       └── main
│           └── java
│               └── org
│                   └── 📁 dateroad
│                       ├── feign
│                       │   ├── apple
│                       │   ├── config
│                       │   └── kakao
│                       └── s3
└── gradle
    └── wrapper
```

<hr></hr>

## ERD
![DATEROAD (1)](https://github.com/TeamDATEROAD/DATEROAD-SERVER/assets/40743105/9431c82d-4685-47ec-a49a-f2376c119426)

[ERD](https://www.erdcloud.com/d/c4ym2bKefwc4fG9gh)

<hr></hr>

## Branch Convention

- `main` : 프로덕트를 배포하는 브랜치입니다.
- `develop`: 프로덕트 배포 전 기능을 개발하는 브랜치입니다.
- `feature`: 단위 기능을 개발하는 브랜치로 단위 기능 개발이 완료되면 develop 브랜치에 merge 합니다.
- `hotfix`: main 브랜치로 프로덕트가 배포 된 이후 이슈가 발생했을 때 이를 긴급하게 해결하는 브랜치입니다.

<hr></hr>

## Commit Convetion
- **feat** : 새로운 기능 구현 `feat: 구글 로그인 API 기능 구현 - #11`
- **fix** : 코드 오류 수정 `fix: 회원가입 비즈니스 로직 오류 수정 (#10)`
- **del** : 불필요한 코드 삭제 `del: 불필요한 import 제거 (#12)`
- **docs** : README나 wiki 등의 문서 개정 `docs: 리드미 수정 (#14)`
- **refactor** : 내부 로직은 변경 하지 않고 기존의 코드를 개선하는 리팩터링 `refactor: 코드 로직 개선 (#15)`
- **chore** : 의존성 추가, yml 추가와 수정, 패키지 구조 변경, 파일 이동 등의 작업 `chore: yml 수정 (#21)`, `chore: lombok 의존성 추가 (#22)`
- **test**: 테스트 코드 작성, 수정 `test: 로그인 API 테스트 코드 작성 (#20)`
- **setting**: 세팅
- **merge**: 머지

<br>

<hr></hr>


## Git Convention 📋
[데이트로드 서버 팀의 깃 컨벤션이 궁금하다면? ✔️](https://hooooooni.notion.site/Git-Convention-d8f7892977924a76a0fcad3019d3f692?pvs=4)

<br>
<br>
<hr></hr>

## Code Convention 📋
[데이트로드 서버 팀의 코드 컨벤션이 궁금하다면? ✔️](https://hooooooni.notion.site/Code-Convention-25494a4fa90d4875a277dd51e27e51d6?pvs=4)

<br>
<hr></hr>

<br>

## Teck Stack ✨

| IDE | IntelliJ |
|:---|:---|
| Language | Java 21 |
| Framework | Spring Boot 3.3.1, Gradle |
| Authentication | Spring Security, JSON Web Tokens |
| Orm | Spring Data JPA |
| Database | PostgreSQL |
| External | AWS EC2, AWS RDS, Nginx, Docker, Docker-Compose, Redis |
| CI/CD | Github Action |
| API Docs | Notion, Swagger |
| Other Tool | Discord, Postman, Figma |

<br>


## 서버데로 모음zip💜
<p align="center">  
  <img src="https://github.com/user-attachments/assets/46db3c82-dd8a-472e-ad95-bc2fcb1e2f0d" align="center" width="49%"> 
  <img src="https://github.com/user-attachments/assets/0fd6fb91-830f-4fac-b734-a64fc1581a1d" align="center" width="49%"> 
</p>
<p align="center">  
  <img src="https://github.com/user-attachments/assets/173ee2ac-97af-441c-8656-dc7acaf2b646" align="center" width="32%"> 
  <img src="https://github.com/user-attachments/assets/045a9b9c-86f6-4885-bf84-db505913d882" align="center" width="32%">
  <img src="https://github.com/user-attachments/assets/2deb0d97-87ff-4227-a386-f9307646c9d9" align="center" width="32%"> 
</p>



