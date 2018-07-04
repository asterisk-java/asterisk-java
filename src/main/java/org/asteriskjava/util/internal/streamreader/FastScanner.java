package org.asteriskjava.util.internal.streamreader;

import java.io.Closeable;
import java.io.IOException;

public interface FastScanner extends Closeable
{
    public String next() throws IOException;

}
