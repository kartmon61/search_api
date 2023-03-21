# search_api
카카오 api와 네이버api를 사용한 블로그 검색 서비스 입니다.

JAR 파일 실행 방법
- search.jar 다운 받은 후, 파일이 있는 위치에서 java -jar search.jar 실행

API 명세

1. 블로그 검색 API
- 설명: 
    - 카카오 api를 사용하여 다음 블로그 서비스에서 검색을 합니다.
    - 네이버 api를 사용하여 네이버 블로그 서비스에서 검색을 합니다. (네이버 api 사용시)
- Endpoint: GET /api/v1/search
- Query Parameters:
    - query: 검색 키워드 (필수)
    - sort: 정렬 기준 (옵션, 기본값: 정확도순)
        - accuracy: 정확도순
        - recency: 최신순
    - page: 페이지 번호 (옵션, 기본값: 1)
    - size: 페이지 당 결과 수 (옵션, 기본값: 10)
- Response:

```java
{
  "content": [
    {
      "title": String,
      "url": String,
      "contents": String,
      "thumbnail": String,
      "datetime": String
    },
    ...
  ],
  "page": Number,
  "size": Number,
  "totalElements": Number,
  "totalPages": Number
}
```

2. 인기 검색어 목록 API
- 설명:
  - 상위 10개의 검색어와 검색된 횟수 정보를 검색횟수 기준으로 내림차순으로 정보를 전달 합니다.
- Endpoint: GET /api/v1/search/popular
- Response:

```java
[
  {
    "keyword": String,
    "count": Number
  },
  ...
]
```