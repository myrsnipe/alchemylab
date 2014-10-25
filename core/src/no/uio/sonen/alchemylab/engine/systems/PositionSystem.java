package no.uio.sonen.alchemylab.engine.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.ComponentType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Bits;
import no.uio.sonen.alchemylab.engine.components.BoundsComponent;
import no.uio.sonen.alchemylab.engine.components.MovementComponent;
import no.uio.sonen.alchemylab.engine.components.TransformComponent;

public class PositionSystem extends IteratingSystem {
    private static final Bits all = ComponentType.getBitsFor(TransformComponent.class, MovementComponent.class);
    private static final Bits one = ComponentType.getBitsFor();
    private static final Bits exclude = ComponentType.getBitsFor();
    private static final Family family = Family.getFor(all, one, exclude);

    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<BoundsComponent> bm;


    public PositionSystem(int priority) {
        super(family, priority);

        bm = ComponentMapper.getFor(BoundsComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        TransformComponent pos = tm.get(entity);
        BoundsComponent bound = bm.get(entity);

        pos.curPos.set(pos.nextPos);
        bound.curBounds.setCenter(pos.nextPos);
    }


}
