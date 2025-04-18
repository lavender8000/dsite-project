<template>
  <div class="main-layout">
    <TopBar />
    <main class="main-layout-content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import TopBar from '@/components/TopBar.vue';
</script>

<style scoped>
.main-layout-content {
  display: flex;
  flex: 1;
  margin-top: 60px; /* 避開頂部懸浮欄的 TopBar */
}
</style>
