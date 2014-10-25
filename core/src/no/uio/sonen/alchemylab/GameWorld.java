package no.uio.sonen.alchemylab;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import no.uio.sonen.alchemylab.engine.components.*;
import no.uio.sonen.alchemylab.engine.systems.RenderSystem;

public class GameWorld implements Disposable {
    public static final Vector2 gravity = new Vector2(0, -32);

    private final PooledEngine engine;
    private final OrthographicCamera camera;
    private final TmxMapLoader mapLoader;

    private TiledMap map;

    public GameWorld(PooledEngine engine, OrthographicCamera camera) {
        this.engine = engine;
        this.camera = camera;

        mapLoader = new TmxMapLoader();
        testWorld();
    }

    private void testWorld() {
        loadMap(WorldMaps.WORLD1);
        spawnPlayer(64, 32);
    }

    private void spawnPlayer(float x, float y) {
        Entity entity = engine.createEntity();

        BoundsComponent boundsComponent = new BoundsComponent();
        MovementComponent movementComponent = new MovementComponent();
        TransformComponent transformComponent= new TransformComponent();
        TextureComponent textureComponent = new TextureComponent();
        PlayerComponent playerComponent = new PlayerComponent();

        boundsComponent.rectangle.setPosition(x, y);
        transformComponent.pos.set(x, y);
        textureComponent.region = Assets.mario;

        entity.add(boundsComponent);
        entity.add(movementComponent);
        entity.add(transformComponent);
        entity.add(textureComponent);
        entity.add(playerComponent);

        engine.addEntity(entity);
    }


    public void loadMap(WorldMaps mapFile) {
        if (this.map != null) {
            this.map.dispose();
        }

        this.map = mapLoader.load(mapFile.getFilename());
        engine.getSystem(RenderSystem.class).setMap(this.map);
    }

    @Override
    public void dispose() {
        map.dispose();
    }
}
