package com.santeut.mountain.repository;


import com.santeut.mountain.entity.CourseEntity;
import com.santeut.mountain.entity.MountainEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<CourseEntity, Integer> {

  Page<CourseEntity> findAllByMountainId(MountainEntity mountain, Pageable pageable);

}
