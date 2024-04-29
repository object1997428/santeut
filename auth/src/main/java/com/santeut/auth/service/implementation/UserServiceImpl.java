package com.santeut.auth.service.implementation;

import com.santeut.auth.common.exception.CustomException;
import com.santeut.auth.common.response.ResponseCode;
import com.santeut.auth.entity.UserEntity;
import com.santeut.auth.repository.UserRepository;
import com.santeut.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void userLoginInfo() {

    }

    @Override
    public UserEntity userInfo(int userId) {

        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_EXISTS_USER));

        return userEntity.getUserInfo(userEntity);
    }
}
