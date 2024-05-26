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

  public static double getDistance(double lat1, double lon1, double lat2, double lon2, String unit) {

    double theta = lon1 - lon2;
    double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

    dist = Math.acos(dist);
    dist = rad2deg(dist);
    dist = dist * 60 * 1.1515;

    if (unit == "kilometer") {
      dist = dist * 1.609344;
    } else if(unit == "meter"){
      dist = dist * 1609.344;
    }

    return (dist);
  }

  // This function converts decimal degrees to radians
  private static double deg2rad(double deg) {
    return (deg * Math.PI / 180.0);
  }

  // This function converts radians to decimal degrees
  private static double rad2deg(double rad) {
    return (rad * 180 / Math.PI);
  }
}