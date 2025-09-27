package com.itheima.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.po.Address;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.PageQuery;
import com.itheima.mp.domain.vo.AddressVO;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.itheima.mp.enums.UserStatus.NORMAL;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    //    @Override
//    public void deductBalance(Long id, Integer money) {
//        // 1.查询用户
//        User user = getById(id);
//        // 2.判断用户状态
//        if (user == null || user.getStatus() == 2) {
//            throw new RuntimeException("用户状态异常");
//        }
//        // 3.判断用户余额
//        if (user.getBalance() < money) {
//            throw new RuntimeException("用户余额不足");
//        }
//        // 4.扣减余额
//        baseMapper.deductMoneyById(id, money);
//    }
    @Override
    @Transactional
    public void deductBalance(Long id, Integer money) {
        // 1.查询用户
        User user = getById(id);
        // 2.校验用户状态
        if (user == null || user.getStatus() == NORMAL) {
            throw new RuntimeException("用户状态异常！");
        }
        // 3.校验余额是否充足
        if (user.getBalance() < money) {
            throw new RuntimeException("用户余额不足！");
        }
        // 4.扣减余额 update tb_user set balance = balance - ?
        int remainBalance = user.getBalance() - money;
        lambdaUpdate().set(User::getBalance, remainBalance) // 更新余额
                .set(remainBalance == 0, User::getStatus, 2) // 动态判断，是否更新status
                .eq(User::getId, id).eq(User::getBalance, user.getBalance()) // 乐观锁
                .update();
    }

    @Override
    public UserVO queryUserAndAddressById(Long userId) {
        // 1.查询用户
        User user = getById(userId);
        if (user == null) {
            return null;
        }
        // 2.查询收货地址
        List<Address> addresses = Db.lambdaQuery(Address.class)
                .eq(Address::getUserId, userId)
                .list();
        // 3.处理vo
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        userVO.setAddresses(BeanUtil.copyToList(addresses, AddressVO.class));
        return userVO;
    }

    @Override
    public PageDTO<UserVO> queryUsersPage(PageQuery query) {
        // 1.构建条件
        // 1.1.分页条件
        // 确保 pageNo 和 pageSize 不为 null
        int pageNo = query.getPageNo() != null ? query.getPageNo() : 1;
        int pageSize = query.getPageSize() != null ? query.getPageSize() : 10;
        Page<User> page = Page.of(pageNo, pageSize);

        // 1.2.排序条件
        if (query.getSortBy() != null && !query.getSortBy().isEmpty()) {
            page.addOrder(new OrderItem(query.getSortBy(), query.getIsAsc()));
        } else {
            // 默认按照更新时间排序
            page.addOrder(new OrderItem("update_time", false));
        }

        // 2.查询
        page(page);

        // 3.数据非空校验
        List<User> records = page.getRecords();
        if (records == null) {
            records = new ArrayList<>(); // 防止 getRecords 返回 null
        }
        // 4.有数据，转换
        List<UserVO> list = BeanUtil.copyToList(records, UserVO.class);
        // 5.封装返回
        PageDTO<UserVO> pageDTO=new PageDTO<UserVO>();
        pageDTO.setPages(page.getPages());
        pageDTO.setList(list);
        pageDTO.setTotal(page.getTotal());
        return pageDTO;
    }
}
