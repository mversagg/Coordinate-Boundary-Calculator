package processors;

import data.Coordinate;

import java.util.List;

/**
 * Created by mversaggi on 5/13/2017.
 */
public interface BoundaryCalculator
{
    List<Coordinate> calculateBoundaryCoordinates();
}
