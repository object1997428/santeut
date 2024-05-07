package com.santeut.common.repository;

import com.santeut.common.entity.ImageEntity;
import com.santeut.common.entity.LikeEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {
    Optional<List<ImageEntity>> findAllByImageReferenceIdAndImageReferenceType(Integer imageReferenceId, Character imageReferenceType);
}
