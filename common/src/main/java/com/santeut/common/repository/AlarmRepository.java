package com.santeut.common.repository;

import com.santeut.common.dto.response.AlarmListResponseDto;
import com.santeut.common.entity.AlarmEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlarmRepository extends JpaRepository<AlarmEntity, Integer> {
    Optional<List<AlarmEntity>>findAllByIdAndIsDeletedFalseOrderByCreatedAtDesc(int id);
}
