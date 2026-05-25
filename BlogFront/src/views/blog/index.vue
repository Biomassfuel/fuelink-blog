<script setup>
import { ref, watch, onMounted, nextTick, onBeforeUnmount } from 'vue'
import { queryPageApi, updateBlogStatusApi, deleteBlogApi, deleteBlogsApi ,queryBlogByIdApi,updateBlogApi,changeTopApi} from '@/api/blog'
import { ElBacktop, ElMessage, ElMessageBox, ElUpload } from 'element-plus'
import {uploadFileApi,getAllLabelApi} from '@/api/write'

// 数据
const List = ref([])

// 分页配置
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const searchEmp = ref({
  name: '',
  status: '',
  date: [],
  begin: '',
  end: '',
})




watch(() => searchEmp.value.date, (newVal, oldVal) => {
  if (newVal.length > 0) {
    searchEmp.value.begin = newVal[0]
    searchEmp.value.end = newVal[1]
  } else {
    searchEmp.value.begin = ''
    searchEmp.value.end = ''
  }
})

const tableRef = ref()
const tableWidth = ref(0)
const t1 = ref()
const t2 = ref()
const t3 = ref()
const t4 = ref()
const t5 = ref()
const t6 = ref()
const t7 = ref()
const t8 = ref()
const t9 = ref()

// 获取表格宽度
const getTableWidth = () => {
  if (tableRef.value) {
    tableWidth.value = tableRef.value.$el.offsetWidth
    t1.value = tableWidth.value * 0.04
    t2.value = tableWidth.value * 0.06
    t3.value = tableWidth.value * 0.1
    t4.value = tableWidth.value * 0.075
    t5.value = tableWidth.value * 0.125
    t6.value = tableWidth.value * 0.075
    t7.value = tableWidth.value * 0.15
    t8.value = tableWidth.value * 0.15
    // t9.value = tableWidth.value *0.18
  }
}

// 查询
const handleSearch = async () => {

  const result = await queryPageApi(
    searchEmp.value.name,
    searchEmp.value.status,
    searchEmp.value.begin,
    searchEmp.value.end,
    currentPage.value,
    pageSize.value
  );
  if (result.code) {
    List.value = result.data.row
    total.value = result.data.total
  }
}
const openWin = ref(false)
const textContent = ref('')
const cherryInstance = ref(null)
const open = (content) => {
  openWin.value = true
  textContent.value = content
  nextTick(() => {
    if (!cherryInstance.value) {
      // 只创建一次实例
      cherryInstance.value = new Cherry({
        id: 'markdown-container',
        value: textContent.value,
        editor: {
          defaultModel: "previewOnly"
        }

      })
    } else {
      // 如果实例已存在，更新内容
      cherryInstance.value.setMarkdown(textContent.value)
    }
  })
}

//重置表单
const handleReset = () => {
  searchEmp.value = {
    name: '',
    status: '',
    date: [],
    begin: '',
    end: '',
  }
  handleSearch()
}

//状态改变
const ChangeStatus = async (id, status) => {
  try {
    await updateBlogStatusApi(id, status);

    ElMessage.success('状态更新成功');
    handleSearch();
  } catch (error) {
    ElMessage.error('状态更新失败');
  }
};

//删除
const handleDelete = async (id) => {
  try {
    ElMessageBox.confirm('确认删除该博客吗？', '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }).then(async () => {
      await deleteBlogApi(id);
      ElMessage.success('删除成功');
      handleSearch();
    });

  } catch (error) {
    ElMessage.error('删除失败');
  }
};

// 存储选中的 ID
const selectedIds = ref([]);

// 处理复选框选择变化的函数
function handleSelectionChange(selection) {
  const ids = selection.map(item => item.id);
  selectedIds.value = ids;
}


//批量删除deleteByIds
const deleteByIds = async () => {
  //弹出一个确认框, 如果确认, 就删除;
  ElMessageBox.confirm('确定删除选中博客吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    // 删除员工
    const result = await deleteBlogsApi(selectedIds.value);
    if (result.code) {
      ElMessage.success('删除博客成功')
      handleSearch()
    } else {
      ElMessage.error(result.msg)
    }

  })
}

let options = ref([])
// 获取所有标签
const loadLabels = async () => {
  const res = await getAllLabelApi()
  options.value = res.data.map(item => ({
    value: item.id,
    label: item.name
  }))
}

