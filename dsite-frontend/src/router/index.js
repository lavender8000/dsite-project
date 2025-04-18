import { createRouter, createWebHistory } from 'vue-router';
import MainLayout from '@/layouts/MainLayout.vue';
import PostLayout from '@/layouts/PostLayout.vue';
import AuthorLayout from '@/layouts/AuthorLayout.vue';
import PostList from '@/components/PostList.vue';
import PostDetail from '@/components/PostDetail.vue';
import AuthorPage from '@/components/AuthorPage.vue';
import Login from '@/components/Login.vue';
import Register from '@/components/Register.vue';
import LoginOauth2Result from '@/components/LoginOauth2Result.vue';
import LinkOauth2Result from '@/components/LinkOauth2Result.vue';
import PostForm from '@/components/PostForm.vue';
import Profile from '@/components/Profile.vue';
import { useAuthStore } from '@/stores/auth';

const routes = [
  {
    path: '/',
    name: 'Home',
    component: MainLayout,
    redirect: '/posts',
    children: [
      {
        path: 'posts',
        name: 'PostLayout',
        component: PostLayout,
        children: [
          {
            path: '',
            name: 'PostList',
            component: PostList,
            props: (route) => ({ userId: Number(route.query.userId), forumId: Number(route.query.forumId) })
          },
          {
            path: ':postId',
            name: 'PostDetail',
            component: PostDetail,
            props: (route) => ({ postId: Number(route.params.postId) })
          }
        ]
      },
      {
        path: 'post-form/new-post',
        name: 'PostFormNew',
        component: PostForm,
        meta: { requiresAuth: true }
      },
      {
        path: 'post-form/edit/:postId',
        name: 'PostFormEdit',
        component: PostForm,
        props: (route) => ({ postId: Number(route.params.postId) }),
        meta: { requiresAuth: true }
      },
      {
        path: 'authors',
        name: 'AuthorLayout',
        component: AuthorLayout,
        children: [
          {
            path: ':userId',
            name: 'AuthorPage',
            component: AuthorPage,
            props: (route) => ({ userId: Number(route.params.userId) })
          }
        ]
      },
      {
        path: 'login',
        name: 'Login',
        component: Login,
        meta: { requiresGuest: true }
      },
      {
        path: 'register',
        name: 'Register',
        component: Register,
        meta: { requiresGuest: true }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: Profile,
        meta: { requiresAuth: true }
      },
      {
        path: 'login-oauth2-result',
        name: 'LoginOauth2Result',
        component: LoginOauth2Result,
        props: (route) => ({ status: route.query.status, error: route.query.error })
      },
      {
        path: 'link-oauth2-result',
        name: 'LinkOauth2Result',
        component: LinkOauth2Result,
        props: (route) => ({ status: route.query.status, error: route.query.error })
      }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// 路由守衛
router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore();
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
  const requiresGuest = to.matched.some(record => record.meta.requiresGuest);
  
  // 檢查登入狀態，確保已經檢查
  if (authStore.isAuthenticated === null) {
    await authStore.checkLogin();
  }

  // 如果需要登入且未登入
  if (requiresAuth && !authStore.isAuthenticated) {
    localStorage.setItem('redirectAfterLoginUnauthorized', to.fullPath);
    next({ name: 'Login' });
  }
  // 如果需要訪客且已登入
  else if (requiresGuest && authStore.isAuthenticated) {
    next({ name: 'Home' });
  }
  // 如果是登入頁面
  else if (to.name === 'Login') {
    const redirectPath = localStorage.getItem('redirectAfterLoginUnauthorized');
    if (redirectPath) {
      localStorage.setItem('redirectAfterLogin', redirectPath);
      localStorage.removeItem('redirectAfterLoginUnauthorized');
    } else {
      localStorage.setItem('redirectAfterLogin', from.fullPath);
    }
    next();
  }
  else {
    next();
  }



});

export default router;
