package gd26.asteroids;

/**
 * Math utility class
 *
 * Created by gdorwin26 on 3/30/2017.
 */
public final class MathUtil
{
    public static double squareDistance(double x1, double y1, double x2, double y2)
    {
        double dX = x2 - x1;
        double dY = y2 - y1;
        return dX * dX + dY * dY;
    }
}