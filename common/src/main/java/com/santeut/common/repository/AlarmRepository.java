package com.santeut.common.repository;

import com.santeut.common.entity.AlarmEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AlarmRepository extends JpaRepository<AlarmEntity, Integer> {

    // 삭제 된거 제외한 알람 리스트 받아오기
    @Modifying
    @Transactional
    @Query("SELECT a FROM AlarmEntity  a WHERE a.userId=:id and a.isDeleted=FALSE ORDER BY a.createdAt DESC")
    Optional<List<AlarmEntity>>findAlarmDirectly(int id);

    @Modifying
    @Transactional
    @Query("UPDATE AlarmEntity a SET a.isDeleted=true, a.deletedAt=:deletedAt WHERE a.id=:id")
    void deleteAlarmDirectly(int id, LocalDateTime deletedAt);
}
