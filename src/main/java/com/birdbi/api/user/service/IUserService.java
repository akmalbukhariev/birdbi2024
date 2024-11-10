package com.birdbi.api.user.service;

import com.birdbi.api.dto.UserInfoDto;

public interface IUserService {
    UserInfoDto getUser(String phone) throws Exception;

    int register(UserInfoDto dto) throws Exception;
}
