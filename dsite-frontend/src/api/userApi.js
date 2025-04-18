import axiosInstance from '@/api/axiosInstance.js';

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;

/**
 * 獲取用戶信息
 * @param {number} userId - 要獲取的用戶 ID
 * @returns {Promise} - 獲取用戶信息的 Promise
 */
export const getUser = async (userId) => await axiosInstance.get(`/users/${userId}/public`);

/**
 * 獲取用戶賬戶信息
 * @returns {Promise} - 獲取用戶賬戶信息的 Promise
 */
export const getUserAccountProfile = async () => await axiosInstance.get(`/users/me/profile`);

/**
 * 更新用戶密碼
 * @param {string} currentPassword - 現在的密碼
 * @param {string} newPassword - 新密碼
 * @returns {Promise} - 更新用戶密碼的 Promise
 */
export const updateUserPassword = async (currentPassword, newPassword) => await axiosInstance.put('/users/me/password', { currentPassword, newPassword });

/**
 * 更新用戶一般信息
 * @param {string} nickname - 暱稱
 * @returns {Promise} - 更新用戶信息的 Promise
 */
export const updateUserInfo = async (nickname) => await axiosInstance.put('/users/me', { nickName: nickname });

/**
 * 連接 Google 賬戶
 */
export const linkGoogle = () => {
    window.location.href = `${apiBaseUrl}/users/me/link-google`;
}