package no.uio.sonen.alchemylab;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable {
    public static final Assets INSTANCE = new Assets();

    private static final String FILE_SPRITE_ATLAS = "sprites.pack";

    private final AssetManager manager;
    private final TextureAtlas atlas;

    public static AtlasRegion mario;


    public Assets() {
        manager = new AssetManager();

        manager.load(FILE_SPRITE_ATLAS, TextureAtlas.class);
        manager.finishLoading();

        atlas = manager.get(FILE_SPRITE_ATLAS, TextureAtlas.class);

        mario = atlas.findRegion("mario");
    }

    @Override
    public void dispose() {
        manager.dispose();
        atlas.dispose();
    }
}
