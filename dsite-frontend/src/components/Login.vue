<template>
    <div class="login">
      <div class="login-container">
        <h1>登入</h1>
  
        <!-- 登入表單 -->
        <form @submit.prevent="handleLogin">
          <div class="form-group">
            <label for="email">電子郵件</label>
            <input 
              type="email" 
              id="email" 
              v-model="email" 
              placeholder="請輸入電子郵件" 
              required 
              maxlength="320"
            />
          </div>
  
          <div class="form-group">
            <label for="password">密碼</label>
            <input 
              type="password" 
              id="password" 
              v-model="password" 
              placeholder="請輸入密碼" 
              required 
              maxlength="255"
            />
          </div>
  
          <button type="submit" class="btn primary" :disabled="isLoading">
            {{ isLoading ? "登入中..." : "登入" }}
          </button>
        </form>

        <!-- 錯誤訊息 -->
        <p v-if="errorMessage" class="error-message">
          <font-awesome-icon :icon="['fas', 'circle-exclamation']" /> {{ errorMessage }}
        </p>

        <!-- 功能按鈕 -->
        <div class="actions">
          <button class="btn secondary" @click="goToRegister">註冊</button>
          <button class="btn google" @click="loginWithGoogle">使用 Google 登入</button>
        </div>
      </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import { useForumStore } from '@/stores/forum';
import { useToast } from 'vue-toastification';
import * as authApi from '@/api/authApi';

const router = useRouter();
const authStore = useAuthStore();
const forumStore = useForumStore();
const toast = useToast();
const email = ref('');
const password = ref('');
const isLoading = ref(false);
const errorMessage = ref('');

forumStore.resetForum();

// 使用 信箱 和 密碼 登入
async function handleLogin() {
  isLoading.value = true;
  errorMessage.value = '';
  const success = await authStore.login(email.value, password.value);
  if (success) {
    const redirectPath = localStorage.getItem('redirectAfterLogin');
    if (redirectPath) {
      localStorage.removeItem('redirectAfterLogin');
      toast.success('登入成功');
      router.replace(redirectPath);
      return;
    } else {
      toast.success('登入成功');
      router.replace({ name: 'Home' });
      return;
    }
  } else {
    isLoading.value = false;
    errorMessage.value = '登入失敗，請檢查您的帳號密碼';
  }
}

// 跳轉到註冊頁面
function goToRegister() {
  router.push({ name: 'Register' });
  return;
}

// 使用 Google 登入
async function loginWithGoogle() {
  await authApi.loginWithGoogle();
}
</script>

<style scoped>
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: auto;
  padding: 20px;
}

.login-container {
  background: white;
  padding: 20px;
  border-radius: 4px;
  max-width: 400px;
  width: 100%;
  text-align: center;
}

h1 {
  margin-bottom: 24px;
}

.form-group {
  margin-bottom: 16px;
  text-align: left;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: bold;
}

.form-group input {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 16px;
  box-sizing: border-box;
}

button {
  display: inline-block;
  width: 100%;
  padding: 12px;
  font-size: 14px;
  font-weight: bold;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 8px;
}

button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn.primary {
  background-color: #0d547d;
  color: white;
}

.btn.secondary {
  background-color: #6c757d;
  color: white;
}

.btn.google {
  background-color: #db4437;
  color: white;
}

.btn:hover {
  opacity: 0.8;
}

.actions {
  margin-top: 16px;
}

.actions .btn {
  margin-bottom: 8px;
}

.error-message {
  color: #db4437;
  font-size: 14px;
}
</style>
