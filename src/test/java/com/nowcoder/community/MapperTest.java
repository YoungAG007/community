package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public void testSelectUserMapper(){
        User user = userMapper.selectById(1);
        System.out.println(user);

        User system = userMapper.selectByName("SYSTEM");
        System.out.println(system);

        User user1 = userMapper.selectByEmail("nowcoder1@sina.com");
        System.out.println(user1);

    }

    @Test
    public void testInsertUser(){
        User user = new User();
        user.setUsername("young1");
        user.setPassword("123456");
        user.setSalt("carry");
        user.setEmail("22025202@zju.edu.cn");
        user.setActivationCode(null);
        user.setHeaderUrl("http://www.nowcoder.com/101.png");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);
        System.out.println(user.getId());
    }
    @Test
    public void testUpdate(){
        int row1 = userMapper.updateStatus(150, "1");
        int row2 = userMapper.updateHeader(151, "http://www.nowcoder.com/102.png");
        int row3 = userMapper.updatePassword(151, "78910");
        System.out.println(row1);
        System.out.println(row2);
        System.out.println(row3);
    }

    @Test
    public void testSelectPosts(){
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(0, 0, 10);
        for(DiscussPost post : list){
            System.out.println(post);
        }
        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(rows);
    }

}
