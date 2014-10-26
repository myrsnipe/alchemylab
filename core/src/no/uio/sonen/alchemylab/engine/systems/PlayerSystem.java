package no.uio.sonen.alchemylab.engine.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.ComponentType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Bits;
import no.uio.sonen.alchemylab.Constants;
import no.uio.sonen.alchemylab.JumpState;
import no.uio.sonen.alchemylab.controller.DirectionX;
import no.uio.sonen.alchemylab.controller.PlayerController;
import no.uio.sonen.alchemylab.engine.components.MovementComponent;
import no.uio.sonen.alchemylab.engine.components.PlayerComponent;
import no.uio.sonen.alchemylab.engine.components.StateComponent;

public class PlayerSystem extends IteratingSystem {
    private static final Bits all = ComponentType.getBitsFor(PlayerComponent.class, StateComponent.class);
    private static final Bits one = ComponentType.getBitsFor();
    private static final Bits exclude = ComponentType.getBitsFor();
    private static final Family family = Family.getFor(all, one, exclude);;

    private final PlayerController controller;
    private final ComponentMapper<MovementComponent> mm;
    private final ComponentMapper<PlayerComponent> pm;
    private final ComponentMapper<StateComponent> sm;

    public PlayerSystem(PlayerController controller, int priority) {
        super(family, priority);
        
        this.controller = controller;

        mm = ComponentMapper.getFor(MovementComponent.class);
        pm = ComponentMapper.getFor(PlayerComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        MovementComponent mc = mm.get(entity);
        StateComponent sc = sm.get(entity);

        if (controller.directionX == DirectionX.LEFT) {
            mc.velocity.x = -Constants.PLAYER_WALK_VELOCITY;
        } else if (controller.directionX == DirectionX.RIGHT) {
            mc.velocity.x = Constants.PLAYER_WALK_VELOCITY;
        } else if (controller.directionX == DirectionX.STILL) {
            mc.velocity.x = 0;
        }

        if (controller.jumping && sc.jump == JumpState.STILL) {
            mc.velocity.y += Constants.PLAYER_JUMP_IMPULSE_INITIAL;
            sc.jump = JumpState.JUMPING;
        } else if (sc.jumpTicks > 0 && controller.jumping && sc.jump == JumpState.JUMPING) {
            mc.velocity.y += Constants.PLAYER_JUMP_IMPULSE_TICK ;
            sc.jumpTicks--;
        } else if (!controller.jumping && sc.jump == JumpState.JUMPING) {
            sc.jump = JumpState.FALLING;
        }
    }
}
