# DSite 後端專案

本專案是使用 **Spring Boot** 開發的後端 API，採用前後端分離架構，提供帳號密碼與 Google OAuth2 登入、文章管理、JWT 驗證等功能，並整合 Redis 進行快取與 Token 黑名單處理。

## 使用技術

- Java 17
- Spring Boot 3
- Spring Security（支援帳號密碼與 Google OAuth2 登入）
- Spring Data JPA（MySQL）
- Redis（作為 Token 快取與黑名單）
- JWT（JSON Web Token 驗證）

## 專案結構

```
dsite-backend/src/main/
├── java/com/lav/dsite/
│   ├── common/               # 共用回應格式 Result
│   ├── config/               # JWT、Redis、Security 設定
│   ├── controller/           # 各種 API Controller
│   ├── dto/                  # 請求與回應資料封裝 (DTO)
│   ├── entity/               # JPA Entity 模型
│   ├── enums/                # 系統常數與 Enum
│   ├── exception/            # 自定義例外處理
│   ├── filter/               # JWT 驗證過濾器
│   ├── repository/           # 資料操作介面
│   ├── security/             # OAuth2 登入流程與處理器
│   ├── service               # 核心商業邏輯
│   └── DsiteApplication.java # 啟動入口
└── resources/
    └── application.yml       # 設定檔
```

## 功能簡介

### 使用者系統
- 支援帳號密碼註冊、登入
- 支援 Google OAuth2 第三方登入
- 支援文章發佈、瀏覽、編輯與刪除

### 驗證系統
- JWT 驗證過濾器
- Redis 快取登入使用者資訊與處理 Token 黑名單

## API 範例

#### 使用者登入（JWT）
`POST /auth/login`
```json
{
  "email": "test@example.com",
  "password": "123456"
}
```

#### Google 登入（OAuth2）
- 前端導向 `/auth/login-oauth2/google`
- 成功後將透過 redirect 傳回前端頁面

#### 建立文章
`POST /posts`
```json
{
  "forumId": 1,
  "title": "這是一篇測試文章",
  "content": "文章內容..."
}
```
- 需要攜帶 JWT Token

## 附註

本專案已部署於 GCP VM（Ubuntu 20.04）

🔗 線上網站：https://dsite.ddns.net