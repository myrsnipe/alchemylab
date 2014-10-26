package no.uio.sonen.alchemylab.engine.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import no.uio.sonen.alchemylab.Constants;

public class DebugRenderSystem extends EntitySystem {

    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final BitmapFont font;
    private CharSequence fpsCounter;
    private CharSequence possibleCollisions;
    private CharSequence collisions;
    private long startTime;

    public DebugRenderSystem(SpriteBatch batch, OrthographicCamera camera, int priority) {
        super(priority);

        this.batch = batch;
        this.camera = camera;
        font = new BitmapFont();
        startTime = TimeUtils.nanoTime();

        fpsCounter = "fps";
        possibleCollisions = "pCol";
        collisions = "col";
    }

    @Override
    public void update(float deltaTime) {
        if (TimeUtils.nanoTime() - startTime > 1000000000) /* 1,000,000,000ns == one second */ {
            startTime = TimeUtils.nanoTime();

            float fps = Gdx.graphics.getFramesPerSecond();

            fpsCounter = "fps: " + Gdx.graphics.getFramesPerSecond();

            if (fps > 0) {
                possibleCollisions = "pCol: " + MathUtils.ceil(Constants.possibleCollisions / fps);
                collisions = "col: " + MathUtils.ceil(Constants.collisions / fps);
            }

            Constants.possibleCollisions = 0;
            Constants.collisions = 0;
        }

        float fpsX = camera.position.x - (camera.viewportWidth / 2) + 8;
        float fpsY = camera.position.y + (camera.viewportHeight / 2);

        batch.begin();
        font.draw(batch, fpsCounter, fpsX, fpsY - 8);
        font.draw(batch, possibleCollisions, fpsX, fpsY - 24);
        font.draw(batch, collisions, fpsX, fpsY - 40);
        batch.end();
    }
}
