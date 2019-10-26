package main.specs;

public enum RatingType {
    LIKE(0),
    NONE(1),
    DISLIKE(2);

    public static RatingType fromValue(int rawValue) {
        if (rawValue == 0) return LIKE;
        if (rawValue == 1) return NONE;
        if (rawValue == 2) return DISLIKE;
        return NONE;
    }

    public final int rawValue;

    RatingType(int rawValue) {
        this.rawValue = rawValue;
    }
}
