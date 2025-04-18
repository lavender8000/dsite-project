# DSite 專案

DSite 是一個前後端分離的網站專案，具備使用者註冊／登入（支援帳號密碼與 Google OAuth2 第三方登入）、文章發佈與管理，並透過 JWT 進行身份驗證。  
本專案由個人開發，部署於 GCP VM，作為作品集展示。

🔗 線上網站：https://dsite.ddns.net

## 專案架構

本倉庫包含前端與後端兩個子專案：

```
dsite-project/
├── dsite-backend             # 使用 Spring Boot 開發的後端 API
├── dsite-frontend            # 使用 Vue 3 + Vite 開發的前端應用
└── README.md                 # 專案總覽說明文件
```

## 技術架構總覽

| 部分   | 技術 |
|--------|------|
| 前端   | Vue 3, Vite, Pinia, Vue Router, Axios |
| 後端   | Java 17, Spring Boot 3, Spring Security, JPA, Redis, JWT, OAuth2（Google 登入）|
| 資料庫 | MySQL |
| 部署   | GCP VM（Ubuntu 20.04）|
