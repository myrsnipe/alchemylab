package no.uio.sonen.alchemylab.screens;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import no.uio.sonen.alchemylab.AlchemyLab;
import no.uio.sonen.alchemylab.GameWorld;
import no.uio.sonen.alchemylab.SpatialHashGrid;
import no.uio.sonen.alchemylab.controller.CameraController;
import no.uio.sonen.alchemylab.controller.GameController;
import no.uio.sonen.alchemylab.controller.PlayerController;
import no.uio.sonen.alchemylab.engine.systems.*;

public class GameScreen extends ScreenAdapter {
    private final Game game;
    private final SpriteBatch batch;

    private final PooledEngine engine;
    private final OrthographicCamera camera;

    private GameWorld world;

    public GameController controller;
    public CameraController cameraController;
    public PlayerController playerController;

    public GameScreen(Game game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;

        playerController = new PlayerController();
        cameraController = new CameraController();
        controller = new GameController(playerController, cameraController);

        Gdx.input.setInputProcessor(controller);

        engine = new PooledEngine();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, AlchemyLab.VIEWPORT_WIDTH, AlchemyLab.VIEWPORT_HEIGHT);

        PlayerSystem playerSystem = new PlayerSystem(playerController, 0);
        CameraSystem cameraSystem = new CameraSystem(camera, cameraController, 1);
        MovementSystem movementSystem = new MovementSystem(2);
        GravitySystem gravitySystem = new GravitySystem(3);
        CollisionSystem collisionSystem = new CollisionSystem(4);
        PositionSystem positionSystem= new PositionSystem(5);
        RenderSystem renderSystem = new RenderSystem(batch, camera, 6);
        DebugRenderSystem debugRenderSystem = new DebugRenderSystem(batch, camera, 7);

        engine.addSystem(playerSystem);
        engine.addSystem(cameraSystem);
        engine.addSystem(movementSystem);
        engine.addSystem(gravitySystem);
        engine.addSystem(collisionSystem);
        engine.addSystem(positionSystem);
        engine.addSystem(renderSystem);
        engine.addSystem(debugRenderSystem);

        world = new GameWorld(engine, camera);
    }

    @Override
    public void render(float delta) {
        camera.update();
        engine.update(delta);
    }
}