<template>
  <aside class="sidebar">
    <div class="sidebar-container">
      <ul class="nav-list">
        <li 
          v-for="forum in forums" :key="forum.id"
        >
        
          <!-- <div
            class="nav-item"
            :class="{ active: forumStore.selectedForumId === forum.id }"
            @click="selectForum(forum.id)"
          >
            {{ forum.name }}
          </div> -->

          <router-link
            :to="{ name: 'PostList', query: { forumId: forum.id } }"
            :class="{ active: forumStore.selectedForumId === forum.id }"
            @click="selectForum(forum.id)"
          >
            <div class="nav-item">{{ forum.name }}</div>
          </router-link>

        </li>
      </ul>
    </div>
  </aside>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useForumStore } from '@/stores/forum';
import * as forumApi from '@/api/forumApi';

const router = useRouter();
const forumStore = useForumStore();
const forums = ref([]);

// 獲取 forums 數據
onMounted(async () => {
  try {
    const response = await forumApi.getForums();
    forums.value = response.data.data;
  } catch (error) {
    console.error('[Sidebar] 獲取 forums 異常');
  }
});

const selectForum = (newForumId) => {
  forumStore.updateForum(newForumId);
  // router.push({ name: 'PostList', query: { forumId: newForumId } });
  // return;
};
</script>

<style scoped>
.sidebar {
  background-color: transparent;
  color: white;
  display: flex;
  flex-direction: column;
}

.sidebar-container {
  display: flex;
}

.nav-list {
  list-style-type: none;
  margin: 0;
  padding: 0;
  width: 100%;
}

.nav-list li {
  cursor: pointer;
}

.nav-item {
  padding: 10px 0 10px 20px;
  font-weight: bold;
}

.nav-item:hover {
  cursor: pointer;
  background-color: #002133;
}

.nav-list .active .nav-item{
  background-color: #4D7184;
}

</style>
