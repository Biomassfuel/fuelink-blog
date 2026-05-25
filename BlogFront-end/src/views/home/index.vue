<script setup>
import ElementPlus from 'element-plus'
import { onMounted, ref, watch, onBeforeUnmount } from 'vue'
import { allBlogQueryApi } from '@/api/home'
import {useRoute} from 'vue-router'
import router from '../../router'
const route = useRoute()
// 查询
const searchForm = ref({
  titleSearch:'',
  tagId: ''
})
watch(() => route.query.titleSearch, (newValue) => {
   searchForm.value.titleSearch = newValue || ''
  handleSearch()
})
//标签筛选
watch(() => route.query.tagId, (newValue) => {
  searchForm.value.tagId = newValue || ''
  handleSearch()
})
const handleImageError = (event) => {
  event.target.style.display = 'none';
}
// 分页配置
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const BlogList = ref([])



const handleSearch = async () => {
  const result = await allBlogQueryApi(
    
    searchForm.value.titleSearch,
    searchForm.value.tagId,
    currentPage.value,
    pageSize.value
  );
  if (result.code) {
    BlogList.value = result.data.row
    total.value = result.data.total
  }
}

// 重置
const handleReset = () => {
  searchForm.value = {
    titleSearch: '',
    tagId: ''
  }
  handleSearch()
}


//分页组件
const pagination = ref({ currentPage: 1, pageSize: 10, total: 0 })

//当前页码发生变化时触发
const handleCurrentChange = (page) => {
  currentPage.value = page
  handleSearch()
}


onMounted(() => {
  // 组件挂载时检查路由参数
  searchForm.value.titleSearch = route.query.titleSearch || ''
  searchForm.value.tagId = route.query.tagId || ''
  handleSearch()
});
// 监听搜索框searchKeyword变化搜索
// watch(() => searchKeyword, (newValue) => {
//   searchForm.value.titleSearch = newValue
//   handleSearch()
// })

// 在组件卸载前清空搜索
onBeforeUnmount(() => {
  // 清空本地搜索参数
  searchForm.value.titleSearch = ''
  searchForm.value.tagId = ''
  // 通过事件总线通知 layout 清空搜索框和标签
  window.dispatchEvent(new CustomEvent('clear-search'))
})

// 监听路由变化，当离开首页时清空搜索
watch(() => route.path, (newPath) => {
  if (newPath !== '/') {
    // 清空本地搜索参数
    searchForm.value.titleSearch = ''
    searchForm.value.tagId = ''
    // 通过事件总线通知 layout 清空搜索框和标签
    window.dispatchEvent(new CustomEvent('clear-search'))
  }
})

const handleClick = (item) => {

  router.push({
    path: '/front/blog',
    query: {
      id: item.id
    }
  })
}

</script>
<template>
  <div v-for="item in BlogList" :key="item" class="contentRouter" @click="handleClick(item)">
    <div style="display: flex; flex-direction: column; width: 85%;">
       <div class="blog-title-container">
        <span v-if="item.isTop" class="top-indicator">置顶</span>
        <span class="title">{{ item.title }}</span>
      </div>
      <span class="subTitle">{{ item.summary }}</span>

      <div style="display: flex; flex-wrap: wrap;">

        <span class="subTimeAndAuthor">
          <el-icon style="vertical-align: middle;">
            <Calendar />
          </el-icon>
          {{ item.createTime.substring(0, 10) }}&nbsp;&nbsp;
        </span>
        <span class="subTimeAndAuthor">
          #{{ item.tags.split(',').slice(0, 4).join(' #') }}
          
        </span><span v-if="item.tags.split(',').length > 4" class="subTimeAndAuthor">&nbsp;#...</span>
      </div>
    </div>

    <div style="display: flex; flex-direction: column; justify-content: center;">
      <img :src="item.imageUrl"  class="img"  @error="handleImageError" />
    </div>
  </div>

  <div class="page">
    <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" size="small" :disabled="false"
      :background="true" :pager-count="5" layout="prev, pager, next, jumper" :total="total"
      @current-change="handleCurrentChange" />
  </div>

</template>
<style scoped>
  .blog-title-container {
  display: flex;
  align-items: center;
}

.top-indicator {
  background-color: #ff6b6b; 
  color: white; 
  padding: 2px 6px; 
  border-radius: 4px; 
  font-size: 12px; 
  margin-right: 8px; 
  flex-shrink: 0; 
  box-shadow: 0 2px 4px rgba(0,0,0,0.2); 
}
.sun-mode .top-indicator {
  background-color: #e74c3c; 
  color: white;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1); 
}

.page {
  display: flex;
  justify-content: center;
  padding-top: 10px;
}

.page .el-pagination {
  background-color: transparent;
}

/* 明亮模式下的分页样式 */
.sun-mode .page ::v-deep(.el-pagination) {
  --el-pagination-button-color: #4a5568;
  --el-pagination-button-disabled-color: #a0aec0;
  --el-pagination-hover-color: #409EFF;
}

.sun-mode .page ::v-deep(.el-pagination .btn-next),
.sun-mode .page ::v-deep(.el-pagination .btn-prev) {
  color: #4a5568;
  background-color: rgba(255, 255, 255, 0.7);
  border: 1px solid rgba(0, 0, 0, 0.1);
}

