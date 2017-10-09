package com.coder520.user.service;

import com.coder520.user.entity.User;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zrkj on 2017/7/29.
 */
public interface UserService {
    User findUserByUserName(String username);

    void createUser(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException;

}
