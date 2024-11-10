package com.birdbi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoDto {
    public int user_id;
    public String name;
    public String phone_number;
    public String email;
    public String car_number;
    public String profile_picture;
}
