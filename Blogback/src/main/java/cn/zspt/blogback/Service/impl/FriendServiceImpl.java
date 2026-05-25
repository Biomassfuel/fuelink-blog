package cn.zspt.blogback.Service.impl;

import cn.zspt.blogback.Mapper.FriendMapper;
import cn.zspt.blogback.Service.FriendService;
import cn.zspt.blogback.dto.FriendQueryParam;
import cn.zspt.blogback.entity.FriendEntity;
import cn.zspt.blogback.entity.LabelEntity;
import cn.zspt.blogback.vo.PageResultVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FriendServiceImpl implements FriendService {
    @Autowired private FriendMapper friendMapper;

    @Override
    public void addFriend(FriendEntity friendEntity) {
        friendMapper.addFriend(friendEntity);
    }

    @Override
    public PageResultVO<FriendEntity> getFriend(FriendQueryParam friendQueryParam) {
        PageHelper.startPage(friendQueryParam.getPage(), friendQueryParam.getPageSize());
        List<FriendEntity> friendList= friendMapper.getLabel(friendQueryParam);
        Page<FriendEntity> page = (Page<FriendEntity>) friendList;
        return new PageResultVO<FriendEntity>(page.getTotal(), page.getResult());
    }

    @Override
    public FriendEntity getFriendById(Integer id) {
        return friendMapper.getFriendById(id);
    }

    @Override
    public void updateFriend(FriendEntity friendEntity, Integer id) {
        friendMapper.updateFriend(friendEntity, id);
    }

    @Override
    public void deleteFriend(Integer id) {
        friendMapper.deleteFriend(id);
    }

    @Override
    public List<FriendEntity>  getAllFriend() {
        return friendMapper.getAllFriend();
    }


}
