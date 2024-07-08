# DATEROAD-SERVER
탄탄데로 Server Repository🩷

![image](https://github.com/TeamDATEROAD/DATEROAD-SERVER/assets/40743105/ee012011-5718-46d1-abad-1ddaa169dfc1)

<br>

## 💘 Date Road
<aside>
장소 중심이 아닌 코스 중심의 데이트 추천 서비스 Date Road 입니다~!<br/>
유저가 직접 공유한 생생한 데이트 코스를 통해 특별한 데이트를 계획할 수 있어요

</aside>

<br/><br/>

## Contributors ✨

<div align=center>
  
| [@gardening-y](https://github.com/gardening-y) | [@sjk4618](https://github.com/sjk4618) | [@rlarlgnszx](https://github.com/rlarlgnszx) | 
| :---: | :---: | :---: |
|<img width="300" src="https://github.com/TeamDATEROAD/DATEROAD-SERVER/assets/102401928/b2e98dff-0e5f-49a7-b9bb-7cdca755dd64">|<img width="300" src="https://github.com/TeamDATEROAD/DATEROAD-SERVER/assets/102401928/2360f94a-bf0e-4b5e-b008-f34a6ab7bc77">|<img width="300" src="https://github.com/TeamDATEROAD/DATEROAD-SERVER/assets/102401928/054941bd-3132-4051-bac4-c2c9a04a366a">|
  
| | [@gardening-y](https://github.com/gardening-y) | [@sjk4618](https://github.com/sjk4618) | [@rlarlgnszx](https://github.com/rlarlgnszx) | 
|-------|--------------|----------|-------------|
| 역할 | 팀 리더 | 백엔드 개발 | 백엔드 개발 |
| 담당 API | <li>데이트 코스 좋아요 해제 API</li><li>데이트 코스 좋아요 등록 API</li><li>유저 등록 데이트 코스 조회 API (내가 등록한 코스)</li><li>데이트 일정 등록 API</li><li>데이트 일정 삭제 API</li><li>지난 & 다가올 데이트 일정 전체 조회 API</li><li>데이트 일정 상세 조회 API</li><li>데이트 코스 필터링 조회 API</li> | <li>메인 다가올 일정(1개) 조회 API</li><li>JWT 재발급 API</li><li>유저 프로필 조회 API (마이페이지)</li><li>유저 프로필 수정 API</li><li>유저 포인트 조회 API (메인)</li><li>탈퇴하기 API</li><li>로그아웃 API</li><li>로그인 API</li><li>회원가입 API</li><li>닉네임 중복확인 API</li> | <li>포인트 사용 [유저 데이트 코스 열람] API</li><li>데이트 코스 삭제 API</li><li>데이트 코스 상세 조회 API</li><li>데이트 코스 등록 API</li><li>데이트 코스 전체 조회 API</li><li>열람 데이트 코스 전체 조회 API</li><li>포인트 내역 조회 API</li><li>광고 상세 조회 API</li><li>광고 전체 조회 API</li> |

</div>
<hr></hr>

### API 명세서
[API 명세서](https://hooooooni.notion.site/API-872d0ad3c313452a8452997b0a193015)
<br>

<hr></hr>

## 폴더 구조

```
├── dateroad-api
│   └── src
│       └── main
│           └── java
│               └── org
│                   └── dateroad
│                       ├── auth
│                       │   ├── argumentresolve
│                       │   ├── config
│                       │   ├── exception
│                       │   ├── filter
│                       │   └── jwt
│                       ├── common
│                       ├── config
│                       └── domain
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
│                   └── dateroad
│                       ├── code
│                       ├── common
│                       └── exception
├── dateroad-domain
│   └── src
│       └── main
│           └── java
│               └── org
│                   └── dateroad
│                       ├── advertisement
│                       │   └── domain
│                       ├── common
│                       ├── config
│                       ├── date
│                       │   ├── domain
│                       │   └── repository
│                       ├── dataAccess
│                       │   ├── domain
│                       │   └── repository
│                       ├── image
│                       │   ├── domain
│                       │   └── repository
│                       ├── like
│                       │   ├── domain
│                       │   └── repository
│                       ├── place
│                       │   ├── domain
│                       │   └── repository
│                       ├── point
│                       │   ├── domain
│                       │   └── repository
│                       ├── tag
│                       │   ├── domain
│                       │   └── repository
│                       ├── user
│                           ├── domain
│                           └── repository
├── dateroad-external
│   └── src
│       └── main
│           └── java
│               └── org
│                   └── dateroad
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
- merge: 머지할때

## Git Convention 📋
[데이트로드 서버 팀의 깃 컨벤션이 궁금하다면? ✔️](https://hooooooni.notion.site/Git-Convention-d8f7892977924a76a0fcad3019d3f692?pvs=4)

<br>

## Code Convention 📋
[데이트로드 서버 팀의 코드 컨벤션이 궁금하다면? ✔️](https://hooooooni.notion.site/Code-Convention-25494a4fa90d4875a277dd51e27e51d6?pvs=4)

<br>
