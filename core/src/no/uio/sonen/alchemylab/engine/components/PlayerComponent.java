package no.uio.sonen.alchemylab.engine.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Disposable;
import no.uio.sonen.alchemylab.JumpState;

public class PlayerComponent extends Component implements Disposable {
    public JumpState jump = JumpState.STILL;

    @Override
    public void dispose() {
        jump = JumpState.STILL;
    }
}
