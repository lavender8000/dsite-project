<template>
  <div class="profile-container">
    <!-- 選單 -->
    <div class="profile-sidebar">
      <button @click="changeTab('my-posts')">
        <font-awesome-icon :icon="['fas', 'user']" class="action-icon"/>
        我的文章
      </button>
      <button @click="changeTab('settings')">
        <font-awesome-icon :icon="['fas', 'gear']" class="action-icon"/>
        設定
      </button>
    </div>
    
    <!-- 內容 -->
    <div class="profile-content">
      <!-- 顯示 我的文章 -->
      <div v-if="activeTab === 'my-posts'" class="my-posts">
        <AuthorPage :userId="authStore.user.id" />
      </div>

      <!-- 顯示 設定 -->
      <div v-if="activeTab === 'settings'" class="settings">
        <div class="profile-content-header">
          <h2>設定</h2>
        </div>
        
        <div class="settings-container">
          <div class="setting-item" id="email">
            <div class="item-content">
              <div class="item-title">信箱</div>
              <div class="item-info">
                <p>{{ authStore.user.email }}</p>
              </div>
            </div>
          </div>
          
          <div class="setting-item" id="nickname">
            <div class="item-content">
              <div class="item-title">暱稱</div>
              <div class="item-info">
                <p>{{ authStore.user.nickname }}</p>
              </div>
            </div>
            <button class="edit-button" @click="openModal('nickname')">更改</button>
          </div>
          
          <div class="setting-item" id="password">
            <div class="item-content">
              <div class="item-title">密碼</div>
              <div class="item-info">
              </div>
            </div>
            <button v-if="userProfile.passwordSet" class="edit-button" @click="openModal('password')">更改</button>
            <button v-else class="edit-button" @click="openModal('newPassword')">新增</button>
          </div>
          
          <div class="setting-item" id="googleLinked">
            <div class="item-content">
              <div class="item-title">Google 帳號連結</div>
              <div class="item-info">
                <p>{{ googleLinked ? userOauthAccount.oauthEmail : '未連結' }}</p>
              </div>
            </div>
            <button v-if="!googleLinked" class="edit-button" @click="linkGoogle">連結</button>
          </div>
        </div>
      </div>

    </div>

    <!-- 模擬彈出框 -->
    <SimpleModal
      :visible="modalVisible"
      :title="modalTitle"
      @cancel="closeModal"
    >

      <form @submit.prevent="handleModalSubmit">
        <!-- 修改暱稱 -->
        <template v-if="modalType === 'nickname'">
          <label class="modal-label" for="nickname">新的暱稱</label>
          <input class="modal-input" type="text" v-model="modalForm.nickname" id="nickname" placeholder="新的暱稱" required/>
        </template>

        <!-- 修改密碼 -->
        <template v-if="modalType === 'password'">
          <label class="modal-label" for="currentPassword">目前密碼</label>
          <input class="modal-input" :type="showPassword ? 'text' : 'password'" v-model="modalForm.currentPassword" id="currentPassword" placeholder="目前密碼" required/>
          <label class="modal-label" for="newPassword">新密碼</label>
          <input class="modal-input" :type="showPassword ? 'text' : 'password'" v-model="modalForm.newPassword" id="newPassword" placeholder="新密碼" required/>
          <label class="modal-label" for="confirmPassword">確認密碼</label>
          <input class="modal-input" :type="showPassword ? 'text' : 'password'" v-model="modalForm.confirmPassword" id="confirmPassword" placeholder="再次輸入新密碼" required/>
          <div class="show-password-toggle">
            <input type="checkbox" id="showPassword" v-model="showPassword">
            <label for="showPassword">顯示密碼</label>
          </div>
        </template>

        <!-- 新增密碼 -->
        <template v-if="modalType === 'newPassword'">
          <label class="modal-label" for="newPassword">新密碼</label>
          <input class="modal-input" :type="showPassword ? 'text' : 'password'" v-model="modalForm.newPassword" id="newPassword" placeholder="新密碼" required/>
          <label class="modal-label" for="confirmPassword">確認密碼</label>
          <input class="modal-input" :type="showPassword ? 'text' : 'password'" v-model="modalForm.confirmPassword" id="confirmPassword" placeholder="再次輸入新密碼" required/>
          <div class="show-password-toggle">
            <input type="checkbox" id="showPassword" v-model="showPassword">
            <label for="showPassword">顯示密碼</label>
          </div>
        </template>

        <!-- 按鈕 -->
        <div class="modal-actions">
          <button type="button" class="cancel-button" @click="closeModal">取消</button>
          <button type="submit" class="submit-button" :disabled="isSubmitting">
            {{ isSubmitting ? "送出中..." : "送出" }}
          </button>
        </div>
      </form>

    </SimpleModal>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { useRouter } from 'vue-router';
import { useToast } from 'vue-toastification';
import * as userApi from '@/api/userApi';
import AuthorPage from '@/components/AuthorPage.vue';
import SimpleModal from '@/components/SimpleModal.vue';

const authStore = useAuthStore();
const router = useRouter();
const toast = useToast();

const userProfile = ref({});
const userOauthAccount = ref({
  oauthProvider: '',
  oauthEmail: ''
});
const googleLinked = ref(false);


// 默認顯示 "設定" 頁面
const activeTab = ref('settings');

