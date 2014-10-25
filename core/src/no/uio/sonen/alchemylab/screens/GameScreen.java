package no.uio.sonen.alchemylab.screens;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import no.uio.sonen.alchemylab.AlchemyLab;
import no.uio.sonen.alchemylab.GameController;
import no.uio.sonen.alchemylab.GameWorld;
import no.uio.sonen.alchemylab.engine.systems.CameraSystem;
import no.uio.sonen.alchemylab.engine.systems.RenderSystem;

public class GameScreen extends ScreenAdapter {
    private final Game game;
    private final SpriteBatch batch;

    private final PooledEngine engine;
    private final OrthographicCamera camera;

    private GameWorld world;

    public GameController controller;

    public GameScreen(Game game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;

        controller = new GameController();
        Gdx.input.setInputProcessor(controller);

        engine = new PooledEngine();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, AlchemyLab.VIEWPORT_WIDTH, AlchemyLab.VIEWPORT_HEIGHT);

        CameraSystem cameraSystem = new CameraSystem(camera, controller, 0);

        RenderSystem renderSystem = new RenderSystem(batch, camera, 1);

        engine.addSystem(cameraSystem);
        engine.addSystem(renderSystem);

        world = new GameWorld(engine, camera);
    }

    @Override
    public void render(float delta) {
        camera.update();
        engine.update(delta);
    }
}