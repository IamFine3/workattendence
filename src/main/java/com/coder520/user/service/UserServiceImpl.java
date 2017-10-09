package com.coder520.user.service;

import com.coder520.common.utils.SecurityUtils;
import com.coder520.user.dao.UserMapper;
import com.coder520.user.entity.User;
import jdk.internal.instrumentation.TypeMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zrkj on 2017/7/29.
 */
@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /***
     *
     * @param username
     * @return
     */
    @Override
    public User findUserByUserName(String username) {
        User user = userMapper.selectByUserName(username);
        return user;
    }


    @Override
    public void createUser(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {

            user.setPassword(SecurityUtils.encrptyPassword(user.getPassword()));
            userMapper.insertSelective(user);

    }
}
