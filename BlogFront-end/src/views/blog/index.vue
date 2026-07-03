<script setup>
  
import { ref, nextTick, onMounted, onUnmounted, watch } from 'vue'
import Cherry from 'cherry-markdown';
import 'cherry-markdown/dist/cherry-markdown.css';
import '@/assets/sass/themes/write-theme.scss'
import { useRoute } from 'vue-router'
import router from '../../router'
const route = useRoute()
const cherryInstance = ref(null)
const contentEl = ref(null)   // 详情卡片根节点，用于监听进场动画结束
const mdReady = ref(false)    // 正文(Cherry)是否已渲染，用于正文淡入
const textContent = ref("")
const previewImageUrl = ref("")
const isPreviewVisible = ref(false)
const previewScale = ref(1)
const BlogContentID = route.query.id
const title = ref("")
const createTime = ref("");
const tags = ref("");
const viewCount = ref("")
const imageUrl = ref("")
import '@/assets/sass/themes/black-theme.scss'


import { viewCountAddApi } from '@/api/blog'



import { BlogQueryApi } from '@/api/blog'

// 更新Cherry Markdown主题
const updateCherryTheme = () => {
  if (cherryInstance.value) {
    const isSunMode = document.documentElement.classList.contains('sun-mode')
    const newTheme = isSunMode ? 'write' : 'black'
    const newCodeBlockTheme = isSunMode ? 'one-light' : 'one-dark'
    cherryInstance.value.setTheme(newTheme)
    cherryInstance.value.setCodeBlockTheme(newCodeBlockTheme)
  }
}

// 监听明亮模式变化
onMounted(() => {
  // 使用MutationObserver监听明亮模式变化
  const observer = new MutationObserver((mutations) => {
    mutations.forEach((mutation) => {
      if (mutation.type === 'attributes' && mutation.attributeName === 'class') {
        const isSunMode = document.documentElement.classList.contains('sun-mode')
        updateCherryTheme()
      }
    })
  })

  // 配置观察器选项
  const config = {
    attributes: true,
    attributeFilter: ['class']
  }

  // 观察documentElement的类名变化
  observer.observe(document.documentElement, config)

  // 在组件卸载时停止观察
  onUnmounted(() => {
    observer.disconnect()
  })
})

const blogContent = async (id) => {
  const result = await BlogQueryApi(id)
  if (result.code) {
    textContent.value = result.data.content
    title.value = result.data.title
    createTime.value = result.data.createTime
    tags.value = result.data.tags
    viewCount.value = result.data.viewCount
    imageUrl.value = result.data.imageUrl
  }
  // 延后重型的 Cherry 初始化：它是同步的、会占住主线程，若在挂载即执行会吞掉进场动画。
  // 等 .content 进场动画结束后再初始化，让开场先顺滑播完。
  scheduleCherryRender()
}

// 初始化 / 更新 Cherry，并在就绪后触发正文淡入
const renderCherry = () => {
  if (!cherryInstance.value) {
    cherryInstance.value = new Cherry({
      id: 'markdown-container',
      value: textContent.value,
      editor: {
        defaultModel: "previewOnly",
      },
      previewer: {
        enablePreviewerBubble: false,
      },
    })
    updateCherryTheme()
    bindMarkdownImagePreview()
  } else {
    cherryInstance.value.setMarkdown(textContent.value)
    bindMarkdownImagePreview()
  }
  // 正文渲染完成后淡入（避免"空白→内容"硬跳）
  nextTick(() => { mdReady.value = true })
}

// 等 .content 进场动画结束后再渲染 Cherry；带兜底定时器，动画不存在也能触发
const scheduleCherryRender = () => {
  if (cherryInstance.value) {
    renderCherry()
    return
  }
  nextTick(() => {
    const el = contentEl.value
    let done = false
    const run = () => {
      if (done) return
      done = true
      el && el.removeEventListener('animationend', onEnd)
      renderCherry()
    }
    const onEnd = (e) => {
      if (e.target === el) run()   // 只认 .content 自身的进场动画
    }
    if (el) el.addEventListener('animationend', onEnd)
    setTimeout(run, 700)           // 兜底
  })
}

const openPreviewImage = (src) => {
  previewImageUrl.value = src
  previewScale.value = 1
  isPreviewVisible.value = true
}

