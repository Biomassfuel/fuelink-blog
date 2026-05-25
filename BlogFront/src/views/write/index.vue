<script setup>
import { ref, onMounted, onBeforeUnmount } from "vue";
import 'cherry-markdown/dist/cherry-markdown.css';
import Cherry from 'cherry-markdown';
import { ElMessage } from 'element-plus';

import {addBlogApi,getAllLabelApi,uploadFileApi} from '@/api/write'


const formModel = ref({
  title: '',
  summary: '',
  isTop: 0,
  tags: [],
  imageUrl: '',
})


let options = ref([])

// const submitForm = () => {
//   // 提交表单逻辑
//   const formData = {
//     title: inputtitle.value,
//     summary: summary.value,
//     isTop: isTop.value,
//     tags: tags.value.join(','),
//     content: summary.value,
//     img: imageUrl.value,
//   }
//   addBlogApi(formData).then(res => {
//     if (res.code) {
//       ElMessage.success('添加成功')
//     }
//   })

// }

const formRef = ref(); // 添加表单引用

const submitForm = () => {
  // 使用表单验证
  formRef.value.validate((valid) => {
    if (valid) {
      if(!cherryInstance.getMarkdown()){
        ElMessage.error('请输入内容')
        return
      }
      const formData = {
        title: formModel.value.title,
        summary: formModel.value.summary,
        isTop: formModel.value.isTop,
        tags: '[' + formModel.value.tags.join(',') + ']',
        content: cherryInstance.getMarkdown(),
        imageUrl: formModel.value.imageUrl,
      }
      addBlogApi(formData).then(res => {
        if (res.code) {
          ElMessage.success('发布成功')
          resetForm()
        }
        else {
          ElMessage.error('发布失败')
        }
      })
    } else {
      ElMessage.error('请完善表单信息');
    }
  });
}



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




let cherryInstance = null;
onMounted(() => {
  // 在组件挂载后创建编辑器
  cherryInstance = new Cherry({
    id: 'markdown-container',
    value: '# 博客文章写入示例\n\n这是博客文章写入的示例。',
     fileUpload: myFileUpload,
    toolbars: {
       toolbar: ['bold','italic','strikethrough','|','color','header','ruby','|','list','panel','detail' ,'codeTheme',{insert: ['image',  'video', 'link', 'hr', 'br', 'code', 'formula', 'toc', 'table', 'pdf','word']},'graph','shortcutKey','togglePreview','undo','redo'],
          toolbarRight: ['fullScreen', ],
          sidebar: ['theme','mobilePreview', 'copy'],
          float:['h1', 'h2', 'h3', '|', 'checklist', 'quote', 'table', 'code']
      },
       fileUpload: myFileUpload,
  }
);
// 初始化标签
loadLabels();
});

// 获取所有标签
const loadLabels = async () => {
  const res = await getAllLabelApi()
  options.value = res.data.map(item => ({
    value: item.id,
    label: item.name
  }))
}



onBeforeUnmount(() => {
  if (cherryInstance && typeof cherryInstance.destroy === 'function') {
    cherryInstance.destroy();
  }
  cherryInstance = null;
});


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


// 重置表单
const resetForm = () => {
  formRef.value.resetFields();
  formModel.value = {
    title: '',
    summary: '',
    isTop: 0,
    tags: [],
    imageUrl: '',
  }
  cherryInstance.setMarkdown('# 博客文章写入示例\n\n这是博客文章写入的示例。')
}

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


</script>


<template>
  <div class="body">
    <el-form :model="formModel" :rules="rules" ref="formRef">
      <div class="top">
        <div class="leftTop">
          <h2 style="margin-bottom: 5px;">标题：</h2>
          <el-form-item prop="title">
            <el-input v-model="formModel.title" style="width: 100% " placeholder="请输入文章标题" />
          </el-form-item>
          <h2 style="margin-bottom: 5px;margin-top: 10px;">摘要：</h2>
          <el-form-item prop="summary">
            <el-input v-model="formModel.summary" style="width: 100%" :rows="5" type="textarea" placeholder="请输入文章摘要" />
          </el-form-item>
        </div>
        <div class="rightTop">
          <span style="font-size: 18px;">封面图：</span>
          <!-- <el-form-item prop="imageUrl"> -->
            <el-upload class="avatar-uploader" :http-request="handleAvatarUpload"
              :show-file-list="false" :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">
              <img v-if="formModel.imageUrl" :src="formModel.imageUrl" class="avatar" />
              <el-icon v-else class="avatar-uploader-icon">
                <Plus />
              </el-icon>
            </el-upload>
          <!-- </el-form-item> -->
          
          <div class="rightRight">
            <div>
              <span>是否置顶：</span>
              <el-switch v-model="formModel.isTop" size="large" active-text="Open"  :active-value="1" :inactive-value="0"/>
            </div>

            
            <div class="tag">
              <span>标签：</span>
              <el-form-item prop="tags">
                <el-select v-model="formModel.tags" multiple placeholder="请选择标签" style="width: 240px">
                  <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
                </el-select>
              </el-form-item>
            </div>
            <div >
              <el-button type="primary" @click="submitForm">提 交</el-button>
               <el-button type="danger" @click="resetForm">全 部 重 置</el-button>
            </div>
            
          </div>

        </div>
      </div>
      <!-- <el-form-item style="height: 100%;"> 
        <h2>内容：</h2>
      <div id="markdown-container" style="height: 100%;"></div>
      </el-form-item> -->
      
    </el-form>
    <h2>内容：</h2>
      <div id="markdown-container" ></div>
  </div>
</template>

<style scoped>

.body {
  display: flex;
  flex-direction: column;
  background-color: #f5f8fd;
  height: 100%;

  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 16px;
  min-height: 400px;
  width: 100%;
}

.top {
  display: flex;
  flex-direction: row;
}

.leftTop {
  width: 50%;
  display: block;
  /* justify-content: space-between; */
}

.rightTop {
  width: 50%;
  display: flex;
  margin-left: 20px;
}

.avatar-uploader .avatar {
  /* width: 178px; */
  /* height: 178px; */
  /* display: block; */
  width: 100%;
  height: 100%;
  display: block;
  object-fit: cover;
  /* 保持图片填充并裁剪超出部分 */

}

.avatar-uploader {
  width: 30%;
  max-width: 320px;
  min-width: 120px;
  aspect-ratio: 1 / 1;
  /* 保持正方形 */

  display: block;
}

.rightRight {
  margin-left: 35px;
}
.rightRight div {
  margin-bottom: 10px;
  margin-left: 5px;
}
.tag {
  display: flex;
  flex-direction: row;
}
.rightRight span{
  font-size: 18px;
}

</style>
<style>
.avatar-uploader .el-upload {
  border: 1px dashed rgb(100, 98, 98);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);

  width: 100%;
  height: 100%;
  box-sizing: border-box;
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  /* width: 178px; */
  /* height: 178px; */
  text-align: center;

  width: 100%;
  height: 100%;
  display: flex;
}
</style>
