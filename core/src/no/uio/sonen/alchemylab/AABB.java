package no.uio.sonen.alchemylab;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.io.Serializable;
import java.util.List;

public class AABB implements Serializable {
    private static final long serialVersionUID = -1286954817115294624L;

    final Vector2 corners[] = new Vector2[4];
    public final Vector2 min = new Vector2();
    public final Vector2 max = new Vector2();
    private final Vector2 center = new Vector2();
    public final Vector2 dimensions = new Vector2();
    private boolean center_dirty = true;
    private boolean corners_dirty = true;

    /** @return the center of the bounding box */
    public Vector2 getCenter () {
        if (center_dirty) {
            updateCenter();
        }

        return center;
    }

    private void updateCenter() {
        if (center_dirty) {
            center.set(min).add(max).scl(0.5f);
            center_dirty = false;
        }
    }


    private void updateCorners () {
        if (corners_dirty) {
            corners[0].set(min.x, min.y);
            corners[1].set(max.x, min.y);
            corners[2].set(max.x, max.y);
            corners[3].set(min.x, max.y);

            corners_dirty = false;
        }
    }

    /** @return the corners of this bounding box */
    public Vector2[] getCorners () {
        if (corners_dirty) {
            updateCorners();
        }
        return corners;
    }

    /** @return The dimensions of this bounding box on both axis */
    public Vector2 getDimensions () {
        return dimensions;
    }

    /** @return The minimum vector */
    public Vector2 getMin () {
        return min;
    }

    /** @return The maximum vector */
    public synchronized Vector2 getMax () {
        return max;
    }

    /** Constructs a new bounding box with the minimum and maximum vector set to zeros. */
    public AABB() {
        corners_dirty = true;
        corners_dirty = true;
        for (int l_idx = 0; l_idx < 4; l_idx++)
            corners[l_idx] = new Vector2();
        clr();
    }

    /** Constructs a new bounding box from the given bounding box.
     *
     * @param bounds The bounding box to copy */
    public AABB(AABB bounds) {
        corners_dirty = true;
        corners_dirty = true;
        for (int l_idx = 0; l_idx < 4; l_idx++)
            corners[l_idx] = new Vector2();
        this.set(bounds);
    }

    /** Constructs the new bounding box using the given minimum and maximum vector.
     *
     * @param minimum The minimum vector
     * @param maximum The maximum vector */
    public AABB(Vector2 minimum, Vector2 maximum) {
        corners_dirty = true;
        corners_dirty = true;
        for (int l_idx = 0; l_idx < 4; l_idx++)
            corners[l_idx] = new Vector2();
        this.set(minimum, maximum);
    }

    /** Sets the given bounding box.
     *
     * @param bounds The bounds.
     * @return This bounding box for chaining. */
    public AABB set(AABB bounds) {
        corners_dirty = true;
        center_dirty = true;
        return this.set(bounds.min, bounds.max);
    }

    /** Sets the given minimum and maximum vector.
     *
     * @param minimum The minimum vector
     * @param maximum The maximum vector
     * @return This bounding box for chaining. */
    public AABB set(Vector2 minimum, Vector2 maximum) {
        min.set(minimum.x < maximum.x ? minimum.x : maximum.x, minimum.y < maximum.y ? minimum.y : maximum.y);
        max.set(minimum.x > maximum.x ? minimum.x : maximum.x, minimum.y > maximum.y ? minimum.y : maximum.y);
        dimensions.set(max).sub(min);
        corners_dirty = true;
        center_dirty = true;
        return this;
    }

    /** Sets the bounding box minimum and maximum vector from the given points.
     *
     * @param points The points.
     * @return This bounding box for chaining. */
    public AABB set(Vector2[] points) {
        this.inf();
        for (Vector2 l_point : points)
            this.ext(l_point);
        corners_dirty = true;
        center_dirty = true;
        return this;
    }

    /** Sets the bounding box minimum and maximum vector from the given points.
     *
     * @param points The points.
     * @return This bounding box for chaining. */
    public AABB set(List<Vector2> points) {
        this.inf();
        for (Vector2 l_point : points)
            this.ext(l_point);
        corners_dirty = true;
        center_dirty = true;
        return this;
    }

    public AABB setCenter(float x, float y, float width, float height) {
        min.set(x - width / 2, y - height / 2);
        max.set(x + width / 2, y + height / 2);

        dimensions.set(width, height);
        corners_dirty = true;
        center_dirty = true;

        return this;
    }

    public AABB setCenter(Vector2 pos, float width, float height) {
        min.set(pos.x - width / 2, pos.y - height / 2);
        max.set(pos.x + width / 2, pos.y + height / 2);

        dimensions.set(width, height);
        corners_dirty = true;
        center_dirty = true;

        return this;
    }

    public AABB setCenter(Vector2 pos, Vector2 dimensions) {
        min.set(pos.x - dimensions.x / 2, pos.y - dimensions.y / 2);
        max.set(pos.x + dimensions.x / 2, pos.y + dimensions.y / 2);

        this.dimensions.set(dimensions.x, dimensions.y);
        corners_dirty = true;
        center_dirty = true;

        return this;
    }

