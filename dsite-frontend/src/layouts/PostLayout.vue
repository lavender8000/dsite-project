<template>
  <div class="post-layout">
    <div class="post-layout-sidebar">
        <Sidebar />
    </div>
    <div class="post-layout-content">
        <router-view />
    </div>
  </div>
</template>

<script setup>
import Sidebar from '@/components/Sidebar.vue';
</script>

<style scoped>
.post-layout {
  display: flex;
  width: 1200px;
  margin: auto;
}

.post-layout-sidebar {
  width: 180px;
  padding: 20px 0;
}

.post-layout-content {
  width: 720px;
  padding: 20px 0;
  margin-left: 12px;
}
</style>