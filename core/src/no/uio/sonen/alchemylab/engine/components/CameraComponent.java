package no.uio.sonen.alchemylab.engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Pool.Poolable;

public class CameraComponent extends Component implements Poolable {

    public OrthographicCamera camera;

    @Override
    public void reset() {
        camera = null;
    }
}
