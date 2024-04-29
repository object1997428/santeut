package com.santeut.auth.service;


import com.santeut.auth.entity.UserEntity;

public interface UserService {

    void userLoginInfo();

    UserEntity userInfo(int userId);
}
