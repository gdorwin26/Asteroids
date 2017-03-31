package gd26.asteroids;

/**
 * Vector class
 *
 * Created by gdorwin26 on 3/29/2017.
 */
public class Vec2D
{
    private double x, y;

    public Vec2D()
    {
        this(0D, 0D);
    }

    public Vec2D(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public double sqrMag()
    {
        return x * x + y * y;
    }

    public double mag()
    {
        return Math.sqrt(sqrMag());
    }

    public Vec2D rot(double theta)
    {
        double x1 = x;
        double cos = Math.cos(theta);
        double sin = Math.sin(theta);
        x = x * cos - y * sin;
        y = x1 * sin + y * cos;
        return this;
    }

    public double dot(Vec2D vec)
    {
        return x * vec.x + y * vec.y;
    }

    public double theta()
    {
        return Math.atan2(y, x);
    }

    public Vec2D add(Vec2D vec)
    {
        x += vec.x;
        y += vec.y;
        return this;
    }

    public Vec2D sub(Vec2D vec)
    {
        x -= vec.x;
        y -= vec.x;
        return this;
    }

    public Vec2D mult(double scalar)
    {
        x *= scalar;
        y *= scalar;
        return this;
    }

    public Vec2D div(double scalar)
    {
        x /= scalar;
        y /= scalar;
        return this;
    }

    public Vec2D copy()
    {
        return new Vec2D(x, y);
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public void setY(double y)
    {
        this.y = y;
    }
}