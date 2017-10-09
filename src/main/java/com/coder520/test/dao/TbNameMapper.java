package com.coder520.test.dao;

import com.coder520.test.entity.TbName;

public interface TbNameMapper {
    int deleteByPrimaryKey(String id);

    int insert(TbName record);

    int insertSelective(TbName record);

    TbName selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TbName record);

    int updateByPrimaryKey(TbName record);
}