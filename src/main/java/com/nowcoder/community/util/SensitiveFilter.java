package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    private static final Logger logger  = LoggerFactory.getLogger(SensitiveFilter.class);

    //替换符
    private static final String REPLACEMENT = "***";

    //初始化根节点
    private TrieNode rootNode = new TrieNode();

    @PostConstruct //初始化方法 容器初始化后 这个方法会被自动调用 服务启动的时候bean创建 这个方法是就会被调用
    public void init(){
        try(
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ){
           String keyword;
           while((keyword = reader.readLine()) != null){
               //添加前缀树
               this.addKeyword(keyword);
           }
        }catch (IOException e){
            logger.error("加载敏感词文件失败: " + e.getMessage());
        }
    }

    //将一个敏感词 添加到 前缀树中去
    private void addKeyword(String keyword){
        TrieNode tempNode = rootNode;
        for(int i = 0; i<keyword.length();i++){
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if(subNode == null){
                //初始化节点
                subNode = new TrieNode();
                tempNode.addSubNode(c,subNode);
            }
            //指向子节点 进入下一轮循环
            tempNode=subNode;
            //设置结束的标识
            if(i == keyword.length()-1){
                tempNode.setKeywordEnd(true);
            }
        }
    }

    /**
     *过滤敏感词
     * @param text 待过滤的文本
     * @return 过滤后的文本
     */
    public String filter(String text){
       if(StringUtils.isBlank(text)){
           return null;
       }
       //指针1 指向树的节点
        TrieNode tempNode = rootNode;
       //指针2
        int begin = 0;
        //指针3
        int position = 0;
        //结果是一个变长的字符串
        StringBuilder sb = new StringBuilder();
        while(begin < text.length()){
            if(position < text.length()) {
                Character c = text.charAt(position);

                // 跳过符号(不是普通字符(abc123...)且不是东亚字符)
                if (!CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF)) {
                    // 若此时处于根结点，首指针++
                    if (tempNode == rootNode) {
                        begin++;
                        sb.append(c);
                    }
                    // 无论符号在开头还是中间，尾指针++
                    position++;
                    // 进行下一次循环
                    continue;
                }

                // 检查下级节点
                tempNode = tempNode.getSubNode(c);
                if (tempNode == null) {
                    // 以begin开头的字符串不是敏感词
                    sb.append(text.charAt(begin));
                    // 进入下一个位置
                    begin++;
                    position = begin;
                    // 重新指向根节点
                    tempNode = rootNode;
                }
                // 发现敏感词，将begin~position字符串替换
                else if (tempNode.isKeywordEnd()) {
                    sb.append(REPLACEMENT);
                    // 进入下一个位置
                    position++;
                    begin = position;
                    // 重新指向根节点
                    tempNode = rootNode;
                }
                // 检查到敏感词部分，继续检查下一个字符
                else {
                    position++;
                }
            }
            // position遍历越界仍未匹配到敏感词
            else{
                sb.append(text.charAt(begin));
                // 进入下一个位置
                begin++;
                position = begin;
                // 重新指向根节点
                tempNode = rootNode;
            }
        }
        return sb.toString();
    }

    //判断是否为符号
      private boolean isSymbol(Character c){
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
      }
    //前缀树
    private class TrieNode{

        //关键词结束标志
        private boolean isKeywordEnd = false;

        //子节点（key是下级字符 value是下级节点）
        private Map<Character,TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        //添加子节点的方法
        public void addSubNode(Character c, TrieNode node){
            subNodes.put(c,node);
        }
        //获取子节点的办法
        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }
}
