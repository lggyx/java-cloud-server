package com.itheima.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.UserVO;

public interface IUserService extends IService<User> {
    void deductBalance(Long id, Integer money);

    UserVO queryUserAndAddressById(Long userId);
    // 拓展自定义方法
}
