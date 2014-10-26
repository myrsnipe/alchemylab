package no.uio.sonen.alchemylab.engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import no.uio.sonen.alchemylab.AABB;

public class BoundsComponent extends Component implements Poolable {
    public final AABB curBounds = new AABB();
    public final AABB nextBounds = new AABB();

    public BoundsComponent(float x, float y, float width, float height) {
        curBounds.setCenter(x, y, width, height);
        nextBounds.set(curBounds);
    }

    @Override
    public void reset() {
        curBounds.clr();
        nextBounds.clr();
    }
}
