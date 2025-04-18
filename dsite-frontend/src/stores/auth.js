import { defineStore } from 'pinia';
import * as authApi from '@/api/authApi';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    isAuthenticated: null, // 初始值為 null 表示尚未檢查
    user: null,
  }),

  actions: {
    /**
     * 設置用戶資料
     * @param {object} userData - 用戶資料
     */
    setAuthenticatedUser(userData) {
      this.user = userData;
      this.isAuthenticated = true;
    },
    
    /**
     * 設置未認證用戶
     */
    setUnauthenticatedUser() {
      this.user = null;
      this.isAuthenticated = false;
    },

    /**
     * 檢查登入狀態，調用後端 API 並更新本地狀態
     * @returns {boolean} - 登入狀態
     */
    async checkLogin() {
      try {
        const response = await authApi.checkLogin();
        if (response.status === 200) {
          this.setAuthenticatedUser(response.data.data);
          return true;
        } else {
          this.setUnauthenticatedUser();
          return false;
        }
      } catch (error) {
        console.error('[authStore] 檢查登入狀態失敗');
        this.setUnauthenticatedUser();
        return false;
      }
    },

    /**
     * 登入操作，調用後端 API 並更新本地狀態
     * @param {string} email - 用戶的郵箱
     * @param {string} password - 用戶的密碼
     * @returns {boolean} - 登入是否成功
     */
    async login(email, password) {
      try {
        const response = await authApi.login(email, password);
        if (response.status === 200) {
          this.setAuthenticatedUser(response.data.data);
          return true;
        } else {
          this.setUnauthenticatedUser();
          return false;
        }
      } catch (error) {
        console.error('[authStore] 登入失敗');
        this.setUnauthenticatedUser();

        // 測試用：打印出 錯誤信息
        console.error(error)

        return false;
      }
    },

    /**
     * 登出操作，調用後端 API 並清理本地狀態
     */
    async logout() {
      try {
        await authApi.logout();
      } catch (error) {
        console.error('[authStore] 登出異常');
      } finally {
        this.setUnauthenticatedUser();
      }
    },

    /**
     * 註冊操作，調用後端 API 並更新本地狀態
     * @param {string} email - 用戶的郵箱
     * @param {string} password - 用戶的密碼
     * @param {string} nickName - 用戶的暱稱
     * @returns {object} - 註冊結果的物件 {success: boolean, message: string}
     */
    async register(email, password, nickName) {
      try {
        const response = await authApi.register(email, password, nickName);
        if (response.status === 201) {
          this.setAuthenticatedUser(response.data.data);
          return { success: true, message: '註冊成功' };
        } else {
          this.setUnauthenticatedUser();
          return { success: false, message: '註冊失敗' };
        }
      } catch (error) {
        console.error('[authStore] 註冊失敗');
        this.setUnauthenticatedUser();
        if (error.response) {
          const { responseStatus, msg } = error.response.data;
          if (responseStatus === 'USER_EMAIL_EXISTS') {
            return { success: false, message: '該郵箱已被註冊，請更換郵箱' };
          } else if (responseStatus === 'INVALID_ARGUMENT') {
            return { success: false, message: '請確認輸入的資料是否正確' };
          } else {
            return { success: false, message: `註冊失敗：${msg}` };
          }
        } else {
          return { success: false, message: '網絡錯誤，請稍後再試' };
        }
      }
    },

    /**
     * 登入操作並使用 Google 認證，調用後端 API 並由後端進行重定向。
     * 後端重定向至 LoginOauth2Result.vue 後會執行 checkLogin()。
     */
    // async loginWithGoogle() {
    //   try {
    //     await authApi.loginWithGoogle();
    //   } catch (error) {
    //     console.error('[authStore] Google 登入失敗');
    //   }
    // },
  },
});
