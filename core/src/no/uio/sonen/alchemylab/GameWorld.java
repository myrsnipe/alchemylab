package no.uio.sonen.alchemylab;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import no.uio.sonen.alchemylab.engine.components.*;
import no.uio.sonen.alchemylab.engine.systems.CollisionSystem;
import no.uio.sonen.alchemylab.engine.systems.RenderSystem;

public class GameWorld implements Disposable {
    public static final int PIXEL_SIZE = 16;
    public static final Vector2 gravity = new Vector2(0, -32);

    private final PooledEngine engine;
    private final OrthographicCamera camera;
    private final TmxMapLoader mapLoader;

    private TiledMap map;
    private SpatialHashGrid spatialHashGrid;

    public GameWorld(PooledEngine engine, OrthographicCamera camera) {
        this.engine = engine;
        this.camera = camera;

        mapLoader = new TmxMapLoader();
        testWorld();
    }

    private void testWorld() {
        loadMap(WorldMaps.WORLD1);
        spawnPlayer(64, 64);
    }

    private void spawnPlayer(float x, float y) {
        Entity entity = engine.createEntity();

        MovementComponent movementComponent = new MovementComponent();
        TransformComponent transformComponent= new TransformComponent(x, y, 0);
        TextureComponent textureComponent = new TextureComponent();
        PlayerComponent playerComponent = new PlayerComponent();
        StateComponent stateComponent = new StateComponent();

        textureComponent.region = Assets.mario;

        float width = textureComponent.region.getRegionWidth();
        float height = textureComponent.region.getRegionHeight();

        BoundsComponent boundsComponent = new BoundsComponent(x, y, width, height);

        entity.add(boundsComponent);
        entity.add(movementComponent);
        entity.add(transformComponent);
        entity.add(textureComponent);
        entity.add(playerComponent);
        entity.add(stateComponent);

        engine.addEntity(entity);
    }


    public void loadMap(WorldMaps mapFile) {
        if (this.map != null) {
            this.map.dispose();
        }

        this.map = mapLoader.load(mapFile.getFilename());
        engine.getSystem(RenderSystem.class).setMap(this.map);

        float mapWidth = Float.parseFloat("" + map.getProperties().get("width")) * PIXEL_SIZE;
        float mapHeight = Float.parseFloat("" + map.getProperties().get("height")) * PIXEL_SIZE;

        Gdx.app.log("map", "mapWidth: " + mapWidth);
        Gdx.app.log("map", "mapHeight: " + mapHeight);


        spatialHashGrid = new SpatialHashGrid(mapWidth, mapHeight, PIXEL_SIZE);

        TiledMapTileLayer.Cell cell;
        MapProperties cellProperties;
        for (int i = 0; i < this.map.getLayers().getCount(); i++) {
            MapLayer mapLayer = this.map.getLayers().get(i);

            if (mapLayer instanceof TiledMapTileLayer) {
                TiledMapTileLayer tileLayer = (TiledMapTileLayer) mapLayer;

                for (int y = 0; y < tileLayer.getHeight(); y++) {
                    for (int x = 0; x < tileLayer.getWidth(); x++) {
                        cell = tileLayer.getCell(x, y);

                        if (cell != null) {
                            cellProperties = cell.getTile().getProperties();

                            if (cellProperties.containsKey("block")) {
                                createTileBounds(x * PIXEL_SIZE, y * PIXEL_SIZE);
                            }
                        }
                    }
                }
            }
        }

        engine.getSystem(CollisionSystem.class).setSpatialHashGrid(spatialHashGrid);
    }

    private void createTileBounds(int x, int y) {
        Entity entity = new Entity();

        BoundsComponent boundsComponent = new BoundsComponent(x, y, PIXEL_SIZE, PIXEL_SIZE);

        entity.add(boundsComponent);

        engine.addEntity(entity);

        spatialHashGrid.addStatic(entity);
    }

    @Override
    public void dispose() {
        map.dispose();
    }
}
