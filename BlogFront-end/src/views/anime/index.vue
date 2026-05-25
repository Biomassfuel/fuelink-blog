<script setup>

import { ref, watch, onMounted, onUnmounted } from 'vue'
import siteConfig from '@/config/siteConfig'
const List = ref([])
const watchingListLength = ref(0)
const WatchedListLength = ref(0)
const WantListLength = ref(0)
const DroppedListLength = ref(0)
const NoWatchingListLength = ref(0)

const WatchingList = ref([])
const WantList = ref([])
const WatchedList = ref([])
const DroppedList = ref([])
const NoWatchingList = ref([])
const list = ref([])

const getList = async () => {
  const params = new URLSearchParams({
    limit: siteConfig.bangumi.collectionsLimit,
    offset: siteConfig.bangumi.collectionsOffset
  })
  const res = await fetch(`${siteConfig.bangumi.apiBaseUrl}/v0/users/${siteConfig.bangumi.userId}/collections?${params}`)
  const data = await res.json()
  List.value = data.data
  ListFunction()

}
const ListFunction = () => {
  watchingListLength.value = List.value.filter(item => item.type === 3).length
  WatchedListLength.value = List.value.filter(item => item.type === 2).length
  WantListLength.value = List.value.filter(item => item.type === 1).length
  DroppedListLength.value = List.value.filter(item => item.type === 4).length
  NoWatchingListLength.value = List.value.filter(item => item.type === 5).length
  for (let i = 0; i < List.value.length; i++) {
    const element = List.value[i];
    if (element.type === 3) {
      WatchingList.value.push(element)
    } else if (element.type === 2) {
      WatchedList.value.push(element)
    } else if (element.type === 1) {
      WantList.value.push(element)
    } else if (element.type === 4) {
      DroppedList.value.push(element)
    } else if (element.type === 5) {
      NoWatchingList.value.push(element)
    }
  }
  AllList()
}

const AllList = () => { 
  list.value =List.value
}
const WatchingListFunction = () => { 
  list.value =WatchingList.value
}
const WatchedListFunction = () => { 
  list.value =WatchedList.value
}
const WantListFunction = () => { 
  list.value =WantList.value
}
const DroppedListFunction = () => { 
  list.value =DroppedList.value
}
const NoWatchingListFunction = () => { 
  list.value =NoWatchingList.value
}

onMounted(() => {
  getList()
})

const handleClick = (item) => {
  window.open(`${siteConfig.bangumi.subjectBaseUrl}${item.subject_id}`, '_blank')
}

</script>


<template>
  <div class="content">
    <span class="titleName"> 番剧列表 </span>
    <br/>
    <span class="description"> 记录我的二次元之旅 </span>
    <div class="btn-container"> 
      <el-button class="btn" @click="AllList" size="large"> 全部（{{ List.length }}） </el-button>
    <el-button class="btn" @click="WatchingListFunction" size="large"> 在看（{{ watchingListLength }}） </el-button>
    <el-button class="btn" @click="WatchedListFunction" size="large"> 看过（{{ WatchedListLength }}） </el-button>
    <el-button class="btn" @click="WantListFunction" size="large"> 想看（{{ WantListLength }}） </el-button>
    <el-button class="btn" @click="DroppedListFunction" size="large"> 搁置（{{ DroppedListLength }}） </el-button>
    <el-button class="btn" @click="NoWatchingListFunction" size="large"> 抛弃（{{ NoWatchingListLength }}） </el-button>
    </div>
    
    <div class="item-container">
      <div class="item" v-for="item in list" :key="item.id" @click="handleClick(item)">
        <div class="img-container">
          <img class="img" :src="item.subject.images.large" alt="">
        </div>
        <div style="margin-top: 5px; margin-left: 10px;">
          <span class="title">{{ item.subject.name_cn || item.subject.name }}</span>
        </div>
        <div style=" margin-left: 10px; margin-right: 10px;">
          <span class="summary">{{ item.subject.short_summary }}</span>
        </div>
        <div class="year-container">
          <span class="year-label">年份</span>
          <span class="year">{{ item.subject.date || '未知' }}</span>
        </div>
        <div class="eps-container">
          <span class="eps-label">总集数</span>
          <span class="eps">{{ item.subject.eps ? item.subject.eps+'集' : '未知' }}</span>
        </div>
        <div class="tags-container">
          <el-tag class="tag" v-for="tag in item.tags" :key="tag.id"  size="small">{{ tag }}</el-tag>
        </div>


      </div>
    </div>

  </div>
</template>

