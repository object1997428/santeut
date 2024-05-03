package com.santeut.auth.common.userDetail;

import com.santeut.auth.common.exception.DataNotFoundException;
import com.santeut.auth.entity.UserEntity;
import com.santeut.auth.repository.UserRepository;
import com.santeut.auth.common.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
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
                .orElseThrow(() ->  new DataNotFoundException(ResponseCode.NOT_EXISTS_USER));

        return User.builder()
                .username(userEntity.getUserLoginId())
                .password(userEntity.getUserPassword())
                .build();
    }
}
