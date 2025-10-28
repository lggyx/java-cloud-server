package com.hmall.item.service.impl;

import com.hmall.domain.dto.OrderDetailDTO;
import com.hmall.service.IItemService;
import com.hmall.utils.JwtTool;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.util.List;

@SpringBootTest
class ItemServiceImplTest {

    @Autowired
    protected IItemService itemService;

    @Autowired
    private JwtTool jwtTool;

    @Test
    void testJwt() {
        String token = jwtTool.createToken(1L, Duration.ofMinutes(30));
        System.out.println("token = " + token);
    }

    @Test
    void deductStock() {
        List<OrderDetailDTO> items = List.of(
                new OrderDetailDTO().setItemId(317578L).setNum(1),
                new OrderDetailDTO().setItemId(317580L).setNum(1)
        );
        itemService.deductStock(items);
    }

    @Test
    void passwdBCrypt() {
        // 1. 生成一个正确的 BCrypt 密文
        String raw = "123456";
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String correct = encoder.encode(raw);
        System.out.println(correct);   // $2a$10$....

        // 2. 把它手动贴到数据库，再试登录，WARN 应该消失
        boolean ok = encoder.matches(raw, correct);
        System.out.println(ok);        // true
    }
}
