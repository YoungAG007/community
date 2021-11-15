package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {
    //查询当前用户的会话列表，针对每个会话 只返回一条最新的私信
    List<Message> selectConversations(int userId,int offset, int limit);
    //查询当前用户的会话数量
    int selectConversationCount(int userId);
    //查询 某个会话所包含的 私信列表
    List<Message> selectLetters(String conversationId, int offset, int limit);
    //查询某个会话所包含的私信数量
    int selectLetterCount(String conversationId);
    //查询未读会话的数量 或 一个会话中未读私信的数量
    int selectLetterUnreadCount(int userId, String conversationId);
    //新增消息
    int insertMessage(Message message);
    //更改消息状态 将消息设置为已读 或者删除
    int updateStatus(List<Integer> ids, int status);

}
