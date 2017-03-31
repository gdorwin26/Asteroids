package gd26.asteroids;

/**
 * Game object class
 *
 * Created by gdorwin26 on 3/29/2017.
 */
public class GameObject
{
    private Vec2D pos, vel, acc;

    public GameObject()
    {
        pos = new Vec2D();
        vel = new Vec2D();
        acc = new Vec2D();
    }

    public void update(double deltaTime)
    {
        vel.add(acc.mult(deltaTime));
        acc.mult(0);
        pos.add(vel.copy().mult(deltaTime));
    }

    public Vec2D getPos()
    {
        return pos;
    }

    public Vec2D getVel()
    {
        return vel;
    }

    public Vec2D getAcc()
    {
        return acc;
    }

    public void setPos(Vec2D pos)
    {
        this.pos = pos;
    }

    public void setVel(Vec2D vel)
    {
        this.vel = vel;
    }

    public void setAcc(Vec2D acc)
    {
        this.acc = acc;
    }
}