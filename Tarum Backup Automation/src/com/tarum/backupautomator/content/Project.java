package com.tarum.backupautomator.content;

import com.tarum.util.FileTracker;
import com.tarum.util.GlueList;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Project {

    private ProjectManager projectManager;
    private String name;
    private String sourceDirectory;
    private String backupDirectory;
    private FileTracker fileTracker;

    private GlueList<File> fileList = new GlueList<>();

    private HashMap<String, EntryFile> entryFileMap = new HashMap<>();

    private long lastBackupTime;

    public ProjectManager getProjectManager() {
        return projectManager;
    }
    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }
    public String getName(){
        return this.name;
    }
    public void setName (String name){
        this.name = name;
    }
    public String getSourceDirectory() {
        return sourceDirectory;
    }
    public void setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }
    public String getBackupDirectory() {
        return backupDirectory;
    }
    public void setBackupDirectory(String backupDirectory) {
        this.backupDirectory = backupDirectory;
    }
    public GlueList<File> getFileList() {
        return fileList;
    }
    public void setFileList(GlueList<File> fileList) {
        this.fileList = fileList;
    }
    public HashMap<String, EntryFile> getEntryFileMap() {
        return entryFileMap;
    }
    public void setEntryFileMap(HashMap<String, EntryFile> entryFileMap) {
        this.entryFileMap = entryFileMap;
    }
    public long getLastBackupTime() {
        return lastBackupTime;
    }
    public void setLastBackupTime(long lastBackupTime) {
        this.lastBackupTime = lastBackupTime;
    }

    public Project (ProjectManager projectManager){
        this (projectManager, "Default Project", System.getenv("user.home")+"/IdeaProjects/");
    }
    public Project (ProjectManager projectManager, String name){
        this(projectManager, name, null);
    }
    public Project (ProjectManager projectManager, String name, String sourceDirectory){
        this.projectManager = projectManager;
        this.name = name;
        this.sourceDirectory = sourceDirectory == null ? System.getProperty("user.home") +"/IdeaProjects/" : sourceDirectory;
        this.backupDirectory = ProjectManager.getDesktopPath()+"Project backups/" + name + "/";

        init();
    }

    private void init(){

    }

    public boolean containsFile (File file){
        if (file == null) return false;
        if (fileList.isEmpty()) return false;
        return fileList.contains(file);
    }

    public void addFile (File file){
        if (file == null) return;
        if (!fileList.contains(file)) {
            this.fileList.add(file);
        }
    }

    public boolean removeFile (File file){
        if (file == null || !fileList.contains(file)) return false;

        this.fileList.remove(file);
        return true;
    }

}
