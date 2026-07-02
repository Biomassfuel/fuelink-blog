<script setup>
import ElementPlus from 'element-plus'
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { useRouter, useRoute } from 'vue-router'
let router = useRouter()
const route = useRoute()
import { labelQueryAllApi, queryPageApi } from '@/api/home'
import siteConfig from '@/config/siteConfig'

// 响应式菜单控制
const isMobileMenuOpen = ref(false)

const toggleMobileMenu = () => {
  isMobileMenuOpen.value = !isMobileMenuOpen.value
}


const isScrolled = ref(false)
// let scrollHandler = null
// const scrollbarRef = ref(null)
const titleSearch = ref('')
const isFocused = ref(false)
const searchInput = ref(null)


// 根据索引返回不同的标签类型
const getTagType = (index) => {
  const types = ['primary', 'success', 'info', 'warning', 'danger']
  return types[index % types.length]
}



const sponsorModalVisible = ref(false)
const openSponsorModal = () => {
  sponsorModalVisible.value = true
}
const closeSponsorModal = () => {
  sponsorModalVisible.value = false
}



//监听滚动事件
// scrollHandler = () => {
//   if (window.scrollY > 0) {
//     isScrolled.value = true
//   } else {
//     isScrolled.value = false
//   }
// }
// const scrollHandler = (e) => {
//   // 获取滚动位置
//   const scrollTop = e.scrollTop || 0
//   if (scrollTop > 0) {
//     isScrolled.value = true
//   } else {
//     isScrolled.value = false
//   }
// }
const handleCustomScroll = (e) => {
  const scrollTop = e.detail.scrollTop || 0
  if (scrollTop > 0) {
    isScrolled.value = true
  } else {
    isScrolled.value = false
  }
}

// 标签列表
const labelList = ref([])



onMounted(() => {
  startSentenceRotation()
  getAllFriendLink()
  // 监听清空搜索事件
  window.addEventListener('clear-search', () => {
    titleSearch.value = ''
    Tag.value = ''
  })

  window.addEventListener('customScroll', handleCustomScroll)
  // window.addEventListener('scroll', scrollHandler)
  document.addEventListener('keydown', handleGlobalKeydown)
  timeInterval = setInterval(updateTime, 1000)
  // document.getElementById('app').classList.add('sun-mode')
  // 标签查询
  labelQueryAllApi().then(res => {
    // index 从 0 开始 ，id,name
    //  labelList.value = res.data.map(item => item.name) 
    labelList.value = res.data.map(item => ({
      id: item.id,
      name: item.name
    }))
  })
})

onBeforeUnmount(() => {

  window.removeEventListener('customScroll', handleCustomScroll)
  // window.removeEventListener('scroll', scrollHandler)
  document.removeEventListener('keydown', handleGlobalKeydown)

  window.removeEventListener('clear-search', () => {
    titleSearch.value = ''
  })
  if (timeInterval) clearInterval(timeInterval)
  if (sentenceInterval) clearInterval(sentenceInterval.value)

})

// 计算属性：根据输入内容决定显示的快捷键提示文本
const shortcutHintText = computed(() => {
  if (titleSearch.value.length > 0) {
    return '<kbd>Enter</kbd>'
  } else {
    return '<kbd>Ctrl</kbd> + <kbd>K</kbd>'
  }
})

// 聚焦搜索框
const focusSearchInput = () => {
  searchInput.value.focus()
}

// 处理键盘事件
const handleKeydown = (event) => {
  // 按Enter键执行搜索
  if (event.key === 'Enter') {
    event.preventDefault()
    performSearch()
  }

  // 按ESC键退出搜索
  if (event.key === 'Escape') {
    searchInput.value.blur()
  }
}
const performSearch = () => {

  router.push({
    path: '/',
    query: { titleSearch: titleSearch.value, tagId: Tag.value }

  })
}

//标签筛选
const Tag = ref('')
const handleTagClick = (tag) => {
  Tag.value = tag
  router.push({
    path: '/',
    query: { tagId: tag, titleSearch: titleSearch.value }
  })
}


// 全局键盘事件监听
const handleGlobalKeydown = (event) => {
  // 检查是否按下 Ctrl/Cmd + K
  if ((event.ctrlKey || event.metaKey) && event.key === 'k') {
    event.preventDefault() // 阻止浏览器默认搜索快捷键
    focusSearchInput()
  }
}
// 切换明亮模式
const code = ref(1)
const applySunMode = () => {
  code.value = 1 - code.value
  document.documentElement.classList.toggle('sun-mode');
  const appElement = document.getElementById('app');
  if (appElement) {
    appElement.classList.toggle('sun-mode');
  }
}

