package no.uio.sonen.alchemylab.engine.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.ComponentType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Bits;
import no.uio.sonen.alchemylab.JumpState;
import no.uio.sonen.alchemylab.controller.DirectionX;
import no.uio.sonen.alchemylab.controller.PlayerController;
import no.uio.sonen.alchemylab.engine.components.MovementComponent;
import no.uio.sonen.alchemylab.engine.components.PlayerComponent;

public class PlayerSystem extends IteratingSystem {
    private static final Bits all = ComponentType.getBitsFor(PlayerComponent.class);
    private static final Bits one = ComponentType.getBitsFor();
    private static final Bits exclude = ComponentType.getBitsFor();
    private static final Family family = Family.getFor(all, one, exclude);

    private static final float PLAYER_WALK_VELOCITY = 32;
    private static final float PLAYER_JUMP_IMPULSE = 70;

    private final PlayerController controller;
    private final ComponentMapper<MovementComponent> mm;
    private final ComponentMapper<PlayerComponent> pm;

    public PlayerSystem(PlayerController controller, int priority) {
        super(family, priority);
        
        this.controller = controller;

        mm = ComponentMapper.getFor(MovementComponent.class);
        pm = ComponentMapper.getFor(PlayerComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        MovementComponent mc = mm.get(entity);
        PlayerComponent pc = pm.get(entity);

        if (controller.directionX == DirectionX.LEFT) {
            mc.velocity.x = -PLAYER_WALK_VELOCITY;
        } else if (controller.directionX == DirectionX.RIGHT) {
            mc.velocity.x = PLAYER_WALK_VELOCITY;
        } else if (controller.directionX == DirectionX.STILL)  {
            mc.velocity.x = 0;
        }

        if (controller.jumping && pc.jump == JumpState.STILL) {
            mc.velocity.y = PLAYER_JUMP_IMPULSE;
            pc.jump = JumpState.JUMPING;
        } else if (!controller.jumping && pc.jump == JumpState.JUMPING) {
            pc.jump = JumpState.FALLING;
        }
    }
}
