package main.utils;

public class Range {
    private Integer offset = 0;
    private Integer count = 0;

    public Range(Integer offset, Integer count) {
        this.offset = offset;
        this.count = count;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
