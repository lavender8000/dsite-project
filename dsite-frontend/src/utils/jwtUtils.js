import { jwtDecode } from "jwt-decode";

const tokenKey = 'authToken';

/**
 * 取得 JWT
 * @returns {string|null} - JWT 或 null
 */
export function getToken() {
  return localStorage.getItem(tokenKey);
}

/**
 * 檢查 JWT 是否有效
 * @param {string} token - JWT 字符串
 * @returns {boolean} - 是否有效
 */
export function isTokenValid(token) {
  if (!token) return false;

  try {
    const decoded = jwtDecode(token);
    const currentTime = Math.floor(Date.now() / 1000);
    return decoded.exp > currentTime;
  } catch {
    console.error("[jwtUtils] 解析 JWT 失敗:");
    return false;
  }
}
