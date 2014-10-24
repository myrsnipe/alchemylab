package no.uio.sonen.alchemylab.engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool.Poolable;

public class TextureComponent extends Component implements Poolable {
    public TextureRegion region;

    @Override
    public void reset() {
        region = null;
    }
}
