package cn.zspt.blogback.Controller.back;

import cn.zspt.blogback.Service.BlogService;
import cn.zspt.blogback.dto.BlogDTO;
import cn.zspt.blogback.dto.BlogQueryParam;
import cn.zspt.blogback.entity.BlogEntity;
import cn.zspt.blogback.result.Result;
import cn.zspt.blogback.utils.AliOssUtil;
import cn.zspt.blogback.vo.BlogResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/backend/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private AliOssUtil aliOssUtil;



    //上传图片
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传：{}",file);
        try {
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            //截取原始文件名的后缀
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //构造新文件的名称
            String objectName = UUID.randomUUID().toString() + extension;

            //文件的请求路径
            String filePath = aliOssUtil.upload(file.getBytes(),objectName);
            return Result.success(filePath);
        } catch (IOException e) {
            log.error("文件上传失败：{}",e.getMessage());
        }
        return Result.error("文件上传失败");
    }

    //添加文章
    @PostMapping
    public Result<String> addBlog(@RequestBody BlogDTO blogDTO){
        log.info("添加文章{}", blogDTO);
        blogService.addBlog(blogDTO);
        return Result.success("添加成功");
    }

    //查询文章
    @GetMapping
    public Result<BlogResultVO> getBlog(BlogQueryParam blogQueryParam){
        BlogResultVO<BlogEntity> blogResultVO = blogService.getBlog(blogQueryParam);
        return Result.success(blogResultVO);
    }

    //改变状态
    @PutMapping("/status/{id}/{status}")
    public Result<String> changeStatus(@PathVariable Integer id, @PathVariable Integer status){
        log.info("改变文章{}的状态为{}",id,status);
        blogService.changeStatus(id,status);
        return Result.success("修改成功");
    }

    //删除文章
    @DeleteMapping("/{id}")
    public Result<String> deleteBlog(@PathVariable Integer id){
        log.info("删除文章{}",id);
        blogService.deleteBlog(id);
        return Result.success("删除成功");
    }
    //批量删除文章
    @DeleteMapping
    public Result<String> deleteBlog(@RequestParam List<Integer> id){
        log.info("批量删除文章{}",id);
        for (Integer i : id) {
            blogService.deleteBlog(i);
        }
        return Result.success("删除成功");
    }
    //获取数据
    @GetMapping("/{id}")
    public Result<BlogDTO> getBlogAll(@PathVariable Integer id){
        log.info("获取文章{}",id);
        BlogDTO blogEntity = blogService.getBlogAll(id);
        log.info("获取文章{}成功",blogEntity);
        return Result.success(blogEntity);
    }

    //修改文章
    @PutMapping
    public Result<String> updateBlog(@RequestBody BlogEntity blogEntity){
        log.info("修改文章{}",blogEntity);
        blogService.updateBlog(blogEntity);
        return Result.success("修改成功");
    }

    //修改置顶
    @PutMapping("/isTop/{id}/{isTop}")
    public Result<String> changeTop(@PathVariable Integer id, @PathVariable Integer isTop){
        log.info("改变文章{}的状态为{}",id,isTop);
        blogService.changeTop(id,isTop);
        return Result.success("修改成功");
    }
}
