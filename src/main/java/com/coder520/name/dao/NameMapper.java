package com.coder520.name.dao;

import com.coder520.name.entity.Name;

public interface NameMapper {
    int insert(Name record);

    int insertSelective(Name record);
}