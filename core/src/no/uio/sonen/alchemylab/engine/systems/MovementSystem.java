package no.uio.sonen.alchemylab.engine.systems;

import com.badlogic.ashley.core.ComponentType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Bits;
import no.uio.sonen.alchemylab.engine.components.MovementComponent;
import no.uio.sonen.alchemylab.engine.components.TransformComponent;

public class MovementSystem extends IteratingSystem {
    private static final Bits all = ComponentType.getBitsFor(TransformComponent.class, MovementComponent.class);
    private static final Bits one = ComponentType.getBitsFor();
    private static final Bits exclude = ComponentType.getBitsFor();
    private static final Family family = Family.getFor(all, one, exclude);

    public MovementSystem(int priority) {
        super(family, priority);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {

    }
}
