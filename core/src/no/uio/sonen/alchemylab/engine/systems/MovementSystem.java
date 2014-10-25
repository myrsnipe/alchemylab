package no.uio.sonen.alchemylab.engine.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.ComponentType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Bits;
import no.uio.sonen.alchemylab.engine.components.MovementComponent;
import no.uio.sonen.alchemylab.engine.components.TransformComponent;

public class MovementSystem extends IteratingSystem {
    private static final Bits all = ComponentType.getBitsFor(TransformComponent.class, MovementComponent.class);
    private static final Bits one = ComponentType.getBitsFor();
    private static final Bits exclude = ComponentType.getBitsFor();
    private static final Family family = Family.getFor(all, one, exclude);

    private Vector2 tmp = new Vector2();

    private ComponentMapper<TransformComponent> tm;
//    private ComponentMapper<BoundsComponent> bm;
    private ComponentMapper<MovementComponent> mm;


    public MovementSystem(int priority) {
        super(family, priority);

        //        bm = ComponentMapper.getFor(BoundsComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        mm = ComponentMapper.getFor(MovementComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        TransformComponent pos = tm.get(entity);
//        BoundsComponent bound = bm.get(entity);
        MovementComponent mov = mm.get(entity);

        tmp.set(mov.accel).scl(deltaTime);
        mov.velocity.add(tmp);

        tmp.set(mov.velocity).scl(deltaTime);
        pos.pos.add(tmp.x, tmp.y);

//        bound.bounds.x = pos.pos.x;
//        bound.bounds.y = pos.pos.y;
    }


}
