package cn.zspt.blogback.Mapper;

import cn.zspt.blogback.dto.FriendQueryParam;
import cn.zspt.blogback.entity.FriendEntity;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FriendMapper {
    @Insert("insert into friend(title, imgurl, `desc`, siteurl ) values(#{title},#{imgurl},#{desc},#{siteurl})" )
    void addFriend(FriendEntity friendEntity);

    @Select("select * from friend")
    List<FriendEntity> getLabel(FriendQueryParam friendQueryParam);

    @Select("select * from friend where id=#{id}")
    FriendEntity getFriendById(Integer id);

    @Select("update friend set title=#{friendEntity.title},imgurl=#{friendEntity.imgurl},`desc`=#{friendEntity.desc},siteurl=#{friendEntity.siteurl} where id=#{id}")
    void updateFriend(FriendEntity friendEntity, Integer id);

    @Select("delete from friend where id= #{id}")
    void deleteFriend(Integer id);

    @Select("select * from friend")
    List<FriendEntity> getAllFriend();
}
