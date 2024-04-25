package com.santeut.auth.common.userDetail;

import com.santeut.auth.Auth.entity.UserEntity;
import com.santeut.auth.Auth.repository.UserRepository;
import com.santeut.auth.common.exception.CustomException;
import com.santeut.auth.common.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       UserEntity userEntity = userRepository.findByUserLoginId(username)
                .orElseThrow(() ->  new CustomException(ResponseCode.NOT_EXISTS_USER));

       return new CustomUserDetails(userEntity);
    }
}
