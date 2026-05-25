import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

import 'element-plus/dist/index.css'
import  ElMessage  from 'element-plus'
// import './assets/main.css'

const app = createApp(App)

app.use(router)
app.use(ElMessage)
app.mount('#app')
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}