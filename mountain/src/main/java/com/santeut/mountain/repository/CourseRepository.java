package com.santeut.mountain.repository;


import com.santeut.mountain.entity.CourseEntity;
import com.santeut.mountain.entity.MountainEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<CourseEntity, Integer> {

  List<CourseEntity> findAllByMountainId(MountainEntity mountain);

}
