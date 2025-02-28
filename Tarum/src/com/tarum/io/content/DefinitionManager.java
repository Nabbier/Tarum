package com.tarum.io.content;
import com.tarum.app.Application;
import com.tarum.io.content.type.Definition;

import java.io.File;

public class DefinitionManager {

    private transient ContentManager contentManager;

    private String directory;

    public ContentManager getContentManager() {
        return contentManager;
    }
    public void setContentManager(ContentManager contentManager) {
        this.contentManager = contentManager;
    }
    public String getDirectory() {
        return directory;
    }
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public DefinitionManager (ContentManager contentManager){
        this(contentManager, null);
    }
    public DefinitionManager (ContentManager contentManager, String directory){
        this.contentManager = contentManager == null ? Application.getDefaultContentManager() : contentManager;
        this.directory = directory == null ? contentManager.getContentDirectory() + "definitions/" : directory;

        init();
    }

    private void init(){
        // TODO: RETRIEVE THE ESSENTIAL INFORMATION FOR OUR DEFINITION FILES WITHOUT LOADING THEM INTO MEMORY,
        //  SUCH AS UIDS, NAMES, AND FILE PATHS
    }

    public Definition loadDefinitionFile (File file){

        return null;
    }

}
