//package com.santeut.hiking.entity;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.EntityListeners;
//import jakarta.persistence.MappedSuperclass;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.LastModifiedDate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//import java.time.LocalDateTime;
//
//@Getter
//@NoArgsConstructor
//@MappedSuperclass   //JPA Entity 클래스들이 해당 추상 클래스를 상속할 경우 createDate, modifiedDate를 컬럼으로 인식
//@EntityListeners(AuditingEntityListener.class)  //해당 클래스에 Auditing 기능을 포함
//public abstract class BaseEntity {
//
//    @CreatedDate
//    @Column(updatable = false, name = "created_at", nullable = false)
//    private LocalDateTime createdAt;
//
//    @LastModifiedDate
//    @Column(name = "modified_at", nullable = false)
//    private LocalDateTime modifiedAt;
//
//    @Column(name = "deleted_at")
//    private LocalDateTime deletedAt;
//
//    @Column(name = "is_deleted", nullable = false)
//    private boolean isDeleted;
//
//    /**
//     * 비즈니스 로직
//     **/
//    //소프트딜리트(삭제)용
//    public void setDeleted(boolean deleted) {
//        isDeleted = deleted;
//        if (isDeleted) {
//            deletedAt = LocalDateTime.now();
//        } else {
//            deletedAt = null;
//        }
//    }
//}
