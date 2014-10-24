package no.uio.sonen.alchemylab.engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class TransformComponent extends Component implements Poolable {
    public final Vector2 pos = new Vector2();
    public final Vector2 scale = new Vector2();
    public float rotation = 0f;
    public int z = 0;

    @Override
    public void reset() {
        pos.set(Vector2.Zero);
        scale.set(Vector2.Zero);
        rotation = 0f;
        z = 0;
    }
}