// 控制模擬彈出框
const modalVisible = ref(false);
const modalType = ref('');
const modalTitle = ref('');
const modalForm = ref({
  nickname: '',
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
});
const showPassword = ref(false);
const isSubmitting = ref(false);

onMounted(async () => {
  try {
    const checked = await authStore.checkLogin();
    if (!checked) {
      router.push({ name: 'Login' });
      return;
    }
    const response = await userApi.getUserAccountProfile();
    userProfile.value = response.data.data;

    if (userProfile.value.oauthAccounts.length > 0) {
      userOauthAccount.value = userProfile.value.oauthAccounts[0];
    }

    if (userOauthAccount.value.oauthProvider === 'GOOGLE' && userOauthAccount.value.oauthEmail) {
      googleLinked.value = true;
    }

  } catch (error) {
    console.error('[Profile] 獲取用戶資料或文章異常');
  }
});

// 綁定 Google 帳號
async function linkGoogle() {
  await userApi.linkGoogle();
}

// 切換選項卡
function changeTab(tab) {
  activeTab.value = tab;
}

// 開啟模擬彈出框
function openModal(type) {
  modalType.value = type;
  modalVisible.value = true;
  if (type === 'nickname') {
    modalTitle.value = '更改暱稱';
  } else if (type === 'password') {
    modalTitle.value = '更改密碼';
  }
}

// 關閉模擬彈出框
function closeModal() {
  modalVisible.value = false;
  modalTitle.value = '';
  resetModalForm();
}

function resetModalForm() {
  modalForm.value = {
    nickname: '',
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  };
}

async function handleModalSubmit() {
  try {
    isSubmitting.value = true;
    if (modalType.value === 'nickname') {
      // 修改暱稱
      const { nickname } = modalForm.value;
      if (!nickname.trim()) {
        toast.error('暱稱不可為空白');
        return;
      }
      const response = await userApi.updateUserInfo(nickname.trim());
      if (response.status === 200) {
        toast.success('暱稱更新成功');
        userProfile.value.nickname = nickname.trim();
        closeModal();
        authStore.checkLogin();
        return;
      }
    } 
    else if (modalType.value === 'password' || modalType.value === 'newPassword') {
      // 修改密碼
      const { currentPassword, newPassword, confirmPassword } = modalForm.value;
      if (newPassword !== confirmPassword) {
        toast.error('新密碼與確認密碼不一致');
        return;
      }
      if (currentPassword === newPassword) {
        toast.error('新密碼與舊密碼相同');
        return;
      }
      if (modalType.value === 'password') {
        if (!currentPassword.trim() || !newPassword.trim() || !confirmPassword.trim()) {
          toast.error('密碼不可為空白');
          return;
        }
      } else if (modalType.value === 'newPassword') {
        if (!newPassword.trim() || !confirmPassword.trim()) {
          toast.error('密碼不可為空白');
          return;
        }
      }
      const response = await userApi.updateUserPassword(currentPassword, newPassword);
      if (response.status === 200) {
        toast.success('密碼更新成功');
        closeModal();
        authStore.checkLogin();
        return;
      }
    }
  } catch (error) {
    const responseStatus = error.response ? error.response.data.responseStatus : null;
    switch (responseStatus) {
      case 'USER_PASSWORD_NOT_MATCHED':
        toast.error('密碼不正確');
        break;
      case 'USER_PASSWORD_ALREADY_SET':
        toast.error('密碼已設定');
        break;
      case 'USER_NOT_FOUND':
        toast.error('用戶不存在');
        break;
      default:
        toast.error('更新失敗');
        console.error('[Profile] 更新異常', error);
        break;
    }
  } finally {
    isSubmitting.value = false;
  }
}

</script>

<style scoped>
.profile-container {
  display: flex;
  margin: 20px auto;
}

.profile-content {
  width: 700px;
  border-radius: 4px;
  background-color: #F2F2F2;
  padding: 20px 40px;
}

.profile-sidebar {
  width: 180px;
  padding-right: 20px;
}

.profile-sidebar button {
  display: block;
  width: 100%;
  margin-bottom: 10px;
  padding: 10px 20px;
  background-color: #0d547d;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
  font-size: 16px;
  text-align: left;
}

.profile-sidebar button:hover {
  background-color: #335c77;
}

.profile-sidebar button .action-icon {
  margin-right: 10px;
}

.profile-content-header {
  border-bottom: 1px solid #ccc;
  margin-bottom: 20px;
}

.profile-content-header h2 {
  font-weight: normal;
}

.setting-item {
  border-bottom: 1px solid #ccc;
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
}

.setting-item div {
  display: block;
  margin-bottom: 5px;
}

.setting-item .item-title {
  font-weight: bold;
}

.setting-item .item-info {
  min-height: 40px;
}

.my-posts .post-item {
  padding: 20px;
  border-bottom: 1px solid #ccc;
}

.my-posts .post-item:last-child {
  border-bottom: none;
}

button {
  cursor: pointer;
}

.edit-button {
  align-self: start;
  background-color: #0d547d;
  color: white;
  padding: 5px 10px;
  border-radius: 4px;
  cursor: pointer;
}

.edit-button:hover {
  background-color: #335c77;
}

.show-password-toggle {
  color: #555;
  margin-left: 5px;
  margin-top: 20px;
}

</style>
