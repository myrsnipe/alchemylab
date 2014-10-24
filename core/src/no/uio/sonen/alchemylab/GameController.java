package no.uio.sonen.alchemylab;

import com.badlogic.gdx.Gdx;
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

    public Direction direction = Direction.STILL;

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.UP) {
            direction = Direction.UP;
            return true;
        } else if (keycode == Input.Keys.DOWN) {
            direction = Direction.DOWN;
            return true;
        }

        if (keycode == Input.Keys.LEFT) {
            direction = Direction.LEFT;
            return true;
        } else if (keycode == Input.Keys.RIGHT) {
            direction = Direction.RIGHT;
            return true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.UP && direction == Direction.UP) {
            direction = Direction.STILL;
            return true;
        } else if (keycode == Input.Keys.DOWN && direction == Direction.DOWN) {
            direction = Direction.STILL;
            return true;
        }

        if (keycode == Input.Keys.LEFT && direction == Direction.LEFT) {
            direction = Direction.STILL;
            return true;
        } else if (keycode == Input.Keys.RIGHT && direction == Direction.RIGHT) {
            direction = Direction.STILL;
            return true;
        }

        return false;
    }
}
