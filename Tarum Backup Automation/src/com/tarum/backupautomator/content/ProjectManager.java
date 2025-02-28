package com.tarum.backupautomator.content;

import com.tarum.backupautomator.Main;
import com.tarum.util.FileTracker;
import com.tarum.util.FileUtils;
import com.tarum.util.GlueList;
import com.tarum.util.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class ProjectManager {

    private Main main;
    private Logger logger;

    private String sourceDirectory = DEFAULT_PROJECTS_SOURCE_DIRECTORY;

    private HashMap<String, Project> projectMap = new HashMap<>();

    private int projectBackupOperationsPerformed = 0;

    private long elapsedTime;
    private long lastProjectBackupTime;

    private long projectBackupRate = DEFAULT_PROJECT_BACKUP_RATE;

    public static final long SECOND = 1000;
    public static final long MINUTE = SECOND * 60;
    public static final long HOUR = MINUTE * 60;
    public static final long HALF_HOUR = HOUR / 2;

    public static final long DEFAULT_PROJECT_BACKUP_RATE = (1 * HOUR);

    public static final String DEFAULT_PROJECTS_SOURCE_DIRECTORY = System.getProperty("user.home")+"/IdeaProjects/";

    public ProjectManager(Main main) {
        this(main, null);
    }
    public ProjectManager(Main main, String projectsSourceDirectory) {
        this.main = main;
        this.sourceDirectory = projectsSourceDirectory == null ? DEFAULT_PROJECTS_SOURCE_DIRECTORY : projectsSourceDirectory;

        init();
    }

    private void init(){
        this.logger = new Logger (System.getenv("APPDATA")+"/.Tarum Backup Automator/logs/");
        logger.start();

    }

    public static String getDesktopPath(){
        String os = System.getProperty("os.name").toLowerCase();
        Path desktopPath;

        if (os.contains("win")) {
            desktopPath = Paths.get(System.getProperty("user.home"), "Desktop");
        } else if (os.contains("mac") || os.contains("darwin")) {
            desktopPath = Paths.get(System.getProperty("user.home"), "Desktop");
        } else if (os.contains("nix") || os.contains("nux")) {
            desktopPath = Paths.get(System.getProperty("user.home"), "Desktop");
        } else {
            desktopPath = Paths.get(System.getProperty("user.home"));
            System.err.println("Unknown OS, defaulting to user home directory.");
        }

        return desktopPath.toString()+"/";
    }

    public void addProject (Project project){
        if (project == null) return;

        projectMap.put(project.getSourceDirectory(), project);
    }

    public void update(long delta){
        this.elapsedTime += delta;

        if (elapsedTime >= projectBackupRate){
            performProjectBackup();

            onProjectBackupCompleted();

            lastProjectBackupTime = System.currentTimeMillis();
            elapsedTime = 0;
        }
    }

    public void performProjectBackup(){
        retrieveProjectInformation();

        generateBackups();

        projectBackupOperationsPerformed++;
    }

    private void onProjectBackupCompleted(){
        log ("Successfully completed project backup (Backup #" + projectBackupOperationsPerformed + ")!");

        // TODO: PERFORM UPLOAD TO WEBSERVER,
        // TODO: SYNCHRONIZE GITHUB REPOSITORIES (IF ANY)
    }

    public void retrieveProjectInformation(){
        log ("Generating projects (IDE source directory: " + sourceDirectory + ")...");
        /**
        * SCAN THE IDE PROJECT SOURCE DIRECTORY FOR PROJECT DIRECTORIES
         */
        List<File> fileList = FileUtils.GetAllDirectories(sourceDirectory);

        log ("Located (" + fileList.size() + ") project directories!");

        for (File projectDirectory : fileList){
            if (projectMap.containsKey(projectDirectory.getAbsolutePath())){
                log ("Scanning project file(s) for changes" +
                        " (directory_path:" + projectDirectory.getAbsolutePath() + "..");
                Project project = projectMap.get(projectDirectory.getAbsolutePath());

                List<File> projectSourceFiles = FileUtils.GetAllFilesRecursively(projectDirectory.getAbsolutePath());
                int projectFileCount = 0;

                for (File projectFile : projectSourceFiles){
                    if (!project.containsFile(projectFile)){
                        project.addFile(projectFile);
                        projectFileCount++;
                    }
                }

                log ("Successfully retrieved (" + projectFileCount + ") project files for project " +
                        "(project_directory: " + projectDirectory.getAbsolutePath() + ")!");
            } else {
                log ("Generating project information (directory_path:" + projectDirectory.getAbsolutePath() + "..");

                Project project = new Project (this, projectDirectory.getName(), projectDirectory.getAbsolutePath());
                addProject(project);
            }
        }
    }

    public void generateBackups(){
        log ("Generating backup file(s) for projects...");

        Iterator i = projectMap.entrySet().iterator();

        while (i.hasNext()){
            Map.Entry<String, Project> entry = (Map.Entry<String, Project>) i.next();

            String path = entry.getKey();
            Project project = entry.getValue();

            log ("Backing up files for project (path:" + path + ", project_name: " + project.getName() + ")...");

            String latestProjectBackupDirectoryPath = generateLatestBackupPath(project);
            File dir = new File (latestProjectBackupDirectoryPath);

            if (!dir.exists()){
                dir.mkdirs();
            }

            log ("Successfully generated backup directory path for project (path: " + latestProjectBackupDirectoryPath + ")!");

            /**
             * RETRIEVE A LIST OF ALL OF THE FILES (RECURSIVELY) IN THE PROJECT'S SOURCE DIRECTORY
             */
            List<File> projectFileList = retrieveLatestProjectFiles(project);
            int fileCopyCount = 0;

            /**
             * ITERATE THROUGH THE LIST OF RETRIEVED PROJECT FILES AND UPDATE THE PROJECT'S FILE INFORMATION IN MEMORY,
             * THEN GENERATE THE NECESSARY BACKUP PATHS FOR EACH OF THE FILES
             */
            for (File projectFile : projectFileList){

                /**
                 * DETERMINE THE PATH OF THE TARGET PROJECT FILE ON THE FILESYSTEM, THEN GENERATE A BACKUP
                 * FILE PATH FOR THE TARGET FILE SO THAT IT MAY BE COPIED OVER TO THE BACKUP DIRECTORY
                 * PROPERLY
                 */
                String subDirectoryPath = projectFile.getAbsolutePath().replace(project.getSourceDirectory(), "");
                String generatedSubDirectoryBackupPath = (latestProjectBackupDirectoryPath + subDirectoryPath).replace(projectFile.getName(), "");
                dir  = new File (latestProjectBackupDirectoryPath + subDirectoryPath);
                if (!dir.exists()){
                    dir.mkdirs();
                }

                /**
                 * ENSURE THE TARGET FILE INFORMATION IS STORED IN THE PROJECT INSTANCE WE'RE HANDLING CURRENTLY
                 */
                if (!project.containsFile (projectFile)){
                    project.addFile(projectFile);
                }

                /**
                 * CHECK IF THE TARGET FILE IS A DIRECTORY TYPE (JUST AS A PRECAUTION, WITH THE METHOD USED FOR FILE
                 * RETRIEVAL IT SHOULD ONLY RETRIEVE FILE TYPES)
                 */
                if (projectFile.isDirectory()) continue;

                System.out.println("generatedSubDirectoryBackupPath: " + generatedSubDirectoryBackupPath);

                if (FileUtils.CopyFile(projectFile, generatedSubDirectoryBackupPath)){
                    log ("Successfully copied project file to backup directory (original_file: " +
                            projectFile.getAbsolutePath() + ", backup_path: " + latestProjectBackupDirectoryPath + ")!");
                    fileCopyCount++;
                } else {
                    logError ("Failed to copy project file to backup directory (original_file: " +
                            projectFile.getAbsolutePath() + ", backup_path: " + latestProjectBackupDirectoryPath + ")!");
                }
            }

            log ("Successfully copied (" + fileCopyCount + ") files to the backup directory " +
                    "(path: " + latestProjectBackupDirectoryPath + ")!");
        }
    }

    private String generateLatestBackupPath (Project project){
        String backupsDirectory = project.getBackupDirectory();
        String result = null;

        File dir = new File (backupsDirectory);

        if (!dir.exists()) dir.mkdirs();

        File[] files = dir.listFiles();
        int fileCount = files == null ? 0 : files.length;

        String dateTime = getDateTimestamp();
        String projectName = project.getName();

        if (projectName == null){
            // TODO: THIS SHOULD NEVER BE NULL
        }

        String directoryName = project.getName() + " " + dateTime;
        backupsDirectory += directoryName + "/";
        return backupsDirectory;
    }

    public List<File> retrieveLatestProjectFiles(Project project){
        log ("Project source dir: " + project.getSourceDirectory());
        List<File> fileList = FileUtils.GetAllFilesRecursively(project.getSourceDirectory());
        return fileList;
    }

    public String getDateTimestamp(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String result = formatter.format(date);
        return result.replaceAll("/", "-").replaceAll(":", "_");
    }

    public void log (String logEntryText) {
        logger.logLine("ProjectManager: " + logEntryText);
    }
    public void logError (String logEntryText){
        logger.logError("ProjectManager: " + logEntryText);
    }

}
