package com.itheima.mp;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.controller.UserController;
import com.itheima.mp.domain.po.Address;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.service.IAddressService;
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
    @Test
    void testDbGet() {
        User user = Db.getById(1L, User.class);
        System.out.println(user);
    }

    @Test
    void testDbList() {
        // 利用Db实现复杂条件查询
        List<User> list = Db.lambdaQuery(User.class)
                .like(User::getUsername, "o")
                .ge(User::getBalance, 1000)
                .list();
        list.forEach(System.out::println);
    }

    @Test
    void testDbUpdate() {
        Db.lambdaUpdate(User.class)
                .set(User::getBalance, 2000)
                .eq(User::getUsername, "Rose");
    }


    @Test
    void queryUserAndAddressById(){
       UserVO userVO= userController.queryUserById(2L);
        System.out.println(userVO.getUsername());
    }
    @Autowired
    IAddressService addressService;

    @Test
    void testDeleteByLogic() {
        // 删除方法与以前没有区别
        addressService.removeById(59L);
    }
    @Test
    void testQuery() {
        List<Address> list = addressService.list();
        list.forEach(System.out::println);
    }
}
