package com.santeut.auth.domain.repository;

import com.santeut.auth.domain.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    Optional<RefreshToken> findByUserLoginId(String userLoginId);

    boolean existsByUserLoginId(String userLoginId);

    void deleteByUserLoginId(String userLoginId);

}
