package no.uio.sonen.alchemylab.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class PlayerController extends InputAdapter {
    public DirectionY directionY = DirectionY.STILL;
    public DirectionX directionX = DirectionX.STILL;
    public boolean jumping = false;

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.W) {
            directionY = DirectionY.UP;
            return true;

        } else if (keycode == Input.Keys.S) {
            directionY = DirectionY.DOWN;
            return true;
        }

        if (keycode == Input.Keys.A) {
            directionX = DirectionX.LEFT;
            return true;
        } else if (keycode == Input.Keys.D) {
            directionX = DirectionX.RIGHT;
            return true;
        }

        if (keycode == Input.Keys.SPACE) {
            jumping = true;
            return true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.W && directionY == DirectionY.UP) {
            directionY = DirectionY.STILL;
            return true;
        } else if (keycode == Input.Keys.S && directionY == DirectionY.DOWN) {
            directionY = DirectionY.STILL;
            return true;
        }

        if (keycode == Input.Keys.A && directionX == DirectionX.LEFT) {
            directionX = DirectionX.STILL;
            return true;
        } else if (keycode == Input.Keys.D && directionX == DirectionX.RIGHT) {
            directionX = DirectionX.STILL;
            return true;
        }

        if (keycode == Input.Keys.SPACE) {
            jumping = false;
            return true;
        }

        return false;
    }
}
