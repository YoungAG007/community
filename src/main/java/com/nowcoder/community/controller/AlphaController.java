package com.nowcoder.community.controller;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/alpha")
public class AlphaController {
    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello(String name){
        return "Hello Spring boot 1122!!!"+name;
    }

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response){
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        System.out.println(request.getParameter("code"));

        //
        response.setContentType("text/html;charset=utf-8");
        try {
            PrintWriter writer = response.getWriter();
            writer.write("<h1>nowcoder<h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/students")
    @ResponseBody
    public String students(@RequestParam(name="current",required = false,defaultValue = "1") int current,
                          @RequestParam(name="limit",required = false,defaultValue = "10") int limit){
        System.out.println(current);
        System.out.println(limit);
        return "return students";
    }

    //Resful风格
    @RequestMapping(path = "/student/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String student(@PathVariable("id") int id){
        return "id="+id+"student";
    }

    @RequestMapping(value = "/student",method = RequestMethod.POST)
    @ResponseBody
    public String student_post(String name,int age){
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    @RequestMapping(value = "/teacher",method = RequestMethod.GET)
    public ModelAndView getTeacher(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name", "张三");
        modelAndView.addObject("age", 20);
        modelAndView.setViewName("/demo/view");
        return modelAndView;
    }

    //响应JSON数据（异步请求）
    //java对象 -> json ->js对象
    @RequestMapping(path = "/emp",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getEmp(){
        Map<String,Object> map = new HashMap<>();
        map.put("name", "张三");
        map.put("age", 20);
        map.put("salary", 1000);
        return map;
    }

    @RequestMapping(path = "/emps",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> getEmps(){
        List<Map<String,Object>> empList = new ArrayList<>();
        Map<String,Object> map1 = new HashMap<>();

        map1.put("name", "张三");
        map1.put("age", 20);
        map1.put("salary", 1000);
        Map<String,Object> map2 = new HashMap<>();

        map2.put("name", "李四");
        map2.put("age", 21);
        map2.put("salary", 2000);

        empList.add(map1);
        empList.add(map2);
        return empList;
    }
}
