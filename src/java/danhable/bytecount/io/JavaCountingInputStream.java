package danhable.bytecount.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A brain dead simple InputStream filter that counts every time it returns
 * a byte. Provides a means of fetching that byte later on.
 */
public class JavaCountingInputStream extends FilterInputStream {
    private long count;

    public JavaCountingInputStream(InputStream in) {
        super(in);
        this.count = 0;
    }

    @Override
    public int read() throws IOException {
        final int value = super.read();
        if (value > -1) {
            count++;
        }
        return value;
    }

    public long getCount() {
        return count;
    }
}