<style scoped>
  .description{
    font-size: 18px;
    color: #c2c2b8;
    margin-left: 10px;
    letter-spacing: 1.5px;
  }
  .titleName{
    font-size: 35px;
    color: #fcfdff;
    /* font-weight: bold; */
    margin-left: 10px;
  }

  .btn-container {
  margin: 20px 0 0 10px;
}
  .tag {
  margin: 5px 5px 0 0;
  background-color: #452e35;
  color: #d3d3ca;
  border: none;
}

  .tags-container {
  margin: 5px 10px 0 10px;
}

  .eps-container {
  margin: 2px 10px 0 10px;
  display: flex;
  justify-content: space-between;
}

.eps-label,
.eps {
  font-size: 14px;
  color: #d3d3ca;
}

.year-container {
  margin: 5px 10px 0 10px;
  display: flex;
  justify-content: space-between;
}

.year-label,
.year {
  font-size: 14px;
  color: #d3d3ca;
}

.summary {
  font-size: 12px;
  color: #c2c2b8;
  /* margin-top: 20px; */
  /* 最多显示两行，超出部分省略号 */
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.title {
  font-size: 15px;
  color: #fcfdff;
  /* margin-top: 20px; */
}

.item-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  margin: 20px 0;

}

.img-container {
  /* width: 250px; */
  height: 375px;
  /* background-color: aqua; */
}

.item {
  width: 250px;
  margin: 10px;
  height: 540px;
  background-color: rgba(20, 20, 24, 0.7);
  border-radius: 15px;
  transition: transform 0.3s ease, box-shadow 0.7s ease;
}

.item:hover {
  box-shadow: 0 6px 20px rgba(255, 255, 255, 0.8);
  cursor: pointer;
  transform: translateY(-15px);
}
.img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 15px 15px 0 0;
}

.btn {
  padding: 0 12px;
  min-width: 80px;
  margin: 0 2px 0 0;
  background-color: #3f4958;
  color: #fcfdff;
  border: 1px solid #374151;
  transition: all 0.3s ease;
}

.btn:hover {
  background-color: #4a5464;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}
.content {
  /* min-height: 1200px; */
  margin-left: 8px;
  background-color: rgba(27, 27, 31, 0.7);
  color: white;
  padding: 12px 15px 10px 15px;
  border-radius: 5px;
  margin: 5px 25px 10px 8px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.5);
  border: 1px solid rgba(255, 255, 255, 0.1);
  /* display: flex; */
  /* flex-direction: column; */
  /* justify-content: space-between; */

}


/* 明亮模式适配 - 修复阴影问题 */
.sun-mode .description {
  color: #4a5568;
}

.sun-mode .titleName {
  color: #2d3748;
}

.sun-mode .tag {
  background-color: rgba(255, 255, 255, 0.5);
  color: #2d3748;
  border: 1px solid rgba(0, 0, 0, 0.05);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.sun-mode .eps-label,
.sun-mode .eps,
.sun-mode .year-label,
.sun-mode .year {
  color: #4a5568;
}

.sun-mode .summary {
  color: #718096;
}

.sun-mode .title {
  color: #2d3748;
}

.sun-mode .item {
  background-color: rgba(255, 255, 255, 0.5);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  /* border: 1px solid rgba(0, 0, 0, 0.05); */
}

.sun-mode .item:hover {
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.2);
}

.sun-mode .btn {
  background-color: #e2e8f0;
  color: #2d3748;
  border: 1px solid #cbd5e0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.sun-mode .btn:hover {
  background-color: #cbd5e0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.sun-mode .content {
  background-color: rgba(255, 255, 255, 0.5);
  color: #2d3748;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(0, 0, 0, 0.05);
}

/* 响应式布局 */
@media (max-width: 768px) {
  /* 平板端适配 */
  .content {
    margin: 5px 8px 10px 8px;
    padding: 10px 12px 8px 12px;
  }
  
  .titleName {
    font-size: 28px;
  }
  
  .description {
    font-size: 16px;
  }
  
  .btn-container {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    margin: 15px 0 0 0;
  }
  
  .btn {
    margin: 5px;
    min-width: auto;
    padding: 0 10px;
  }
  
  .item {
    width: calc(50% - 20px);
    height: auto;
    min-height: 500px;
  }
  
  .img-container {
    height: 270px;
  }
  
  .item:hover {
    transform: none;
    box-shadow: none;
  }
}

@media (max-width: 480px) {
  /* 手机端适配 */
  .content {
    margin: 5px 4px 10px 4px !important;
    padding: 8px 10px 6px 10px !important;
  }
  
  .titleName {
    font-size: 24px;
  }
  
  .description {
    font-size: 14px;
  }
  
  .btn {
    margin: 3px;
    padding: 0 8px;
    font-size: 12px;
  }
  
  .item {
    width: calc(100% - 20px);
    min-height: 520px;
  }
  
  .img-container {
    height: 380px;
  }
  
  .img {
    width: 100%;
    height: 100%;
    object-fit: fill;
  }
}
</style>
