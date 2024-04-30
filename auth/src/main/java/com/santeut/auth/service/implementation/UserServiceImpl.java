package com.santeut.auth.service.implementation;

import com.santeut.auth.common.exception.DataNotFoundException;
import com.santeut.auth.common.response.ResponseCode;
import com.santeut.auth.dto.request.UpdatePasswordRequest;
import com.santeut.auth.dto.request.UpdateProfileRequest;
import com.santeut.auth.dto.response.GetUserInfoResponse;
import com.santeut.auth.dto.response.GetUserLevelResponse;
import com.santeut.auth.entity.UserEntity;
import com.santeut.auth.entity.UserTierEntity;
import com.santeut.auth.repository.UserRepository;
import com.santeut.auth.repository.UserTierRepository;
import com.santeut.auth.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserTierRepository userTierRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public GetUserInfoResponse userLoginInfo(String userLoginId) {

        UserEntity userEntity = userRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_USER));

        return new GetUserInfoResponse(userEntity);
    }

    @Override
    public GetUserInfoResponse userInfo(int userId) {

        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_USER));


        return new GetUserInfoResponse(userEntity);
    }

    @Override
    public void updatePassword(String userLoginId, UpdatePasswordRequest request) {

        UserEntity userEntity = userRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_USER));

        if(!bCryptPasswordEncoder.matches(request.getCurrentPassword(), userEntity.getUserPassword())){
            throw new DataNotFoundException(ResponseCode.NOT_MATCH_USER_PASSWORD);
        }

        String updatePassword = bCryptPasswordEncoder.encode(request.getUpdatePassword());
        userEntity.setUserPassword(updatePassword);
        userRepository.save(userEntity);
    }

    @Override
    public void updateProfile(String userLoginId, UpdateProfileRequest request) {

        UserEntity userEntity = userRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_USER));

        userEntity.setUserNickname(request.getUserNickname());
        userEntity.setUserProfile(request.getUserProfile());
        userEntity.setModifiedAt(LocalDateTime.now());

        userRepository.save(userEntity);

    }

    @Override
    public GetUserLevelResponse getLevel(String userLoginId) {

        UserEntity userEntity = userRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_USER));

        UserTierEntity userTierEntity = userTierRepository.findByUserId(userEntity.getUserId())
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_USER_LEVEL));

        return new GetUserLevelResponse(userTierEntity.getUserTierId(),
                userTierEntity.getUserTierName(), userTierEntity.getUserTierPoint());
    }
}
