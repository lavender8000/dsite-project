// vite.config.js
import { defineConfig } from "file:///D:/WorkFiles/LearningWebsite/Project_Dsite/frontend/dsite/node_modules/vite/dist/node/index.js";
import vue from "file:///D:/WorkFiles/LearningWebsite/Project_Dsite/frontend/dsite/node_modules/@vitejs/plugin-vue/dist/index.mjs";
import { resolve } from "path";
var __vite_injected_original_dirname = "D:\\WorkFiles\\LearningWebsite\\Project_Dsite\\frontend\\dsite";
var vite_config_default = defineConfig({
  server: {
    port: 443,
    https: {
      key: "./ssl/key.pem",
      cert: "./ssl/cert.pem"
    }
  },
  plugins: [vue()],
  resolve: {
    alias: {
      "@": resolve(__vite_injected_original_dirname, "src")
    }
  }
});
export {
  vite_config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5jb25maWcuanMiXSwKICAic291cmNlc0NvbnRlbnQiOiBbImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJEOlxcXFxXb3JrRmlsZXNcXFxcTGVhcm5pbmdXZWJzaXRlXFxcXFByb2plY3RfRHNpdGVcXFxcZnJvbnRlbmRcXFxcZHNpdGVcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZmlsZW5hbWUgPSBcIkQ6XFxcXFdvcmtGaWxlc1xcXFxMZWFybmluZ1dlYnNpdGVcXFxcUHJvamVjdF9Ec2l0ZVxcXFxmcm9udGVuZFxcXFxkc2l0ZVxcXFx2aXRlLmNvbmZpZy5qc1wiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9pbXBvcnRfbWV0YV91cmwgPSBcImZpbGU6Ly8vRDovV29ya0ZpbGVzL0xlYXJuaW5nV2Vic2l0ZS9Qcm9qZWN0X0RzaXRlL2Zyb250ZW5kL2RzaXRlL3ZpdGUuY29uZmlnLmpzXCI7aW1wb3J0IHsgZGVmaW5lQ29uZmlnIH0gZnJvbSAndml0ZSc7XG5pbXBvcnQgdnVlIGZyb20gJ0B2aXRlanMvcGx1Z2luLXZ1ZSc7XG5pbXBvcnQgeyByZXNvbHZlIH0gZnJvbSAncGF0aCc7XG5cbi8vIGh0dHBzOi8vdml0ZWpzLmRldi9jb25maWcvXG5leHBvcnQgZGVmYXVsdCBkZWZpbmVDb25maWcoe1xuICBzZXJ2ZXI6IHtcbiAgICBwb3J0OiA0NDMsXG4gICAgaHR0cHM6IHtcbiAgICAgIGtleTogJy4vc3NsL2tleS5wZW0nLFxuICAgICAgY2VydDogJy4vc3NsL2NlcnQucGVtJyxcbiAgICB9XG4gIH0sXG4gIHBsdWdpbnM6IFt2dWUoKV0sXG4gIHJlc29sdmU6IHtcbiAgICBhbGlhczoge1xuICAgICAgJ0AnOiByZXNvbHZlKF9fZGlybmFtZSwgJ3NyYycpLFxuICAgIH0sXG4gIH0sXG59KTtcbiJdLAogICJtYXBwaW5ncyI6ICI7QUFBeVcsU0FBUyxvQkFBb0I7QUFDdFksT0FBTyxTQUFTO0FBQ2hCLFNBQVMsZUFBZTtBQUZ4QixJQUFNLG1DQUFtQztBQUt6QyxJQUFPLHNCQUFRLGFBQWE7QUFBQSxFQUMxQixRQUFRO0FBQUEsSUFDTixNQUFNO0FBQUEsSUFDTixPQUFPO0FBQUEsTUFDTCxLQUFLO0FBQUEsTUFDTCxNQUFNO0FBQUEsSUFDUjtBQUFBLEVBQ0Y7QUFBQSxFQUNBLFNBQVMsQ0FBQyxJQUFJLENBQUM7QUFBQSxFQUNmLFNBQVM7QUFBQSxJQUNQLE9BQU87QUFBQSxNQUNMLEtBQUssUUFBUSxrQ0FBVyxLQUFLO0FBQUEsSUFDL0I7QUFBQSxFQUNGO0FBQ0YsQ0FBQzsiLAogICJuYW1lcyI6IFtdCn0K
