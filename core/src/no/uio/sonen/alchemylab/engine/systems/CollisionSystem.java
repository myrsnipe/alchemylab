package no.uio.sonen.alchemylab.engine.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.ComponentType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Bits;
import no.uio.sonen.alchemylab.SpatialHashGrid;
import no.uio.sonen.alchemylab.engine.components.BoundsComponent;
import no.uio.sonen.alchemylab.engine.components.MovementComponent;
import no.uio.sonen.alchemylab.engine.components.StateComponent;
import no.uio.sonen.alchemylab.engine.components.TransformComponent;

public class CollisionSystem extends IteratingSystem {
    private static final Bits all = ComponentType.getBitsFor(BoundsComponent.class, TransformComponent.class, MovementComponent.class);
    private static final Bits one = ComponentType.getBitsFor();
    private static final Bits exclude = ComponentType.getBitsFor();
    private static final Family family = Family.getFor(all, one, exclude);

    private static final int BUFFERSIZE = 20;

    private final Array<Entity> possibleCollitions;
    private final Array<Entity> movingEntities;

    private Vector2[] tmpCorners;

    private SpatialHashGrid spatialHashGrid;

    private ComponentMapper<BoundsComponent> bm;
    private ComponentMapper<MovementComponent> mm;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<StateComponent> sm;

    public CollisionSystem(int priority) {
        super(family, priority);

        possibleCollitions = new Array<Entity>(BUFFERSIZE);
        movingEntities = new Array<Entity>(BUFFERSIZE);

        tmpCorners = new Vector2[4];

        bm = ComponentMapper.getFor(BoundsComponent.class);
        mm = ComponentMapper.getFor(MovementComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        possibleCollitions.clear();

        for (Entity entity : movingEntities) {
            possibleCollitions.addAll(getPossibleCollisions(entity));
            resolveCollisions(entity, possibleCollitions);
        }

        spatialHashGrid.clearDynamic();
        movingEntities.clear();
    }

    private void resolveCollisions(Entity entity, Array<Entity> possibleCollisions) {
        BoundsComponent entityBounds = bm.get(entity);
        StateComponent sc = sm.get(entity);

        for (Entity collision : possibleCollisions) {
            BoundsComponent colliderBounds = bm.get(collision);

            if (entityBounds.nextBounds.intersects(colliderBounds.nextBounds)) {
                MovementComponent mc = mm.get(entity);
                TransformComponent tcEntity = tm.get(entity);
                TransformComponent tcCollider = tm.get(collision);

                tmpCorners = entityBounds.nextBounds.getCorners();

                mc.velocity.y = 0;
                mc.accel.y = 0;
                tcEntity.nextPos.y = entityBounds.nextBounds.getDimensions().y / 2 + colliderBounds.nextBounds.max.y + 1;
            }
        }
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        MovementComponent mc = mm.get(entity);
        spatialHashGrid.addDynamic(entity);

        if (mc != null && !mc.velocity.isZero()) {
            movingEntities.add(entity);
        }
    }

    private Array<Entity> getPossibleCollisions(Entity entity) {
        return spatialHashGrid.getPotentialColliders(entity);
    }

    public void setSpatialHashGrid(SpatialHashGrid spatialHashGrid) {
        this.spatialHashGrid = spatialHashGrid;
    }
}
