package com.santeut.mountain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.awt.geom.Line2D;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.geolatte.geom.Geometry;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "course")
public class CourseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int courseId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "mountain_id")
  private MountainEntity mountainId;

  @Column(name = "course_name", columnDefinition = "varchar(150)")
  private String courseName;

  @Column(name = "course_level", columnDefinition = "varchar(9)")
  private String courseLevel;

  @Column(name = "course_distance", columnDefinition = "float")
  private float distance;

  @Column(name = "course_uptime", columnDefinition = "int")
  private int upTime;

  @Column(name = "course_downtime", columnDefinition = "int")
  private int downTime;

  @Column(name = "course_points", columnDefinition = "geometry")
  private Geometry coursePoints;

}