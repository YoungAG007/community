package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    // @Param注解用于给参数取别名,
    // 如果只有一个参数,并且在<if>里使用,则必须加别名.
    int selectDiscussPostRows(@Param("userId") int userId);
    //增加帖子的方法
    int insertDiscussPost(DiscussPost discussPost);
    //根据帖子id查询指定的帖子
    DiscussPost selectDiscussPostById(int id);
    //更新帖子评论数量的方法
    int updateCommentCount(int id,int commentCount);
}
