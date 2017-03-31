package gd26.asteroids;

import java.awt.*;
import java.util.Random;

/**
 * Debris class
 *
 * Created by gdorwin26 on 3/30/2017.
 */
public class Debris extends GameObject
{
    private Vec2D[] particles;
    private Vec2D[] particleOriginals;
    private double time;

    public Debris(Vec2D position, Vec2D velocity)
    {
        setPos(position);
        setVel(velocity);
        time = 0;
        particles = new Vec2D[Properties.DEBRIS_PARTICLE_COUNT];
        particleOriginals = new Vec2D[Properties.DEBRIS_PARTICLE_COUNT];
        Random random = new Random();
        for(int i = 0; i < Properties.DEBRIS_PARTICLE_COUNT; i++)
        {
            particles[i] = new Vec2D(1, 0).rot(random.nextDouble() * Math.PI * 2).mult(1 +
                    random.nextDouble() * 4);
            particleOriginals[i] = particles[i].copy();
        }
    }

    @Override
    public void update(double deltaTime)
    {
        super.update(deltaTime);
        for(int i = 0; i < particles.length; i++)
        {
            Vec2D particle = particles[i];
            particle.add(particleOriginals[i].copy().mult(Properties.DEBRIS_SPREAD * deltaTime));
        }
        time += deltaTime;
        if(time >= Properties.DEBRIS_LIFETIME)
        {
            AsteroidsMain.DEBRIS_TO_REMOVE.add(this);
        }
    }

    public void render(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate((int) getPos().getX(), (int) getPos().getY());

        int alpha = (int) (255 * (Properties.DEBRIS_LIFETIME - time) / Properties.DEBRIS_LIFETIME);
        g2d.setColor(new Color(32, 128, 255, alpha));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for(Vec2D particle : particles)
        {
            g2d.fillOval((int) particle.getX(), (int) particle.getY(), 2, 2);
        }

        g2d.dispose();
    }
}