package gd26.asteroids;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Peripheral input class
 *
 * Created by gdorwin26 on 3/30/2017.
 */
public final class Input
{
    private Input() {}

    private static KeyAdapter KEY_LISTENER;

    private static ArrayList<Integer> KEYS_DOWN;

    static
    {
        KEYS_DOWN = new ArrayList<>();
        KEY_LISTENER = new KeyAdapter()
        {
            @Override
            public void keyTyped(KeyEvent event)
            {

            }

            @Override
            public void keyPressed(KeyEvent event)
            {
                if(!KEYS_DOWN.contains(event.getKeyCode()))
                {
                    KEYS_DOWN.add(event.getKeyCode());
                }
            }

            @Override
            public void keyReleased(KeyEvent event)
            {
                if(KEYS_DOWN.contains(event.getKeyCode()))
                {
                    KEYS_DOWN.remove((Integer) event.getKeyCode());
                }
            }
        };
    }

    public static void registerToFrame(JFrame frame)
    {
        frame.addKeyListener(KEY_LISTENER);
    }

    public static boolean isKeyDown(int keyCode)
    {
        return KEYS_DOWN.contains(keyCode);
    }
}