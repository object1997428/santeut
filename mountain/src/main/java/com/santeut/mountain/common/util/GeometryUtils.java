package com.santeut.mountain.common.util;

import com.santeut.mountain.dto.response.LocationData;
import org.locationtech.jts.geom.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeometryUtils {
  private static final GeometryFactory geometryFactory = new GeometryFactory();

  public static LineString createLineStringFromLocations(List<LocationData> locations) {
    Coordinate[] coordinates = locations.stream()
        .map(loc -> new Coordinate(loc.lng, loc.lat))
        .toArray(Coordinate[]::new);
    return geometryFactory.createLineString(coordinates);
  }

  public static List<LocationData> convertGeometryToListLocationData(Geometry geometry) {
    List<LocationData> locationDataList = new ArrayList<>();
    if (geometry instanceof LineString lineString) {
      locationDataList.addAll(Arrays.stream(lineString.getCoordinates())
          .map(coordinate -> new LocationData(coordinate.y, coordinate.x))
          .toList());
    }else if (geometry instanceof MultiLineString multiLineString) {
      for (int i = 0; i < multiLineString.getNumGeometries(); i++) {
        LineString lineString = (LineString) multiLineString.getGeometryN(i);
        locationDataList.addAll(Arrays.stream(lineString.getCoordinates())
            .map(coordinate -> new LocationData(coordinate.y, coordinate.x))
            .toList());
      }
    }
    return locationDataList;
  }
}