<script setup>
import { ref, onMounted } from 'vue'
import adminConfig from '@/config/adminConfig'

const sentence = ref('')
const loading = ref(false)

const fetchSentence = async () => {
  loading.value = true

  try {
    const response = await fetch(adminConfig.sentenceApiUrl)
    const data = await response.json()
    sentence.value = data?.data?.hitokoto || '路漫漫其修远兮，吾将上下而求索'
  } catch (error) {
    sentence.value = '路漫漫其修远兮，吾将上下而求索'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchSentence()
})
</script>

<template>
  <div class="home-page">
    <section class="sentence-panel">
      <span class="sentence-label">每日一句</span>
      <p class="sentence-text">{{ loading ? '加载中...' : sentence }}</p>
    </section>
  </div>
</template>

<style scoped>
.home-page {
  min-height: 100%;
  background-color: #f5f8fd;
  padding: 24px;
}

.sentence-panel {
  min-height: 180px;
  background-color: #ffffff;
  border: 1px solid #dcd9d4;
  border-radius: 6px;
  padding: 28px 32px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.sentence-label {
  color: #64748b;
  font-size: 14px;
  margin-bottom: 14px;
}

.sentence-text {
  margin: 0;
  color: #1f2937;
  font-size: 24px;
  line-height: 1.8;
  letter-spacing: 0;
}

@media (max-width: 768px) {
  .home-page {
    padding: 16px;
  }

  .sentence-panel {
    padding: 22px;
  }

  .sentence-text {
    font-size: 20px;
  }
}
</style>
