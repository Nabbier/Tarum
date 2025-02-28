package com.tarum.util;

import java.io.File;
import java.io.Serializable;

public class FileSignature implements Serializable {

    private long originalFileUID;
    private String name;

    private String originalFilePath;

    /**
     * The absolute file path of the copy of the file associated with the FileSignature
     */
    private String fileCopyStoragePath;

    /**
     * This is required if you wish to X,Y,Z
     */
    private boolean copyFileData = true;

    private byte[] originalFileContent;

    public long getOriginalFileUID() {
        return originalFileUID;
    }
    public void setOriginalFileUID(long originalFileUID) {
        this.originalFileUID = originalFileUID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getOriginalFilePath() {
        return originalFilePath;
    }
    public void setOriginalFilePath(String originalFilePath) {
        this.originalFilePath = originalFilePath;
    }
    public String getFileCopyStoragePath(){
        return this.fileCopyStoragePath;
    }
    public void setFileCopyStoragePath (String filePath){
        fileCopyStoragePath = filePath;
    }
    public boolean isFileDataCopyingEnabled(){
        return this.copyFileData;
    }
    public void setFileDataCopyingEnabled (boolean enabled){
        this.copyFileData = enabled;
    }
    public byte[] getFileContent(){
        return this.originalFileContent;
    }
    public void setFileContent (byte[] fileContent){
        this.originalFileContent = fileContent;
    }

    public FileSignature(File originalFile, long fileUID){
        this (originalFile, fileUID, null, null);
    }
    public FileSignature (File originalFile, long fileUID, byte[] fileContent){
        this (originalFile, fileUID, fileContent, null);
    }
    public FileSignature(File originalFile, long fileUID, String fileCopyStoragePath) {
        this(originalFile, fileUID, null, fileCopyStoragePath);
    }
    public FileSignature(File originalFile, long fileUID, byte[] fileContent, String fileCopyStoragePath) {
        this.originalFilePath = originalFile.getAbsolutePath();
        this.originalFileUID = fileUID;
        this.originalFileContent = fileContent;
        this.fileCopyStoragePath = fileCopyStoragePath;
    }

}
