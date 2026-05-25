package cn.zspt.blogback.Service;

import cn.zspt.blogback.dto.FriendQueryParam;
import cn.zspt.blogback.entity.FriendEntity;
import cn.zspt.blogback.vo.PageResultVO;

import java.util.List;

public interface FriendService {
    void addFriend(FriendEntity friendEntity);

    PageResultVO<FriendEntity> getFriend(FriendQueryParam friendQueryParam);

    FriendEntity getFriendById(Integer id);

    void updateFriend(FriendEntity friendEntity, Integer id);

    void deleteFriend(Integer id);

    List<FriendEntity>  getAllFriend();
}
