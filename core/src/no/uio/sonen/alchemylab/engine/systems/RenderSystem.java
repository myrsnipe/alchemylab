package no.uio.sonen.alchemylab.engine.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.ComponentType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Bits;
import com.badlogic.gdx.utils.Disposable;
import no.uio.sonen.alchemylab.engine.components.TextureComponent;
import no.uio.sonen.alchemylab.engine.components.TransformComponent;

import java.util.Comparator;

public class RenderSystem extends IteratingSystem implements Disposable {
    private static final Bits all = ComponentType.getBitsFor(TextureComponent.class, TransformComponent.class);
    private static final Bits one = ComponentType.getBitsFor();
    private static final Bits exclude = ComponentType.getBitsFor();
    private static final Family family = Family.getFor(all, one, exclude);


    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final Array<Entity> renderQueue;
    private final Comparator<Entity> comparator;
    private final ComponentMapper<TextureComponent> texm;
    private final ComponentMapper<TransformComponent> posm;

    private OrthoCachedTiledMapRenderer tiledMapRenderer;

    public RenderSystem(SpriteBatch batch, OrthographicCamera camera, int priority) {
        super(family, priority);

        this.batch = batch;
        this.camera = camera;

        renderQueue = new Array<Entity>();
        texm = ComponentMapper.getFor(TextureComponent.class);
        posm = ComponentMapper.getFor(TransformComponent.class);

        comparator = new Comparator<Entity>() {
            @Override
            public int compare(Entity entityA, Entity entityB) {
                return (int)Math.signum(posm.get(entityB).z -
                        posm.get(entityA).z);
            }
        };


    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        renderQueue.sort(comparator);
        camera.update();

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for (Entity entity : renderQueue) {
            TextureComponent tex = texm.get(entity);
            TransformComponent t = posm.get(entity);

            batch.draw(tex.region, t.pos.x, t.pos.y);
        }

        batch.end();
        renderQueue.clear();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

    @Override
    public void dispose() {
        tiledMapRenderer.dispose();
    }

    public void setMap(TiledMap map) {
        if (tiledMapRenderer != null) {
            tiledMapRenderer.dispose();
        }

        tiledMapRenderer = new OrthoCachedTiledMapRenderer(map);
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.setBlending(true);
    }
}
