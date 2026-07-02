<script setup>
import { ref, onMounted } from 'vue'
import siteConfig from '@/config/siteConfig'

const todayReport = ref('')
const loadFailed = ref(false)

const getTodayReport = () => {
  const today = new Date()
  const year = today.getFullYear()
  const month = String(today.getMonth() + 1).padStart(2, '0')
  const day = String(today.getDate()).padStart(2, '0')
  const date = `${year}${month}${day}`
  const baseUrl = siteConfig.todayReport.apiUrl.replace(/\/?$/, '/')

  todayReport.value = `${baseUrl}moyu_${date}.jpg`
  loadFailed.value = false
}

const handleImageError = () => {
  loadFailed.value = true
}

onMounted(() => {
  getTodayReport()
})
</script>

<template>
  <div class="today-report-container">
    <img
      v-if="todayReport && !loadFailed"
      :src="todayReport"
      class="today-report-image"
      alt="今日日报"
      @error="handleImageError"
    >
    <div v-else class="today-report-empty">今日日报暂时无法加载</div>
  </div>
</template>

<style scoped>
.today-report-container {
  min-height: auto;
  margin-left: 8px;
  background-color: rgba(27, 27, 31, 0.7);
  color: white;
  padding: 20px 25px 30px 25px;
  border-radius: 5px;
  margin: 5px 25px 10px 8px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.5);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.today-report-image {
  display: block;
  width: 100%;
  height: auto;
}

.today-report-empty {
  min-height: 240px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: rgba(255, 255, 255, 0.7);
}

.sun-mode .today-report-container {
  background-color: rgba(255, 255, 255, 0.5);
  color: #2d3748;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.sun-mode .today-report-empty {
  color: rgba(45, 55, 72, 0.7);
}

@media (max-width: 768px) {
  .today-report-container {
    margin: 5px 8px 10px 8px;
    padding: 15px 18px 25px 18px;
  }
}

@media (max-width: 480px) {
  .today-report-container {
    margin: 5px 4px 10px 4px !important;
    padding: 12px 15px 20px 15px !important;
  }
}
</style>
