package gd26.asteroids;

import javax.swing.*;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static gd26.asteroids.Properties.*;

/**
 * Main class
 *
 * Created by gdorwin26 on 3/29/2017.
 */
public final class AsteroidsMain
{
    public static ArrayList<Asteroid> ASTEROIDS;
    public static ArrayList<Asteroid> ASTEROIDS_TO_REMOVE;
    public static ArrayList<Asteroid> ASTEROIDS_TO_ADD;

    public static ArrayList<Debris> DEBRIS;
    public static ArrayList<Debris> DEBRIS_TO_REMOVE;
    public static ArrayList<Debris> DEBRIS_TO_ADD;

    public static ArrayList<Bullet> BULLETS;
    public static ArrayList<Bullet> BULLETS_TO_REMOVE;
    public static ArrayList<Bullet> BULLETS_TO_ADD;

    public static Ship SHIP;

    public static int SCORE;

    private static boolean IS_WINDOW_OPEN;
    private static JFrame FRAME;
    private static Canvas CANVAS;

    private AsteroidsMain() {}

    public static void main(String[] args)
    {
        IS_WINDOW_OPEN = false;
        openWindow();
    }

    private static void openWindow()
    {
        if(!IS_WINDOW_OPEN)
        {
            SwingUtilities.invokeLater(() ->
            {
                FRAME = new JFrame(WINDOW_TITLE);
                FRAME.setSize(WINDOW_START_WIDTH, WINDOW_START_HEIGHT);
                FRAME.setLocationRelativeTo(null);
                FRAME.add(CANVAS = new Canvas());
                FRAME.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                FRAME.addWindowListener(new WindowAdapter()
                {
                    @Override
                    public void windowOpened(WindowEvent ignored)
                    {
                        IS_WINDOW_OPEN = true;
                    }

                    @Override
                    public void windowClosing(WindowEvent ignored)
                    {
                        requestClose();
                    }

                    @Override
                    public void windowClosed(WindowEvent ignored)
                    {
                        IS_WINDOW_OPEN = false;
                    }
                });
                FRAME.setResizable(false);
                Input.registerToFrame(FRAME);
                FRAME.setVisible(true);
                CANVAS.startDrawing();
            });
        }
    }

    private static void requestClose()
    {
        SwingUtilities.invokeLater(() ->
        {
            if(IS_WINDOW_OPEN)
            {
                CANVAS.stopDrawing();
                FRAME.dispose();
            }
        });
    }

    private static class Canvas extends JComponent
    {
        private Timer updateTimer;
        private double deltaTime;
        private long lastTime, thisTime;
        private BufferedImage previousFrame;

        private Canvas()
        {
            lastTime = System.currentTimeMillis();
            updateTimer = new Timer(15, (event) ->
            {
                thisTime = System.currentTimeMillis();
                deltaTime = (thisTime - lastTime) / 1000D;
                repaint();
                Toolkit.getDefaultToolkit().sync();
                lastTime = thisTime;
            });

            previousFrame = new BufferedImage(WINDOW_START_WIDTH, WINDOW_START_HEIGHT, BufferedImage.TYPE_INT_ARGB);

            ASTEROIDS = new ArrayList<>();
            ASTEROIDS_TO_REMOVE = new ArrayList<>();
            ASTEROIDS_TO_ADD = new ArrayList<>();

            DEBRIS = new ArrayList<>();
            DEBRIS_TO_REMOVE = new ArrayList<>();
            DEBRIS_TO_ADD = new ArrayList<>();

            BULLETS = new ArrayList<>();
            BULLETS_TO_REMOVE = new ArrayList<>();
            BULLETS_TO_ADD = new ArrayList<>();

            SCORE = 0;

            for(int i = 0; i < ASTEROID_START_COUNT; i++)
            {
                ASTEROIDS.add(new Asteroid());
            }

            SHIP = new Ship();
        }

        private void startDrawing()
        {
            if(!updateTimer.isRunning())
            {
                updateTimer.start();
            }
        }

        private void stopDrawing()
        {
            if(updateTimer.isRunning())
            {
                updateTimer.stop();
            }
        }

        protected void paintComponent(Graphics g)
        {
            Graphics g2 = previousFrame.getGraphics();
            g2.setColor(new Color(0, 0, 0, 64));
            g2.fillRect(0, 0, getWidth(), getHeight());

            for(Asteroid asteroid : ASTEROIDS)
            {
                asteroid.render(g2);
                asteroid.update(deltaTime);
            }

            for(Debris debris : DEBRIS)
            {
                debris.render(g2);
                debris.update(deltaTime);
            }

            for(Bullet bullet : BULLETS)
            {
                bullet.render(g2);
                bullet.update(deltaTime);
            }

            ASTEROIDS.removeAll(ASTEROIDS_TO_REMOVE);
            ASTEROIDS_TO_REMOVE.clear();
            ASTEROIDS.addAll(ASTEROIDS_TO_ADD);
            ASTEROIDS_TO_ADD.clear();

            DEBRIS.removeAll(DEBRIS_TO_REMOVE);
            DEBRIS_TO_REMOVE.clear();
            DEBRIS.addAll(DEBRIS_TO_ADD);
            DEBRIS_TO_ADD.clear();

            BULLETS.removeAll(BULLETS_TO_REMOVE);
            BULLETS_TO_REMOVE.clear();
            BULLETS.addAll(BULLETS_TO_ADD);
            BULLETS_TO_ADD.clear();

            SHIP.render(g2);
            SHIP.update(deltaTime);

            g2.dispose();
            g.drawImage(previousFrame, 0, 0, null);
        }
    }
}