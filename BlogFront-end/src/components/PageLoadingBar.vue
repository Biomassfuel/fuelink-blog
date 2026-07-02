<script setup>
import { loadingProgress, loadingVisible } from '@/composables/useLoadingBar'
</script>

<template>
  <transition name="loadingbar-fade">
    <div v-show="loadingVisible" class="page-loading-bar">
      <div
        class="page-loading-bar__inner"
        :style="{ width: loadingProgress + '%' }"
      >
        <div class="page-loading-bar__glow"></div>
      </div>
    </div>
  </transition>
</template>

<style scoped>
.page-loading-bar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 3px;
  z-index: 20000;
  pointer-events: none;
  background: transparent;
}

.page-loading-bar__inner {
  height: 100%;
  /* 与站点主色一致：绿 -> 蓝 渐变 */
  background: linear-gradient(90deg, #10b981, #409EFF);
  border-radius: 0 3px 3px 0;
  box-shadow: 0 0 10px rgba(64, 158, 255, 0.6), 0 0 5px rgba(16, 185, 129, 0.5);
  transition: width 0.25s ease;
  position: relative;
}

/* 进度条头部的高光，营造流动感 */
.page-loading-bar__glow {
  position: absolute;
  right: 0;
  top: 0;
  height: 100%;
  width: 80px;
  border-radius: 3px;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.6));
  opacity: 0.8;
}

/* 进度条整体的淡入淡出 */
.loadingbar-fade-enter-active,
.loadingbar-fade-leave-active {
  transition: opacity 0.3s ease;
}

.loadingbar-fade-enter-from,
.loadingbar-fade-leave-to {
  opacity: 0;
}
</style>
