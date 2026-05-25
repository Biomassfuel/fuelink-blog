<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'


  const router = useRouter()
const handleClick = async (id) => {
  router.push({
    path: '/front/blog',
    query: {
      id: id
    }
  })
  
}

const BlogList = ref([])
import {archiveApi} from '@/api/archive'

const fetchArchive = async () => {
  try {
    const response = await archiveApi()
    BlogList.value = response.data
    // BlogYearList.value = response.data.blogYearList
  } catch (error) {
    BlogList.value = []
  }
}
onMounted(() => {
  fetchArchive()
})

const isSunMode = computed(() => {
  return document.documentElement.classList.contains('sun-mode')
})

</script>

<template>
  <div class="archive-container">
    <div v-for="item in BlogList" :key="item.id">
      <h2>{{ item.year }}</h2>

      <el-timeline style="max-width: 600px">
        <el-timeline-item class="timeline-item" placement="top" size="large" color="#ee63a0" hollow="true">
          <el-card class="timeline-card" style="display: flex; align-items: flex-start;">
            <span class="eps" @click="handleClick">{{ item.count }}篇文章</span>
          </el-card>
        </el-timeline-item>

        <el-timeline-item :timestamp="item.createTime.substring(0, 10)" placement="top" v-for="item in item.articles" :key="item.id" :color="isSunMode ? '#2d3748' : '#BBB5B5FF'">
          <el-card class="timeline-card">
            <h4 style="float: left;" @click="handleClick(item.id)">{{ item.title }}</h4>
            <p style="float: right; ">{{ item.tags }}</p>
          </el-card>
        </el-timeline-item>


      </el-timeline>
    </div>

  </div>
</template>

<style scoped>
.archive-container {
  min-height: 1200px;
  margin-left: 8px;
  background-color: rgba(27, 27, 31, 0.7);
  color: white;
  padding: 12px 15px 30px 15px;
  border-radius: 5px;
  margin: 5px 25px 10px 8px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.5);
  border: 1px solid rgba(255, 255, 255, 0.1);

}

.timeline-card {
  margin: 0;
  padding: 0;
  background-color: transparent;
  color: white;
  border: none;
  width: 700px;
  /* display: flex; */
  align-items: flex-start;
  box-shadow:none!important
}

.timeline-card h4 {
  margin: 0;
}

.timeline-card p {
  margin: 0;
  color: #BBB5B5FF;
}

.timeline-card h4:hover {
  cursor: pointer;
  color: #409eff;
}

::v-deep(.el-card__body) {
  padding: 0 !important;
}

::v-deep(.timeline-item .el-timeline-item__timestamp.is-top) {
  padding-top: 0 !important;
  margin-bottom: 8px;
}

::v-deep(.timeline-item .el-timeline-item__content) {
  vertical-align: top;
  display: flex;
  align-items: flex-start;
}

.eps {
  font-size: 18px;
  color: #838080;
  vertical-align: top;
  line-height: 1;
  display: inline-block;
}

.content {
  color: #BBB5B5FF;
}

/* 明亮模式样式 */
.sun-mode .archive-container {
  background-color: rgba(255, 255, 255, 0.7);
  color: #2d3748;
  border: 1px solid rgba(0, 0, 0, 0.1);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.sun-mode .timeline-card {
  color: #2d3748;
}

.sun-mode .timeline-card h4:hover {
  color: #409eff;
}

.sun-mode .eps {
  color: #6b7280;
}
.sun-mode .timeline-card p {
  color: #6b7280; /* 或其他适合明亮模式的灰色 */
}

/* 响应式布局 */
@media (max-width: 768px) {
  /* 平板端适配 */
  .archive-container {
    margin: 5px 8px 10px 8px;
    padding: 10px 12px 25px 12px;
  }
  
  .timeline-card {
    width: 100%;
    max-width: 500px;
  }
  
  .el-timeline {
    max-width: 100% !important;
  }
  
  .timeline-card h4 {
    font-size: 16px;
  }
  
  .timeline-card p {
    font-size: 14px;
  }
}

@media (max-width: 480px) {
  /* 手机端适配 */
  .archive-container {
    margin: 5px 4px 10px 4px !important;
    padding: 8px 10px 20px 10px !important;
  }
  
  .timeline-card {
    max-width: 100%;
  }
  
  .timeline-card h4 {
    font-size: 15px;
    margin-bottom: 5px;
  }
  
  .timeline-card p {
    font-size: 12px;
    margin-top: 5px;
  }
  
  .eps {
    font-size: 16px;
  }
  
  h2 {
    font-size: 20px;
  }
}
</style>
