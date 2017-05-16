package processors;

import data.Coordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mversaggi on 5/13/2017.
 */
public class DecimalDegreeBoundaryCalculator implements BoundaryCalculator
{

    private final Coordinate centerPoint;
    private final int numPoints;
    private final int distanceToEdgeInMeters;

    private final int metersToOneLatitudinalDegree = 111111;

    public DecimalDegreeBoundaryCalculator(Coordinate centerPoint, int numPoints, int distanceToEdgeInMeters)
    {
        this.centerPoint = centerPoint;
        this.numPoints = numPoints;
        this.distanceToEdgeInMeters = distanceToEdgeInMeters;
    }

    public List<Coordinate> calculateBoundaryCoordinates()
    {
        double latitudeIncrease;
        double longitudeIncrease;
        double angleIncrementAmount = 360.0 / (double) numPoints;
        List<Coordinate> boundaryCoordinates = new ArrayList<>();

        for (double currentAngle = 0; currentAngle < 360; currentAngle += angleIncrementAmount)
        {
            System.out.println("-----------------------------------");
            latitudeIncrease = getLatitudeIncreaseInDecimalDegrees(currentAngle);
            longitudeIncrease = getLongitudeIncreaseInDecimalDegrees(currentAngle, latitudeIncrease + centerPoint.getLatitude());
            System.out.println("\nCalculated coordinate: " + (centerPoint.getLatitude() + latitudeIncrease)
                               + ", " + (centerPoint.getLongitude() + longitudeIncrease));
            boundaryCoordinates.add(new Coordinate(centerPoint.getLatitude() + latitudeIncrease,
                                                   centerPoint.getLongitude() + longitudeIncrease));
        }

        return boundaryCoordinates;
    }

    private double getLatitudeIncreaseInDecimalDegrees(double currentAngle)
    {
        double latitudeIncrease;
        double differenceOverNinetyDegrees = currentAngle % 90.0;
        double differenceInRadians = Math.toRadians(differenceOverNinetyDegrees);

        if (currentAngle >=0 && currentAngle < 90)
        {
            latitudeIncrease = latitudeMetersToDecimalDegrees(Math.cos(differenceInRadians) * distanceToEdgeInMeters);
        }
        else if (currentAngle >= 90 && currentAngle < 180)
        {
            latitudeIncrease = -latitudeMetersToDecimalDegrees(Math.sin(differenceInRadians) * distanceToEdgeInMeters);
        }
        else if (currentAngle >= 180 && currentAngle < 270)
        {
            latitudeIncrease = -latitudeMetersToDecimalDegrees(Math.cos(differenceInRadians) * distanceToEdgeInMeters);
        }
        else
        {
            latitudeIncrease = latitudeMetersToDecimalDegrees(Math.sin(differenceInRadians) * distanceToEdgeInMeters);
        }

        System.out.println("Calculated LATITUDE increase of " + latitudeIncrease + " with current angle "
                           + currentAngle + " , differenceOverNinetyDegrees " + differenceOverNinetyDegrees +
                           ", differenceInRadians " + differenceInRadians + ", and distanceToEdgeInMeters " + distanceToEdgeInMeters);

        return latitudeIncrease;
    }

   private double getLongitudeIncreaseInDecimalDegrees(double currentAngle, double latitude)
    {
        double longitudeIncrease;
        double differenceOverNinetyDegrees = currentAngle % 90.0;
        double differenceInRadians = Math.toRadians(differenceOverNinetyDegrees);

        if (currentAngle >=0 && currentAngle < 90)
        {
            longitudeIncrease = longitudeMetersToDecimalDegrees(Math.sin(differenceInRadians) * distanceToEdgeInMeters, latitude);
        }
        else if (currentAngle >= 90 && currentAngle < 180)
        {
            longitudeIncrease = longitudeMetersToDecimalDegrees(Math.cos(differenceInRadians) * distanceToEdgeInMeters, latitude);
        }
        else if (currentAngle >= 180 && currentAngle < 270)
        {
            longitudeIncrease = -longitudeMetersToDecimalDegrees(Math.sin(differenceInRadians) * distanceToEdgeInMeters, latitude);
        }
        else
        {
            longitudeIncrease = -longitudeMetersToDecimalDegrees(Math.cos(differenceInRadians) * distanceToEdgeInMeters, latitude);
        }

        System.out.println("Calculated LONGITUDE increase of " + longitudeIncrease + " with current angle "
                           + currentAngle + " , differenceOverNinetyDegrees " + differenceOverNinetyDegrees +
                           ", differenceInRadians " + differenceInRadians + ", and distanceToEdgeInMeters " + distanceToEdgeInMeters);

        return longitudeIncrease;
    }

    private double latitudeMetersToDecimalDegrees(double meters)
    {
        System.out.println("Converting LATITUDE: " + meters + "m to " + meters / metersToOneLatitudinalDegree + "DD");
        return meters / metersToOneLatitudinalDegree;
    }

    private double longitudeMetersToDecimalDegrees(double meters, double latitude)
    {
        System.out.println("Converting LONGITUDE: " + meters + "m to " + meters / metersToOneLatitudinalDegree + "DD at latitude " + latitude);
        return meters / (metersToOneLatitudinalDegree * Math.cos(Math.toRadians(latitude)));
    }
}