    public AABB setCenter(Vector2 pos) {
        min.set(pos.x - dimensions.x / 2, pos.y - dimensions.y / 2);
        max.set(pos.x + dimensions.x / 2, pos.y + dimensions.y / 2);

        corners_dirty = true;
        center_dirty = true;

        return this;
    }

    /** Sets the minimum and maximum vector to positive and negative infinity.
     *
     * @return This bounding box for chaining. */
    public AABB inf() {
        min.set(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
        max.set(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
        center.set(0, 0);
        dimensions.set(0, 0);
        corners_dirty = true;
        center_dirty = false;

        return this;
    }

    /** Extends the bounding box to incorporate the given {@link Vector3}.
     *
     * @param point The vector
     * @return This bounding box for chaining. */
    public AABB ext(Vector2 point) {
        corners_dirty = true;
        center_dirty = true;

        return this.set(min.set(min(min.x, point.x), min(min.y, point.y)),
                max.set(Math.max(max.x, point.x), Math.max(max.y, point.y)));
    }

    /** Sets the minimum and maximum vector to zeros
     *
     * @return This bounding box for chaining. */
    public AABB clr() {
        corners_dirty = true;
        center_dirty = true;

        return this.set(min.set(0, 0), max.set(0, 0));
    }

    /** Returns whether this bounding box is valid. This means that min != max and min < max.
     *
     * @return True in case the bounding box is valid, false otherwise */
    public boolean isValid() {
        return min.x < max.x && min.y < max.y;
    }

    /** Extends this bounding box by the given bounding box.
     *
     * @param a_bounds The bounding box
     * @return This bounding box for chaining. */
    public AABB ext(AABB a_bounds) {
        corners_dirty = true;
        center_dirty = true;

        return this.set(min.set(min(min.x, a_bounds.min.x), min(min.y, a_bounds.min.y)),
                max.set(max(max.x, a_bounds.max.x), max(max.y, a_bounds.max.y)));
    }

    /** Extends this bounding box by the given transformed bounding box.
     *
     * @param bounds The bounding box
     * @param transform The transformation matrix to apply to bounds, before using it to extend this bounding box.
     * @return This bounding box for chaining. */
    public AABB ext(AABB bounds, Matrix3 transform) {
        bounds.updateCorners();
        for (Vector2 pnt : corners) {
            pnt.mul(transform);
            min.set(min(min.x, pnt.x), min(min.y, pnt.y));
            max.set(max(max.x, pnt.x), max(max.y, pnt.y));
        }
        corners_dirty = true;
        center_dirty = true;
        bounds.corners_dirty = true;
        return this.set(min, max);
    }

    /** Multiplies the bounding box by the given matrix. This is achieved by multiplying the 4 corner points and then calculating
     * the minimum and maximum vectors from the transformed points.
     *
     * @param matrix The matrix
     * @return This bounding box for chaining. */
    public AABB mul(Matrix3 matrix) {
        updateCorners();
        this.inf();
        for (Vector2 l_pnt : corners) {
            l_pnt.mul(matrix);
            min.set(min(min.x, l_pnt.x), min(min.y, l_pnt.y));
            max.set(max(max.x, l_pnt.x), max(max.y, l_pnt.y));
        }
        corners_dirty = true;
        center_dirty = true;

        return this.set(min, max);
    }

    /** Returns whether the given bounding box is contained in this bounding box.
     * @param b The bounding box
     * @return Whether the given bounding box is contained */
    public boolean contains(AABB b) {
        return !isValid()
                || (min.x <= b.min.x && min.y <= b.min.y && max.x >= b.max.x && max.y >= b.max.y);
    }

    /** Returns whether the given bounding box is intersecting this bounding box (at least one point in).
     * @param b The bounding box
     * @return Whether the given bounding box is intersected */
    public boolean intersects(AABB b) {
        if (!isValid()) return false;

        // test using SAT (separating axis theorem)

        float lx = Math.abs(this.center.x - b.center.x);
        float sumx = (this.dimensions.x / 2.0f) + (b.dimensions.x / 2.0f);

        float ly = Math.abs(this.center.y - b.center.y);
        float sumy = (this.dimensions.y / 2.0f) + (b.dimensions.y / 2.0f);

        return (lx <= sumx && ly <= sumy);
    }

    /** Returns whether the given vector is contained in this bounding box.
     * @param v The vector
     * @return Whether the vector is contained or not. */
    public boolean contains(Vector2 v) {
        return min.x <= v.x && max.x >= v.x && min.y <= v.y && max.y >= v.y;
    }

    @Override
    public String toString () {
        return "[" + min + "|" + max + "]";
    }

    /** Extends the bounding box by the given vector.
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return This bounding box for chaining. */
    public AABB ext(float x, float y) {
        corners_dirty = true;
        center_dirty = true;

        return this.set(min.set(min(min.x, x), min(min.y, y)), max.set(max(max.x, x), max(max.y, y)));
    }

    static float min (float a, float b) {
        return a > b ? b : a;
    }

    static float max (float a, float b) {
        return a > b ? a : b;
    }
}
