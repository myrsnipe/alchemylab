package no.uio.sonen.alchemylab;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import no.uio.sonen.alchemylab.engine.components.CameraComponent;
import no.uio.sonen.alchemylab.engine.systems.RenderSystem;

public class GameWorld implements Disposable {
    public static final Vector2 gravity = new Vector2(0, -256);

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
        spawnCamera();
    }

    private void spawnCamera() {
        Entity entity = new Entity();

        CameraComponent cameraComponent = new CameraComponent();

        cameraComponent.camera = this.camera;

        entity.add(cameraComponent);

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
