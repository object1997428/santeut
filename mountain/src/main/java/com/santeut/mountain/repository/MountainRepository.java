package com.santeut.mountain.repository;

import com.santeut.mountain.entity.MountainEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MountainRepository extends JpaRepository<MountainEntity, Integer> {

  List<MountainEntity> findAllByMountainNameContainingOrderByViewsDesc(String mountainName);

  List<MountainEntity> findTop10ByOrderByViewsDesc();

}
