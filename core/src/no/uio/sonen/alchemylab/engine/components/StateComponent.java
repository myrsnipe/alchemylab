package no.uio.sonen.alchemylab.engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import no.uio.sonen.alchemylab.JumpState;

public class StateComponent extends Component implements Poolable {
    public JumpState jump = JumpState.STILL;

    @Override
    public void reset() {
        jump = JumpState.STILL;
    }
}
