package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.LikeServcie;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeServcie likeServcie;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "你还没有登录哦!");
        }

        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        System.out.println(post);
        int feedback = discussPostService.addDiscussPost(post);
        System.out.println(feedback);
        // 报错的情况,将来统一处理.
        return CommunityUtil.getJSONString(0, "发布成功!");
    }

    @RequestMapping(path = "/detail/{discussPostId}",method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page){
        //帖子
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", post);
        //帖子作者
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);

        //点赞数量
        long likeCount = likeServcie.findEntityLikeCount(ENTITY_TYPE_COMMENT,discussPostId);
        model.addAttribute("likeCount",likeCount);
        //点赞状态
        int likeStatus =hostHolder.getUser() == null? 0: likeServcie.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST,discussPostId);
        model.addAttribute("likeStatus",likeStatus);

        //评论分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(post.getCommentCount());


        //评论：给帖子的评论 回帖
        //回复：给评论的评论

        //回帖列表（评论列表）
        List<Comment> commentList = commentService.findCommentsByEntity(ENTITY_TYPE_POST, post.getId(), page.getOffset(), page.getLimit());
        //评论的VO列表
        List<Map<String,Object>> commentVoList = new ArrayList<>();
        if(commentList != null){
            for(Comment comment:commentList){
                //一个评论的VO
                Map<String, Object> commentVo = new HashMap<>();
                //评论
                commentVo.put("comment", comment);
                //作者
                commentVo.put("user", userService.findUserById(comment.getUserId()));
                //点赞数量
                likeCount = likeServcie.findEntityLikeCount(ENTITY_TYPE_COMMENT,comment.getId());
                commentVo.put("likeCount", likeCount);
                //点赞状态
                likeStatus =hostHolder.getUser() == null? 0: likeServcie.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT,comment.getId());
                commentVo.put("likeStatus", likeStatus);
                //回复列表
                List<Comment> replyList= commentService.findCommentsByEntity(ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                //回复VO列表
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if(replyList != null){
                    for(Comment reply : replyList){
                        Map<String, Object> replyVo = new HashMap<>();
                        //回复
                        replyVo.put("reply", reply);
                        //作者
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        //回复的目标
                        User target = reply.getTargetId() == 0 ? null:userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target);

                        //点赞数量
                        likeCount = likeServcie.findEntityLikeCount(ENTITY_TYPE_COMMENT,reply.getId());
                        replyVo.put("likeCount", likeCount);
                        //点赞状态
                        likeStatus =hostHolder.getUser() == null? 0: likeServcie.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT,reply.getId());
                        replyVo.put("likeStatus", likeStatus);

                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);
                //回复数量
                int replyCount = commentService.findCountByEntity(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount",replyCount);

                commentVoList.add(commentVo);
            }
        }
        model.addAttribute("comments", commentVoList);
        return "/site/discuss-detail";
    }

    @GetMapping("/discussPost/{userId}")
    public String getMyDiscussPost(@PathVariable("userId") int userId, Model model, Page page){
        //当前个人主页的作者
        User user = userService.findUserById(userId);
        model.addAttribute("user", user);
        //评论分页信息
        page.setLimit(5);
        page.setPath("/discuss/discussPost/" + userId);
        page.setRows(discussPostService.findDiscussPostRows(userId));

        //查找当前作者发布的帖子
        List<DiscussPost> myDiscussPosts = discussPostService.findDiscussPosts(userId, page.getOffset(), page.getLimit());

        //当前帖子VO、
        List<Map<String, Object>> discussPostVoList = new ArrayList<>();
        for(DiscussPost discussPost:myDiscussPosts){
            Map<String, Object> map = new HashMap<>();
            map.put("post", discussPost);
            long likeCount = likeServcie.findEntityLikeCount(ENTITY_TYPE_COMMENT, discussPost.getId());
            map.put("likeCount", likeCount);
            discussPostVoList.add(map);
        }
        model.addAttribute("postVoList", discussPostVoList);

        //当前作者发布的帖子总数
        int mypostCount = discussPostService.findDiscussPostRows(userId);
        model.addAttribute("mypostCount", mypostCount);

        return "/site/my-post";
    }


}
