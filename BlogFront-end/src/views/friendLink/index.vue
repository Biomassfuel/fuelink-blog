
<script setup>
import { computed } from 'vue'
import siteConfig from '@/config/siteConfig'

const friendMailUrl = computed(() => {
  const subject = encodeURIComponent('友链申请')
  const body = encodeURIComponent('title: 名称\nimgurl: 头像地址\ndesc: 简介\nsiteurl: 博客地址')
  return `mailto:${siteConfig.contact.email}?subject=${subject}&body=${body}`
})
</script>
<template>
  <div class="friend-link-container">
   <span class="friend-link-title">&友链申请规则</span>
   <br/>
   <span class="friend-link-content">欢迎大佬们添加友链，目前有以下几个要求:</span>
   <ul class="friend-link-list">
     <li class="friend-link-item">您的站点在大陆地区需能正常访问，同时能维持站点的稳定运行</li>
     <li class="friend-link-item">您的网站需要有可读的内容，不接受空壳网站的友链</li>
     <li class="friend-link-item">如站点有头像，请保证图片能长期稳定地打开</li>
     <li class="friend-link-item">您的站点需要保持本站的友链</li>
     <li class="friend-link-item">您的站点不可含有违规的内容</li>
   </ul>
   <span class="friend-link-title">&友链申请</span>
   <br/>
   <span class="friend-link-content">请在您的网站添加我的友链后，将您的网站信息发送至我的邮箱</span>
   <br/>
   <span class="friend-link-content">我的邮箱地址为：</span>
     <a class="friend-link-content-a" :href="friendMailUrl">{{ siteConfig.contact.email }}</a>
  <br/>
  <span class="friend-link-content">友链格式如下:</span>
  <ul class="friend-link-list"> 
  <li class="friend-link-item">title: 您的站点名称</li>
  <li class="friend-link-item">imgurl: 您的站点头像URL</li>
  <li class="friend-link-item">desc: 您的站点描述</li>
  <li class="friend-link-item">siteurl: 您的站点URL</li>
  </ul>
  <span class="friend-link-title">&我的友链信息:</span>
  <ul class="friend-link-list"> 
  <li class="friend-link-item">名称: {{ siteConfig.friendLink.title }}</li>
  <li class="friend-link-item">头像: {{ siteConfig.friendLink.avatarUrl }}</li>
  <li class="friend-link-item">描述: {{ siteConfig.friendLink.description }}</li>
  <li class="friend-link-item">地址: {{ siteConfig.friendLink.siteUrl }}</li>
  </ul>
  </div>
</template>


<style scoped>
.friend-link-container {
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
.friend-link-title {
  font-size: 36px;
  color: #fcfdff;
  margin-bottom: 30px;
  letter-spacing: 1px;
}
.friend-link-content {
  font-size: 20px;
  color: #b1b5bd;
  margin-bottom: 20px;
}
.friend-link-list {
  font-size: 18px;
  color: #cacfda;
  margin-bottom: 20px;
}
.friend-link-item {
  margin-bottom: 10px;
}
.friend-link-content-a {
  font-size: 20px;
  color: #5caaf4;
  margin-bottom: 20px;
  text-decoration: none;
}
.friend-link-content-a:hover {
  /* text-decoration: underline; */
  color: #8ab4f8;
}

.sun-mode {
  /* 全局明亮模式样式 */
  --background-color: #f5f5f5;
  --text-color: #2d3748;
  --link-color: #1a73e8;
  --border-color: rgba(0, 0, 0, 0.05);
  --shadow-color: rgba(0, 0, 0, 0.1);
}

.sun-mode .friend-link-container {
  background-color: rgba(255, 255, 255, 0.5);
  color: #2d3748;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.sun-mode .friend-link-title {
  color: #2d3748;
}

.sun-mode .friend-link-content {
  color: #4a5568;
}

.sun-mode .friend-link-list {
  color: #4a5568;
}

.sun-mode .friend-link-content-a {
  color: #1a73e8;
}

.sun-mode .friend-link-content-a:hover {
  color: #0d47a1;
}

/* 响应式布局 */
@media (max-width: 768px) {
  /* 平板端适配 */
  .friend-link-container {
    margin: 5px 8px 10px 8px;
    padding: 15px 18px 25px 18px;
  }
  
  .friend-link-title {
    font-size: 28px;
    margin-bottom: 20px;
  }
  
  .friend-link-content {
    font-size: 16px;
    margin-bottom: 15px;
  }
  
  .friend-link-list {
    font-size: 15px;
    margin-bottom: 15px;
  }
  
  .friend-link-item {
    margin-bottom: 12px;
  }
  
  .friend-link-content-a {
    font-size: 16px;
  }
}

@media (max-width: 480px) {
  /* 手机端适配 */
  .friend-link-container {
    margin: 5px 4px 10px 4px !important;
    padding: 12px 15px 20px 15px !important;
  }
  
  .friend-link-title {
    font-size: 22px;
    margin-bottom: 15px;
  }
  
  .friend-link-content {
    font-size: 14px;
    margin-bottom: 12px;
  }
  
  .friend-link-list {
    font-size: 13px;
    margin-bottom: 12px;
    padding-left: 18px;
  }
  
  .friend-link-item {
    margin-bottom: 10px;
  }
  
  .friend-link-content-a {
    font-size: 14px;
    word-break: break-all;
  }
}
</style>
