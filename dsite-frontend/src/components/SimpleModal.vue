<template>
  <div v-if="visible" class="modal-overlay" @click.self="onCancel">
    <div class="modal-container" @click.stop>
      <div class="modal-content">
        <h2>{{ title }}</h2>
        <slot></slot> <!-- 傳入的表單內容 -->
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  visible: Boolean,
  title: String,
});

const emit = defineEmits(['cancel']);

const onCancel = () => {
  emit('cancel');
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-container {
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 380px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.modal-content {
  width: 280px;
  margin: 0 auto;
}

/* 通用 輸入框樣式 */
::v-deep(.modal-input) {
  display: block;
  width: 100%;
  margin: 10px 0;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
  font-size: 16px;
}

::v-deep(.modal-label) {
  display: block;
  margin-bottom: 5px;
}

/* 通用 按鈕樣式 */
::v-deep(.modal-actions) {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

::v-deep(.modal-actions button) {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

::v-deep(.modal-actions button:hover) {
  opacity: 0.8;
}

::v-deep(.modal-actions .cancel-button) {
  background: #6c757d;
}

::v-deep(.modal-actions .submit-button) {
  background: #0d547d;
  color: white;
}
</style>
