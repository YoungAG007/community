package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    //根据实体类型查询评论
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);
    //查询评论数量
    int selectCountByEntity(int entityType,int entityId);
    //增加评论
    int insertComment(Comment comment);
    //根据评论id查询评论
    Comment selectCommentById(int id);
}
