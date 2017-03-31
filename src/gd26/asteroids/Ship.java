package gd26.asteroids;

import java.awt.*;
import java.awt.event.KeyEvent;

import static gd26.asteroids.Properties.ASTEROID_START_COUNT;

/**
 * Ship class
 *
 * Created by gdorwin26 on 3/30/2017.
 */
public class Ship extends ShapedObject
{
    private boolean isDead;
    private double theta;
    private double lastTimeBullet;

    public Ship()
    {
        setVertices(new Vec2D[]
        {
                new Vec2D(0, -1).mult(20),
                new Vec2D(-0.5, 0.5).mult(20),
                new Vec2D(0, 0.2).mult(20),
                new Vec2D(0.5, 0.5).mult(20)
        });
        setRadius(20);
        setPos(new Vec2D(Properties.WINDOW_START_WIDTH / 2 - 20, Properties.WINDOW_START_HEIGHT / 2 - 20));
        isDead = false;
        theta = -Math.PI / 2;
        lastTimeBullet = 0;
    }

    @Override
    public void update(double deltaTime)
    {
        if(!isDead)
        {
            if(getVel().sqrMag() > Properties.SHIP_MAX_SPEED * Properties.SHIP_MAX_SPEED)
            {
                getVel().div(getVel().mag()).mult(Properties.SHIP_MAX_SPEED);
            }
            super.update(deltaTime);
            if(Input.isKeyDown(KeyEvent.VK_W))
            {
                getAcc().add(new Vec2D(0, -1));
            }
            if(Input.isKeyDown(KeyEvent.VK_A))
            {
                getAcc().add(new Vec2D(-1, 0));
            }
            if(Input.isKeyDown(KeyEvent.VK_S))
            {
                getAcc().add(new Vec2D(0, 1));
            }
            if(Input.isKeyDown(KeyEvent.VK_D))
            {
                getAcc().add(new Vec2D(1, 0));
            }
            if(Input.isKeyDown(KeyEvent.VK_LEFT))
            {
                double deltaTheta = -Properties.SHIP_TURN_SPEED * deltaTime;
                rotate(deltaTheta);
                theta += deltaTheta;
            }
            if(Input.isKeyDown(KeyEvent.VK_RIGHT))
            {
                double deltaTheta = Properties.SHIP_TURN_SPEED * deltaTime;
                rotate(deltaTheta);
                theta += deltaTheta;
            }
            if(Input.isKeyDown(KeyEvent.VK_SPACE))
            {
                if(lastTimeBullet >= 1 / Properties.BULLETS_PER_SECOND)
                {
                    AsteroidsMain.BULLETS_TO_ADD.add(new Bullet(getPos(), theta));
                    lastTimeBullet = 0;
                }
            }
            double accMag = getAcc().mag();
            getAcc().div(accMag > 1 ? accMag : 1);
            getAcc().mult(Properties.SHIP_ACCELERATION);
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
            lastTimeBullet += deltaTime;
            for(Asteroid asteroid : AsteroidsMain.ASTEROIDS)
            {
                if(asteroid.checkCollision(this))
                {
                    AsteroidsMain.DEBRIS_TO_ADD.add(new Debris(getPos().copy(), getVel().copy()));
                    asteroid.destroy();
                    isDead = true;
                    return;
                }
            }
        }
        else if(Input.isKeyDown(KeyEvent.VK_SPACE))
        {
            AsteroidsMain.SHIP = new Ship();
            AsteroidsMain.SCORE = 0;
            AsteroidsMain.BULLETS.clear();
            AsteroidsMain.DEBRIS.clear();
            AsteroidsMain.ASTEROIDS.clear();
            for(int i = 0; i < ASTEROID_START_COUNT; i++)
            {
                AsteroidsMain.ASTEROIDS.add(new Asteroid());
            }
            isDead = false;
        }
    }

    @Override
    public void render(Graphics g)
    {
        if(!isDead)
        {
            super.render(g);
        }
        else
        {
            g.setColor(Color.RED);
            g.setFont(new Font("Monospaced", Font.BOLD, 64));
            g.drawString("YOU LOSE", Properties.WINDOW_START_WIDTH / 2 - 165,
                    Properties.WINDOW_START_HEIGHT / 2 - 100);
            g.setFont(new Font("Monospaced", Font.BOLD, 32));
            g.drawString("FINAL SCORE: " + AsteroidsMain.SCORE, Properties.WINDOW_START_WIDTH / 2 - 185,
                    Properties.WINDOW_START_HEIGHT / 2);
            g.drawString("(PRESS SPACE TO RESTART)", Properties.WINDOW_START_WIDTH / 2 - 235,
                    Properties.WINDOW_START_HEIGHT / 2 + 100);
        }
    }
}