<template>
  <div class="author-page">
    <div class="author-page-container">
      <div class="author-page-content">
        <div v-if="user" class="author-page-info">
          <h1>{{ user.nickName }}</h1>
          <p>{{ totalElements }} 篇文章</p>
        </div>
        <div v-if="postsResponseData" class="author-page-list">
          <PostList :forumId="null" :userId="props.userId" :postsResponseData="postsResponseData"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import * as userApi from '@/api/userApi';
import * as postApi from '@/api/postApi';
import PostList from '@/components/PostList.vue';

const props = defineProps({
  userId: Number
});
const user = ref(null);
const posts = ref([]);
const postsResponseData = ref(null);
const totalElements = ref(0);

onMounted(async () => {
  try {
    const [userResponse, postsResponse] = await Promise.all([
      userApi.getUser(props.userId),
      postApi.getPostsByUserId(props.userId)
    ]);
    user.value = userResponse.data.data;
    postsResponseData.value = postsResponse.data.data;
    posts.value = postsResponse.data.data.content;
    totalElements.value = postsResponse.data.data.totalElements;
  } catch (error) {
    console.error('[AuthorPage] 獲取用戶或文章時異常');
  }
});

</script>

<style scoped>
.author-page-container {
  border-radius: 4px;
  background-color: #F2F2F2;
  padding: 0 40px;
}

.author-page-content {
  padding: 20px 0;
}

.author-page-info {
  border-bottom: 1px solid #ccc;
}

.author-page-list {
  padding: 20px 0;
}
</style>