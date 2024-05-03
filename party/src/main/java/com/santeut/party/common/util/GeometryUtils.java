package com.santeut.party.common.util;

import com.santeut.party.dto.request.LocationData;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GeometryUtils {
    private static final GeometryFactory geometryFactory = new GeometryFactory();

    public static LineString createLineStringFromLocations(List<LocationData> locations) {
        Coordinate[] coordinates = locations.stream()
                .map(loc -> new Coordinate(loc.lng, loc.lat))
                .toArray(Coordinate[]::new);
        return geometryFactory.createLineString(coordinates);
    }

    public static List<LocationData> convertGeometryToListLocationData(Geometry geometry) {
        if (geometry instanceof LineString) {
            LineString lineString = (LineString) geometry;
            return Arrays.stream(lineString.getCoordinates())
                    .map(coordinate -> new LocationData(coordinate.y, coordinate.x))
                    .collect(Collectors.toList());
        }
        return null;
    }
}
