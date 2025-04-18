<template>
  <div class="post-list-container">
    <!-- 文章列表區域 -->
    <div class="posts">
      <router-link v-for="post in posts" :key="post.id" :to="{ name: 'PostDetail', params: { postId: post.id } }" class="post-item">
        <h3>{{ post.title }}</h3>
        <p>{{ post.content }}</p>
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
      </router-link>
    </div>

    <!-- 分頁控制區域 -->
    <div class="pagination">
      <button @click="changePage(currentPage - 1)" :disabled="currentPage === 0">上一頁</button>
      <span>第 {{ currentPage + 1 }} 頁 / 共 {{ totalPages }} 頁</span>
      <button @click="changePage(currentPage + 1)" :disabled="currentPage === totalPages - 1">下一頁</button>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, nextTick } from 'vue';
import * as postApi from '@/api/postApi';
import { formatDate } from '@/utils/date.js';
import { useForumStore } from '@/stores/forum';

const forumStore = useForumStore();
const props = defineProps({
  forumId: Number,
  userId: Number,
  postsResponseData: Object
});
const posts = ref(props.postsResponseData ? props.postsResponseData.content : []);
const totalPages = ref(props.postsResponseData ? props.postsResponseData.totalPages : 0);
const currentPage = ref(0);

// 監控 forumId 的變化，並根據它獲取對應的文章
watch(
  () => ({
    forumId: props.forumId,
    userId: props.userId,
  }),
  async ({ forumId, userId }) => {
    // 如果已經有外部傳入的文章數據，不需要再獲取
    if (props.postsResponseData) {
      return;
    }
    currentPage.value = 0;
    await fetchPosts(forumId, userId, currentPage.value);
    if (forumId) {
      forumStore.updateForum(forumId);
    }
  },
  { immediate: true }
);

// 獲取文章數據
async function fetchPosts(forumId, userId, page) {
  try {
    const response = await postApi.getPosts(page, forumId, userId);
    posts.value = response.data.data.content;
    totalPages.value = response.data.data.totalPages;

    nextTick(() => {
      window.scrollTo(0, 0);
    });
  } catch (error) {
    console.error('[PostList] 獲取文章列表異常');
  }
}

// 切換頁面
function changePage(page) {
  if (page >= 0 && page < totalPages.value) {
    currentPage.value = page;
    fetchPosts(props.forumId, props.userId, page);
  }
}
</script>

<style scoped>
.post-list-container {
  border-radius: 4px;
  padding: 0 40px;
  background-color: #ffffff;
}

.posts {
  padding: 20px 0;
  display: flex;
  flex-direction: column;
}

.post-item {
  padding: 20px 0;
  border-bottom: 1px solid #ccc;
}

.post-item:last-child {
  border-bottom: none;
}

.post-item:hover {
  cursor: pointer;
}

.author-info span{
  color: #555;
  margin-right: 8px;
}

.author-link:hover {
  cursor: pointer;
  color: #bc91fd;
}

.post-item p {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  line-clamp: 1;
  -webkit-box-orient: vertical;
  box-orient: vertical;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px 0;
}

button {
  border-radius: 4px;
  margin: 0 10px;
}
</style>