//编辑
const editWin = ref(false)
const formModel = ref({
  id: '',
  title: '',
  summary: '',
  // isTop: '',
  tags: '',
  content: '',
  imageUrl: '',
})
const cherryInstanceEdit = ref(null)
const handleEdit = async (id) => {
  const result = await queryBlogByIdApi(id)
  if (result.code) {
    formModel.value.id = result.data.id
    formModel.value.title = result.data.title
    formModel.value.summary = result.data.summary
    // formModel.value.isTop = result.data.isTop
    // formModel.value.tags = result.data.tags
    if (Array.isArray(result.data.tags)) {
        formModel.value.tags = result.data.tags;
      } else if (typeof result.data.tags === 'string') {
        // 如果后端返回的是字符串格式，需要转换为数组
        try {
          formModel.value.tags = JSON.parse(result.data.tags);
        } catch (e) {
          formModel.value.tags = result.data.tags ? [result.data.tags] : [];
        }
      } else {
        formModel.value.tags = [];
      }
    formModel.value.content = result.data.content
    formModel.value.imageUrl = result.data.imageUrl
  }
  editWin.value = true
  loadLabels()

  nextTick(() => {
    if (!cherryInstanceEdit.value) {
      // 只创建一次实例
      cherryInstanceEdit.value = new Cherry({
        id: 'markdown-container-edit',
        value: formModel.value.content,
        fileUpload: myFileUpload,
        toolbars: {
          toolbar: ['bold', 'italic', 'strikethrough', '|', 'color', 'header', 'ruby', '|', 'list', 'panel', 'detail', 'codeTheme', { insert: ['image', 'video', 'link', 'hr', 'br', 'code', 'formula', 'toc', 'table', 'pdf', 'word'] }, 'graph', 'shortcutKey', 'togglePreview', 'undo', 'redo'],
          toolbarRight: ['fullScreen',],
          sidebar: ['theme', 'mobilePreview', 'copy'],
          float: ['h1', 'h2', 'h3', '|', 'checklist', 'quote', 'table', 'code']
        },
        fileUpload: myFileUpload,

      })
    } else {
      // 如果实例已存在，更新内容
      cherryInstanceEdit.value.setMarkdown(formModel.value.content)

    }
  })

}

onBeforeUnmount(() => {
  if (cherryInstanceEdit.value && typeof cherryInstanceEdit.value.destroy === 'function') {
    cherryInstanceEdit.value.destroy();
  }
  cherryInstanceEdit.value = null;
});

/**
 * 上传文件函数
 * @param file 上传文件的文件对象
 * @param callback 回调函数，回调函数接收两个参数，第一个参数为文件上传后的url，第二个参数可选，为额外配置信息
 */
function myFileUpload(file, callback) {
    // 先把文件上传到服务端，上传文件的具体代码需要自行实现
    putFile(file, function(err, url, file) {
        if (err) {
            // 上传失败
            callback(null, null, file);
            ElMessage.error('上传失败')
        } else {
            // 如果上传的是视频
            if (/video/i.test(file.type)) {
                callback(url, {
                name: '视频',
                // poster: `${url}?poster=true`, // 视频的封面图片url
                isBorder: true, // 是否显示边框，默认false
                isShadow: true, // 是否显示阴影，默认false
                isRadius: true, // 是否显示圆角，默认false
                width: '100%',
                height: 'center', // 视频的高度，默认auto
                });
            } else if (/image/i.test(file.type)) {
                // 如果上传的是图片
                callback(url, {
                    name: '图片',
                    isBorder: true, // 是否显示边框，默认false
                    isShadow: true, // 是否显示阴影，默认false
                    isRadius: true, // 是否显示圆角，默认false
                    width: '100%', // 图片的宽度，默认100%，可配置百分比，也可配置像素值
                    height: 'center', // 图片的高度，默认auto
                });
            } else {
                // 如果上传的是文件
                callback(url, {name: file.name});
            }
        }
    });
}

// 文件上传函数
const putFile = (file, callback) => {
  // 创建 FormData 对象
  const formData = new FormData();
  formData.append('file', file);
  
  // 调用上传 API
  uploadFileApi(formData)
    .then(response => {
      if (response.code ) {
        callback(null, response.data, file);
      } else {
        callback(new Error('上传失败'), null, file);
      }
    })
    .catch(error => {
      callback(error, null, file);
    });
};

