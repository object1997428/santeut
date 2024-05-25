package com.santeut.common.common.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class GeoUtils {
    private static final GeometryFactory geometryFactory = new GeometryFactory();

    public static Point createPoint(double latitude, double longitude) {
        // 좌표 객체 생성
        Coordinate coordinate = new Coordinate(longitude, latitude);
        // Point 객체 생성
        return geometryFactory.createPoint(coordinate);
    }
}
