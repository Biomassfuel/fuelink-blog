<script setup>
import { onMounted, ref, watch, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

import { addFriendApi, queryPageApi, getFriendByIdApi, updateFriendApi ,deleteFriendApi} from '@/api/friend'




// const formLabelWidth = '80px'
let timer

const dialog = ref(false)
const loading = ref(false)
//搜索条件
const friendForm = ref({
  title: '',
  desc: '',
  imgurl: '',
  siteurl: ''
})
//增加标签表单数据
const form = ref({
  title: '',
  desc: '',
  imgurl: '',
  siteurl: '',
  id: ''
})

const save = async () => {
  loading.value = true
  let result = null
  try {
    if (form.value.id) { 
      result = await updateFriendApi(form.value,form.value.id)
      dialog.value = false
      if (result.code) {
        ElMessage.success('修 改 成 功')
        queryPage()
        form.value = { title: '', desc: '', imgurl: '', siteurl: '', id: '' }
      }
    } else { 
      // 发送数据到后端
      result = await addFriendApi(form.value)
      // 成功后关闭对话框
      dialog.value = false
      if (result.code) {
        ElMessage.success('添 加 成 功')
        queryPage()
        form.value = { title: '', desc: '', imgurl: '', siteurl: '', id: '' }
      }
    }
  } catch (error) {
    ElMessage.error(result.msg)
  } finally {
    loading.value = false
  }
}


//加载所有标签
const tableData = ref([])

//分页条件查询
const queryPage = async () => {
  
  const result = await queryPageApi(
    pagination.value.currentPage,
    pagination.value.pageSize
  );

  if (result.code) {

    tableData.value = result.data.row
    pagination.value.total = result.data.total
  }
}
//钩子
onMounted(() => {
  queryPage()
});

const cancelForm = () => {
  loading.value = false
  dialog.value = false
  clearTimeout(timer)
  form.value = { name: '', status: 0 }

}

//分页组件
const pagination = ref({ currentPage: 1, pageSize: 10, total: 0 })
//每页展示记录数发生变化时触发
const handleSizeChange = (pageSize) => {
  pagination.value.pageSize = pageSize
  queryPage()
}
//当前页码发生变化时触发
const handleCurrentChange = (page) => {
  pagination.value.currentPage = page
  queryPage()
}

const drawer = ref(false)


//重置按钮
// const resetForm = () => {
//   friendForm.value.title = ''
//   friendForm.value.desc = ''
//   friendForm.value.imgurl = ''
//   friendForm.value.siteurl = ''
//   queryPage()
// }


const dialogTitle = ref('新 增 友 链')
//编辑标签
const updateLabel = async (id) => { 
  // 更改对话框标题
  dialogTitle.value = '编 辑 友 链'
  dialog.value = true
  // 加载标签详情
  const result = await getFriendByIdApi(id)
  if (result.code) {
    form.value = result.data
  }
}




// 删除标签
const delById = async (id) => { 
  ElMessageBox.confirm('确认删除该友链吗？', '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const result = await deleteFriendApi(id)
    if (result.code) {
      ElMessage.success('删除成功')
      queryPage()
    }
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '已取消删除'
    });
  });
}


</script>

<template>
  <div class="body">
   

    <div class="connect">
      <el-button @click="dialog = true" type="success" id="buttonAdd">
        + 新 增 友 链
      </el-button>


      <el-drawer v-model="dialog" size="30%" @close="cancelForm" direction="rtl" class="demo-drawer">
        <template v-slot:title>

          <h2>{{dialogTitle}}</h2>
        </template>
        <div class="demo-drawer__content">
          <el-form :model="form">
            <el-form-item label="名称" :label-width="formLabelWidth">
              <el-input v-model="form.title" autocomplete="off" placeholder="请输入好友名称" />

            </el-form-item>
             <el-form-item label="描述" :label-width="formLabelWidth">
              <el-input v-model="form.desc" autocomplete="off" placeholder="请输入好友描述" />
            </el-form-item>
             <el-form-item label="图标" :label-width="formLabelWidth">
              <el-input v-model="form.imgurl" autocomplete="off" placeholder="请输入好友图标" />
            </el-form-item>
             <el-form-item label="地址" :label-width="formLabelWidth">
              <el-input v-model="form.siteurl" autocomplete="off" placeholder="请输入好友地址" />
            </el-form-item>

          </el-form>
          <hr />
          <div class="demo-drawer__footer">
            <el-button @click="cancelForm">取 消</el-button>
            <el-button type="primary" :loading="loading" @click="save">
              {{ loading ? '提交中 ...' : '提交' }}
            </el-button>
          </div>
        </div>
      </el-drawer>

      <el-table :data="tableData" border style="width: 100%" fit>
        <el-table-column type="index" label="序号" width="55" align="center" />
        <el-table-column prop="title" label="友链名称" align="center" width="250px" />
        <el-table-column prop="desc" label="好友描述" align="center" width="150px"/>
        <el-table-column prop="siteurl" label="友链地址" align="center" width="250px" />

        
        <el-table-column prop="imgurl" label="友链图标" align="center" width="300px" />
        <el-table-column label="操作" align="center">
          <template #default="scope">
            <el-button type="primary" size="small" @click="updateLabel(scope.row.id)">编辑</el-button>
            <el-button type="danger" size="small" @click="delById(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <br />

      <!-- 分页组件Pagination -->
      <el-pagination v-model:current-page="pagination.currentPage" v-model:page-size="pagination.pageSize"
        :page-sizes="[5, 10, 20, 50, 100]" layout="total, sizes, prev, pager, next, jumper" :total="pagination.total"
        @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </div>


  </div>

</template>

<style scoped>
.body {
  display: flex;
  flex-direction: column;
  background-color: #f5f8fd;
}

.search {
  background-color: #ffffff;
  border-radius: 10px;
  border-radius: 10px;
  border-color: rgb(240, 236, 230);
  border-width: 1px;
  border-style: solid;
}

.search .el-form-item {
  margin: 20px
}

.connect {
  margin-top: 20px;
  background-color: #ffffff;
  border-radius: 10px;
  border-radius: 10px;
  border-color: rgb(240, 236, 230);
  border-width: 1px;
  border-style: solid;
  padding: 20px;
}

#buttonAdd {
  margin-bottom: 20px;
}

.demo-drawer__footer {
  text-align: right;
  margin-right: 20px;
  margin-top: 20px;
}
</style>
