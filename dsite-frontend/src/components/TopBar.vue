<template>
    <header class="top-bar">
      <div class="top-bar-container">
        <router-link :to="{ name: 'Home' }" class="top-bar-left" @click="resetForumState">
          <img class="logo" src="/dsite-purple-circle.svg" alt="Logo" />
          <span class="site-name">Dsite</span>
        </router-link>
        <div class="top-bar-right">
          <!-- 未登入狀態 -->
          <button v-if="!authStore.isAuthenticated" class="auth-btn" @click="goToLogin">註冊 / 登入</button>

          <!-- 已登入狀態 -->
          <div v-else class="user-actions">
            <router-link :to="{ name: 'PostFormNew' }" class="action-icon" @click="resetForumState" title="發表文章">
              <font-awesome-icon :icon="['fas', 'pen']"/>
            </router-link>
            <router-link :to="{ name: 'Profile' }" class="action-icon" @click="resetForumState" title="個人資料">
              <font-awesome-icon :icon="['fas', 'user']"/>
            </router-link>
            
            <font-awesome-icon :icon="['fas', 'right-from-bracket']" class="action-icon" @click="handleLogout" title="登出"/>
          </div>
        </div>
      </div>
    </header>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { useForumStore } from '@/stores/forum';
import { useAuthStore } from '@/stores/auth';

const router = useRouter();
const forumStore = useForumStore();
const authStore = useAuthStore();

function resetForumState() {
  forumStore.resetForum();
}

function goToLogin() {
  forumStore.resetForum();
  router.push({ name: 'Login' });
  return;
}

async function handleLogout() {
  const confirmation = window.confirm("確定要登出嗎？");
  if (!confirmation) {
    return;
  }
  await authStore.logout();
  forumStore.resetForum();
  router.push({ name: 'Home' }).then(() => {
    window.location.reload();
  });
  return;
}
</script>

<style scoped>
/* 頂部懸浮欄樣式 */
.top-bar {
  position: fixed;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 100%;
  background-color: #0d547d;
  z-index: 1000;
  display: flex;
  justify-content: center;
  align-items: center;
}

.top-bar-container {
  width: 1200px; /* 固定寬度，與內容框保持一致 */
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 60px; /* 頂部懸浮欄高度 */
}

.top-bar-left {
  display: flex;
  align-items: center;
  margin-left: 20px;
  color: #fff;
}

.top-bar-left:hover {
  cursor: pointer;
}

.logo {
  height: 40px;
  margin-right: 10px;
}

.site-name {
  font-size: 24px;
  font-weight: bold;
}

.top-bar-right {
  display: flex;
  align-items: center;
  margin-right: 20px;
}

.auth-btn {
  background-color: #fff;
  color: #0d547d;
  padding: 5px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.user-actions {
  display: flex;
  gap: 15px;
  align-items: center;
}

.action-icon {
  font-size: 20px;
  cursor: pointer;
  color: white;
  margin-right: 20px;
}

.action-icon:hover {
  color: #BC91FD;
}
</style>
