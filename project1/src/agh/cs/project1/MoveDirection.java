package agh.cs.project1;

public enum MoveDirection {
    FORWARD,
    BACKWARD,
    RIGHT,
    LEFT;

    @Override
    public String toString() {
        return switch (this) {
            case FORWARD -> "F";
            case BACKWARD -> "B";
            case RIGHT -> "R";
            case LEFT -> "L";
        };
    }
}