const closePreviewImage = () => {
  isPreviewVisible.value = false
  previewImageUrl.value = ''
}

const onPreviewWheel = (event) => {
  const step = 0.1
  const minScale = 1
  const maxScale = 3
  const delta = event.deltaY > 0 ? -step : step
  previewScale.value = Math.min(maxScale, Math.max(minScale, previewScale.value + delta))
}

const bindMarkdownImagePreview = () => {
  nextTick(() => {
    const container = document.querySelector('#markdown-container')
    if (!container) return
    const imgs = container.querySelectorAll('img')
    imgs.forEach((img) => {
      img.style.cursor = 'zoom-in'
      if (!img.dataset.previewBound) {
        img.dataset.previewBound = 'true'
        img.addEventListener('click', () => {
          openPreviewImage(img.src)
        })
      }
    })
  })
}

// 浏览量增加
const viewCountAdd = async (id) => {
  const result = await viewCountAddApi(id)
  // if (result.code) {
  // }
}

onMounted(() => {
  blogContent(BlogContentID)
  viewCountAdd(BlogContentID)
})

// 返回:有浏览历史则回到来源页（保留主页搜索结果/归档滚动位置），否则回主页
const goBack = () => {
  if (window.history.state && window.history.state.back) {
    router.back()
  } else {
    router.push('/')
  }
}
</script>
<template>
  <div class="content" ref="contentEl" :class="{ 'md-ready': mdReady }">
    <div class="back-btn" @click="goBack">
      <el-icon><ArrowLeft /></el-icon>
      <span>返回</span>
    </div>
    <div class="title">{{ title }}</div>
    <div class="time">
      <el-icon class="icon"><Clock /></el-icon>
      {{createTime.substring(0, 10) }}
       <span>&nbsp;&nbsp;&nbsp;&nbsp;</span>
      #{{tags.split(',').slice(0, 4).join(' #') }}
      <span>&nbsp;&nbsp;&nbsp;&nbsp;</span>
      {{viewCount+1}} 次阅读
    </div>
    <hr class="line">
    <div class="image" v-if="imageUrl">
      <img :src="imageUrl"  @error="handleImageError">
    </div>

    <div id="markdown-container"></div>

    <div v-if="isPreviewVisible" class="image-preview-mask" @click="closePreviewImage">
      <img class="image-preview-img" :src="previewImageUrl" :style="{ transform: `scale(${previewScale})` }" @click.stop @wheel.prevent="onPreviewWheel">
    </div>
  </div>
</template>
<style scoped>

/* 返回按钮：定位在卡片左上角，不占额外行高 */
.back-btn {
  position: absolute;
  top: 12px;
  left: 15px;
  z-index: 2;
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 4px 12px;
  font-size: 14px;
  color: #a9b0b8;
  background-color: rgba(27, 27, 31, 0.7);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 20px;
  cursor: pointer;
  user-select: none;
  transition: color 0.3s ease, transform 0.3s ease, background-color 0.3s ease;
}

.back-btn:hover {
  color: #409EFF;
  transform: translateX(-3px);
  background-color: rgba(27, 27, 31, 0.9);
}

.back-btn .el-icon {
  margin: 0;
}

.sun-mode .back-btn {
  color: #4a5568;
  background-color: rgba(255, 255, 255, 0.7);
  border: 1px solid rgba(0, 0, 0, 0.1);
}

.sun-mode .back-btn:hover {
  color: #409EFF;
  background-color: rgba(255, 255, 255, 0.9);
}

  /* 明亮模式样式 */
.sun-mode .time {
  font-size: 14px;
  color: #888;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  text-align: center;
}

.sun-mode .icon {
  margin-right: 5px;
}
  .image img{
    width: 100%;
    height: 250px;
    object-fit: contain;
  }
.image {
  /* background-color: aliceblue; */
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
.line {
  margin: 10px 0 20px 0;
   color: #888;
}
.time {
  font-size: 14px;
  color: #888;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  text-align: center;
}
.icon {
  margin-right: 5px;
}

#markdown-container {
  width: 100%;
  /* height: 100%; */
  min-height: 1000px;
  box-sizing: border-box;
}

/* 确保markdown内容在手机端占满宽度 */
#markdown-container :deep(.cherry-markdown) {
  width: 100% !important;
  padding: 0 !important;
  margin: 0 !important;
}

