<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import siteConfig from '@/config/siteConfig'

const iconTheme = ref('dark')
const skillIconNames = siteConfig.skillsIcon.names.join(',')
const skillIconUrl = (theme) => `${siteConfig.skillsIcon.apiUrl}?i=${skillIconNames}&perline=${siteConfig.skillsIcon.perLine}&theme=${theme}`

const updateIconTheme = () => {
  const isSunMode = document.documentElement.classList.contains('sun-mode') || 
                   document.getElementById('app')?.classList.contains('sun-mode')
  iconTheme.value = isSunMode ? 'light' : 'dark'
}

onMounted(() => {
  // 初始设置图标主题
  updateIconTheme()
  
  // 监听主题切换
  const observer = new MutationObserver(() => {
    updateIconTheme()
  })
  
  observer.observe(document.documentElement, {
    attributes: true,
    attributeFilter: ['class']
  })
  
  // 同时观察app元素
  const appElement = document.getElementById('app')
  if (appElement) {
    observer.observe(appElement, {
      attributes: true,
      attributeFilter: ['class']
    })
  }
  
  onBeforeUnmount(() => {
    observer.disconnect()
  })
})
</script>

<template>
  <div class="about">
    <div>
      <span class="title">About</span>
    </div>

    <br />
    <span class="titleT">概述</span>
    <ul class="aboutList">
      <li>本网站是一个动态网站，采用前后端分离的分布式架构，并使用CC BY-NC-SA 4.0协议开源于 GitHub</li>
      <li>后端：Spring Boot 3.5.8 + MyBatis + MySQL 8.0.33</li>
      <li>前端：Vue 3.4.21 + Element Plus 2.4.2</li>
      <li>其他：Element Plus UI 组件库 + Cherry Markdown 富文本编辑器 + 阿里云 OSS 图片存储 + JWT 认证 + 分页插件 PageHelper</li>
    </ul>
    <span class="titleT">关于我</span>
    <ul class="aboutList">
      <li v-for="line in siteConfig.about.introLines" :key="line">{{ line }}</li>
      <li>QQ: {{ siteConfig.contact.qq }}</li>
      <li>Email: {{ siteConfig.contact.email }}</li>
    </ul>
    <span class="titleT">project</span>
    <ul class="aboutList"> 
      <li>广告位出租</li>
      <li>广告位出租</li>
    </ul>
    <br/>
    
    <span class="titleT" style="font-size: 35px;">skills</span>
    <p align="center" class="skills-container">
      <img
        :src="skillIconUrl(iconTheme)" 
        class="skills-image"
      />
    </p>
  </div>
</template>

<style scoped>
.about {
  min-height: 1000px;
  margin-left: 8px;
  background-color: rgba(27, 27, 31, 0.7);
  color: white;
  padding: 20px 25px 30px 25px;
  border-radius: 5px;
  margin: 5px 25px 10px 8px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.5);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.title {
  font-size: 30px;
  font-weight: bold;
}

.titleT {
  font-size: 20px;
  font-weight: bold;
}

.aboutList {
  font-size: 18px;
}

.aboutList li {
  margin: 5px 0;
  color: #d1d5db;
}

/* 明亮模式样式 */
.sun-mode .about {
  background-color: rgba(255, 255, 255, 0.5);
  color: #2d3748;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.sun-mode .title {
  color: #2d3748;
}

.sun-mode .titleT {
  color: #2d3748;
}

.sun-mode .aboutList {
  color: #4a5568;
}
.sun-mode .aboutList li {
  color: #4a5568;
}

/* Skills 容器样式 */
.skills-container {
  max-width: 100%;
  overflow: hidden;
  padding: 0 10px;
}

.skills-image {
  max-width: 100%;
  height: auto;
  display: block;
  margin: 0 auto;
}

/* 响应式布局 */
@media (max-width: 768px) {
  /* 平板端适配 */
  .about {
    margin: 5px 8px 10px 8px;
    padding: 15px 18px 25px 18px;
  }
  
  .title {
    font-size: 26px;
  }
  
  .titleT {
    font-size: 18px;
  }
  
  .aboutList {
    font-size: 16px;
  }
  
  .aboutList li {
    margin: 8px 0;
  }
  
  img {
    max-width: 100%;
    height: auto;
  }
  
  /* 平板端skills图片适配 */
  .skills-container {
    padding: 0 5px;
  }
  
  .skills-image {
    max-width: 100%;
    height: auto;
  }
}

@media (max-width: 480px) {
  /* 手机端适配 */
  .about {
    margin: 5px 4px 10px 4px !important;
    padding: 12px 15px 20px 15px !important;
  }
  
  .title {
    font-size: 22px;
  }
  
  .titleT {
    font-size: 16px;
  }
  
  .titleT[style*="font-size: 35px"] {
    font-size: 20px !important;
  }
  
  .aboutList {
    font-size: 14px;
    padding-left: 18px;
  }
  
  .aboutList li {
    margin: 6px 0;
  }
  
  img {
    max-width: 100%;
    height: auto;
  }
}
</style>
