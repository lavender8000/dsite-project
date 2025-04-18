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

const handleLoginResult = async () => {
  if (porps.status == 'SUCCESS') {
    try {
      const isAuthenticated = await authStore.checkLogin();
      if (isAuthenticated) {
        const redirectPath = localStorage.getItem('redirectAfterLogin');
        if (redirectPath) {
          localStorage.removeItem('redirectAfterLogin');
          toast.success('登入成功');
          router.replace(redirectPath);
          return;
        } else {
          toast.success('登入成功');
          router.replace({ name: 'Home' });
          return;
        }
      } else {
        toast.error('登入失敗');
        router.replace({ name: 'Login' });
        return;
      }
    } catch (error) {
      toast.error('登入失敗');
      router.replace({ name: 'Login' });
      return;
    }
  }
  else if (porps.status == 'FAILURE') {
    switch (porps.error) {
      case 'USER_EMAIL_EXISTS':
        toast.error('此 Email 已存在，請改用其他方式登入');
        break;
      case 'UNSUPPORTED_OAUTH_PROVIDER':
        toast.error('不支援的 OAuth 提供者');
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
    router.replace({ name: 'Login' });
    return;
  }
  else {
    router.replace({ name: 'Home' });
    return;
  }
};

onMounted(() => {
  handleLoginResult();
});
</script>
