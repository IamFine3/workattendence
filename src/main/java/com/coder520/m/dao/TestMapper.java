package com.coder520.m.dao;

import com.coder520.m.entity.Test;

public interface TestMapper {
    int insert(Test record);

    int insertSelective(Test record);
}