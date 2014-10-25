package no.uio.sonen.alchemylab;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import no.uio.sonen.alchemylab.engine.components.BoundsComponent;

import java.util.ArrayList;
import java.util.List;

public class SpatialHashGrid {
    private static final int DEFAULT_BUFFER_SIZE = 32;

    private final ComponentMapper<BoundsComponent> bm;

    private final List<Entity>[] dynamicBucket;
    private final List<Entity>[] staticBucket;

    private final int cellsPerRow;
    private final int cellsPerCol;
    private final float cellSize;

    /** tmp store cell id's of an entity */
    private int[] cellIds = new int[4];

    /** tmp store return values for getPotentialColliders */
    private final Array<Entity> foundEntities;

    private final Vector2 tmp;

    public static int possibleColliders = 0;

    @SuppressWarnings("unchecked")
    public SpatialHashGrid(float worldWidth, float worldHeight, float cellSize) {
        this.cellSize = cellSize;
        cellsPerRow = MathUtils.ceil(worldWidth / cellSize);
        cellsPerCol = MathUtils.ceil(worldHeight / cellSize);

        int numCells = cellsPerRow * cellsPerCol;

        dynamicBucket = new List[numCells];
        staticBucket = new List[numCells];

        for (int i = 0; i < numCells; i++) {
            dynamicBucket[i] = new ArrayList<Entity>(DEFAULT_BUFFER_SIZE);
            staticBucket[i] = new ArrayList<Entity>(DEFAULT_BUFFER_SIZE);
        }

        foundEntities = new Array<Entity>(DEFAULT_BUFFER_SIZE);

        bm = ComponentMapper.getFor(BoundsComponent.class);

        tmp = new Vector2();
    }

    public void addDynamic(Entity entity) {
        cellIds = getCellIds(entity);
        int i = 0;
        int cellId;

        while (i <= 3 && (cellId = cellIds[i++]) != -1) {
            dynamicBucket[cellId].add(entity);
        }
    }

    public void addStatic(Entity entity) {
        cellIds = getCellIds(entity);
        int i = 0;
        int cellId;

        while (i <= 3 && (cellId = cellIds[i++]) != -1) {
            staticBucket[cellId].add(entity);
        }
    }

    public void clearDynamic() {
        int len = dynamicBucket.length;
        for (int i = 0; i < len; i++) {
            dynamicBucket[i].clear();
        }
    }

    public void clearStatic() {
        int len = staticBucket.length;
        for (int i = 0; i < len; i++) {
            staticBucket[i].clear();
        }
    }

    public Array<Entity> getPotentialColliders(Entity entity) {
        foundEntities.clear();

        cellIds = getCellIds(entity);

        int i = 0;
        int cellId;
        int len;
        Entity collider;

        while (i <= 3 && (cellId = cellIds[i++]) != -1) {
            len = dynamicBucket[cellId].size();
            for (int j = 0; j < len; j++) {
                collider = dynamicBucket[cellId].get(j);

                if (entity != collider && !foundEntities.contains(collider, true)) {
                    foundEntities.add(collider);
                }
            }
        }

        i = 0;

        while (i <= 3 && (cellId = cellIds[i++]) != -1) {
            len = staticBucket[cellId].size();
            for (int j = 0; j < len; j++) {
                collider = staticBucket[cellId].get(j);

                if (entity != collider && !foundEntities.contains(collider, true)) {
                    foundEntities.add(collider);
                }
            }
        }

        possibleColliders = foundEntities.size;
        return foundEntities;
    }

    private int[] getCellIds(Entity entity) {
        BoundsComponent bound = bm.get(entity);

        tmp.set(bound.nextBounds.getMin());
        int x1 = MathUtils.floor(tmp.x / cellSize);
        int y1 = MathUtils.floor(tmp.y / cellSize);

        tmp.set(bound.nextBounds.getMax());
        int x2 = MathUtils.floor(tmp.x / cellSize);
        int y2 = MathUtils.floor(tmp.y / cellSize);

        int i;

        if (x1 == x2 && y1 == y2) {
            if (x1 >= 0 && x1 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol) {
                cellIds[0] = x1 + y1 * cellsPerRow;
            } else {
                cellIds[0] = -1;
            }

            cellIds[1] = -1;
            cellIds[2] = -1;
            cellIds[3] = -1;
        } else if (x1 == x2) {
            i = 0;

            if (x1 >= 0 && x1 < cellsPerRow) {
                if (y1 >= 0 && y1 < cellsPerCol) {
                    cellIds[i++] = x1 + y1 * cellsPerRow;
                }

                if (y2 >= 0 && y2 < cellsPerCol) {
                    cellIds[i++] = x1 + y2 * cellsPerRow;
                }

                while (i <= 3) {
                    cellIds[i++] = -1;
                }
            }
        } else if (y1 == y2) {
            i = 0;

            if (y1 >= 0 && y1 < cellsPerCol) {
                if (x1 >= 0 && x1 < cellsPerRow) {
                    cellIds[i++] = x1 + y1 * cellsPerRow;
                }

                if (x2 >= 0 && x2 < cellsPerRow) {
                    cellIds[i++] = x1 + y1 * cellsPerRow;
                }

                while (i <= 3) {
                    cellIds[i++] = -1;
                }
            }
        } else {
            i = 0;

            int y1CellsPerRow = y1 * cellsPerRow;
            int y2CellsPerRow = y2 * cellsPerRow;

            if (x1 >= 0 && x1 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol) {
                cellIds[i++] = x1 + y1CellsPerRow;
            }

            if (x2 >= 0 && x2 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol) {
                cellIds[i++] = x2 + y1CellsPerRow;
            }

            if (x2 >= 0 && x2 < cellsPerRow && y2 >= 0 && y2 < cellsPerCol) {
                cellIds[i++] = x2 + y2CellsPerRow;
            }

            if (x1 >= 0 && x1 < cellsPerRow && y2 >= 0 && y2 < cellsPerCol) {
                cellIds[i++] = x1 + y2CellsPerRow;
            }

            while (i <= 3) {
                cellIds[i++] = -1;
            }
        }

        return cellIds;
    }
}
