package no.uio.sonen.alchemylab.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class CameraController extends InputAdapter {
    public DirectionY camY = DirectionY.STILL;
    public DirectionX camX = DirectionX.STILL;

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.UP) {
            camY = DirectionY.UP;
            return true;
        } else if (keycode == Input.Keys.DOWN) {
            camY = DirectionY.DOWN;
            return true;
        }

        if (keycode == Input.Keys.LEFT) {
            camX = DirectionX.LEFT;
            return true;
        } else if (keycode == Input.Keys.RIGHT) {
            camX = DirectionX.RIGHT;
            return true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.UP && camY == DirectionY.UP) {
            camY = DirectionY.STILL;
            return true;
        } else if (keycode == Input.Keys.DOWN && camY == DirectionY.DOWN) {
            camY = DirectionY.STILL;
            return true;
        }

        if (keycode == Input.Keys.LEFT && camX == DirectionX.LEFT) {
            camX = DirectionX.STILL;
            return true;
        } else if (keycode == Input.Keys.RIGHT && camX == DirectionX.RIGHT) {
            camX = DirectionX.STILL;
            return true;
        }

        return false;
    }
}
