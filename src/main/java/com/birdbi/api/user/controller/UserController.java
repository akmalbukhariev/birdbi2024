package com.birdbi.api.user.controller;

import com.birdbi.api.BaseController;
import com.birdbi.api.VersionResponseResult;
import com.birdbi.api.constant.Result;
import com.birdbi.api.dto.UserInfoDto;
import com.birdbi.api.user.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@Tag(name = "User Common", description = "User API")
@RequestMapping(value={"/birdbi/api/v1/user"})
public class UserController extends BaseController {

    private final IUserService userService;

    @Operation(tags = {"User"}, summary = "1. register", description = "register", hidden = false, responses = {
            @ApiResponse(responseCode = "200", description = "success")
    })
    @PostMapping(value= "/register", headers = { "Content-type=application/json" })
    public ResponseEntity<Object> register(@RequestBody UserInfoDto userInfoDto) {
        VersionResponseResult result = null;

        try {
            int resultNum = this.userService.register(userInfoDto);
            if (resultNum == Result.USER_EXIST.getCode()) {
                result = this.setResult(Result.USER_EXIST);
            } else if (resultNum != 0) {
                result = this.setResult(Result.SUCCESS);
            } else {
                result = this.setResult(Result.SERVER_ERROR);
            }
        } catch (Exception var4) {
            result = this.setResult(Result.SERVER_ERROR);
        }

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @Operation(tags = {"User"}, summary = "2. get user", description = "get user info", hidden = false,
            responses = {@ApiResponse(responseCode = "200", description = "success")
            })
    @GetMapping(value= "/getUser/{phone_number}", headers = { "Content-type=application/json" })
    public ResponseEntity<Object> getUser(@PathVariable String phone_number) {
        VersionResponseResult result = null;

        try {
            UserInfoDto userInfo = this.userService.getUser(phone_number);
            if (userInfo == null) {
                result = this.setResult(Result.USER_NOT_EXIST);
            } else {
                result = this.setResult(Result.USER_EXIST, userInfo);
            }
        } catch (Exception var4) {
            result = this.setResult(Result.SERVER_ERROR);
        }

        return new ResponseEntity(result, HttpStatus.OK);
    }
}
