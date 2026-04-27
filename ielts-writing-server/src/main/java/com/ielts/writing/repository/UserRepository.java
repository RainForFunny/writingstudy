package com.ielts.writing.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ielts.writing.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserRepository extends BaseMapper<User> {
    default Optional<User> findByUsername(String username) {
        return Optional.ofNullable(selectOne(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)));
    }
}