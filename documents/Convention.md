# Convention

## Version

| Project       | Version   | Description |
| ------------- | --------- | ----------- |
| Java          | 17        |             |
| SpringBoot    | 3.2.1     |             |
| Gradle        | 8.5       |             |
|               |           |             |
|               |           |             |
| React Native  |           |             |
| Node.js       | 20.11.0   |             |
|               |           |             |
|               |           |             |
| MySQL         | 8.0.35    |             |
| Redis         | latest    |             |
|               |           |             |

<br />

## Git
- GitLab

<br />

## Branch

### Branch Naming
- `master` : 배포활 완성 프로젝트 브랜치

- `develop` : 개발 완료한 기능(feature)을 통합하는 브랜치

- `feature/fe/~` : 기능 단위로 개발을 진행하는 브랜치

- `document` : 개발 외적인 것에 대한 브랜치

<br />

## Commit

### Commit Structure
```
[commit type]: [commit message]
ex) Docs: README 수정
```

### Commit Type
- `Init:` : 프로젝트 초기 생성

- `Feat:` : 새로운 기능 추가

- `Fix:` : 버그 수정 또는 typo

- `Design:` : CSS 등 사용자 UI 디자인 변경

- `Comment:` : 필요한 주석 추가 및 변경

- `Refactor:` : 리팩토링

- `Rename: ` : 파일 혹은 폴더명 수정하거나 이동

- `Remove:` : 파일 삭제

- `Style:` : 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우

- `Test:` : 테스트(테스트 코드 추가, 수정, 삭제, 비즈니스 로직에 변경이 없는 경우)

- `Chore:`: 위와 일치하지 않는 기타 변경사항(빌드 스크립트 수정, assets image, 패키지 매니저 등)

- `Docs:` : 산출물 등의 문서를 추가, 수정, 삭제한 경우

### Commit Message
- 작업한 내용을 설명한다.
- 마침표와 특수 문자를 사용하지 않는다.

<br />

## Jira

### 제목(개요)
```
날짜_작업내용
ex) 0304_README 작성
```

### Sprint
- duration 1 week

### Epic
- 업무의 큰 분류
- 기능, 작업물, 회의 등

### Story
- epic의 하위 분류
- 작은 업무들을 구체적으로 명시

### Release
- 각 sprint별

<br />

## Gerrit

### Work
1. Gerrit에 접속해서 branch 생성
2. Local에서 동일한 이름으로 branch 생성 후 작업
3. HEAD:resf/[branch name]로 push

### Review
1. 
2. 

### Submit & Abadon
1. 
2. 

<br />

## Frontend

<br />

## Backend


### IntelliJ IDEA
```
IntelliJ File -> Settings -> Editor -> Code Style -> Import Scheme -> IntelliJ IDEA code style XML -> [ /document/intellij-java-google-style.xml ]
```


<br />
