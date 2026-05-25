<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus';
import { useRouter } from 'vue-router'

let router = useRouter()

const loginName = ref('')
//定义钩子函数, 获取登录用户名
onMounted(() => {
  //获取登录用户名
  let loginUser = JSON.parse(localStorage.getItem('loginUser'))
  if (loginUser) {
    loginName.value = loginUser.name
  }
})

const logout = () => {
  //弹出确认框, 如果确认, 则退出登录, 跳转到登录页面
  ElMessageBox.confirm('确认退出登录吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {//确认, 则清空登录信息
    ElMessage.success('退出登录成功')
    localStorage.removeItem('loginUser')
    router.push('/login')//跳转到登录页面
  })
}
</script>

<template>
  <div class="common-layout">
    <el-container>
      <!-- Header 区域 -->
      <el-header class="header">
        <span class="title">博客后台管理系统</span>
        <span class="right_tool">
          <a href="">
            <el-icon><EditPen /></el-icon> 修改密码 &nbsp;&nbsp;&nbsp; |  &nbsp;&nbsp;&nbsp;
          </a>
          <a href="javascript:void(0)" @click="logout">
            <el-icon><SwitchButton /></el-icon> 退出登录 【{{ loginName }}】
          </a>
        </span>
      </el-header>
      
      <el-container>
        <!-- 左侧菜单 -->
        <el-aside  class="aside">

          <el-menu router>
            <!-- 首页菜单 -->
            <el-menu-item index="/index">
              <el-icon><Promotion /></el-icon> 首页
            </el-menu-item>
          
            
            <!-- 班级管理菜单 -->
            <el-sub-menu index="/manage">
              <template #title>
                <el-icon><Menu /></el-icon> 博客管理
              </template>
              <el-menu-item index="/blog">
                <el-icon><List /></el-icon>博客列表
              </el-menu-item>
              <el-menu-item index="/write">
                <el-icon><HelpFilled /></el-icon>写博客
              </el-menu-item>
              <el-menu-item index="/label">
                <el-icon><Management /></el-icon>标签管理
              </el-menu-item>
              <el-menu-item index="/sentence">
                <el-icon><CirclePlusFilled /></el-icon>循环语句
              </el-menu-item>
              <el-menu-item index="/friend">
                <el-icon><PictureFilled /></el-icon>友链管理
              </el-menu-item>
            </el-sub-menu>
            

            <!-- 数据统计管理 -->
            <el-sub-menu index="/report">
              <template #title>
                <el-icon><Histogram /></el-icon>数据统计管理
              </template>
              <el-menu-item index="/report/emp">
                <el-icon><InfoFilled /></el-icon>博客信息统计
              </el-menu-item>
              <el-menu-item index="/report/stu">
                <el-icon><Share /></el-icon>浏览信息统计
              </el-menu-item>
              <el-menu-item index="/log">
                <el-icon><Document /></el-icon>日志信息统计
              </el-menu-item>
            </el-sub-menu>
          </el-menu>
        </el-aside>
        
        <!-- 主展示区域 -->
        <el-main class="main">
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
.header {
  background-color: #409EFF;
}

.title {
  color: white;
  font-size: 40px;
  font-family: 楷体;
  line-height: 60px;
  font-weight: bolder;
}

.right_tool{
  float: right;
  line-height: 60px;
}

a {
  color: white;
  text-decoration: none;
}

.aside {
  /* width: 220px; */
  width: 15%;
  border-right: 1px solid #ccc;
  height: 100%;

}
.main {
  background-color: #f5f8fd;
}
.common-layout {
  height: 100vh;
}
/* 外层 el-container 占满 .common-layout */
.common-layout > .el-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

</style>
