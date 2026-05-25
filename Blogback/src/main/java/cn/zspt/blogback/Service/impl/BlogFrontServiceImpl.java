package cn.zspt.blogback.Service.impl;

import cn.zspt.blogback.Mapper.BlogFrontMapper;
import cn.zspt.blogback.Service.BlogFrontService;
import cn.zspt.blogback.dto.BlogAllQueryParam;
import cn.zspt.blogback.entity.BlogEntity;
import cn.zspt.blogback.utils.AliOssUtil;
import cn.zspt.blogback.vo.BlogArchiveResultVO;
import cn.zspt.blogback.vo.BlogResultVO;
import cn.zspt.blogback.vo.LabelResultVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BlogFrontServiceImpl implements BlogFrontService {
    @Autowired
    private BlogFrontMapper blogFrontMapper;
    @Autowired
    private AliOssUtil aliOssUtil;

    @Override
    public List<LabelResultVO> getAllLabel() {
        return blogFrontMapper.getAllLabel();
    }

    @Override
    public BlogResultVO<BlogEntity> getAllBlog(BlogAllQueryParam blogAllQueryParam) {
        PageHelper.startPage(blogAllQueryParam.getPage(), blogAllQueryParam.getPageSize());
        List<BlogEntity> blogList = blogFrontMapper.getAllBlog(blogAllQueryParam);
        blogList.forEach(this::replaceOssUrls);
        Page<BlogEntity> page = (Page<BlogEntity>) blogList;
        return new BlogResultVO<BlogEntity>(page.getTotal(), page.getResult());
    }

    @Override
    public BlogEntity getBlogById(Integer id) {
        BlogEntity blogEntity = blogFrontMapper.getBlogById(id);
        replaceOssUrls(blogEntity);
        return blogEntity;
    }

    @Override
    public void viewCountAdd(Integer id) {
        blogFrontMapper.viewCountAdd(id);
    }

    @Override
    public List<BlogArchiveResultVO> archive() {
        List<BlogEntity> list = blogFrontMapper.getAllBlogNoPage();
        list.forEach(this::replaceOssUrls);
        List<BlogArchiveResultVO> blogArchiveResultVO = list.stream()
                .collect(Collectors.groupingBy(
                        blog -> blog.getCreateTime().getYear(),
                        TreeMap::new,
                        Collectors.toList()
                ))
                .descendingMap().entrySet().stream()
                .map(entry -> new BlogArchiveResultVO(entry.getKey(), entry.getValue().size(), entry.getValue()))
                .collect(Collectors.toList());
        return blogArchiveResultVO;
    }

    private void replaceOssUrls(BlogEntity blogEntity) {
        if (blogEntity == null) {
            return;
        }

        blogEntity.setImageUrl(aliOssUtil.replaceWithPublicUrl(blogEntity.getImageUrl()));
        blogEntity.setContent(aliOssUtil.replaceWithPublicUrl(blogEntity.getContent()));
    }

}
