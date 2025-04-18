import axiosInstance from '@/api/axiosInstance.js';

/**
 * 獲取論壇列表
 * @param {number} size - 每頁的項目數量，預設為 10
 * @returns {Promise} - 論壇列表的 Promise
 */
export const getForums = async (size = 10) => await axiosInstance.get(`/forums?size=${size}`);