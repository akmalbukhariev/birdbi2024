package com.birdbi.api.user.service;

import com.birdbi.api.CamelCaseMap;
import com.birdbi.api.dto.UserInfoDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    public CamelCaseMap selectUser(String phone_number) throws Exception;

    int insertUser(UserInfoDto dto) throws Exception;
}
