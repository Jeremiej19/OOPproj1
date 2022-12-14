package agh.oop;

public enum MapDirection {
    NORTH(0),
    NORTH_EAST(1),
    EAST(2),
    SOUTH_EAST(3),
    SOUTH(4),
    SOUTH_WEST(5),
    WEST(6),
    NORTH_WEST(7);

    final int value;

    MapDirection(int a) {
        value = a;
    }

    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> new Vector2d(0, 1);
            case NORTH_EAST -> new Vector2d(1, 1);
            case EAST -> new Vector2d(1, 0);
            case SOUTH_EAST -> new Vector2d(1, -1);
            case SOUTH -> new Vector2d(0, -1);
            case SOUTH_WEST -> new Vector2d(-1, -1);
            case WEST -> new Vector2d(-1, 0);
            case NORTH_WEST -> new Vector2d(-1, 1);
        };
    }
    public int toDirection(){
        return value;
    }
    public static MapDirection fromNumber(int x) {
        for (MapDirection d : MapDirection.values()) {
            if (d.value == x) {
                return d;
            }
        }
        throw new IndexOutOfBoundsException(x + " is not a vaild MapDirection");
    }

    @Override
    public String toString() {
        return switch (this.value) {
            case 0 -> "dol";
            case 1 -> "prawo dol";
            case 2 -> "prawo";
            case 3 -> "prawo gora";
            case 4 -> "gora";
            case 5 -> "lewo gora";
            case 6 -> "lewo";
            case 7 -> "lewo dol";
            default -> throw new IllegalStateException("Unexpected value: " + this.value);
        };
    }
}
