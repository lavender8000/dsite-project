<template>
  <div class="post-form">
    <div class="post-form-container">
      <div class="post-form-content">
        <h1>{{ isEdit ? "編輯文章" : "新增文章" }}</h1>
        <form @submit.prevent="handleSubmit">

          <!-- 論壇選擇 -->
          <div class="form-group">
            <label for="forumId" class="form-label">論壇:</label>
            <div v-if="isEdit">
              <input
                type="text"
                id="forumId"
                v-model="forumName"
                class="form-input"
                :disabled="true"
                required
              />
            </div>
            <div v-else>
              <select id="forumId" v-model="form.forumId" class="form-select" required>
                <option value="" disabled>選擇論壇分類</option>
                <option v-for="forum in forums" :key="forum.id" :value="forum.id">
                  {{ forum.name }}
                </option>
              </select>
            </div>
          </div>

          <!-- 標題輸入 -->
          <div class="form-group">
            <label for="title" class="form-label">標題:</label>
            <input
              type="text"
              id="title"
              v-model="form.title"
              class="form-input"
              maxlength="80"
              required
            />
          </div>

          <!-- 內容輸入 -->
          <div class="form-group">
            <label for="content" class="form-label">內容:</label>
            <textarea id="content" v-model="form.content" class="form-textarea" required></textarea>
          </div>

          <!-- 提交按鈕 -->
          <div class="form-actions">
            <button type="submit" class="form-button" :disabled="isSubmitting">
              {{ isSubmitting ? "提交中..." : isEdit ? "更新文章" : "提交文章" }}
            </button>
          </div>

        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from "vue";
import { useRouter, useRoute } from "vue-router";
import * as postApi from '@/api/postApi';
import * as forumApi from '@/api/forumApi';
import { useAuthStore } from "@/stores/auth";
import { useToast } from 'vue-toastification';

const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();
const toast = useToast();
const porps = defineProps({
  postId: {
    type: Number,
    default: null,
  },
});
const isEdit = ref(porps.postId !== null);
const postId = ref(porps.postId);
const form = ref({
  forumId: "",
  title: "",
  content: "",
});
const forumName = ref("");
const forums = ref([]);
const isSubmitting = ref(false);

const loadPost = async () => {
  if (!isEdit.value) {
    // 新增文章模式
    form.value = { forumId: "", title: "", content: "" };
    forumName.value = "";
  } else {
    // 編輯文章模式
    try {
      if (isNaN(postId.value)) {
        console.log("[PostForm] 無此文章或權限N");
        router.replace({ name: "Home" });
        return;
      }

      const response = await postApi.getPostById(postId.value);
      const { forumId, title, content, forumName: fetchedForumName, userId } = response.data.data;

      // 設置表單數據
      form.value = { forumId, title, content };
      forumName.value = fetchedForumName;

      console.log(authStore.user.id, userId);
      console.log(authStore.isAuthenticated);

      // 驗證權限
      if (authStore.user.id !== userId) {
        authStore.checkLogin();
        console.log("[PostForm] 無此文章或權限...");
        router.replace({ name: "Home" });
        return;
      }
    } catch (error) {
      console.log("[PostForm] 載入文章失敗");
      router.replace({ name: "Home" });
      return;
    }
  }
};

const loadForums = async () => {
  if (isEdit.value) return;
  try {
    const response = await forumApi.getForums();
    forums.value = response.data.data;
  } catch (error) {
    console.log("[PostForm] 載入論壇失敗");
  }
};

const handleSubmit = async () => {
  if (isSubmitting.value) return;
  isSubmitting.value = true;

  try {
    if (isEdit.value) {
      const response = await postApi.updatePost(postId.value, form.value.title, form.value.content, form.value.forumId);
      toast.success("文章更新成功");
      router.replace({ name: "PostDetail", params: { postId: response.data.data.id } });
      return;
    } else {
      const response = await postApi.createPost(form.value.title, form.value.content, form.value.forumId);
      toast.success("文章新增成功");
      router.replace({ name: "PostDetail", params: { postId: response.data.data.id } });
      return;
    }
  } catch (error) {
    toast.error("操作失敗");
  } finally {
    isSubmitting.value = false;
  }
};

watch(
  () => route.params.postId, (newPostId) => {
    isEdit.value = newPostId ? true : false;
    postId.value = Number(newPostId);
    loadForums();
    loadPost();
  }
);

onMounted(() => {
  loadForums();
  loadPost();
});
</script>

<style scoped>
.post-form {
  display: flex;
  margin: auto;
  padding: 20px 0;
  justify-content: center;
  width: 700px;
}

.post-form-container {
  border-radius: 4px;
  background-color: #ffffff;
  padding: 0 40px;
  flex: 1;
}

.post-form-content {
  padding: 20px 0;
}

.form-title {
  font-size: 24px;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.form-label {
  display: block;
  margin-bottom: 8px;
  font-weight: bold;
}

.form-input,
.form-select,
.form-textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}

.form-textarea {
  min-height: 200px;
}

.form-actions {
  display: flex;
  justify-content: center;
}

.form-button {
  padding: 10px 20px;
  background-color: #0d547d;
  border: none;
  border-radius: 4px;
  color: white;
  cursor: pointer;
  font-size: 16px;
}

.form-button:hover {
  opacity: 0.8;
}
</style>