.sun-mode .page ::v-deep(.el-pagination .el-pager li) {
  color: #4a5568;
  background-color: rgba(255, 255, 255, 0.7);
}

/* 暗色模式下的分页样式 */
.page ::v-deep(.el-pagination) {
  --el-pagination-button-color: #a9b0b8;
  --el-pagination-button-disabled-color: #4a5568;
  --el-pagination-hover-color: #409EFF;
}

.page ::v-deep(.el-pagination .btn-next),
.page ::v-deep(.el-pagination .btn-prev) {
  color: #a9b0b8;
  background-color: rgba(27, 27, 31, 0.7);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.page ::v-deep(.el-pagination .el-pager li) {
  color: #e1e2d9;
  background-color: rgba(27, 27, 31, 0.7);
}

.page ::v-deep(.el-pagination .el-pager li.active) {
  color: #409EFF;
  background-color: transparent;
}

/* 增加更具体的样式选择器 */
.page ::v-deep(.el-pagination .btn-prev[disabled]),
.page ::v-deep(.el-pagination .btn-next[disabled]) {
  background-color: rgba(27, 27, 31, 0.7) !important;
  opacity: 0.6;
}

/* 增加更具体的样式选择器 */
.sun-mode .page ::v-deep(.el-pagination .btn-prev[disabled]),
.sun-mode .page ::v-deep(.el-pagination .btn-next[disabled]) {
  background-color: rgba(255, 255, 255, 0.7) !important;
  opacity: 0.6;
}

/* 更精确地修改 Goto 字的颜色 */
.page ::v-deep(.el-pagination .el-pagination__jump span) {
  color: #fcfdff;
}

.sun-mode .page ::v-deep(.el-pagination .el-pagination__jump span) {
  color: #000000;
  font-weight: bold;
}

/* 覆盖 el-input__wrapper 的背景颜色 */
.page ::v-deep(.el-pagination .el-pagination__jump .el-input__wrapper) {
  background-color: transparent !important;
  border: none !important;
  box-shadow: none !important;
  padding: 0 !important;
}

/* 确保 input 本身也正确设置 */
.page ::v-deep(.el-pagination .el-pagination__jump input) {
  background-color: rgba(27, 27, 31, 0.7) !important;
  border: 1px solid rgba(255, 255, 255, 0.1) !important;
  color: #e1e2d9 !important;
  padding: 0 8px !important;
  border-radius: 4px;
  box-shadow: none !important;
  outline: none !important;

  &:focus {
    background-color: rgba(27, 27, 31, 0.7) !important;
    border-color: rgba(255, 255, 255, 0.2) !important;
  }
}

.sun-mode .page ::v-deep(.el-pagination .el-pagination__jump .el-input__wrapper) {
  background-color: transparent !important;
}

.sun-mode .page ::v-deep(.el-pagination .el-pagination__jump input) {
  background-color: rgba(255, 255, 255, 0.7) !important;
  border: 1px solid rgba(0, 0, 0, 0.1) !important;
  color: #4a5568 !important;
}

/* 明亮模式样式 */
.sun-mode .contentRouter {
  background-color: rgba(255, 255, 255, 0.7);
  color: #2d3748;
  border: 1px solid rgba(0, 0, 0, 0.1);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

/* 桌面端悬浮效果 */
@media (min-width: 769px) {
  .contentRouter:hover {
    transform: translateX(25px);
    transition: transform 0.3s ease;
  }
  
  .sun-mode .contentRouter:hover {
    background-color: rgba(255, 255, 255, 0.9);
  }
}

.sun-mode .title {
  color: #2d3748;
}

.sun-mode .subTitle {
  color: #4a5568;
}

.sun-mode .subTimeAndAuthor {
  color: #718096;
}

.sun-mode .img {
  filter: brightness(0.9);
}


.subTitle {
  margin: 8px 20px 5px 0;
  color: #7096af;
}

.img { 
  max-height: 80px;
  max-width: 130px;
  /* min-width: 100px; */
 
  border-radius: 5px;
  object-fit: cover;
  width: 100%;
  height: 100%;
  size: cover;
}

.subTimeAndAuthor {
  display: inline-flex;
  align-items: center;
  margin: 5px 0 0 0;
  color: #a9b0b8;
  font-size: 14px;
}

.subTimeAndAuthor .el-icon {
  margin-right: 4px;
}

.title {
  margin: 0;
  color: #e1e2d9;
  font-size: 18px;
}

.contentRouter {
  margin-left: 8px;
  background-color: rgba(27, 27, 31, 0.7);
  color: white;
  padding: 12px 15px 10px 15px;
  border-radius: 5px;
  margin: 5px 25px 10px 8px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.5);
  border: 1px solid rgba(255, 255, 255, 0.1);
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  cursor: pointer;
}

/* 桌面端悬浮背景色变化 */
@media (min-width: 769px) {
  .contentRouter:hover {
    background-color: rgba(27, 27, 31, 0.9);
  }
}

/* 移动端宽度优化 */
@media (max-width: 768px) {
  .contentRouter {
    margin: 5px 8px 10px 8px;
    padding: 10px 12px 8px 12px;
  }
  
  .contentRouter > div:first-child {
    width: 100%;
  }
}
</style>
