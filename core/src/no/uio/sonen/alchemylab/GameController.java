package no.uio.sonen.alchemylab;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class GameController extends InputAdapter {
    public enum Direction {
        STILL,
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    public Direction camY = Direction.STILL;
    public Direction camX = Direction.STILL;

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.UP) {
            camY = Direction.UP;
            return true;
        } else if (keycode == Input.Keys.DOWN) {
            camY = Direction.DOWN;
            return true;
        }

        if (keycode == Input.Keys.LEFT) {
            camX = Direction.LEFT;
            return true;
        } else if (keycode == Input.Keys.RIGHT) {
            camX = Direction.RIGHT;
            return true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        boolean handled = false;

        if (keycode == Input.Keys.UP && camY == Direction.UP) {
            camY = Direction.STILL;
            handled = true;
        } else if (keycode == Input.Keys.DOWN && camY == Direction.DOWN) {
            camY = Direction.STILL;
            handled = true;
        }

        if (keycode == Input.Keys.LEFT && camX == Direction.LEFT) {
            camX = Direction.STILL;
            handled = true;
        } else if (keycode == Input.Keys.RIGHT && camX == Direction.RIGHT) {
            camX = Direction.STILL;
            handled = true;
        }

        return handled;
    }
}
