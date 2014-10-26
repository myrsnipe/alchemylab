package no.uio.sonen.alchemylab.engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import no.uio.sonen.alchemylab.Constants;
import no.uio.sonen.alchemylab.JumpState;

public class StateComponent extends Component implements Poolable {
    public JumpState jump = JumpState.STILL;
    public int jumpTicks = Constants.PLAYER_JUMP_MAX_TICKS;

    @Override
    public void reset() {
        jump = JumpState.STILL;
        jumpTicks = Constants.PLAYER_JUMP_MAX_TICKS;
    }
}
