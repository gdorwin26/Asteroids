package gd26.asteroids;

import java.awt.*;

/**
 * Shaped object class
 *
 * Created by gdorwin26 on 3/30/2017.
 */
public class ShapedObject extends GameObject
{
    private Vec2D[] vertices;
    private double radiusSquared;

    public ShapedObject(Vec2D[] vertices)
    {
        this.vertices = vertices;
    }

    public ShapedObject()
    {

    }

    public void render(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate((int) getPos().getX(), (int) getPos().getY());

        g2d.setColor(new Color(32, 128, 255));
        g2d.setStroke(new BasicStroke(2F));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawLine((int) vertices[0].getX(), (int) vertices[0].getY(),
                (int)vertices[vertices.length - 1].getX(), (int) vertices[vertices.length - 1].getY());
        for(int i = 0; i < vertices.length - 1; i++)
        {
            g2d.drawLine((int) vertices[i].getX(), (int) vertices[i].getY(),
                    (int) vertices[i + 1].getX(), (int) vertices[i + 1].getY());
        }

        g2d.dispose();
    }

    public void setVertices(Vec2D[] vertices)
    {
        this.vertices = vertices;
    }

    public void rotate(double theta)
    {
        for(Vec2D vertex : vertices)
        {
            vertex.rot(theta);
        }
    }

    public void setRadius(double radius)
    {
        radiusSquared = radius * radius;
    }

    public boolean checkCollision(ShapedObject so)
    {
        return MathUtil.squareDistance(getPos().getX(), getPos().getY(), so.getPos().getX(), so.getPos().getY()) <=
                radiusSquared + so.radiusSquared;
    }

    public boolean checkCollision(Vec2D point)
    {
        return MathUtil.squareDistance(getPos().getX(), getPos().getY(), point.getX(), point.getY()) <= radiusSquared;
    }
}