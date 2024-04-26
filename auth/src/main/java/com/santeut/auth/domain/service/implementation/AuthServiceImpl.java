package com.santeut.auth.domain.service.implementation;

import com.santeut.auth.domain.dto.requestDto.SignInRequestDto;
import com.santeut.auth.domain.dto.requestDto.SignUpRequestDto;
import com.santeut.auth.domain.dto.responseDto.JwtTokenResponseDto;
import com.santeut.auth.domain.entity.RefreshToken;
import com.santeut.auth.domain.entity.UserEntity;
import com.santeut.auth.domain.repository.RefreshTokenRepository;
import com.santeut.auth.domain.repository.UserRepository;
import com.santeut.auth.domain.service.AuthService;
import com.santeut.auth.common.exception.CustomException;
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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public void signUp(SignUpRequestDto dto) {

        String password = dto.getUserPassword();
        String encodedPassword = bCryptPasswordEncoder.encode(password);

        boolean usedUserNickname = userRepository.existsByUserNickname(dto.getUserNickname());
        if (usedUserNickname) throw new CustomException(ResponseCode.EXISTS_USER_NICKNAME);

        boolean usedUserLoginId = userRepository.existsByUserLoginId(dto.getUserLoginId());
        if(usedUserLoginId) throw new CustomException(ResponseCode.EXISTS_USER_LOGIN_ID);

        UserEntity userEntity = UserEntity.signUp(dto);
        userEntity.setUserPassword(encodedPassword);

        userRepository.save(userEntity);
    }

    @Override
    public JwtTokenResponseDto signIn(SignInRequestDto dto) {

        String userLoginId = dto.getUserLoginId();
        String userPassword = dto.getUserPassword();

        log.debug("1");
        UserEntity userEntity = userRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_EXISTS_USER));

        if(!userLoginId.equals(userEntity.getUserLoginId())){
            throw new CustomException(ResponseCode.NOT_MATCH_USER_LOGIN_ID);
        }
        if (!bCryptPasswordEncoder.matches(userPassword, userEntity.getUserPassword())){
            throw new CustomException(ResponseCode.NOT_MATCH_USER_PASSWORD);
        }

        JwtTokenResponseDto jwtTokenResponse = jwtTokenProvider.issueToken(
                userLoginId);

        RefreshToken refreshToken = refreshTokenRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new CustomException(ResponseCode.INVALID_REFRESH_TOKEN));
        log.debug("userLoginId :" + refreshToken.getUserLoginId());
        log.debug("refreshToken :" + refreshToken.getRefreshToken());

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
