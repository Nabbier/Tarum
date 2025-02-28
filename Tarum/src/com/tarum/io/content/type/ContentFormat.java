package com.tarum.io.content.type;

import com.tarum.app.Application;
import com.tarum.io.content.ContentManager;

import java.io.File;
import java.io.Serializable;

public class ContentFormat implements Serializable {

    private transient ContentManager contentManager;
    private long uid;
    private String name;

    private String filePath;
    private transient File file;

    public static enum Type{
        XML_DOCUMENT, UML_DOCUMENT
    }

    public ContentFormat (){
        this("ContentFormat");
    }
    public ContentFormat (File file){
        this(null, file);
    }
    public ContentFormat (String name){
        this.name = name;
    }
    public ContentFormat (ContentManager contentManager){
        this(contentManager, "ContentFormat");
    }
    public ContentFormat (ContentManager contentManager, String name){
        this.contentManager = contentManager == null ? Application.getDefaultContentManager() : contentManager;
        this.name = name == null ? "ContentFormat" : name;
    }
    public ContentFormat (ContentManager contentManager, File file){
        this.contentManager = contentManager == null ? Application.getDefaultContentManager() : contentManager;
        this.file = file;
        this.filePath = file.getAbsolutePath();
    }

}