package com.santeut.auth.domain.service;


import com.santeut.auth.domain.entity.UserEntity;

public interface UserService {

    void userLoginInfo();

    UserEntity userInfo(int userId);
}
