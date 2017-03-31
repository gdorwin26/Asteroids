package gd26.asteroids;

/**
 * Properties class containing information about
 * the game.
 *
 * Created by gdorwin26 on 3/29/2017.
 */
public final class Properties
{
    private Properties() {}

    public static final String WINDOW_TITLE = "Asteroids by Garek D.";
    public static final int WINDOW_START_WIDTH = 1200;
    public static final int WINDOW_START_HEIGHT = 750;

    public static final int ASTEROID_START_COUNT = 10;
    public static final int ASTEROID_MAX_COUNT = 20;
    public static final int ASTEROID_MIN_SIZE = 25;
    public static final int ASTEROID_MAX_SIZE = 75;

    public static final double DEBRIS_LIFETIME = 2.5;
    public static final double DEBRIS_SPREAD = 10;
    public static final int DEBRIS_PARTICLE_COUNT = 20;

    public static final double SHIP_ACCELERATION = 1000;
    public static final double SHIP_TURN_SPEED = 2 * Math.PI;
    public static final double SHIP_MAX_SPEED = 250;

    public static final double BULLET_SPEED = 500;
    public static final double BULLETS_PER_SECOND = 10;
}