package no.uio.sonen.alchemylab.engine.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Bits;
import no.uio.sonen.alchemylab.Constants;
import no.uio.sonen.alchemylab.controller.CameraController;
import no.uio.sonen.alchemylab.controller.DirectionX;
import no.uio.sonen.alchemylab.controller.DirectionY;
import no.uio.sonen.alchemylab.engine.components.PlayerComponent;
import no.uio.sonen.alchemylab.engine.components.TextureComponent;
import no.uio.sonen.alchemylab.engine.components.TransformComponent;

public class CameraSystem extends IteratingSystem {
    private static final Bits all = ComponentType.getBitsFor(PlayerComponent.class);
    private static final Bits one = ComponentType.getBitsFor();
    private static final Bits exclude = ComponentType.getBitsFor();
    private static final Family family = Family.getFor(all, one, exclude);

    private final OrthographicCamera camera;
    private final CameraController controller;

    private final float movementSpeed = 128;

    private final ComponentMapper<TransformComponent> tm;

    public CameraSystem(OrthographicCamera camera, CameraController controller, int priority) {
        super(family, priority);

        this.camera = camera;
        this.controller = controller;

        tm = ComponentMapper.getFor(TransformComponent.class);
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

//        controllerCam(deltaTime);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        TransformComponent tc = tm.get(entity);

        camera.position.set(tc.nextPos, 0);

        if (camera.position.y < Constants.VIEWPORT_HEIGHT / 2) {
            camera.position.set(camera.position.x, Constants.VIEWPORT_HEIGHT / 2, 0);
        }

        if (camera.position.x < Constants.VIEWPORT_WIDTH / 2) {
            camera.position.set(Constants.VIEWPORT_WIDTH/ 2, camera.position.y, 0);
        }
    }

    private void controllerCam(float deltaTime) {
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
