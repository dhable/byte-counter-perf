package danhable.bytecount.util;

public class SimpleLongCounter {
    private long value;

    public SimpleLongCounter(final long initialValue) {
        value = initialValue;
    }

    public void increment(final long delta) {
        value += delta;
    }

    public long getCount() {
        return value;
    }
}
