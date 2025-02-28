import com.tarum.app.Application;
import com.tarum.io.content.ContentManager;
import com.tarum.io.content.type.Configuration;
import com.tarum.io.content.type.ContentContainer;

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
        ContentContainer container = cm.createContentContainer("Test_Container");
        boolean contentExportationResult = cm.exportContentContainer(container);

        if (contentExportationResult){
            log ("Successfully exported content container to the local filesystem (file_path:" + container.getFile().getAbsolutePath()
        + "!");
        } else {
            logError("Failed to export content container to the local filesystem (file_path:" + container.getFile().getAbsolutePath()
            + "!");
        }
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