const toggleSunMode = (event) => {
  const prefersReduced = window.matchMedia('(prefers-reduced-motion: reduce)').matches

  // 不支持 View Transitions 或用户偏好减少动效时，直接切换
  if (!document.startViewTransition || prefersReduced) {
    applySunMode()
    return
  }

  // 以点击位置为圆心，计算能覆盖整屏的半径
  const x = event?.clientX ?? window.innerWidth / 2
  const y = event?.clientY ?? window.innerHeight / 2
  const endRadius = Math.hypot(
    Math.max(x, window.innerWidth - x),
    Math.max(y, window.innerHeight - y)
  )

  const transition = document.startViewTransition(() => applySunMode())

  transition.ready.then(() => {
    document.documentElement.animate(
      {
        clipPath: [
          `circle(0px at ${x}px ${y}px)`,
          `circle(${endRadius}px at ${x}px ${y}px)`
        ]
      },
      {
        duration: 500,
        easing: 'ease-in-out',
        pseudoElement: '::view-transition-new(root)'
      }
    )
  })
}


// 时间相关数据
const currentTime = ref(new Date())
const monthsCN = ['1月', '2月', '3月', '4月', '5月', '6月',
  '7月', '8月', '9月', '10月', '11月', '12月']
const weekdaysCN = ['日', '一', '二', '三', '四', '五', '六']

let timeInterval = null

// 格式化时间
const formattedTime = computed(() => {
  const now = currentTime.value
  let hours = now.getHours()
  const minutes = now.getMinutes()
  const seconds = now.getSeconds()
  const period = hours >= 12 ? 'PM' : 'AM'
  hours = hours % 12 || 12

  return {
    hour: hours.toString().padStart(2, '0'),
    minute: minutes.toString().padStart(2, '0'),
    second: seconds.toString().padStart(2, '0'),
    period
  }
})

// 当前日期
const currentDate = computed(() => {
  const now = currentTime.value
  const year = now.getFullYear()
  const month = now.getMonth()
  const day = now.getDate()
  const weekday = now.getDay()

  return {
    year,
    month: monthsCN[month],
    day: day.toString().padStart(2, '0'),
    weekday: weekdaysCN[weekday]
  }
})

// 更新时间
const updateTime = () => {
  currentTime.value = new Date()
}

// 主页函数
const HomeFunction = () => {
  //清空搜索
  Tag.value = ''
  //清空搜索框
  titleSearch.value = ''
  router.push('/')
}

const friendLinkList = ref([])
const getAllFriendLink = async () => {
  const result = await queryPageApi();
  if (result.code) {
    friendLinkList.value = result.data
  }
}

const handleFriendLinkClick = (siteurl) => {
  window.open(siteurl, '_blank')
}

const sentence = ref(['路漫漫其修远兮，吾将上下而求索', '欲买桂花同载酒，终不似，少年游','长风破浪会有时，直挂云帆济沧海','纸上得来终觉浅，绝知此事要躬行'])
const currentSentenceIndex = ref(0)
const sentenceInterval = ref(null)

// 获取当前显示的句子
const currentSentence = computed(() => {
  return sentence.value[currentSentenceIndex.value]
})

// 开始句子轮播
const startSentenceRotation = () => {
  sentenceInterval.value = setInterval(() => {
    nextSentence()
  }, 4000) // 每5秒切换一次
}

// 切换到下一个句子
const nextSentence = () => {
  currentSentenceIndex.value = (currentSentenceIndex.value + 1) % sentence.value.length
}

// 点击句子时切换到下一句
const handleSentenceClick = () => {
  // 清除当前的定时器
  if (sentenceInterval.value) {
    clearInterval(sentenceInterval.value)
  }
  // 立即切换到下一句
  nextSentence()
  // 重新启动定时器
  startSentenceRotation()
}

