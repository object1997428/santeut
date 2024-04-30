package com.santeut.mountain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "mountain")
public class MountainEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "mountain_id", nullable = false)
  private int mountainId;

  @Column(name = "mountain_address", columnDefinition = "varchar(600)", nullable = false)
  private String address;

  @Column(name="mountain_region", columnDefinition = "char(6)")
  private String regionName;

  @Column(name = "mountain_name", columnDefinition = "varchar(150)")
  private String mountainName;

  @Column(name = "mountain_description", columnDefinition = "varchar(3000)")
  private String description;

  @Column(name = "mountain_height", columnDefinition = "decimal(5)", nullable = false)
  private int height;

  @Column(name = "mountain_image", columnDefinition = "varchar(255)")
  private String image;

  @Column(name = "mountain_top", columnDefinition = "point")
  private Point mountainTop;

  @Column(name = "mountain_top100")
  private boolean isTop100;

  @Column(name = "mountain_code", columnDefinition = "varchar(9)", nullable = false)
  private String mountainCode;

  @Column(name = "mountain_views", nullable = false)
  private int views;

  @OneToMany(mappedBy = "mountainId")
  List<CourseEntity> courseEntityList = new ArrayList<>();

}
