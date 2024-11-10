package com.birdbi.api.constant;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Constant {
    public static final String api_version = "1.0.0";
    public static final String web_version = "1.0.0";
    public static final String HEADER_AUTH = "Authorization";
    public static final String HEADER_BEARER = "Bearer";
    public static final String HEADER_ACCESS_TOKEN = "access-token";
    public static final String HEADER_REFRESH_TOKEN = "refresh-token";
    public static final String HEADER_ROLE = "auth-role";
    public static final String HEADER_USER_NAME = "user-name";
    public static final Path UPLOAD_DIRECTORY = Paths.get("images/");

    public Constant() {
    }
}
