package com.santeut.auth.service.implementation;

import com.santeut.auth.common.exception.DataNotFoundException;
import com.santeut.auth.dto.request.SignInRequestDto;
import com.santeut.auth.dto.request.SignUpRequestDto;
import com.santeut.auth.dto.response.JwtTokenResponseDto;
import com.santeut.auth.entity.RefreshToken;
import com.santeut.auth.entity.UserEntity;
import com.santeut.auth.entity.UserTierEntity;
import com.santeut.auth.repository.RefreshTokenRepository;
import com.santeut.auth.repository.UserRepository;
import com.santeut.auth.repository.UserTierRepository;
import com.santeut.auth.service.AuthService;
import com.santeut.auth.common.jwt.JwtTokenProvider;
import com.santeut.auth.common.response.ResponseCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserTierRepository userTierRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public void signUp(SignUpRequestDto dto) {

        String password = dto.getUserPassword();
        String encodedPassword = bCryptPasswordEncoder.encode(password);

        boolean usedUserNickname = userRepository.existsByUserNickname(dto.getUserNickname());
        if (usedUserNickname) throw new DataNotFoundException(ResponseCode.EXISTS_USER_NICKNAME);

        boolean usedUserLoginId = userRepository.existsByUserLoginId(dto.getUserLoginId());
        if(usedUserLoginId) throw new DataNotFoundException(ResponseCode.EXISTS_USER_LOGIN_ID);

        UserEntity userEntity = UserEntity.signUp(dto);
        userEntity.setUserPassword(encodedPassword);

        userRepository.save(userEntity);

        UserEntity user = userRepository.findByUserLoginId(userEntity.getUserLoginId())
                        .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_USER));

        UserTierEntity userTierEntity = new UserTierEntity();
        userTierEntity.setUserTierName("언덕");
        userTierEntity.setUserTierPoint(0);
        userTierEntity.setUserId(user.getUserId());
        userTierRepository.save(userTierEntity);
    }

    @Override
    public JwtTokenResponseDto signIn(SignInRequestDto dto) {

        String userLoginId = dto.getUserLoginId();
        String userPassword = dto.getUserPassword();

        UserEntity userEntity = userRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_USER));

        if(!userLoginId.equals(userEntity.getUserLoginId())){
            throw new DataNotFoundException(ResponseCode.NOT_MATCH_USER_LOGIN_ID);
        }
        if (!bCryptPasswordEncoder.matches(userPassword, userEntity.getUserPassword())){
            throw new DataNotFoundException(ResponseCode.NOT_MATCH_USER_PASSWORD);
        }

        JwtTokenResponseDto jwtTokenResponse = jwtTokenProvider.issueToken(
                userLoginId);


        if(refreshTokenRepository.existsByUserLoginId(userLoginId)){
            refreshTokenRepository.deleteByUserLoginId(userLoginId);
        }

        refreshTokenRepository.save(new RefreshToken(userLoginId, jwtTokenResponse.getRefreshToken()));

        return jwtTokenResponse;
    }

    @Override
    public JwtTokenResponseDto reissueToken(HttpServletRequest request) {

        JwtTokenResponseDto jwtTokenResponseDto = jwtTokenProvider.reissueToken(request);

        String userLoginId = jwtTokenProvider.extractUserLoginId(jwtTokenResponseDto.getRefreshToken());

        if(refreshTokenRepository.existsByUserLoginId(userLoginId)){
            refreshTokenRepository.deleteByUserLoginId(userLoginId);
        }

        refreshTokenRepository.save(new RefreshToken(userLoginId,jwtTokenResponseDto.getRefreshToken()));

        return jwtTokenResponseDto;
    }
}
