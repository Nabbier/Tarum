import com.tarum.app.Application;
import com.tarum.io.content.ContentManager;
import com.tarum.io.content.type.Configuration;
import com.tarum.io.content.type.ContentContainer;
import com.tarum.io.content.type.ContentMap;
import com.tarum.io.content.type.ContentMapImpl;

import java.io.File;

public class Main extends Application {

    private String[] args;

    public Main(String[] args){
        this.args = args;
    }

    @Override
    public void onInitializationCompleted(){
        // TODO: TEST THE CONFIGURATION I/O FUNCTIONALITY
        Configuration config = getConfiguration();

        ContentManager cm = getContentManager();

        ContentMap contentMap = new ContentMapImpl(cm, "Test");

        String testObj = "TestObj";
        long testKey = contentMap.add(testObj);
        String testObj2 = "TestObj2";
        long testKey2 = contentMap.add(testObj2);

        boolean contentExportationResult = cm.exportContentContainer(contentMap);

        if (contentExportationResult){
            log ("Successfully exported content container to the local filesystem (file_path:" + contentMap.getFile().getAbsolutePath()
        + "!");
        } else {
            logError("Failed to export content container to the local filesystem (file_path:" + contentMap.getFile().getAbsolutePath()
            + "!");
        }

        File containerFile = contentMap.getFile();
//        ContentMap loadedContentMap = (ContentMap) cm.loadContentContainerFile(containerFile);
//
//        if (loadedContentMap != null){
//        }


    }

    @Override
    public void update (long delta){

    }

    public void log (String logEntryText){
        getLogger().logLine("Main: " + logEntryText);
    }
    public void logError (String logEntryText){
        getLogger().logError("Main: " + logEntryText);
    }

    public static void main(String[] args) {
        Main main = new Main(args);

        Thread t = new Thread (main);
        t.start();
    }

}