package com.example.myappjwt.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.myappjwt.model.User;
import com.example.myappjwt.model.UserExample;

public interface UserMapper {
    
    long countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(User row);

    int insertSelective(User row);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Long id);
    User selectByUsername(@Param("username") String username);
    User selectByUserpassword(@Param("userpassword") String password);
    
    int updateByExampleSelective(@Param("row") User row, @Param("example") UserExample example);

    int updateByExample(@Param("row") User row, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User row);

    int updateByPrimaryKey(User row);
}