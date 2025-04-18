<template>
  <div class="author-layout">
    <div class="author-layout-content">
        <router-view />
    </div>
  </div>
</template>

<style scoped>
.author-layout {
  display: flex;
  width: 700px;
  margin: auto;
}

.author-layout-content {
  flex: 1;
  padding: 20px 0;
}
</style>