package cn.zspt.blogback.Service.impl;

import cn.zspt.blogback.Mapper.BlogMapper;
import cn.zspt.blogback.Service.BlogService;
import cn.zspt.blogback.dto.BlogDTO;
import cn.zspt.blogback.dto.BlogQueryParam;
import cn.zspt.blogback.dto.LabelQueryParam;
import cn.zspt.blogback.entity.BlogEntity;
import cn.zspt.blogback.utils.AliOssUtil;
import cn.zspt.blogback.vo.BlogResultVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private AliOssUtil aliOssUtil;


    @Override
    public void addBlog(BlogDTO blogDTO) {
        BlogEntity blogEntity = new BlogEntity();
        BeanUtils.copyProperties(blogDTO, blogEntity);
        blogEntity.setCreateTime(LocalDateTime.now());
        blogEntity.setUpdateTime(LocalDateTime.now());
        blogEntity.setViewCount(0);
        blogEntity.setStatus(1);
        blogMapper.addBlog(blogEntity);
        log.info("添加文章{}", blogEntity);
    }

    @Override
    public BlogResultVO<BlogEntity> getBlog(BlogQueryParam blogQueryParam) {
        PageHelper.startPage(blogQueryParam.getPage(), blogQueryParam.getPageSize());
        List<BlogEntity> blogList= blogMapper.getBlog(blogQueryParam);
        blogList.forEach(this::replaceOssUrls);
        Page<BlogEntity> page = (Page<BlogEntity>) blogList;
        return new BlogResultVO<BlogEntity>(page.getTotal(), page.getResult());
    }

    @Override
    public void changeStatus(Integer id, Integer status) {
        blogMapper.changeStatus(id, status);
    }

    @Override
    public void deleteBlog(Integer id) {
        blogMapper.deleteBlog(id);
    }

    @Override
    public BlogDTO getBlogAll(Integer id) {
        BlogEntity blogEntity = blogMapper.getBlogAll(id);
        replaceOssUrls(blogEntity);
        BlogDTO blogDTO = new BlogDTO();
        BeanUtils.copyProperties(blogEntity, blogDTO);
        return blogDTO;
    }

    @Override
    public void updateBlog(BlogEntity blogEntity) {
        BlogEntity blogEntityNew = new BlogEntity();
        BeanUtils.copyProperties(blogEntity, blogEntityNew);
        blogEntityNew.setUpdateTime(LocalDateTime.now());
        blogMapper.updateBlog(blogEntityNew);
    }

    @Override
    public void changeTop(Integer id, Integer isTop) {
        blogMapper.changeTop(id, isTop);
    }

    private void replaceOssUrls(BlogEntity blogEntity) {
        if (blogEntity == null) {
            return;
        }

        blogEntity.setImageUrl(aliOssUtil.replaceWithPublicUrl(blogEntity.getImageUrl()));
        blogEntity.setContent(aliOssUtil.replaceWithPublicUrl(blogEntity.getContent()));
    }
}
