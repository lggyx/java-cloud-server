package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.itheima.mp.domain.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testInsert() {
        User user = new User();
        user.setId(6L);
        user.setUsername("Lucy2");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(200);
        user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        //userMapper.saveUser(user); //MyBatis操作
        userMapper.insert(user);
    }

    @Test
    void testSelectById() {
//        User user = userMapper.queryUserById(5L);
        User user =userMapper.selectById(6L);
        System.out.println("user = " + user);
    }


    @Test
    void testQueryByIds() {
//        List<User> users = userMapper.queryUserByIds(List.of(1L, 2L, 3L, 4L));
        List<User> users=userMapper.selectBatchIds(List.of(1L,2L,3L));
        users.forEach(System.out::println);
    }

    @Test
    void testUpdateById() {
        User user = new User();
        user.setId(5L);
        user.setBalance(20000);
//        userMapper.updateUser(user);
        userMapper.updateById(user);
    }

    @Test
    void testDeleteUser() {
//        userMapper.deleteUser(5L);
        userMapper.deleteById(6L);
    }

    /**
     * TODO 无论是修改、删除、查询，都可以使用QueryWrapper来构建查询条件。
     */
    @Test
    void testQueryWrapper() {
        // 1.构建查询条件 where name like "%o%" AND balance >= 1000
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .select("id", "username", "info", "balance")
                .like("username", "o")
                .ge("balance", 1000);
        // 2.查询数据
        List<User> users = userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }
    @Test
    void testUpdateByQueryWrapper() {
        // 1.构建查询条件 where name = "Jack"
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", "Jack");
        // 2.更新数据，user中非null字段都会作为set语句
        User user = new User();
        user.setBalance(2000);
        userMapper.update(user, wrapper);
    }
    @Test
    void testUpdateWrapper() {
        List<Long> ids = List.of(1L, 2L, 4L);
        // 1.生成SQL
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
                .setSql("balance = balance - 200") // SET balance = balance - 200
                .in("id", ids); // WHERE id in (1, 2, 4)
        // 2.更新，注意第一个参数可以给null，也就是不填更新字段和数据，
        // 而是基于UpdateWrapper中的setSQL来更新
        userMapper.update(null, wrapper);
    }
    /**
     * TODO LambdaQueryWrapper
     *  无论是QueryWrapper还是UpdateWrapper在构造条件的时候都需要写死字段名称，
     *  会出现字符串魔法值。
     *  这在编程规范中显然是不推荐的。
     *  字符串魔法值就是凭空出现的字符串常量，影响代码可读性
     */
    @Test
    void testLambdaQueryWrapper() {
        // 1.构建条件 WHERE username LIKE "%o%" AND balance >= 1000
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .select(User::getId, User::getUsername, User::getInfo, User::getBalance)
                .like(User::getUsername, "o")
                .ge(User::getBalance, 1000);
        // 2.查询
        List<User> users = userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }
 /*   MyBatis-Plus为了实现某些功能（如Lambda表达式相关功能），
 使用了Java的反射机制访问了JDK内部的私有字段
 从Java 9开始，模块系统限制了对JDK内部API的非法访问
 */
}
