package cn.zspt.blogback.Controller.back;

import cn.zspt.blogback.Service.FriendService;
import cn.zspt.blogback.dto.FriendQueryParam;
import cn.zspt.blogback.dto.LabelQueryParam;
import cn.zspt.blogback.entity.FriendEntity;
import cn.zspt.blogback.entity.LabelEntity;
import cn.zspt.blogback.result.Result;
import cn.zspt.blogback.vo.PageResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("backend/friend")
public class FriendController {
    @Autowired private FriendService friendService;

    @PostMapping
    public Result<String> addFriend(@RequestBody FriendEntity friendEntity){
        friendService.addFriend(friendEntity);
        return Result.success("添加成功");

    }

    @GetMapping
    public Result<PageResultVO> getLabel(FriendQueryParam friendQueryParam){
        PageResultVO<FriendEntity> pageResultVO = friendService.getFriend(friendQueryParam);
        return Result.success(pageResultVO);
    }

    @GetMapping("/{id}")
    public Result<FriendEntity> getLabelById(@PathVariable Integer id){
        return Result.success(friendService.getFriendById(id));
    }

    @PutMapping("/{id}")
    public Result<String> updateLabel(@PathVariable Integer id, @RequestBody FriendEntity friendEntity){
        log.info("修改友链{}", friendEntity);
        friendService.updateFriend(friendEntity, id);
        return Result.success("修改成功");
    }
    @DeleteMapping("/{id}")
    public Result<String> deleteLabel(@PathVariable Integer id){
        friendService.deleteFriend(id);
        return Result.success("删除成功");
    }
    //前台
//    @GetMapping("/all")
//    public Result<List<FriendEntity> > getAllFriend(){
//        return Result.success(friendService.getAllFriend());
//    }
}
