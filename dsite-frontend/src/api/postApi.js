import axiosInstance from '@/api/axiosInstance.js';

/**
 * 獲取用戶文章
 * @param {number} userId - 要獲取的用戶 ID
 * @returns {Promise} - 獲取用戶文章的 Promise
 */
export const getPostsByUserId = async (userId) => await axiosInstance.get(`/posts?userId=${userId}`);

/**
 * 獲取單一文章
 * @param {number} postId - 要獲取的文章 ID
 * @returns {Promise} - 獲取單一文章的 Promise
 */
export const getPostById = async (postId) => await axiosInstance.get(`/posts/${postId}`);

/**
 * 獲取文章列表
 * @param {number} page - 頁碼，預設為 0
 * @param {number} forumId - 要獲取的論壇 ID
 * @param {number} userId - 要獲取的用戶 ID
 * @returns {Promise} - 獲取文章列表的 Promise
 */
export const getPosts = async (page = 0, forumId, userId) => {
    let url = `/posts?page=${page}`;
    if (forumId) url += `&forumId=${forumId}`;
    if (userId) url += `&userId=${userId}`;
    return await axiosInstance.get(url);
}

/**
 * 新增文章
 * @param {Object} post - 要新增的文章數據，格式為 { title: string, content: string, forumId: number }
 * @returns {Promise} - 新增文章的 Promise
 */
export const createPost = async (title, content, forumId) => await axiosInstance.post('/posts', { title, content, forumId });

/**
 * 更新文章
 * @param {number} postId - 要更新的文章 ID
 * @param {Object} post - 要更新的文章數據，格式為 { title: string, content: string, forumId: number }
 * @returns {Promise} - 更新文章的 Promise
 */
export const updatePost = async (postId, title, content, forumId) => await axiosInstance.put(`/posts/${postId}`, { title, content, forumId });

/**
 * 刪除文章
 * @param {number} postId - 要刪除的文章 ID
 * @returns {Promise} - 刪除文章的 Promise
 */
export const deletePost = async (postId) => await axiosInstance.delete(`/posts/${postId}`);