// 图片上传成功后触发
const handleAvatarSuccess = (response) => {
  formModel.value.imageUrl = response.data  
}
// 文件上传之前触发
const beforeAvatarUpload = (rawFile) => {
  if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
    ElMessage.error('只支持上传图片')
    return false
  } else if (rawFile.size / 1024 / 1024 > 10) {
    ElMessage.error('只能上传10M以内图片')
    return false
  }
  return true
}
// 封面图片自定义上传方法
const handleAvatarUpload = (params) => {
  const formData = new FormData()
  formData.append('file', params.file)
  
  uploadFileApi(formData)
    .then(response => {
      if (response.code) {
        params.onSuccess(response)
      } else {
        params.onError()
      }
    })
    .catch(error => {
      params.onError(error)
    })
}

//规则
const rules = ref({
  title: [
    { required: true, message: '请输入文章标题', trigger: 'blur' }
  ],
  summary: [
    { required: true, message: '请输入文章摘要', trigger: 'blur' }
  ],
  tags: [
    { required: true, message: '请选择标签', trigger: 'change' }
  ],
  imageUrl: [
    { required: true, message: '请上传封面图', trigger: 'change' }
  ]
})

//分页组件
const pagination = ref({ currentPage: 1, pageSize: 10, total: 0 })
//每页展示记录数发生变化时触发
const handleSizeChange = (pageSizes) => {
  pageSize.value = pageSizes
  handleSearch()
}
//当前页码发生变化时触发
const handleCurrentChange = (page) => {
  currentPage.value = page
  handleSearch()
}

//修改saveEdit
const saveEdit = () => {
  // 使用表单验证
  formRef.value.validate((valid) => {
    if (valid) {
      if(!cherryInstanceEdit.value.getMarkdown()){
        ElMessage.error('请输入内容')
        return
      }
      const formData = {
        id: formModel.value.id,
        title: formModel.value.title,
        summary: formModel.value.summary,
        // isTop: formModel.value.isTop,
        tags: '[' + formModel.value.tags.join(',') + ']',
        // content: cherryInstanceEdit.getMarkdown(),
        content: cherryInstanceEdit.value.getMarkdown(),
        imageUrl: formModel.value.imageUrl,
      }
      updateBlogApi(formData).then(res => {
        if (res.code) {
          ElMessage.success('编辑成功')
          editWin.value = false
          handleSearch()
        }
        else {
          ElMessage.error('编辑失败')
        }
      })
    } else {
      ElMessage.error('请完善表单信息');
    }
  });
}

