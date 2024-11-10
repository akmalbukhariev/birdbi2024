package com.birdbi.api.user.service.impl;

import com.birdbi.api.CamelCaseMap;
import com.birdbi.api.constant.Result;
import com.birdbi.api.dto.UserInfoDto;
import com.birdbi.api.user.service.IUserService;
import com.birdbi.api.user.service.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserMapper userMapper;

    @Override
    public UserInfoDto getUser(String phone) throws Exception {
        try {
            CamelCaseMap map = userMapper.selectUser(phone);
            if (map != null) {
                return new UserInfoDto(
                        (Integer) map.get("user_id"),
                        map.get("name").toString(),
                        map.get("phone_number").toString(),
                        map.get("email").toString(),
                        map.get("car_number").toString(),
                        map.get("profile_picture").toString()
                );
            }
            return null;  // Return null if no user found
        } catch (Exception e) {
            //log.error("Error fetching user information", e);
            throw e;  // Re-throw for proper error handling
        }
    }

    @Override
    public int register(UserInfoDto dto) throws Exception {
        CamelCaseMap map = userMapper.selectUser(dto.getPhone_number());
        if (map != null && !map.isEmpty()) {
            return Result.USER_EXIST.getCode();
        }
        return userMapper.insertUser(dto);
    }
}
