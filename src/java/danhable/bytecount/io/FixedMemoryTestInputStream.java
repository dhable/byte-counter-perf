package danhable.bytecount.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * InputStream fixture for the tests that uses a consistent amount of memory to generate
 * any length of stream.
 */
public class FixedMemoryTestInputStream extends InputStream {
    private volatile long remaining;

    public FixedMemoryTestInputStream(final long size) {
        this.remaining = size;
    }

    @Override
    public int read() throws IOException {
        if (remaining > 0) {
            --remaining;
            return (byte) 0x42;
        } else {
            return -1;
        }
    }
}