</script>
<template>

  <div class="common-layout">
    <el-container>
      <!-- 导航栏 -->
      <el-header class="header" :class="{ 'scrolled': isScrolled }">
        <div class="headerContent">
          <div class="leftHeader">
            <img src="@/assets/logo.png" alt="logo" style="height: 30px; width: 30px; margin-right: 10px;"></img>
            <span class="name" @click="HomeFunction()">{{ siteConfig.ownerName }}</span>
            <div class="search-container">
              <div class="search-box" :class="{ focused: isFocused }" @click="focusSearchInput">
                <!-- 搜索图标 -->
                <svg class="search-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"
                  stroke-linecap="round" stroke-linejoin="round">
                  <circle cx="11" cy="11" r="8"></circle>
                  <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
                </svg>

                <!-- 搜索输入框 -->
                <input type="text" class="search-input" placeholder="搜索" v-model="titleSearch" @focus="isFocused = true"
                  @blur="isFocused = false" @keydown="handleKeydown" ref="searchInput">

                <!-- 快捷键提示 -->
                <div class="shortcut-hint" @click.stop="focusSearchInput" v-html="shortcutHintText"
                  @onclick="handleKeydown"></div>
              </div>
            </div>
          </div>
          <!-- 汉堡菜单按钮 -->
            <div class="mobile-menu-button" :class="{ 'active': isMobileMenuOpen }" @click="toggleMobileMenu">
              <span class="bar"></span>
              <span class="bar"></span>
              <span class="bar"></span>
            </div>
            
            <!-- 桌面导航菜单 -->
            <div class="rightHeader">
              <span @click="HomeFunction()" class="rightHeaderItem">主页</span>
              <span @click="router.push('/archive')" class="rightHeaderItem">归档</span>
              <span @click="router.push('/about')" class="rightHeaderItem">关于</span>
              <span @click="router.push('/anime')" class="rightHeaderItem">追番</span>
              <span @click="router.push('/friendLink')" class="rightHeaderItem">友链</span>
              <a :href="siteConfig.repositoryUrl" target="_blank" class="rightHeaderItem">前往</a>
              
              <span @click="router.push('/todayReport')" class="rightHeaderItem">今日日报</span>
            
              <span class="rightHeaderItem" @click="openSponsorModal">赞助</span>
              <span class="rightHeaderItemSecond" :class="{ 'scrolled': isScrolled }">|</span>
              <el-icon class="rightHeaderItem" @click="toggleSunMode($event)">
                <!-- <Sunny /> <Moon /> -->
                <el-icon v-if="code" class="el-icon--sun">
                  <sunny />
                </el-icon>
                <el-icon v-else class="el-icon--moon">
                  <moon />
                </el-icon>
              </el-icon>
            </div>
            
            <!-- 移动导航菜单 -->
            <div class="mobile-menu" :class="{ 'open': isMobileMenuOpen }">
              <span @click="HomeFunction(); toggleMobileMenu()" class="mobile-menu-item">主页</span>
              <span @click="router.push('/archive'); toggleMobileMenu()" class="mobile-menu-item">归档</span>
              <span @click="router.push('/about'); toggleMobileMenu()" class="mobile-menu-item">关于</span>
              <span @click="router.push('/anime'); toggleMobileMenu()" class="mobile-menu-item">追番</span>
              <span @click="router.push('/friendLink'); toggleMobileMenu()" class="mobile-menu-item">友链</span>
              <a :href="siteConfig.repositoryUrl" target="_blank" class="mobile-menu-item" @click="toggleMobileMenu()">前往</a>
              <span @click="() => { router.push('/todayReport'); toggleMobileMenu(); }" class="mobile-menu-item">今日日报</span>
              <span class="mobile-menu-item" @click="() => { openSponsorModal(); toggleMobileMenu(); }">赞助</span>
              <span class="mobile-menu-item divider">|</span>
              <el-icon class="mobile-menu-item" @click="(e) => { toggleSunMode(e); toggleMobileMenu(); }">
                <el-icon v-if="code" class="el-icon--sun">
                  <sunny />
                </el-icon>
                <el-icon v-else class="el-icon--moon">
                  <moon />
                </el-icon>
              </el-icon>
            </div>
        </div>
      </el-header>

      <!-- 博客标题 -->
      <el-main style="z-index: 999;">
        <el-main class="blogTitle" style="height: 100%; width: 100%; margin: 0 auto; padding: 40px 0 20px 0;">
          <span class="blogName" @click="HomeFunction()">{{ siteConfig.siteTitle }}</span>
          <span class="blogDescRight">{{ siteConfig.siteSubtitle }}</span>
          <!-- <br/> -->
          <br />
          <h1></h1>
