package com.norddev.downloadmanager;

import java.io.Closeable;
import java.io.IOException;

public final class Util {

    private Util() {
    }

    public static void closeQuietly(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException ignored) {
            }
        }
    }
}
