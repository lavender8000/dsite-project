<template>
    <div></div>
</template>

<script setup>
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import { useToast } from 'vue-toastification';

const router = useRouter();
const authStore = useAuthStore();
const toast = useToast();
const porps = defineProps({
  status: String,
  error: String
});

const handleLinkResult = async () => {
  if (porps.status == 'SUCCESS') {
    try {
      const isAuthenticated = await authStore.checkLogin();
      if (isAuthenticated) {
        toast.success('綁定成功');
        router.replace({ name: 'Profile' });
        return;
      } else {
        toast.error('綁定失敗');
        router.replace({ name: 'Profile' });
        return;
      }
    } catch (error) {
      toast.error('綁定失敗');
      router.replace({ name: 'Profile' });
      return;
    }
  }
  else if (porps.status == 'FAILURE') {
    switch (porps.error) {
      case 'USER_EMAIL_EXISTS':
        toast.error('此 Email 已存在');
        break;
      case 'USER_OAUTH_ACCOUNT_EXISTS':
        toast.error('此帳號已被使用');
        break;
      case 'UNSUPPORTED_OAUTH_PROVIDER':
        toast.error('不支援的 OAuth 提供者');
        break;
      case 'INVALID_ARGUMENT':
        toast.error('參數錯誤');
        break;
      case 'REDIS_ERROR':
        toast.error('伺服器 Redis 錯誤，請稍後再試');
        break;
      case 'OPTIMISTIC_LOCK_EXCEPTION':
        toast.error('伺服器忙碌，請稍後再試');
        break;
      default:
        toast.error('伺服器異常，請稍後再試');
    }
    router.replace({ name: 'Profile' });
    return;
  }
  else {
    router.replace({ name: 'Home' });
    return;
  }
};

onMounted(() => {
  handleLinkResult();
});
</script>
