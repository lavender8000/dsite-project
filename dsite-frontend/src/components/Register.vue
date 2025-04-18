<template>
  <div class="register">
    <div class="register-container">
      <h1>註冊</h1>

      <!-- 註冊表單 -->
      <form @submit.prevent="handleRegister">
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
  
        <div class="form-group">
          <label for="nickName">暱稱</label>
          <input
            type="text"
            id="nickName"
            v-model="nickName"
            placeholder="請輸入暱稱"
            required
            maxlength="50"
          />
        </div>
  
        <button type="submit" class="btn primary" :disabled="isLoading">
          {{ isLoading ? '註冊中...' : '註冊' }}
        </button>
      </form>

      <!-- 錯誤訊息 -->
      <p v-if="errorMessage" class="error-message">
        <font-awesome-icon :icon="['fas', 'circle-exclamation']" /> {{ errorMessage }}
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import { useForumStore } from '@/stores/forum';
import { useToast } from 'vue-toastification';

const router = useRouter();
const authStore = useAuthStore();
const forumStore = useForumStore();
const toast = useToast();
const email = ref('');
const password = ref('');
const nickName = ref('');
const isLoading = ref(false);
const errorMessage = ref('');

forumStore.resetForum();

async function handleRegister() {
  isLoading.value = true;
  errorMessage.value = '';
  const result = await authStore.register(email.value, password.value, nickName.value);
  if (result.success) {
    const redirectPath = localStorage.getItem('redirectAfterLogin');
    if (redirectPath) {
      localStorage.removeItem('redirectAfterLogin');
      toast.success('註冊成功');
      router.replace(redirectPath);
      return;
    } else {
      toast.success('註冊成功');
      router.replace({ name: 'Home' });
      return;
    }
  } else {
    isLoading.value = false;
    errorMessage.value = result.message;
  }
}
</script>

<style scoped>
.register {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: auto;
  padding: 20px;
}

.register-container {
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

.btn:hover {
  opacity: 0.8;
}

.error-message {
  color: #db4437;
  font-size: 14px;
}
</style>
