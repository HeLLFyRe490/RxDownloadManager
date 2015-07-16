package com.norddev.downloadmanager.cache;

import com.norddev.downloadmanager.cache.api.Cache;
import com.norddev.downloadmanager.common.Util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class DirectoryCache implements Cache {

    private static final Pattern CACHE_FILE_PATTERM = Pattern.compile("^(.+)\\.(\\d+)$");
    private final File mDirectory;

    /**
     * @param directory
     */
    public DirectoryCache(File directory) {
        mDirectory = directory;
    }

    @Override
    public CacheFile createFile(String key, long fileSizeBytes){
        File file = new File(mDirectory, generateFileName(key, fileSizeBytes));
        return new CacheFile(key, file, fileSizeBytes);
    }

    private static String generateFileName(String key, long fileSizeBytes){
        return key + "." + fileSizeBytes;
    }

    private static CacheFile loadCacheFile(File file){
        Matcher matcher = CACHE_FILE_PATTERM.matcher(file.getName());
        if(matcher.find()){
            return new CacheFile(matcher.group(1), file, Long.parseLong(matcher.group(2)));
        }
        return null;
    }

    @Override
    public CacheFile getFile(final String key) {
        File[] files = mDirectory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                Matcher matcher = CACHE_FILE_PATTERM.matcher(filename);
                return matcher.find() && key.equals(matcher.group(1));
            }
        });
        return files[0] != null ? loadCacheFile(files[0]) : null;
    }

    @Override
    public List<CacheFile> getFiles() {
        List<CacheFile> cacheFiles = new LinkedList<>();
        for(File file : mDirectory.listFiles()){
            CacheFile cacheFile = loadCacheFile(file);
            if(cacheFile != null){
                cacheFiles.add(cacheFile);
            }
        }
        return cacheFiles;
    }

    @Override
    public void clear() {
        Util.deleteDirectoryContents(mDirectory);
    }
}
