package cn.zspt.blogback.Mapper;

import cn.zspt.blogback.dto.BlogAllQueryParam;
import cn.zspt.blogback.entity.BlogEntity;
import cn.zspt.blogback.vo.LabelResultVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface BlogFrontMapper {
    List<LabelResultVO> getAllLabel();

    List<BlogEntity> getAllBlog(BlogAllQueryParam blogAllQueryParam);

    BlogEntity getBlogById(Integer id);

    @Update("update articles set view_count = view_count + 1 where id = #{id}")
    void viewCountAdd(Integer id);

    List<BlogEntity> getAllBlogNoPage();
}
