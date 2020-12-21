package agh.cs.project1;

public enum MapDirection {
    NORTH,
    SOUTH,
    WEST,
    EAST,
    NORTHWEST,
    SOUTHWEST,
    NORTHEAST,
    SOUTHEAST;

    @Override
    public String toString() {
        return switch (this) {
            case NORTH -> "↑";
            case SOUTH -> "↓";
            case WEST -> "←";
            case EAST -> "→";
            case NORTHWEST -> "↖";
            case SOUTHWEST -> "↙";
            case NORTHEAST -> "↗";
            case SOUTHEAST -> "↘";
        };
    }

    public MapDirection next() {
        return switch (this) {
            case NORTH -> NORTHEAST;
            case SOUTH -> SOUTHWEST;
            case WEST -> NORTHWEST;
            case EAST -> SOUTHEAST;
            case NORTHWEST -> NORTH;
            case SOUTHWEST -> WEST;
            case NORTHEAST -> EAST;
            case SOUTHEAST -> SOUTH;
        };
    }

    public MapDirection previous() {
        return switch (this) {
            case NORTH -> NORTHWEST;
            case SOUTH -> SOUTHEAST;
            case WEST -> SOUTHWEST;
            case EAST -> NORTHEAST;
            case NORTHWEST -> WEST;
            case SOUTHWEST -> SOUTH;
            case NORTHEAST -> NORTH;
            case SOUTHEAST -> EAST;
        };
    }

    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> new Vector2d(0, 1);
            case SOUTH -> new Vector2d(0, -1);
            case WEST -> new Vector2d(-1, 0);
            case EAST -> new Vector2d(1, 0);
            case NORTHWEST -> new Vector2d(-1, 1);
            case SOUTHWEST -> new Vector2d(-1, -1);
            case NORTHEAST -> new Vector2d(1, 1);
            case SOUTHEAST -> new Vector2d(1, -1);
        };
    }
}
