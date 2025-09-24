package com.itheima.mp;

import com.itheima.mp.controller.UserController;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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

    @Autowired
    IUserService userService;
    @Test
    void testSaveOneByOne() {
        long b = System.currentTimeMillis();
        for (int i = 1; i <= 1000; i++) {
            userService.save(buildUser(i));
        }
        long e = System.currentTimeMillis();
        System.out.println("耗时：" + (e - b));
    }

    private User buildUser(int i) {
        User user = new User();
        user.setUsername("user_" + i);
        user.setPassword("123");
        user.setPhone("" + (18688190000L + i));
        user.setBalance(2000);
        user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(user.getCreateTime());
        return user;
    }

}
