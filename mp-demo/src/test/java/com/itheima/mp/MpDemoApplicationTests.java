package com.itheima.mp;

import com.itheima.mp.controller.UserController;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.UserVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class MpDemoApplicationTests {

    @Test
    void contextLoads() {

    }

    @Autowired
   UserController userController;
    @Test
    void queryUsers(){
        UserQuery userQuery=new UserQuery();
        userQuery.setName("");
        userQuery.setStatus(1);
        userQuery.setMaxBalance(20000);
        userQuery.setMinBalance(1);
        List<UserVO> ans=userController.queryUsers(userQuery);
//        for(UserVO user : ans){
//            System.out.println(user.getUsername());
//        }
        ans.forEach(System.out::println);
    }
}
