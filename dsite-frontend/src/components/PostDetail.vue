<template>
  <div class="post-detail-container">
    <div v-if="post" class="post-detail-content">
      <div class="post-header">
        <h1>{{ post.title }}</h1>
        <div v-if="isAuthor" class="post-actions">
          <button @click="editPost" class="edit-btn">編輯</button>
          <button @click="deletePost" class="delete-btn">刪除</button>
        </div>
      </div>
      <div class="author-info">
        <small>
          <router-link :to="{ name: 'AuthorPage', params: { userId: post.userId } }" @click.stop>
            <span class="author-link">
              {{ post.userNickName }}
            </span>
          </router-link>
          <span>|</span>
          <span>{{ formatDate(post.createdAt) }}</span>
        </small>
      </div>
      <p class="post-content">{{ post.content }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import * as postApi from '@/api/postApi';
import { formatDate } from '@/utils/date.js';
import { useAuthStore } from '@/stores/auth';
import { useForumStore } from '@/stores/forum';
import { useToast } from 'vue-toastification';

const router = useRouter();
const authStore = useAuthStore();
const forumStore = useForumStore();
const toast = useToast();
const props = defineProps({
  postId: Number
});
const post = ref(null);
const isAuthor = ref(false);

function editPost() {
  if (!isAuthor.value) {
    toast.error('權限不足');
    return;
  }
  router.push({ name: 'PostFormEdit', params: { postId: post.value.id } });
  return;
}

async function deletePost() {
  if (!isAuthor.value) {
    toast.error('權限不足');
    return;
  }
  if (!confirm('確定要刪除此文章嗎？')) return;

  try {
    const checked = await authStore.checkLogin();
    if (!checked) return;
    
    await postApi.deletePost(post.value.id);
    toast.success('文章刪除成功');
    router.push({ name: 'Home' });
    return;
  } catch (error) {
    toast.error('文章刪除異常');
    console.error('[PostDetail] 刪除文章異常');
  }
}

// 獲取文章詳細信息
onMounted(async () => {
  try {
    const response = await postApi.getPostById(props.postId);
    post.value = response.data.data;
    forumStore.updateForum(response.data.data.forumId);
    isAuthor.value = post.value.userId === authStore.user.id;
  } catch (error) {
    console.error('[PostDetail] 獲取文章詳情信息異常');
  }
});
</script>

<style scoped>
.post-detail-container {
  border-radius: 4px;
  padding: 0 40px;
  background-color: #ffffff;
}

.post-detail-content {
  padding: 20px 0;
}

.post-content {
  white-space: pre-wrap;
  word-wrap: break-word;
}

.author-info span{
  color: #555;
  margin-right: 8px;
}

.author-link:hover {
  cursor: pointer;
  color: #BC91FD;
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.post-actions {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  margin-left: 10px;
}

.edit-btn, .delete-btn {
  background-color: #f0f0f0;
  border: none;
  border-radius: 4px;
  width: 60px;
  padding: 5px 10px;
  cursor: pointer;
  font-size: 14px;
  color: #4d4d4d;
  text-align: center;
}

.edit-btn:hover {
  background-color: #e0e0ff;
  color: #333333;
}

.delete-btn:hover {
  background-color: #ffe0e0;
  color: #333333;
}
</style>
