package cn.zspt.blogback.Mapper;

import cn.zspt.blogback.entity.LoginEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoginMapper {


    @Select("select * from users where username = #{username}")
    LoginEntity getLogin(String username);

    @Select("select count(*) from users where username = #{username}")
    int countByUsername(String username);

    @Insert("insert into users (id, username, password) values (#{id}, #{username}, #{password})")
    int insert(LoginEntity loginEntity);
}
