package cn.zspt.blogback.Service;

import cn.zspt.blogback.dto.BlogAllQueryParam;
import cn.zspt.blogback.entity.BlogEntity;
import cn.zspt.blogback.vo.BlogArchiveResultVO;
import cn.zspt.blogback.vo.BlogResultVO;
import cn.zspt.blogback.vo.LabelResultVO;

import java.util.List;

public interface BlogFrontService {
    List<LabelResultVO> getAllLabel();

    BlogResultVO<BlogEntity> getAllBlog(BlogAllQueryParam blogAllQueryParam);

    BlogEntity getBlogById(Integer id);

    void viewCountAdd(Integer id);

    List<BlogArchiveResultVO> archive();
}
