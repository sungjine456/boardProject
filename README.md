## 혼자서 게시판만들기
----
	1. 내가 써보지 않은 기술들을 경험하기 위한 게시판 만들기
	2. 하루 1회 이상 커밋 하기!

## 진행해야 되는 기능들..
----
	에디터에 글자 크기 넣기
	에디터에서 이미지 등록시 두 개 이상 넣으면 안되는 이슈 해결하기
	뎃글, 답글에 삭제 기능 넣기

## 진행된 기능들..
----
	email 양식 검사
	비밀번호 단방향 암호화
	회원 가입 & 수정 & 탈퇴, 로그인
	xss(Cross Site Scripting) 공격 방어
	자동로그인
	게시글 페이징
	뎃글 작성 & 수정
	유저의 이미지 저장
	뎃글의 답글 : 최신 글이 위로 향하게 
	게시글의 조회수 (cookie를 통한 새로고침으로 조회수 올리는 것 방지)
	messageSource를 통한 message관리
	간단한 웹 에디터 만들어보기(볼드체, 정렬(왼쪽, 가운데, 오른쪽))
	mail검증 절차 & 메일로 비밀번호 재발급

## 사용기술
----
```
front end
	1. Freemarker
	2. bootstrap
	3. jQuery
```
```
back end
	1. java 8
	2. Spring Boot
	3. Spring Data JPA(ORM)
	4. QueryDSL
	5. MySql
	6. SLF4J(Logger)
	7. jUnit(Test)
	8. javamail
```
```
util library
	1. joda-time
	2. apache common lang3
```
```
build tool
	1. Gradle
```
```
issue tracker
	1. Trello
	2. Github
```
```
DVCS
	1. Git
```