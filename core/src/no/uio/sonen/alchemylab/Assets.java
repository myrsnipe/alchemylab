package no.uio.sonen.alchemylab;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable {
    public static final Assets INSTANCE = new Assets();

    private static final String SPRITESHEET = "tiles.png";

    private final AssetManager manager;
    private final Texture texture;

    public static TextureRegion player;
    public static TextureRegion master;
    public static TextureRegion potion;
    public static TextureRegion butterfly;


    public Assets() {
        manager = new AssetManager();

        manager.load(SPRITESHEET, Texture.class);
        manager.finishLoading();

        texture = manager.get(SPRITESHEET, Texture.class);
        player = new TextureRegion(texture, 0 * 16, 16 * 16, 16, 32);
        master = new TextureRegion(texture, 4 * 16, 16 * 16, 16, 32);
        potion = new TextureRegion(texture, 0 * 16, 14 * 16, 16, 16);
        butterfly = new TextureRegion(texture, 3 * 16, 18 * 16, 32, 32);
    }

    @Override
    public void dispose() {
        manager.dispose();
        texture.dispose();
    }
}
