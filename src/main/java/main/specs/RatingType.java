package main.specs;

public enum RatingType {
    LIKE(1),
    DISLIKE(-1),
    NONE(0);

    public static RatingType fromValue(int rawValue) {
        if (rawValue == 1) return LIKE;
        if (rawValue == -1) return DISLIKE;
        if (rawValue == 0) return NONE;
        return NONE;
    }

    public final int rawValue;

    RatingType(int rawValue) {
        this.rawValue = rawValue;
    }
}
