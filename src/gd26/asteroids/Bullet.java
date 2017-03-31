package gd26.asteroids;

import java.awt.*;

/**
 * Bullet class
 *
 * Created by gdorwin26 on 3/30/2017.
 */
public class Bullet extends GameObject
{
    public Bullet(Vec2D pos, double theta)
    {
        setPos(pos.copy());
        setVel(new Vec2D(1, 0).rot(theta).mult(Properties.BULLET_SPEED));
    }

    @Override
    public void update(double deltaTime)
    {
        super.update(deltaTime);
        for(Asteroid asteroid : AsteroidsMain.ASTEROIDS)
        {
            if(asteroid.checkCollision(getPos()))
            {
                asteroid.destroy();
                AsteroidsMain.SCORE += 5;
                AsteroidsMain.BULLETS_TO_REMOVE.add(this);
            }
        }
    }

    public void render(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate((int) getPos().getX(), (int) getPos().getY());

        g2d.setColor(new Color(32, 128, 255));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.fillOval(0, 0, 5, 5);

        g2d.dispose();
    }
}