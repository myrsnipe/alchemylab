package no.uio.sonen.alchemylab.engine.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import no.uio.sonen.alchemylab.GameController;

import static no.uio.sonen.alchemylab.GameController.Direction;

public class CameraSystem extends EntitySystem {

    private final OrthographicCamera camera;
    private final GameController controller;

    private final float movementSpeed = 128;

    public CameraSystem(OrthographicCamera camera, GameController controller, int priority) {
        super(priority);

        this.camera = camera;
        this.controller = controller;
    }


    @Override
    public void update(float deltaTime) {
        Gdx.app.log("CameraSystem", "" + controller.direction);

        if (controller.direction == Direction.UP) {
            camera.position.add(0 , movementSpeed * deltaTime, 0);
            Gdx.app.log("CameraSystem", "1");
        } else if (controller.direction == Direction.DOWN) {
            camera.position.add(0 , -movementSpeed * deltaTime, 0);
            Gdx.app.log("CameraSystem", "2");
        } else if (controller.direction == Direction.LEFT) {
            camera.position.add(-movementSpeed * deltaTime, 0, 0);
            Gdx.app.log("CameraSystem", "3");
        } else if (controller.direction == Direction.RIGHT) {
            camera.position.add(movementSpeed * deltaTime, 0, 0);
            Gdx.app.log("CameraSystem", "4");
        }
    }
}
