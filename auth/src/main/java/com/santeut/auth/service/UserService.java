package com.santeut.auth.service;


import com.santeut.auth.dto.request.UpdatePasswordRequest;
import com.santeut.auth.dto.request.UpdateProfileRequest;
import com.santeut.auth.dto.response.GetUserInfoResponse;
import com.santeut.auth.dto.response.GetUserLevelResponse;
import com.santeut.auth.entity.UserEntity;
import org.hibernate.sql.Update;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    GetUserInfoResponse userLoginInfo(String userLoginId);

    GetUserInfoResponse userInfo(int userId);

    void updatePassword(String userLoginId, UpdatePasswordRequest request);

    void updateProfile(String userLoginId, UpdateProfileRequest request);

    GetUserLevelResponse getLevel(String userLoginId);
}
