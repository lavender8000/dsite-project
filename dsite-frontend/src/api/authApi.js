import axiosInstance from '@/api/axiosInstance.js';

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;

/**
 * 登入
 * @param {string} email - 郵箱
 * @param {string} password - 密碼
 * @returns {Promise} - 登入的 Promise
 */
export const login = async (email, password) => await axiosInstance.post('/auth/login', { email, password });

/**
 * 登出
 * @returns {Promise} - 登出的 Promise
 */
export const logout = async () => await axiosInstance.post('/auth/logout');

/**
 * 註冊
 * @param {string} email - 郵箱
 * @param {string} password - 密碼
 * @param {string} nickName - 暱稱
 * @returns {Promise} - 註冊的 Promise
 */
export const register = async (email, password, nickName) => await axiosInstance.post('/auth/register', { email, password, nickName });

/**
 * 檢查登入狀態
 * @returns {Promise} - 檢查登入狀態的 Promise
 */
export const checkLogin = async () => await axiosInstance.get('/auth/check-login');

/**
 * 使用 Google 認證登入
 */
export const loginWithGoogle = () => {
    window.location.href = `${apiBaseUrl}/auth/login-oauth2/google`;
};