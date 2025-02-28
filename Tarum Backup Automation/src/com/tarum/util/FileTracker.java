package com.tarum.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileTracker implements Runnable {

    private String sourceDirectory = DEFAULT_DIRECTORY;

    private HashMap<Long, FileSignature> fileSignatureMap = new HashMap<>();
    private HashMap<String, Long> signatureKeyMap = new HashMap<>();

    private boolean running = true;

    private long elapsedTime=0;
    private long lastUpdatePerformed=0;
    private long initializationStartTime=0, initializationCompletionTime=0, startTime=0, pauseTime=0, stopTime=0;

    public static final String DEFAULT_DIRECTORY = System.getenv("APPDATA")+"/.Tarum Backup Automator/file_tracker/";

    public String getSourceDirectory() {
        return sourceDirectory;
    }
    public void setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }
    public HashMap<Long, FileSignature> getFileSignatureMap() {
        return fileSignatureMap;
    }
    public void setFileSignatureMap(HashMap<Long, FileSignature> fileSignatureMap) {
        this.fileSignatureMap = fileSignatureMap;
    }
    public HashMap<String, Long> getSignatureKeyMap() {
        return signatureKeyMap;
    }
    public void setSignatureKeyMap(HashMap<String, Long> signatureKeyMap) {
        this.signatureKeyMap = signatureKeyMap;
    }
    public boolean isRunning() {
        return running;
    }
    public void setRunning(boolean running) {
        this.running = running;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }
    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
    public long getLastUpdatePerformed() {
        return lastUpdatePerformed;
    }
    public void setLastUpdatePerformed(long lastUpdatePerformed) {
        this.lastUpdatePerformed = lastUpdatePerformed;
    }
    public long getInitializationStartTime() {
        return initializationStartTime;
    }
    public void setInitializationStartTime(long initializationStartTime) {
        this.initializationStartTime = initializationStartTime;
    }
    public long getInitializationCompletionTime() {
        return initializationCompletionTime;
    }
    public void setInitializationCompletionTime(long initializationCompletionTime) {
        this.initializationCompletionTime = initializationCompletionTime;
    }
    public long getStartTime() {
        return startTime;
    }
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    public long getPauseTime() {
        return pauseTime;
    }
    public void setPauseTime(long pauseTime) {
        this.pauseTime = pauseTime;
    }
    public long getStopTime() {
        return stopTime;
    }
    public void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }

    public FileTracker(){
        this(DEFAULT_DIRECTORY);
    }
    public FileTracker(String directory){
        this.sourceDirectory = directory == null ? DEFAULT_DIRECTORY : directory;

//        initialize();
    }

    public boolean containsSignature (long signatureUID){
        return fileSignatureMap.containsKey(signatureUID);
    }

    public String addSignature (FileSignature fileSignature){
        String result = fileSignature.getName();
        fileSignatureMap.put(fileSignature.getOriginalFileUID(), fileSignature);
        signatureKeyMap.put(fileSignature.getName(), fileSignature.getOriginalFileUID());
        return result;
    }

    public FileSignature getSignature (long fileSignatureUID){
        if (fileSignatureUID < 0 || !fileSignatureMap.containsKey(fileSignatureUID)) return null;
        return fileSignatureMap.get(fileSignatureUID);
    }

    public FileSignature generateFileSignature (File file){
        if (file == null || !file.exists()) return null;

        byte[] data = FileUtils.RetrieveFileData(file);
        long uid = MathUtils.GenerateUIDFromBytes(data);

        if (!containsSignature(uid)){
            FileSignature signature = new FileSignature(file, uid);
            String fileCopyStoragePath = generateFileCopyStoragePath (file);
            signature = new FileSignature(file, uid, fileCopyStoragePath);
            addSignature(signature);
            return signature;
        } else {
            return getSignature(uid);
        }
    }

    public static String generateFileCopyStoragePath (File targetFile){
        File dir = new File (DEFAULT_DIRECTORY+"files/");

        if (!dir.exists()) dir.mkdirs();

        String originalFileName = targetFile.getName();
        String fileCopyStoragePath = dir.getAbsolutePath() + originalFileName;

        return fileCopyStoragePath;
    }

    public List<FileSignature> performFileSignatureGeneration(String targetDirectory, boolean recursively){
        ArrayList<FileSignature> generatedSignatureList = new ArrayList<>();

        List<File> files = recursively ? FileUtils.GetAllFilesRecursively(targetDirectory) : FileUtils.GetAllFiles(targetDirectory);

        if (files.isEmpty()){
            return generatedSignatureList;
        }

        for (File file : files){
            if (!file.canRead()) continue;

            FileSignature signature = generateFileSignature(file);
            generatedSignatureList.add(signature);
        }

        return generatedSignatureList;
    }

    public void pause(){

        onPause();
    }

    public void stop(){

        onStop();
    }

    @Override
    public void run() {
        onInitializationStarted();

        initialize();

        onInitializationCompleted();

        onStart();

        long time = System.currentTimeMillis();
        long delta = 0;

        if (lastUpdatePerformed > 0) {
            delta = time - lastUpdatePerformed;
        }

        while (isRunning()) {
            update(delta);

            lastUpdatePerformed = System.currentTimeMillis();
        }
    }

    private void initialize(){
        File dir = new File (sourceDirectory);

        if (!dir.exists()) dir.mkdirs();
    }

    private void update (long delta){

    }

    public void onInitializationStarted(){
    }
    public void onInitializationCompleted(){
    }

    public void onStart(){

    }
    public void onPause(){
    }
    public void onStop(){
    }


}
