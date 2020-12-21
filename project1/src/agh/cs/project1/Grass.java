package agh.cs.project1;

import java.awt.*;

public class Grass implements IMapElement {
    private final Vector2d position;

    public Grass(Vector2d position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "*";
    }

    public Color toColor() {
        return new Color(61, 113, 68);
    }

    public Vector2d getPosition() {
        return position;
    }
}
