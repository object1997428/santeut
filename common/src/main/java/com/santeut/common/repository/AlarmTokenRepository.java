package com.santeut.common.repository;

import com.santeut.common.entity.AlarmTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlarmTokenRepository extends JpaRepository<AlarmTokenEntity,Integer> {
    List<AlarmTokenEntity> findByIdInAndActivated(List<Integer> userIds,boolean isActivated);

    Optional<AlarmTokenEntity> findByFcmToken(String fcmToken);

    void deleteById(int userId);

    void deleteByFcmToken(String fcmToken);

    @Override
    void deleteById(Integer integer);
}
