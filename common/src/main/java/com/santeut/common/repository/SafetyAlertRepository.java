package com.santeut.common.repository;

import com.santeut.common.entity.SafetyAlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SafetyAlertRepository extends JpaRepository<SafetyAlertEntity,Integer> {
}
