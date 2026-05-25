package cn.zspt.blogback.Controller.front;

import cn.zspt.blogback.Service.BlogFrontService;
import cn.zspt.blogback.Service.FriendService;
import cn.zspt.blogback.dto.BlogAllQueryParam;
import cn.zspt.blogback.entity.BlogEntity;
import cn.zspt.blogback.entity.FriendEntity;
import cn.zspt.blogback.result.Result;
import cn.zspt.blogback.vo.BlogArchiveResultVO;
import cn.zspt.blogback.vo.BlogResultVO;
import cn.zspt.blogback.vo.LabelResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping
public class BlogFrontController {
    @Autowired
    private BlogFrontService blogFrontService;
    @Autowired
    private FriendService friendService;


    @GetMapping("/allLabel")
    public Result<List<LabelResultVO>> getAllLabel(){
        return Result.success(blogFrontService.getAllLabel());
    }
    @GetMapping("/allBlog")
    public Result<BlogResultVO> getAllBlog(BlogAllQueryParam blogAllQueryParam){
        log.info("获取所有文章{}", blogAllQueryParam);
        BlogResultVO<BlogEntity> blogResultVO = blogFrontService.getAllBlog(blogAllQueryParam);
        return Result.success(blogResultVO);
    }
    @GetMapping("/front/blog")
    public Result<BlogEntity> getBlogById(Integer id){
        log.info("获取文章{}", id);
        return Result.success(blogFrontService.getBlogById(id));
    }
    @GetMapping("/front/blog/viewCountAdd")
    public Result<String> viewCountAdd(Integer id){
        log.info("文章{}浏览数加1", id);
        blogFrontService.viewCountAdd(id);
        return Result.success("成功");
    }
    @GetMapping("/archive")
    public Result<List<BlogArchiveResultVO>> archive(){
        log.info("获取归档");
        List<BlogArchiveResultVO> blogArchiveResultVO = blogFrontService.archive();
        log.info("归档结果{}", blogArchiveResultVO);
        return Result.success(blogArchiveResultVO);
    }

    @GetMapping("/friend/all")
    public Result<List<FriendEntity> > getAllFriend(){
        return Result.success(friendService.getAllFriend());
    }
}
