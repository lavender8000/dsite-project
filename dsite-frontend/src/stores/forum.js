import { ref } from 'vue';
import { defineStore } from 'pinia';

export const useForumStore = defineStore('forum', () => {
    const selectedForumId = ref(null);
  
    const resetForum = () => {
      selectedForumId.value = null;
    };
  
    const updateForum = (forumId) => {
      selectedForumId.value = forumId;
    };
  
    return {
      selectedForumId,
      resetForum,
      updateForum,
    };
  });