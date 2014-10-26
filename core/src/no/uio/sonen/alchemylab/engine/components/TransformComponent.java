package no.uio.sonen.alchemylab.engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class TransformComponent extends Component implements Poolable {
    public final Vector2 curPos = new Vector2();
    public final Vector2 nextPos = new Vector2();

    public final Vector2 scale = new Vector2();
    public float rotation = 0f;
    public float z = 0;

    public TransformComponent(float x, float y) {
        curPos.set(x, y);
        nextPos.set(x, y);
        z = 0;
    }

    public TransformComponent(float x, float y, float z) {
        curPos.set(x, y);
        nextPos.set(x, y);
        this.z = z;
    }

    @Override
    public void reset() {
        curPos.set(Vector2.Zero);
        nextPos.set(Vector2.Zero);
        scale.set(Vector2.Zero);
        rotation = 0f;
        z = 0;
    }
}