<span class="blogDesc" @click="handleSentenceClick">{{ currentSentence }}</span>
        </el-main>
      </el-main>


      <!-- 主展示区域 -->
      <el-main class="main">
        <el-container style="height: 100%; width: 100%; margin: 0; ">
          <!-- 侧边栏 -->
          <el-aside class="aside">
            <div class="introduction">
              <img class="avatar" src="@/assets/a.webp" alt="avatar"></img>
              <h4 style=" margin-top: 10px;">{{ siteConfig.ownerName }}</h4>
              <hr>
              <p class="introductionDesc">{{ siteConfig.ownerBio }}</p>
              <a :href="siteConfig.socialLinks.bilibili" target="_blank">
                <img src="@/assets/fonts/bilibili.png" alt="bilibili" class="icon">
              </a>
              <a :href="siteConfig.socialLinks.steam" target="_blank">
                <img src="@/assets/fonts/steam.png" alt="steam" class="icon">
              </a>
              <a :href="siteConfig.socialLinks.github" target="_blank">
                <img src="@/assets/fonts/github.png" alt="github" class="icon">
              </a>
            </div>
            <!-- 时间组件 -->
            <div class="time-container-mini">
              <div class="time-row-mini">
                <span class="time-text-mini">{{ formattedTime.hour }}:{{ formattedTime.minute }}:{{ formattedTime.second
                }}</span>
                <span class="period-mini">{{ formattedTime.period }}</span>
              </div>
              <div class="date-row-mini">
                <span class="date-text-mini">{{ currentDate.year }}年 {{ currentDate.month }}{{ currentDate.day }} 星期{{
                  currentDate.weekday }}</span>
              </div>
            </div>
            <!-- 友链 -->
            <div class="friendLink">
              <div class="friendLinkTitle">
                <el-icon class="friendLinkIcon">
                  <Avatar />
                </el-icon>
                <span>友链链接</span>
              </div>
              <hr class="friendLinkLine">

              <el-scrollbar height="150px">
                <div v-for="item in friendLinkList" :key="item" class="friendLinkItem" @click="handleFriendLinkClick(item.siteurl)">
                  <img class="friendLinkAvatar" :src="item.imgurl" alt="友链头像"></img>
                  <div class="friendLinkInfo">
                    <h5 class="friendLinkName">{{ item.title }}</h5>
                    <p class="friendLinkDesc">{{ item.desc }}</p>
                  </div>
                </div>
              </el-scrollbar>

            </div>
            <!-- 标签 -->
            <div class="tag">
              <div class="tagTitle">
                <el-icon>
                  <Management />
                </el-icon>
                <span class="tagTitle">标签</span>
              </div>
              <hr class="friendLinkLine">
              <div>
                <el-tag v-for="(item, index) in labelList" :key="item" class="tagItem" size="default" effect="dark"
                  @click="handleTagClick(item.id)" :type="getTagType(index)">
                  {{ item.name }}
                </el-tag>
              </div>
            </div>




          </el-aside>
          <el-main class="content">
            <img class="snake" src="@/assets/snake-Light.svg"></img>
            <router-view v-slot="{ Component }">
              <transition name="page-slide" mode="out-in">
                <component :is="Component" :key="route.path" />
              </transition>
            </router-view>
          </el-main>
        </el-container>
      </el-main>


      <el-footer class="footer">
        <span style="font-size: 16px;">&copy; </span><span>{{ siteConfig.copyrightYear }} {{ siteConfig.ownerName }}&nbsp;&nbsp;</span><a
          :href="siteConfig.license.url" target="_blank">  采用{{ siteConfig.license.name }}协议</a>
        <br style="margin: 0;"></br>
        <a :href="siteConfig.beian.icpUrl" target="_blank" style="margin: 0 10px 0 0; font-size: 10px;">{{ siteConfig.beian.icpText }}
</a>
        <img :src="siteConfig.beian.policeIconUrl" class="w-full" style="width: 10px;margin-right: 5px;">
        <a :href="siteConfig.beian.policeUrl" target="_blank" style="margin: 0; font-size: 10px;">{{ siteConfig.beian.policeText }}</a>
      </el-footer>
    </el-container>

  </div>
  <div class="sponsor">
    <el-dialog title="赞助我" v-model="sponsorModalVisible" width="90%" center>
      <div class="sponsor-container">
        <div class="sponsor-item">
          <img src="@/assets/pay.jpg" alt="微信" class="sponsor-image">
          <div class="sponsor-info">
            <p style="font-size: 14px; margin: 0;">请扫描二维码赞助</p>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>

</template>
<style scoped>
/* 页面切换过渡：轻微上移 + 淡入，贴合项目卡片风格 */
.page-slide-enter-active {
  transition: opacity 0.35s ease, transform 0.35s cubic-bezier(0.22, 0.61, 0.36, 1);
}

.page-slide-leave-active {
  transition: opacity 0.2s ease, transform 0.2s ease;
}

.page-slide-enter-from {
  opacity: 0;
  transform: translateY(18px);
}

.page-slide-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}

/* 尊重用户的"减少动效"系统偏好 */
@media (prefers-reduced-motion: reduce) {
  .page-slide-enter-active,
  .page-slide-leave-active {
    transition: opacity 0.2s ease;
  }
  .page-slide-enter-from,
  .page-slide-leave-to {
    transform: none;
  }
}

/* footer */
.footer {
  height: 65px;
  background-color: #1b1b1f;
  color: #98989f;
  text-align: center;
  padding: 10px 0;
  z-index: 1000;
  margin-top: 150px;
}

.footer a {
  color: #98989f;
  font-size: 12px;
  text-decoration: none;
}

.footer a:hover {
  color: #fff;
}

.footer span {
  font-size: 12px;
  margin-top: 2px;
}

/* 标签 */
.tagTitle {
  display: flex;
  align-items: center;
  gap: 10px;
}

.tagItem {
  margin: 5px 5px;
  /* transition: transform 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94); */
}

.tagItem:hover {
  cursor: pointer;
  transform: scale(1.1);
}

