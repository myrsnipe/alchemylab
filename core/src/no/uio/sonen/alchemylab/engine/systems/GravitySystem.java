package no.uio.sonen.alchemylab.engine.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.ComponentType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Bits;
import no.uio.sonen.alchemylab.Constants;
import no.uio.sonen.alchemylab.GameWorld;
import no.uio.sonen.alchemylab.engine.components.MovementComponent;
import no.uio.sonen.alchemylab.engine.components.PlayerComponent;

public class GravitySystem extends IteratingSystem {
    private static final Bits all = ComponentType.getBitsFor(PlayerComponent.class);
    private static final Bits one = ComponentType.getBitsFor();
    private static final Bits exclude = ComponentType.getBitsFor();
    private static final Family family = Family.getFor(all, one, exclude);

    private ComponentMapper<MovementComponent> mm;

    public GravitySystem(int priority) {
        super(family, priority);

        mm = ComponentMapper.getFor(MovementComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        MovementComponent mov = mm.get(entity);
        mov.velocity.add(0, Constants.gravity * deltaTime);
    }
}
