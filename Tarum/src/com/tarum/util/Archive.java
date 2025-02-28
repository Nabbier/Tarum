package com.tarum.util;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Archive {

    private String filePath;
    private transient File file;
    private transient JarFile jarFile;

    private transient GlueList<JarEntry> entryList;

    public Archive(){
        this(null);
    }
    public Archive (File file){
        this.file = file;

        if (file != null) {
            this.filePath = file.getAbsolutePath();
        }
    }

    /**
     * TODO: COMPLETE LOADING OF JAR FILE INTO THE ARCHIVE
     * @param targetFile
     * @return
     */
    public GlueList<JarEntry> loadJarFile (File targetFile){
        entryList = new GlueList<>();
        if (targetFile == null || !targetFile.exists()) return entryList;

        try {
            jarFile = new JarFile(targetFile);

            Enumeration<JarEntry> entries = jarFile.entries();
            Iterator i = entries.asIterator();

            while (i.hasNext()){
                JarEntry entry = (JarEntry) i.next();

                entryList.add(entry);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return entryList;
    }

}