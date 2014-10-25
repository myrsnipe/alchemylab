package no.uio.sonen.alchemylab.engine.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DebugRenderSystem extends EntitySystem {

    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final BitmapFont font;
    private CharSequence fpsCounter;

    public DebugRenderSystem(SpriteBatch batch, OrthographicCamera camera, int priority) {
        super(priority);

        this.batch = batch;
        this.camera = camera;
        font = new BitmapFont();
    }

    @Override
    public void update(float deltaTime) {
        fpsCounter =  "fps: " + Gdx.graphics.getFramesPerSecond();
        float fpsX = camera.position.x - (camera.viewportWidth / 2) + 8;
        float fpsY = camera.position.y + (camera.viewportHeight / 2) - 8;

        batch.begin();
        font.draw(batch, fpsCounter, fpsX, fpsY);
        batch.end();
    }
}
