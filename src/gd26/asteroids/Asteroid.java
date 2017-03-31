package gd26.asteroids;

import java.util.Random;

/**
 * Asteroid class
 *
 * Created by gdorwin26 on 3/29/2017.
 */
public class Asteroid extends ShapedObject
{
    private double rotationalSpeed;
    private int size;
    private double age;

  //10 - 12 vertices
    public Asteroid(int size)
    {
        Random random = new Random();
        int vertCount = 10 + random.nextInt(3);
        this.size = size;
        Vec2D[] vertices = new Vec2D[vertCount];
        double thetaInc = Math.PI * 2 / vertCount;
        for(int i = 0; i < vertCount; i++)
        {
            vertices[i] = new Vec2D(1, 0).rot(thetaInc * i).mult(size - random.nextInt(size / 2));
        }
        setVertices(vertices);
        setPos(new Vec2D(-100, random.nextInt(Properties.WINDOW_START_HEIGHT)));
        setVel(new Vec2D(1, 0).rot(random.nextDouble() * Math.PI * 2).mult(35 + random.nextInt(85)));
        rotationalSpeed = random.nextDouble() * 4 - random.nextDouble() * 2;
        setRadius(size);
    }

    public Asteroid()
    {
        this(Properties.ASTEROID_MIN_SIZE + new Random().nextInt(Properties.ASTEROID_MAX_SIZE -
                Properties.ASTEROID_MIN_SIZE));
    }

    @Override
    public void update(double deltaTime)
    {
        rotate(rotationalSpeed * deltaTime);
        super.update(deltaTime);
        if(getPos().getX() < -10)
        {
            getPos().setX(Properties.WINDOW_START_WIDTH + 10);
        }
        else if(getPos().getX() > Properties.WINDOW_START_WIDTH + 10)
        {
            getPos().setX(-10);
        }
        if(getPos().getY() < -10)
        {
            getPos().setY(Properties.WINDOW_START_HEIGHT + 10);
        }
        else if(getPos().getY() > Properties.WINDOW_START_HEIGHT + 10)
        {
            getPos().setY(-10);
        }
    }

    public void destroy()
    {
        int newSize = size / 2;
        AsteroidsMain.ASTEROIDS_TO_REMOVE.add(this);
        AsteroidsMain.DEBRIS_TO_ADD.add(new Debris(getPos(), getVel()));
        if(newSize < Properties.ASTEROID_MIN_SIZE)
        {
            //totally break and ADD ONE SCORE
            if(AsteroidsMain.ASTEROIDS.size() < Properties.ASTEROID_MAX_COUNT)
            {
                AsteroidsMain.ASTEROIDS_TO_ADD.add(new Asteroid());
            }
            return;
        }
        Asteroid a = new Asteroid(newSize);
        Asteroid b = new Asteroid(newSize);
        a.setVel(getVel().copy().rot(-Math.PI / 6).mult(1.25));
        b.setVel(getVel().copy().rot(Math.PI / 6).mult(1.25));
        a.setPos(getPos().copy().add(a.getVel().copy().mult(0.1)));
        b.setPos(getPos().copy().add(b.getVel().copy().mult(0.1)));
        AsteroidsMain.ASTEROIDS_TO_ADD.add(a);
        AsteroidsMain.ASTEROIDS_TO_ADD.add(b);
    }
}