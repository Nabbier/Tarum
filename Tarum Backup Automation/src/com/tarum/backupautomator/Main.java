package com.tarum.backupautomator;

import com.tarum.backupautomator.content.Project;
import com.tarum.backupautomator.content.ProjectManager;
import com.tarum.util.FileTracker;
import com.tarum.util.Logger;

public class Main implements Runnable{

    private FileTracker fileTracker;
    private ProjectManager projectManager;
    private Project project;

    private Logger logger;

    private boolean running = true;

    private long elapsedTime=0, lastUpdateCycleInvocation=0;

    public boolean isRunning() {
        return running;
    }
    public void setRunning(boolean running) {
        this.running = running;
    }

    public Main(){

    }

    @Override
    public void run() {

        onInitializationStarted();
        initialize();
        onInitializationCompleted();

        onStart();

        update();
    }

    private void initialize(){
        this.logger = new Logger (System.getenv("APPDATA")+"/.Tarum Backup Automator/logs/");
        logger.start();

        logger.log("Initializing Tarum backup automator..");

        this.fileTracker = new FileTracker();
        this.projectManager = new ProjectManager(this);
        project = new Project (projectManager, "Tarum");

        logger.log ("Successfully completed initialization!");
    }

    private void update (){
        logger.log ("Starting project manager..");

        long time = System.currentTimeMillis();
        long delta = 0;

        /**
         * PERFORMS PROJECT BACKUP OPERATIONS EVERY HOUR
         */
        while (isRunning()){
            delta = (time - lastUpdateCycleInvocation);

            this.elapsedTime += delta;

            projectManager.update(delta);

            this.lastUpdateCycleInvocation = System.currentTimeMillis();
        }

    }

    private void onInitializationStarted(){
    }
    private void onInitializationCompleted(){
    }
    private void onStart(){
    }
    private void onPause(){
    }
    private void onStop(){
    }

    public static void main(String[] args) {
        Main main = new Main();
        Thread t = new Thread(main);
        t.start();
    }

}