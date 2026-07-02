import { ref } from 'vue'

// 顶部加载进度条的全局状态
export const loadingProgress = ref(0)
export const loadingVisible = ref(false)

let trickleTimer = null
let doneTimer = null

// 开始加载：显示进度条并缓慢向 90% 爬升
export function startLoading() {
  clearInterval(trickleTimer)
  clearTimeout(doneTimer)

  loadingVisible.value = true
  loadingProgress.value = 8

  // 下一帧再推进，保证过渡动画能触发
  requestAnimationFrame(() => {
    loadingProgress.value = 25
  })

  // 缓慢向 90% 逼近（越接近越慢），营造"仍在加载"的感觉
  trickleTimer = setInterval(() => {
    if (loadingProgress.value < 90) {
      loadingProgress.value += (90 - loadingProgress.value) * 0.18
    }
  }, 220)
}

// 结束加载：补满到 100% 后淡出
export function doneLoading() {
  clearInterval(trickleTimer)
  clearTimeout(doneTimer)

  loadingProgress.value = 100
  doneTimer = setTimeout(() => {
    loadingVisible.value = false
    // 隐藏之后再复位，避免看到回退过程
    setTimeout(() => {
      loadingProgress.value = 0
    }, 300)
  }, 260)
}