.tag {
  margin: 10px 15px;
  background-color: rgba(27, 27, 31, 0.7);
  border-radius: 5px;
  padding: 10px 15px;
  box-shadow:
    0 4px 15px rgba(0, 0, 0, 0.5);
  border: 1px solid rgba(255, 255, 255, 0.05);

}

.tagTitle {
  color: #d5d6cd;
  font-size: 14px;
}



/* 友链 */
.friendLinkAvatar {
  width: 45px;
  height: 45px;
  border-radius: 50%;
}

.friendLinkItem {
  margin: 10px 0;
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 8px 8px 4px;
  border-radius: 4px;
  transition: background-color 0.3s ease;
}

.friendLinkItem:hover {
  background-color: rgba(255, 255, 255, 0.1);
  cursor: pointer;
}

.friendLinkInfo {
  flex: 1;
  overflow: hidden;
}

.friendLinkName {
  margin: 0 0 4px 0;
  font-size: 14px;
  color: #f8fafc;
  font-weight: 500;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.friendLinkDesc {
  margin: 0;
  font-size: 12px;
  color: #94a3b8;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.sun-mode .friendLinkItem:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

.sun-mode .friendLinkName {
  color: #2d3748;
}

.sun-mode .friendLinkDesc {
  color: #4a5568;
}


.friendLink {
  margin: 10px 15px;
  background-color: rgba(27, 27, 31, 0.7);
  border-radius: 5px;
  padding: 10px 15px;
  box-shadow:
    0 4px 15px rgba(0, 0, 0, 0.5);
  border: 1px solid rgba(255, 255, 255, 0.05);

}

.friendLink span {
  color: #d5d6cd;
  font-size: 14px;
}

.friendLinkTitle {
  display: flex;
  align-items: center;
  gap: 10px;
}

.friendLinkIcon {
  color: #d5d6cd;
  font-size: 14px;
}

.friendLinkLine {
  border: 1px solid rgba(255, 255, 255, 0.25);
  margin: 5px 0;
}


/* 时间组件 */
.time-container-mini {
  /* width: 215px; */
  height: 70px;
  background-color: rgba(27, 27, 31, 0.7);
  border-radius: 5px;
  padding: 12px 15px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  box-shadow:
    0 4px 15px rgba(0, 0, 0, 0.5);
  border: 1px solid rgba(255, 255, 255, 0.05);
  position: relative;
  overflow: hidden;
  margin: 10px 15px;

}

.time-row-mini {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.time-text-mini {
  font-size: 1.4rem;
  font-weight: 300;
  color: #f8fafc;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}

.period-mini {
  font-size: 0.7rem;
  color: #10b981;
  background: rgba(16, 185, 129, 0.1);
  padding: 2px 6px;
  border-radius: 4px;
  border: 1px solid rgba(16, 185, 129, 0.2);
}

.date-row-mini {
  border-top: 1px solid rgba(255, 255, 255, 0.08);
  padding-top: 6px;
}

.date-text-mini {
  font-size: 0.75rem;
  color: #cbd5e1;
  font-weight: 300;
}

/* 明亮模式适配 */
.sun-mode .time-container-mini {
  background-color: rgba(255, 255, 255, 0.5);
  /* 明亮模式下的背景色 */
  box-shadow:
    0 8px 20px rgba(0, 0, 0, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.5);
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.sun-mode .time-text-mini {
  color: #2d3748;
}

.sun-mode .period-mini {
  color: #10b981;
  background: rgba(16, 185, 129, 0.1);
  border: 1px solid rgba(16, 185, 129, 0.2);
}

.sun-mode .date-text-mini {
  color: #4a5568;
}

/* 响应式设计 */
@media (max-width: 750px) {
  .time-container-mini {
    width: auto;
    height: 65px;
    padding: 10px 12px;
  }

  .time-text-mini {
    font-size: 1.2rem;
  }

  .date-text-mini,
  .period-mini {
    font-size: 0.7rem;
  }
}

@media (max-width: 480px) {
  .time-container-mini {
    width: auto;
    height: 60px;
    padding: 8px 10px;
  }

  .time-text-mini {
    font-size: 1.1rem;
  }

  .date-text-mini,
  .period-mini {
    font-size: 0.65rem;
  }
}

.time-text-mini {
  font-size: 1.25rem;
  font-weight: 300;
  color: #f8fafc;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}



.blogName:hover {
  cursor: pointer;
  color: #409EFF;
}

.blogDesc:hover {
  cursor: pointer;
}

.icon {
  height: 33px;
  width: 33px;
  margin: 0 8px;
  transition: transform 0.5s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}

.icon:hover {
  transform: scale(1.3) rotate(5deg);
  cursor: pointer;
}

.sun-mode .icon {
  filter: brightness(115%) contrast(100%) invert(100%) saturate(60%);
}


.introduction .avatar {
  height: 130px;
  width: 130px;
  border-radius: 50%;
  border: 3px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  transition: transform 0.3s ease;
}

/* .introduction img:hover {
  transform: scale(1.05);
} */

.introductionDesc {
  font-size: 14px;
  color: #d8dfd0;
  line-height: 1.6;
}

.introduction hr {
  border: none;
  height: 1px;
  background: linear-gradient(to right, transparent, #ffffff, transparent);
  margin: 15px auto;
  width: 100%;
}

.introduction {
  background-color: rgba(27, 27, 31, 0.7);
  color: white;
  padding: 20px 15px 10px 15px;
  border-radius: 5px;
  margin: 15px 15px 10px 15px;
  text-align: center;
  /* 阴影 */
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.5);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.snake {
  /* 默认样式 */
  max-width: 100%;
  height: auto;
  display: block;
  margin: 0 auto 20px;
}

/* 明亮模式下的蛇形元素样式 */
.sun-mode .snake {
  filter: brightness(115%) contrast(100%);
}

/* 移动端优化 */
@media (max-width: 768px) {
  .snake {
    max-width: 90%;
    margin: 0 auto 15px;
    /* 调整移动端的边距和大小 */
  }
}

@media (max-width: 480px) {
  .snake {
    max-width: 95%;
    margin: 0 auto 10px;
    /* 进一步优化小屏幕显示 */
  }
}

.common-layout {
  /* height: 100vh; */
  height: 100%;
  width: 100%;
  margin: 0;
  padding: 0;

}

.header {
  background-color: transparent;
  /* background-color: #409EFF; */
  position: sticky;
  top: 0;
  z-index: 1000;
  transition: background-color 0.5s ease;
  display: flex;
  justify-content: center;
  align-items: center;

}

.header.scrolled {
  background-color: #1b1b1f;
  z-index: 1000;
}

.headerContent {
  display: flex;
  justify-content: space-between;
  align-items: center;
  /* width: 55%; */
  margin: 0 auto;
  /* background-color: green; */
  background-color: transparent;
  height: 100%;
}

.leftHeader {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  /* float: left; */
  width: 10%;
  /* margin-top: 20px; */
  margin-right: 250px;
}

.search-container {
  width: auto;
  /* 改为auto而不是固定百分比 */
  flex: 1;
  /* 占据剩余空间 */
  margin: 0 30px;
  /* 左右留出适当间距 */
  max-width: none;
  /* 移除最大宽度限制 */
}

.search-box {
  position: relative;
  width: 100%;
  height: 40px;
  background: linear-gradient(135deg, #2d3748, #4a5568);
  border-radius: 12px;
  display: flex;
  align-items: center;
  padding: 0 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.search-box.focused {
  box-shadow: 0 6px 25px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.search-icon {
  width: 16px;
  height: 16px;
  margin-right: 8px;
  color: #e2e8f0;
  flex-shrink: 0;
}

.search-input {
  flex: 1;
  height: 100%;
  background: transparent;
  border: none;
  outline: none;
  color: white;
  font-size: 14px;
  font-weight: 500;
  width: 50px;
}

.search-input:hover {
  cursor: text;
}

.search-input::placeholder {
  color: #cbd5e0;
  font-weight: 400;
}

.shortcut-hint {
  background-color: rgba(0, 0, 0, 0.3);
  color: white;
  font-size: 11px;
  padding: 4px 8px;
  border-radius: 6px;
  font-weight: 500;
  letter-spacing: 0.5px;
  user-select: none;
  transition: background-color 0.2s ease;
  cursor: pointer;
}

.shortcut-hint:hover {
  background-color: rgba(0, 0, 0, 0.4);
}

.shortcut-hint kbd {
  font-family: inherit;
  font-weight: 600;
}

.description {
  text-align: center;
  margin-top: 24px;
  color: #718096;
  font-size: 14px;
  line-height: 1.6;
}

.description a {
  color: #4299e1;
  text-decoration: none;
}

.description a:hover {
  text-decoration: underline;
}

/* 响应式调整 */
@media (max-width: 640px) {
  .search-box {
    height: 52px;
    padding: 0 14px;
  }

  .shortcut-hint {
    display: none;
  }

  .search-icon {
    width: 18px;
    height: 18px;
  }
}



.blogTitle {
  height: 200px;
  /* line-height: 60px; */
  font-family: 楷体;
  width: 1086px;
  /* background-color: red; */
  color: black;
  text-align: center;
  margin: 0 auto;
  padding-top: 40px;
}


.blogName {
  font-size: 32px;
  margin: 0 auto;
  color: #FFFFFFFF;
}

.blogDescRight {
  font-size: 14px;
  margin-left: 10px;
  color: #FFFFFFFF;
  letter-spacing: 1px;
}

.blogDesc {
  font-size: 18px;
  margin: 20px auto;
  color: #dfdfd6;
  letter-spacing: 1px;
}


.main {
  /* background-color: orange; */
  /* width: 45%; */
  width: 1155px;
  min-height: 1200px;
  margin: 0 auto;
  padding: 0;
  z-index: 999;
}

.content {
  /* background-color: yellow; */
  /* width: 50%; */
  height: 100%;
  /* margin: 0 auto; */
  /* padding-left: 20px; */
  padding: 0;
  /* 去除滚动条 */
  overflow-y: hidden;
  overflow-x: hidden;
}

.aside {
  /* background-color: pink; */
  width: 24%;
  height: 100%;
  margin: 0 auto;
}

.rightHeader {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  /* float: left; */
  width: 50%;
  margin-right: 20px;
}

.rightHeaderItem {
  color: white;
  font-size: 16px;
  line-height: 60px;
  margin-right: 20px;
  text-decoration: none;
}

.rightHeaderItem:hover {
  color: #409EFF;
  /* 鼠标悬停是鼠标指针 */
  cursor: pointer;
}

.rightHeaderItemSecond {
  color: #25272c;
  font-size: 25px;
  line-height: 60px;
  margin-right: 20px;
  text-decoration: none;
}

.rightHeaderItemSecond.scrolled {
  color: rgb(201, 201, 201);
}

.name {
  color: white;
  font-size: 18px;
  font-weight: bolder;
  transition: transform 0.5s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}

.name:hover {
  transform: scale(1.05) rotate(2deg);
  cursor: pointer;
}





/* 明亮模式样式 */
.sun-mode .header.scrolled {
  background-color: #f5f5f5;
}

.sun-mode .name {
  color: #2d3748;
}

.sun-mode .header {
  background-color: transparent;
}

.sun-mode .header.scrolled {
  background-color: #f5f5f5;
}

.sun-mode .headerContent {
  background-color: transparent;
}

.sun-mode .search-box {
  background: linear-gradient(135deg, #e2e8f0, #cbd5e0);
}

.sun-mode .search-icon {
  color: #4a5568;
}

.sun-mode .search-input {
  color: #2d3748;
}

.sun-mode .search-input::placeholder {
  color: #718096;
}

.sun-mode .shortcut-hint {
  background-color: rgba(255, 255, 255, 0.3);
  color: #2d3748;
}

.sun-mode .rightHeaderItem {
  color: #2d3748;
}

.sun-mode .rightHeaderItem:hover {
  color: #409EFF;
}

/* 响应式菜单样式 */
/* 汉堡菜单按钮 */
.mobile-menu-button {
  display: none;
  flex-direction: column;
  justify-content: space-between;
  width: 30px;
  height: 21px;
  cursor: pointer;
  z-index: 1001;
}

.mobile-menu-button .bar {
  width: 100%;
  height: 3px;
  background-color: white;
  border-radius: 2px;
  transition: all 0.3s ease;
}

/* 明亮模式下的汉堡菜单颜色 */
.sun-mode .mobile-menu-button .bar {
  background-color: #2d3748;
}

/* 汉堡菜单动画 */
.mobile-menu-button.active .bar:nth-child(1) {
  transform: translateY(9px) rotate(45deg);
}

.mobile-menu-button.active .bar:nth-child(2) {
  opacity: 0;
}

.mobile-menu-button.active .bar:nth-child(3) {
  transform: translateY(-9px) rotate(-45deg);
}

/* 移动菜单 */
.mobile-menu {
  display: none;
  position: fixed;
  top: 60px;
  right: 0;
  width: 200px;
  background-color: #1b1b1f;
  border-radius: 0 0 0 10px;
  box-shadow: -2px 2px 10px rgba(0, 0, 0, 0.3);
  z-index: 1000;
  flex-direction: column;
  padding: 15px 0;
  transition: all 0.3s ease;
}

.mobile-menu.open {
  display: flex;
}

.mobile-menu-item {
  color: white;
  font-size: 16px;
  padding: 12px 20px;
  text-align: left;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.mobile-menu-item:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.mobile-menu-item.divider {
  color: #25272c;
  padding: 8px 20px;
  font-size: 20px;
  text-align: center;
  cursor: default;
}

.mobile-menu-item.divider:hover {
  background-color: transparent;
}

/* 明亮模式下的移动菜单样式 */
.sun-mode .mobile-menu {
  background-color: #f5f5f5;
}

.sun-mode .mobile-menu-item {
  color: #2d3748;
}

.sun-mode .mobile-menu-item:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

.sun-mode .mobile-menu-item.divider {
  color: #e2e8f0;
}

/* 响应式布局媒体查询 */
@media (max-width: 1200px) {
  .main {
    width: 90%;
  }
  
  .blogTitle {
    width: 90%;
  }
  
  /* 平板端header布局优化 */
  .headerContent {
    width: 95%;
  }
  
  .leftHeader {
    width: auto;
    margin-right: 20px;
  }
  
  .search-container {
    flex: 1;
    margin: 0 20px 0 10px;
    max-width: 300px;
  }
  
  .rightHeader {
    width: auto;
  }
  
  .rightHeaderItem {
    margin-right: 10px;
    font-size: 14px;
  }
}

@media (max-width: 768px) {
  /* 隐藏桌面导航菜单 */
  .rightHeader {
    display: none;
  }
  
  /* 显示汉堡菜单按钮 */
  .mobile-menu-button {
    display: flex;
    margin-left: 15px; /* 增加与搜索框之间的边距 */
  }
  
  /* 调整头部内容 */
  .headerContent {
    width: 95%;
    padding: 10px 0;
  }
  
  .leftHeader {
    width: auto;
    margin-right: 0;
    flex: 1;
    justify-content: flex-start; /* 向左显示logo和标题 */
  }
  
  /* 调整主内容区域 */
  .main {
    width: 95%;
    min-height: auto;
  }
  
  /* 调整侧边栏和内容区域 */
  .el-container {
    flex-direction: column;
  }
  
  .aside {
    width: 100%;
    margin-bottom: 20px;
    order: 2; /* 将侧边栏放在后面 */
  }
  
  .content {
    order: 1; /* 将主内容放在前面 */
  }
  
  /* 调整博客标题 */
  .blogName {
    font-size: 24px;
  }
  
  .blogDesc {
    font-size: 16px;
  }
  
  /* 在移动端隐藏搜索框 */
  .search-container {
    display: none;
  }
}

@media (max-width: 480px) {
  .blogName {
    font-size: 20px;
  }
  
  .blogDesc {
    font-size: 14px;
  }
  
  /* 调整搜索框大小 */
  .search-box {
    height: 36px;
  }
  
  /* 调整移动菜单宽度 */
  .mobile-menu {
    width: 100%;
    border-radius: 0;
  }
  
  /* 进一步优化小屏幕显示宽度 */
  .main {
    width: 98%;
  }
  
  .contentRouter {
    margin: 5px 4px 10px 4px !important;
    padding: 8px 10px 6px 10px !important;
  }
}

.sun-mode .rightHeaderItemSecond {
  color: #2c2d2d;
}

.sun-mode .rightHeaderItemSecond.scrolled {
  color: #4a5568;
}

.sun-mode .leftHeader span {
  color: #2d3748;
}

.sun-mode .blogName {
  color: #2d3748;
}

.sun-mode .blogDescRight {
  color: #2d3748;
}

.sun-mode .blogDesc {
  color: #4a5568;
}

.sun-mode .snake {
  filter: brightness(115%) contrast(100%);
}

.sun-mode .introduction hr {
  background: linear-gradient(to right, transparent, #2d3748, transparent);
}

.sun-mode .introduction {
  background-color: rgba(255, 255, 255, 0.5);
  color: #2d3748;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.sun-mode .introduction h4 {
  color: #2d3748;
}



.sun-mode .introduction p {
  color: #4a5568;
}


.sun-mode .blogName:hover {
  cursor: pointer;
  color: #409EFF;
}

.sun-mode .friendLinkItem:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

.sun-mode .friendLinkName {
  color: #2d3748;
}

.sun-mode .friendLinkDesc {
  color: #4a5568;
}

.sun-mode .friendLink {
  background-color: rgba(255, 255, 255, 0.5);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(0, 0, 0, 0.05);
}

/* 明亮模式适配 */
.sun-mode .friendLink span {
  color: #2d3748;
}

.sun-mode .friendLinkIcon {
  color: #2d3748;
}

.sun-mode .friendLinkLine {
  border: 1px solid rgb(156, 156, 156);
}

.sun-mode .tag {
  background-color: rgba(255, 255, 255, 0.5);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.sun-mode .tagTitle {
  color: #2d3748;
}

.sun-mode .footer {
  background-color: #f5f5f5;
  color: #4a5568;
}

.sun-mode .footer a {
  color: #4a5568;
}

.sun-mode .footer a:hover {
  color: #005eff;
}
/* 赞助窗口样式 */
.sponsor-container {
  padding: 10px;
}

.sponsor-item {
  text-align: center;
}

.sponsor-image {
  max-width: 100%;
  max-height: 400px;
  height: auto;
  display: block;
  margin: 0 auto;
  object-fit: contain;
}

.sponsor-info {
  margin-top: 15px;
  color: #606266;
}

/* 响应式优化 */
@media (min-width: 769px) {
  /* 在大屏幕上限制最大宽度 */
  :deep(.el-dialog) {
    max-width: 500px;
  }
}

@media (max-width: 768px) {
  .sponsor-image {
    max-height: 300px;
  }
}

@media (max-width: 480px) {
  .sponsor-image {
    max-height: 250px;
  }
  
  .sponsor-info p {
    font-size: 12px;
  }
}
</style>