#markdown-container :deep(.cherry-markdown__content) {
  width: 100% !important;
  max-width: 100% !important;
  padding: 0 !important;
}

#markdown-container :deep(img) {
  cursor: zoom-in;
}

/* 强制清除所有相关元素的边框和阴影 */
#markdown-container :deep(*) {
  /* border: none !important; */
  box-shadow: none !important;
  box-sizing: border-box;
}

.image-preview-mask {
  position: fixed;
  inset: 0;
  z-index: 9999;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.8);
}

.image-preview-img {
  max-width: 90%;
  max-height: 90%;
  object-fit: contain;
  border-radius: 8px;
  box-shadow: 0 0 30px rgba(0, 0, 0, 0.6);
  transition: transform 0.08s ease;
  transform-origin: center center;
}


.title {
  font-size: 35px;
  font-weight: 600;
  margin-bottom: 5px;
  text-align: center;
  margin-top: 8px;
  /* 左右留出返回键宽度，居中标题文字不会伸到左上角按钮下方 */
  padding: 0 96px;
  line-height: 1.5;
  letter-spacing: 1px;


}

.content {
  position: relative;
  min-height: 1200px;
  margin-left: 8px;
  background-color: rgba(27, 27, 31, 0.7);
  color: white;
  padding: 12px 15px 10px 15px;
  border-radius: 5px;
  margin: 5px 25px 10px 8px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.5);
  border: 1px solid rgba(255, 255, 255, 0.1);
  /* 进场：轻微放大 + 上浮 + 淡入，沉稳落位。backwards 填充 → 结束不残留 transform，
     以免困住内部 position:fixed 的图片预览遮罩 */
  animation: article-in 0.45s cubic-bezier(0.22, 0.61, 0.36, 1) backwards;
  transform-origin: top center;
  will-change: transform, opacity;
  /* display: flex; */
  /* flex-direction: column; */
  /* justify-content: space-between; */

}

@keyframes article-in {
  from {
    opacity: 0;
    transform: translateY(14px) scale(0.985);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

/* 正文体：进场时先留白，Cherry 就绪后淡入，避免"空白→内容"硬跳 */
#markdown-container {
  opacity: 0;
  transition: opacity 0.35s ease;
}
.content.md-ready #markdown-container {
  opacity: 1;
}

/* 减少动效偏好：退化为简单淡入，正文直接显示 */
@media (prefers-reduced-motion: reduce) {
  .content {
    animation: article-fade 0.3s ease backwards;
  }
  #markdown-container {
    transition: none;
  }
}
@keyframes article-fade {
  from { opacity: 0; }
  to { opacity: 1; }
}

/* 明亮模式样式 */
.sun-mode .content {
  background-color: rgba(255, 255, 255, 0.7);
  color: #2d3748;
  border: 1px solid rgba(0, 0, 0, 0.1);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

/* 响应式布局 */
@media (max-width: 768px) {
  /* 平板端适配 */
  .content {
    margin: 5px 8px 10px 8px;
    padding: 12px 15px 10px 15px;
  }

  /* 移动端标题左对齐，返回键恢复正常流以免与标题重叠 */
  .back-btn {
    position: static;
    margin-bottom: 8px;
  }

  .title {
    font-size: 28px;
    line-height: 1.4;
    /* 移动端返回键在正常流中，标题无需额外留白，占满宽度 */
    margin-top: 10px;
    padding: 0;
  }
  
  .time {
    font-size: 13px;
    flex-wrap: wrap;
    gap: 8px;
  }
  
  .time span {
    display: none;
  }
  
  .image img {
    height: auto;
    max-height: 300px;
  }
}

@media (max-width: 480px) {
  /* 手机端适配 */
  .content {
    margin: 5px 4px 10px 4px !important;
    padding: 10px 12px 8px 12px;
  }
  
  .title {
    font-size: 22px;
    line-height: 1.3;
    text-align: left;
  }
  
  .time {
    font-size: 12px;
    flex-direction: column;
    align-items: flex-start;
    gap: 4px;
  }
  
  .image img {
    height: auto;
    max-height: 250px;
  }
  
  .line {
    margin: 8px 0 15px 0;
  }
}
</style>
