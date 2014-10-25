package no.uio.sonen.alchemylab.engine.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DebugRenderSystem extends EntitySystem {

    private final SpriteBatch batch;

    public DebugRenderSystem(SpriteBatch batch, int priority) {
        super(priority);

        this.batch = batch;
    }

    @Override
    public void update(float deltaTime) {

    }
}