const formRef = ref()
//editWin关闭时触发
const editWinClose = (done) => {
  // 确定关闭窗口

  ElMessageBox
    .confirm('确定关闭窗口吗？未保存的更改将会丢失', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    .then(() => {
      // 确认关闭窗口，重置表单
      done()
      formRef.value.resetFields()
      
    })
    .catch(() => {
      // 取消关闭窗口
    });
}

//修改置顶
const ChangeTop = async (id, isTop) => {
  try {
    const res = await changeTopApi(id, isTop)
    if (res.code) {
      ElMessage.success('修改成功')
      handleSearch()
    }
    else {
      ElMessage.error('修改失败')
    }
  } catch (error) {
    ElMessage.error('修改失败')
  }
}


onMounted(() => {
  getTableWidth()
  handleSearch()

})


</script>

<template>


  <h1>文章管理</h1> <br>
  <el-form :inline="true" :model="searchEmp">
    <el-form-item label="标题关键词">
      <el-input v-model="searchEmp.name" placeholder="请输入文章标题关键词"></el-input>
    </el-form-item>

    <el-form-item label="展示状态">
      <el-select v-model="searchEmp.status" placeholder="请选择">
        <el-option label="展示" value="1"></el-option>
        <el-option label="不展示" value="2"></el-option>
      </el-select>
    </el-form-item>

    <el-form-item label="发布日期">
      <el-date-picker v-model="searchEmp.date" type="daterange" range-separator="至" start-placeholder="开始日期"
        end-placeholder="结束日期" value-format="YYYY-MM-DD"></el-date-picker>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="handleSearch">查询</el-button>
      <el-button @click="handleReset">清空</el-button>
    </el-form-item>
  </el-form>


  <el-button type="danger" @click="deleteByIds"> - 批量删除</el-button>
  <br><br>

  <!-- 表格 -->
  <el-table ref="tableRef" :data="List" border style="width: 100%" @selection-change="handleSelectionChange">
    <el-table-column type="selection" :width="t1" align="center"></el-table-column>
    <el-table-column label="置顶" :width="t2" align="center">
      <template #default="scope">
        <!-- {{ scope.row.isTop == 1 ? '是' : '否' }} -->
        <el-button v-if="scope.row.isTop == 1" type="success" plain size="small" @click="ChangeTop(scope.row.id, 0)">是</el-button>
        <el-button v-else type="danger" plain size="small" @click="ChangeTop(scope.row.id, 1)">否</el-button>
      </template>
    </el-table-column>
    <el-table-column prop="title" label="标题" :width="t3" align="center" show-overflow-tooltip></el-table-column>
    <el-table-column prop="content" label="内容" :width="t4" align="center">
      <template #default="scope">
        <el-button size="small" type="primary" plain @click="open(scope.row.content)">显示</el-button>
      </template>
    </el-table-column>


    <el-table-column label="标签" :width="t5" align="center" show-overflow-tooltip prop="tags">
      
    </el-table-column>
    <el-table-column prop="status" label="展示状态" :width="t6" align="center">
      <template #default="scope">
        <!-- <el-tag v-if="scope.row.status == 1" type="success">展示</el-tag> -->
        <!-- <el-tag v-else type="danger">不展示</el-tag> -->
        <!-- 改成按钮 -->
        <el-button v-if="scope.row.status == 0" type="danger" @click="ChangeStatus(scope.row.id, 1)"
          size="small">不展示</el-button>
        <el-button v-else type="success" @click="ChangeStatus(scope.row.id, 0)" size="small">展示</el-button>
      </template>
    </el-table-column>
    <el-table-column prop="createTime" label="发布日期" :width="t7" align="center"></el-table-column>
    <el-table-column prop="updateTime" label="最后更新时间" :width="t8" align="center"></el-table-column>
    <el-table-column label="操作" fixed="right" align="center">
      <template #default="scope">
        <el-button size="small" type="primary" @click="handleEdit(scope.row.id)">编辑</el-button>
        <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  <br />

  <!-- 分页 -->
  <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage"
    :page-sizes="[10, 20, 30, 40]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
    :total="total">
  </el-pagination>

  <el-dialog v-model="openWin" title="文章内容" style="background-color:#a8e6e6;" width="45%">
    <!-- <el-form :model="textContent"> -->
    <!-- <h1>content: {{ textContent }}</h1> -->
    <div id="markdown-container"></div>
    <!-- </el-form> -->
  </el-dialog>
  
  <el-dialog v-model="editWin"  :before-close="editWinClose" title="编辑博客" width="45%">
     
      <el-form :model="formModel" :rules="rules" ref="formRef">
      <el-form-item label="标题" prop="title">
        <el-input v-model="formModel.title" placeholder="请输入文章标题"></el-input>
      </el-form-item>
      <el-form-item label="摘要" prop="summary">
        <el-input v-model="formModel.summary" type="textarea" placeholder="请输入文章摘要"></el-input>
      </el-form-item>
      <el-form-item label="标签" prop="tags">
        <el-select v-model="formModel.tags"  multiple placeholder="请选择标签">
          <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </el-form-item prop="imageUrl" label="图片">
      <el-upload class="avatar-uploader" :http-request="handleAvatarUpload"
              :show-file-list="false" :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">
              <img v-if="formModel.imageUrl" :src="formModel.imageUrl" class="avatar" />
              <el-icon v-else class="avatar-uploader-icon">
                <Plus />
              </el-icon>
            </el-upload>
        <div id="markdown-container-edit"></div>
         
    </el-form>
    
      <div style="text-align: right; margin: 20px 0 5px;">
        <el-button @click="editWin = false" size="large" type="info">取消</el-button>
        <el-button type="primary" @click="saveEdit" size="large" color="#626aef">保存</el-button>
      </div>
    
  </el-dialog>
  
  
</template>

<style scoped>


</style>
