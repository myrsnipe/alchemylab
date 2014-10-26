package no.uio.sonen.alchemylab.engine.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.ComponentType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Bits;
import no.uio.sonen.alchemylab.engine.components.BoundsComponent;
import no.uio.sonen.alchemylab.engine.components.PlayerComponent;

public class CollisionRenderSystem extends IteratingSystem {
    private static final Bits all = ComponentType.getBitsFor(BoundsComponent.class);
    private static final Bits one = ComponentType.getBitsFor();
    private static final Bits exclude = ComponentType.getBitsFor();
    private static final Family family = Family.getFor(all, one, exclude);

    private static final Color color = new Color(0, 0, 0, 255f);

    private final Array<Entity> renderQueue;
    private final ShapeRenderer renderer;
    private final OrthographicCamera camera;

    private final ComponentMapper<BoundsComponent> bm;
    private final ComponentMapper<PlayerComponent> pm;

    private Texture texture;
    private Pixmap pixmap;

    public CollisionRenderSystem(OrthographicCamera camera, int priority) {
        super(family, priority);

        this.camera = camera;

        renderQueue = new Array<Entity>();

        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);

        bm = ComponentMapper.getFor(BoundsComponent.class);
        pm = ComponentMapper.getFor(PlayerComponent.class);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        renderer.setProjectionMatrix(camera.combined);
        renderer.begin();
        renderer.set(ShapeRenderer.ShapeType.Filled);

        for (Entity entity : renderQueue) {
            BoundsComponent bc = bm.get(entity);
            PlayerComponent pc = pm.get(entity);

            if (pc == null) {
                renderer.setColor(0f, 1.0f, 0, 0.5f);
            } else {
                renderer.setColor(1.0f, 0, 0, 0.5f);
            }

            renderer.rect(bc.nextBounds.getCenter().x, bc.nextBounds.getCenter().y, bc.nextBounds.dimensions.x, bc.nextBounds.dimensions.y);
        }

        renderer.end();
        renderQueue.clear();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }
}
