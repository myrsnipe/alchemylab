package no.uio.sonen.alchemylab.engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class MovementComponent extends Component implements Poolable {
    public final Vector2 accel = new Vector2();
    public final Vector2 velocity = new Vector2();

    @Override
    public void reset() {
        accel.set(Vector2.Zero);
        velocity.set(Vector2.Zero);
    }
}
