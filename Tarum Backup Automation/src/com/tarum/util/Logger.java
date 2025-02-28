package com.tarum.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

    private String directory = DEFAULT_DIRECTORY;
    private String fileName = DEFAULT_FILE_NAME;
    private String fileExtension = DEFAULT_FILE_EXTENSION;
    private File file;

    private boolean printToConsole = true;

    private BufferedWriter writer;

    public static final String DEFAULT_DIRECTORY = System.getenv("APPDATA")+"/.Tarum/logs/";
    public static final String DEFAULT_FILE_NAME = "Log";
    public static final String DEFAULT_FILE_EXTENSION = ".txt";

    public static enum EntryFlag{
        NONE, WARNING, ERROR
    }

    public String getDirectory() {
        return directory;
    }
    public void setDirectory(String directory) {
        if (directory == null) return;

        this.directory = directory;

        initialize();
    }
    public String getFileName(){
        return this.fileName;
    }
    public void setFileName (String fileName){
        this.fileName = fileName;
    }
    public String getFileExtension(){
        return this.fileExtension;
    }
    public void setFileExtension (String fileExtension){
        if (fileExtension == null) return;
        if (!fileExtension.startsWith(".")){
            fileExtension = "." + fileExtension;
        }
        this.fileExtension = fileExtension;
    }
    public File getFile() {
        return this.file;
    }
    public void setFile(File file) {
        if (file == null) return;
        if (getFile() != null && getFile().getAbsolutePath().equalsIgnoreCase(file.getAbsolutePath())) return;

        this.file = file;
        this.fileName = file.getName().split(".")[0];
        this.fileExtension = file.getName().split(".")[1];

        initialize();
    }
    public boolean isPrintToConsoleEnabled() {
        return printToConsole;
    }
    public void setPrintToConsole(boolean printToConsole) {
        this.printToConsole = printToConsole;
    }
    public void enablePrintToConsole() {
        this.printToConsole = true;
    }
    public void disablePrintToConsole() {
        this.printToConsole = false;
    }
    public BufferedWriter getWriter() {
        return writer;
    }
    public void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    public Logger(){
        this(DEFAULT_DIRECTORY);
    }
    public Logger(String directory){
        this.directory = directory == null ? DEFAULT_DIRECTORY : directory;
    }

    public void start(){
        initialize();
    }

    private void initialize(){
        File dir = new File (getDirectory());

        if (!dir.exists()){
            dir.mkdirs();
        }

        this.file = generateOutputFile();

        try{
            if (writer != null){
                writer.flush();
                writer.close();
            }

            this.writer = new BufferedWriter(new FileWriter(getFile()));
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    private File generateOutputFile(){
        if (getDirectory() == null){
            this.directory = DEFAULT_DIRECTORY;
        }

        File dir = new File (getDirectory());

        if (!dir.exists()){
            dir.mkdirs();
        }

        int fileCount = dir.listFiles().length;

        String preparedFileName = getFileName();

        if (fileCount > 0){
            preparedFileName += "_" + fileCount;
        }

        preparedFileName += DEFAULT_FILE_EXTENSION;

        return new File (getDirectory(), preparedFileName);
    }

    public boolean log (String logEntry){
        try{
            if (writer == null || logEntry == null) return false;

            if (printToConsole) System.out.println(logEntry);

            String entry = logEntry;

            writer.write(entry);

            writer.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
        return true;
    }
    public boolean logLine (String logEntry){
        return logLine(logEntry, EntryFlag.NONE);
    }
    public boolean logLine (String logEntry, EntryFlag entryFlag){
        try{
            if (writer == null) return false;

            String entry = null;

            if (entryFlag != null && entryFlag != EntryFlag.NONE){
                entry = "[FLAG_" + entryFlag.name() + "]: ";
            }

            if (entry == null){
                entry = logEntry + "\n";
            } else {
                entry += logEntry + "\n";
            }

            if (printToConsole){
                System.out.println(entry);
            }
            writer.write(entry);
            writer.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
        return true;
    }
    public boolean logWarning (String logEntry){
        return logLine (logEntry, EntryFlag.WARNING);
    }
    public boolean logError (String logEntry){
        return logLine (logEntry, EntryFlag.ERROR);
    }

    public void close(){
        try{
            if (writer != null){
                writer.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
