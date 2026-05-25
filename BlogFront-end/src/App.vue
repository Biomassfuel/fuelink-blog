<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'

const scrollbarRef = ref(null)
const showBacktop = ref(false)
let hideTimer = null

// 将滚动事件传递给 layout 组件
const handleScroll = (e) => {
  // 显示回到顶部按钮
  showBacktop.value = true
  
  // 清除之前的定时器
  if (hideTimer) {
    clearTimeout(hideTimer)
  }
  
  // 设置新的定时器，4秒后隐藏
  hideTimer = setTimeout(() => {
    showBacktop.value = false
  }, 4000)
  
  // 通过事件总线或 provide/inject 传递给 layout 组件
  window.dispatchEvent(new CustomEvent('customScroll', { detail: e }))
}

onBeforeUnmount(() => {
  if (hideTimer) {
    clearTimeout(hideTimer)
  }
})

</script>

<template>
  <el-scrollbar height="100vh" ref="scrollbarRef" @scroll="handleScroll">
     <div id="app">
    <router-view></router-view> 
  </div>
  <transition name="fade" style="background-color: transparent;">
      <el-backtop 
        v-show="showBacktop"
        :visibility-height="200" 
        target=".el-scrollbar__wrap"
        class="custom-backtop"
      >
        <div class="backtop-content">
          <!-- <el-icon><Top /></el-icon> -->
           <span>∧</span>
        </div>
      </el-backtop>
    </transition>

  </el-scrollbar>
 
</template>

<style>
/* 过渡动画 */
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}

.custom-backtop {
  width: 40px !important;
  height: 40px !important;
  z-index: 9999 !important;
}

.backtop-content {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #409EFF, #306cce);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  box-shadow: 0 2px 12px 0 rgba(64, 158, 255, 0.3);
  transition: all 0.3s ease;
}

.backtop-content:hover {
  /* background: linear-gradient(135deg, #306cce, #409EFF); */
  /* transform: translateY(-2px); */
  /* 放大 */
  transform: translateY(-2px) scale(1.1);
  box-shadow: 0 4px 16px 0 rgba(64, 158, 255, 0.4);
}

.backtop-content .el-icon {
  font-size: 20px;
  font-weight: bold;
}

/* 明亮模式适配 */
.sun-mode .backtop-content {
  background: linear-gradient(135deg, #409EFF, #306cce);
  box-shadow: 0 2px 12px 0 rgba(64, 158, 255, 0.3);
}

.sun-mode .backtop-content:hover {
  background: linear-gradient(135deg, #306cce, #409EFF);
  box-shadow: 0 4px 16px 0 rgba(64, 158, 255, 0.4);
}


/* 全局样式  */
html, body {
  margin: 0 ;
  padding: 0 ;
  height: 100% ;
  width: 100% ;
  /* overflow-x: hidden ; */
  background-image: url('@/assets/bg.jpg');
  /* cover 背景图片铺满整个屏幕 */
  background-size: cover;

  background-attachment: fixed; /* 确保背景不随滚动移动 */
  
}
html::before {
  content: "";
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}


#app {
  /* margin: 0 ;
  padding: 0 ;
  height: 100vh ;
  width: 100% ; */
  
  /* position: relative;
  overflow: auto;
  background-color: rgba(0, 0, 0, 0.3);
  z-index: 0; */
   background-color: rgba(0, 0, 0, 0.3);
   /* overflow: auto; */
   /* height: 100%; */
   
}

.sun-mode #app{
  background-color: transparent;
}


/* 直接在App.vue中定义字体 */
@font-face {
  font-family: 'LXGW WenKai Screen'; /* 与模板中使用的名称一致 */
  src: url('@/assets/fonts/LXGWWenKaiScreen.ttf') format('truetype');
  font-weight: 400;
  font-style: normal;
  font-display: swap;
}

/* 全局应用字体 */
* {
  font-family: 'LXGW WenKai Screen', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
}

/* 或者只应用于特定元素 */
body {
  font-family: 'MyCustomFont', -apple-system, BlinkMacSystemFont, sans-serif;
  font-size: 16px;
  line-height: 1.6;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

/* 标题字体 */
h1, h2, h3, h4, h5, h6 {
  font-family: 'MyCustomFont', sans-serif;
  font-weight: 700;
  line-height: 1.2;
}
</style>