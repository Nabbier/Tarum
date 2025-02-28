package com.tarum.backupautomator.content;

import com.tarum.util.FileUtils;

import java.io.File;

public class EntryFile {

    private Project parentProject;

    private String fileName;
    private String fileExtension;
    private String filePath;
    private File file;

    public EntryFile(Project parentProject){
        this(parentProject, null);
    }
    public EntryFile (Project parentProject, File file){
        this.parentProject = parentProject;
        setFile(file);
    }

    public boolean setFile (File file){
        if (file == null) return false;
        this.file = file;
        this.filePath = file.getAbsolutePath();
        this.fileName = FileUtils.getFileName(file);
        this.fileExtension = FileUtils.getFileExtension(file);
        return true;
    }

}
