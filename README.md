# inflearn-hodolog

## 댓글 -> 테이블 모델링 (comment) (=Comment Entity)

## 비공개, 공개 여부 (상태값) -> (Enum)

## 카테고리 -> DB(or Enum)

## 로그인 -> spring security

## 비밀번호 암호화

1. 해시
2. 해시 방식
   1. SHA1
   2. SHA256
   3. MD5
   4. 왜 이런걸로 비번 암호화 하면 안되는지
3. BCrypt, SCrypt, Argon2
   1. salt 값

### 게시글
POST /posts  
GET /posts/{postId}

### 댓글
POST /comments?postId=1  
{  
author: "ho",  
password: "1234",  
content: "ASDF"  
}  

POST /posts/{postId}/comments  
{  
   author: "ho",  
   password: "1234",  
   content: "ASDF"  
}  
1. 첫번째는 댓글이 여러 개 생기면 중복 문제 발생
2. 두번째는 URL이 길어질 문제가 발생(규모가 커졌을 때, 중간 레이어가 생길 때)

DELETE /comments/{commentsId}