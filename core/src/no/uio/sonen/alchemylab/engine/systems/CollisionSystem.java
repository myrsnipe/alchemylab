package no.uio.sonen.alchemylab.engine.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.ComponentType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Bits;
import no.uio.sonen.alchemylab.Constants;
import no.uio.sonen.alchemylab.JumpState;
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

    private SpatialHashGrid spatialHashGrid;

    private ComponentMapper<BoundsComponent> bm;
    private ComponentMapper<MovementComponent> mm;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<StateComponent> sm;

    // tmp stuff
    private BoundsComponent ba;
    private BoundsComponent bb;
    private final Vector2 aMin;
    private final Vector2 aMax;
    private final Vector2 bMin;
    private final Vector2 bMax;
    private final Vector2 aNormVel;

    public CollisionSystem(int priority) {
        super(family, priority);

        possibleCollitions = new Array<Entity>(BUFFERSIZE);
        movingEntities = new Array<Entity>(BUFFERSIZE);

        bm = ComponentMapper.getFor(BoundsComponent.class);
        mm = ComponentMapper.getFor(MovementComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);

        aMin = new Vector2();
        aMax = new Vector2();
        bMin = new Vector2();
        bMax = new Vector2();
        aNormVel = new Vector2();
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

    private void resolveCollisions(Entity a, Array<Entity> possibleCollisions) {
        Constants.possibleCollisions += possibleCollisions.size;
        boolean checkX = true;
        boolean checkY = true;

        for (Entity b : possibleCollisions) {
            ba = bm.get(a);
            bb = bm.get(b);

            aMin.set(ba.nextBounds.getMin());
            aMax.set(ba.nextBounds.getMax());

            bMin.set(bb.nextBounds.getMin());
            bMax.set(bb.nextBounds.getMax());

            MovementComponent mc = mm.get(a);

            if (checkY && ((aMin.x <= bMax.x && aMin.x >= bMin.x) || (aMax.x >= bMin.x && aMax.x <= bMax.x))) {
                if (mc.velocity.y < 0) {
                    if (aMin.y < bMax.y && aMin.y > bMin.y) {
                        fixCollisionFloor(a, b);
                        checkY = false;
                    }
                } else if (mc.velocity.y > 0) {
                    if (aMax.y > bMin.y && aMax.y < bMax.y) {
                        fixCollisionCeil(a, b);
                        checkY = false;
                    }
                }
            }

            if (checkX && ((aMin.y + 8 <= bMax.y))) {
                if (mc.velocity.x < 0) {
                    if (aMin.x < bMax.x && aMin.x > bMin.x) {
                        fixCollisionRightSide(a, b);
                        checkX = false;
                    }
                } else if (mc.velocity.x > 0) {
                    if (aMax.x > bMin.x && aMax.x < bMax.x) {
                        fixCollisionLeftSide(a, b);
                        checkX = false;
                    }
                }
            }

            Constants.collisions++;
        }
    }

    private void fixCollisionLeftSide(Entity a, Entity b) {
        TransformComponent tc = tm.get(a);
        MovementComponent mc = mm.get(a);
        BoundsComponent bc = bm.get(a);

        float width = ba.nextBounds.dimensions.x;

        if (mc.accel.x > 0) {
            mc.accel.y = 0;
        }

        mc.velocity.x = 0;
        tc.nextPos.set((bb.nextBounds.min.x - 1) - (width / 2), tc.nextPos.y);
        bc.nextBounds.setCenter(tc.nextPos);
    }

    private void fixCollisionRightSide(Entity a, Entity b) {
        TransformComponent tc = tm.get(a);
        MovementComponent mc = mm.get(a);
        BoundsComponent bc = bm.get(a);

        float width = ba.nextBounds.dimensions.x;

        if (mc.accel.x < 0) {
            mc.accel.y = 0;
        }

        mc.velocity.x = 0;
        tc.nextPos.set((bb.nextBounds.max.x + 1) + (width / 2), tc.nextPos.y);
        bc.nextBounds.setCenter(tc.nextPos);
    }

    private void fixCollisionCeil(Entity a, Entity b) {
        TransformComponent tc = tm.get(a);
        MovementComponent mc = mm.get(a);
        StateComponent sc = sm.get(a);
        BoundsComponent bc = bm.get(a);

        float height = ba.nextBounds.dimensions.y;

        if (mc.accel.y > 0) {
            mc.accel.y = 0;
        }

        mc.velocity.y = 0;

        tc.nextPos.set(tc.nextPos.x, (bb.nextBounds.min.y - 1) - (height / 2) - 8);
        bc.nextBounds.setCenter(tc.nextPos);

        if (sc != null) {
            sc.jump = JumpState.FALLING;
        }
    }

    private void fixCollisionFloor(Entity a, Entity b) {
        TransformComponent tc = tm.get(a);
        MovementComponent mc = mm.get(a);
        StateComponent sc = sm.get(a);
        BoundsComponent bc = bm.get(a);

        float height = ba.nextBounds.dimensions.y;

        if (mc.accel.y < 0) {
            mc.accel.y = 0;
        }

        mc.velocity.y = 0;

        tc.nextPos.set(tc.nextPos.x, (bb.nextBounds.max.y + 1) + (height / 2) - 8);
        bc.nextBounds.setCenter(tc.nextPos);

        if (sc != null) {
            sc.jump = JumpState.STILL;
            sc.jumpTicks = Constants.PLAYER_JUMP_MAX_TICKS;
        }
    }

    /**
     * Fixes the collision
     * @param e1 : this gets pushed out
     * @param e2 : this stays where it is
     */
    private void fixCollision(Entity e1, Entity e2) {
        // http://stackoverflow.com/questions/8515198/basic-aabb-collision-using-projection-vector

        // aNormVel
        aNormVel.set(ba.nextBounds.getCenter());
        aNormVel.sub(bb.nextBounds.getCenter());
        normalize(aNormVel);
        float angle = aNormVel.angle();

        TransformComponent tc = tm.get(e1);
        MovementComponent mc = mm.get(e1);
        StateComponent sc = sm.get(e1);

        float height = ba.nextBounds.dimensions.y;

        if (angle >= 315 || angle < 45) {
            Gdx.app.log("collision", "ground: " + angle);

            if (mc.accel.y < 0) {
                mc.accel.y = 0;
            }

            if (mc.velocity.y < 0) {
                mc.velocity.y = 0;

                tc.nextPos.set(tc.nextPos.x, (bb.nextBounds.max.y + 1) + (height / 2) - 8);

                if (sc != null) {
                    sc.jump = JumpState.STILL;
                    sc.jumpTicks = Constants.PLAYER_JUMP_MAX_TICKS;
                }
            }
        } else if (angle >= 135 || angle < 225) {
            Gdx.app.log("collision", "underside: " + angle);

            if (mc.accel.y >= 0) {
                mc.accel.y = 0;
            }

            if (mc.velocity.y >= 0) {
                mc.velocity.y = 0;

                tc.nextPos.set(tc.nextPos.x, (bb.nextBounds.min.y - 1) - (height / 2) + 8);

                if (sc != null) {
                    sc.jump = JumpState.FALLING;
                }
            }
        } else if (angle >= 225 || angle < 315) {
            Gdx.app.log("collision", "leftside: " + angle);

        } else if (angle >= 0 || angle < 135) {
            Gdx.app.log("collision", "rightside: " + angle);
        }
    }

    private Vector2 normalize(Vector2 v) {
        float length = (float) Math.sqrt(v.x * v.x + v.y * v.y);

        if (length != 0) {
            v.x = v.x / length;
            v.y = v.y / length;
        }

        return v;
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
