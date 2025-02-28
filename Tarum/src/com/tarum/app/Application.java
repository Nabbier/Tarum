package com.tarum.app;

import com.tarum.io.content.ContentManager;
import com.tarum.io.content.type.Configuration;
import com.tarum.util.Logger;

import java.io.File;

public abstract class Application implements Runnable{

    private Configuration configuration;
    private ContentManager contentManager;
    private Logger logger;

    private String[] args;

    private boolean running;

    public static final String DEFAULT_APPLICATION_NAME = "Tarum";
    public static final String DEFAULT_DIRECTORY = System.getenv("APPDATA")+"/."+DEFAULT_APPLICATION_NAME + "/";

    public static final String getDefaultDirectory(){
        return DEFAULT_DIRECTORY;
    }

    public static ContentManager getDefaultContentManager(){
        return new ContentManager();
    }

    public Configuration getConfiguration() {
        return configuration;
    }
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
    public ContentManager getContentManager() {
        return contentManager;
    }
    public void setContentManager(ContentManager contentManager) {
        this.contentManager = contentManager;
    }
    public Logger getLogger() {
        return logger;
    }
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public String[] getArgs() {
        return args;
    }
    public void setArgs(String[] args) {
        this.args = args;
    }
    public boolean isRunning() {
        return running;
    }
    public void setRunning(boolean running) {
        this.running = running;
    }

    public Application(){
        this(null);
    }
    public Application (String[] args){
        this.args = args;
    }

    @Override
    public void run() {
        onInitializationStarted();

        initialize();

        onInitializationCompleted();

        onStart();

        long currentTime=0, lastUpdateCall=0;
        long delta;

        /*while (isRunning()){
            currentTime = System.currentTimeMillis();
            delta = lastUpdateCall > 0 ? currentTime - lastUpdateCall : 0;

            update(delta);
        }*/
    }

    public void initialize(){
        this.logger = new Logger();
        logger.start();

        logger.logLine("Initializing application..");

        onInitializationStarted();

        contentManager = new ContentManager();

        File configFile = new File(System.getenv("APPDATA")+"/.Tarum/application.cfg");
        this.configuration = new Configuration(configFile);

        if (configuration.load()){
            logger.logLine("Successfully loaded application configuration settings! " +
                    "(file_path: " + configFile.getAbsolutePath() + ")");
        } else {
            this.configuration = getDefaultApplicationConfiguration();
            configuration.export();
        }

        this.running = true;
    }

    public void update (long delta){
    }

    /**
     * TODO: CREATE A DEFAULT SET OF CONFIGURATION SETTINGS FOR THE APPLICATION
     */
    public static Configuration getDefaultApplicationConfiguration(){
        File file = new File (System.getenv("APPDATA")+"/." + DEFAULT_APPLICATION_NAME + "/", "application.cfg");
        Configuration config = new Configuration(file);
//        config.setDataType(String.class);

//        config.set("Test_field", "Test_Value");

        return config;
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
    public void onDispositionStarted(){
    }

}
