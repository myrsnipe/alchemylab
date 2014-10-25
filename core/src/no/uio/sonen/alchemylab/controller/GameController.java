package no.uio.sonen.alchemylab.controller;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

public class GameController extends InputMultiplexer {
    public GameController(InputProcessor... processors) {
        super(processors);
    }
}
