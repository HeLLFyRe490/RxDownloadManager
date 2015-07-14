package com.norddev.downloadmanager;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Util {

    private static final Pattern RANGE_HEADER = Pattern.compile("^bytes=(\\d+)-(\\d+)?$");
    private static final Pattern CONTENT_RANGE_HEADER = Pattern.compile("^bytes (\\d+)-(\\d+)/(\\d+)$");

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

    /**
     * Formats the number of bytes as a human-readable file size string
     *
     * @param bytes number of bytes
     * @return the file size as a human-readable string
     */
    public static String formatFileSize(long bytes) {
        if (bytes < 0) {
            return "Unknown";
        }
        float result = bytes;
        String suffix = "B";
        if (result > 900) {
            suffix = "KB";
            result = result / 1024;
        }
        if (result > 900) {
            suffix = "MB";
            result = result / 1024;
        }
        if (result > 900) {
            suffix = "GB";
            result = result / 1024;
        }
        if (result > 900) {
            suffix = "TB";
            result = result / 1024;
        }
        if (result > 900) {
            suffix = "PB";
            result = result / 1024;
        }
        return String.format(Locale.US, "%.2f %s", result, suffix);
    }

    /**
     * Deletes directory and all files and subdirectories
     *
     * @param dir directory
     * @return True if deletion was successful
     */
    public static boolean deleteDirectory(File dir) {
        return deleteDirectory(dir, true);
    }

    /**
     * Deletes directory and all files and subdirectories
     *
     * @param dir directory
     * @return True if deletion was successful
     */
    public static boolean deleteDirectoryContents(File dir) {
        return deleteDirectory(dir, false);
    }

    private static boolean deleteDirectory(File dir, boolean includeDir) {
        if (dir != null) {
            if (dir.isDirectory()) {
                File[] dirContents = dir.listFiles();
                for (File dirFile : dirContents) {
                    if (!deleteDirectory(dirFile, true)) {
                        return false;
                    }
                }
            }
            if (includeDir) {
                return dir.delete();
            }
            return true;
        }
        return false;
    }

    public static String buildRange(long startByte, long requestedLength) {
        long endByte = startByte + requestedLength - 1;
        String range;
        if (requestedLength == C.UNKNOWN) {
            range = String.format("bytes=%d-", startByte);
        } else {
            range = String.format("bytes=%d-%d", startByte, endByte);
        }
        return range;
    }

    public static long[] parseRange(String range) {
        if (range != null) {
            Matcher matcher = RANGE_HEADER.matcher(range);
            if (matcher.find()) {
                try {
                    return new long[]{
                            Long.parseLong(matcher.group(1)),
                            matcher.group(2) == null ? C.UNKNOWN : Long.parseLong(matcher.group(2))
                    };
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return null;
    }

    public static long[] parseContentRange(String contentRange) {
        if (contentRange != null) {
            Matcher matcher = CONTENT_RANGE_HEADER.matcher(contentRange);
            if (matcher.find()) {
                try {
                    return new long[]{
                            Long.parseLong(matcher.group(1)),
                            Long.parseLong(matcher.group(2)),
                            Long.parseLong(matcher.group(3))
                    };
                } catch (NumberFormatException e) {
                }
            }
        }
        return null;
    }
}
