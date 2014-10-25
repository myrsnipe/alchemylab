package no.uio.sonen.alchemylab.engine.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import no.uio.sonen.alchemylab.controller.CameraController;
import no.uio.sonen.alchemylab.controller.DirectionX;
import no.uio.sonen.alchemylab.controller.DirectionY;

public class CameraSystem extends EntitySystem {

    private final OrthographicCamera camera;
    private final CameraController controller;

    private final float movementSpeed = 128;

    public CameraSystem(OrthographicCamera camera, CameraController controller, int priority) {
        super(priority);

        this.camera = camera;
        this.controller = controller;
    }


    @Override
    public void update(float deltaTime) {
        if (controller.camY == DirectionY.UP) {
            camera.position.add(0, movementSpeed * deltaTime, 0);
        } else if (controller.camY == DirectionY.DOWN) {
            camera.position.add(0, -movementSpeed * deltaTime, 0);
        }

        if (controller.camX == DirectionX.LEFT) {
            camera.position.add(-movementSpeed * deltaTime, 0, 0);
        } else if (controller.camX == DirectionX.RIGHT) {
            camera.position.add(movementSpeed * deltaTime, 0, 0);
        }
    }
}
