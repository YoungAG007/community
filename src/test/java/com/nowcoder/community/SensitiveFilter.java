package com.nowcoder.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveFilter {
    @Autowired
    private com.nowcoder.community.util.SensitiveFilter sensitiveFilter;
    @Test
    public void testSensitiveFilter(){
        String text = "这里♥可以赌博，可以♥❣♂嫖娼，可♥❣♂以吸毒，可♥❣♂以开票，哈哈哈";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
    }
}
