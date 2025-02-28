package com.tarum.io.content.type;

import com.tarum.io.content.ContentManager;

import java.io.File;
import java.io.Serializable;

public abstract class ContentContainer extends BasicContentContainer implements Serializable {

    private transient ContentFormat contentFormat;

    public ContentContainer (ContentManager contentManager){
        this(contentManager, "ContentContainer");
    }
    public ContentContainer (ContentManager contentManager, File file){
        super (contentManager, file);
    }
    public ContentContainer (ContentManager contentManager, String name){
        super(contentManager, name);
    }



}
