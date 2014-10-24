package no.uio.sonen.alchemylab;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import no.uio.sonen.alchemylab.screens.GameScreen;

public class AlchemyLab extends Game {
    private static final float red = 107f / 255f;
    private static final float green = 140f / 255f;
    private static final float blue = 1f;
    private static final float alpha = 0f;

//    public static final float VIEWPORT_HEIGHT = 448;
//    public static final float VIEWPORT_WIDTH = 512;
    public static final float VIEWPORT_HEIGHT = 256;
    public static final float VIEWPORT_WIDTH = 240;

	public SpriteBatch batch;
	
	@Override
	public void create () {
        batch = new SpriteBatch();
        batch.enableBlending();
        setScreen(new GameScreen(this, batch));
	}

    @Override
    public void render() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(red, green, blue, alpha);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render();
    }
}
